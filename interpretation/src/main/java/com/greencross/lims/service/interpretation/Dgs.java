package com.greencross.lims.service.interpretation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gcgenome.lims.test.dgs.TestInfo;
import com.greencross.lims.dto.interpretation.InterpretationParam;
import com.greencross.lims.entity.Patient;
import com.greencross.lims.entity.Sample;
import com.greencross.lims.service.InterpretationDAO;
import com.greencross.lims.service.SnvDAO;
import com.greencross.lims.trans.Phrase;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// DES와 동일한데 param.suffix를 recommendation에 따로 입력
@Component
public class Dgs implements Interpretable {
	private final InterpretationDAO dao;
	private final SnvDAO snvRepo;
	private final ObjectMapper om;
	public Dgs(InterpretationDAO dao, SnvDAO snvRepo, ObjectMapper om) {
		this.dao = dao;
		this.snvRepo = snvRepo;
		this.om = om;
	}

	@Override
	public boolean chk(long sample, String service) {
		for(TestInfo test: TestInfo.TESTS) if(test.code().equalsIgnoreCase(service)) return "KOKR".equalsIgnoreCase(test.i18n());
		return false;
	}

	@Override
	@Transactional(readOnly = true)
	public Mono<Object> interpret(long sample, String service, InterpretationParam param) {
		Patient patient = dao.em().find(Sample.class, sample).patient();
		com.greencross.lims.dto.interpretation.Dgs prev = om.convertValue(param.json(), com.greencross.lims.dto.interpretation.Dgs.class);
		AtomicBoolean hasMaf = new AtomicBoolean(false);
		if(prev.variants()!=null) prev.variants(Arrays.stream(prev.variants())
													  .filter(c->reportable(c.clazz()))
													  .toArray(com.greencross.lims.dto.interpretation.Dgs.Variant[]::new));
		if(prev.variants()==null || prev.variants().length <= 0) {
			StringBuilder resultText = new StringBuilder().append("의뢰 사유와 관련된 변이는 발견되지 않았습니다.");
			prev.result("NEGATIVE").resultText(resultText.toString());
			prev.interpretation(NEGATIVE_FORMAT).recommendation("");
		} else {
			List<com.greencross.lims.dto.interpretation.Dgs.Variant> variants = Arrays.asList(prev.variants());

			List<String> comments = new LinkedList<>();
			List<String> abbreviationReference = new ArrayList<>();
			List<String> abbreviationDisease = new ArrayList<>();
			List<String> abbreviationZygosity = new ArrayList<>();
			List<String> abbreviationInheritance = new ArrayList<>();
			List<String> abbreviationClass = new ArrayList<>();
			for(com.greencross.lims.dto.interpretation.Dgs.Variant variant: prev.variants()) {
				String snv = variant.snv();
				if(snv == null || snv.trim().isEmpty()) continue;
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

				String comment = comment(variant, values, diseases, hasMaf);
				comments.add(comment);
				String[] inhs = abbreviationInheritance(variant, diseases);
				if(inhs!=null) abbreviationInheritance.addAll(Arrays.asList(inhs));
				abbreviationClass.add(abbreviationClass(variant));
			}
			prev.calculateResult(Arrays.asList(prev.variants()));

			String resultText = resultText(prev.result(), variants);
			if(prev.incidentalFindings()!=null && prev.incidentalFindings().variants()!=null && prev.incidentalFindings().variants().length > 0) resultText += " [추가소견 참조]";
			prev.resultText(resultText);
			String abbrReference = abbreviationReference.stream().distinct().collect(Collectors.joining("; "));
			String abbrDisease = abbreviationDisease.stream().distinct().filter(Objects::nonNull).filter(s->s.trim().length() > 0).collect(Collectors.joining("; "));
			String abbr = Stream.concat(Stream.concat(abbreviationZygosity.stream(), abbreviationInheritance.stream()), abbreviationClass.stream())
								.distinct().sorted().collect(Collectors.joining("; "));
			String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
			String header = "[" + date + "]\n" + resultHeader(variants);
			prev.abbreviationReference(abbrReference).abbreviationDisease(abbrDisease).abbreviation(abbr)
				.interpretation(header + "\r\n\r\n" + String.join("\r\n\r\n", comments));
			if(param.suffix()!=null && !param.suffix().trim().isEmpty()) prev.recommendation(param.suffix().trim());
		}
		if(prev.incidentalFindings()==null || prev.incidentalFindings().variants()==null ||
				   prev.incidentalFindings().variants().length <= 0) prev.incidentalFindings(null);
		else {
			List<com.greencross.lims.dto.interpretation.Dgs.Variant> variants = Arrays.stream(prev.incidentalFindings().variants())
											   .filter(c -> reportable(c.clazz()))
											   .collect(Collectors.toCollection(LinkedList::new));
			String summary = "추가로 분석된 " + resultHeader(variants);
			prev.incidentalFindings().resultText(summary);

			List<String> comments = new LinkedList<>();
			List<String> abbreviationReference = new ArrayList<>();
			List<String> abbreviationDisease = new ArrayList<>();
			List<String> abbreviationZygosity = new ArrayList<>();
			List<String> abbreviationInheritance = new ArrayList<>();
			List<String> abbreviationClass = new ArrayList<>();
			for(com.greencross.lims.dto.interpretation.Dgs.Variant variant: prev.incidentalFindings().variants()) {
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

				String comment = comment(variant, values, diseases, hasMaf);
				comments.add(comment);
				String[] inhs = abbreviationInheritance(variant, diseases);
				if(inhs!=null) abbreviationInheritance.addAll(Arrays.asList(inhs));
				abbreviationClass.add(abbreviationClass(variant));
			}
			String abbrReference = abbreviationReference.stream().filter(Objects::nonNull).filter(Predicate.not(String::isBlank)).distinct().collect(Collectors.joining("; "));
			String abbrDisease = abbreviationDisease.stream().filter(Objects::nonNull).filter(Predicate.not(String::isBlank)).distinct().collect(Collectors.joining("; "));
			String abbr = Stream.concat(Stream.concat(abbreviationZygosity.stream(), abbreviationInheritance.stream()), abbreviationClass.stream())
								.filter(Objects::nonNull).filter(Predicate.not(String::isBlank)).distinct().collect(Collectors.joining("; "));
			prev.incidentalFindings().abbreviationReference(abbrReference).abbreviationDisease(abbrDisease).abbreviation(abbr)
				.interpretation(String.join("\r\n\r\n", comments));
		}

		return Mono.just(prev);
	}
	private static String resultText(String result, List<com.greencross.lims.dto.interpretation.Dgs.Variant> variants) {
		if("Inconclusive".equalsIgnoreCase(result)) return "의뢰 사유와 관련 가능성이 불분명한 변이가 발견되었습니다.";
		if(variants.size() > 0) return "의뢰 사유와 관련된 변이가 발견되었습니다.";
		else return "의뢰 사유와 관련된 변이는 발견되지 않았습니다.";
	}
	protected String header = "DGS 분석 결과, ";

	public String resultHeader(List<com.greencross.lims.dto.interpretation.Dgs.Variant> variants) {
		StringBuilder sb = new StringBuilder(header);
		Map<String, List<com.greencross.lims.dto.interpretation.Dgs.Variant>> vars = variants.stream().collect(Collectors.groupingBy(com.greencross.lims.dto.interpretation.Dgs.Variant::clazz));
		List<com.greencross.lims.dto.interpretation.Dgs.Variant> Ps = vars.getOrDefault("P", new ArrayList<>());
		Ps.addAll(vars.getOrDefault("PV", new ArrayList<>()));
		List<com.greencross.lims.dto.interpretation.Dgs.Variant> LPs = vars.getOrDefault("LP", new ArrayList<>());
		LPs.addAll(vars.getOrDefault("LPV", new ArrayList<>()));
		List<com.greencross.lims.dto.interpretation.Dgs.Variant> VUSs = vars.getOrDefault("VUS", new ArrayList<>());
		ArrayList<String> headerVariantStrings = new ArrayList<>();
		headerVariantStrings.add(addLineToResultHeader(Ps, "PV"));
		headerVariantStrings.add(addLineToResultHeader(LPs, "LPV"));
		headerVariantStrings.add(addLineToResultHeader(VUSs, "VUS"));
		sb.append(String.join("\n", headerVariantStrings.stream().filter(line -> !line.isBlank()).toList()));
		return sb.toString();
	}

	private String addLineToResultHeader(List<com.greencross.lims.dto.interpretation.Dgs.Variant> variants, String clazz){
		StringBuilder sb = new StringBuilder();
		variants.forEach((var) -> {
			sb.append(var.gene());
			sb.append(", ");
		});
		if(!variants.isEmpty()){
			sb.setLength(sb.length()-2);
			sb.append(" 유전자에서 ");
			sb.append(Phrase.classFull(clazz));
			sb.append("가 발견되었습니다.");
		}
		return sb.toString();
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

	private static final String NEGATIVE_FORMAT = "DGS 검사 결과 임상적으로 의미 있는 변이가 발견되지 않았습니다.\n" +
												  "이 검사는 유전자의 coding exon과 intron 영역을 포함한 전체 유전체 영역을 분석하며, splicing에 영향을 미치지 않을 것으로 예측되는 " +
			"synonymous variant 및 intronic variant는 보고하지 않습니다. 또한 일부 결실/중복 및 높은 상동성, 낮은 Coverage, 낮은 염기서열 품질을 보이는 영역에서 변이 검출에 한계가 있습니다. " +
			"검사의 기술적 한계에 대한 자세한 내용은 아래 검사의 한계를 참조하십시오.";
	@Override
	public Object negative(long sample, String service) {
		var interpretation = new com.greencross.lims.dto.interpretation.Dgs();
		interpretation.result("NEGATIVE").resultText("의뢰 사유와 관련된 변이는 발견되지 않았습니다.");
		interpretation.interpretation(NEGATIVE_FORMAT);
		return interpretation;
	}
	private String abbreviationReference(com.greencross.lims.dto.interpretation.Dgs.Variant variant) {
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
	private String abbreviationZygosity(Patient.Sex sex, com.greencross.lims.dto.interpretation.Dgs.Variant variant, Document values) {
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
	private String[] abbreviationInheritance(com.greencross.lims.dto.interpretation.Dgs.Variant variant, List<InterpretationParam.InterpretationParamDisease> param) {
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
	private String abbreviationClass(com.greencross.lims.dto.interpretation.Dgs.Variant variant) {
		return variant.clazz() + ", " + Phrase.classLong(variant.clazz());
	}
	private String comment(com.greencross.lims.dto.interpretation.Dgs.Variant variant, Document values, List<InterpretationParam.InterpretationParamDisease> diseases, AtomicBoolean ms) {
		String commentHgvs = commentHgvs(variant, values);
		String commentPopulation = commentPopulation(variant, values, ms);
		String commentInsilico = commentInsilico(variant, values);
		if(commentInsilico==null || commentInsilico.trim().isEmpty()) commentPopulation += "변이입니다.";
		else commentPopulation += "변이로,";
		String paragraph1 = List.of(commentHgvs, commentPopulation, commentInsilico).stream().filter(s->!s.trim().isEmpty()).collect(Collectors.joining(" "));
		String commentClinvarHgmd = commentClinvarHgmd(variant, values);
		String commentDisease = commentDisease(variant, diseases);
		String phrase1 =  List.of(paragraph1, commentClinvarHgmd).stream().filter(s->!s.trim().isEmpty()).collect(Collectors.joining(" "));
		if(!commentDisease.trim().isEmpty()) return phrase1 + "\r\n" + commentDisease;
		else return phrase1;
	}
	private static final String GENE_KEY = "gene.refgene";
	private String commentHgvs(com.greencross.lims.dto.interpretation.Dgs.Variant variant, Document values) {
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
	private String commentPopulation(com.greencross.lims.dto.interpretation.Dgs.Variant variant, Document values, AtomicBoolean hasPreviousPopulationComment) {
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
	private String commentInsilico(com.greencross.lims.dto.interpretation.Dgs.Variant variant, Document values) {
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
	private String commentClinvarHgmd(com.greencross.lims.dto.interpretation.Dgs.Variant variant, Document values) {
		try {
			String clinvar = commentClinvar(variant, values);
			String hgmd = commentHgmd(variant, values);
			if(clinvar.length() > 0) {
				if(hgmd.length() > 0) return "이 변이는 " + clinvar.replace("분류되어 있습니다(", "분류되어 있으며(") + ", " + hgmd;
				else return "이 변이는 " + clinvar + ".";
			} else if(hgmd.length() > 0) return "이 변이는 " + hgmd;
			else return "";
		} catch(Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	private static final String CLINVAR_CLASS_KEY = "clinvar.class";
	private static final String CLINVAR_ID_KEY = "clinvar.id";
	private String commentClinvar(com.greencross.lims.dto.interpretation.Dgs.Variant variant, Document values) {
		try {
			boolean hasClinvar = hasValue(values, CLINVAR_CLASS_KEY, "", ".");
			if(!hasClinvar) return "";
			String clinvar = values.getString(CLINVAR_CLASS_KEY);
			clinvar = clinvar.replace("Uncertain_significance", "Uncertain significance");
			clinvar = clinvar.replace("Likely_pathogenic", "Likely Pathogenic");
			clinvar = clinvar.replace("Likely_Pathogenic", "Likely Pathogenic");
			// clinvar = clinvar.replace("Pathogenic", "Pathogenic");
			clinvar = clinvar.replace("Likely_benign", "Likely Benign");
			clinvar = clinvar.replace("Likely_Benign", "Likely Benign");
			// clinvar = clinvar.replace("Benign", Phrase.classLong("BV"));
			if(clinvar.contains("Conflicting")) {
				clinvar = clinvar.substring(clinvar.indexOf('|') + 1);
				clinvar = clinvar.replace("&", " 또는 ");
			}
			clinvar = clinvar.replace("_", " ");
			String id = values.getString(CLINVAR_ID_KEY);
			return "ClinVar에서 "  + clinvar + (!clinvar.endsWith("e")?"으":"") + "로 분류되어 있습니다(ID: " + id + ")";
		} catch(Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	private static final String HGMD_KEY = "hgmd.tag";
	private static final String HGMD_DISEASE_KEY = "hgmd.codon.disease";
	private static final String HGMD_ID_KEY = "hgmd.pmid";
	private String commentHgmd(com.greencross.lims.dto.interpretation.Dgs.Variant variant, Document values) {
		try {
			boolean hasHgmd = hasValue(values, HGMD_KEY, "", ".");
			if(!hasHgmd) return "";
			String disease = unwrap(values.getString(HGMD_DISEASE_KEY)).replace("_", " ");
			disease = Phrase.diseaseName(disease);
			String id = unwrap(values.getString(HGMD_ID_KEY));
			return "기존에 " + disease + " 환자에서 보고된 바 있습니다(PMID: " + id + ").";
		} catch(Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	private String commentDisease(com.greencross.lims.dto.interpretation.Dgs.Variant variant, List<InterpretationParam.InterpretationParamDisease> diseases) {
		try {
			boolean hasDisease = diseases!=null && !diseases.isEmpty();
			StringBuilder sb = new StringBuilder(variant.gene()).append(" 유전자는 ");
			if(hasDisease) {
				String disease = diseases.stream().map(InterpretationParam.InterpretationParamDisease::fullName).collect(Collectors.joining(", "));
				String inheritance = diseases.stream().map(InterpretationParam.InterpretationParamDisease::inheritance)
											 .flatMap(Collection::stream).distinct().map(Phrase::inheritanceKor)
											 .collect(Collectors.joining(" 또는 "));
				if(inheritance.trim().isEmpty()) return sb.append(disease).append(" 관련 유전자로 알려져 있습니다.").toString();
				else return sb.append(disease).append(" 관련 유전자로 ").append(inheritance).append(" 유전 양상을 보입니다.").toString();
			} else return "";
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
