package com.greencross.lims.service.interpretation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gcgenome.lims.test.panel.TestInfo;
import com.google.common.base.Functions;
import com.greencross.lims.dto.interpretation.BaseVariant;
import com.greencross.lims.dto.interpretation.InterpretationParam;
import com.greencross.lims.dto.interpretation.PanelTest;
import com.greencross.lims.entity.Patient;
import com.greencross.lims.entity.Sample;
import com.greencross.lims.service.InterpretationDAO;
import com.greencross.lims.service.SnvDAO;
import com.greencross.lims.trans.Phrase;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.gcgenome.lims.test.panel.TestInfo.G2200101;
import static com.gcgenome.lims.test.panel.TestInfo.G2200201;

@Component
public class Cancer implements Interpretable {
	private final InterpretationDAO dao;
	private final SnvDAO snvRepo;
	private final ObjectMapper om;
	public Cancer(InterpretationDAO dao, SnvDAO snvRepo, ObjectMapper om) {
		this.dao = dao;
		this.snvRepo = snvRepo;
		this.om = om;
	}

	@Override
	public boolean chk(long sample, String service) {
		if("ON001".equalsIgnoreCase(service)) return false;
		if("ON040".equalsIgnoreCase(service)) return false;
		if(G2200101.code().equals(service)) return false;
		if(G2200201.code().equals(service)) return false;
		for(TestInfo test: TestInfo.TESTS) if(test.category() == TestInfo.Category.CANCER && test.code().equalsIgnoreCase(service)) return true;
		return false;
	}

	@Override
	@Transactional(readOnly = true)
	public Mono<Object> interpret(long sample, String service, InterpretationParam param) {
		TestInfo test = Arrays.stream(TestInfo.TESTS).filter(t->t.code().equalsIgnoreCase(service)).findFirst().get();
		Patient patient = dao.em().find(Sample.class, sample).patient();
		var prev = om.convertValue(param.json(), PanelTest.class);
		prev.reasonForReferral("R/O " + test.referralDefault());
		AtomicBoolean hasMaf = new AtomicBoolean(false);
		if(prev.variants()!=null) prev.variants(Arrays.stream(prev.variants())
													  .filter(c->reportable(c.clazz()))
													  .toArray(PanelTest.Variant[]::new));
		if(prev.variants()==null || prev.variants().length <= 0) prev.result("NEGATIVE").resultText(NEGATIVE_RESULT_FORMAT).interpretation(NEGATIVE_FORMAT.replace("%p", test.name().replace("검사","")));
		else {
			List<PanelTest.Variant> variants = new LinkedList<>(Arrays.asList(prev.variants()));
			long pv = variants.stream().filter(v->"P".equalsIgnoreCase(v.clazz()) || "PV".equalsIgnoreCase(v.clazz())).count();
			long lpv = variants.stream().filter(v->"LP".equalsIgnoreCase(v.clazz()) || "LPV".equalsIgnoreCase(v.clazz())).count();
			if(pv <= 0 && lpv <= 0) prev.result("INCONCLUSIVE");
			else prev.result("POSITIVE");
			prev.resultText(summary(variants));

			List<String> abbreviationReference = new ArrayList<>();
			List<String> abbreviationDisease = new ArrayList<>();
			List<String> abbreviationZygosity = new ArrayList<>();
			List<String> abbreviationInheritance = new ArrayList<>();
			List<String> abbreviationClass = new ArrayList<>();
			for(PanelTest.Variant variant: prev.variants()) {
				String snv = variant.snv();
				String analysis = variant.analysis();
				String batch = analysis.trim().split(":", -1)[1];
				String gene = variant.gene();
				List<InterpretationParam.InterpretationParamDisease> diseases = param.disease().get(gene);
				variant.disease(diseases.stream().map(InterpretationParam.InterpretationParamDisease::abbreviation).distinct().collect(Collectors.joining(", ")));
				variant.inheritance(diseases.stream().map(InterpretationParam.InterpretationParamDisease::inheritance).flatMap(Collection::stream).distinct().collect(Collectors.joining(", ")));

				Document values = snvRepo.find(batch, analysis + ":" + snv, Document.class);
				String abbrZyg = abbreviationZygosity(patient.sex(), variant, values);
				abbreviationReference.add(abbreviationReference(variant));
				for(InterpretationParam.InterpretationParamDisease info: diseases) abbreviationDisease.add(abbreviationDisease(info));
				abbreviationZygosity.add(abbrZyg);

				String[] inhs = abbreviationInheritance(variant, param.disease().get(gene));
				if(inhs!=null) abbreviationInheritance.addAll(Arrays.asList(inhs));
				abbreviationClass.add(abbreviationClass(variant));
			}
			String abbrReference = abbreviationReference.stream().distinct().collect(Collectors.joining("; "));
			String abbrDisease = abbreviationDisease.stream().distinct().collect(Collectors.joining("; "));
			String abbr = Stream.concat(Stream.concat(abbreviationZygosity.stream(), abbreviationInheritance.stream()), abbreviationClass.stream())
								.distinct().sorted().collect(Collectors.joining("; "));
			String comment = positive(patient, variants, hasMaf);
			if(param.suffix()!=null && !param.suffix().trim().isEmpty()) comment = comment + "\r\n\r\n" + param.suffix();
			prev.abbreviationReference(abbrReference).abbreviationDisease(abbrDisease).abbreviation(abbr)
				.interpretation(comment);
		}
		if(prev.addendum()==null || prev.addendum().variants()==null || prev.addendum().variants().length <= 0) prev.addendum(null);
		else {
			List<PanelTest.Variant> variants = new LinkedList<>(Arrays.asList(prev.addendum().variants()));

			List<String> abbreviationReference = new ArrayList<>();
			List<String> abbreviationDisease = new ArrayList<>();
			List<String> abbreviationZygosity = new ArrayList<>();
			List<String> abbreviationInheritance = new ArrayList<>();
			List<String> abbreviationClass = new ArrayList<>();
			for(PanelTest.Variant variant: prev.addendum().variants()) {
				String snv = variant.snv();
				String analysis = variant.analysis();
				String batch = analysis.trim().split(":", -1)[1];
				String gene = variant.gene();
				List<InterpretationParam.InterpretationParamDisease> diseases = param.disease().get(gene);
				variant.disease(diseases.stream().map(InterpretationParam.InterpretationParamDisease::abbreviation).distinct().collect(Collectors.joining(", ")));
				variant.inheritance(diseases.stream().map(InterpretationParam.InterpretationParamDisease::inheritance).flatMap(Collection::stream).distinct().collect(Collectors.joining(", ")));

				Document values = snvRepo.find(batch, analysis + ":" + snv, Document.class);
				String abbrZyg = abbreviationZygosity(patient.sex(), variant, values);
				abbreviationReference.add(abbreviationReference(variant));
				for(InterpretationParam.InterpretationParamDisease info: diseases) abbreviationDisease.add(abbreviationDisease(info));
				abbreviationZygosity.add(abbrZyg);

				String[] inhs = abbreviationInheritance(variant, param.disease().get(gene));
				if(inhs!=null) abbreviationInheritance.addAll(Arrays.asList(inhs));
				abbreviationClass.add(abbreviationClass(variant));
			}
			String abbrReference = abbreviationReference.stream().distinct().collect(Collectors.joining("; "));
			String abbrDisease = abbreviationDisease.stream().distinct().collect(Collectors.joining("; "));
			String abbr = Stream.concat(Stream.concat(abbreviationZygosity.stream(), abbreviationInheritance.stream()), abbreviationClass.stream())
								.distinct().sorted().collect(Collectors.joining("; "));
			prev.addendum().abbreviationReference(abbrReference).abbreviationDisease(abbrDisease).abbreviation(abbr)
				.interpretation(positive(patient, variants, hasMaf));
		}
		return Mono.just(prev);
	}
	private static boolean reportable(String clazz) {
		if(clazz == null) return false;
		clazz = clazz.trim().toUpperCase();
		if("P".equals(clazz)) return true;
		if("PV".equals(clazz)) return true;
		if("LP".equals(clazz)) return true;
		if("LPV".equals(clazz)) return true;
		if("VUS".equals(clazz)) return true;
		return false;
	}
	private static String summary(List<PanelTest.Variant> variants) {
		StringBuilder sb = new StringBuilder();
		Map<String, List<PanelTest.Variant>> vars = variants.stream().collect(Collectors.groupingBy(PanelTest.Variant::clazz));
		List<PanelTest.Variant> Ps = vars.getOrDefault("P", new ArrayList<>());
		Ps.addAll(vars.getOrDefault("PV", new ArrayList<>()));
		List<PanelTest.Variant> LPs = vars.getOrDefault("LP", new ArrayList<>());
		LPs.addAll(vars.getOrDefault("LPV", new ArrayList<>()));
		List<PanelTest.Variant> VUSs = vars.getOrDefault("VUS", List.of());
		addLineToResultHeader(Ps, "PV", sb);
		addLineToResultHeader(LPs, "LPV", sb);
		addLineToResultHeader(VUSs, "VUS", sb);
		return sb.toString();
	}

	private static Stream<String> uniqueAndSortGenes(List<PanelTest.Variant> variants){
		return variants.stream().map(BaseVariant::gene).collect(Collectors.toSet()).stream().sorted();
	}
	private static void addLineToResultHeader(List<PanelTest.Variant> variants, String clazz, StringBuilder sb){
		uniqueAndSortGenes(variants).forEach((var) -> {
			sb.append(var);
			sb.append(", ");
		});
		if(!variants.isEmpty()){
			sb.setLength(sb.length()-2);
			sb.append(" 유전자에서 ");
			sb.append(Phrase.classFull(clazz));
			sb.append("가 발견되었습니다. ");
		}
	}

	private static final String NEGATIVE_RESULT_FORMAT = "질환과 관련된 변이는 발견되지 않았습니다.";
	private static final String NEGATIVE_FORMAT =  "%p 분석 결과, 질환과 관련된 변이가 발견되지 않았습니다.\n\n" +
												  "본 검사는 검사에 포함된 유전자들의 coding exon과 인접 intron 영역을 분석하며, " +
												  "deep intronic variant, exon deletion/duplication, genomic rearrangement를 " +
												  "포함한 copy number variant 검출은 제한적입니다. 또한, GC 함량이 높거나 " +
												  "염기서열 상동성이 높은 일부 유전자의 exon 부위는 본 검사법으로 변이를 검출하는데 " +
												  "한계가 있습니다. 검사의 기술적 한계에 대한 자세한 내용은 검사의 한계를 참조하십시오.";
	@Override
	public Object negative(long sample, String service) {
		TestInfo test = Arrays.stream(TestInfo.TESTS).filter(t->t.code().equalsIgnoreCase(service)).findFirst().get();
		var interpretation = new PanelTest();
		interpretation.result("NEGATIVE")
					  .reasonForReferral("R/O " + test.referralDefault())
					  .resultText(NEGATIVE_RESULT_FORMAT).interpretation(NEGATIVE_FORMAT.replace("%p", test.name().replace("검사","")));
		return interpretation;
	}

	private String positive(Patient patient, List<PanelTest.Variant> variants, AtomicBoolean hasMaf) {
		Map<String, List<PanelTest.Variant>> groupByGene = variants.stream().collect(Collectors.groupingBy(PanelTest.Variant::gene));
		Set<String> hit = new HashSet<>();
		return variants.stream().filter(v->!hit.contains(v.gene())).peek(v->hit.add(v.gene())).map(v->
			positive(patient, v.gene(), groupByGene.get(v.gene()), hasMaf)
		).collect(Collectors.joining("\r\n\r\n"));
	}
	private String positive(Patient patient, String gene, List<PanelTest.Variant> variants, AtomicBoolean hasMaf) {
		Map<PanelTest.Variant, Document> documents = variants.stream().collect(Collectors.toMap(Functions.identity(), v-> {
			String batch = v.analysis().split(":", -1)[1];
			return snvRepo.find(batch,v.analysis() + ":" + v.snv(), Document.class);
		}));
		String comment = variants.stream()
								 .map(v->comment(gene, v, documents.get(v), hasMaf))
								 .collect(Collectors.joining("\r\n"));
		boolean isSingle = variants.size() == 1;
		if(!isSingle) return comment;
		PanelTest.Variant target = variants.get(0);
		Document values = documents.get(target);
		if(!"VUS".equalsIgnoreCase(target.clazz())) return comment;
		String chrom = values.getString("chrom");
		String genotype = values.getString("genotype");
		String zygosity = Phrase.zygosity(genotype, patient.sex(), chrom);
		if(!"Het".equalsIgnoreCase(zygosity)) return comment;
		if(!target.inheritance().contains("AR")) return comment;
		else return comment +
		"\r\n상염색체 열성 질환에서 한 개의 heterozygous VUS 변이만이 발견된 경우, 1) 이 변이가 질환과 관련이 없는 변이일 가능성, " +
		"2) 실제 질환과 관련이 있지만 보인자일 가능성, 3) 실제 이 유전자와 관련된 질환을 가지고 있지만 나머지 하나의 대립 유전자가 " +
		"본 검사로 검출되지 않는 종류의 변이(large deletion or insertion, deep intronic variant, etc.)를 동반할 가능성 등이 있습니다.";
	}
	private String comment(String gene, PanelTest.Variant variant, Document values, AtomicBoolean hasMaf) {
		StringBuilder sb = new StringBuilder(gene).append("에서 ");
		String commentHgvsc = Phrase.hgvscText(variant.hgvsc());
		String commentHgvsp = Phrase.hgvspText(variant.hgvsp());
		String commentPopulation = commentPopulation(values, hasMaf).trim();
		String commentClinvar = commentClinvar(values).trim();
		String commentInsilico = commentInsilico(values);
		if(commentHgvsc!=null) {
			sb.append(commentHgvsc);
			if(commentHgvsp!=null) sb.append("되어 ").append(commentHgvsp).append("예상되는 ");
			else sb.append("되는 ");
		}
		sb.append(Phrase.classLong(variant.clazz())).append("인 ")
		  .append(variant.hgvsc());
		sb.append("가 발견되었습니다.");
		if(!commentPopulation.isEmpty()) {
			sb.append(" 이 변이는 ").append(commentPopulation);
			if(commentClinvar.isEmpty()) sb.append(" 변이입니다.");
			else sb.append(" 변이로 ").append(commentClinvar);
		} else if(!commentClinvar.isEmpty()) sb.append(" 이 변이는 ").append(commentClinvar);
		if(!commentInsilico.isEmpty()) sb.append(commentInsilico);
		return sb.toString();
	}
	private static int compare(String g1, String g2, Map<String, Long> cnt) {
		if(cnt.containsKey(g1)) {
			if(cnt.containsKey(g2)) return Long.compare(cnt.get(g2), cnt.get(g1));
			else return -1;
		} else if(cnt.containsKey(g2)) return 1;
		else return 0;
	}
	private static final String GNOMAD_KEY = "gnomad.total";
	private static final String KRGDB_KEY = "krgdb_af";
	private String commentPopulation(Document values, AtomicBoolean hasPreviousPopulationComment) {
		boolean hasGnomad = hasValue(values, GNOMAD_KEY, "", ".", "0", "0.0", "0.00", "0.000", "0.0000");
		boolean hasKrgdb = hasValue(values, KRGDB_KEY, "", ".", "0", "0.0", "0.00", "0.000", "0.0000");
		String maf = hasPreviousPopulationComment.get()?"MAF":"minor allele frequency (MAF)";
		if(hasGnomad & hasKrgdb) {
			hasPreviousPopulationComment.set(true);
			return String.format("전체 인구집단(gnomAD)에서의 " + maf + "는 %s%%이며 한국인 인구집단(KRGDB)에서는 %s%%인 ", percentage(values.getString(GNOMAD_KEY)), percentage(values.getString(KRGDB_KEY)));
		} else if(hasGnomad) {
			hasPreviousPopulationComment.set(true);
			return String.format("전체 인구집단(gnomAD)에서의 " + maf + "는 %s%%이며 한국인 인구집단(KRGDB)에서는 보고된 바 없는 ", percentage(values.getString(GNOMAD_KEY)));
		} else if(hasKrgdb) {
			hasPreviousPopulationComment.set(true);
			return String.format("전체 인구집단(gnomAD)에서 보고된 바 없으나  한국인 인구집단(KRGDB)에서의 " + maf + "는 %s%%인 ", percentage(values.getString(KRGDB_KEY)));
		}
		else return "일반 인구집단(gnomAD, KRGDB)에서 보고된 바 없는 매우 드문 ";
	}
	private String percentage(String s) {
		if(s == null) return null;
		double number = Double.parseDouble(s) * 100;
		if(number < 0.0001) return String.format("%f", number);
		else return String.format("%.4f", number);
	}
	private boolean hasValue(Document doc, String key, Object... empties) {
		if(!doc.containsKey(key)) return false;
		if(empties!=null) {
			Object value = doc.get(key);
			for(Object empty: empties) if(empty.equals(value)) return false;
		}
		return true;
	}
	private static final String CLINVAR_CLASS_KEY = "clinvar.class";
	private static final String CLINVAR_ID_KEY = "clinvar.id";
	private static final String[] CLINVAR_IGNORES = new String[] {
			",_other", ",_risk_factor", ",_protective", "not_provided"
	};
	public String commentClinvar(Document values) {
		boolean hasClinvar = hasValue(values, CLINVAR_CLASS_KEY, "", ".");
		if(!hasClinvar) return "";
		String clinvar = values.getString(CLINVAR_CLASS_KEY);
		for(String ignore: CLINVAR_IGNORES) clinvar = clinvar.replace(ignore, "");
		if(clinvar.length()<=0 || ".".equalsIgnoreCase(clinvar)) return "";
		if(clinvar.startsWith("Conflicting_interpretations_of_pathogenicity"))
			return "Clinvar에서 Submitter마다 서로 다르게 분류한 바 있습니다["
						   + clinvar.substring(clinvar.indexOf("|")+1).replace('_', ' ')
						   + "].";
		else {
			String id = values.getString(CLINVAR_ID_KEY);
			return "Clinvar에서 " + clinvar.replace('_', ' ') +(clinvar.endsWith("cance")?"":"으")+"로 분류되어 있습니다(ID: " + id + ").";
		}
	}

	private static final String SIFT_PRED_KEY = "sift.pred";
	private static final String POLY_PRED_KEY = "polyphen.pred";
	private static final String MUT_PRED_KEY = "mutationtaster.pred";
	private String commentInsilico(Document values) {
		boolean hasSift = hasValue(values, SIFT_PRED_KEY, "", ".");
		boolean hasPoly = hasValue(values, POLY_PRED_KEY, "", ".");
		boolean hasMuta = hasValue(values, MUT_PRED_KEY, "", ".");
		boolean hasInsilico = hasSift & hasPoly & hasMuta;
		if(!hasInsilico) return "";

		var sv = values.getString(SIFT_PRED_KEY);
		var pv = values.getString(POLY_PRED_KEY);
		var mv = values.getString(MUT_PRED_KEY);
		if(sv == null) sv = "";
		if(pv == null) pv = "";
		if(mv == null) mv = "";
		if("unknown".equalsIgnoreCase(pv)) return "";
		boolean sift = false;
		boolean polyphen = false;

		if(sv.length()<=2) sift = !"T".equalsIgnoreCase(sv);
		else sift = !sv.toLowerCase().startsWith("tolerated");
		if(pv.length()<=2) polyphen = !"B".equalsIgnoreCase(pv);
		else polyphen = !"benign".equalsIgnoreCase(pv);
		boolean mutationtaster = (mv.charAt(0) == 'D') || (mv.charAt(0) == 'A');

		StringBuilder sb = new StringBuilder();
		if(sift & polyphen & mutationtaster) sb.append(" In-silico prediction(SIFT, PolyPhen-2, MutationTaster)에서는 Deleterious로 예측되었습니다.");
		else if(!sift & !polyphen & !mutationtaster) sb.append(" In-silico prediction(SIFT, PolyPhen-2, MutationTaster)에서는 Tolerated로 예측되었습니다.");
		else {
			sb.append(" In-silico prediction에서는 서로 상이한 결과를 보였습니다(");
			List<String> deleteriouses = new LinkedList<>();
			List<String> benignes = new LinkedList<>();
			if(sift) deleteriouses.add("SIFT");
			else benignes.add("SIFT");
			if(polyphen) deleteriouses.add("PolyPhen-2");
			else benignes.add("PolyPhen-2");
			if(mutationtaster) deleteriouses.add("MutationTaster");
			else benignes.add("MutationTaster");
			sb.append(deleteriouses.stream().collect(Collectors.joining(", "))).append(": Deleterious, ")
					.append(benignes.stream().collect(Collectors.joining(", "))).append(": Tolerated).");
		}
		return sb.toString();
	}
	private String abbreviationReference(PanelTest.Variant variant) {
		if(variant.hgvsc() == null || variant.hgvsp() == null) throw new IllegalArgumentException();
		if(variant.hgvsc().contains(":")) {
			String ref = variant.hgvsc().split(":")[0].trim();
			variant.hgvsc(variant.hgvsc().substring(variant.hgvsc().indexOf(":") + 1).trim());
			return ref + "(" + variant.gene() + ")";
		} else return "";
	}
	private String abbreviationDisease(InterpretationParam.InterpretationParamDisease param) {
		if(param == null) return "";
		String disease = param.fullName();
		String diseaseShort = param.abbreviation();
		if(disease.equals(diseaseShort)) return "";
		else return diseaseShort + ", " + disease;
	}
	private String abbreviationZygosity(Patient.Sex sex, PanelTest.Variant variant, Document values) {
		if("Het".equalsIgnoreCase(variant.zygosity()) || "Hom".equalsIgnoreCase(variant.zygosity()) || "Hem".equalsIgnoreCase(variant.zygosity())) {
			String zygosity = Phrase.zygosityFull(variant.zygosity());
			return variant.zygosity() + ", " + zygosity;
		} else {
			String chrom = values.getString("chrom");
			String genotype = values.getString("genotype");
			String zygosity = Phrase.zygosity(genotype, sex, chrom);
			String zygosityShort = Phrase.zygosityShort(zygosity);
			if (variant.zygosity() == null || variant.zygosity().trim().isEmpty() || variant.zygosity().contains("0") || variant.zygosity().contains("1"))
				variant.zygosity(zygosityShort);
			return zygosityShort + ", " + zygosity;
		}
	}
	private String[] abbreviationInheritance(PanelTest.Variant variant, List<InterpretationParam.InterpretationParamDisease> param) {
		if(param==null || param.isEmpty() || param.stream().filter(p->p.inheritance()!=null).allMatch(p->p.inheritance().isEmpty())) return null;
		return param.stream().filter(p->p.inheritance()!=null)
					.map(InterpretationParam.InterpretationParamDisease::inheritance)
					.flatMap(Collection::stream)
					.map(c->c.split(","))
					.flatMap(Arrays::stream)
					.map(String::trim).distinct()
					.filter(c->!c.trim().equals(Phrase.inheritanceLong(c.trim())))
					.filter(c->!"<?>".equals(Phrase.inheritanceLong(c.trim())))
					.map(c -> c.trim() + ", " + Phrase.inheritanceLong(c.trim()))
					.toArray(String[]::new);
	}
	private String abbreviationClass(PanelTest.Variant variant) {
		return variant.clazz() + ", " + Phrase.classLong(variant.clazz());
	}
	public String unwrap(String str) {
		if(str == null || str.isEmpty()) return str;
		str = str.trim();
		if(!str.startsWith("[") || !str.endsWith("]")) return str;
		try {
			String[] arr = om.readValue(str, String[].class);
			return Arrays.stream(arr).collect(Collectors.joining(","));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return str;
	}
}
