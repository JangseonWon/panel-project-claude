package com.greencross.lims.worker;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gcgenome.lims.test.HasGene;
import com.gcgenome.lims.test.HasGenes;
import com.google.common.collect.ImmutableMap;
import com.greencross.lims.dao.PanelTypeService;
import com.greencross.lims.dao.RequestDAO;
import com.greencross.lims.dao.SnvCandidateDAO;
import com.greencross.lims.dao.SnvConsensualClassDao;
import com.greencross.lims.entity.*;
import com.greencross.lims.variant.data.AnnotationFileToMap;
import com.greencross.lims.variant.data.IdGenerator;
import com.greencross.lims.variant.data.MapToIndexQuery;
import com.greencross.lims.webhook.VariantCountMap;
import com.greencross.lims.webhook.VariantCountRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQuery;

import javax.annotation.Nullable;
import java.io.File;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class SnvWorkerImpl implements Worker {
	public static Map<String, String> CONSENSUAL_CLASSES = new HashMap<>();
	private final ElasticsearchRestTemplate em;
	private final RequestDAO requestDAO;
	private final SnvCandidateDAO candidateDao;
	private final SnvConsensualClassDao consensualDao;
	private final ObjectMapper om;
	private final Process processor;
	@Autowired
	private PanelTypeService panelTypeService;
	@Autowired
	private VariantCountMap variantCountMap;
	protected SnvWorkerImpl(ElasticsearchRestTemplate em, RequestDAO requestDAO, SnvCandidateDAO candidateDao, SnvConsensualClassDao consensualDao, ObjectMapper om, Process processor) {
		this.em = em;
		this.requestDAO = requestDAO;
		this.candidateDao = candidateDao;
		this.consensualDao = consensualDao;
		this.om = om;
		this.processor = processor;
	}
	private static final String FIND_REQUEST_BY_SAMPLE = "SELECT * FROM request WHERE " +
			"sample IN (SELECT id FROM sample where patient=(SELECT patient FROM sample WHERE id=:sample)) " +
			"AND date_request >= (SELECT max(date_request)+'-1 week' FROM request WHERE sample=:sample) " +
			"AND date_request <= (SELECT max(date_request)+'1 week' FROM request WHERE sample=:sample)";
	private List<Analysis> findEntity(String fileName) {
		var sample = processor.sample(fileName);
		var panel = processor.panel(fileName);
		String batchPanel = processor.batch(fileName);
		batchPanel = batchPanel.substring(2, batchPanel.length()-3);
		if(sample == null) {
			var entity = processor.map(fileName,null);
			entity = requestDAO.em().merge(entity);
			return List.of(entity);
		} else {
			var targets = Arrays.stream(services()).collect(Collectors.toSet());
			List<Request> req = requestDAO.em().createNativeQuery(FIND_REQUEST_BY_SAMPLE, Request.class).setParameter("sample", sample).getResultList();

			Set<String> panelServices = panelTypeService.getPanelType(batchPanel).stream()
					.map(PanelType::service)
					.collect(Collectors.toSet());
			return req.stream()
					.filter(r->targets.contains(r.pk().service()))
					.filter(r->panelServices.contains(r.pk().service()))
					.map(r->processor.map(fileName, r))
					.map(a->{
						System.out.println("Analysis:" + a);
						Analysis prev = requestDAO.em().find(Analysis.class, a.pk());
						System.out.println("Prev:" + prev);
						if(prev!=null) return prev;
						else return requestDAO.em().merge(a);
					}).collect(Collectors.toList());
		}
	}
	@Override
	public final String batch(File file) {
		return processor.batch(file.getName());
	}
	@Override
	public final void process(AnalysisFile entity, File file) throws Exception {
		List<Analysis> targets = findEntity(entity.name());
		System.out.println("Target:" + targets);
		for(Analysis target: targets) {
			String batch = target.pk().batch();
			String indexName = ("analysis-snv-" + batch).toLowerCase();
			if(!em.indexOps(IndexCoordinates.of(indexName)).exists()) createIndex(batch);
			boolean vus = false;
			HasGenes test = TestUtil.findTest(target.service());
			if(test instanceof HasGene) vus = true;
			Set<String> genes = test!=null?Set.of(test.genes()):new HashSet<>();
			String analysisId = IdGenerator.analysisId(target);
			Map<String, Map<String, String>> annotations = AnnotationFileToMap.merge(Files.lines(file.toPath()).iterator());
			List<IndexQuery> bulks = new LinkedList<>();
			for (Map.Entry<String, Map<String, String>> entry : annotations.entrySet()) {
				String id = analysisId + ":" + entry.getKey();
				Map<String, String> row = em.get(id, Map.class, IndexCoordinates.of(indexName));
				if (row == null) {
					row = entry.getValue();
					row.put("analysis", analysisId);
					row.put("snv", entry.getKey());
				} else for(String key: entry.getValue().keySet()) {
					if(!row.containsKey(key)) row.put(key, entry.getValue().get(key));
				}
				Iterator<Map.Entry<String, String>> iter = row.entrySet().iterator();
				while(iter.hasNext()) {
					Map.Entry<String, String> e = iter.next();
					String value = e.getValue();
					if(value==null || value.isBlank()) iter.remove();
				}
				appendTag(row, processor.tags(row));
				bulks.add(MapToIndexQuery.map(id, row, om));
				if(bulks.size() > 1000) {
					em.bulkIndex(bulks, IndexCoordinates.of(indexName));
					bulks.clear();
				}
				requestDAO.em().merge(target);

				String gene = entry.getValue().get("gene.refgene");
				if(gene!=null && genes.contains(gene.trim())) if(CONSENSUAL_CLASSES.containsKey(entry.getKey())) {
					var candidate = new SnvCandidate();
					String clazz = CONSENSUAL_CLASSES.get(entry.getKey());
					if(!"VUS".equalsIgnoreCase(clazz) || vus) {
						candidate.pk(new SnvCandidate.SnvPK().sample(target.sample()).service(target.service()).snv(id)).classification(clazz);
						candidateDao.merge(candidate);
						System.out.println(id + ": Consensual Class " + entry.getValue() + " assigned to " + target.sample() + "/" + target.service());
					}
				}
			}
			if (!bulks.isEmpty()) em.bulkIndex(bulks, IndexCoordinates.of(indexName));
			addToCountQueue(targets, indexName);
		}
	}
	private void addToCountQueue(List<Analysis> targets, String index){
		targets.forEach(target -> {
			String sample = target.sample().toString();
			String formatted = sample.substring(0, 8) + "-" + sample.substring(8, 11) + "-" + sample.substring(11);
			String batchRow = target.pk().batch() + ":" + String.format("%03d", target.pk().row());
			variantCountMap.addToMap(target.serial(), new VariantCountRequest(formatted, target.service(), batchRow, index));
		});

	}
	private void appendTag(Map<String, String> map, List<String> tags) throws JsonProcessingException {
		if(!map.containsKey("tags")) map.put("tags", om.writeValueAsString(tags));
		else {
			String[] previous = om.readValue(map.get("tags"), String[].class);
			map.put("tags", om.writeValueAsString(Stream.concat(Arrays.stream(previous), tags.stream()).distinct().collect(Collectors.toList())));
		}
	}
	protected abstract String[] services();
	public interface Process {
		Long sample(String fileName);
		default String panel(String fileName) { return ""; }
		String batch(String fileName);
		Analysis map(String fileName, @Nullable Request request);
		List<String> tags(Map<String, String> values);
	}

	private final static Map<String, Object> ANALYSIS_MAPPING;
	private final static Map<String, Object> ANALYSIS_SETTING;
	private final static Map<String, String> KEYWORD = ImmutableMap.of("type", "keyword");
	private final static Map<String, Object> KEYWORD_GLOBAL = ImmutableMap.of("type", "keyword", "eager_global_ordinals", true);
	private final static Map<String, String> DATE = ImmutableMap.of("type", "date");
	private final static Map<String, String> BOOLEAN = ImmutableMap.of("type", "boolean");
	private final static Map<String, String> INTEGER = ImmutableMap.of("type", "integer");
	private final static Map<String, String> LONG = ImmutableMap.of("type", "long");
	private final static Map<String, String> DOUBLE = ImmutableMap.of("type", "double");
	private final static Map<String, String> SHORT = ImmutableMap.of("type", "short");
	private  final static Map<String, Object> TEXT = ImmutableMap.of(
			"type", "text",
			"fields", ImmutableMap.of("keyword", ImmutableMap.of("type", "text")));
	private static Map<String, Object> TEXT(int length) {
		return ImmutableMap.of(
				"type", "text",
				"fields", ImmutableMap.of(
						"keyword", ImmutableMap.of(
								"type", "keyword",
								"ignore_above", length
						)
				));
	}
	private static Map<String, Object> TEXT(int length, String analyzer) {
		return ImmutableMap.of(
				"type", "text",
				"analyzer", analyzer,
				"fields", ImmutableMap.of(
						"keyword", ImmutableMap.of(
								"type", "keyword",
								"ignore_above", length
						)
				));
	}
	static {
		Map<String, Object> properties = new HashMap<>(Map.of(
				"analysis",  KEYWORD,
				"create_at", DATE,
				"reference",  KEYWORD,
				"chrom",  KEYWORD,
				"pos",  LONG
		));
		properties.put("ref",  TEXT(1024));
		properties.put("alt",  TEXT(1024));
		properties.put("sample",  KEYWORD);
		properties.put("snv",  KEYWORD_GLOBAL);
		properties.put("tags",  KEYWORD);
		properties.put("hgvsc",  TEXT(256, "escape_special_characters_analyzer"));
		properties.put("hgvsp", TEXT(256, "escape_special_characters_analyzer"));
		properties.put("genotype",  KEYWORD);
		properties.put("qual", INTEGER);
		properties.put("depth", INTEGER);
		properties.put("vaf",  DOUBLE);
		properties.put("class",  KEYWORD);
		properties.put("class_order", SHORT);
		properties.put("tier",  KEYWORD);
		properties.put("dbsnp",  KEYWORD);
		properties.put("effect_level",  KEYWORD);
		properties.put("essential_gene",  KEYWORD);
		properties.put("insilico_sanger",  KEYWORD);
		properties.put("intervar_",  KEYWORD);
		properties.put("known_rec_info",  KEYWORD);
		properties.put("effect_level",  KEYWORD);
		properties.put("gene", ImmutableMap.of(
				"properties", ImmutableMap.of(
						"refgene",  KEYWORD,
						"full_name", TEXT(256)
				)));
		properties.put("organism",  KEYWORD);
		properties.put("exon",  TEXT(256, "escape_slash_analyzer"));
		properties.put("exon_in_hgmd",  TEXT(256, "escape_slash_analyzer"));
		properties.put("splicing_distance", LONG);
		properties.put("hgmd", ImmutableMap.of("properties", Map.of(
				"pmid", KEYWORD,
				"hgvsc", TEXT(256, "escape_special_characters_analyzer"),
				"hgvsp", TEXT(256, "escape_special_characters_analyzer"),
				"mut", KEYWORD,
				"tag", KEYWORD,
				"web", ImmutableMap.of("properties", ImmutableMap.of(
						"tag",  KEYWORD,
						"disease", TEXT(256),
						"literature", TEXT(256)
				)), "codon", ImmutableMap.of("properties", ImmutableMap.of(
						"tag",  KEYWORD,
						"disease", TEXT(256)
				))
		)));
		properties.put("clinvar", ImmutableMap.of("properties", Map.of(
				"id", KEYWORD,
				"class", TEXT(256),
				"clndbn", TEXT(256),
				"clnrevstat", TEXT(256),
				"clnsig", TEXT(256),
				"updated_class", TEXT(256),
				"updated_review", TEXT(256)
		)));
		properties.put("mim", ImmutableMap.of("properties", ImmutableMap.of(
				"phenotype_id", TEXT(256),
				"disease", TEXT,
				"inheritance", TEXT(256)
		)));
		properties.put("intervar", ImmutableMap.of("properties", ImmutableMap.of(
				"class", KEYWORD,
				"evidence_", TEXT(256, "escape_special_characters_analyzer"),
				"evidence", ImmutableMap.of("properties", ImmutableMap.of(
						"bv",  TEXT(256, "escape_special_characters_analyzer"),
						"pv", TEXT(256, "escape_special_characters_analyzer")
				))
		)));
		properties.put("disease_description",  TEXT);
		properties.put("function_description",  TEXT);
		properties.put("strand",  BOOLEAN);
		properties.put("filter",  KEYWORD);
		properties.put("codon",  TEXT(256, "escape_slash_analyzer"));
		properties.put("1000g", DOUBLE);
		properties.put("krg_db_1100", DOUBLE);
		properties.put("krgdb_af", DOUBLE);
		properties.put("gnomad", ImmutableMap.of("properties", ImmutableMap.of(
				"exome", ImmutableMap.of("properties", Map.of(
						"all", DOUBLE,
						"afr", DOUBLE,
						"amr", DOUBLE,
						"asj", DOUBLE,
						"eas", DOUBLE,
						"eas_kor", DOUBLE,
						"fin", DOUBLE,
						"nfe", DOUBLE,
						"oth", DOUBLE,
						"sas", DOUBLE
				)),"genome", ImmutableMap.of("properties", Map.of(
						"all", DOUBLE,
						"afr", DOUBLE,
						"amr", DOUBLE,
						"asj", DOUBLE,
						"eas", DOUBLE,
						"eas_kor", DOUBLE,
						"fin", DOUBLE,
						"nfe", DOUBLE,
						"oth", DOUBLE,
						"sas", DOUBLE
				)), "total", DOUBLE
		)));
		properties.put("go", ImmutableMap.of("properties", ImmutableMap.of(
				"biological_process", TEXT,
				"cellular_component", TEXT,
				"molecular_function", TEXT
		)));
		properties.put("mgi_mouse", ImmutableMap.of("properties", ImmutableMap.of(
				"gene", KEYWORD,
				"phenotype", TEXT(256)
		)));
		properties.put("esp6500", ImmutableMap.of("properties", ImmutableMap.of(
				"all", DOUBLE
		)));
		properties.put("exac", ImmutableMap.of("properties", ImmutableMap.of(
				"all", DOUBLE,
				"eas", DOUBLE,
				"sas", DOUBLE
		)));
		properties.put("cadd", ImmutableMap.of("properties", ImmutableMap.of(
				"pred", DOUBLE,
				"raw", DOUBLE,
				"result", TEXT(24)
		)));
		properties.put("dann", ImmutableMap.of("properties", ImmutableMap.of(
				"score", DOUBLE
		)));
		properties.put("gerp++", ImmutableMap.of("properties", ImmutableMap.of(
				"gt2", DOUBLE,
				"rs", DOUBLE
		)));
		properties.put("sift", ImmutableMap.of("properties",ImmutableMap.of(
				"pred", KEYWORD,
				"score", DOUBLE
		)));
		properties.put("provean", ImmutableMap.of("properties", ImmutableMap.of(
				"pred", KEYWORD,
				"score", DOUBLE
		)));
		properties.put("polyphen", ImmutableMap.of("properties", ImmutableMap.of(
				"pred", KEYWORD,
				"score", DOUBLE
		)));
		properties.put("polyphen2", Map.of("properties", ImmutableMap.of(
				"hdiv", ImmutableMap.of("properties", ImmutableMap.of(
						"pred", KEYWORD,
						"score", DOUBLE
				)),"hvar", ImmutableMap.of("properties", ImmutableMap.of(
						"pred", KEYWORD,
						"score", DOUBLE
				)), "score", TEXT(256)
		)));
		properties.put("mutationtaster", ImmutableMap.of("properties", ImmutableMap.of(
				"pred", KEYWORD,
				"score", KEYWORD
		)));
		properties.put("mutationassessor", ImmutableMap.of("properties", ImmutableMap.of(
				"pred", KEYWORD,
				"score", KEYWORD
		)));
		properties.put("m-cap", ImmutableMap.of("properties", ImmutableMap.of(
				"pred", KEYWORD,
				"score", DOUBLE
		)));
		properties.put("lrt", ImmutableMap.of("properties", ImmutableMap.of(
				"pred", KEYWORD,
				"score", DOUBLE
		)));
		properties.put("metasvm", ImmutableMap.of("properties", ImmutableMap.of(
				"pred", KEYWORD,
				"score", DOUBLE
		)));
		properties.put("metalr", ImmutableMap.of("properties", ImmutableMap.of(
				"pred", KEYWORD,
				"score", DOUBLE
		)));
		properties.put("dbscsnv1", ImmutableMap.of("properties", ImmutableMap.of(
				"1_ada_score", DOUBLE,
				"1_rf_score", DOUBLE
		)));
		properties.put("fathmmw", ImmutableMap.of("properties", ImmutableMap.of(
				"pred", TEXT(256),
				"score", TEXT(256)
		)));
		properties.put("ncc", ImmutableMap.of("properties", ImmutableMap.of(
				"class", TEXT(256),
				"interpret", TEXT,
				"sample", TEXT(256)
		)));
		properties.put("apogee", ImmutableMap.of("properties", ImmutableMap.of(
				"prob", KEYWORD,
				"score", KEYWORD
		)));
		properties.put("rmsk", TEXT(256, "escape_special_characters_analyzer"));
		properties.put("p(hi)", DOUBLE);
		properties.put("p(rec)", DOUBLE);
		properties.put("phastcons20way_mammalian", DOUBLE);
		properties.put("phylop20way_mammalian", DOUBLE);
		properties.put("siphy_29way_logodds", DOUBLE);
		properties.put("vest3_score", DOUBLE);
		properties.put("trait_association(gwas)", TEXT(512));
		ANALYSIS_MAPPING = ImmutableMap.of("properties", properties);
		ANALYSIS_SETTING = ImmutableMap.of("index", ImmutableMap.of(
				"number_of_shards", "1",
				"number_of_replicas", "2",
				"sort.field", List.of("snv","create_at"),
				"sort.order", List.of("asc", "desc"),
				"analysis", ImmutableMap.of(
						"analyzer", ImmutableMap.of(
								"escape_special_characters_analyzer", ImmutableMap.of(
										"filter", new String[] {
												"lowercase",
												"stop",
												"snowball"
										}, "char_filter", new String[] {"special_character_filter"},
										"tokenizer", "whitespace"
								),"escape_slash_analyzer", ImmutableMap.of(
										"filter", new String[] {
												"lowercase",
												"stop",
												"snowball"
										}, "char_filter", new String[] {"slash_filter"},
										"tokenizer", "whitespace"
								)
						),"char_filter", ImmutableMap.of(
								"special_character_filter", ImmutableMap.of(
										"type", "mapping",
										"mappings", new String[] {
												": => _doublecolon_",
												"> => _greaterthan_",
												"< => _lessthan_",
												". => _dot_",
												"= => _equals_",
												"+ => _plus_",
												"- => _minus_",
												"( => _openbracket_",
												") => _closebracket_"
										}
								),"slash_filter", ImmutableMap.of(
										"type", "mapping",
										"mappings", new String[] {"/ => _slash_"}
								)
						)
				)
		));
	}
	private void createIndex(String batch) {
		String indexName = "analysis-snv-" + batch;
		em.indexOps(IndexCoordinates.of(indexName.toLowerCase())).create(ANALYSIS_SETTING, Document.from(ANALYSIS_MAPPING));
	}
}
