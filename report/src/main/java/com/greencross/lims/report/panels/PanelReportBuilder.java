package com.greencross.lims.report.panels;

import com.gcgenome.lims.report.func.PageBuilder;
import com.gcgenome.lims.report.func.Painter;
import com.gcgenome.lims.test.panel.TestInfo;
import com.greencross.lims.dto.interpretation.PanelTest;
import com.greencross.lims.entity.Patient;
import com.greencross.lims.entity.Request;
import com.greencross.lims.entity.Sample;
import com.greencross.lims.report.HasSign;
import com.greencross.lims.report.ReportFactory;
import com.greencross.lims.report.SectionLDT;
import com.greencross.lims.report.SectionPage;
import com.greencross.lims.report.builder.LogoType;
import com.greencross.lims.report.builder.Sex;
import com.greencross.lims.report.kokr.SectionFooterLabs;
import com.greencross.lims.report.kokr.SectionSign;
import com.greencross.lims.report.panels.enus.PanelHeaderEnUs;
import com.greencross.lims.report.panels.enus.PanelResourceEnUs;
import com.greencross.lims.report.panels.enus.PanelTemplateEnUs;
import com.greencross.lims.report.panels.kokr.PanelHeaderKoKr;
import com.greencross.lims.report.panels.kokr.PanelResourceKoKr;
import com.greencross.lims.report.panels.kokr.PanelTemplateKoKr;
import org.apache.commons.lang.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.greencross.lims.report.ReportUtil.*;

@Component
public class PanelReportBuilder implements ReportFactory<PanelTest> {
	private PanelDto dto;
	private TestInfo test;
	private PanelTemplate template;

	private final List<ReportFactory<PanelTest>> delegates = List.of(new CancerPanelReportFactory(), new RareDiseasePanelReportFactory());
	@Override
	public Class<PanelTest> clazz() {
		return PanelTest.class;
	}
	@Override
	public boolean match(Request request) {
		return delegates.stream().anyMatch(d->d.match(request));
	}
	@Override
	public byte[] build(Request request, PanelTest value, Class<PanelTest> clazz) {
		return delegates.stream().filter(d->d.match(request)).findFirst().map(d->{
			try {
				return d.build(request, value);
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
			}
			catch (IOException e) {
				throw new RuntimeException(e);
			}
		});
	}
	private <T> T processDelegate(Request request, Function<ReportFactory<PanelTest>, T> mapper) {
		return delegates.stream()
				.filter(d -> d.match(request))
				.findFirst()
				.map(d -> mapper.apply(d))
				.orElseThrow(RuntimeException::new);
	}
	private static final Pattern BIRTHDAT_PATTERN = Pattern.compile("^\\d{6}$");
	private static final Pattern BIRTHDAT_PATTERN2 = Pattern.compile("^(\\d{6})(-)*(\\d)$");
	private PanelDto dto(TestInfo test, Request request, PanelTest value) {
		Objects.requireNonNull(request.sample(), "Sample cannot be null");
		Objects.requireNonNull(request.sample().patient(), "Patient cannot be null");

		PanelDto dto = new PanelDto();
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
		   .result(PanelDto.Result.from(value.result()))
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
		PanelDto.Report report = new PanelDto.Report().resultText(value.resultText());
		if(value.variants()!=null) report.variants(Arrays.stream(value.variants()).map(PanelReportBuilder::map).toArray(PanelDto.Variant[]::new));
		report.reference(value.abbreviationReference()).omimDisease(value.abbreviationDisease()).abbreviation(value.abbreviation())
			  .interpretation(value.interpretation());
		dto.report(report).meanDepth(value.meanDepth()).x10Coverage(value.coverage());
		if(value.addendum()!=null) {
			PanelTest cast2 = value.addendum();
			PanelDto.Report addendum = new PanelDto.Report().resultText(cast2.resultText());
			if(cast2.variants()!=null) addendum.variants(Arrays.stream(cast2.variants()).map(PanelReportBuilder::map).toArray(PanelDto.Variant[]::new));
			addendum.reference(cast2.abbreviationReference()).omimDisease(cast2.abbreviationDisease()).abbreviation(cast2.abbreviation())
					.interpretation(cast2.interpretation());
			dto.addendum(addendum);
		}
		request.customInfos().stream().filter(i->"TA0023".equals(i.code())).findFirst().ifPresent(barcode->dto.barcode(barcode.value()));
		request.customInfos().stream().filter(i->"TA0028".equals(i.code())).findFirst().ifPresent(relation->dto.relation(relation.value()));
		return dto;
	}
	private final class CancerPanelReportFactory implements ReportFactory<PanelTest> {
		@Override
		public Class<PanelTest> clazz() {
			return PanelTest.class;
		}
		@Override
		public boolean match(Request request) {
			if("ON001".equalsIgnoreCase(request.pk().service()) || "ON040".equalsIgnoreCase(request.pk().service())) return false;
			for(TestInfo test: TestInfo.TESTS) if(test.category() == TestInfo.Category.CANCER && test.code().equalsIgnoreCase(request.pk().service())) return true;
			return false;
		}
		private TestInfo info(String service) {
			for(TestInfo test: TestInfo.TESTS) if(test.category() == TestInfo.Category.CANCER && test.code().equalsIgnoreCase(service)) return test;
			return null;
		}
		@Override
		public byte[] build(Request request, PanelTest value, Class<PanelTest> clazz) throws IOException {
			PDDocument doc = new PDDocument();
			test = info(request.pk().service());
			dto = dto(test, request, value);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();

			PanelHeader header = null;
			Painter<PanelTemplate, PanelDto> ldt = null;
			Painter<PanelTemplate, PanelDto> footer = null;
			Painter<PanelTemplate, PanelDto> sign = null;
			Painter<PanelTemplate, PanelDto> page = null;
			LogoType logoType;

			if("KOKR".equalsIgnoreCase(test.i18n())) {
				var resource = new PanelResourceKoKr(test, doc)
						.colorPrimary(Color.decode("#E24585"))
						.colorTextWithPrimary(Color.WHITE)
						.colorSecondary(Color.decode("#E24585"))
						.colorPrimaryLine(Color.decode("#E24585"))
						.colorSecondaryLine(Color.decode("#F1A6C5"));
				if(isLabsRequest(request.sample())) {
					logoType = LogoType.DEPENDENT;
					ldt = new SectionLDT<>(logoType, 120);
					sign = new SectionSign<>(81);
					footer = new SectionFooterLabs<>();
					page = new SectionPage<>(539, 81, resource.fontDefault());
				} else {
					logoType = LogoType.INDEPENDENT;
					ldt = new SectionLDT<>(logoType, 90);
					sign = new SectionSign<>(65);
					footer = new com.greencross.lims.report.kokr.SectionFooterGenome<>();
					page = new SectionPage<>(547, 65, resource.fontDefault());
				}
				template = new PanelTemplateKoKr(resource, test, logoType);
				header = new PanelHeaderKoKr(template);
			} else if("ENUS".equalsIgnoreCase(test.i18n())) {
				var resource = new PanelResourceEnUs(test, doc).colorPrimary(Color.decode("#E24585"))
						.colorTextWithPrimary(Color.WHITE)
						.colorSecondary(Color.decode("#E24585"))
						.colorPrimaryLine(Color.decode("#E24585"))
						.colorSecondaryLine(Color.decode("#F1A6C5"));
				if(isLabsRequest(request.sample())) {
					logoType = LogoType.DEPENDENT;
					ldt = new SectionLDT<>(logoType, 120);
					sign = new com.greencross.lims.report.enus.SectionSign<>(80);
					footer = new com.greencross.lims.report.enus.SectionFooterLabs<>();
					page = new SectionPage<>(539, 81, resource.fontDefault());
				} else {
					logoType = LogoType.INDEPENDENT;
					ldt = new SectionLDT<>(logoType, 120);
					sign = new com.greencross.lims.report.enus.SectionSign<>(65);
					footer = new com.greencross.lims.report.enus.SectionFooterGenome<>();
					page = new SectionPage<>(565, 65, resource.fontDefault());
				}
				template = new PanelTemplateEnUs(resource, test, logoType);
				header = new PanelHeaderEnUs(template);
			}
			new PageBuilder<>(template, dto).add(new PanelPage(template, header, ldt, footer, sign, page).page()).build().save(baos);
			return baos.toByteArray();
		}

		@Override
		public String buildLongFormText(Request request) {
			test = info(request.service().id());
			StringBuilder sb = new StringBuilder();
			if(dto.report().variants()!=null && dto.report().variants().length > 0) {
				for(PanelDto.Variant variant: dto.report().variants()) {
					sb.append(String.format(VARIANT_TABLE_FORMAT, StringUtils.center(variant.gene(), 12), StringUtils.center(variant.dnaChange(), 15),
							StringUtils.center(variant.predictedAa(), 20), StringUtils.center(variant.zygosity(),6),
							StringUtils.center(variant.omim(), 15), StringUtils.center(variant.inherit(), 6),
							StringUtils.center(variant.clazz(), 6)));
				}
				sb.append("----------------------------------------------------------------------------------------------------\r\n");
				if(dto.report().reference()!=null && !dto.report().reference().trim().isEmpty()) sb.append("Reference sequence : ").append(dto.report().reference()).append("\r\n");
				if(dto.report().omimDisease()!=null && !dto.report().omimDisease().trim().isEmpty()) sb.append("OMIM Disease : ").append(dto.report().omimDisease()).append("\r\n");
				if(dto.report().abbreviation()!=null && !dto.report().abbreviation().trim().isEmpty()) sb.append("Abbreviations : ").append(dto.report().abbreviation()).append("\r\n");
			} else {
				sb.append(StringUtils.center("No identified variant", 100)).append("\r\n");
				sb.append("----------------------------------------------------------------------------------------------------\r\n");
			}
			StringBuilder addendum = new StringBuilder();
			if(dto.addendum()!=null && dto.addendum().variants()!=null && dto.addendum().variants().length > 0) {
				StringBuilder sb2 = new StringBuilder();
				for(PanelDto.Variant variant: dto.addendum().variants()) {
					sb2.append(String.format(VARIANT_TABLE_FORMAT, StringUtils.center(variant.gene(), 12), StringUtils.center(variant.dnaChange(), 15),
							StringUtils.center(variant.predictedAa(), 20), StringUtils.center(variant.zygosity(),6),
							StringUtils.center(variant.omim(), 15), StringUtils.center(variant.inherit(), 6),
							StringUtils.center(variant.clazz(), 6)));
				}
				sb2.append("----------------------------------------------------------------------------------------------------\r\n");
				if(dto.addendum().reference()!=null && !dto.addendum().reference().trim().isEmpty()) sb2.append("Reference sequence : ").append(dto.addendum().reference()).append("\r\n");
				if(dto.addendum().omimDisease()!=null && !dto.addendum().omimDisease().trim().isEmpty()) sb2.append("Disease : ").append(dto.addendum().omimDisease()).append("\r\n");
				if(dto.addendum().abbreviation()!=null && !dto.addendum().abbreviation().trim().isEmpty()) sb2.append("Abbreviations : ").append(dto.addendum().abbreviation()).append("\r\n");
				String template =  "◇ Identified addendum variation(s) : \r\n" +
						"----------------------------------------------------------------------------------------------------\r\n" +
						"    Gene         DNA change        Predicted      Zygosity   OMIM Disease   Inherit     Class\r\n" +
						"                                   AA change\r\n" +
						"----------------------------------------------------------------------------------------------------\r\n" +
						"{:VARIANTS}"+
						"\r\n" +
						"◇ Addendum Interpretation\r\n" +
						"{:INTERPRETATION}\r\n" +
						"\r\n" +
						"\r\n";

				addendum.append(template.replace("{:VARIANTS}", sb2.toString())
						.replace("{:INTERPRETATION}", dto.addendum().interpretation()));
			}
			String[][] labelNamePairs = HasSign.getSanitizedContributorsLabelsAndNames(template, test);
			return TEXT_TEMPLATE.replace("{:NAME}", test.name())
					.replace("{:REFERRAL}", dto.reasonFR())
					.replace("{:RESULT-SUMMARY}", dto.result().toFullText())
					.replace("{:RESULT-TEXT}", dto.report().resultText())
					.replace("{:VARIANTS}", sb.toString())
					.replace("{:INTERPRETATION}", dto.report().interpretation())
					.replace("{:ADDENDUM}", addendum.toString())
					.replace("{:PANEL}", test.panel())
					.replace("{:REGION}", test.region())
					.replace("{:PROBE}", test.probe())
					.replace("{:SEQUENCING}", test.sequencing())
					.replace("{:REFERENCE}", test.reference())
					.replace("{:PIPELINE}", test.pipeline())
					.replace("{:DEPTH}", dto.meanDepth())
					.replace("{:COVERAGE}", dto.x10Coverage())
					.replace("{:LIMITATION}", template.lblLimitations())
					.replace("{:CORES}", String.join(", ", test.tier1()))
					.replace("{:INSPECTOR_LABEL}", labelNamePairs[0][0])
					.replace("{:INSPECTOR}", labelNamePairs[0][1])
					.replace("{:REPORTER_AND_REVIEWER_LABEL}", labelNamePairs[1][0])
					.replace("{:REPORTER}", labelNamePairs[1][1])
					.replace("{:REVIEWER}", labelNamePairs[2][1]);
		}
		@Override
		public String buildShortFormText(Request request) {
			test = info(request.service().id());
			String header = String.format(VARIANT_TABLE_SHORT_FORMAT, VARIANT_TABLE_HEADER[0], VARIANT_TABLE_HEADER[1],
					VARIANT_TABLE_HEADER[2], VARIANT_TABLE_HEADER[3],
					VARIANT_TABLE_HEADER[4], VARIANT_TABLE_HEADER[5],
					VARIANT_TABLE_HEADER[6]);
			StringBuilder sb = new StringBuilder();
			if(dto.report().variants()!=null && dto.report().variants().length > 0) {
				for(PanelDto.Variant variant: dto.report().variants()) {
					sb.append(String.format(VARIANT_TABLE_SHORT_FORMAT, variant.gene(), variant.dnaChange(),
							variant.predictedAa(), variant.zygosity(),
							variant.omim(), variant.inherit(),
							variant.clazz()));
				}
				if(dto.report().reference()!=null && !dto.report().reference().trim().isEmpty()) sb.append("Reference sequence : ").append(dto.report().reference()).append("\r\n");
				if(dto.report().omimDisease()!=null && !dto.report().omimDisease().trim().isEmpty()) sb.append("OMIM Disease : ").append(dto.report().omimDisease()).append("\r\n");
				if(dto.report().abbreviation()!=null && !dto.report().abbreviation().trim().isEmpty()) sb.append("Abbreviations : ").append(dto.report().abbreviation()).append("\r\n");
			} else sb.append(StringUtils.center("No identified variant", 100)).append("\r\n");
			StringBuilder addendum = new StringBuilder();
			if(dto.addendum()!=null && dto.addendum().variants()!=null && dto.addendum().variants().length > 0) {
				StringBuilder sb2 = new StringBuilder();
				for(PanelDto.Variant variant: dto.addendum().variants()) {
					sb2.append(String.format(VARIANT_TABLE_SHORT_FORMAT, variant.gene(), variant.dnaChange(),
							variant.predictedAa(), variant.zygosity(),
							variant.omim(), variant.inherit(),
							variant.clazz()));
				}
				if(dto.addendum().reference()!=null && !dto.addendum().reference().trim().isEmpty()) sb2.append("Reference sequence : ").append(dto.addendum().reference()).append("\r\n");
				if(dto.addendum().omimDisease()!=null && !dto.addendum().omimDisease().trim().isEmpty()) sb2.append("Disease : ").append(dto.addendum().omimDisease()).append("\r\n");
				if(dto.addendum().abbreviation()!=null && !dto.addendum().abbreviation().trim().isEmpty()) sb2.append("Abbreviations : ").append(dto.addendum().abbreviation()).append("\r\n");
				String template =  "◇ Identified addendum variation(s) : \r\n" +
						"{:HEADER}"+
						"{:VARIANTS}"+
						"\r\n" +
						"◇ Addendum Interpretation\r\n" +
						"{:INTERPRETATION}\r\n" +
						"\r\n" +
						"\r\n";
				addendum.append(template.replace("{:HEADER}", header)
						.replace("{:VARIANTS}", sb2.toString())
						.replace("{:INTERPRETATION}", dto.addendum().interpretation()));
			}
			String[][] labelNamePairs = HasSign.getSanitizedContributorsLabelsAndNames(template, test);
			String result = TEXT_SHORT_TEMPLATE.replace("{:NAME}", test.name())
					.replace("{:REFERRAL}", dto.reasonFR())
					.replace("{:RESULT-SUMMARY}", dto.result().toFullText())
					.replace("{:RESULT-TEXT}", dto.report().resultText())
					.replace("{:HEADER}", header)
					.replace("{:VARIANTS}", sb.toString())
					.replace("{:INTERPRETATION}", dto.report().interpretation())
					.replace("{:ADDENDUM}", addendum.toString())
					.replace("{:PANEL}", test.panel())
					.replace("{:REGION}", test.region())
					.replace("{:PROBE}", test.probe())
					.replace("{:SEQUENCING}", test.sequencing())
					.replace("{:REFERENCE}", test.reference())
					.replace("{:PIPELINE}", test.pipeline())
					.replace("{:DEPTH}", dto.meanDepth())
					.replace("{:COVERAGE}", dto.x10Coverage())
					.replace("{:INSPECTOR_LABEL}", labelNamePairs[0][0])
					.replace("{:INSPECTOR}", labelNamePairs[0][1])
					.replace("{:REPORTER_AND_REVIEWER_LABEL}", labelNamePairs[1][0])
					.replace("{:REPORTER}", labelNamePairs[1][1])
					.replace("{:REVIEWER}", labelNamePairs[2][1]);
			while(result.contains("  ")) result = result.replace("  ", " ");
			return result;
		}
	}
	private final class RareDiseasePanelReportFactory implements ReportFactory<PanelTest> {
		@Override
		public Class<PanelTest> clazz() {
			return PanelTest.class;
		}
		@Override
		public boolean match(Request request) {
			for(TestInfo test: TestInfo.TESTS) if(test.category() == TestInfo.Category.RARE && test.code().equalsIgnoreCase(request.pk().service())) return true;
			return false;
		}
		private TestInfo info(String service) {
			for(TestInfo test: TestInfo.TESTS) if(test.category() == TestInfo.Category.RARE && test.code().equalsIgnoreCase(service)) return test;
			return null;
		}
		@Override
		public byte[] build(Request request, PanelTest value, Class<PanelTest> clazz) throws IOException {
			PDDocument doc = new PDDocument();
			test = info(request.pk().service());
			dto = dto(test, request, value);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();

			template = null;
			PanelHeader header = null;
			Painter<PanelTemplate, PanelDto> ldt = null;
			Painter<PanelTemplate, PanelDto> footer = null;
			Painter<PanelTemplate, PanelDto> sign = null;
			Painter<PanelTemplate, PanelDto> page = null;
			LogoType logoType;

			if("KOKR".equalsIgnoreCase(test.i18n())) {
				var resource = new PanelResourceKoKr(test, doc);
				if(isLabsRequest(request.sample())) {
					logoType = LogoType.DEPENDENT;
					ldt = new SectionLDT<>(logoType, 120);
					sign = new SectionSign<>(81);
					footer = new com.greencross.lims.report.kokr.SectionFooterLabs<>();
					page = new SectionPage<>(539, 81, resource.fontDefault());
				} else {
					logoType = LogoType.INDEPENDENT;
					ldt = new SectionLDT<>(logoType, 90);
					sign = new SectionSign<>(65);
					footer = new com.greencross.lims.report.kokr.SectionFooterGenome<>();
					page = new SectionPage<>(547, 65, resource.fontDefault());
				}
				template = new PanelTemplateKoKr(resource, test, logoType);
				header = new PanelHeaderKoKr(template);
			} else if("ENUS".equalsIgnoreCase(test.i18n())) {
				var resource = new PanelResourceEnUs(test, doc);
				if(request.pk().service() == "ON122") {
					logoType = LogoType.DEPENDENT;
					ldt = new SectionLDT<>(logoType, 120);
					sign = new com.greencross.lims.report.enus.SectionSign<>(80);
					footer = new com.greencross.lims.report.enus.SectionFooterGenomeLabs<>();
					page = new SectionPage<>(539, 81, resource.fontDefault());
				}
				else if(isLabsRequest(request.sample())) {
					logoType = LogoType.DEPENDENT;
					ldt = new SectionLDT<>(logoType, 120);
					sign = new com.greencross.lims.report.enus.SectionSign<>(80);
					footer = new com.greencross.lims.report.enus.SectionFooterLabs<>();
					page = new SectionPage<>(539, 81, resource.fontDefault());
				} else {
					logoType = LogoType.INDEPENDENT;
					ldt = new SectionLDT<>(logoType, 120);
					sign = new com.greencross.lims.report.enus.SectionSign<>(65);
					footer = new com.greencross.lims.report.enus.SectionFooterGenome<>();
					page = new SectionPage<>(565, 65, resource.fontDefault());
				}
				template = new PanelTemplateEnUs(resource, test, logoType);
				header = new PanelHeaderEnUs(template);
			}
			new PageBuilder<>(template, dto).add(new PanelPage(template, header, ldt, footer, sign, page).page()).build().save(baos);
			return baos.toByteArray();
		}
		@Override
		public String buildLongFormText(Request request) {
			test = info(request.service().id());
			StringBuilder sb = new StringBuilder();
			if(dto.report().variants()!=null && dto.report().variants().length > 0) {
				for(PanelDto.Variant variant: dto.report().variants()) {
					sb.append(String.format(VARIANT_TABLE_FORMAT, StringUtils.center(variant.gene(), 12), StringUtils.center(variant.dnaChange(), 15),
							StringUtils.center(variant.predictedAa(), 20), StringUtils.center(variant.zygosity(),6),
							StringUtils.center(variant.omim(), 15), StringUtils.center(variant.inherit(), 6),
							StringUtils.center(variant.clazz(), 6)));
				}
				sb.append("----------------------------------------------------------------------------------------------------\r\n");
				if(dto.report().reference()!=null && !dto.report().reference().trim().isEmpty()) sb.append("Reference sequence : ").append(dto.report().reference()).append("\r\n");
				if(dto.report().omimDisease()!=null && !dto.report().omimDisease().trim().isEmpty()) sb.append("OMIM Disease : ").append(dto.report().omimDisease()).append("\r\n");
				if(dto.report().abbreviation()!=null && !dto.report().abbreviation().trim().isEmpty()) sb.append("Abbreviations : ").append(dto.report().abbreviation()).append("\r\n");
			} else {
				sb.append(StringUtils.center("No identified variant", 100)).append("\r\n");
				sb.append("----------------------------------------------------------------------------------------------------\r\n");
			}
			StringBuilder addendum = new StringBuilder();
			if(dto.addendum()!=null && dto.addendum().variants()!=null && dto.addendum().variants().length > 0) {
				StringBuilder sb2 = new StringBuilder();
				for(PanelDto.Variant variant: dto.addendum().variants()) {
					sb2.append(String.format(VARIANT_TABLE_FORMAT, StringUtils.center(variant.gene(), 12), StringUtils.center(variant.dnaChange(), 15),
							StringUtils.center(variant.predictedAa(), 20), StringUtils.center(variant.zygosity(),6),
							StringUtils.center(variant.omim(), 15), StringUtils.center(variant.inherit(), 6),
							StringUtils.center(variant.clazz(), 6)));
				}
				sb2.append("----------------------------------------------------------------------------------------------------\r\n");
				if(dto.addendum().reference()!=null && !dto.addendum().reference().trim().isEmpty()) sb2.append("Reference sequence : ").append(dto.addendum().reference()).append("\r\n");
				if(dto.addendum().omimDisease()!=null && !dto.addendum().omimDisease().trim().isEmpty()) sb2.append("Disease : ").append(dto.addendum().omimDisease()).append("\r\n");
				if(dto.addendum().abbreviation()!=null && !dto.addendum().abbreviation().trim().isEmpty()) sb2.append("Abbreviations : ").append(dto.addendum().abbreviation()).append("\r\n");
				String template =  "◇ Identified addendum variation(s) : \r\n" +
						"----------------------------------------------------------------------------------------------------\r\n" +
						"    Gene         DNA change        Predicted      Zygosity   OMIM Disease   Inherit     Class\r\n" +
						"                                   AA change\r\n" +
						"----------------------------------------------------------------------------------------------------\r\n" +
						"{:VARIANTS}"+
						"\r\n" +
						"◇ Addendum Interpretation\r\n" +
						"{:INTERPRETATION}\r\n" +
						"\r\n" +
						"\r\n";

				addendum.append(template.replace("{:VARIANTS}", sb2.toString())
						.replace("{:INTERPRETATION}", dto.addendum().interpretation()));
			}
			String[][] labelNamePairs = HasSign.getSanitizedContributorsLabelsAndNames(template, test);
			return TEXT_TEMPLATE.replace("{:NAME}", test.name())
					.replace("{:REFERRAL}", dto.reasonFR())
					.replace("{:RESULT-SUMMARY}", dto.result().toFullText())
					.replace("{:RESULT-TEXT}", dto.report().resultText())
					.replace("{:VARIANTS}", sb.toString())
					.replace("{:INTERPRETATION}", dto.report().interpretation())
					.replace("{:ADDENDUM}", addendum.toString())
					.replace("{:PANEL}", test.panel())
					.replace("{:REGION}", test.region())
					.replace("{:PROBE}", test.probe())
					.replace("{:SEQUENCING}", test.sequencing())
					.replace("{:REFERENCE}", test.reference())
					.replace("{:PIPELINE}", test.pipeline())
					.replace("{:DEPTH}", dto.meanDepth())
					.replace("{:COVERAGE}", dto.x10Coverage())
					.replace("{:LIMITATION}", template.lblLimitations())
					.replace("{:CORES}", String.join(", ", test.tier1()))
					.replace("{:INSPECTOR_LABEL}", labelNamePairs[0][0])
					.replace("{:INSPECTOR}", labelNamePairs[0][1])
					.replace("{:REPORTER_AND_REVIEWER_LABEL}", labelNamePairs[1][0])
					.replace("{:REPORTER}", labelNamePairs[1][1])
					.replace("{:REVIEWER}", labelNamePairs[2][1]);
		}
		@Override
		public String buildShortFormText(Request request) {
			test = info(request.service().id());
			String header = String.format(VARIANT_TABLE_SHORT_FORMAT, VARIANT_TABLE_HEADER[0], VARIANT_TABLE_HEADER[1],
					VARIANT_TABLE_HEADER[2], VARIANT_TABLE_HEADER[3],
					VARIANT_TABLE_HEADER[4], VARIANT_TABLE_HEADER[5],
					VARIANT_TABLE_HEADER[6]);
			StringBuilder sb = new StringBuilder();
			if(dto.report().variants()!=null && dto.report().variants().length > 0) {
				for(PanelDto.Variant variant: dto.report().variants()) {
					sb.append(String.format(VARIANT_TABLE_SHORT_FORMAT, variant.gene(), variant.dnaChange(),
							variant.predictedAa(), variant.zygosity(),
							variant.omim(), variant.inherit(),
							variant.clazz()));
				}
				if(dto.report().reference()!=null && !dto.report().reference().trim().isEmpty()) sb.append("Reference sequence : ").append(dto.report().reference()).append("\r\n");
				if(dto.report().omimDisease()!=null && !dto.report().omimDisease().trim().isEmpty()) sb.append("OMIM Disease : ").append(dto.report().omimDisease()).append("\r\n");
				if(dto.report().abbreviation()!=null && !dto.report().abbreviation().trim().isEmpty()) sb.append("Abbreviations : ").append(dto.report().abbreviation()).append("\r\n");
			} else sb.append(StringUtils.center("No identified variant", 100)).append("\r\n");
			StringBuilder addendum = new StringBuilder();
			if(dto.addendum()!=null && dto.addendum().variants()!=null && dto.addendum().variants().length > 0) {
				StringBuilder sb2 = new StringBuilder();
				for(PanelDto.Variant variant: dto.addendum().variants()) {
					sb2.append(String.format(VARIANT_TABLE_SHORT_FORMAT, variant.gene(), variant.dnaChange(),
							variant.predictedAa(), variant.zygosity(),
							variant.omim(), variant.inherit(),
							variant.clazz()));
				}
				if(dto.addendum().reference()!=null && !dto.addendum().reference().trim().isEmpty()) sb2.append("Reference sequence : ").append(dto.addendum().reference()).append("\r\n");
				if(dto.addendum().omimDisease()!=null && !dto.addendum().omimDisease().trim().isEmpty()) sb2.append("Disease : ").append(dto.addendum().omimDisease()).append("\r\n");
				if(dto.addendum().abbreviation()!=null && !dto.addendum().abbreviation().trim().isEmpty()) sb2.append("Abbreviations : ").append(dto.addendum().abbreviation()).append("\r\n");
				String template =  "◇ Identified addendum variation(s) : \r\n" +
						"{:HEADER}"+
						"{:VARIANTS}"+
						"\r\n" +
						"◇ Addendum Interpretation\r\n" +
						"{:INTERPRETATION}\r\n" +
						"\r\n" +
						"\r\n";
				addendum.append(template.replace("{:HEADER}", header)
						.replace("{:VARIANTS}", sb2.toString())
						.replace("{:INTERPRETATION}", dto.addendum().interpretation()));
			}
			String[][] labelNamePairs = HasSign.getSanitizedContributorsLabelsAndNames(template, test);
			String result = TEXT_SHORT_TEMPLATE.replace("{:NAME}", test.name())
					.replace("{:REFERRAL}", dto.reasonFR())
					.replace("{:RESULT-SUMMARY}", dto.result().toFullText())
					.replace("{:RESULT-TEXT}", dto.report().resultText())
					.replace("{:HEADER}", header)
					.replace("{:VARIANTS}", sb.toString())
					.replace("{:INTERPRETATION}", dto.report().interpretation())
					.replace("{:ADDENDUM}", addendum.toString())
					.replace("{:PANEL}", test.panel())
					.replace("{:REGION}", test.region())
					.replace("{:PROBE}", test.probe())
					.replace("{:SEQUENCING}", test.sequencing())
					.replace("{:REFERENCE}", test.reference())
					.replace("{:PIPELINE}", test.pipeline())
					.replace("{:DEPTH}", dto.meanDepth())
					.replace("{:COVERAGE}", dto.x10Coverage())
					.replace("{:INSPECTOR_LABEL}", labelNamePairs[0][0])
					.replace("{:INSPECTOR}", labelNamePairs[0][1])
					.replace("{:REPORTER_AND_REVIEWER_LABEL}", labelNamePairs[1][0])
					.replace("{:REPORTER}", labelNamePairs[1][1])
					.replace("{:REVIEWER}", labelNamePairs[2][1]);
			while(result.contains("  ")) result = result.replace("  ", " ");
			return result;
		}
	}
	private static PanelDto.Variant map(PanelTest.Variant dto) {
		return new PanelDto.Variant().gene(dto.gene())
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
					"◇ Reason for Referral\r\n" +
					"{:REFERRAL}\r\n" +
					"\r\n" +
					"◇ Result : {:RESULT-SUMMARY}\r\n" +
					"{:RESULT-TEXT}\r\n" +
					"\r\n" +
					"◇ Identified variation(s) : \r\n" +
					"----------------------------------------------------------------------------------------------------\r\n" +
					"    Gene         DNA change        Predicted      Zygosity   OMIM Disease   Inherit     Class\r\n" +
					"                                   AA change\r\n" +
					"----------------------------------------------------------------------------------------------------\r\n" +
					"{:VARIANTS}"+
					"\r\n" +
					"◇ Interpretation\r\n" +
					"{:INTERPRETATION}\r\n" +
					"\r\n" +
					"\r\n" +
					"{:ADDENDUM}◇ Methods\r\n" +
					"Test Panel: {:PANEL}\r\n" +
					"Target region: {:REGION}\r\n" +
					"Target enrichment method: {:PROBE}\r\n" +
					"Massively parallel sequencing: {:SEQUENCING}\r\n" +
					"Reference genome: {:REFERENCE}\r\n" +
					"Bioinformatic pipeline: {:PIPELINE}\r\n" +
					"\r\n" +
					"◇ Coverage\r\n" +
					"Mean depth of coverage(X): {:DEPTH}\r\n" +
					"% of Target Bases ≥ 10X: {:COVERAGE}\r\n" +
					"\r\n" +
					"◇ Limitation\r\n" +
					"{:LIMITATION}\r\n" +
					"\r\n" +
					"◇ Gene List\r\n" +
					"Essential genes: {:CORES}\r\n" +
					"\r\n" +
					"{:INSPECTOR_LABEL} {:INSPECTOR} / {:REPORTER_AND_REVIEWER_LABEL} {:REPORTER}, {:REVIEWER}";
	private static final String VARIANT_TABLE_SHORT_FORMAT = "%s\t%s\t%s\t%s\t%s\t%s\t%s\r\n";
	private static final String TEXT_SHORT_TEMPLATE =
			"◆ {:NAME}\r\n" +
					"\r\n" +
					"◇ Reason for Referral \r\n" +
					"{:REFERRAL}\r\n" +
					"\r\n" +
					"◇ Result : {:RESULT-SUMMARY}\r\n" +
					"{:RESULT-TEXT}\r\n" +
					"\r\n" +
					"◇ Identified variation(s) : \r\n" +
					"{:HEADER}"+
					"{:VARIANTS}"+
					"\r\n" +
					"◇ Interpretation\r\n" +
					"{:INTERPRETATION}\r\n" +
					"\r\n" +
					"\r\n" +
					"{:ADDENDUM}◇ Methods\r\n" +
					"Test Panel: {:PANEL}\r\n" +
					"Target region: {:REGION}\r\n" +
					"Target enrichment method: {:PROBE}\r\n" +
					"Massively parallel sequencing: {:SEQUENCING}\r\n" +
					"Reference genome: {:REFERENCE}\r\n" +
					"Bioinformatic pipeline: {:PIPELINE}\r\n" +
					"\r\n" +
					"◇ Coverage\r\n" +
					"Mean depth of coverage(X): {:DEPTH}\r\n" +
					"% of Target Bases ≥ 10X: {:COVERAGE}\r\n" +
					"\r\n" +
					"◇ Limitation\r\n" +
					"검사 결과지를 참고하십시오.\r\n" +
					"\r\n" +
					"◇ Gene List\r\n" +
					"검사 결과지를 참고하십시오.\r\n" +
					"\r\n" +
					"{:INSPECTOR_LABEL} {:INSPECTOR} / {:REPORTER_AND_REVIEWER_LABEL} {:REPORTER}, {:REVIEWER}";
}
