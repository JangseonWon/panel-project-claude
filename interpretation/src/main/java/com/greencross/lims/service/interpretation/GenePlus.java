package com.greencross.lims.service.interpretation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gcgenome.lims.test.geneplus.TestInfo;
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
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

@Component
public class GenePlus implements Interpretable {
	private final InterpretationDAO dao;
	private final SnvDAO snvRepo;
	private final ObjectMapper om;
	public GenePlus(InterpretationDAO dao, SnvDAO snvRepo, ObjectMapper om) {
		this.dao = dao;
		this.snvRepo = snvRepo;
		this.om = om;
	}

	@Override
	public boolean chk(long sample, String service) {
		for(TestInfo test: TestInfo.TESTS_ETC) if(test.code().equalsIgnoreCase(service)) return true;
		return false;
	}

	protected TestInfo test(String service) {
		return Arrays.stream(TestInfo.TESTS_ETC).filter(t->t.code().equalsIgnoreCase(service)).findFirst().get();
	}

	@Override
	@Transactional(readOnly = true)
	public Mono<Object> interpret(long sample, String service, InterpretationParam param) {
		TestInfo test = test(service);
		Patient patient = dao.em().find(Sample.class, sample).patient();
		var prev = om.convertValue(param.json(), PanelTest.class);
		if(test.referralDefault()!=null) prev.reasonForReferral("R/O " + test.referralDefault());;
		AtomicBoolean hasMaf = new AtomicBoolean(false);
		if(prev.variants()!=null) prev.variants(Arrays.stream(prev.variants())
													  .filter(c->reportable(c.clazz()))
													  .toArray(PanelTest.Variant[]::new));
		if(prev.variants()==null || prev.variants().length <= 0) {
			StringBuilder resultText = new StringBuilder().append("질환과 관련된 변이는 발견되지 않았습니다.");
			if(prev.addendum()!=null && prev.addendum().variants()!=null && prev.addendum().variants().length > 0) resultText.append(" [ADDENDUM RESULT 참조]");
			prev.result("NEGATIVE").resultText(resultText.toString());
			prev.interpretation(NEGATIVE_FORMAT.replace("%p", String.format("%s 유전자", test.gene())));
			if(TestInfo.N121 == test) prev.interpretation(prev.interpretation() + "\r\n\r\n" + POSTFIX_LDLR_FORMAT);
		} else {
			List<PanelTest.Variant> variants = new LinkedList<>(Arrays.asList(prev.variants()));
			List<PanelTest.Variant> pv = variants.stream().filter(v->"P".equalsIgnoreCase(v.clazz()) || "PV".equalsIgnoreCase(v.clazz())).collect(Collectors.toList());
			List<PanelTest.Variant> lpv = variants.stream().filter(v->"LP".equalsIgnoreCase(v.clazz()) || "LPV".equalsIgnoreCase(v.clazz())).collect(Collectors.toList());
			List<PanelTest.Variant> vus = variants.stream().filter(v->"VUS".equalsIgnoreCase(v.clazz())).collect(Collectors.toList());
			StringBuilder resultText = new StringBuilder();
			if(pv.size() <= 0 && lpv.size() <= 0) prev.result("INCONCLUSIVE");
			else prev.result("POSITIVE");
			if(pv.size() == 1 && lpv.size()<=0 && vus.size()<=0) resultText.append("PV가 발견되었습니다.");
			else if(pv.size() <= 0 && lpv.size() == 1 && vus.size()<=0) resultText.append("LPV가 발견되었습니다.");
			else if(pv.size() <= 0 && lpv.size() <= 0 && vus.size()==1) resultText.append("VUS가 발견되었습니다.");
			else {
				List<String> comments = new LinkedList<>();
				if(pv.size() > 0) comments.add(String.format("PV %d개", pv.size()));
				if(lpv.size() > 0) comments.add(String.format("LPV %d개", lpv.size()));
				if(vus.size() > 0) comments.add(String.format("VUS %d개", vus.size()));
				resultText.append(String.join("와 ", comments)).append("가 발견되었습니다.");
			}
			if(prev.addendum()!=null && prev.addendum().variants()!=null && prev.addendum().variants().length > 0) resultText.append(" [ADDENDUM RESULT 참조]");
			prev.resultText(resultText.toString());

			List<String> comments = new LinkedList<>();
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
				variant.disease(diseases.stream().map(InterpretationParam.InterpretationParamDisease::abbreviation).collect(Collectors.joining(", ")));
				variant.inheritance(diseases.stream().map(InterpretationParam.InterpretationParamDisease::inheritance).flatMap(Collection::stream).distinct().collect(Collectors.joining(", ")));
				Document values = snvRepo.find(batch,analysis + ":" + snv, Document.class);
				String abbrZyg = abbreviationZygosity(patient.sex(), variant, values);
				abbreviationReference.add(abbreviationReference(variant));
				for(InterpretationParam.InterpretationParamDisease info: diseases) abbreviationDisease.add(abbreviationDisease(info));
				abbreviationZygosity.add(abbrZyg);

				String comment = comment(test, variant, values, diseases, hasMaf);
				String inheritance = diseases.stream().map(InterpretationParam.InterpretationParamDisease::inheritance).flatMap(List::stream).collect(Collectors.joining(","));
				String commentSingleHeteroVusAr = commentSingleHeteroVusAr(variant, prev.variants(), abbrZyg.split(",")[0].trim(), inheritance);
				comments.add(comment + commentSingleHeteroVusAr);
				String[] inhs = abbreviationInheritance(variant, diseases);
				if(inhs!=null) abbreviationInheritance.addAll(Arrays.asList(inhs));
				abbreviationClass.add(abbreviationClass(variant));
			}
			if(param.suffix()!=null && !param.suffix().trim().isEmpty()) comments.add(param.suffix());
			if(TestInfo.N121 == test) comments.add(POSTFIX_LDLR_FORMAT);
			String abbrReference = abbreviationReference.stream().filter(Objects::nonNull).filter(Predicate.not(String::isBlank)).distinct().collect(Collectors.joining("; "));
			String abbrDisease = abbreviationDisease.stream().filter(Objects::nonNull).filter(Predicate.not(String::isBlank)).distinct().collect(Collectors.joining("; "));
			String abbr = Stream.concat(Stream.concat(abbreviationZygosity.stream(), abbreviationInheritance.stream()), abbreviationClass.stream())
								.filter(Objects::nonNull).filter(Predicate.not(String::isBlank)).distinct().sorted().collect(Collectors.joining("; "));
			String header = "%p 분석 결과, ".replace("%p", String.format("%s 유전자", test.gene())) + summary(test, variants);
			prev.abbreviationReference(abbrReference).abbreviationDisease(abbrDisease).abbreviation(abbr)
				.interpretation(header + "\r\n" + String.join("\r\n\r\n", comments));
		}
		if(prev.addendum()==null || prev.addendum().variants()==null || prev.addendum().variants().length <= 0) prev.addendum(null);
		else {
			List<PanelTest.Variant> variants = Arrays.stream(prev.addendum().variants())
													 .filter(c -> reportable(c.clazz()))
													 .collect(Collectors.toCollection(LinkedList::new));
			String summary = "추가로 분석된 " + summaryExt(variants);
			prev.addendum().resultText(summary);

			List<String> comments = new LinkedList<>();
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
				variant.disease(diseases.stream().map(InterpretationParam.InterpretationParamDisease::abbreviation).collect(Collectors.joining(", ")));
				variant.inheritance(diseases.stream().map(InterpretationParam.InterpretationParamDisease::inheritance).flatMap(Collection::stream).distinct().collect(Collectors.joining(", ")));
				Document values = snvRepo.find(batch,analysis + ":" + snv, Document.class);
				String abbrZyg = abbreviationZygosity(patient.sex(), variant, values);
				abbreviationReference.add(abbreviationReference(variant));
				for(InterpretationParam.InterpretationParamDisease info: diseases) abbreviationDisease.add(abbreviationDisease(info));
				abbreviationZygosity.add(abbrZyg);

				String comment = comment(test, variant, values, diseases, hasMaf);
				String inheritance = diseases.stream().map(InterpretationParam.InterpretationParamDisease::inheritance).flatMap(List::stream).collect(Collectors.joining(","));
				String commentSingleHeteroVusAr = commentSingleHeteroVusAr(variant, prev.addendum().variants(), abbrZyg.split(",")[0].trim(), inheritance);
				comments.add(comment + commentSingleHeteroVusAr);
				String[] inhs = abbreviationInheritance(variant, diseases);
				if(inhs!=null) abbreviationInheritance.addAll(Arrays.asList(inhs));
				abbreviationClass.add(abbreviationClass(variant));
			}
			String abbrReference = abbreviationReference.stream().filter(Objects::nonNull).filter(Predicate.not(String::isBlank)).distinct().collect(Collectors.joining("; "));
			String abbrDisease = abbreviationDisease.stream().filter(Objects::nonNull).filter(Predicate.not(String::isBlank)).distinct().collect(Collectors.joining("; "));
			String abbr = Stream.concat(Stream.concat(abbreviationZygosity.stream(), abbreviationInheritance.stream()), abbreviationClass.stream())
								.filter(Objects::nonNull).filter(Predicate.not(String::isBlank)).distinct().sorted().collect(Collectors.joining("; "));
			prev.addendum().abbreviationReference(abbrReference).abbreviationDisease(abbrDisease).abbreviation(abbr)
				.interpretation(String.join("\r\n\r\n", comments));
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
		return "VUS".equals(clazz);
	}

	private static final String NEGATIVE_FORMAT = "%p 결과, 질환과 관련된 변이는 발견되지 않았습니다.\n\n" +
												   "본 검사는 검사에 포함된 유전자들의 coding exon과 인접 intron 영역을 분석하며, deep intronic variant, exon " +
												   "deletion/duplication, genomic rearrangement를 포함한 copy number variant 검출은 제한적입니다. " +
												   "검사의 기술적 한계에 대한 자세한 내용은 검사의 한계를 참조하십시오.";
	private static final String POSTFIX_LDLR_FORMAT = "[추가소견]\r\n" +
													  "APOB, LDLRAP1, PCSK9 유전자의 모든 exon과 인접 intron의 염기서열을 분석한 결과, 질환 관련 변이는 발견되지 않았습니다.";

	@Override
	public Object negative(long sample, String service) {
		TestInfo test = test(service);
		var interpretation = new PanelTest();
		if(test.referralDefault()!=null) interpretation.reasonForReferral("R/O " + test.referralDefault());
		interpretation.result("NEGATIVE").resultText("질환과 관련된 변이는 발견되지 않았습니다.");
		interpretation.interpretation(NEGATIVE_FORMAT.replace("%p", String.format("%s 유전자", test.gene())));
		if(TestInfo.N121 == test) interpretation.interpretation(interpretation.interpretation() + "\r\n\r\n" + POSTFIX_LDLR_FORMAT);
		return interpretation;
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
		String chrom = values.getString("chrom");
		String genotype = values.getString("genotype");
		String zygosity = Phrase.zygosity(genotype, sex, chrom);
		String zygosityShort = Phrase.zygosityShort(zygosity);
		if(variant.zygosity()==null || variant.zygosity().trim().isEmpty()
				   || variant.zygosity().contains("0") || variant.zygosity().contains("1")) variant.zygosity(zygosityShort);
		return zygosityShort + ", " + zygosity;
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

	private void appendVariantsToSummary(TestInfo test, Set<String> genes, String clazz, StringBuilder sb){
		if(genes.size()!=1 || !test.gene().equals(genes.stream().findFirst().get())) {
			sb.append(String.join(", ", genes));
			sb.append(" 유전자에서 ");
		}
		sb.append(clazz);
		sb.append("가 발견되었습니다.\n");
	}
	public String summary(TestInfo test, List<PanelTest.Variant> variants){
		Map<String, Set<String>> hits = variants.stream()
				.collect(Collectors.groupingBy(PanelTest.Variant::clazz,
						Collectors.mapping(PanelTest.Variant::gene, toSet())));
		StringBuilder sb = new StringBuilder();
		Set<String> PVs = hits.getOrDefault("PV", Set.of());
		if(!PVs.isEmpty()) appendVariantsToSummary(test, PVs, "PV", sb);
		Set<String> LPVs = hits.getOrDefault("LPV", Set.of());
		if(!LPVs.isEmpty()) appendVariantsToSummary(test, LPVs, "LPV", sb);
		Set<String> VUSs = hits.getOrDefault("VUS", Set.of());
		if(!VUSs.isEmpty()) appendVariantsToSummary(test, VUSs, "VUS", sb);
		return sb.toString();
	}
	private String summaryExt(List<PanelTest.Variant> variants) {
		Map<String, Set<String>> hit = variants.stream()
				.collect(Collectors.groupingBy(PanelTest.Variant::gene,
						Collectors.mapping(PanelTest.Variant::clazz, toSet())));
		Set<String> proven = new HashSet<>();
		StringBuilder sb = new StringBuilder();
		sb.append(
				variants.stream()
						.filter(v->!proven.contains(v.gene())).peek(v->proven.add(v.gene()))
						.map(v->"%g 유전자에서 ".replace("%g", v.gene()) + summaryGene(hit.get(v.gene())))
						.collect(Collectors.joining(" 발견되었고, "))
		).append(" 발견되었습니다.");
		return sb.toString();
	}
	private String summaryGene(Set<String> classes) {
		StringBuilder sb = new StringBuilder();
		String acmgs = classes.stream().sorted(Comparator.comparing(Phrase::ordinalClass))
							  .map(Phrase::classShort)
							  .collect(Collectors.joining("와 "));
		sb.append(acmgs).append("가");
		return sb.toString();
	}
	private String comment(TestInfo test, PanelTest.Variant variant, Document values, List<InterpretationParam.InterpretationParamDisease> diseases, AtomicBoolean ms) {
		String commentHgvs = commentHgvs(variant, values);
		String commentPopulation = commentPopulation(test, variant, values, ms);
		String commentInsilico = commentInsilico(test, variant, values);
		if(commentInsilico==null || commentInsilico.trim().isEmpty()) commentPopulation += "변이입니다. ";
		else commentPopulation += "변이로, ";
		String paragraph1 = List.of(commentHgvs, commentPopulation, commentInsilico).stream().filter(s->!s.trim().isEmpty()).collect(Collectors.joining(" "));
		String commentClinvarHgmd = commentClinvarHgmd(test, variant, values);
		String commentDisease = commentDisease(test, variant, diseases);
		String phrase1 =  List.of(paragraph1, commentClinvarHgmd).stream().filter(s->!s.trim().isEmpty()).collect(Collectors.joining(" "));
		if(!commentDisease.trim().isEmpty()) return phrase1 + "\r\n" + commentDisease;
		else return phrase1;
	}
	private static final String GENE_KEY = "gene.refgene";
	private String commentHgvs(PanelTest.Variant variant, Document values) {
		try {
			String gene = values.getString(GENE_KEY);
			String hgvsc = variant.hgvsc();
			String hgvsp = variant.hgvsp();
			boolean hasHgvsc = hgvsc!=null && !hgvsc.trim().equals(".") && !hgvsc.trim().equals("");
			boolean hasHgvsp = hgvsp!=null && !hgvsp.trim().equals(".") && !hgvsp.trim().equals("");
			if (hasHgvsc & hasHgvsp) {
				if (hgvsc.contains(":")) hgvsc = hgvsc.substring(hgvsc.indexOf(":") + 1).trim();
				return gene + " 유전자의 " + hgvsc + " (" + hgvsp + ") 변이는";
			} else if (hasHgvsc) {
				if (hgvsc.contains(":")) hgvsc = hgvsc.substring(hgvsc.indexOf(":") + 1).trim();
				return gene + " 유전자의 " + hgvsc + " 변이는";
			} else return gene + " 유전자의 변이는";
		} catch(Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	private static final String GNOMAD_KEY = "gnomad.total";
	private static final String KRGDB_KEY = "krgdb_af";
	private String commentPopulation(TestInfo test, PanelTest.Variant variant, Document values, AtomicBoolean hasPreviousPopulationComment) {
		try {
			boolean hasGnomad = hasValue(values, GNOMAD_KEY, "", ".", "0", "0.0", "0.00", "0.000", "0.0000");
			boolean hasKrgdb = hasValue(values, KRGDB_KEY, "", ".", "0", "0.0", "0.00", "0.000", "0.0000");
			String maf = hasPreviousPopulationComment.get()?"MAF":"minor allele frequency (MAF)";
			if(hasGnomad & hasKrgdb) {
				hasPreviousPopulationComment.set(true);
				return String.format("전체 인구집단(gnomAD)에서의 " + maf + "가 %s%%이며 한국인 인구집단(KRGDB)에서는 %s%%인 ", percentage(values.getString(GNOMAD_KEY)), percentage(values.getString(KRGDB_KEY)));
			} else if(hasGnomad) {
				hasPreviousPopulationComment.set(true);
				return String.format("전체 인구집단(gnomAD)에서의 " + maf + "가 %s%%이며 한국인 인구집단(KRGDB)에서는 보고된 바 없는 ", percentage(values.getString(GNOMAD_KEY)));
			} else if(hasKrgdb) {
				hasPreviousPopulationComment.set(true);
				return String.format("전체 인구집단(gnomAD)에서 보고된 바 없으나  한국인 인구집단(KRGDB)에서의 " + maf + "는 %s%%인 ", percentage(values.getString(KRGDB_KEY)));
			} else return "일반 인구집단(gnomAD, KRGDB)에서 보고된 바 없는 매우 드문 ";
		} catch(Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	private String percentage(String s) {
		if(s == null) return null;
		double number = Double.parseDouble(s) * 100;
		if(number < 0.0001) return String.format("%f", number);
		else return String.format("%.4f", number);
	}
	private static final String SIFT_PRED_KEY = "sift.pred";
	private static final String POLY_PRED_KEY = "polyphen.pred";
	private static final String MUT_PRED_KEY = "mutationtaster.pred";
	private String commentInsilico(TestInfo test, PanelTest.Variant variant, Document values) {
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
	private String commentClinvarHgmd(TestInfo test, PanelTest.Variant variant, Document values) {
		try {
			String hgmd = commentHgmd(test, variant, values);
			String clinvar = commentClinvar(test, variant, values);
			if(hgmd.length() > 0) {
				if(clinvar.length() > 0) return "이 변이는 " + hgmd.replace("보고된 바 있습니다(", "보고된 바 있으며(") + ", " + clinvar + ".";
				else return "이 변이는 " + hgmd + ".";
			} else if(clinvar.length() > 0) return "이 변이는 " + clinvar + ".";
			else return "";
		} catch(Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	private static final String CLINVAR_CLASS_KEY = "clinvar.class";
	private static final String CLINVAR_ID_KEY = "clinvar.id";
	private String commentClinvar(TestInfo test, PanelTest.Variant variant, Document values) {
		try {
			boolean hasClinvar = hasValue(values, CLINVAR_CLASS_KEY, "", ".");
			if(!hasClinvar) return "";
			String clinvar = values.getString(CLINVAR_CLASS_KEY);
			clinvar = clinvar.replace("Uncertain_significance", "VUS");
			clinvar = clinvar.replace("Likely_pathogenic", "LPV");
			clinvar = clinvar.replace("Likely_Pathogenic", "LPV");
			clinvar = clinvar.replace("Pathogenic", "PV");
			clinvar = clinvar.replace("Likely_benign", "LBV");
			clinvar = clinvar.replace("Likely_Benign", "LBV");
			clinvar = clinvar.replace("Benign", "BV");
			if(clinvar.contains("Conflicting")) {
				clinvar = clinvar.substring(clinvar.indexOf('|') + 1);
				clinvar = clinvar.replace("&", " 또는 ");
			}
			clinvar = clinvar.replace("_", " ");
			String id = values.getString(CLINVAR_ID_KEY);
			return "ClinVar에서 "  + clinvar + "로 분류되어 있습니다 (ID: " + id + ")";
		} catch(Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	private static final String HGMD_KEY = "hgmd.tag";
	private static final String HGMD_DISEASE_KEY = "hgmd.codon.disease";
	private static final String HGMD_ID_KEY = "hgmd.pmid";
	private String commentHgmd(TestInfo test, PanelTest.Variant variant, Document values) {
		try {
			boolean hasHgmd = hasValue(values, HGMD_KEY, "", ".");
			if(!hasHgmd) return "";
			String id = unwrap(values.getString(HGMD_ID_KEY));
			return "기존에 " + variant.disease() + " 환자에서 보고된 바 있습니다(PMID: " + id + ")";
		} catch(Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	private static final String MIM_DISEASE_KEY = "mim.disease";
	private static final String MIM_INHERITANCE_KEY = "mim.inheritance";
	private String commentDisease(TestInfo test, PanelTest.Variant variant, List<InterpretationParam.InterpretationParamDisease> diseases) {
		try {
			boolean hasDisease = diseases!=null && !diseases.isEmpty();
			StringBuilder sb = new StringBuilder(variant.gene()).append(" 유전자는 ");
			if(hasDisease) {
				String disease = diseases.stream().map(InterpretationParam.InterpretationParamDisease::abbreviation).collect(Collectors.joining(", "));
				String inheritance = diseases.stream().map(InterpretationParam.InterpretationParamDisease::inheritance)
						.flatMap(Collection::stream).distinct().map(Phrase::inheritanceKor).collect(Collectors.joining(" 또는 "));
				if(inheritance.trim().isEmpty()) return sb.append(disease).append(" 관련 유전자로 알려져 있습니다.").toString();
				else return sb.append(disease).append(" 관련 유전자로 ").append(inheritance).append(" 유전 양상을 보입니다.").toString();
			} else return "";
		} catch(Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	private String commentSingleHeteroVusAr(PanelTest.Variant target, PanelTest.Variant[] variants, String zygosity, String inheritance) {
		try {
			if(!"VUS".equalsIgnoreCase(target.clazz())) return "";
			if(!"Het".equalsIgnoreCase(zygosity)) return "";
			boolean hasInheritance = inheritance!=null && !inheritance.isEmpty();
			if(!hasInheritance) return "";
			if(!Objects.requireNonNull(inheritance).contains("AR")) return "";
			boolean isSingle = Arrays.stream(variants).filter(v->target!=v).noneMatch(v->v.gene().equalsIgnoreCase(target.gene()));
			if(!isSingle) return "";
			else return " 상염색체 열성 질환에서 한 개의 heterozygous VUS 변이만이 발견된 경우, 1) 이 변이가 질환과 관련이 없는 변이일 가능성, " +
								"2) 실제 질환과 관련이 있지만 보인자일 가능성, 3) 실제 이 유전자와 관련된 질환을 가지고 있지만 나머지 하나의 대립 유전자가 " +
								"본 검사로 검출되지 않는 종류의 변이(large deletion or insertion, deep intronic variant, etc.)를 동반할 가능성 등이 있습니다.";
		} catch(Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	private boolean hasValue(Document doc, String key, Object... empties) {
		if(!doc.containsKey(key)) return false;
		if(empties!=null) {
			Object value = doc.get(key);
			for(Object empty: empties) if(empty.equals(value)) return false;
		}
		return true;
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
