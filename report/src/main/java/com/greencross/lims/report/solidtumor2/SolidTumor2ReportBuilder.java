package com.greencross.lims.report.solidtumor2;

import com.gcgenome.lims.report.func.PageBuilder;
import com.gcgenome.lims.report.func.Painter;
import com.gcgenome.lims.test.solidtumor2.TestInfo;
import com.greencross.lims.dto.interpretation.SolidTumor2;
import com.greencross.lims.entity.Patient;
import com.greencross.lims.entity.Request;
import com.greencross.lims.entity.Sample;
import com.greencross.lims.report.HasSign;
import com.greencross.lims.report.ReportFactory;
import com.greencross.lims.report.SectionLDT;
import com.greencross.lims.report.SectionPage;
import com.greencross.lims.report.builder.LogoType;
import com.greencross.lims.report.builder.Sex;
import com.greencross.lims.report.kokr.SectionSign;
import com.greencross.lims.report.solidtumor2.enus.SolidTumorHeaderEnUs;
import com.greencross.lims.report.solidtumor2.enus.SolidTumorResourceEnUs;
import com.greencross.lims.report.solidtumor2.enus.SolidTumorTemplateEnUs;
import com.greencross.lims.report.solidtumor2.kokr.SolidTumorHeaderKoKr;
import com.greencross.lims.report.solidtumor2.kokr.SolidTumorResourceKoKr;
import com.greencross.lims.report.solidtumor2.kokr.SolidTumorTemplateKoKr;
import org.apache.commons.lang.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.greencross.lims.report.ReportUtil.*;

@Component
public class SolidTumor2ReportBuilder implements ReportFactory<SolidTumor2> {
	private TestInfo test;
	private SolidTumorDto dto;
	private SolidTumorTemplate template;
	@Override
	public Class<SolidTumor2> clazz() {
		return SolidTumor2.class;
	}
	@Override
	public boolean match(Request request) {
		String service = request.pk().service();
		return Arrays.stream(TestInfo.TESTS).anyMatch(m->m.code().equalsIgnoreCase(service));
	}
	@Override
	public byte[] build(Request request, SolidTumor2 interpretation, Class clazz) throws IOException {
		PDDocument doc = new PDDocument();
		test = info(request.pk().service());
		dto = dto(test, request, interpretation);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		Painter<SolidTumorTemplate, SolidTumorDto> footer = null;
		Painter<SolidTumorTemplate, SolidTumorDto> sign = null;
		Painter<SolidTumorTemplate, SolidTumorDto> page = null;
		Painter<SolidTumorTemplate, SolidTumorDto> ldt = null;
		LogoType logoType;

		if("KOKR".equalsIgnoreCase(test.i18n())) {
			Painter<SolidTumorTemplate, SolidTumorDto> header = new SolidTumorHeaderKoKr();
			if(isLabsRequest(request.sample())) {
				logoType = LogoType.DEPENDENT;
				sign = new SectionSign<>(81);
				footer = new com.greencross.lims.report.kokr.SectionFooterLabs<>();
				template = new SolidTumorTemplateKoKr(new SolidTumorResourceKoKr(test, doc), test, logoType);
				page = new SectionPage<>(539, 81, template.resource().fontDefault());
				ldt = new SectionLDT<>(template.logoType(), 130);
			} else {
				logoType = LogoType.INDEPENDENT;
				sign = new SectionSign<>(65);
				footer = new com.greencross.lims.report.kokr.SectionFooterGenome<>();
				template = new SolidTumorTemplateKoKr(new SolidTumorResourceKoKr(test, doc), test, logoType);
				page = new SectionPage<>(547, 65, template.resource().fontDefault());
				ldt = new SectionLDT<>(template.logoType(), 100);
			}
			new PageBuilder<>(template, dto).add(new SolidTumorPageBuilder(template, header, ldt, footer, sign, page).page()).build().save(baos);
		} else if ("ENUS".equalsIgnoreCase(test.i18n())) {
			Painter<SolidTumorTemplate, SolidTumorDto> header = new SolidTumorHeaderEnUs();
			if (isLabsRequest(request.sample())) {
				logoType = LogoType.DEPENDENT;
				sign = new com.greencross.lims.report.enus.SectionSign<>(79);
				footer = new com.greencross.lims.report.enus.SectionFooterLabs<>();
				template = new SolidTumorTemplateEnUs(new SolidTumorResourceEnUs(test, doc), test, logoType);
				page = new SectionPage<>(539, 81, template.resource().fontDefault());
				ldt = new SectionLDT<>(template.logoType(), 130);
			} else {
				logoType = LogoType.INDEPENDENT;
				sign = new com.greencross.lims.report.enus.SectionSign<>(65);
				footer = new com.greencross.lims.report.enus.SectionFooterGenomeLabs<>();
				template = new SolidTumorTemplateEnUs(new SolidTumorResourceEnUs(test, doc), test, logoType);
				page = new SectionPage<>(560, 65, template.resource().fontDefault());
				ldt = new SectionLDT<>(template.logoType(), 100);
			}
			new PageBuilder<>(template, dto).add(new SolidTumorPageBuilder(template, header, ldt, footer, sign, page).page()).build().save(baos);
		}
		return baos.toByteArray();
	}
	@Override
	public String buildLongFormText(Request request) throws IOException {
		test = info(request.service().id());
		if(test.i18n().equalsIgnoreCase("ENUS")) return null;
		else return buildLongFormText();
	}
	public String buildLongFormText() throws IOException {
		var method = dto.method();
		String txt = TEXT_TEMPLATE.replace("{:NAME}", test.name())
				.replace("{:CANCER_CATEGORY}", dto.cancerCategory())
				.replace("{:CANCER_TYPE}", dto.cancerType());
		String header = String.format(VARIANT_TABLE_FORMAT, StringUtils.center(VARIANT_TABLE_HEADER[0], 15), StringUtils.center(VARIANT_TABLE_HEADER[1], 15),
				StringUtils.center(VARIANT_TABLE_HEADER[2], 25), StringUtils.center(VARIANT_TABLE_HEADER[3], 5),
				StringUtils.center(VARIANT_TABLE_HEADER[4], 10), StringUtils.center(VARIANT_TABLE_HEADER[5], 10));
		{
			var variants = Arrays.stream(dto.variants()).filter(s->s.tier() == SolidTumorDto.Tier.Tier1).collect(Collectors.toList());
			txt = variants(txt, variants, "{:VARIANTS-TIER1}");
			txt = interpretations(txt, variants, "{:INTERPRETATION-TIER1}");
		} {
			var variants = Arrays.stream(dto.variants()).filter(s->s.tier() == SolidTumorDto.Tier.Tier2).collect(Collectors.toList());
			txt = variants(txt, variants, "{:VARIANTS-TIER2}");
			txt = interpretations(txt, variants, "{:INTERPRETATION-TIER2}");
		} {
			var variants = Arrays.stream(dto.variants()).filter(s->s.tier() == SolidTumorDto.Tier.Tier3).collect(Collectors.toList());
			txt = variants(txt, variants, "{:VARIANTS-TIER3}");
		}
		StringBuilder genes = new StringBuilder();
		var snvs = dto.method().geneSets().stream().filter(gs -> gs.label().contains("SNV/Indel")).findAny().orElse(null);
		var cnvs = dto.method().geneSets().stream().filter(gs -> gs.label().contains("Copy Number Variation")).findAny().orElse(null);
		var fusions = dto.method().geneSets().stream().filter(gs -> gs.label().contains("Fusion")).findAny().orElse(null);
		if(snvs.genes().size()>0) genes.append("SNV/Indel (n=").append(snvs.genes().size()).append(")\r\n").append(String.join(", ", snvs.genes())).append("\r\n");

		if(cnvs.genes().size()>0) genes.append("Copy Number Variation (n=").append(cnvs.genes().size()).append(")\r\n").append(String.join(", ", cnvs.genes())).append("\r\n");
		if(fusions.genes().size()>0) {
			genes.append("Fusion (n=").append(fusions.genes().size()).append(")\r\n").append(String.join(", ", fusions.genes())).append("\r\n");
			if(dto.method().fusionInfo()!=null) genes.append(dto.method().fusionInfo()).append("\r\n");
		}
		String[] limitations = (method.limitations() != null) ? method.limitations().stream().toArray(String[]::new) : test.limitations();
		String[][] labelNamePairs = HasSign.getSanitizedContributorsLabelsAndNames(template, test);
		return txt.replace("{:HEADER}", header)
				.replace("{:SUMMARY}", summary(dto))
				.replace("{:TARGET-REGION}", method.region())
				.replace("{:TESTED-PANEL}", method.panel())
				.replace("{:TARGET-ENRICHMENT-METHOD}", method.method())
				.replace("{:SEQUENCING-METHOD}", method.sequencing())
				.replace("{:PIPELINE}", method.pipeline())
				.replace("{:REFERENCE-GENOME}", method.reference())
				.replace("{:QC}", qc(dto.qc()))
				.replace("{:LIMITATIONS}", String.join("\r\n", limitations))
				.replace("{:GENES}", genes)
				.replace("{:INSPECTOR_LABEL}", labelNamePairs[0][0])
				.replace("{:INSPECTOR}", labelNamePairs[0][1])
				.replace("{:REPORTER_AND_REVIEWER_LABEL}", labelNamePairs[1][0])
				.replace("{:REPORTER}", labelNamePairs[1][1])
				.replace("{:REVIEWER}", labelNamePairs[2][1]);
	}

	@Override
	public String buildShortFormText(Request request) {
		return null;
	}
	protected final NumberFormat nf = NumberFormat.getInstance();
	protected final DecimalFormat df = new DecimalFormat("#.#");
	protected final DecimalFormat df2 = new DecimalFormat("#.###");
	public String summary(SolidTumorDto dto) throws IOException {
		switch (test.code()){
			case "N198": {
				List<SolidTumorDto.Variant> t1 = Arrays.stream(dto.variants()).filter(r -> r.tier() == SolidTumorDto.Tier.Tier1).collect(Collectors.toList());
				List<SolidTumorDto.Variant> t2 = Arrays.stream(dto.variants()).filter(r -> r.tier() == SolidTumorDto.Tier.Tier2).collect(Collectors.toList());
				String g1 = t1.stream().map(SolidTumorDto.Variant::gene).distinct().collect(Collectors.joining(", "));
				String g2 = t2.stream().map(SolidTumorDto.Variant::gene).distinct().collect(Collectors.joining(", "));
				if (g1.isEmpty()) g1 = "-";
				if (g2.isEmpty()) g2 = "-";
				return SUMMARY_TEMPLATE_N198.replace("{:T1-VARIANTS}", nf.format(t1.size()))
						.replace("{:T2-VARIANTS}", nf.format(t2.size()))
						.replace("{:T1-GENES}", g1)
						.replace("{:T2-GENES}", g2)
						.replace("{:TMB}", dto.hypermutability().tmb())
						.replace("{:MSS}", dto.hypermutability().msi() + " (" + df2.format(dto.hypermutability().msiScore()) + ")")
						.replace("{:IMMUNOTHERAPY_INFO}", dto.method().immunotherapyInfo() != null ? dto.method().immunotherapyInfo() : "")
						.replace("{:SNV_QC}", dto.qc().snvQc())
						.replace("{:CNV_QC}", dto.qc().cnvQc())
						.replace("{:MSAF}", df2.format(dto.qc().msaf()) + "%")
						.replace("{:QC_INFO}", dto.method().qcInfo() != null ? dto.method().qcInfo() : "")
						.replace("{:QC_INTERPRETATION}", dto.qc().interpretation() != null ? (dto.qc().interpretation() + "\r\n\r\n") : "");
			}
			case "N199", "G0022402": {
				List<SolidTumorDto.Variant> t1 = Arrays.stream(dto.variants()).filter(r -> r.tier() == SolidTumorDto.Tier.Tier1).collect(Collectors.toList());
				List<SolidTumorDto.Variant> t2 = Arrays.stream(dto.variants()).filter(r -> r.tier() == SolidTumorDto.Tier.Tier2).collect(Collectors.toList());
				String g1 = t1.stream().map(SolidTumorDto.Variant::gene).distinct().collect(Collectors.joining(", "));
				String g2 = t2.stream().map(SolidTumorDto.Variant::gene).distinct().collect(Collectors.joining(", "));
				if (g1.isEmpty()) g1 = "-";
				if (g2.isEmpty()) g2 = "-";
				return SUMMARY_TEMPLATE_N199.replace("{:T1-VARIANTS}", nf.format(t1.size()))
						.replace("{:T2-VARIANTS}", nf.format(t2.size()))
						.replace("{:T1-GENES}", g1)
						.replace("{:T2-GENES}", g2)
						.replace("{:TMB}", dto.hypermutability().tmb())
						.replace("{:MSS}", dto.hypermutability().msi() + " (" + df2.format(dto.hypermutability().msiScore()) + ")")
						.replace("{:IMMUNOTHERAPY_INFO}", dto.method().immunotherapyInfo() != null ? dto.method().immunotherapyInfo() : "")
						.replace("{:SNV_QC}", dto.qc().snvQc())
						.replace("{:CNV_QC}", dto.qc().cnvQc())
						.replace("{:MSI_QC}", dto.qc().msiQc())
						.replace("{:RNA_QC}", dto.qc().rnaQc())
						.replace("{:PURITY}", df.format(dto.qc().purity()) + "%")
						.replace("{:QC_INFO}", dto.method().qcInfo() != null ? dto.method().qcInfo() : "")
						.replace("{:QC_INTERPRETATION}", dto.qc().interpretation() != null ? (dto.qc().interpretation() + "\r\n\r\n") : "");
			}
			case "N200": {
				List<SolidTumorDto.Variant> t1 = Arrays.stream(dto.variants()).filter(r->r.tier()==SolidTumorDto.Tier.Tier1).collect(Collectors.toList());
				List<SolidTumorDto.Variant> t2 = Arrays.stream(dto.variants()).filter(r->r.tier()==SolidTumorDto.Tier.Tier2).collect(Collectors.toList());
				String g1 = t1.stream().map(SolidTumorDto.Variant::gene).distinct().collect(Collectors.joining(", "));
				String g2 = t2.stream().map(SolidTumorDto.Variant::gene).distinct().collect(Collectors.joining(", "));
				if(g1.isEmpty()) g1 = "-";
				if(g2.isEmpty()) g2 = "-";
				return SUMMARY_TEMPLATE_N200.replace("{:T1-VARIANTS}", nf.format(t1.size()))
						.replace("{:T2-VARIANTS}", nf.format(t2.size()))
						.replace("{:T1-GENES}", g1)
						.replace("{:T2-GENES}", g2)
						.replace("{:TMB}", dto.hypermutability().tmb())
						.replace("{:MSS}", dto.hypermutability().msi() + " (" + df2.format(dto.hypermutability().msiScore()) + ")")
						.replace("{:IMMUNOTHERAPY_INFO}", dto.method().immunotherapyInfo()!=null?dto.method().immunotherapyInfo():"")
						.replace("{:SNV_QC}", dto.qc().snvQc())
						.replace("{:CNV_QC}", dto.qc().cnvQc())
						.replace("{:MSI_QC}", dto.qc().msiQc())
						.replace("{:RNA_QC}", dto.qc().rnaQc())
						.replace("{:PURITY}", df.format(dto.qc().purity()) + "%")
						.replace("{:QC_INFO}", dto.method().qcInfo()!=null?dto.method().qcInfo():"")
						.replace("{:QC_INTERPRETATION}", dto.qc().interpretation()!=null?(dto.qc().interpretation()+"\r\n\r\n"):"");

			}
		}
		return null;
	}
	protected String qc(SolidTumorDto.Qc qc) {
		switch (test.code()) {
			case "N198":
				return QC_TEMPLATE_N198.replace("{:COVERAGE}", nf.format(qc.medianExonCoverage()))
						.replace("{:PCT_OVER_1000X}", df.format(qc.pctExonOver1000X()))
						.replace("{:MAD}", df2.format(qc.mad()))
						.replace("{:MBC}", df.format(qc.mbc()));

			case "N199", "G0022402":
				return QC_TEMPLATE_N199
						.replace("{:PCT_OVER_100X}", df.format(qc.pctExonOver100X()))
						.replace("{:MAD}", df2.format(qc.mad()))
						.replace("{:MBC}", df.format(qc.mbc()))
						.replace("{:USABLE_MSI}", df.format(qc.usableMsi()))
						.replace("{:ON_TARGET_READS}", nf.format(qc.onTargetReads()))
						.replace("{:MEDIAN_CV_OVER_500X}", df2.format(qc.medianCvOver500X()));

			case "N200":
				return QC_TEMPLATE_N200
						.replace("{:PCT_OVER_100X}", df.format(qc.pctExonOver100X()))
						.replace("{:MAD}", df2.format(qc.mad()))
						.replace("{:MBC}", df.format(qc.mbc()))
						.replace("{:USABLE_MSI}", df.format(qc.usableMsi()));

		}
		return null;
	}
	private String variants(String txt, List<SolidTumorDto.Variant> variants, String placeholder) {
		StringBuilder sb = new StringBuilder();
		if(!variants.isEmpty()) for(SolidTumorDto.Variant variant: variants) {
			if("SNV".equalsIgnoreCase(variant.kind())) sb.append(String.format(VARIANT_TABLE_FORMAT,
					// StringUtils.center(NumberFormat.getInstance().format(variant.tier().ordinal()+1), 15),
					StringUtils.center(variant.gene() != null ? variant.gene().replace("\n", " ") : "", 15),
					StringUtils.center(variant.hgvsc() != null ? variant.hgvsc().replace("\n", " ") : "", 15),
					StringUtils.center(variant.hgvsp() != null ? variant.hgvsp().replace("\n", " ") : "", 25),
					StringUtils.center(variant.vaf() != null ? NumberFormat.getInstance().format(Double.parseDouble(variant.vaf().toString())) : "", 5),
					StringUtils.center(variant.depth() != null ? NumberFormat.getInstance().format(variant.depth()) : "", 10),
					StringUtils.center(variant.significance() != null ? variant.significance().replace("\n", " ") : "", 10)));
			else if("CNV".equalsIgnoreCase(variant.kind())) sb.append(String.format(VARIANT_TABLE_FORMAT,
					// StringUtils.center(NumberFormat.getInstance().format(variant.tier().ordinal()+1), 15),
					StringUtils.center(variant.gene() != null ? variant.gene().replace("\n", " ") : "", 15),
					StringUtils.center(variant.hgvsc() != null ? variant.hgvsc().replace("\n", " ") : "", 15),
					variant.hgvsp() != null ? variant.hgvsp().replace("\n", " ") : "",
					StringUtils.center("", 5),
					StringUtils.center("", 10),
					StringUtils.center(variant.significance() != null ? variant.significance().replace("\n", " ") : "", 10)));
			else if("FUSION".equalsIgnoreCase(variant.kind())) sb.append(String.format(VARIANT_TABLE_FORMAT,
					// StringUtils.center(NumberFormat.getInstance().format(variant.tier().ordinal()+1), 15),
					StringUtils.center(variant.gene() != null ? variant.gene().replace("\n", " ") : "", 15),
					variant.hgvsc() != null ? variant.hgvsc().replace("\n", "") : " ",
					StringUtils.center(variant.hgvsp() != null ? variant.hgvsp().replace("\n", " ") : "", 25),
					StringUtils.center("", 5),
					StringUtils.center("", 10),
					StringUtils.center(variant.significance() != null ? variant.significance().replace("\n", " ") : "", 10)));
		} else sb.append(StringUtils.center("No variant", 100)).append("\r\n");
		return txt.replace(placeholder, sb.toString());
	}
	private String interpretations(String txt, List<SolidTumorDto.Variant> variants, String placeholder) {
		StringBuilder sb = new StringBuilder();
		if(!variants.isEmpty()) for(SolidTumorDto.Variant variant: variants) sb.append(variant.interpretation()).append("\r\n\r\n");
		else sb.append(StringUtils.center("No variant", 100)).append("\r\n");
		return txt.replace(placeholder, sb.toString());
	}
	private TestInfo info(String service) {
		return Arrays.stream(TestInfo.TESTS).filter(m->m.code().equalsIgnoreCase(service)).findFirst().get();
	}
	private static final Pattern BIRTHDAT_PATTERN = Pattern.compile("^\\d{6}$");
	private static final Pattern BIRTHDAT_PATTERN2 = Pattern.compile("^(\\d{6})(-)*(\\d)$");
	private SolidTumorDto dto(TestInfo test, Request request, SolidTumor2 interpretation) {
		Objects.requireNonNull(request.sample(), "Sample cannot be null");
		Objects.requireNonNull(request.sample().patient(), "Patient cannot be null");

		var dto = new SolidTumorDto();
		Sample sample = request.sample();
		Patient patient = sample.patient();

		String patientCode = patient.code();
		if(patient.code()!=null && BIRTHDAT_PATTERN.matcher(patient.code()).find()) patientCode = patient.code() + "-*******";
		else if(patient.code()!=null && BIRTHDAT_PATTERN2.matcher(patient.code()).find()) {
			Matcher m = BIRTHDAT_PATTERN2.matcher(patient.code());
			m.find();
			patientCode = m.group(1) + "-" + m.group(3) + "******";
		}
		dto.code(request.pk().service())
		   .cancerCategory(interpretation.cancerCategory())
		   .cancerType(interpretation.cancerType())
		   .variants(Arrays.stream(interpretation.variants()).map(this::map).sorted(Comparator.comparing(SolidTumorDto.Variant::tier)).toArray(SolidTumorDto.Variant[]::new))
		   .hypermutability(map(interpretation.hypermutability()))
		   .qc(map(interpretation.qc()))
		   .method(map(interpretation.method(), test))
		   .patientName(patient.name())
		   .medicalRecordNumber(patient.mrn())
		   .age(patient.age())
		   .birthDate(patient.birth())
		   .collectionDate(request.dateSampling())
		   .patientInfo(request.info())
		   .physician(request.physician())
		   .department(request.customerDeptName())
		   .ward(request.ward())
		   .sex(Sex.from(patient.sex()))
		   .specimenType(sample.sampleType())
		   .receiptDate(request.dateRequest())
		   .reportDate(LocalDate.now())
		   .patientCode(patientCode)
		   .medicalInstitution(determineMedicalInstitution(test::i18n, request, patient))
		   .requestNumber(determineRequestNumber(sample));
		request.customInfos().stream().filter(i->"TA0023".equals(i.code())).findFirst().ifPresent(barcode->dto.barcode(barcode.value()));
		request.customInfos().stream().filter(i->"TA0028".equals(i.code())).findFirst().ifPresent(relation->dto.relation(relation.value()));
		return dto;
	}
	private SolidTumorDto.Tier map(SolidTumor2.Tier tier) {
		if(tier == SolidTumor2.Tier.Tier1) return SolidTumorDto.Tier.Tier1;
		if(tier == SolidTumor2.Tier.Tier2) return SolidTumorDto.Tier.Tier2;
		if(tier == SolidTumor2.Tier.Tier3) return SolidTumorDto.Tier.Tier3;
		return null;
	}
	private SolidTumorDto.Variant map(SolidTumor2.Variant variant) {
		var var = new SolidTumorDto.Variant().tier(map(variant.tier()))
				.gene(variant.gene())
				.significance(variant.significance())
				.interpretation(variant.interpretation())
				.kind(variant.kind());
		if("SNV".equalsIgnoreCase(variant.kind())) var.snv(variant.snv())
				.analysis(variant.analysis())
				.hgvsc(variant.hgvsc())
				.hgvsp(variant.hgvsp())
				.vaf(variant.vaf())
				.depth(variant.depth());
		else if("CNV".equalsIgnoreCase(variant.kind())) var.snv(variant.cnv())
				.hgvsc(variant.type())
				.hgvsp(variant.copyNumber());
		else if("FUSION".equalsIgnoreCase(variant.kind())) var.snv(variant.fusion())
				.hgvsc(variant.fusion())
				.hgvsp(variant.readCount())
				.vaf(variant.vaf())
				.depth(variant.depth());
		return var;
	}
	private SolidTumorDto.Method map(SolidTumor2.Method method, TestInfo test) {
		var m = new SolidTumorDto.Method();
		if(method.limitations()!=null && method.limitations().length>0) m.limitations(Arrays.asList(method.limitations()));
		else m.limitations(Arrays.asList(test.limitations()));
		return m.geneSets(Arrays.stream(method.geneSets()).map(this::map).toList())
				.fusionInfo(method.fusionInfo())
				.immunotherapyInfo(method.immunotherapyInfo())
				.qcInfo(method.qcInfo())
				.method(method.method())
				.panel(method.panel())
				.pipeline(method.pipeline())
				.region(method.region())
				.reference(method.reference())
				.sequencing(method.sequencing());
	}
	private SolidTumorDto.GeneSet map(SolidTumor2.GeneSet geneset) {
		String label = null;
		if("small_variants".equalsIgnoreCase(geneset.label())) label = String.format("SNV/Indel (n=%d)", geneset.genes().length);
		else if("copy_number_variants".equalsIgnoreCase(geneset.label())) label = String.format("Copy Number Variation (n=%d)",geneset.genes().length);
		else if("fusion_variants".equalsIgnoreCase(geneset.label())) label = String.format("Fusion (n=%d)", geneset.genes().length);
		return new SolidTumorDto.GeneSet().label(label).genes(Arrays.asList(geneset.genes()));
	}
	private SolidTumorDto.Hypermutability map(SolidTumor2.Hypermutability hypermutability) {
		return new SolidTumorDto.Hypermutability().tmb(hypermutability.tmb()).msi(hypermutability.msi()).msiScore(hypermutability.msiScore());
	}
	private SolidTumorDto.Qc map(SolidTumor2.Qc qc) {
		return new SolidTumorDto.Qc().snvQc(qc.snvQc())
				.cnvQc(qc.cnvQc())
				.msiQc(qc.msiQc())
				.rnaQc(qc.rnaQc())
				.interpretation(qc.interpretation())
				.purity(qc.purity())
				.pctExonOver100X(qc.pctExonOver100X())
				.pctExonOver1000X(qc.pctExonOver1000X())
				.medianExonCoverage(qc.medianExonCoverage())
				.mad(qc.mad())
				.mbc(qc.mbc())
				.usableMsi(qc.usableMsi())
				.onTargetReads(qc.onTargetReads())
				.medianCvOver500X(qc.medianCvOver500X())
				.msaf(qc.msaf());
	}
	private static final String VARIANT_TABLE_FORMAT = "%1$-15s%2$-20s%3$-25s%4$-10s%5$-15s%6$-10s\r\n";

	private static final String[] VARIANT_TABLE_HEADER = new String[] {
			"Gene",
			"DNA",
			"Protein",
			"VAF(%)",
			"Depth(x)",
			"Clinical Significance"
	};
	private final String SUMMARY_TEMPLATE_N198 =  "◇ 결과 요약\r\n" +
			"----------------------------------------------------------------------------------------------------\r\n" +
			"Tier 1\r\n" +
			" Variants: {:T1-VARIANTS}\r\n" +
			" Gene: {:T1-GENES}\r\n" +
			"Tier 2\r\n" +
			" Variants: {:T2-VARIANTS}\r\n" +
			" Gene: {:T2-GENES}\r\n" +
			"TMB Score: {:TMB}\r\n" +
			"MSI Status(MSI): {:MSS}\r\n" +
			"----------------------------------------------------------------------------------------------------\r\n" +
			"{:IMMUNOTHERAPY_INFO}\r\n" +
			"◇ QC 결과\r\n" +
			"----------------------------------------------------------------------------------------------------\r\n" +
			"SNV&TMB: {:SNV_QC}\r\n" +
			"CNV: {:CNV_QC}\r\n" +
			"Max Somatic VAF: {:MSAF}\r\n" +
			"----------------------------------------------------------------------------------------------------\r\n" +
			"{:QC_INFO}\r\n" +
			"{:QC_INTERPRETATION}\r\n";
	private final String SUMMARY_TEMPLATE_N199 =  "◇ 결과 요약\r\n" +
			"----------------------------------------------------------------------------------------------------\r\n" +
			"Tier 1\r\n" +
			" Variants: {:T1-VARIANTS}\r\n" +
			" Gene: {:T1-GENES}\r\n" +
			"Tier 2\r\n" +
			" Variants: {:T2-VARIANTS}\r\n" +
			" Gene: {:T2-GENES}\r\n" +
			"TMB Score: {:TMB}\r\n" +
			"MSI Status(MSI): {:MSS}\r\n" +
			"----------------------------------------------------------------------------------------------------\r\n\r\n" +
			"{:IMMUNOTHERAPY_INFO}\r\n" +
			"◇ QC 결과\r\n" +
			"----------------------------------------------------------------------------------------------------\r\n" +
			"SNV&TMB: {:SNV_QC}\r\n" +
			"CNV: {:CNV_QC}\r\n" +
			"MSI: {:MSI_QC}\r\n" +
			"RNA: {:RNA_QC}\r\n" +
			"Tumor purity: {:PURITY}\r\n" +
			"----------------------------------------------------------------------------------------------------\r\n" +
			"{:QC_INFO}\r\n" +
			"{:QC_INTERPRETATION}\r\n";
	private final String SUMMARY_TEMPLATE_N200 =  "◇ 결과 요약\r\n" +
			"----------------------------------------------------------------------------------------------------\r\n" +
			"Tier 1\r\n" +
			" Variants: {:T1-VARIANTS}\r\n" +
			" Gene: {:T1-GENES}\r\n" +
			"Tier 2\r\n" +
			" Variants: {:T2-VARIANTS}\r\n" +
			" Gene: {:T2-GENES}\r\n" +
			"TMB Score: {:TMB}\r\n" +
			"MSI Status(MSI): {:MSS}\r\n" +
			"----------------------------------------------------------------------------------------------------\r\n\r\n" +
			"{:IMMUNOTHERAPY_INFO}\r\n" +
			"◇ QC 결과\r\n" +
			"----------------------------------------------------------------------------------------------------\r\n" +
			"SNV&TMB: {:SNV_QC}\r\n" +
			"CNV: {:CNV_QC}\r\n" +
			"MSI: {:MSI_QC}\r\n" +
			"Tumor purity: {:PURITY}\r\n" +
			"----------------------------------------------------------------------------------------------------\r\n" +
			"{:QC_INFO}\r\n" +
			"{:QC_INTERPRETATION}\r\n";

	private final String QC_TEMPLATE_N198 = "◇ QC 정보\r\n" +
			"----------------------------------------------------------------------------------------------------\r\n" +
			"SNV&TMB QC\r\n" +
			"  Median Exon Coverage(X): {:COVERAGE}\r\n" +
			"  % of Exon 1000X: {:PCT_OVER_1000X}\r\n" +
			"CNV QC\r\n" +
			"  MAD: {:MAD}\r\n" +
			"  Median bin count: {:MBC}\r\n" +
			"----------------------------------------------------------------------------------------------------\r\n\r\n";
	private final String QC_TEMPLATE_N199 = "◇ QC 정보\r\n" +
			"----------------------------------------------------------------------------------------------------\r\n" +
			"SNV&TMB QC\r\n" +
			"  % of Exon 100X: {:PCT_OVER_100X}\r\n" +
			"CNV QC\r\n" +
			"  MAD: {:MAD}\r\n" +
			"  Median bin count: {:MBC}\r\n" +
			"MSI QC\r\n" +
			"  Usable MSI Sites: {:USABLE_MSI}\r\n" +
			"RNA QC\r\n" +
			"  Total on target reads: {:ON_TARGET_READS}\r\n" +
			"  Median CV for genes with > 500x: {:MEDIAN_CV_OVER_500X}\r\n" +
			"----------------------------------------------------------------------------------------------------\r\n\r\n";
	private final String QC_TEMPLATE_N200 = "◇ QC 정보\r\n" +
			"----------------------------------------------------------------------------------------------------\r\n" +
			"SNV&TMB QC\r\n" +
			"  % of Exon 100X: {:PCT_OVER_100X}\r\n" +
			"CNV QC\r\n" +
			"  MAD: {:MAD}\r\n" +
			"  Median bin count: {:MBC}\r\n" +
			"MSI QC\r\n" +
			"  Usable MSI Sites: {:USABLE_MSI}\r\n" +
			"----------------------------------------------------------------------------------------------------\r\n\r\n";

	protected String TEXT_TEMPLATE =
			"◆ {:NAME}\r\n" +
					"\r\n" +
					"◇ 검사목적질환 \r\n" +
					"암 조직 부위: {:CANCER_CATEGORY}\r\n" +
					"암종: {:CANCER_TYPE}\r\n" +
					"\r\n" +
					"{:SUMMARY}◇ 검사결과(Tier 1) \r\n" +
					"Identified variations : \r\n" +
					"----------------------------------------------------------------------------------------------------\r\n" +
					"{:HEADER}" +
					"----------------------------------------------------------------------------------------------------\r\n" +
					"{:VARIANTS-TIER1}" +
					"----------------------------------------------------------------------------------------------------\r\n" +
					"\r\n" +
					"◇ 결과해석\r\n" +
					"{:INTERPRETATION-TIER1}" +
					"◇ 검사결과(Tier 2) \r\n" +
					"Identified variations : \r\n" +
					"----------------------------------------------------------------------------------------------------\r\n" +
					"{:HEADER}" +
					"----------------------------------------------------------------------------------------------------\r\n" +
					"{:VARIANTS-TIER2}" +
					"----------------------------------------------------------------------------------------------------\r\n" +
					"\r\n" +
					"◇ 결과해석\r\n" +
					"{:INTERPRETATION-TIER2}" +
					"◇ 검사결과(Tier 3) \r\n" +
					"Identified variations : \r\n" +
					"----------------------------------------------------------------------------------------------------\r\n" +
					"{:HEADER}" +
					"----------------------------------------------------------------------------------------------------\r\n" +
					"{:VARIANTS-TIER3}" +
					"----------------------------------------------------------------------------------------------------\r\n" +
					"\r\n" +
					"\r\n" +
					"◇ 검사 방법\r\n" +
					"----------------------------------------------------------------------------------------------------\r\n" +
					"Target Region: {:TARGET-REGION}\r\n" +
					"Tested Panel / Size: {:TESTED-PANEL}\r\n" +
					"Target Enrichment Method: {:TARGET-ENRICHMENT-METHOD}\r\n" +
					"Massively Parallel Sequencing: {:SEQUENCING-METHOD}\r\n" +
					"Bioinformatic Pipeline: {:PIPELINE}\r\n" +
					"Reference Genome: {:REFERENCE-GENOME}\r\n" +
					"----------------------------------------------------------------------------------------------------\r\n\r\n" +
					"{:QC}◇ 검사의 한계\r\n" +
					"{:LIMITATIONS}\r\n\r\n" +
					"◇ Gene List\r\n" +
					"{:GENES}\r\n"+
					"\r\n" +
					"{:INSPECTOR_LABEL} {:INSPECTOR} / {:REPORTER_AND_REVIEWER_LABEL} {:REPORTER}, {:REVIEWER}";
}
