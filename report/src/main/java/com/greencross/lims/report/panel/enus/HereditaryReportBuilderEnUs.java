package com.greencross.lims.report.panel.enus;

import com.gcgenome.lims.report.func.Painter;
import com.gcgenome.lims.test.panel.TestInfo;
import com.greencross.lims.dto.interpretation.PanelTest;
import com.greencross.lims.entity.Patient;
import com.greencross.lims.entity.Request;
import com.greencross.lims.entity.Sample;
import com.greencross.lims.report.ReportFactory;
import com.greencross.lims.report.SectionPage;
import com.greencross.lims.report.builder.LogoType;
import com.greencross.lims.report.builder.Sex;
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
public class HereditaryReportBuilderEnUs implements ReportFactory<PanelTest> {
    @Override
    public Class<PanelTest> clazz() {
        return PanelTest.class;
    }

    private TestInfo info(String service) {
        for (TestInfo test : TestInfo.TESTS) if (test.code().equalsIgnoreCase(service)) return test;
        return null;
    }

    @Override
    public boolean match(Request request) {
        if ("ON001".equalsIgnoreCase(request.pk().service())) return true;
        if ("ON040".equalsIgnoreCase(request.pk().service())) return true;
        return false;
    }

    private HereditaryTemplateEnUs template(TestInfo test, LogoType logoType) throws IOException {
        PDDocument doc = new PDDocument();
        if (TestInfo.ON001 == test)
            return new HereditaryTemplateEnUsON001(new HereditaryResourceEnUsON001(doc), test, logoType);
        else if (TestInfo.ON040 == test)
            return new HereditaryTemplateEnUsON040(new HereditaryResourceEnUsON040(doc), test, logoType);
        else return null;
    }

    private HereditaryEnUsPageBuilder<?> builder(TestInfo test, HereditaryTemplateEnUs template, HereditaryEnUsDto dto,
                                                 Painter<HereditaryTemplateEnUs, HereditaryEnUsDto> sign,
                                                 Painter<HereditaryTemplateEnUs, HereditaryEnUsDto> footer,
                                                 Painter<HereditaryTemplateEnUs, HereditaryEnUsDto> page) {
        if (TestInfo.ON001 == test)
            return new HereditaryON001((HereditaryTemplateEnUsON001) template, dto, sign, footer, page);
        else if (TestInfo.ON040 == test)
            return new HereditaryON040((HereditaryTemplateEnUsON040) template, dto, sign, footer, page);
        else return null;
    }

    @Override
    public byte[] build(Request request, PanelTest value, Class<PanelTest> clazz) throws IOException {
        TestInfo test = info(request.pk().service());
        Painter<HereditaryTemplateEnUs, HereditaryEnUsDto> sign;
        Painter<HereditaryTemplateEnUs, HereditaryEnUsDto> footer;
        Painter<HereditaryTemplateEnUs, HereditaryEnUsDto> page;
        HereditaryTemplateEnUs template;
        LogoType logoType;
        if (isLabsRequest(request.sample()) || test == TestInfo.ON040) {
            logoType = LogoType.DEPENDENT;
            footer = new com.greencross.lims.report.enus.SectionFooterGenomeLabs<>();
            sign = new com.greencross.lims.report.enus.SectionSign<>(60);
            template = template(test, logoType);
            page = new SectionPage<>(542, 60, template.resource().fontDefault());
        } else {
            logoType = LogoType.INDEPENDENT;
            footer = new com.greencross.lims.report.enus.SectionFooterGenome<>();
            sign = new com.greencross.lims.report.enus.SectionSign<>(65);
            template = template(test, logoType);
            page = new SectionPage<>(547, 65, template.resource().fontDefault());
        }
        HereditaryEnUsDto dto = dto(test, request, value);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        builder(test, template, dto, sign, footer, page).build().save(baos);
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

    private static final Pattern BIRTHDATE_PATTERN = Pattern.compile("^\\d{6}$");
    private static final Pattern BIRTHDATE_PATTERN2 = Pattern.compile("^(\\d{6})(-)*(\\d)$");

    private HereditaryEnUsDto dto(TestInfo test, Request request, PanelTest value) {
        Objects.requireNonNull(request.sample(), "Sample cannot be null");
        Objects.requireNonNull(request.sample().patient(), "Patient cannot be null");

        HereditaryEnUsDto dto = new HereditaryEnUsDto();
        Sample sample = request.sample();
        Patient patient = sample.patient();

        Double.parseDouble(value.coverage());
        Double.parseDouble(value.meanDepth());

        String patientCode = patient.code();
        if (patient.code() != null && BIRTHDATE_PATTERN.matcher(patient.code()).find())
            patientCode = patient.code() + "-*******";
        else if (patient.code() != null && BIRTHDATE_PATTERN2.matcher(patient.code()).find()) {
            Matcher m = BIRTHDATE_PATTERN2.matcher(patient.code());
            m.find();
            patientCode = m.group(1) + "-" + m.group(3) + "******";
        }
        dto.code(request.pk().service())
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
        request.customInfos().stream().filter(i -> "TA0023".equals(i.code())).findFirst().ifPresent(barcode -> dto.barcode(barcode.value()));
        HereditaryEnUsDto.Report report = new HereditaryEnUsDto.Report().resultText(value.resultText());
        if (value.variants() != null)
            report.variants(Arrays.stream(value.variants()).map(this::map).toArray(HereditaryEnUsDto.Variant[]::new));
        report.reference(value.abbreviationReference()).omimDisease(value.abbreviationDisease()).abbreviation(value.abbreviation()).interpretation(value.interpretation());
        dto.report(report).result(HereditaryEnUsDto.Result.from(value.result())).meanDepth(value.meanDepth()).x10Coverage(value.coverage());
        return dto;
    }

    private HereditaryEnUsDto.Variant map(PanelTest.Variant dto) {
        return new HereditaryEnUsDto.Variant().gene(dto.gene()).dnaChange(dto.hgvsc())
                .predictedAa(dto.hgvsp()).zygosity(dto.zygosity()).omim(dto.disease()).inherit(dto.inheritance())
                .clazz(dto.clazz());
    }
}
