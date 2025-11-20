package com.greencross.lims.worker;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.greencross.lims.dao.RequestDAO;
import com.greencross.lims.entity.Analysis;
import com.greencross.lims.entity.AnalysisFile;
import com.greencross.lims.entity.Request;
import com.greencross.lims.variant.data.IdGenerator;
import com.greencross.lims.variant.data.MapToIndexQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQuery;

import javax.annotation.Nullable;
import java.io.File;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

public abstract class DepthWorkerImpl implements Worker {
	private final ElasticsearchRestTemplate em;
	private final RequestDAO requestDAO;
	private final ObjectMapper om;
	private final Process processor;
	protected DepthWorkerImpl(ElasticsearchRestTemplate em, RequestDAO requestDAO, ObjectMapper om, Process processor) {
		this.em = em;
		this.requestDAO = requestDAO;
		this.om = om;
		this.processor = processor;
	}
	private static final String FIND_REQUEST_BY_SAMPLE = "SELECT * FROM request WHERE " +
			"sample IN (SELECT id FROM sample where patient=(SELECT patient FROM sample WHERE id=:sample)) " +
			"AND date_request >= (SELECT max(date_request)+'-1 week' FROM request WHERE sample=:sample) " +
			"AND date_request <= (SELECT max(date_request)+'1 week' FROM request WHERE sample=:sample)";
	private List<Analysis> findEntity(String fileName) {
		var sample = processor.sample(fileName);
		if(sample == null) {
			var entity = processor.map(fileName,null);
			entity = requestDAO.em().merge(entity);
			return List.of(entity);
		} else {
			var targets = Arrays.stream(services()).collect(Collectors.toSet());
			List<Request> req = requestDAO.em().createNativeQuery(FIND_REQUEST_BY_SAMPLE, Request.class).setParameter("sample", sample).getResultList();
			return req.stream()
					  .filter(r->targets.contains(r.pk().service()))
					  .map(r->processor.map(fileName, r))
					  .map(a->{
						  Analysis prev = requestDAO.em().find(Analysis.class, a.pk());
						  if(prev!=null) return prev;
						  else return requestDAO.em().merge(a);
					  }).collect(Collectors.toList());
		}
	}
	@Override
	public void process(AnalysisFile entity, File file) throws Exception {
		List<Analysis> targets = findEntity(entity.name());
		for(Analysis target: targets) {
			String batch = target.pk().batch();
			String indexName = ("depth-" + batch).toLowerCase();
			if(!em.indexOps(IndexCoordinates.of(indexName)).exists()) createIndex(batch);
			String analysisId = IdGenerator.analysisId(target);
			Map<String, Map<String, String>> annotations = merge(Files.lines(file.toPath()).iterator());
			List<IndexQuery> bulks = new LinkedList<>();
			for (Map.Entry<String, Map<String, String>> entry : annotations.entrySet()) {
				String id = analysisId + ":" + entry.getKey();
				Map<String, String> row = em.get(id, Map.class, IndexCoordinates.of(indexName));
				if (row == null) {
					row = entry.getValue();
					row.put("analysis", analysisId);
				} else for(String key: entry.getValue().keySet()) {
					row.put(key, entry.getValue().get(key));
				}
				Iterator<Map.Entry<String, String>> iter = row.entrySet().iterator();
				while(iter.hasNext()) {
					Map.Entry<String, String> e = iter.next();
					String value = e.getValue();
					if(value==null || value.isBlank()) iter.remove();
				}
				bulks.add(MapToIndexQuery.map(id, row, om));
				if(bulks.size() > 5000) {
					em.bulkIndex(bulks, IndexCoordinates.of(indexName));
					bulks.clear();
				}
				requestDAO.em().merge(target);

			}
			if (!bulks.isEmpty()) em.bulkIndex(bulks, IndexCoordinates.of(indexName));

		}
	}
	protected abstract String[] services();
	private static Map<String, Map<String, String>> merge(Iterator<String> rows) {
		Map<String, Map<String, String>> called = new HashMap<>();
		Map<String, Integer> header = header(rows.next());
		rows.forEachRemaining(row -> {
			Map<String, String> map = map(header, row);
			String depthId = IdGenerator.depthId(map);
			called.put(depthId, map);
		});
		return called;
	}
	private static Map<String, Integer> header(String line) {
		Map<String, Integer> map = new HashMap<>();
		String[] split = line.split("\t", -1);
		for (int i = 0; i < split.length; ++i) {
			String key = split[i].toLowerCase();
			if(key.startsWith("coverage") && !key.contains("(%)")) key = key + "(%)";
			if(key.startsWith("coverage") && map.containsKey(key)) key = key + "_mapq_not_filtered";
			if(key.equals("depth") && map.containsKey(key)) key = "depth_not_filtered";
			map.put(key, i);
		}
		return Collections.unmodifiableMap(map);
	}
	private static Map<String, String> map(Map<String, Integer> header, String line) {
		Map<String, String> map = new HashMap<>();
		String[] split = line.split("\t", -1);
		String chr = cellValue(split[header.containsKey("chr")?header.get("chr"):header.get("chrom")]);
		String gene = cellValue(split[header.get("gene")]);
		String[] info = cellValue(split[header.containsKey("exon")?header.get("exon"):header.get("info")]).split("\\|");
		if(info.length == 4) {
			String reference = info[1];
			String exon = info[2];
			String pos = info[3];
			String[] startend = pos.substring(pos.indexOf(":")+1).split("-");
			String start = startend[0];
			String end = startend[1];
			map.put("reference", reference);
			map.put("exon", exon);
			map.put("start", start);
			map.put("end", end);
		} else if(info.length == 2) {
			String pos = info[1];
			String[] startend = pos.substring(pos.indexOf(":")+1).split("-");
			String start = startend[0];
			String end = startend[1];
			map.put("start", start);
			map.put("end", end);
		} else if(info.length == 1) {
			String pos = info[0];
			String[] startend = pos.substring(pos.indexOf(":")+1).split("-");
			String start = startend[0];
			String end = startend[1];
			map.put("start", start);
			map.put("end", end);
		} else throw new RuntimeException("Exon format error:" + split[header.get("exon")]);
		String depth = cellValue(split[header.get("depth")]);
		String depthNf = cellValue(split[header.get("depth_not_filtered")]);
		String x1 = cellValue(split[header.get("coverage1x(%)")]);
		String x5 = cellValue(split[header.get("coverage5x(%)")]);
		String x10 = cellValue(split[header.get("coverage10x(%)")]);
		String x20 = cellValue(split[header.get("coverage20x(%)")]);
		String x30 = cellValue(split[header.get("coverage30x(%)")]);
		String x1Nf = cellValue(split[header.get("coverage1x(%)_mapq_not_filtered")]);
		String x5Nf = cellValue(split[header.get("coverage5x(%)_mapq_not_filtered")]);
		String x10Nf = cellValue(split[header.get("coverage10x(%)_mapq_not_filtered")]);
		String x20Nf = cellValue(split[header.get("coverage20x(%)_mapq_not_filtered")]);
		String x30Nf = cellValue(split[header.get("coverage30x(%)_mapq_not_filtered")]);
		map.put("chrom", chr);
		map.put("gene", gene);
		map.put("depth.filtered", depth);
		map.put("depth.not_filtered", depthNf);
		map.put("coverage.filtered.1x", x1);
		map.put("coverage.filtered.5x", x5);
		map.put("coverage.filtered.10x", x10);
		map.put("coverage.filtered.20x", x20);
		map.put("coverage.filtered.30x", x30);
		map.put("coverage.not_filtered.1x", x1Nf);
		map.put("coverage.not_filtered.5x", x5Nf);
		map.put("coverage.not_filtered.10x", x10Nf);
		map.put("coverage.not_filtered.20x", x20Nf);
		map.put("coverage.not_filtered.30x", x30Nf);
		return map;
	}
	private static String cellValue(String value) {
		if(".".equals(value)) return null;
		else return value;
	}
	public interface Process {
		Long sample(String fileName);
		Analysis map(String fileName, @Nullable Request request);
	}
	private final static Map<String, Object> DEPTH_MAPPING;
	private final static Map<String, Object> DEPTH_SETTING;
	private final static Map<String, String> KEYWORD = ImmutableMap.of("type", "keyword");
	private final static Map<String, String> LONG = ImmutableMap.of("type", "long");
	private final static Map<String, String> DOUBLE = ImmutableMap.of("type", "double");
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
				"analysis",  TEXT(256),
				"reference",  KEYWORD,
				"chrom",  KEYWORD,
				"start",  LONG,
				"end",  LONG,
				"gene",  KEYWORD
		));
		properties.put("exon",  TEXT(256, "escape_slash_analyzer"));
		properties.put("depth", ImmutableMap.of("properties", ImmutableMap.of(
				"filtered", DOUBLE,
				"not_filtered", DOUBLE
		)));
		properties.put("coverage", ImmutableMap.of("properties", ImmutableMap.of(
				"filtered",  ImmutableMap.of("properties", Map.of(
						"1x", DOUBLE, "5x", DOUBLE, "10x", DOUBLE,
						"20x", DOUBLE, "30x", DOUBLE, "50x", DOUBLE,
						"100x", DOUBLE
				)), "not_filtered",  ImmutableMap.of("properties", Map.of(
						"1x", DOUBLE, "5x", DOUBLE, "10x", DOUBLE,
						"20x", DOUBLE, "30x", DOUBLE, "50x", DOUBLE,
						"100x", DOUBLE
				))
		)));
		DEPTH_MAPPING = ImmutableMap.of("properties", properties);
		DEPTH_SETTING = ImmutableMap.of("index", ImmutableMap.of(
				"number_of_shards", "16",
				"number_of_replicas", "3",
				"routing", ImmutableMap.of("allocation", ImmutableMap.of("include", ImmutableMap.of( "_tier_preference", "data_content"))),
				"analysis", ImmutableMap.of(
						"analyzer", ImmutableMap.of(
								"escape_slash_analyzer", ImmutableMap.of(
										"filter", new String[] {
												"lowercase",
												"stop",
												"snowball"
										}, "char_filter", new String[] {"slash_filter"},
										"tokenizer", "whitespace"
								)
						),"char_filter", ImmutableMap.of(
								"slash_filter", ImmutableMap.of(
										"type", "mapping",
										"mappings", new String[] {"/ => _slash_"}
								)
						)
				)
		));
	}
	private void createIndex(String batch) {
		String indexName = "depth-" + batch;
		em.indexOps(IndexCoordinates.of(indexName.toLowerCase())).create(DEPTH_SETTING, Document.from(DEPTH_MAPPING));
	}
}
