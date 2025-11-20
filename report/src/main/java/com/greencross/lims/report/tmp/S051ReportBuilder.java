package com.greencross.lims.report.tmp;

import com.gcgenome.lims.report.func.PageBuilder;
import com.gcgenome.lims.report.func.Painter;
import com.gcgenome.lims.test.tmp.S051;
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
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.greencross.lims.report.ReportUtil.*;

@Component
public class S051ReportBuilder implements ReportFactory<com.gcgenome.lims.dto.interpretation.tmp.S051Dto> {
	private S051Dto dto;
	private S051 test;
	private S051Template template;
	@Override
	public Class<com.gcgenome.lims.dto.interpretation.tmp.S051Dto> clazz() {
		return com.gcgenome.lims.dto.interpretation.tmp.S051Dto.class;
	}
	@Override
	public boolean match(Request request) {
		return S051.instance.code().equalsIgnoreCase(request.pk().service());
	}
	private S051 info(String service) {
		return S051.instance;
	}
	@Override
	public byte[] build(Request request, com.gcgenome.lims.dto.interpretation.tmp.S051Dto value, Class<com.gcgenome.lims.dto.interpretation.tmp.S051Dto> clazz) throws IOException {
		PDDocument doc = new PDDocument();
		test = info(request.pk().service());
		dto = dto(test, request, value);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Painter<S051Template, S051Dto> ldt;
		Painter<S051Template, S051Dto> sign;
		Painter<S051Template, S051Dto> footer;
		Painter<S051Template, S051Dto> pn;
		LogoType logoType;

		if(isLabsRequest(request.sample())) {
			logoType = LogoType.DEPENDENT;
			ldt = new SectionLDT<>(logoType, 120);
			sign = new SectionSign<>(80);
			footer = new com.greencross.lims.report.kokr.SectionFooterLabs<>();
			template = new S051TemplateKoKr(new S051ResourceKoKr(doc), test);
			pn = new SectionPage<>(565, 80, template.resource().fontDefault());
		} else {
			logoType = LogoType.INDEPENDENT;
			ldt = new SectionLDT<>(logoType, 90);
			sign = new SectionSign<>(65);
			footer = new com.greencross.lims.report.kokr.SectionFooterGenome<>();
			template = new S051TemplateKoKr(new S051ResourceKoKr(doc), test);
			pn = new SectionPage<>(565, 65, template.resource().fontDefault());
		}
		new PageBuilder<>(template, dto).add(new S051Page(template, ldt, footer, sign, pn).page()).build().save(baos);
		return baos.toByteArray();
	}

	@Override
	public String buildLongFormText(Request request) throws IOException {
		test = info(request.service().id());
		String[][] labelNamePairs = HasSign.getSanitizedContributorsLabelsAndNames(template, test);
		String result = TEXT_TEMPLATE.replace("{:NAME}", test.name())
				.replace("{:RESULT}", dto.result())
				.replace("{:SUMMARY}", dto.summary()!=null?dto.summary():"")
				.replace("{:INTERPRETATION}", dto.interpretation())
				.replace("{:METHOD}", test.method())
				.replace("{:GENE}", test.gene())
				.replace("{:INSPECTOR_LABEL}", labelNamePairs[0][0])
				.replace("{:INSPECTOR}", labelNamePairs[0][1])
				.replace("{:REPORTER_AND_REVIEWER_LABEL}", labelNamePairs[1][0])
				.replace("{:REPORTER}", labelNamePairs[1][1])
				.replace("{:REVIEWER}", labelNamePairs[2][1]);
		if(dto.header()!=null && !dto.header().isEmpty()) result = result.replace("{:HEADER}", dto.header() +"\r\n\r\n");
		else result.replace("{:HEADER}", "");
		return result;	}

	@Override
	public String buildShortFormText(Request request) throws IOException {
		return buildLongFormText(request);
	}

	private static final Pattern BIRTHDAT_PATTERN = Pattern.compile("^\\d{6}$");
	private static final Pattern BIRTHDAT_PATTERN2 = Pattern.compile("^(\\d{6})(-)*(\\d)$");
	private S051Dto dto(S051 test, Request request, com.gcgenome.lims.dto.interpretation.tmp.S051Dto value) {
		Objects.requireNonNull(request.sample(), "Sample cannot be null");
		Objects.requireNonNull(request.sample().patient(), "Patient cannot be null");

		S051Dto dto = new S051Dto();
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
		   .header(value.header())
		   .result(value.result())
		   .summary(value.summary())
		   .interpretation(value.interpretation())
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
		request.customInfos().stream().filter(i->"TA0023".equals(i.code())).findFirst().ifPresent(barcode->dto.barcode(barcode.value()));
		return dto;
	}
	private static final String TEXT_TEMPLATE =
			"◆ {:NAME}\r\n" +
					"\r\n" +
					"{:HEADER}◇ 검사결과 \r\n" +
					"----------------------------------------------------------------------------------------------------\r\n" +
					"FLT3-ITD mutation : {:RESULT} {:SUMMARY}\r\n" +
					"----------------------------------------------------------------------------------------------------\r\n" +
					"\r\n" +
					"◇ 소견\r\n" +
					"{:INTERPRETATION}\r\n" +
					"\r\n" +
					"\r\n" +
					"◇ 검사정보\r\n" +
					"----------------------------------------------------------------------------------------------------\r\n" +
					"검사방법: {:METHOD}\r\n" +
					"검사대상: {:GENE}\r\n" +
					"----------------------------------------------------------------------------------------------------\r\n" +
					"\r\n" +
					"{:INSPECTOR_LABEL} {:INSPECTOR} / {:REPORTER_AND_REVIEWER_LABEL} {:REPORTER}, {:REVIEWER}";
}
