package com.greencross.lims.service.interpretation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.greencross.lims.dto.interpretation.Des;
import com.greencross.lims.dto.interpretation.InterpretationParam;
import com.greencross.lims.dto.interpretation.PanelTest;
import com.greencross.lims.entity.Patient;
import com.greencross.lims.entity.Sample;
import com.greencross.lims.service.InterpretationDAO;
import com.greencross.lims.service.SnvDAO;
import com.greencross.lims.trans.Phrase;
import com.ibm.icu.text.NumberFormat;
import com.ibm.icu.text.RuleBasedNumberFormat;
import com.ibm.icu.util.ULocale;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class BrcaCancerEnUs implements Interpretable {
	private final InterpretationDAO dao;
	private final SnvDAO snvRepo;
	private final ObjectMapper om;
	public BrcaCancerEnUs(InterpretationDAO dao, SnvDAO snvRepo, ObjectMapper om) {
		this.dao = dao;
		this.snvRepo = snvRepo;
		this.om = om;
	}

	@Override
	public boolean chk(long sample, String service) {
		if("ON001".equalsIgnoreCase(service)) return true;
		if("ON040".equalsIgnoreCase(service)) return true;
		return false;
	}

	protected final static String NEGATIVE_RESULT_TEXT = "No pathogenic variants were detected.";
	private final static String NEGATIVE_INTERPRETATION = "No pathogenic variants were detected from the genes included in the panel.\n\n" +
			"In this test, coding exons and flanking introns of the target genes are analyzed. Detection of deep intronic variants and structural variants such as genetic " +
			"rearrangements and copy number variants are limited. Please refer to the LIMITATION section for further information of the test.";
	@Override
	public Object negative(long sample, String service) {
		var interpretation = new Des();
		interpretation.result("NEGATIVE").resultText(NEGATIVE_RESULT_TEXT);
		interpretation.interpretation(NEGATIVE_INTERPRETATION);
		return interpretation;
	}
	@Override
	@Transactional(readOnly = true)
	public Mono<Object> interpret(long sample, String service, InterpretationParam param) {
		Patient patient = dao.em().find(Sample.class, sample).patient();
		PanelTest prev = om.convertValue(param.json(), PanelTest.class);
		if(prev.variants()==null || prev.variants().length<=0) return Mono.just(negative(sample, service));
		String resultText = resultText(prev.variants(), patient.sex(), param.disease());
		List<String> abbreviationReference = new ArrayList<>();
		List<String> abbreviationDisease = new ArrayList<>();
		List<String> abbreviationZygosity = new ArrayList<>();
		List<String> abbreviationInheritance = new ArrayList<>();
		List<String> abbreviationClass = new ArrayList<>();
		List<String> interpretationPerGenes = new LinkedList<>();

		List<PanelTest.Variant> variants = new LinkedList<>(Arrays.asList(prev.variants()));
		long pv = variants.stream().filter(v->"P".equalsIgnoreCase(v.clazz()) || "PV".equalsIgnoreCase(v.clazz())).count();
		long lpv = variants.stream().filter(v->"LP".equalsIgnoreCase(v.clazz()) || "LPV".equalsIgnoreCase(v.clazz())).count();
		if(pv <= 0 && lpv <= 0) prev.result("INCONCLUSIVE");
		else prev.result("POSITIVE");

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

			String comment = interpretationPerGene(variant, patient.sex(), diseases, values);
			interpretationPerGenes.add(comment);
			String[] inhs = abbreviationInheritance(variant, diseases);
			if(inhs!=null) abbreviationInheritance.addAll(Arrays.asList(inhs));
			abbreviationClass.add(abbreviationClass(variant));
		}
		String abbrReference = abbreviationReference.stream().distinct().collect(Collectors.joining("; "));
		String abbrDisease = abbreviationDisease.stream().distinct().filter(Objects::nonNull).filter(s->s.trim().length() > 0).collect(Collectors.joining("; "));
		String abbr = Stream.concat(Stream.concat(abbreviationZygosity.stream(), abbreviationInheritance.stream()), abbreviationClass.stream())
				.distinct().sorted().collect(Collectors.joining("; "));

		String interpretation = interpretationPerGenes.stream().collect(Collectors.joining("\n\n"));

		prev.resultText(resultText)
				.abbreviationReference(abbrReference).abbreviationDisease(abbrDisease).abbreviation(abbr)
				.interpretation(interpretation)
				.addendum(null);
		return Mono.just(prev);
	}
	private String resultText(PanelTest.Variant[] variants, Patient.Sex sex, Map<String, List<InterpretationParam.InterpretationParamDisease>> param) {
		if(variants == null || variants.length <= 0) return NEGATIVE_RESULT_TEXT;
		List<String> genes = Arrays.stream(variants).map(v->v.gene()).collect(Collectors.toList());
		return genes.stream().map(gene->{
			PanelTest.Variant[] target = Arrays.stream(variants).filter(v->gene.equalsIgnoreCase(v.gene())).toArray(PanelTest.Variant[]::new);
			return resultTextPerGene(gene, target, sex, param.get(gene));
		}).collect(Collectors.joining(";"));
	}
	private String resultTextPerGene(String gene, PanelTest.Variant[] variants, Patient.Sex sex, List<InterpretationParam.InterpretationParamDisease> param) {
		if(variants == null || variants.length <= 0) return "";
		else if(variants.length <= 1) {
			PanelTest.Variant variant = variants[0];
			String clsLong = Phrase.classLong(variant.clazz());
			String inheritance = param.stream().map(InterpretationParam.InterpretationParamDisease::inheritance)
					.flatMap(Collection::stream).distinct().collect(Collectors.joining(", "));
			String zygosity = Phrase.zygosity(variant.zygosity(), sex, inheritance);
			return "A {:zyg}{:cls} was identified in the {:gene} gene."
					.replace("{:zyg}", ("Heterozygous".equalsIgnoreCase(zygosity)||(zygosity==null))?"":(zygosity.toLowerCase() + " "))
					.replace("{:cls}", clsLong.toLowerCase())
					.replace("{:gene}", gene);
		} else {
			ULocale locale = new ULocale("en_US");
			NumberFormat formatter = new RuleBasedNumberFormat(locale, RuleBasedNumberFormat.SPELLOUT);
			long p = Arrays.stream(variants).map(v->Phrase.classShort(v.clazz())).filter("PV"::equalsIgnoreCase).count();
			long lp = Arrays.stream(variants).map(v->Phrase.classShort(v.clazz())).filter("LPV"::equalsIgnoreCase).count();
			long vus = Arrays.stream(variants).map(v->Phrase.classShort(v.clazz())).filter("VUS"::equalsIgnoreCase).count();
			List<String> classes = new LinkedList<>();
			if(p > 0) classes.add(resultTextPerClsas(p, "PV", formatter));
			if(lp > 0) classes.add(resultTextPerClsas(lp, "LPV", formatter));
			if(vus > 0) classes.add(resultTextPerClsas(vus, "VUS", formatter));
			String text = classes.stream().collect(Collectors.joining(" and ")) +
					" were  identified in the {:gene} gene."
							.replace("{:gene}", gene);
			text = text.substring(0, 1).toUpperCase() + text.substring(1);
			return text;
		}
	}
	private String resultTextPerClsas(long n, String clazz, NumberFormat formatter) {
		String clsLong = Phrase.classLong(clazz);
		String num = formatter.format(n);
		if(n == 1) num = "a";
		else clsLong = clsLong + "s";
		return num + " " + clsLong.toLowerCase();
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
		if(disease == null && diseaseShort == null) return "";
		if(Objects.requireNonNull(disease).equals(diseaseShort)) return "";
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
	private String interpretationPerGene(PanelTest.Variant variant, Patient.Sex sex, List<InterpretationParam.InterpretationParamDisease> param, Document values) {
		String gene = variant.gene();
		String hgvsc = variant.hgvsc();
		String hgvsp = variant.hgvsp();
		String clazz = Phrase.classFull(variant.clazz());
		String header = "A {:class}, {:hgvs}, was detected from {:gene}.".replace("{:class}", clazz).replace("{:gene}", gene);
		if(hgvsp == null || hgvsp.trim().isEmpty()) header = header.replace("{:hgvs}", hgvsc);
		else if(hgvsc == null || hgvsc.trim().isEmpty()) header = header.replace("{:hgvs}", hgvsp);
		else header = header.replace("{:hgvs}", hgvsc + " (" + hgvsp + ")");
		String proteinChangeDesc = Phrase.hgvspTextEnglish(gene, hgvsp);
		String pm2 = pm2(values);
		String literature = literature(variant, values);
		String pp5 = pp5(values);
		String pp3 = pp3(values);
		String conclusion = conclusion(variant);
		String diseaseDesc = diseaseDescription(variant, param);
		return Stream.of(header, proteinChangeDesc, pm2, literature, pp5, pp3, conclusion, diseaseDesc)
				.filter(Objects::nonNull)
				.filter(v->!v.isEmpty())
				.collect(Collectors.joining(" "));
	}
	private static final String GNOMAD_KEY = "gnomad.total";
	private String pm2(Document values) {
		boolean hasGnomad = hasValue(values, GNOMAD_KEY, "", ".", "0", "0.0", "0.00", "0.000", "0.0000");
		if(hasGnomad) return String.format("This variant is present in population databases (gnomAD %s%%).", percentage(values.getString(GNOMAD_KEY)));
		else return "This variant is not present in population databases.";
	}
	private static final String HGMD_KEY = "hgmd.tag";
	private static final String HGMD_DISEASE_KEY = "hgmd.codon_disease";
	private static final String HGMD_ID_KEY = "hgmd.pmid";
	private String literature(PanelTest.Variant variant, Document values) {
		try {
			boolean hasHgmd = hasValue(values, HGMD_KEY, "", ".");
			if(!hasHgmd) return "This variant has not been reported in the literature in individuals with {:gene}-related disorder.".replace("{:gene}", variant.gene());
			String disease = unwrap(values.getString(HGMD_DISEASE_KEY)).replace("_", " ");
			disease = Phrase.diseaseName(disease);
			String id = unwrap(values.getString(HGMD_ID_KEY));
			return "This variant has been reported in individuals affected with {:disease} (PMID: {:PMID}).".replace("{:disease}", disease).replace("{:PMID}", id);
		} catch(Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	private static final String CLINVAR_CLASS_KEY = "clinvar.class";
	private static final String CLINVAR_ID_KEY = "clinvar.id";
	private String pp5(Document values) {
		try {
			boolean hasClinvar = hasValue(values, CLINVAR_CLASS_KEY, "", ".");
			if(!hasClinvar) return "";
			String clinvar = values.getString(CLINVAR_CLASS_KEY);
			clinvar = clinvar.replace("Uncertain_significance", "VUS");
			clinvar = clinvar.replace("Likely_pathogenic", "Likely pathogenic");
			clinvar = clinvar.replace("Likely_Pathogenic", "Likely pathogenic");
			clinvar = clinvar.replace("Pathogenic", "Pathogenic");
			clinvar = clinvar.replace("Likely_benign", "Likely benign");
			clinvar = clinvar.replace("Likely_Benign", "Likely benign");
			clinvar = clinvar.replace("Benign", "Benign");
			if(clinvar.contains("Conflicting")) {
				clinvar = clinvar.substring(clinvar.indexOf('|') + 1);
				clinvar = clinvar.replace("&", " and ");
			}
			clinvar = clinvar.replace("_", " ");
			String id = values.getString(CLINVAR_ID_KEY);
			return "ClinVar contains an entry for this variant as {:clinvar} (Variation ID: {:clinvar-id})".replace("{:clinvar}", clinvar).replace("{:clinvar-id}", id);
		} catch(Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	private static final String SIFT_PRED_KEY = "sift.pred";
	private static final String POLY_PRED_KEY = "polyphen.pred";
	private static final String MUT_PRED_KEY = "mutationtaster.pred";
	private String pp3(Document values) {
		try {
			boolean hasSift = hasValue(values, SIFT_PRED_KEY, "", ".");
			boolean hasPoly = hasValue(values, POLY_PRED_KEY, "", ".");
			boolean hasMuta = hasValue(values, MUT_PRED_KEY, "", ".");
			boolean hasInsilico = hasSift & hasPoly & hasMuta;
			if(!hasInsilico) return "";
			StringBuilder sb = new StringBuilder();
			sb.append("In silico analyses, which predict the effect of the effect of missense changes on protein structure and function output, are as the following: ");
			List<String> scores = new LinkedList<>();
			if(hasSift) scores.add("SIFT: {:score}".replace("{:score}", values.getString(SIFT_PRED_KEY)));
			if(hasPoly) scores.add("PolyPhen: {:score}".replace("{:score}", values.getString(POLY_PRED_KEY).replace("_", " ")));
			if(hasMuta) {
				String code = values.getString(MUT_PRED_KEY);
				String pred = null;
				if("A".equalsIgnoreCase(code)) pred = "disease causing";
				else if("D".equalsIgnoreCase(code)) pred = "disease causing";
				else if("N".equalsIgnoreCase(code)) pred = "polymorphism";
				else if("P".equalsIgnoreCase(code)) pred = "polymorphism";
				if(pred!=null) scores.add("MutationTaster: {:score}".replace("{:score}", pred));
			}
			return sb.append(scores.stream().collect(Collectors.joining(", "))).append(".").toString();
		} catch(Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	private String conclusion(PanelTest.Variant variant) {
		if("PV".equalsIgnoreCase(variant.clazz())) return "For these reasons, this variant has been classified as Pathogenic.";
		else if("LPV".equalsIgnoreCase(variant.clazz())) return "For these reasons, this variant has been classified as Likely pathogenic.";
		else if("VUS".equalsIgnoreCase(variant.clazz())) return "In summary, the available evidence is currently insufficient to determine " +
				"the role of this variant in disease. Therefore, it has been classified as a " +
				"Variant of Uncertain Significance.";
		else return null;
	}

	private String diseaseDescription(PanelTest.Variant variant, List<InterpretationParam.InterpretationParamDisease> param) {
		StringBuilder sb = new StringBuilder("\r\nPathogenic ");
		sb.append(variant.gene()).append(" variants are associated with ");
		sb.append(param.stream().map(p->p.fullName()).collect(Collectors.joining(", ")));
		return sb.append(".").toString();
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

