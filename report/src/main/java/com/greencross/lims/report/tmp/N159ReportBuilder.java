package com.greencross.lims.report.tmp;

import com.gcgenome.lims.report.func.PageBuilder;
import com.gcgenome.lims.report.func.Painter;
import com.greencross.lims.dto.interpretation.tmp.N159;
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
public class N159ReportBuilder implements ReportFactory<N159> {
	@Override
	public Class<N159> clazz() {
		return N159.class;
	}
	@Override
	public boolean match(Request request) {
		return com.gcgenome.lims.test.tmp.N159.builder().build().code().equalsIgnoreCase(request.pk().service());
	}
	private com.gcgenome.lims.test.tmp.N159 info(String service) {
		return com.gcgenome.lims.test.tmp.N159.builder().build();
	}
	@Override
	public byte[] build(Request request, N159 value, Class<N159> clazz) throws IOException {
		PDDocument doc = new PDDocument();
		com.gcgenome.lims.test.tmp.N159 test = info(request.pk().service());
		N159Dto dto = dto(test, request, value);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		N159Template template = new N159TemplateKoKr(new N159ResourceKoKr(doc), test);
		Painter<N159Template, N159Dto> ldt;
		Painter<N159Template, N159Dto> sign;
		Painter<N159Template, N159Dto> footer;
		Painter<N159Template, N159Dto> pn;
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
		new PageBuilder<>(template, dto).add(new N159Page(template, ldt, footer, sign, pn).page()).build().save(baos);
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
	private N159Dto dto(com.gcgenome.lims.test.tmp.N159 test, Request request, N159 value) {
		Objects.requireNonNull(request.sample(), "Sample cannot be null");
		Objects.requireNonNull(request.sample().patient(), "Patient cannot be null");

		N159Dto dto = new N159Dto();
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
		   .result(N159Dto.Result.from(value.result()))
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
		N159Dto.Report report = new N159Dto.Report().resultText(value.resultText());
		if(value.variants()!=null) report.variants(Arrays.stream(value.variants()).map(N159ReportBuilder::map).toArray(N159Dto.Variant[]::new));
		report.reference(value.abbreviationReference())
			  .interpretation(value.interpretation());
		dto.report(report);
		request.customInfos().stream().filter(i->"TA0023".equals(i.code())).findFirst().ifPresent(barcode->dto.barcode(barcode.value()));
		return dto;
	}
	private static N159Dto.Variant map(N159.Variant dto) {
		return new N159Dto.Variant().gene(dto.gene())
								 .dnaChange(dto.hgvsc())
								 .predictedAa(dto.hgvsp())
								 .vaf(dto.vaf())
								 .depth(dto.depth())
								 .cosmic(dto.cosmic())
								 .tier(dto.tier().toString());
	}
}
