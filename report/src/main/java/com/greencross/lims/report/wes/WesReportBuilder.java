package com.greencross.lims.report.wes;

import com.gcgenome.lims.report.func.PageBuilder;
import com.gcgenome.lims.report.func.Painter;
import com.gcgenome.lims.test.wes.TestInfo;
import com.gcgenome.lims.test.wes.TestWithSingleInfo;
import com.greencross.lims.dto.interpretation.Des;
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
import com.greencross.lims.report.wes.enus.WesHeaderEnUs;
import com.greencross.lims.report.wes.enus.WesResourceEnUs;
import com.greencross.lims.report.wes.enus.WesTemplateEnUs;
import com.greencross.lims.report.wes.kokr.WesHeaderKoKr;
import com.greencross.lims.report.wes.kokr.WesResourceKoKr;
import com.greencross.lims.report.wes.kokr.WesTemplateKoKr;
import org.apache.commons.lang.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.greencross.lims.report.ReportUtil.*;

@Component
public class WesReportBuilder implements ReportFactory<Des> {
	private WesDto dto;
	private TestInfo test;
	private WesTemplate template;
	@Override
	public Class<Des> clazz() {
		return Des.class;
	}
	private TestInfo info(String service) {
		for(TestInfo test: TestInfo.TESTS) if(test.code().equalsIgnoreCase(service)) return test;
		return null;
	}
	@Override
	public boolean match(Request request) {
		for(TestInfo test: TestInfo.TESTS) if(test.code().equalsIgnoreCase(request.pk().service())) return true;
		return false;
	}
	@Override
	public byte[] build(Request request, Des value, Class<Des> clazz) throws IOException {
		PDDocument doc = new PDDocument();
		test = info(request.pk().service());
		dto = dto(test, request, value);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		WesHeader header = null;
		Painter<WesTemplate, WesDto> footer = null;
		Painter<WesTemplate, WesDto> sign = null;
		Painter<WesTemplate, WesDto> ldt = null;
		Painter<WesTemplate, WesDto> pn = null;
		LogoType logoType;

		if ("KOKR".equalsIgnoreCase(test.i18n())) {
			template = new WesTemplateKoKr(new WesResourceKoKr(doc, dto), test);
			header = new WesHeaderKoKr(template);
			if(isLabsRequest(request.sample())) {
				logoType = LogoType.DEPENDENT;
				ldt = new SectionLDT<>(logoType, 120);
				sign = new SectionSign<>(80);
				footer = new com.greencross.lims.report.kokr.SectionFooterLabs<>();
				pn = new SectionPage<>(539, 80, template.resource().fontDefault());
			} else {
				logoType = LogoType.INDEPENDENT;
				ldt = new SectionLDT<>(logoType, 90);
				sign = new SectionSign<>(65);
				footer = new com.greencross.lims.report.kokr.SectionFooterGenome<>();
				pn = new SectionPage<>(565, 65, template.resource().fontDefault());
			}
		} else if ("ENUS".equalsIgnoreCase(test.i18n())) {
			template = new WesTemplateEnUs(new WesResourceEnUs(doc, dto), test);
			header = new WesHeaderEnUs(template);
			if(isLabsRequest(request.sample())) {
				logoType = LogoType.DEPENDENT;
				ldt = new SectionLDT<>(logoType, 100);
				sign = new com.greencross.lims.report.enus.SectionSign<>(60);
				footer = new com.greencross.lims.report.enus.SectionFooterGenomeLabs<>();
				pn = new SectionPage<>(539, 60, template.resource().fontDefault());
			} else {
				logoType = LogoType.INDEPENDENT;
				footer = new com.greencross.lims.report.enus.SectionFooterGenome<>();
				sign = new com.greencross.lims.report.enus.SectionSign<>(65);
				pn = new SectionPage<>(565, 65, template.resource().fontDefault());
				ldt = new SectionLDT<>(logoType, 100);
			}
		}

		new PageBuilder<>(template, dto).add(new WesPage(template, header, ldt, footer, sign, pn).page()).build().save(baos);
		return baos.toByteArray();
	}

	@Override
	public String buildLongFormText(Request request) {
		test = info(request.pk().service());
		StringBuilder sb = new StringBuilder();
		if((dto.report().variants()!=null && dto.report().variants().length > 0)) {
			for(WesDto.Variant variant: dto.report().variants()) {
				sb.append(String.format(VARIANT_TABLE_FORMAT, StringUtils.center(variant.gene(), 12), StringUtils.center(variant.dnaChange(), 15),
						StringUtils.center(variant.predictedAa(), 20), StringUtils.center(variant.zygosity(),6),
						StringUtils.center(variant.omim(), 15), StringUtils.center(variant.inherit(), 6),
						StringUtils.center(variant.clazz(), 6)));
			}
			sb.append("----------------------------------------------------------------------------------------------------\r\n");
			if(dto.report().reference()!=null && !dto.report().reference().trim().isEmpty()) sb.append("Reference sequence : ").append(dto.report().reference()).append("\r\n");
			if(dto.report().omimDisease()!=null && !dto.report().omimDisease().trim().isEmpty()) sb.append("OMIM Disease : ").append(dto.report().omimDisease()).append("\r\n");
			if(dto.report().abbreviation()!=null && !dto.report().abbreviation().trim().isEmpty()) sb.append("Abbreviations : ").append(dto.report().abbreviation()).append("\r\n");
		} else sb.append(StringUtils.center("No identified variant", 100)).append("\r\n");

		StringBuilder incidentalFindings = new StringBuilder();
		if(dto.consentIncidentalFindings()) {
			incidentalFindings.append("◇ Incidental findings : \r\n");
			if (dto.addendum()!= null && dto.addendum().variants() != null && dto.addendum().variants().length > 0) {
				StringBuilder sb2 = new StringBuilder();
				for (WesDto.Variant variant : dto.addendum().variants()) {
					sb2.append(String.format(VARIANT_TABLE_FORMAT, StringUtils.center(variant.gene(), 12), StringUtils.center(variant.dnaChange(), 15),
							StringUtils.center(variant.predictedAa(), 20), StringUtils.center(variant.zygosity(), 6),
							StringUtils.center(variant.omim(), 15), StringUtils.center(variant.inherit(), 6),
							StringUtils.center(variant.clazz(), 6)));
				}
				sb2.append("----------------------------------------------------------------------------------------------------\r\n");
				if (dto.addendum().reference() != null && !dto.addendum().reference().trim().isEmpty())
					sb2.append("Reference sequence : ").append(dto.addendum().reference()).append("\r\n");
				if (dto.addendum().omimDisease() != null && !dto.addendum().omimDisease().trim().isEmpty())
					sb2.append("Disease : ").append(dto.addendum().omimDisease()).append("\r\n");
				if (dto.addendum().abbreviation() != null && !dto.addendum().abbreviation().trim().isEmpty())
					sb2.append("Abbreviations : ").append(dto.addendum().abbreviation()).append("\r\n");
				String template = "----------------------------------------------------------------------------------------------------\r\n" +
						"    Gene         DNA change        Predicted      Zygosity   OMIM Disease   Inherit     Class\r\n" +
						"                                   AA change\r\n" +
						"----------------------------------------------------------------------------------------------------\r\n" +
						"{:VARIANTS}" +
						"\r\n" +
						"{:INTERPRETATION}\r\n" +
						"\r\n" +
						"\r\n";

				incidentalFindings.append(template.replace("{:VARIANTS}", sb2.toString())
						.replace("{:INTERPRETATION}", dto.addendum().interpretation() + "\r\n" + lblIncidentalFindingInfo));
			} else {
				String template = "{:INTERPRETATION}\r\n" +
						"\r\n" +
						"\r\n";
				incidentalFindings.append(template.replace("{:INTERPRETATION}", dto.addendum().interpretation() + "\r\n" + lblIncidentalFindingInfo));
			}
		}
		String[][] labelNamePairs = HasSign.getSanitizedContributorsLabelsAndNames(template, test);
		return TEXT_TEMPLATE.replace("{:NAME}", test.title())
				.replace("{:REFERRAL}", dto.reasonFR())
				.replace("{:RESULT-TEXT}", dto.report().resultText())
				.replace("{:VARIANTS}", sb.toString())
				.replace("{:INTERPRETATION}", dto.report().interpretation())
				.replace("{:INCIDENTAL-FINDINGS}", incidentalFindings.toString())
				.replace("{:PANEL}", test.panel())
				.replace("{:DEPTH}", dto.meanDepth())
				.replace("{:COVERAGE}", dto.x10Coverage())
				.replace("{:METHOD}", Arrays.stream(test.methods()).collect(Collectors.joining("\r\n")))
				.replace("{:LIMITATIONS}", String.join("\r\n", test.limitation()))
				.replace("{:INSPECTOR_LABEL}", labelNamePairs[0][0])
				.replace("{:INSPECTOR}", labelNamePairs[0][1])
				.replace("{:REPORTER_AND_REVIEWER_LABEL}", labelNamePairs[1][0])
				.replace("{:REPORTER}", labelNamePairs[1][1])
				.replace("{:REVIEWER}", labelNamePairs[2][1]);
	}

	@Override
	public String buildShortFormText(Request request) {
		test = info(request.pk().service());
		String header = String.format(VARIANT_TABLE_SHORT_FORMAT, VARIANT_TABLE_HEADER[0], VARIANT_TABLE_HEADER[1],
				VARIANT_TABLE_HEADER[2], VARIANT_TABLE_HEADER[3],
				VARIANT_TABLE_HEADER[4], VARIANT_TABLE_HEADER[5],
				VARIANT_TABLE_HEADER[6]);
		StringBuilder sb = new StringBuilder();
		if(dto.report().variants()!=null && dto.report().variants().length > 0) {
			for(WesDto.Variant variant: dto.report().variants()) {
				sb.append(String.format(VARIANT_TABLE_SHORT_FORMAT, variant.gene(), variant.dnaChange(),
						variant.predictedAa(), variant.zygosity(),
						variant.omim(), variant.inherit(),
						variant.clazz()));
			}
			if(dto.report().reference()!=null && !dto.report().reference().trim().isEmpty()) sb.append("Reference sequence : ").append(dto.report().reference()).append("\r\n");
			if(dto.report().omimDisease()!=null && !dto.report().omimDisease().trim().isEmpty()) sb.append("OMIM Disease : ").append(dto.report().omimDisease()).append("\r\n");
			if(dto.report().abbreviation()!=null && !dto.report().abbreviation().trim().isEmpty()) sb.append("Abbreviations : ").append(dto.report().abbreviation()).append("\r\n");
		} else sb.append(StringUtils.center("No identified variant", 100)).append("\r\n");
		StringBuilder incidentalFindings = new StringBuilder();
		if(dto.consentIncidentalFindings()) {
			incidentalFindings.append("◇ Incidental findings : \r\n");
			if (dto.addendum() != null && dto.addendum().variants() != null && dto.addendum().variants().length > 0) {
				StringBuilder sb2 = new StringBuilder();
				for (WesDto.Variant variant : dto.addendum().variants()) {
					sb2.append(String.format(VARIANT_TABLE_SHORT_FORMAT, variant.gene(), variant.dnaChange(),
							variant.predictedAa(), variant.zygosity(),
							variant.omim(), variant.inherit(),
							variant.clazz()));
				}
				if (dto.addendum().reference() != null && !dto.addendum().reference().trim().isEmpty())
					sb2.append("Reference sequence : ").append(dto.addendum().reference()).append("\r\n");
				if (dto.addendum().omimDisease() != null && !dto.addendum().omimDisease().trim().isEmpty())
					sb2.append("Disease : ").append(dto.addendum().omimDisease()).append("\r\n");
				if (dto.addendum().abbreviation() != null && !dto.addendum().abbreviation().trim().isEmpty())
					sb2.append("Abbreviations : ").append(dto.addendum().abbreviation()).append("\r\n");
				String template = "{:HEADER}" +
						"{:VARIANTS}" +
						"\r\n" +
						"{:INTERPRETATION}\r\n" +
						"\r\n" +
						"\r\n";
				incidentalFindings.append(template.replace("{:HEADER}", header)
						.replace("{:VARIANTS}", sb2.toString())
						.replace("{:INTERPRETATION}", dto.addendum().interpretation() + "\r\n" + lblIncidentalFindingInfo));
			} else {
				String template = "{:INTERPRETATION}\r\n" +
						"\r\n" +
						"\r\n";
				incidentalFindings.append(template.replace("{:INTERPRETATION}", dto.addendum().interpretation() + "\r\n" + lblIncidentalFindingInfo));
			}
		}
		String[][] labelNamePairs = HasSign.getSanitizedContributorsLabelsAndNames(template, test);
		String result = TEXT_SHORT_TEMPLATE.replace("{:NAME}", test.title())
				.replace("{:REFERRAL}", dto.reasonFR())
				.replace("{:RESULT-TEXT}", dto.report().resultText())
				.replace("{:HEADER}", header)
				.replace("{:VARIANTS}", sb.toString())
				.replace("{:INTERPRETATION}", dto.report().interpretation())
				.replace("{:INCIDENTAL-FINDINGS}", incidentalFindings.toString())
				.replace("{:PANEL}", test.panel())
				.replace("{:DEPTH}", dto.meanDepth())
				.replace("{:COVERAGE}", dto.x10Coverage())
				.replace("{:METHOD}", Arrays.stream(test.methods()).collect(Collectors.joining("\r\n")))
				.replace("{:LIMITATIONS}", String.join("\r\n", test.limitation()))
				.replace("{:INSPECTOR_LABEL}", labelNamePairs[0][0])
				.replace("{:INSPECTOR}", labelNamePairs[0][1])
				.replace("{:REPORTER_AND_REVIEWER_LABEL}", labelNamePairs[1][0])
				.replace("{:REPORTER}", labelNamePairs[1][1])
				.replace("{:REVIEWER}", labelNamePairs[2][1]);
		while(result.contains("  ")) result = result.replace("  ", " ");
		return result;	}

	private static final Pattern BIRTHDAT_PATTERN = Pattern.compile("^\\d{6}$");
	private static final Pattern BIRTHDAT_PATTERN2 = Pattern.compile("^(\\d{6})(-)*(\\d)$");
	public WesDto dto(TestInfo test, Request request, Des value) {
		Objects.requireNonNull(request.sample(), "Sample cannot be null");
		Objects.requireNonNull(request.sample().patient(), "Patient cannot be null");

		WesDto dto = WesDto.builder().build();
		Sample sample = request.sample();
		Patient patient = sample.patient();

		Double.parseDouble(value.coverage());
		Double.parseDouble(value.meanDepth());

		String patientCode = patient.code();
		if(patient.code()!=null && BIRTHDAT_PATTERN.matcher(patient.code()).find()) patientCode = patient.code() + "-*******";
		else if(patient.code()!=null && BIRTHDAT_PATTERN2.matcher(patient.code()).find()) {
			Matcher m = BIRTHDAT_PATTERN2.matcher(patient.code());
			m.find();
			patientCode = m.group(1) + "-" + m.group(3) + "******";
		}
		dto.code(request.pk().service())
				.reasonFR(value.reasonForReferral())
				.result(WesDto.Result.from(value.result()))
				.patientName(patient.name())
				.medicalRecordNumber(patient.mrn())
				.age(patient.age())
				.birthDate(patient.birth())
				.patientInfo(request.info())
				.physician(request.physician())
				.department(request.customerDeptName())
				.ward(request.ward())
				.collectionDate(request.dateSampling())
				.sex(Sex.from(patient.sex()))
				.specimenType(sample.sampleType())
				.receiptDate(request.dateRequest())
				.reportDate(LocalDate.now())
				.patientCode(patientCode)
				.medicalInstitution(determineMedicalInstitution(test::i18n, request, patient))
				.requestNumber(determineRequestNumber(sample));
		WesDto.Report report = WesDto.Report.builder().resultText(value.resultText()).build();
		if(value.variants()!=null) report.variants(Arrays.stream(value.variants()).map(WesReportBuilder::map).toArray(WesDto.Variant[]::new));
		report.reference(value.abbreviationReference()).omimDisease(value.abbreviationDisease()).abbreviation(value.abbreviation())
				.interpretation(value.interpretation());
		dto.report(report).meanDepth(value.meanDepth()).x10Coverage(value.coverage())
				.reporter(value.reporter()).reviewer(value.reviewer());
		if(value.consentIncidentalFindings()==null) value.consentIncidentalFindings(false);
		dto.consentIncidentalFindings(value.consentIncidentalFindings());
		if(value.consentIncidentalFindings() && value.incidentalFindings()!=null) {
			Des cast2 = value.incidentalFindings();
			WesDto.Report addendum = WesDto.Report.builder().resultText(cast2.resultText()).build();
			if(cast2.variants()!=null) addendum.variants(Arrays.stream(cast2.variants()).map(WesReportBuilder::map).toArray(WesDto.Variant[]::new));
			addendum.reference(cast2.abbreviationReference()).omimDisease(cast2.abbreviationDisease()).abbreviation(cast2.abbreviation())
					.interpretation(cast2.interpretation());
			dto.addendum(addendum);
		}
		request.customInfos().stream().filter(i->"TA0023".equals(i.code())).findFirst().ifPresent(barcode->dto.barcode(barcode.value()));
		request.customInfos().stream().filter(i->"TA0028".equals(i.code())).findFirst().ifPresent(relation->dto.relation(relation.value()));
		dto.revision(value.revision());
		return dto;
	}
	private static WesDto.Variant map(Des.Variant dto) {
		return new WesDto.Variant().gene(dto.gene())
				.dnaChange(dto.hgvsc())
				.predictedAa(dto.hgvsp())
				.zygosity(dto.zygosity())
				.omim(dto.disease())
				.inherit(dto.inheritance())
				.clazz(dto.clazz());
	}
	private static final String VARIANT_TABLE_FORMAT = "%1$-15s%2$-10s%3$-10s%4$-10s%5$-12s%6$-12s%7$-5s\r\n";
	private static final String[] VARIANT_TABLE_HEADER = new String[] {
			"Gene",
			"DNA",
			"Protein",
			"Zygosity",
			"Disease",
			"Inherit",
			"Class"
	};
	private static final String TEXT_TEMPLATE =
			"◆ {:NAME}\r\n" +
					"\r\n" +
					"◇ 검사의뢰사유 \r\n" +
					"{:REFERRAL}\r\n" +
					"\r\n" +
					"◇ 검사결과 \r\n" +
					"{:RESULT-TEXT}\r\n" +
					"\r\n" +
					"◇ Identified variation(s) : \r\n" +
					"----------------------------------------------------------------------------------------------------\r\n" +
					"    Gene         DNA change        Predicted      Zygosity   OMIM Disease   Inherit     Class\r\n" +
					"                                   AA change\r\n" +
					"----------------------------------------------------------------------------------------------------\r\n" +
					"{:VARIANTS}"+
					"\r\n" +
					"◇ 결과해석\r\n" +
					"{:INTERPRETATION}\r\n" +
					"\r\n" +
					"{:INCIDENTAL-FINDINGS}◇ Coverage\r\n" +
					"--------------------------------------------------------------------------------\r\n" +
					"Mean depth of coverage(X): {:DEPTH}\r\n" +
					"% of Target Bases ≥ 10X: {:COVERAGE}\r\n" +
					"--------------------------------------------------------------------------------\r\n" +
					"\r\n" +
					"◇ Diagnostic Exome sequencing 검사방법\r\n" +
					"{:METHOD}\r\n" +
					"\r\n" +
					"◇ Limitations\r\n" +
					"{:LIMITATIONS}\r\n" +
					"\r\n" +
					"{:INSPECTOR_LABEL} {:INSPECTOR} / {:REPORTER_AND_REVIEWER_LABEL} {:REPORTER}, {:REVIEWER}";
	private static final String VARIANT_TABLE_SHORT_FORMAT = "%s\t%s\t%s\t%s\t%s\t%s\t%s\r\n";
	private String lblIncidentalFindingInfo = String.format(
			"* Investigation of %d genes recommended by ACMG SF v3.3 (Genet Med. 2025.)",
			TestWithSingleInfo.GENE_INCIDENTAL_FINDINGS.length
	);
	private static final String TEXT_SHORT_TEMPLATE =
			"◆ {:NAME}\r\n" +
					"\r\n" +
					"◇ 검사의뢰사유 \r\n" +
					"{:REFERRAL}\r\n" +
					"\r\n" +
					"◇ 검사결과 \r\n" +
					"{:RESULT-TEXT}\r\n" +
					"\r\n" +
					"◇ Identified variation(s) : \r\n" +
					"{:HEADER}" +
					"{:VARIANTS}"+
					"\r\n" +
					"◇ 결과해석\r\n" +
					"{:INTERPRETATION}\r\n" +
					"\r\n" +
					"{:INCIDENTAL-FINDINGS}◇ Coverage\r\n" +
					"Mean depth of coverage(X): {:DEPTH}\r\n" +
					"% of Target Bases ≥ 10X: {:COVERAGE}\r\n" +
					"\r\n" +
					"◇ Diagnostic Exome sequencing 검사방법\r\n" +
					"{:METHOD}\r\n" +
					"\r\n" +
					"◇ Limitations\r\n" +
					"{:LIMITATIONS}\r\n"+
					"\r\n" +
					"{:INSPECTOR_LABEL} {:INSPECTOR} / {:REPORTER_AND_REVIEWER_LABEL} {:REPORTER}, {:REVIEWER}";
}
