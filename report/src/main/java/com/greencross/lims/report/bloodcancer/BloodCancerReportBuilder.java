package com.greencross.lims.report.bloodcancer;

import com.gcgenome.lims.report.func.PageBuilder;
import com.gcgenome.lims.report.func.Painter;
import com.gcgenome.lims.test.bloodcancer.TestInfo;
import com.greencross.lims.dto.interpretation.BloodCancer;
import com.greencross.lims.entity.Patient;
import com.greencross.lims.entity.Request;
import com.greencross.lims.entity.Sample;
import com.greencross.lims.entity.Service;
import com.greencross.lims.report.HasSign;
import com.greencross.lims.report.ReportFactory;
import com.greencross.lims.report.SectionLDT;
import com.greencross.lims.report.SectionPage;
import com.greencross.lims.report.bloodcancer.enus.ALLTemplateEnUs;
import com.greencross.lims.report.bloodcancer.enus.BloodCancerHeaderEnUs;
import com.greencross.lims.report.bloodcancer.enus.BloodCancerResourceEnUs;
import com.greencross.lims.report.bloodcancer.enus.BloodCancerTemplateEnUs;
import com.greencross.lims.report.bloodcancer.kokr.ALLTemplateKoKr;
import com.greencross.lims.report.bloodcancer.kokr.BloodCancerHeaderKoKr;
import com.greencross.lims.report.bloodcancer.kokr.BloodCancerResourceKoKr;
import com.greencross.lims.report.bloodcancer.kokr.BloodCancerTemplateKoKr;
import com.greencross.lims.report.builder.LogoType;
import com.greencross.lims.report.builder.Sex;
import com.greencross.lims.report.kokr.SectionSign;
import org.apache.commons.lang.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.gcgenome.lims.test.bloodcancer.TestInfo.TESTS;
import static com.greencross.lims.report.ReportUtil.*;

@Component
public class BloodCancerReportBuilder implements ReportFactory<BloodCancer> {
	private BloodCancerDto dto;
	private TestInfo test;
	private BloodCancerTemplate template;
	@Override
	public Class<BloodCancer> clazz() {
		return BloodCancer.class;
	}
	@Override
	public boolean match(Request request) {
		String service = request.pk().service();
		return Arrays.stream(TESTS).anyMatch(m->m.code().equalsIgnoreCase(service));
	}
	@Override
	public byte[] build(Request request, BloodCancer interpretation, Class clazz) throws IOException {
		PDDocument doc = new PDDocument();
		test = info(request.pk().service());
		dto = dto(test, request, interpretation);
		boolean isAllPanel = isAllPanel(test, dto);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		BloodCancerResource resource = null;
		Painter<BloodCancerTemplate, BloodCancerDto> header = null;
		Painter<BloodCancerTemplate, BloodCancerDto> ldt = null;
		Painter<BloodCancerTemplate, BloodCancerDto> footer = null;
		Painter<BloodCancerTemplate, BloodCancerDto> sign = null;
		Painter<BloodCancerTemplate, BloodCancerDto> page = null;
		LogoType logoType;

		if ("KOKR".equalsIgnoreCase(test.i18n())) {
			header = new BloodCancerHeaderKoKr();
			resource = new BloodCancerResourceKoKr(test, doc);
			if (isLabsRequest(request.sample())) {
				logoType = LogoType.DEPENDENT;
				ldt = new SectionLDT<>(logoType, 110);
				sign = new SectionSign<>(81);
				footer = new com.greencross.lims.report.kokr.SectionFooterLabs<>();
				template = isAllPanel ? new ALLTemplateKoKr(resource, test, logoType) : new BloodCancerTemplateKoKr(resource, test, logoType);
				page = new SectionPage<>(537, 79, template.resource().fontDefault());
			} else {
				logoType = LogoType.INDEPENDENT;
				ldt = new SectionLDT<>(logoType, 90);
				sign = new SectionSign<>(65);
				footer = new com.greencross.lims.report.kokr.SectionFooterGenomeLabs<>();
				template = isAllPanel ? new ALLTemplateKoKr(resource, test, logoType) : new BloodCancerTemplateKoKr(resource, test, logoType);
				page = new SectionPage<>(563, 63, template.resource().fontDefault());
			}
		} else if ("ENUS".equalsIgnoreCase(test.i18n())) {
			header = new BloodCancerHeaderEnUs();
			resource = new BloodCancerResourceEnUs(doc);
			if (isLabsTest(request.service())) {
				if (isLabsRequest(request.sample())) {
					logoType = LogoType.DEPENDENT;
					ldt = new SectionLDT<>(logoType, 110);
					sign = new com.greencross.lims.report.enus.SectionSign<>(77);
					footer = new com.greencross.lims.report.enus.SectionFooterLabs<>();
					template = isAllPanel ? new ALLTemplateEnUs(resource, test, logoType) : new BloodCancerTemplateEnUs(resource, test, logoType);
					page = new SectionPage<>(543, 77, template.resource().fontDefault());
				} else {
					logoType = LogoType.INDEPENDENT;
					ldt = new SectionLDT<>(logoType, 100);
					sign = new com.greencross.lims.report.enus.SectionSign<>(65);
					footer = new com.greencross.lims.report.enus.SectionFooterGenomeLabs<>();
					template = isAllPanel ? new ALLTemplateEnUs(resource, test, logoType) : new BloodCancerTemplateEnUs(resource, test, logoType);
					page = new SectionPage<>(563, 65, template.resource().fontDefault());
				}
			} else {
				if (isLabsRequest(request.sample())) {
					logoType = LogoType.DEPENDENT;
					ldt = new SectionLDT<>(logoType, 110);
					sign = new com.greencross.lims.report.enus.SectionSign<>(77);
					footer = new com.greencross.lims.report.enus.SectionFooterLabs<>();
					template = isAllPanel ? new ALLTemplateEnUs(resource, test, logoType) : new BloodCancerTemplateEnUs(resource, test, logoType);
					page = new SectionPage<>(543, 77, template.resource().fontDefault());
				} else {
					logoType = LogoType.INDEPENDENT;
					ldt = new SectionLDT<>(logoType, 100);
					sign = new com.greencross.lims.report.enus.SectionSign<>(65);
					footer = new com.greencross.lims.report.enus.SectionFooterGenome<>();
					template = isAllPanel ? new ALLTemplateEnUs(resource, test, logoType) : new BloodCancerTemplateEnUs(resource, test, logoType);
					page = new SectionPage<>(563, 65, template.resource().fontDefault());
				}
			}
		}
		BloodCancerPageBuilder builder = isAllPanel ? new ALLPageBuilder(template, header, ldt, footer, sign, page) : new BloodCancerPageBuilder(template, header, ldt, footer, sign, page);
		new PageBuilder<>(template, dto).add(builder.page()).build().save(baos);
		return baos.toByteArray();
	}
	@Override
	public String buildLongFormText(Request request) {
		test = info(request.service().id());
		if(test.i18n().equalsIgnoreCase("ENUS")) return null;

		String txt = TEXT_TEMPLATE.replace("{:NAME}", test.name())
				.replace("{:CANCER_TYPE}", dto.cancerType());
		{
			final NumberFormat nf = NumberFormat.getInstance();
			BloodCancerDto.Result t1 = Arrays.stream(dto.results()).filter(r->r.tier().equals(BloodCancerDto.Tier.Tier1)).findFirst().orElse(new BloodCancerDto.Result());
			BloodCancerDto.Result t2 = Arrays.stream(dto.results()).filter(r->r.tier().equals(BloodCancerDto.Tier.Tier2)).findFirst().orElse(new BloodCancerDto.Result());
			BloodCancerDto.Result t3 = Arrays.stream(dto.results()).filter(r->r.tier().equals(BloodCancerDto.Tier.Tier3)).findFirst().orElse(new BloodCancerDto.Result());
			int t1n = t1.variants()!=null?t1.variants().length:0;
			int t2n = t2.variants()!=null?t2.variants().length:0;
			int t3n = t3.variants()!=null?t3.variants().length:0;
			String g1 = t1.variants()!=null?Arrays.stream(t1.variants()).map(BloodCancerDto.Variant::gene).distinct().collect(Collectors.joining(", ")):"-";
			String g2 = t2.variants()!=null?Arrays.stream(t2.variants()).map(BloodCancerDto.Variant::gene).distinct().collect(Collectors.joining(", ")):"-";
			String g3 = t3.variants()!=null?Arrays.stream(t3.variants()).map(BloodCancerDto.Variant::gene).distinct().collect(Collectors.joining(", ")):"-";
			if(g1.isEmpty()) g1 = "-";
			if(g2.isEmpty()) g2 = "-";
			if(g3.isEmpty()) g3 = "-";
			txt = txt.replace("{:T1-VARIANTS}", nf.format(t1n))
					.replace("{:T2-VARIANTS}", nf.format(t2n))
					.replace("{:T3-VARIANTS}", nf.format(t3n))
					.replace("{:T1-GENES}", g1)
					.replace("{:T2-GENES}", g2)
					.replace("{:T3-GENES}", g3);
		}{
			BloodCancerDto.Result tier1 = dto.results()!=null?Arrays.stream(dto.results()).filter(r->r.tier().equals(BloodCancerDto.Tier.Tier1)).findFirst().orElse(null):null;
			if(tier1 == null) tier1 = new BloodCancerDto.Result();
			if(tier1.variants()==null) tier1.variants(new BloodCancerDto.Variant[0]);
			StringBuilder sb = new StringBuilder();
			if(tier1.variants().length > 0) for(BloodCancerDto.Variant variant: tier1.variants()) {
				sb.append(String.format(VARIANT_TABLE_FORMAT, StringUtils.center(variant.gene(), 15), StringUtils.center(variant.hgvsc(), 15),
						StringUtils.center(variant.hgvsp(), 25), StringUtils.center(df.format(variant.vaf()),5),
						StringUtils.center(NumberFormat.getInstance().format(variant.depth()), 10), StringUtils.center(variant.cosmic(), 10)));
			} else sb.append(StringUtils.center("No Tier1 variant", 100)).append("\r\n");
			txt = txt.replace("{:VARIANTS-TIER1}", sb.toString())
					.replace("{:INTERPRETATION-TIER1}", (tier1.interpretation()==null||tier1.interpretation().trim().isBlank())?
							"Tier 1 (Strong clinical significance) 에 해당하는 변이가 발견되지 않았습니다.":
							tier1.interpretation());
		} {
			BloodCancerDto.Result tier2 = dto.results()!=null?Arrays.stream(dto.results()).filter(r->r.tier().equals(BloodCancerDto.Tier.Tier2)).findFirst().orElse(null):null;
			if(tier2 == null) tier2 = new BloodCancerDto.Result();
			if(tier2.variants()==null) tier2.variants(new BloodCancerDto.Variant[0]);
			StringBuilder sb = new StringBuilder();
			if(tier2.variants().length > 0) for(BloodCancerDto.Variant variant: tier2.variants()) {
				sb.append(String.format(VARIANT_TABLE_FORMAT, StringUtils.center(variant.gene(), 15), StringUtils.center(variant.hgvsc(), 15),
						StringUtils.center(variant.hgvsp(), 25), StringUtils.center(df.format(variant.vaf()),5),
						StringUtils.center(NumberFormat.getInstance().format(variant.depth()), 10), StringUtils.center(variant.cosmic(), 10)));
			} else sb.append(StringUtils.center("No Tier2 variant", 100)).append("\r\n");
			txt = txt.replace("{:VARIANTS-TIER2}", sb.toString())
					.replace("{:INTERPRETATION-TIER2}", (tier2.interpretation()==null||tier2.interpretation().trim().isBlank())?
							"Tier 2 (Potential clinical significance) 에 해당하는 변이가 발견되지 않았습니다.":
							tier2.interpretation());
		} {
			BloodCancerDto.Result tier3 = dto.results()!=null?Arrays.stream(dto.results()).filter(r->r.tier().equals(BloodCancerDto.Tier.Tier3)).findFirst().orElse(null):null;
			if(tier3 == null) tier3 = new BloodCancerDto.Result();
			if(tier3.variants()==null) tier3.variants(new BloodCancerDto.Variant[0]);
			StringBuilder sb = new StringBuilder();
			if(tier3.variants().length > 0) for(BloodCancerDto.Variant variant: tier3.variants()) {
				sb.append(String.format(VARIANT_TABLE_FORMAT, StringUtils.center(variant.gene(), 15), StringUtils.center(variant.hgvsc(), 15),
						StringUtils.center(variant.hgvsp(), 25), StringUtils.center(df.format(variant.vaf()),5),
						StringUtils.center(NumberFormat.getInstance().format(variant.depth()), 10), StringUtils.center(variant.cosmic(), 10)));
			} else sb.append(StringUtils.center("No Tier3 variant", 100)).append("\r\n");
			txt = txt.replace("{:VARIANTS-TIER3}", sb.toString())
					.replace("{:INTERPRETATION-TIER3}", (tier3.interpretation()==null||tier3.interpretation().trim().isBlank())?
							"Tier 3 (Unknown clinical significance) 에 해당하는 변이가 발견되지 않았습니다.":
							tier3.interpretation());
		}
		String header = String.format(VARIANT_TABLE_FORMAT, StringUtils.center(VARIANT_TABLE_HEADER[0], 15), StringUtils.center(VARIANT_TABLE_HEADER[1], 15),
				StringUtils.center(VARIANT_TABLE_HEADER[2], 25), StringUtils.center(VARIANT_TABLE_HEADER[3],5),
				StringUtils.center(VARIANT_TABLE_HEADER[4], 10), StringUtils.center(VARIANT_TABLE_HEADER[5], 10));
		String drugPhenotype = drugPhenotype(dto.drugPhenotypes());
		String[][] labelNamePairs = HasSign.getSanitizedContributorsLabelsAndNames(template, test);
		String limitations = Arrays.stream(template.lblTestLimitations()).collect(Collectors.joining("\n"));
		return txt.replace("{:HEADER}", header)
				.replace("{:DRUG_PHENOTYPE}", drugPhenotype)
				.replace("{:TARGET-REGION}", test.region())
				.replace("{:TESTED-PANEL}", test.panel())
				.replace("{:TARGET-ENRICHMENT-METHOD}", test.method())
				.replace("{:SEQUENCING-METHOD}", test.sequencing())
				.replace("{:PIPELINE}", test.pipeline())
				.replace("{:REFERENCE-GENOME}", test.reference())
				.replace("{:DNA}", dto.qcDna())
				.replace("{:LIBRARY}", dto.qcLibrary())
				.replace("{:SEQUENCING}", dto.qcSequencing())
				.replace("{:DEPTH}", dto.meanDepth())
				.replace("{:COVERAGE_LABEL}", template.lblQcCoverage())
				.replace("{:COVERAGE}", dto.coverage())
				.replace("{:ESSENTIALS}", Arrays.stream(test.genesEssential()).map(TestInfo.Gene::symbol).collect(Collectors.joining(", ")))
				.replace("{:SELECTIVES}", Arrays.stream(test.genesSelective()).map(TestInfo.Gene::symbol).collect(Collectors.joining(", ")))
				.replace("{:LIMITATIONS}", limitations)
				.replace("{:INSPECTOR_LABEL}", labelNamePairs[0][0])
				.replace("{:INSPECTOR}", labelNamePairs[0][1])
				.replace("{:REPORTER_AND_REVIEWER_LABEL}", labelNamePairs[1][0])
				.replace("{:REPORTER}", labelNamePairs[1][1])
				.replace("{:REVIEWER}", labelNamePairs[2][1]);
	}
	@Override
	public String buildShortFormText(Request request) {
		test = info(request.service().id());
		if(test.i18n().equalsIgnoreCase("ENUS")) return null;

		String txt = TEXT_SHORT_TEMPLATE.replace("{:NAME}", test.name())
				.replace("{:CANCER_TYPE}", dto.cancerType());
		{
			final NumberFormat nf = NumberFormat.getInstance();
			BloodCancerDto.Result t1 = Arrays.stream(dto.results()).filter(r -> r.tier().equals(BloodCancerDto.Tier.Tier1)).findFirst().orElse(new BloodCancerDto.Result());
			BloodCancerDto.Result t2 = Arrays.stream(dto.results()).filter(r -> r.tier().equals(BloodCancerDto.Tier.Tier2)).findFirst().orElse(new BloodCancerDto.Result());
			BloodCancerDto.Result t3 = Arrays.stream(dto.results()).filter(r -> r.tier().equals(BloodCancerDto.Tier.Tier3)).findFirst().orElse(new BloodCancerDto.Result());
			int t1n = t1.variants() != null ? t1.variants().length : 0;
			int t2n = t2.variants() != null ? t2.variants().length : 0;
			int t3n = t3.variants() != null ? t3.variants().length : 0;
			String g1 = t1.variants() != null ? Arrays.stream(t1.variants()).map(BloodCancerDto.Variant::gene).distinct().collect(Collectors.joining(",")) : "-";
			String g2 = t2.variants() != null ? Arrays.stream(t2.variants()).map(BloodCancerDto.Variant::gene).distinct().collect(Collectors.joining(",")) : "-";
			String g3 = t3.variants() != null ? Arrays.stream(t3.variants()).map(BloodCancerDto.Variant::gene).distinct().collect(Collectors.joining(",")) : "-";
			if (g1.isEmpty()) g1 = "-";
			if (g2.isEmpty()) g2 = "-";
			if (g3.isEmpty()) g3 = "-";
			txt = txt.replace("{:T1-VARIANTS}", nf.format(t1n))
					.replace("{:T2-VARIANTS}", nf.format(t2n))
					.replace("{:T3-VARIANTS}", nf.format(t3n))
					.replace("{:T1-GENES}", g1)
					.replace("{:T2-GENES}", g2)
					.replace("{:T3-GENES}", g3);
		}
		{
			BloodCancerDto.Result tier1 = dto.results() != null ? Arrays.stream(dto.results()).filter(r -> r.tier().equals(BloodCancerDto.Tier.Tier1)).findFirst().orElse(null) : null;
			if (tier1 == null) tier1 = new BloodCancerDto.Result();
			if (tier1.variants() == null) tier1.variants(new BloodCancerDto.Variant[0]);
			StringBuilder sb = new StringBuilder();
			if (tier1.variants().length > 0) for (BloodCancerDto.Variant variant : tier1.variants()) {
				sb.append(String.format(VARIANT_TABLE_FORMAT, variant.gene(), variant.hgvsc(),
						variant.hgvsp(), df.format(variant.vaf()),
						NumberFormat.getInstance().format(variant.depth()), variant.cosmic()));
			}
			else sb.append(StringUtils.center("No Tier1 variant", 100)).append("\r\n");
			txt = txt.replace("{:VARIANTS-TIER1}", sb.toString())
					.replace("{:INTERPRETATION-TIER1}", (tier1.interpretation() == null || tier1.interpretation().trim().isBlank()) ?
							"Tier 1 (Strong clinical significance) 에 해당하는 변이가 발견되지 않았습니다." :
							tier1.interpretation());
		}
		{
			BloodCancerDto.Result tier2 = dto.results() != null ? Arrays.stream(dto.results()).filter(r -> r.tier().equals(BloodCancerDto.Tier.Tier2)).findFirst().orElse(null) : null;
			if (tier2 == null) tier2 = new BloodCancerDto.Result();
			if (tier2.variants() == null) tier2.variants(new BloodCancerDto.Variant[0]);
			StringBuilder sb = new StringBuilder();
			if (tier2.variants().length > 0) for (BloodCancerDto.Variant variant : tier2.variants()) {
				sb.append(String.format(VARIANT_TABLE_FORMAT, variant.gene(), variant.hgvsc(),
						variant.hgvsp(), df.format(variant.vaf()),
						NumberFormat.getInstance().format(variant.depth()), variant.cosmic()));
			}
			else sb.append(StringUtils.center("No Tier2 variant", 100)).append("\r\n");
			txt = txt.replace("{:VARIANTS-TIER2}", sb.toString())
					.replace("{:INTERPRETATION-TIER2}", (tier2.interpretation() == null || tier2.interpretation().trim().isBlank()) ?
							"Tier 2 (Potential clinical significance) 에 해당하는 변이가 발견되지 않았습니다." :
							tier2.interpretation());
		}
		{
			BloodCancerDto.Result tier3 = dto.results() != null ? Arrays.stream(dto.results()).filter(r -> r.tier().equals(BloodCancerDto.Tier.Tier3)).findFirst().orElse(null) : null;
			if (tier3 == null) tier3 = new BloodCancerDto.Result();
			if (tier3.variants() == null) tier3.variants(new BloodCancerDto.Variant[0]);
			StringBuilder sb = new StringBuilder();
			if (tier3.variants().length > 0) for (BloodCancerDto.Variant variant : tier3.variants()) {
				sb.append(String.format(VARIANT_TABLE_FORMAT, variant.gene(), variant.hgvsc(),
						variant.hgvsp(), df.format(variant.vaf()),
						NumberFormat.getInstance().format(variant.depth()), variant.cosmic()));
			}
			else sb.append(StringUtils.center("No Tier3 variant", 100)).append("\r\n");
			txt = txt.replace("{:VARIANTS-TIER3}", sb.toString())
					.replace("{:INTERPRETATION-TIER3}", (tier3.interpretation() == null || tier3.interpretation().trim().isBlank()) ?
							"Tier 3 (Unknown clinical significance) 에 해당하는 변이가 발견되지 않았습니다." :
							tier3.interpretation());
		}
		String header = String.format(VARIANT_TABLE_SHORT_FORMAT, VARIANT_TABLE_HEADER[0], VARIANT_TABLE_HEADER[1],
				VARIANT_TABLE_HEADER[2], VARIANT_TABLE_HEADER[3],
				VARIANT_TABLE_HEADER[4], VARIANT_TABLE_HEADER[5]);
		String drugPhenotype = drugPhenotypeShort(dto.drugPhenotypes());
		String limitations = Arrays.stream(template.lblTestLimitations()).collect(Collectors.joining("\n"));
		String[][] labelNamePairs = HasSign.getSanitizedContributorsLabelsAndNames(template, test);
		String result = txt.replace("{:HEADER}", header)
				.replace("{:DRUG_PHENOTYPE}", drugPhenotype)
				.replace("{:TARGET-REGION}", test.region())
				.replace("{:TESTED-PANEL}", test.panel())
				.replace("{:TARGET-ENRICHMENT-METHOD}", test.method())
				.replace("{:SEQUENCING-METHOD}", test.sequencing())
				.replace("{:PIPELINE}", test.pipeline())
				.replace("{:REFERENCE-GENOME}", test.reference())
				.replace("{:DNA}", dto.qcDna())
				.replace("{:LIBRARY}", dto.qcLibrary())
				.replace("{:SEQUENCING}", dto.qcSequencing())
				.replace("{:DEPTH}", dto.meanDepth())
				.replace("{:COVERAGE_LABEL}", template.lblQcCoverage())
				.replace("{:COVERAGE}", dto.coverage())
				.replace("{:LIMITATIONS}", limitations)
				.replace("{:INSPECTOR_LABEL}", labelNamePairs[0][0])
				.replace("{:INSPECTOR}", labelNamePairs[0][1])
				.replace("{:REPORTER_AND_REVIEWER_LABEL}", labelNamePairs[1][0])
				.replace("{:REPORTER}", labelNamePairs[1][1])
				.replace("{:REVIEWER}", labelNamePairs[2][1]);
		while(result.contains("  ")) result = result.replace("  ", " ");
		return result;
	}
	private TestInfo info(String service) {
		return Arrays.stream(TESTS).filter(m->m.code().equalsIgnoreCase(service)).findFirst().get();
	}
	private boolean isAllPanel(TestInfo test, BloodCancerDto dto){
		return test.referralDefault().equalsIgnoreCase("ALL") && dto.drugPhenotypes() != null && dto.drugPhenotypes().length > 0;
	}
	private static final Pattern BIRTHDAT_PATTERN = Pattern.compile("^\\d{6}$");
	private static final Pattern BIRTHDAT_PATTERN2 = Pattern.compile("^(\\d{6})(-)*(\\d)$");
	private BloodCancerDto dto(TestInfo test, Request request, BloodCancer interpretation) {
		Objects.requireNonNull(request, "Request cannot be null");
		Objects.requireNonNull(request.sample(), "Sample cannot be null");
		Objects.requireNonNull(request.sample().patient(), "Patient cannot be null");

		var dto = new BloodCancerDto();
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
				.cancerType(interpretation.cancerType())
				.results(Arrays.stream(interpretation.results()).map(this::map).toArray(BloodCancerDto.Result[]::new))
				.qcDna(interpretation.qcDna()).qcLibrary(interpretation.qcLibrary()).qcSequencing(interpretation.qcSequencing())
				.meanDepth(interpretation.meanDepth()).coverage(interpretation.coverage())
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
		request.customInfos().stream().filter(i->"TA0028".equals(i.code())).findFirst().ifPresent(relation -> dto.relation(relation.value()));
		if(interpretation.drugPhenotypes()!=null) dto.drugPhenotypes(Arrays.stream(interpretation.drugPhenotypes()).map(this::map).toArray(BloodCancerDto.DrugPhenotype[]::new));
		return dto;
	}
	private final DecimalFormat df = new DecimalFormat("#.##");
	private BloodCancerDto.Result map(BloodCancer.Result dto) {
		return new BloodCancerDto.Result().tier(map(dto.tier()))
				.variants(Arrays.stream(dto.variants()).map(this::map).toArray(BloodCancerDto.Variant[]::new))
				.interpretation(dto.interpretation());
	}
	private BloodCancerDto.Tier map(BloodCancer.Tier tier) {
		if(tier == BloodCancer.Tier.Tier1) return BloodCancerDto.Tier.Tier1;
		if(tier == BloodCancer.Tier.Tier2) return BloodCancerDto.Tier.Tier2;
		if(tier == BloodCancer.Tier.Tier3) return BloodCancerDto.Tier.Tier3;
		return null;
	}
	private BloodCancerDto.Variant map(BloodCancer.Variant variant) {
		return new BloodCancerDto.Variant().snv(variant.snv())
				.analysis(variant.analysis())
				.gene(variant.gene())
				.hgvsc(variant.hgvsc())
				.hgvsp(variant.hgvsp())
				.vaf(variant.vaf())
				.depth(variant.depth())
				.cosmic(variant.cosmic());
	}
	private BloodCancerDto.DrugPhenotype map(BloodCancer.DrugPhenotype phenotype) {
		return new BloodCancerDto.DrugPhenotype().gene(phenotype.gene())
				.diplotype(phenotype.diplotype())
				.alleleStatus(phenotype.alleleStatus())
				.phenotype(phenotype.phenotype())
				.result(phenotype.result())
				.interpretation(phenotype.interpretation());
	}
	private String drugPhenotype(BloodCancerDto.DrugPhenotype[] phenotypes) {
		if(phenotypes == null || phenotypes.length <= 0) return "";
		BloodCancerDto.DrugPhenotype pheno1 = Arrays.stream(phenotypes).filter(g->"NUDT15".equalsIgnoreCase(g.gene())).findFirst().orElse(null);
		BloodCancerDto.DrugPhenotype pheno2 = Arrays.stream(phenotypes).filter(g->"TPMT".equalsIgnoreCase(g.gene())).findFirst().orElse(null);
		StringBuilder sb = new StringBuilder();

		String header = String.format("%1$-11s%2$-35s%3$-25s%4$-40s\r\n",
				StringUtils.center("Diplotype", 11),
				StringUtils.center("Allele function status", 35),
				StringUtils.center("Phenotype", 25),
				StringUtils.center("EHR Priority Result",30));
		if(pheno1!=null) {
			String value = String.format("%1$-11s%2$-35s%3$-25s%4$-40s\r\n",
					StringUtils.center(pheno1.diplotype(), 11),
					StringUtils.center(pheno1.alleleStatus(), 35),
					StringUtils.center(pheno1.phenotype(), 25),
					StringUtils.center(pheno1.result(),30));
			sb.append("◇ 약물 유전자 결과: ").append(pheno1.gene()).append("\r\n")
					.append("----------------------------------------------------------------------------------------------------\r\n")
					.append(header)
					.append("----------------------------------------------------------------------------------------------------\r\n")
					.append(value)
					.append("----------------------------------------------------------------------------------------------------\r\n")
					.append(pheno1.interpretation())
					.append("\r\n\r\n");
		}
		if(pheno2!=null) {
			String value = String.format("%1$-11s%2$-35s%3$-25s%4$-40s\r\n",
					StringUtils.center(pheno2.diplotype(), 11),
					StringUtils.center(pheno2.alleleStatus(), 35),
					StringUtils.center(pheno2.phenotype(), 25),
					StringUtils.center(pheno2.result(),30));
			sb.append("◇ 약물 유전자 결과: ").append(pheno2.gene()).append("\r\n")
					.append("----------------------------------------------------------------------------------------------------\r\n")
					.append(header)
					.append("----------------------------------------------------------------------------------------------------\r\n")
					.append(value)
					.append("----------------------------------------------------------------------------------------------------\r\n")
					.append(pheno2.interpretation())
					.append("\r\n\r\n");
		}
		return sb.toString();
	}
	private Boolean isLabsTest(Service service) {
		return Arrays.stream(new String[]{"ON104", "ON064", "ON065", "ON082", "ON083"}).anyMatch(svc->svc.equalsIgnoreCase(service.id()));
	}
	private String drugPhenotypeShort(BloodCancerDto.DrugPhenotype[] phenotypes) {
		if(phenotypes == null || phenotypes.length <= 0) return "";
		BloodCancerDto.DrugPhenotype pheno1 = Arrays.stream(phenotypes).filter(g->"NUDT15".equalsIgnoreCase(g.gene())).findFirst().orElse(null);
		BloodCancerDto.DrugPhenotype pheno2 = Arrays.stream(phenotypes).filter(g->"TPMT".equalsIgnoreCase(g.gene())).findFirst().orElse(null);
		StringBuilder sb = new StringBuilder();
		String header = String.format("%s\t%s\t%s\t%s\r\n", "Diplotype","Allele function status","Phenotype", "EHR Priority Result");
		if(pheno1!=null) {
			String value = String.format("%s\t%s\t%s\t%s\r\n", pheno1.diplotype(), pheno1.alleleStatus(), pheno1.phenotype(), pheno1.result());
			sb.append("◇ 약물 유전자 결과: ").append(pheno1.gene()).append("\r\n").append(header).append(value).append(pheno1.interpretation()).append("\r\n\r\n");
		}
		if(pheno2!=null) {
			String value = String.format("%s\t%s\t%s\t%s\r\n", pheno2.diplotype(), pheno2.alleleStatus(), pheno2.phenotype(), pheno2.result());
			sb.append("◇ 약물 유전자 결과: ").append(pheno2.gene()).append("\r\n").append(header).append(value).append(pheno2.interpretation()).append("\r\n\r\n");
		}
		return sb.toString();
	}
	private static final String VARIANT_TABLE_FORMAT = "%1$-15s%2$-20s%3$-25s%4$-10s%5$-15s%6$-5s\r\n";
	private static final String VARIANT_TABLE_SHORT_FORMAT = "%s\t%s\t%s\t%s\t%s\t%s\r\n";
	private static final String[] VARIANT_TABLE_HEADER = new String[] {
			"Gene",
			"DNA",
			"Protein",
			"VAF(%)",
			"Depth(x)",
			"COSMIC ID"
	};
	private static final String TEXT_TEMPLATE =
			"◆ {:NAME}\r\n" +
					"\r\n" +
					"◇ 검사목적질환 \r\n" +
					"{:CANCER_TYPE}\r\n" +
					"\r\n" +
					"◇ 결과 요약\r\n" +
					"----------------------------------------------------------------------------------------------------\r\n" +
					"Tier 1\r\n" +
					" Variants: {:T1-VARIANTS}\r\n" +
					" Gene: {:T1-GENES}\r\n" +
					"Tier 2\r\n" +
					" Variants: {:T2-VARIANTS}\r\n" +
					" Gene: {:T2-GENES}\r\n" +
					"Tier 3\r\n" +
					" Variants: {:T3-VARIANTS}\r\n" +
					" Gene: {:T3-GENES}\r\n" +
					"\r\n" +
					"◇ 검사결과(Tier 1) \r\n" +
					"Identified variations : \r\n" +
					"----------------------------------------------------------------------------------------------------\r\n" +
					"{:HEADER}" +
					"----------------------------------------------------------------------------------------------------\r\n" +
					"{:VARIANTS-TIER1}" +
					"----------------------------------------------------------------------------------------------------\r\n" +
					"\r\n" +
					"◇ 결과해석\r\n" +
					"{:INTERPRETATION-TIER1}\r\n" +
					"\r\n" +
					"\r\n" +
					"\r\n" +
					"◇ 검사결과(Tier 2) \r\n" +
					"Identified variations : \r\n" +
					"----------------------------------------------------------------------------------------------------\r\n" +
					"{:HEADER}" +
					"----------------------------------------------------------------------------------------------------\r\n" +
					"{:VARIANTS-TIER2}" +
					"----------------------------------------------------------------------------------------------------\r\n" +
					"\r\n" +
					"◇ 결과해석\r\n" +
					"{:INTERPRETATION-TIER2}\r\n" +
					"\r\n" +
					"\r\n" +
					"\r\n" +
					"◇ 검사결과(Tier 3) \r\n" +
					"Identified variations : \r\n" +
					"----------------------------------------------------------------------------------------------------\r\n" +
					"{:HEADER}" +
					"----------------------------------------------------------------------------------------------------\r\n" +
					"{:VARIANTS-TIER3}" +
					"----------------------------------------------------------------------------------------------------\r\n" +
					"\r\n" +
					"◇ 결과해석\r\n" +
					"{:INTERPRETATION-TIER3}\r\n" +
					"\r\n" +
					"\r\n{:DRUG_PHENOTYPE}" +
					"◇ 검사 방법\r\n" +
					"----------------------------------------------------------------------------------------------------\r\n" +
					"Target Region: {:TARGET-REGION}\r\n" +
					"Tested Panel: {:TESTED-PANEL}\r\n" +
					"Target Enrichment Method: {:TARGET-ENRICHMENT-METHOD}\r\n" +
					"Massively Parallel Sequencing: {:SEQUENCING-METHOD}\r\n" +
					"Bioinformatic Pipeline: {:PIPELINE}\r\n" +
					"Reference Genome: {:REFERENCE-GENOME}\r\n" +
					"----------------------------------------------------------------------------------------------------\r\n" +
					"\r\n" +
					"◇ QC 정보\r\n" +
					"----------------------------------------------------------------------------------------------------\r\n" +
					"Sample(DNA) QC: {:DNA}\r\n" +
					"Library QC: {:LIBRARY}\r\n" +
					"Sequencing QC: {:SEQUENCING}\r\n" +
					"Mean depth of coverage(X): {:DEPTH}\r\n" +
					"{:COVERAGE_LABEL}: {:COVERAGE}\r\n" +
					"----------------------------------------------------------------------------------------------------\r\n" +
					"\r\n" +
					"◇ 검사의 한계\r\n" +
					"{:LIMITATIONS}" +
					"\r\n" +
					"\r\n" +
					"◇ Gene List\r\n" +
					"필수 유전자: {:ESSENTIALS}\r\n\r\n" +
					"선택 유전자: {:SELECTIVES}\r\n" +
					"\r\n" +
					"{:INSPECTOR_LABEL} {:INSPECTOR} / {:REPORTER_AND_REVIEWER_LABEL} {:REPORTER}, {:REVIEWER}";
	private static final String TEXT_SHORT_TEMPLATE =
			"◆ {:NAME}\r\n" +
					"\r\n" +
					"◇ 검사목적질환 \r\n" +
					"{:CANCER_TYPE}\r\n" +
					"\r\n" +
					"◇ 결과 요약\r\n" +
					"Tier 1\r\n" +
					" Variants: {:T1-VARIANTS}\r\n" +
					" Gene: {:T1-GENES}\r\n" +
					"Tier 2\r\n" +
					" Variants: {:T2-VARIANTS}\r\n" +
					" Gene: {:T2-GENES}\r\n" +
					"Tier 3\r\n" +
					" Variants: {:T3-VARIANTS}\r\n" +
					" Gene: {:T3-GENES}\r\n" +
					"\r\n" +
					"◇ 검사결과(Tier 1) \r\n" +
					"Identified variations : \r\n" +
					"{:HEADER}" +
					"{:VARIANTS-TIER1}" +
					"\r\n" +
					"◇ 결과해석\r\n" +
					"{:INTERPRETATION-TIER1}\r\n" +
					"\r\n" +
					"\r\n" +
					"\r\n" +
					"◇ 검사결과(Tier 2) \r\n" +
					"Identified variations : \r\n" +
					"{:HEADER}" +
					"{:VARIANTS-TIER2}" +
					"\r\n" +
					"◇ 결과해석\r\n" +
					"{:INTERPRETATION-TIER2}\r\n" +
					"\r\n" +
					"\r\n" +
					"\r\n" +
					"◇ 검사결과(Tier 3) \r\n" +
					"Identified variations : \r\n" +
					"{:HEADER}" +
					"{:VARIANTS-TIER3}" +
					"\r\n" +
					"◇ 결과해석\r\n" +
					"{:INTERPRETATION-TIER3}\r\n" +
					"\r\n" +
					"\r\n{:DRUG_PHENOTYPE}" +
					"◇ QC 정보\r\n" +
					"Sample(DNA) QC: {:DNA}\r\n" +
					"Library QC: {:LIBRARY}\r\n" +
					"Sequencing QC: {:SEQUENCING}\r\n" +
					"Mean depth of coverage(X): {:DEPTH}\r\n" +
					"{:COVERAGE_LABEL}: {:COVERAGE}\r\n" +
					"\r\n" +
					"◇ 검사의 한계\r\n" +
					"{:LIMITATIONS}" +
					"\r\n" +
					"\r\n" +
					"◇ Gene List\r\n" +
					"검사 결과지를 참고하십시오.\r\n" +
					"\r\n" +
					"{:INSPECTOR_LABEL} {:INSPECTOR} / {:REPORTER_AND_REVIEWER_LABEL} {:REPORTER}, {:REVIEWER}";
}
