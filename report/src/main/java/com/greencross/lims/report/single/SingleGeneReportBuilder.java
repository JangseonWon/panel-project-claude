package com.greencross.lims.report.single;

import com.gcgenome.lims.report.func.PageBuilder;
import com.gcgenome.lims.report.func.Painter;
import com.gcgenome.lims.test.single.TestInfo;
import com.greencross.lims.dto.interpretation.PanelTest;
import com.greencross.lims.entity.Patient;
import com.greencross.lims.entity.Request;
import com.greencross.lims.entity.Sample;
import com.greencross.lims.report.HasSign;
import com.greencross.lims.report.ReportFactory;
import com.greencross.lims.report.SectionPage;
import com.greencross.lims.report.builder.LogoType;
import com.greencross.lims.report.builder.Sex;
import com.greencross.lims.report.kokr.SectionSign;
import com.greencross.lims.report.single.kokr.SingleGeneResourceKoKr;
import com.greencross.lims.report.single.kokr.SingleGeneTemplateKoKr;
import org.apache.commons.lang.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.greencross.lims.report.ReportUtil.*;


@Component
public class SingleGeneReportBuilder implements ReportFactory<PanelTest> {
	private SingleGeneDto dto;
	private TestInfo test;
	private SingleGeneTemplate template;
	private final List<ReportFactory<PanelTest>> delegates = List.of(new CancerPanelReportFactory(), new SingleGenePanelReportFactory());

	@Override
	public Class<PanelTest> clazz() {
		return PanelTest.class;
	}

	@Override
	public boolean match(Request request) {
		return delegates.stream().anyMatch(d -> d.match(request));
	}

	@Override
	public byte[] build(Request request, PanelTest interpretation, Class<PanelTest> clazz) {
		return delegates.stream().filter(d -> d.match(request)).findFirst().map(d -> {
			try {
				return d.build(request, interpretation);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}).orElseThrow(RuntimeException::new);
	}

	@Override
	public String buildShortFormText(Request request) {
		return processDelegate(request, d -> {
			try {
				return d.buildShortFormText(request);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		});
	}

	@Override
	public String buildLongFormText(Request request) {
		return processDelegate(request, d -> {
			try {
				return d.buildLongFormText(request);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		});
	}

	private <T> T processDelegate(Request request, Function<ReportFactory<? extends PanelTest>, T> mapper) {
		return delegates.stream()
				.filter(d -> d.match(request))
				.findFirst()
				.map(d -> mapper.apply(d))
				.orElseThrow(RuntimeException::new);
	}

	private static final Pattern BIRTHDAT_PATTERN = Pattern.compile("^\\d{6}$");
	private static final Pattern BIRTHDAT_PATTERN2 = Pattern.compile("^(\\d{6})(-)*(\\d)$");

	public SingleGeneDto dto(TestInfo test, Request request, PanelTest cast) {
		Objects.requireNonNull(request.sample(), "Sample cannot be null");
		Objects.requireNonNull(request.sample().patient(), "Patient cannot be null");

		SingleGeneDto dto = new SingleGeneDto();
		Sample sample = request.sample();
		Patient patient = sample.patient();

		String patientCode = patient.code();
		if (patient.code() != null && BIRTHDAT_PATTERN.matcher(patient.code()).find())
			patientCode = patient.code() + "-*******";
		else if (patient.code() != null && BIRTHDAT_PATTERN2.matcher(patient.code()).find()) {
			Matcher m = BIRTHDAT_PATTERN2.matcher(patient.code());
			m.find();
			patientCode = m.group(1) + "-" + m.group(3) + "******";
		}
		dto.code(request.pk().service())
				.reasonFR(cast.reasonForReferral())
				.result(SingleGeneDto.Result.from(cast.result()))
				.patientName(patient.name())
				.medicalRecordNumber(patient.mrn())
				.age(patient.age())
				.birthDate(patient.birth())
				.collectionDate(request.dateSampling())
				.sex(Sex.from(patient.sex()))
				.patientInfo(request.info())
				.physician(request.physician())
				.department(request.customerDeptName())
				.ward(request.ward())
				.specimenType(sample.sampleType())
				.receiptDate(request.dateRequest())
				.reportDate(LocalDate.now())
				.patientCode(patientCode)
				.medicalInstitution(determineMedicalInstitution(test::i18n, request, patient))
				.requestNumber(determineRequestNumber(sample));
		SingleGeneDto.Report report = new SingleGeneDto.Report().resultText(cast.resultText());
		if (cast.variants() != null) {
			report.variants(Arrays.stream(cast.variants())
					.map(SingleGeneReportBuilder::map)
					.toArray(SingleGeneDto.Variant[]::new));
		}
		report.reference(cast.abbreviationReference())
				.omimDisease(cast.abbreviationDisease())
				.abbreviation(cast.abbreviation())
				.interpretation(cast.interpretation());
		dto.report(report);
		if (cast.addendum() != null) {
			PanelTest cast2 = cast.addendum();
			SingleGeneDto.Report addendum = new SingleGeneDto.Report().resultText(cast2.resultText());
			if (cast2.variants() != null)
				addendum.variants(Arrays.stream(cast2.variants()).map(SingleGeneReportBuilder::map).toArray(SingleGeneDto.Variant[]::new));
			addendum.reference(cast2.abbreviationReference()).omimDisease(cast2.abbreviationDisease()).abbreviation(cast2.abbreviation())
					.interpretation(cast2.interpretation());
			dto.addendum(addendum);
		}
		if(test.category() == TestInfo.Category.WITH_MLPA) {
			boolean detected = "Detected".equalsIgnoreCase(cast.mlpaResult().result());
			dto.mlpaResult(new SingleGeneDto.Mlpa()
					.result(cast.mlpaResult().result())
					.exons(detected ? cast.mlpaResult().exons() : null)
					.zygosity(detected ? cast.mlpaResult().zygosity() : null)
					.delDup(detected ? cast.mlpaResult().delDup() : null)
			);
		}
		request.customInfos().stream().filter(i -> "TA0023".equals(i.code())).findFirst().ifPresent(barcode -> dto.barcode(barcode.value()));
		return dto;
	}

	private static SingleGeneDto.Variant map(PanelTest.Variant dto) {
		return new SingleGeneDto.Variant().gene(dto.gene())
				.dnaChange(dto.hgvsc())
				.predictedAa(dto.hgvsp())
				.zygosity(dto.zygosity())
				.omim(dto.disease())
				.inherit(dto.inheritance())
				.clazz(dto.clazz());
	}

	private final class SingleGenePanelReportFactory implements ReportFactory<PanelTest> {
		private final HashSet<TestInfo.Category> VALID_CATEGORIES = new HashSet<>(Set.of(TestInfo.Category.ETC, TestInfo.Category.WITH_MLPA, TestInfo.Category.CANCER_WITH_RD_REPORT));

		@Override
		public Class<PanelTest> clazz() {
			return PanelTest.class;
		}

		@Override
		public boolean match(Request request) {
			return Arrays.stream(TestInfo.TESTS)
					.filter(test -> VALID_CATEGORIES.contains(test.category()))
					.anyMatch(test -> test.code().equalsIgnoreCase(request.pk().service()));
		}

		private TestInfo info(String service) {
			return Arrays.stream(TestInfo.TESTS)
					.filter(test -> VALID_CATEGORIES.contains(test.category()) && test.code().equalsIgnoreCase(service))
					.findFirst()
					.orElse(null);
		}

		@Override
		public byte[] build(Request request, PanelTest value, Class<PanelTest> clazz) throws IOException {
			PDDocument doc = new PDDocument();
			test = info(request.pk().service());
			dto = dto(test, request, value);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();

			Painter<SingleGeneTemplate, SingleGeneDto> footer = null;
			Painter<SingleGeneTemplate, SingleGeneDto> sign = null;
			Painter<SingleGeneTemplate, SingleGeneDto> page = null;
			LogoType logoType;

			if (isLabsRequest(request.sample())) {
				logoType = LogoType.DEPENDENT;
				sign = new SectionSign<>(81);
				footer = new com.greencross.lims.report.kokr.SectionFooterLabs<>();
				template = new SingleGeneTemplateKoKr(new SingleGeneResourceKoKr(test, doc), test, logoType);
				page = new SectionPage<>(547, 81, template.resource().fontDefault());
			} else {
				logoType = LogoType.INDEPENDENT;
				sign = new SectionSign<>(65);
				footer = new com.greencross.lims.report.kokr.SectionFooterGenome<>();
				template = new SingleGeneTemplateKoKr(new SingleGeneResourceKoKr(test, doc), test, logoType);
				page = new SectionPage<>(547, 65, template.resource().fontDefault());
			}
			switch (test.category()){
				case WITH_MLPA -> new PageBuilder<>(template, dto).add(new SingleWithMlpaPage(template, footer, sign, page).page()).build().save(baos);
				default -> new PageBuilder<>(template, dto).add(new SingleGenePage(template, footer, sign, page).page()).build().save(baos);
			}
			return baos.toByteArray();
		}

		@Override
		public String buildLongFormText(Request request) throws IOException {
			test = info(request.pk().service());
			StringBuilder sb = new StringBuilder();
			if (dto.report().variants() != null && dto.report().variants().length > 0) {
				for (SingleGeneDto.Variant variant : dto.report().variants()) {
					sb.append(String.format(VARIANT_TABLE_FORMAT, StringUtils.center(variant.gene(), 12), StringUtils.center(variant.dnaChange(), 15),
							StringUtils.center(variant.predictedAa(), 20), StringUtils.center(variant.zygosity(), 6),
							StringUtils.center(variant.omim(), 15), StringUtils.center(variant.inherit(), 6),
							StringUtils.center(variant.clazz(), 6)));
				}
				sb.append("----------------------------------------------------------------------------------------------------\r\n");
				if (dto.report().reference() != null && !dto.report().reference().trim().isEmpty())
					sb.append("Reference sequence : ").append(dto.report().reference()).append("\r\n");
				if (dto.report().omimDisease() != null && !dto.report().omimDisease().trim().isEmpty())
					sb.append("OMIM Disease : ").append(dto.report().omimDisease()).append("\r\n");
				if (dto.report().abbreviation() != null && !dto.report().abbreviation().trim().isEmpty())
					sb.append("Abbreviations : ").append(dto.report().abbreviation()).append("\r\n");
			} else sb.append(StringUtils.center("No identified variant", 100)).append("\r\n")
					.append("----------------------------------------------------------------------------------------------------\r\n");
			StringBuilder addendum = new StringBuilder();
			if (dto.addendum() != null && dto.addendum().variants() != null && dto.addendum().variants().length > 0) {
				StringBuilder sb2 = new StringBuilder();
				for (SingleGeneDto.Variant variant : dto.addendum().variants()) {
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
				String template = "◇ Identified addendum variation(s) : \r\n" +
						"----------------------------------------------------------------------------------------------------\r\n" +
						"    Gene         DNA change        Predicted      Zygosity   OMIM Disease   Inherit     Class\r\n" +
						"                                   AA change\r\n" +
						"----------------------------------------------------------------------------------------------------\r\n" +
						"{:VARIANTS}" +
						"\r\n" +
						"◇ 추가 소견\r\n" +
						"{:INTERPRETATION}\r\n" +
						"\r\n" +
						"\r\n";

				addendum.append(template.replace("{:VARIANTS}", sb2.toString())
						.replace("{:INTERPRETATION}", dto.addendum().interpretation()));
			}
			StringBuilder referral = new StringBuilder();
			if (dto.reasonFR() != null && !dto.reasonFR().trim().isEmpty()) {
				referral.append("◇ 검사의뢰사유 \r\n").append(dto.reasonFR().trim()).append("\r\n\r\n");
			}
			StringBuilder resultText = new StringBuilder();
			switch (test.category()) {
				case WITH_MLPA -> resultText = resultText.append("""
						- {:SEQUENCING-HEADER} : {:SEQUENCING-RESULT}
						- {:MLPA-HEADER} : {:MLPA-RESULT}
						"""
						.replace("{:SEQUENCING-HEADER}", template.lblIndividualResults()[0])
						.replace("{:SEQUENCING-RESULT}", template.formatSequencingResult(dto))
						.replace("{:MLPA-HEADER}", template.lblIndividualResults()[1])
						.replace("{:MLPA-RESULT}", template.formatMlpaResult(dto)));
				default -> resultText.append(dto.report().resultText());
			}
			String[][] labelNamePairs = HasSign.getSanitizedContributorsLabelsAndNames(template, test);
			return TEXT_TEMPLATE.replace("{:TITLE}", test.title())
					.replace("{:REFERRAL}", referral.toString())
					.replace("{:RESULT}", dto.result().toFullText())
					.replace("{:RESULT-TEXT}", resultText.toString())
					.replace("{:VARIANTS}", sb.toString())
					.replace("{:INTERPRETATION}", dto.report().interpretation())
					.replace("{:ADDENDUM}", addendum.toString())
					.replace("{:SPECIMEN}", test.specimen())
					.replace("{:GENE}", test.target())
					.replace("{:METHOD}", test.method())
					.replace("{:LIMITATIONS}", String.join("\r\n", test.limitations()))
					.replace("{:REFERENCES}", String.join("\r\n", test.references()))
					.replace("{:INSPECTOR_LABEL}", labelNamePairs[0][0])
					.replace("{:INSPECTOR}", labelNamePairs[0][1])
					.replace("{:REPORTER_AND_REVIEWER_LABEL}", labelNamePairs[1][0])
					.replace("{:REPORTER}", labelNamePairs[1][1])
					.replace("{:REVIEWER}", labelNamePairs[2][1]);
		}

		@Override
		public String buildShortFormText(Request request) throws IOException {
			test = info(request.pk().service());
			String header = String.format(VARIANT_TABLE_SHORT_FORMAT, VARIANT_TABLE_HEADER[0], VARIANT_TABLE_HEADER[1],
					VARIANT_TABLE_HEADER[2], VARIANT_TABLE_HEADER[3],
					VARIANT_TABLE_HEADER[4], VARIANT_TABLE_HEADER[5],
					VARIANT_TABLE_HEADER[6]);
			StringBuilder sb = new StringBuilder();
			if (dto.report().variants() != null && dto.report().variants().length > 0) {
				for (SingleGeneDto.Variant variant : dto.report().variants()) {
					sb.append(String.format(VARIANT_TABLE_SHORT_FORMAT, variant.gene(), variant.dnaChange(),
							variant.predictedAa(), variant.zygosity(),
							variant.omim(), variant.inherit(),
							variant.clazz()));
				}
				if (dto.report().reference() != null && !dto.report().reference().trim().isEmpty())
					sb.append("Reference sequence : ").append(dto.report().reference()).append("\r\n");
				if (dto.report().omimDisease() != null && !dto.report().omimDisease().trim().isEmpty())
					sb.append("OMIM Disease : ").append(dto.report().omimDisease()).append("\r\n");
				if (dto.report().abbreviation() != null && !dto.report().abbreviation().trim().isEmpty())
					sb.append("Abbreviations : ").append(dto.report().abbreviation()).append("\r\n");
			} else sb.append(StringUtils.center("No identified variant", 100)).append("\r\n");
			StringBuilder addendum = new StringBuilder();
			if (dto.addendum() != null && dto.addendum().variants() != null && dto.addendum().variants().length > 0) {
				StringBuilder sb2 = new StringBuilder();
				for (SingleGeneDto.Variant variant : dto.addendum().variants()) {
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
				String template = "◇ Identified addendum variation(s) : \r\n" +
						"{:HEADER}" +
						"{:VARIANTS}" +
						"\r\n" +
						"◇ Addendum Interpretation\r\n" +
						"{:INTERPRETATION}\r\n" +
						"\r\n" +
						"\r\n";

				addendum.append(template.replace("{:HEADER}", header)
						.replace("{:VARIANTS}", sb2.toString())
						.replace("{:INTERPRETATION}", dto.addendum().interpretation()));
			}
			StringBuilder referral = new StringBuilder();
			if (dto.reasonFR() != null && !dto.reasonFR().trim().isEmpty()) {
				referral.append("◇ 검사의뢰사유 \r\n").append(dto.reasonFR().trim()).append("\r\n\r\n");
			}
			StringBuilder resultText = new StringBuilder();
			switch (test.category()) {
				case WITH_MLPA -> resultText = resultText.append("""
						- {:SEQUENCING-HEADER} : {:SEQUENCING-RESULT}
						- {:MLPA-HEADER} : {:MLPA-RESULT}
						"""
						.replace("{:SEQUENCING-HEADER}", template.lblIndividualResults()[0])
						.replace("{:SEQUENCING-RESULT}", template.formatSequencingResult(dto))
						.replace("{:MLPA-HEADER}", template.lblIndividualResults()[1])
						.replace("{:MLPA-RESULT}", template.formatMlpaResult(dto)));
				default -> resultText.append(dto.report().resultText());
			}
			String[][] labelNamePairs = HasSign.getSanitizedContributorsLabelsAndNames(template, test);
			String result = TEXT_SHORT_TEMPLATE.replace("{:TITLE}", test.title())
					.replace("{:REFERRAL}", referral)
					.replace("{:RESULT}", dto.result().toFullText())
					.replace("{:RESULT-TEXT}", resultText.toString())
					.replace("{:HEADER}", header)
					.replace("{:VARIANTS}", sb.toString())
					.replace("{:INTERPRETATION}", dto.report().interpretation())
					.replace("{:ADDENDUM}", addendum.toString())
					.replace("{:SPECIMEN}", test.specimen())
					.replace("{:GENE}", test.target())
					.replace("{:METHOD}", test.method())
					.replace("{:LIMITATIONS}", String.join("\r\n", test.limitations()))
					.replace("{:REFERENCES}", String.join("\r\n", test.references()))
					.replace("{:INSPECTOR_LABEL}", labelNamePairs[0][0])
					.replace("{:INSPECTOR}", labelNamePairs[0][1])
					.replace("{:REPORTER_AND_REVIEWER_LABEL}", labelNamePairs[1][0])
					.replace("{:REPORTER}", labelNamePairs[1][1])
					.replace("{:REVIEWER}", labelNamePairs[2][1]);
			while (result.contains("  ")) result = result.replace("  ", " ");
			return result;
		}
	}

	private final class CancerPanelReportFactory implements ReportFactory<PanelTest> {

		@Override
		public Class<PanelTest> clazz() {
			return PanelTest.class;
		}

		@Override
		public boolean match(Request request) {
			for (TestInfo test : TestInfo.TESTS)
				if ((test.category() == TestInfo.Category.CANCER || test.category() == TestInfo.Category.BRCA)
						&& test.code().equalsIgnoreCase(request.pk().service())) return true;
			return false;
		}

		private TestInfo info(String service) {
			for (TestInfo test : TestInfo.TESTS)
				if ((test.category() == TestInfo.Category.CANCER || test.category() == TestInfo.Category.BRCA)
						&& test.code().equalsIgnoreCase(service)) return test;
			return null;
		}

		@Override
		public byte[] build(Request request, PanelTest interpretation, Class<PanelTest> clazz) throws IOException {
			PDDocument doc = new PDDocument();
			test = info(request.pk().service());
			dto = dto(test, request, interpretation);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();

            SingleGeneResourceKoKr resource = new SingleGeneResourceKoKr(test, doc)
					.colorPrimary(Color.decode("#E24585"))
					.colorTextWithPrimary(Color.WHITE)
					.colorSecondary(Color.decode("#E24585"))
					.colorPrimaryLine(Color.decode("#E24585"))
					.colorSecondaryLine(Color.decode("#F1A6C5"));
			Painter<SingleGeneTemplate, SingleGeneDto> footer = null;
			Painter<SingleGeneTemplate, SingleGeneDto> sign = null;
			Painter<SingleGeneTemplate, SingleGeneDto> page = null;
			LogoType logoType;

			if (isLabsRequest(request.sample())) {
				logoType = LogoType.DEPENDENT;
				sign = new SectionSign<>(81);
				footer = new com.greencross.lims.report.kokr.SectionFooterLabs<>();
				template = new SingleGeneTemplateKoKr(resource, test, logoType);
				page = new SectionPage<>(547, 81, template.resource().fontDefault());
			} else {
				logoType = LogoType.INDEPENDENT;
				sign = new SectionSign<>(65);
				footer = new com.greencross.lims.report.kokr.SectionFooterGenome<>();
				template = new SingleGeneTemplateKoKr(resource, test, logoType);
				page = new SectionPage<>(547, 65, template.resource().fontDefault());
			}

			new PageBuilder<>(template, dto).add(new SingleGenePage(template, footer, sign, page).page()).build().save(baos);
			return baos.toByteArray();
		}

		@Override
		public String buildLongFormText(Request request) throws IOException {
			test = info(request.pk().service());
			StringBuilder sb = new StringBuilder();
			if (dto.report().variants() != null && dto.report().variants().length > 0)
				for (SingleGeneDto.Variant variant : dto.report().variants()) {
					sb.append(String.format(VARIANT_TABLE_FORMAT, StringUtils.center(variant.gene(), 12), StringUtils.center(variant.dnaChange(), 15),
							StringUtils.center(variant.predictedAa(), 20), StringUtils.center(variant.zygosity(), 6),
							StringUtils.center(variant.omim(), 15), StringUtils.center(variant.inherit(), 6),
							StringUtils.center(variant.clazz(), 6)));
					sb.append("----------------------------------------------------------------------------------------------------\r\n");
					if (dto.report().reference() != null && !dto.report().reference().trim().isEmpty())
						sb.append("Reference sequence : ").append(dto.report().reference()).append("\r\n");
					if (dto.report().omimDisease() != null && !dto.report().omimDisease().trim().isEmpty())
						sb.append("OMIM Disease : ").append(dto.report().omimDisease()).append("\r\n");
					if (dto.report().abbreviation() != null && !dto.report().abbreviation().trim().isEmpty())
						sb.append("Abbreviations : ").append(dto.report().abbreviation()).append("\r\n");
				}
			else sb.append(StringUtils.center("No identified variant", 100)).append("\r\n")
					.append("----------------------------------------------------------------------------------------------------\r\n");
			StringBuilder addendum = new StringBuilder();
			if (dto.addendum() != null && dto.addendum().variants() != null && dto.addendum().variants().length > 0) {
				StringBuilder sb2 = new StringBuilder();
				for (SingleGeneDto.Variant variant : dto.addendum().variants()) {
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
				String template = "◇ Identified addendum variation(s) : \r\n" +
						"----------------------------------------------------------------------------------------------------\r\n" +
						"    Gene         DNA change        Predicted      Zygosity   OMIM Disease   Inherit     Class\r\n" +
						"                                   AA change\r\n" +
						"----------------------------------------------------------------------------------------------------\r\n" +
						"{:VARIANTS}" +
						"\r\n" +
						"◇ 추가 소견\r\n" +
						"{:INTERPRETATION}\r\n" +
						"\r\n" +
						"\r\n";

				addendum.append(template.replace("{:VARIANTS}", sb2.toString())
						.replace("{:INTERPRETATION}", dto.addendum().interpretation()));
			}
			StringBuilder referral = new StringBuilder();
			if (dto.reasonFR() != null && !dto.reasonFR().trim().isEmpty()) {
				referral.append("◇ 검사의뢰사유 \r\n").append(dto.reasonFR().trim()).append("\r\n\r\n");
			}
			String[][] labelNamePairs = HasSign.getSanitizedContributorsLabelsAndNames(template, test);
			return TEXT_TEMPLATE.replace("{:TITLE}", test.title())
					.replace("{:REFERRAL}", referral.toString())
					.replace("{:RESULT}", dto.result().toFullText())
					.replace("{:RESULT-TEXT}", dto.report().resultText())
					.replace("{:VARIANTS}", sb.toString())
					.replace("{:INTERPRETATION}", dto.report().interpretation())
					.replace("{:ADDENDUM}", addendum.toString())
					.replace("{:SPECIMEN}", test.specimen())
					.replace("{:GENE}", test.target())
					.replace("{:METHOD}", test.method())
					.replace("{:LIMITATIONS}", String.join("\r\n", test.limitations()))
					.replace("{:REFERENCES}", String.join("\r\n", test.references()))
					.replace("{:INSPECTOR_LABEL}", labelNamePairs[0][0])
					.replace("{:INSPECTOR}", labelNamePairs[0][1])
					.replace("{:REPORTER_AND_REVIEWER_LABEL}", labelNamePairs[1][0])
					.replace("{:REPORTER}", labelNamePairs[1][1])
					.replace("{:REVIEWER}", labelNamePairs[2][1]);
		}

		@Override
		public String buildShortFormText(Request request) throws IOException {
			test = info(request.pk().service());
			String header = String.format(VARIANT_TABLE_SHORT_FORMAT, VARIANT_TABLE_HEADER[0], VARIANT_TABLE_HEADER[1],
					VARIANT_TABLE_HEADER[2], VARIANT_TABLE_HEADER[3],
					VARIANT_TABLE_HEADER[4], VARIANT_TABLE_HEADER[5],
					VARIANT_TABLE_HEADER[6]);
			StringBuilder sb = new StringBuilder();
			if (dto.report().variants() != null && dto.report().variants().length > 0) {
				for (SingleGeneDto.Variant variant : dto.report().variants()) {
					sb.append(String.format(VARIANT_TABLE_SHORT_FORMAT, variant.gene(), variant.dnaChange(),
							variant.predictedAa(), variant.zygosity(),
							variant.omim(), variant.inherit(),
							variant.clazz()));
				}
				if (dto.report().reference() != null && !dto.report().reference().trim().isEmpty())
					sb.append("Reference sequence : ").append(dto.report().reference()).append("\r\n");
				if (dto.report().omimDisease() != null && !dto.report().omimDisease().trim().isEmpty())
					sb.append("OMIM Disease : ").append(dto.report().omimDisease()).append("\r\n");
				if (dto.report().abbreviation() != null && !dto.report().abbreviation().trim().isEmpty())
					sb.append("Abbreviations : ").append(dto.report().abbreviation()).append("\r\n");
			} else sb.append(StringUtils.center("No identified variant", 100)).append("\r\n");
			StringBuilder addendum = new StringBuilder();
			if (dto.addendum() != null && dto.addendum().variants() != null && dto.addendum().variants().length > 0) {
				StringBuilder sb2 = new StringBuilder();
				for (SingleGeneDto.Variant variant : dto.addendum().variants()) {
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
				String template = "◇ Identified addendum variation(s) : \r\n" +
						"{:HEADER}" +
						"{:VARIANTS}" +
						"\r\n" +
						"◇ Addendum Interpretation\r\n" +
						"{:INTERPRETATION}\r\n" +
						"\r\n" +
						"\r\n";

				addendum.append(template.replace("{:HEADER}", header)
						.replace("{:VARIANTS}", sb2.toString())
						.replace("{:INTERPRETATION}", dto.addendum().interpretation()));
			}
			StringBuilder referral = new StringBuilder();
			if (dto.reasonFR() != null && !dto.reasonFR().trim().isEmpty()) {
				referral.append("◇ 검사의뢰사유 \r\n").append(dto.reasonFR().trim()).append("\r\n\r\n");
			}
			String[][] labelNamePairs = HasSign.getSanitizedContributorsLabelsAndNames(template, test);
			String result = TEXT_SHORT_TEMPLATE.replace("{:TITLE}", test.title())
					.replace("{:REFERRAL}", referral)
					.replace("{:RESULT}", dto.result().toFullText())
					.replace("{:RESULT-TEXT}", dto.report().resultText())
					.replace("{:HEADER}", header)
					.replace("{:VARIANTS}", sb.toString())
					.replace("{:INTERPRETATION}", dto.report().interpretation())
					.replace("{:ADDENDUM}", addendum.toString())
					.replace("{:SPECIMEN}", test.specimen())
					.replace("{:GENE}", test.target())
					.replace("{:METHOD}", test.method())
					.replace("{:LIMITATIONS}", String.join("\r\n", test.limitations()))
					.replace("{:REFERENCES}", String.join("\r\n", test.references()))
					.replace("{:INSPECTOR_LABEL}", labelNamePairs[0][0])
					.replace("{:INSPECTOR}", labelNamePairs[0][1])
					.replace("{:REPORTER_AND_REVIEWER_LABEL}", labelNamePairs[1][0])
					.replace("{:REPORTER}", labelNamePairs[1][1])
					.replace("{:REVIEWER}", labelNamePairs[2][1]);
			while (result.contains("  ")) result = result.replace("  ", " ");
			return result;
		}
	}


	private static final String VARIANT_TABLE_FORMAT = "%1$-15s%2$-10s%3$-10s%4$-10s%5$-12s%6$-12s%7$-5s\r\n";
	private static final String[] VARIANT_TABLE_HEADER = new String[]{
			"Gene",
			"DNA change",
			"AA change",
			"Zygosity",
			"OMIM",
			"Inherit",
			"Class"
	};
	private static final String TEXT_TEMPLATE =
			"◆ {:TITLE}\r\n" +
					"\r\n" +
					"{:REFERRAL}◇ Result : {:RESULT}\r\n" +
					"{:RESULT-TEXT}\r\n" +
					"\r\n" +
					"◇ Identified variation(s) : \r\n" +
					"----------------------------------------------------------------------------------------------------\r\n" +
					"    Gene         DNA change        Predicted      Zygosity   OMIM Disease   Inherit     Class\r\n" +
					"                                   AA change\r\n" +
					"----------------------------------------------------------------------------------------------------\r\n" +
					"{:VARIANTS}" +
					"\r\n" +
					"◇ Interpretation\r\n" +
					"{:INTERPRETATION}\r\n" +
					"\r\n" +
					"\r\n" +
					"{:ADDENDUM}◇ Methods\r\n" +
					"----------------------------------------------------------------------------------------------------\r\n" +
					"Specimen: {:SPECIMEN}\r\n" +
					"Analysed Gene: {:GENE}\r\n" +
					"Methods: {:METHOD}\r\n" +
					"----------------------------------------------------------------------------------------------------\r\n" +
					"\r\n" +
					"◇ Limitation\r\n" +
					"{:LIMITATIONS}\r\n" +
					"\r\n" +
					"◇ References\r\n" +
					"{:REFERENCES}\r\n" +
					"\r\n" +
					"{:INSPECTOR_LABEL} {:INSPECTOR} / {:REPORTER_AND_REVIEWER_LABEL} {:REPORTER}, {:REVIEWER}";
	private static final String VARIANT_TABLE_SHORT_FORMAT = "%s\t%s\t%s\t%s\t%s\t%s\t%s\r\n";
	private static final String TEXT_SHORT_TEMPLATE =
			"◆ {:TITLE}\r\n" +
					"\r\n" +
					"{:REFERRAL}◇ Result : {:RESULT}\r\n" +
					"{:RESULT-TEXT}\r\n" +
					"\r\n" +
					"◇ Identified variation(s) : \r\n" +
					"{:HEADER}" +
					"{:VARIANTS}" +
					"\r\n" +
					"◇ Interpretation\r\n" +
					"{:INTERPRETATION}\r\n" +
					"\r\n" +
					"\r\n" +
					"{:ADDENDUM}◇ Methods\r\n" +
					"Specimen: {:SPECIMEN}\r\n" +
					"Analysed Gene: {:GENE}\r\n" +
					"Methods: {:METHOD}\r\n" +
					"\r\n" +
					"◇ Limitation\r\n" +
					"{:LIMITATIONS}\r\n" +
					"\r\n" +
					"◇ References\r\n" +
					"{:REFERENCES}\r\n" +
					"\r\n" +
					"{:INSPECTOR_LABEL} {:INSPECTOR} / {:REPORTER_AND_REVIEWER_LABEL} {:REPORTER}, {:REVIEWER}";
}

