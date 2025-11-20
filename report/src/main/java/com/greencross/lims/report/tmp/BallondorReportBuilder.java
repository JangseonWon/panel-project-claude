package com.greencross.lims.report.tmp;

import com.gcgenome.lims.report.func.PageBuilder;
import com.gcgenome.lims.report.func.Painter;
import com.gcgenome.lims.test.tmp.Ballondor;
import com.greencross.lims.entity.Patient;
import com.greencross.lims.entity.Request;
import com.greencross.lims.entity.Sample;
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
import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.greencross.lims.report.ReportUtil.*;

@Component
public class BallondorReportBuilder implements ReportFactory<com.gcgenome.lims.dto.interpretation.tmp.BallondorDto> {
	@Override
	public Class<com.gcgenome.lims.dto.interpretation.tmp.BallondorDto> clazz() {
		return com.gcgenome.lims.dto.interpretation.tmp.BallondorDto.class;
	}
	@Override
	public boolean match(Request request) {
		return Arrays.stream(Ballondor.TESTS).anyMatch(t->t.code().equalsIgnoreCase(request.pk().service()));
	}
	private Ballondor info(String service) {
		return Arrays.stream(Ballondor.TESTS).filter(t->t.code().equalsIgnoreCase(service)).findFirst().get();
	}
	@Override
	public byte[] build(Request request, com.gcgenome.lims.dto.interpretation.tmp.BallondorDto value, Class<com.gcgenome.lims.dto.interpretation.tmp.BallondorDto> clazz) throws IOException {
		PDDocument doc = new PDDocument();
		Ballondor test = info(request.pk().service());
		BallondorDto dto = dto(test, request, value);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		Painter<BallondorTemplate, BallondorDto> footer;
		Painter<BallondorTemplate, BallondorDto> sign;
		Painter<BallondorTemplate, BallondorDto> ldt;
		Painter<BallondorTemplate, BallondorDto> pn;
		BallondorTemplate template = new BallondorTemplateKoKr(new BallondorResourceKoKr(doc), test);
		LogoType logoType;

		if(isLabsRequest(request.sample())) {
			logoType = LogoType.DEPENDENT;
			ldt = new SectionLDT<>(logoType, 120);
			sign = new SectionSign<>(80);
			footer = new com.greencross.lims.report.kokr.SectionFooterLabs<>();
			pn = new SectionPage<>(565, 80, template.resource().fontDefault());

		} else {
			logoType = LogoType.INDEPENDENT;
			ldt = new SectionLDT<>(logoType, 90);
			sign = new SectionSign<>(65);
			footer = new com.greencross.lims.report.kokr.SectionFooterGenome<>();
			pn = new SectionPage<>(565, 65, template.resource().fontDefault());
		}

		new PageBuilder<>(template, dto).add(new BallondorPage(template, ldt, footer, sign, pn).page()).build().save(baos);
		return baos.toByteArray();
	}

	@Override
	public String buildLongFormText(Request request) {
		return null;
	}

	@Override
	public String buildShortFormText(Request request) {
		return null;
	}

	private static final Pattern BIRTHDAT_PATTERN = Pattern.compile("^\\d{6}$");
	private static final Pattern BIRTHDAT_PATTERN2 = Pattern.compile("^(\\d{6})(-)*(\\d)$");
	private BallondorDto dto(Ballondor test, Request request, com.gcgenome.lims.dto.interpretation.tmp.BallondorDto value) {
		Objects.requireNonNull(request.sample(), "Sample cannot be null");
		Objects.requireNonNull(request.sample().patient(), "Patient cannot be null");

		BallondorDto dto = new BallondorDto();
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
				.result(value.result())
				.summary(value.summary())
				.hgvsc(value.hgvsc())
				.vaf(value.vaf())
				.sample(value.sample())
				.info(value.info())
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
}
