package com.greencross.lims.service.interpretation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gcgenome.lims.test.genomescreen.TestInfo;
import com.gcgenome.lims.test.genomescreen.TestWithRiskScreen;
import com.google.common.base.Functions;
import com.greencross.lims.dto.interpretation.GenomeScreen.Variant;
import com.greencross.lims.dto.interpretation.InterpretationParam;
import com.greencross.lims.entity.Patient;
import com.greencross.lims.entity.Sample;
import com.greencross.lims.service.InterpretationDAO;
import com.greencross.lims.service.SnvDAO;
import com.greencross.lims.service.report.genomescreen.*;
import com.greencross.lims.trans.Korean;
import com.greencross.lims.trans.Phrase;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Component
public class GenomeScreen implements Interpretable {
	private final InterpretationDAO dao;
	private final SnvDAO snvRepo;
	private final ObjectMapper om;
	public GenomeScreen(InterpretationDAO dao, SnvDAO snvRepo, ObjectMapper om) {
		this.dao = dao;
		this.snvRepo = snvRepo;
		this.om = om;
	}
	@Override
	public boolean chk(long sample, String service) {
		for(TestInfo test: TestInfo.TESTS) if("KOKR".equalsIgnoreCase(test.i18n()) && test.code().equalsIgnoreCase(service)) return true;
		return false;
	}
	protected TestInfo test(String service) {
		return Arrays.stream(TestInfo.TESTS).filter(t->"KOKR".equalsIgnoreCase(t.i18n()) && t.code().equalsIgnoreCase(service)).findFirst().get();
	}
	private static boolean reportable(String clazz) {
		if(clazz == null) return false;
		clazz = clazz.trim().toUpperCase();
		if("P".equals(clazz)) return true;
		if("PV".equals(clazz)) return true;
		if("LP".equals(clazz)) return true;
		if("LPV".equals(clazz)) return true;
		if("VUS".equals(clazz)) return true;
		if("B".equals(clazz)) return true;
		if("BV".equals(clazz)) return true;
		if("LB".equals(clazz)) return true;
		if("LBV".equals(clazz)) return true;
		return false;
	}
	private static boolean positive(String clazz) {
		if(clazz == null) return false;
		clazz = clazz.trim().toUpperCase();
		if("P".equals(clazz)) return true;
		if("PV".equals(clazz)) return true;
		if("LP".equals(clazz)) return true;
		if("LPV".equals(clazz)) return true;
		return false;
	}
	@Override
	@Transactional(readOnly = true)
	public Mono<Object> interpret(long sample, String service, InterpretationParam param) {
		Patient patient = dao.em().find(Sample.class, sample).patient();
		var prev = om.convertValue(param.json(), com.greencross.lims.dto.interpretation.GenomeScreen.class);
		AtomicBoolean hasMaf = new AtomicBoolean(false);
		if(prev!=null) prev.variants(Arrays.stream(prev.variants())
				.filter(c->reportable(c.clazz()))
				.sorted(Comparator.comparingInt(a -> Phrase.ordinalClass(a.clazz())))
				.toArray(Variant[]::new));
		var pvs = Arrays.stream(prev.variants())
				.filter(c->positive(c.clazz()))
				.sorted(Comparator.comparingInt(a -> Phrase.ordinalClass(a.clazz())))
				.toArray(Variant[]::new);
		if(prev.variants()==null || prev.variants().length <= 0) {
			prev = negative(sample, service, prev.meanDepth(), prev.coverage());
			if(param.suffix()!=null && !param.suffix().trim().isEmpty()) prev.interpretation((prev.interpretation() + "\r\n\r\n" + param.suffix()).trim());
			return Mono.just(prev);
		}
		else if(pvs.length <= 0) return Mono.just(negative(patient, sample, service, prev.variants(), prev.meanDepth(), prev.coverage()));
		else {
			var test = test(service);
			List<Variant> variants = Arrays.asList(prev.variants());
			Map<Variant, Document> documents = variants.stream().collect(Collectors.toMap(Functions.identity(), v->{
				String batch = v.analysis().split(":", -1)[1];
				return snvRepo.find(batch, v.analysis() + ":" + v.snv(), Document.class);
			}));
			for(Variant var: variants) normalize(patient.sex(), var, documents.get(var));
			String comment = positive(patient, variants, documents, hasMaf);
			if(param.suffix()!=null && !param.suffix().trim().isEmpty()) comment = comment + "\r\n\r\n" + param.suffix();
			prev.summary(summaryPositive(test, pvs)).interpretation(comment.trim());
			Set<String> positives = variants.stream().map(Variant::gene).collect(Collectors.toSet());
			com.greencross.lims.dto.interpretation.GenomeScreen.Disease[] diseases = Arrays.stream(test.diseases()).map(d->{
				com.greencross.lims.dto.interpretation.GenomeScreen.Gene[] genes = Arrays.stream(d.genes()).map(g->
								new com.greencross.lims.dto.interpretation.GenomeScreen.Gene().name(g.gene()).value(positives.contains(g.gene())))
						.toArray(com.greencross.lims.dto.interpretation.GenomeScreen.Gene[]::new);
				return new com.greencross.lims.dto.interpretation.GenomeScreen.Disease().name(d.name()).values(genes);
			}).toArray(com.greencross.lims.dto.interpretation.GenomeScreen.Disease[]::new);
			prev.diseases(diseases);
		}
		return Mono.just(prev);
	}
	private String positive(Patient patient, List<Variant> variants, Map<Variant, Document> documents, AtomicBoolean hasMaf) {
		Map<String, List<Variant>> groupByGene = variants.stream().collect(Collectors.groupingBy(Variant::gene));
		Map<String, Long> cntPathogenicPerGene = variants.stream().filter(v->"P".equalsIgnoreCase(v.clazz()) || "PV".equalsIgnoreCase(v.clazz()))
														 .collect(Collectors.groupingBy(Variant::gene, Collectors.counting()));
		Map<String, Long> cntLikelyPathogenicPerGene = variants.stream().filter(v->"LP".equalsIgnoreCase(v.clazz()) || "LPV".equalsIgnoreCase(v.clazz()))
															   .collect(Collectors.groupingBy(Variant::gene, Collectors.counting()));
		return groupByGene.keySet().stream().sorted((g1, g2)->{
							  int c1 = compare(g1, g2, cntPathogenicPerGene);
							  if(c1!=0) return c1;
							  else return compare(g1, g2, cntLikelyPathogenicPerGene);
						  }).map(gene-> positive(patient, gene, groupByGene.get(gene), documents, hasMaf))
						  .collect(Collectors.joining("\r\n"));
	}
	private static int compare(String g1, String g2, Map<String, Long> cnt) {
		if(cnt.containsKey(g1)) {
			if(cnt.containsKey(g2)) return Long.compare(cnt.get(g2), cnt.get(g1));
			else return -1;
		} else if(cnt.containsKey(g2)) return 1;
		else return 0;
	}
	private String positive(Patient patient, String gene, List<Variant> variants, Map<Variant, Document> documents, AtomicBoolean hasMaf) {
		Patient.Sex sex = patient.sex();
		return variants.stream().sorted(Comparator.comparing(v->Phrase.ordinalClass(v.clazz())))
				.map(v->comment(gene, v, documents.get(v), sex, hasMaf))
				.filter(Objects::nonNull)
				.collect(Collectors.joining("\r\n"));
	}
	private Variant normalize(Patient.Sex sex, Variant variant, Document values) {
		if(variant.hgvsc() == null || variant.hgvsp() == null) throw new IllegalArgumentException();
		if(variant.hgvsc().contains(":")) {
			variant.hgvsc(variant.hgvsc().substring(variant.hgvsc().indexOf(":") + 1).trim());
		}
		String chrom = values.getString("chrom");
		String genotype = values.getString("genotype");
		String zygosity = Phrase.zygosity(genotype, sex, chrom);
		String zygosityShort = Phrase.zygosityShort(zygosity);
		if (variant.zygosity() == null || variant.zygosity().trim().isEmpty() || variant.zygosity().contains("0") || variant.zygosity().contains("1"))
			variant.zygosity(zygosityShort);
		return variant;
	}
	private String comment(String gene, Variant variant, Document values, Patient.Sex sex, AtomicBoolean hasMaf) {
		if(!positive(variant.clazz())) return null;
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
		try {
			boolean hasSift = hasValue(values, SIFT_PRED_KEY, "", ".");
			boolean hasPoly = hasValue(values, POLY_PRED_KEY, "", ".");
			boolean hasMuta = hasValue(values, MUT_PRED_KEY, "", ".");
			boolean hasInsilico = hasSift & hasPoly & hasMuta;
			if(!hasInsilico) return "";

			boolean sift = !values.getString(SIFT_PRED_KEY).toLowerCase().contains("tolerated");	// tolerated, tolerated_low_confidence
			Boolean polyphen = !"benign".equalsIgnoreCase(values.getString(POLY_PRED_KEY));			// benign
			if("unknown".equalsIgnoreCase(values.getString(POLY_PRED_KEY))) polyphen = null;
			boolean mutationtaster = (values.getString(MUT_PRED_KEY).charAt(0) == 'D') || (values.getString(MUT_PRED_KEY).charAt(0) == 'A');
			StringBuilder sb = new StringBuilder();
			if(sift & (polyphen!=null && polyphen) & mutationtaster) sb.append("in-silico prediction (SIFT, PolyPhen, MutationTaster)에서는 Deleterious로 예측되었습니다.");
			else if(!sift & (polyphen!=null && !polyphen) & !mutationtaster) sb.append("in-silico prediction (SIFT, PolyPhen, MutationTaster)에서는 Tolerated로 예측되었습니다.");
			else if(sift & polyphen==null & mutationtaster) sb.append("in-silico prediction (SIFT, MutationTaster)에서는 Deleterious로 예측되었습니다.");
			else if(!sift & polyphen==null & !mutationtaster) sb.append("in-silico prediction (SIFT, MutationTaster)에서는 Tolerated로 예측되었습니다.");
			else {
				sb.append("in-silico prediction에서는 서로 다르게 예측되었습니다(");
				List<String> deleteriouses = new LinkedList<>();
				List<String> benignes = new LinkedList<>();
				if(sift) deleteriouses.add("SIFT");
				else benignes.add("SIFT");
				if(polyphen!=null && polyphen) deleteriouses.add("PolyPhen");
				else if(polyphen!=null) benignes.add("PolyPhen");
				if(mutationtaster) deleteriouses.add("MutationTaster");
				else benignes.add("MutationTaster");
				sb.append(deleteriouses.stream().collect(Collectors.joining(", "))).append(": Deleterious, ")
						.append(benignes.stream().collect(Collectors.joining(", "))).append(": Tolerated).");
			}
			return sb.toString();
		} catch(Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	@Override
	@Transactional(readOnly = true)
	public com.greencross.lims.dto.interpretation.GenomeScreen negative(long sample, String service) {
		 return negative(sample, service, null, null);
	}
	private com.greencross.lims.dto.interpretation.GenomeScreen negative(long sample, String service, String meanDepth, String coverage) {
		Patient patient = dao.em().find(Sample.class, sample).patient();
		return negative(patient, sample, service, new Variant[0], meanDepth, coverage);
	}
	private com.greencross.lims.dto.interpretation.GenomeScreen negative(Patient patient, long sample, String service, Variant[] variants, String meanDepth, String coverage) {
		var test = test(service);
		Map<Variant, Document> documents = Arrays.stream(variants).collect(Collectors.toMap(Functions.identity(), v->{
			String batch = v.analysis().split(":", -1)[1];
			return snvRepo.find(batch, v.analysis() + ":" + v.snv(), Document.class);
		}));
		for(Variant var: variants) normalize(patient.sex(), var, documents.get(var));
		com.greencross.lims.dto.interpretation.GenomeScreen.Disease[] diseases = Arrays.stream(test.diseases()).map(d->{
			com.greencross.lims.dto.interpretation.GenomeScreen.Gene[] genes = Arrays.stream(d.genes()).map(g->
							new com.greencross.lims.dto.interpretation.GenomeScreen.Gene().name(g.gene()).value(false))
					.toArray(com.greencross.lims.dto.interpretation.GenomeScreen.Gene[]::new);
			return new com.greencross.lims.dto.interpretation.GenomeScreen.Disease().name(d.name()).values(genes);
		}).toArray(com.greencross.lims.dto.interpretation.GenomeScreen.Disease[]::new);
		String interpretation = "";
		if("N101".equalsIgnoreCase(service) || "N111".equalsIgnoreCase(service)) interpretation = "유전성 암 관련 염기서열을 분석한 결과, 유전성 암과 관련된 유전자에서 병원성 변이(PV)가 발견되지 않았습니다. 따라서 유전성 암의 위험성은 증가하지 않으나 비유전성 원인으로 인한 암 발생 위험성은 여전히 존재합니다. 암 예방을 위한 꾸준한 건강관리와 정기적인 암 검진이 권고됩니다.";
		return new com.greencross.lims.dto.interpretation.GenomeScreen().summary(summaryNegative(test))
				.interpretation(interpretation).variants(variants)
				.diseases(diseases).meanDepth(meanDepth).coverage(coverage);
	}

	private String testName(TestInfo test) {
		if (TestWithRiskScreen.N087 == test ||
				TestWithRiskScreen.J020 == test) 		return "뇌졸중 지놈 스크린";
		else if(TestWithRiskScreen.N088 == test ||
				TestWithRiskScreen.J021 == test)	return "고지혈증 지놈 스크린";
		else if(TestInfo.N089 == test ||
				TestInfo.J019 == test ||
				TestInfo.N112 == test ||
				TestInfo.N185 == test) 				return "심장 돌연사 지놈 스크린";
		else if(TestInfo.N090 == test ||
				TestInfo.J018 == test)				return "암 지놈 스크린";
		else if(TestInfo.N101 == test ||
				TestInfo.N111 == test)				return "";
		else return null;
	}
	private String diseaseT(TestInfo test) {
		if(TestWithRiskScreen.N087 == test ||
		TestWithRiskScreen.J020 == test) 		return "유전성 뇌졸중";
		else if(TestWithRiskScreen.N088 == test ||
				TestWithRiskScreen.J021 == test)	return "유전성 고지혈증";
		else if(TestInfo.N089 == test ||
				TestInfo.J019 == test ||
				TestInfo.N112 == test ||
				TestInfo.N185 == test) 				return "심장 돌연사";
		else if(TestInfo.N090 == test ||
				TestInfo.J018 == test ||
				TestInfo.N101 == test ||
				TestInfo.N111 == test)				return "유전성 암";
		else return null;
	}
	private GenomeScreenTemplate.Disease[] disease(TestInfo test) {
		if(TestWithRiskScreen.N087 == test ||
				TestWithRiskScreen.J020 == test) 		return GenomeScreenTemplateN087.DiseaseN087.values();
		else if(TestWithRiskScreen.N088 == test ||
				TestWithRiskScreen.J021 == test)	return GenomeScreenTemplateN088.DiseaseN088.values();
		else if(TestInfo.N089 == test ||
				TestInfo.J019 == test ||
				TestInfo.N112 == test ||
				TestInfo.N185 == test) 				return GenomeScreenTemplateN089.DiseaseN089.values();
		else if(TestInfo.N090 == test ||
				TestInfo.J018 == test)				return GenomeScreenTemplateN090.DiseaseN090.values();
		else if(TestInfo.N101 == test ||
				TestInfo.N111 == test) 				return GenomeScreenTemplateN101.DiseaseN101.values();
		else return null;
	}
	private static final String NEGATIVE = "수검자의 %t 검사 결과\n%d 관련된 유전자에서\n병원성 변이(PV)가 발견되지 않았습니다.";
	private String summaryNegative(TestInfo test) {
		String template = NEGATIVE;
		if(TestInfo.N101 == test || TestInfo.N111 == test) template = "%d 관련된 유전자에서\n병원성 변이(PV)가 발견되지 않았습니다.";	// 캔서진스크린
		return template.replace("%t", testName(test)).replace("%d", Korean.한글조사(diseaseT(test), "와", "과"));
	}
	private static final String POSITIVE = "수검자의 %t 검사 결과\n%d 관련된 %g 유전자에서\n병원성 변이(PV)가 발견(Detected) 되었습니다.";
	private String summaryPositive(TestInfo test, Variant[] variants) {
		String template = POSITIVE;
		if(TestInfo.N101 == test || TestInfo.N111 == test) template = "%d 관련된 %g 유전자에서\n병원성 변이(PV)가 발견(Detected) 되었습니다.";	// 캔서진스크린
		List<String> h = Arrays.stream(variants).map(Variant::gene).collect(Collectors.toList());
		GenomeScreenTemplate.Disease[] diseases = disease(test);
		String diseaseNames = Arrays.stream(diseases)
				.filter(d->Arrays.stream(d.subs())
						.map(GenomeScreenTemplate.DiseaseSub::genes)
						.flatMap(Arrays::stream)
						.map(GenomeScreenTemplate.Gene::name)
						.anyMatch(h::contains))
				.distinct().map(this::diseaseName)
				.collect(Collectors.joining(", "));
		String genes = h.stream().distinct().collect(Collectors.joining(", "));
		return template.replace("%t", testName(test)).replace("%d", Korean.한글조사(diseaseNames, "와", "과")).replace("%g", genes);
	}

	private String diseaseName(GenomeScreenTemplate.Disease disease) {
		if(disease == GenomeScreenTemplateN087.DiseaseN087.대동맥과_소동맥_폐핵으로_인한_허혈성_뇌졸중)	return "대동맥과 소동맥 폐핵으로 인한 허혈성 뇌졸중";
		if(disease == GenomeScreenTemplateN087.DiseaseN087.소동맥_폐색으로_인한_허혈성_뇌졸중)			return "소동맥 폐색으로 인한 허혈성 뇌졸중";
		if(disease == GenomeScreenTemplateN087.DiseaseN087.심장_색전성_뇌졸중)						return "심장 색전성 뇌졸중";
		if(disease == GenomeScreenTemplateN087.DiseaseN087.기타_원인으로_인한_허혈성_뇌졸중)			return "기타 원인으로 인한 허혈성 뇌졸중";
		if(disease == GenomeScreenTemplateN087.DiseaseN087.뇌혈관질환_편두통)							return "뇌혈관질환, 편두통";
		if(disease == GenomeScreenTemplateN087.DiseaseN087.모야모야병)								return "모야모야병";
		if(disease == GenomeScreenTemplateN087.DiseaseN087.뇌내출혈)									return "뇌내출혈";
		if(disease == GenomeScreenTemplateN087.DiseaseN087.고콜레스테롤혈증)							return "고콜레스테롤혈증";
		if(disease == GenomeScreenTemplateN087.DiseaseN087.혈전증)									return "혈전증";

		if(disease == GenomeScreenTemplateN088.DiseaseN088.고콜레스테롤혈증)							return "고콜레스테롤혈증";
		if(disease == GenomeScreenTemplateN088.DiseaseN088.약물적합도)								return "약물적합도";
		if(disease == GenomeScreenTemplateN088.DiseaseN088.지질단백결핍)								return "지질단백결핍";
		if(disease == GenomeScreenTemplateN088.DiseaseN088.복합형_이상지질혈증)						return "복합형 이상지질혈증";
		if(disease == GenomeScreenTemplateN088.DiseaseN088.고중성지방혈증)							return "고중성지방혈증";

		if(disease == GenomeScreenTemplateN089.DiseaseN089.흉부대동맥류와_박리증) 						return "흉부대동맥류와 박리증";
		if(disease == GenomeScreenTemplateN089.DiseaseN089.유전성_부정맥)								return "유전성 부정맥";
		if(disease == GenomeScreenTemplateN089.DiseaseN089.유전성_심근병증)							return "유전성 심근병증";
		if(disease == GenomeScreenTemplateN089.DiseaseN089.고콜레스테롤혈증_및_혈전증)					return "고콜레스테롤혈증 및 혈전증";
		if(disease == GenomeScreenTemplateN089.DiseaseN089.동맥비틀림증후군)							return "동맥비틀림증후군";

		if(disease == GenomeScreenTemplateN090.DiseaseN090.유전성_유방암_난소암_증후군)				return "유전성 유방암-난소암 증후군";
		if(disease == GenomeScreenTemplateN090.DiseaseN090.유방암_감수성)								return "유방암 감수성";
		if(disease == GenomeScreenTemplateN090.DiseaseN090.난소암_감수성)								return "난소암 감수성";
		if(disease == GenomeScreenTemplateN090.DiseaseN090.전립선암_감수성)							return "전립선암 감수성";
		if(disease == GenomeScreenTemplateN090.DiseaseN090.리_프라우메니_증후군)						return "리-프라우메니증후군";
		if(disease == GenomeScreenTemplateN090.DiseaseN090.포이츠_제거스_증후군)						return "포이츠-제거스증후군";
		if(disease == GenomeScreenTemplateN090.DiseaseN090.린치_증후군)								return "린치증후군";
		if(disease == GenomeScreenTemplateN090.DiseaseN090.용종증_증후군)								return "용종증 증후군";
		if(disease == GenomeScreenTemplateN090.DiseaseN090.폰히펠_린다우_증후군)						return "폰히펠-린다우 증후군";
		if(disease == GenomeScreenTemplateN090.DiseaseN090.다발성_내분비선종증)						return "다발성 내분비선종증";
		if(disease == GenomeScreenTemplateN090.DiseaseN090.PTEN_과오종_증후군)						return "PTEN 과오종 증후군";
		if(disease == GenomeScreenTemplateN090.DiseaseN090.망막모세포종)								return "망막모세포종";
		if(disease == GenomeScreenTemplateN090.DiseaseN090.유전성_부신경절종_갈색세포종)				return "유전성 부신경절종-갈색세포종";
		if(disease == GenomeScreenTemplateN090.DiseaseN090.결절성_경화증)								return "결절성 결화증";
		if(disease == GenomeScreenTemplateN090.DiseaseN090.WT1_연관_윌름스_종양)						return "WT1 연관 윌름스 종양";
		if(disease == GenomeScreenTemplateN090.DiseaseN090.제2형_신경섬유종증)						return "제2형 신경섬유종증";

		if(disease == GenomeScreenTemplateN101.DiseaseN101.유전성_유방암_난소암_증후군)				return "유전성 유방암-난소암 증후군";
		if(disease == GenomeScreenTemplateN101.DiseaseN101.리_프라우메니_증후군)						return "리-프라우메니증후군";
		return null;
	}
}
