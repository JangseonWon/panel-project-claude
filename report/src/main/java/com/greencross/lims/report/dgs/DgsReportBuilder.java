package com.greencross.lims.report.dgs;

import com.gcgenome.lims.report.func.PageBuilder;
import com.gcgenome.lims.report.func.Painter;
import com.gcgenome.lims.test.dgs.TestInfo;
import com.gcgenome.lims.test.wes.TestWithSingleInfo;
import com.greencross.lims.dto.interpretation.Dgs;
import com.greencross.lims.entity.Patient;
import com.greencross.lims.entity.Request;
import com.greencross.lims.entity.Sample;
import com.greencross.lims.report.HasSign;
import com.greencross.lims.report.ReportFactory;
import com.greencross.lims.report.SectionLDT;
import com.greencross.lims.report.SectionPage;
import com.greencross.lims.report.builder.LogoType;
import com.greencross.lims.report.builder.Sex;
import com.greencross.lims.report.dgs.enus.DgsHeaderEnUs;
import com.greencross.lims.report.dgs.enus.DgsResourceEnUs;
import com.greencross.lims.report.dgs.enus.DgsTemplateEnUs;
import com.greencross.lims.report.dgs.kokr.DgsHeaderKoKr;
import com.greencross.lims.report.dgs.kokr.DgsResourceKoKr;
import com.greencross.lims.report.dgs.kokr.DgsTemplateKoKr;
import com.greencross.lims.report.kokr.SectionSign;
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
public class DgsReportBuilder implements ReportFactory<Dgs> {
    private TestInfo test;
    private DgsDto dto;
    private DgsTemplate template;

    @Override
    public Class<Dgs> clazz() {
        return Dgs.class;
    }

    private TestInfo info(String service) {
        for (TestInfo test : TestInfo.TESTS) if (test.code().equalsIgnoreCase(service)) return test;
        return null;
    }

    @Override
    public boolean match(Request request) {
        for (TestInfo test : TestInfo.TESTS) if (test.code().equalsIgnoreCase(request.pk().service())) return true;
        return false;
    }

    @Override
    public byte[] build(Request request, Dgs value, Class<Dgs> clazz) throws IOException {
        PDDocument doc = new PDDocument();
        test = info(request.pk().service());
        dto = dto(test, request, value);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        DgsHeader header;
        Painter<DgsTemplate, DgsDto> ldt;
        Painter<DgsTemplate, DgsDto> footer;
        Painter<DgsTemplate, DgsDto> sign;
        Painter<DgsTemplate, DgsDto> pn;
        LogoType logoType;

        if ("KOKR".equalsIgnoreCase(test.i18n())) {
            template = new DgsTemplateKoKr(new DgsResourceKoKr(doc, dto), test);
            header = new DgsHeaderKoKr(template);
            logoType = LogoType.INDEPENDENT;
            ldt = new SectionLDT<>(logoType, 100);
            sign = new SectionSign<>(65);
            footer = new com.greencross.lims.report.kokr.SectionFooterGenome<>();
            pn = new SectionPage<>(565, 63, template.resource().fontDefault());
        } else {
            template = new DgsTemplateEnUs(new DgsResourceEnUs(doc, dto), test);
            header = new DgsHeaderEnUs(template);
            logoType = LogoType.INDEPENDENT;
            ldt = new SectionLDT<>(logoType, 100);
            footer = new com.greencross.lims.report.enus.SectionFooterGenome<>();
            sign = new com.greencross.lims.report.enus.SectionSign<>(65);
            pn = new SectionPage<>(565, 65, template.resource().fontDefault());
        }
        new PageBuilder<>(template, dto).add(new DgsPage(template, header, ldt, footer, sign, pn).page()).build().save(baos);
        return baos.toByteArray();
    }

    @Override
    public String buildLongFormText(Request request) {
        test = info(request.service().id());
        StringBuilder sb = new StringBuilder();
        if (dto.report().variants() != null && dto.report().variants().length > 0) {
            for (DgsDto.Variant variant : dto.report().variants()) {
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
        StringBuilder incidentalFindings = new StringBuilder();
        if (dto.consentIncidentalFindings()) {
            incidentalFindings.append("\r\n◇ Incidental findings\r\n");
            if (dto.incidentalFindings() != null && dto.incidentalFindings().variants() != null && dto.incidentalFindings().variants().length > 0) {
                StringBuilder sb2 = new StringBuilder();
                for (DgsDto.Variant variant : dto.incidentalFindings().variants()) {
                    sb2.append(String.format(VARIANT_TABLE_FORMAT, StringUtils.center(variant.gene(), 12), StringUtils.center(variant.dnaChange(), 15),
                            StringUtils.center(variant.predictedAa(), 20), StringUtils.center(variant.zygosity(), 6),
                            StringUtils.center(variant.omim(), 15), StringUtils.center(variant.inherit(), 6),
                            StringUtils.center(variant.clazz(), 6)));
                }
                sb2.append("----------------------------------------------------------------------------------------------------\r\n");
                if (dto.incidentalFindings().reference() != null && !dto.incidentalFindings().reference().trim().isEmpty())
                    sb2.append("Reference sequence : ").append(dto.incidentalFindings().reference()).append("\r\n");
                if (dto.incidentalFindings().omimDisease() != null && !dto.incidentalFindings().omimDisease().trim().isEmpty())
                    sb2.append("Disease : ").append(dto.incidentalFindings().omimDisease()).append("\r\n");
                if (dto.incidentalFindings().abbreviation() != null && !dto.incidentalFindings().abbreviation().trim().isEmpty())
                    sb2.append("Abbreviations : ").append(dto.incidentalFindings().abbreviation()).append("\r\n");
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
                        .replace("{:INTERPRETATION}", dto.incidentalFindings().interpretation() + "\r\n" + lblIncidentalFindingInfo));
            } else {
                String template = "{:INTERPRETATION}\r\n" +
                        "\r\n" +
                        "\r\n";
                incidentalFindings.append(template.replace("{:INTERPRETATION}", dto.incidentalFindings().interpretation() + "\r\n" + lblIncidentalFindingInfo));
            }
        }
        String[][] labelNamePairs = HasSign.getSanitizedContributorsLabelsAndNames(template, test);

        return TEXT_TEMPLATE.replace("{:NAME}", test.title())
                .replace("{:REFERRAL}", dto.reasonFR())
                .replace("{:RESULT-TEXT}", dto.report().resultText())
                .replace("{:VARIANTS}", sb.toString())
                .replace("{:INTERPRETATION}", dto.report().interpretation())
                .replace("{:RECOMMENDATION}", dto.recommendation())
                .replace("{:INCIDENTAL-FINDINGS}", incidentalFindings.toString())
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
        return null;
    }

    private static final Pattern BIRTHDAT_PATTERN = Pattern.compile("^\\d{6}$");
    private static final Pattern BIRTHDAT_PATTERN2 = Pattern.compile("^(\\d{6})(-)*(\\d)$");

    private DgsDto dto(TestInfo test, Request request, Dgs value) {
        Objects.requireNonNull(request.sample(), "Sample cannot be null");
        Objects.requireNonNull(request.sample().patient(), "Patient cannot be null");

        DgsDto dto = new DgsDto();
        Sample sample = request.sample();
        Patient patient = sample.patient();

        Double.parseDouble(value.coverage());
        Double.parseDouble(value.meanDepth());

        String patientCode = patient.code();
        if (patient.code() != null && BIRTHDAT_PATTERN.matcher(patient.code()).find())
            patientCode = patient.code() + "-*******";
        else if (patient.code() != null && BIRTHDAT_PATTERN2.matcher(patient.code()).find()) {
            Matcher m = BIRTHDAT_PATTERN2.matcher(patient.code());
            m.find();
            patientCode = m.group(1) + "-" + m.group(3) + "******";
        }
        dto.code(request.pk().service())
                .reasonFR(value.reasonForReferral())
                .result(DgsDto.Result.from(value.result()))
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
        DgsDto.Report report = new DgsDto.Report().resultText(value.resultText());
        if (value.variants() != null)
            report.variants(Arrays.stream(value.variants()).map(DgsReportBuilder::map).toArray(DgsDto.Variant[]::new));
        report.reference(value.abbreviationReference()).omimDisease(value.abbreviationDisease()).abbreviation(value.abbreviation())
                .interpretation(value.interpretation());
        dto.report(report)
                .recommendation(value.recommendation())
                .meanDepth(value.meanDepth()).x10Coverage(value.coverage())
                .inspector(value.inspector()).reporter(value.reporter()).reviewer(value.reviewer());
        request.customInfos().stream().filter(i -> "TA0023".equals(i.code())).findFirst().ifPresent(barcode -> dto.barcode(barcode.value()));
        request.customInfos().stream().filter(i -> "TA0028".equals(i.code())).findFirst().ifPresent(relation -> dto.relation(relation.value()));
        dto.revision(value.revision());
        if (value.consentIncidentalFindings() == null) value.consentIncidentalFindings(false);
        dto.consentIncidentalFindings(value.consentIncidentalFindings());
        if (value.consentIncidentalFindings() && value.incidentalFindings() != null) {
            var inc = value.incidentalFindings();
            DgsDto.Report incidental = new DgsDto.Report()
                    .reference(inc.abbreviationReference()).omimDisease(inc.abbreviationDisease()).abbreviation(inc.abbreviation())
                    .interpretation(inc.interpretation());
            incidental.variants(Arrays.stream(inc.variants()).map(DgsReportBuilder::map).toArray(DgsDto.Variant[]::new));
            dto.incidentalFindings(incidental);
        }
        return dto;
    }

    private static DgsDto.Variant map(Dgs.Variant dto) {
        return new DgsDto.Variant().gene(dto.gene())
                .dnaChange(dto.hgvsc())
                .predictedAa(dto.hgvsp())
                .zygosity(dto.zygosity())
                .omim(dto.disease())
                .inherit(dto.inheritance())
                .clazz(dto.clazz());
    }

    private String lblIncidentalFindingInfo = String.format(
            "* Investigation of %d genes recommended by ACMG SF v3.3 (Genet Med. 2025.)",
            TestWithSingleInfo.GENE_INCIDENTAL_FINDINGS.length
    );
    private static final String VARIANT_TABLE_FORMAT = "%1$-15s%2$-10s%3$-10s%4$-10s%5$-12s%6$-12s%7$-5s\r\n";
    private static final String TEXT_TEMPLATE =
            "◆ {:NAME}\r\n" +
                    "\r\n" +
                    "◇ Clinical information \r\n" +
                    "{:REFERRAL}\r\n" +
                    "\r\n" +
                    "◇ Result \r\n" +
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
                    "◇ Recommendations\r\n" +
                    "{:RECOMMENDATION}\r\n" +
                    "{:INCIDENTAL-FINDINGS}" +
                    "\r\n" +
                    "◇ Coverage\r\n" +
                    "Mean depth of coverage(X): {:DEPTH}\r\n" +
                    "% of Target Bases ≥ 10X: {:COVERAGE}\r\n" +
                    "\r\n" +
                    "◇ Methods\r\n" +
                    "{:METHOD}\r\n" +
                    "\r\n" +
                    "◇ Limitations\r\n" +
                    "{:LIMITATIONS}\r\n" +
                    "\r\n" +
                    "{:INSPECTOR_LABEL} {:INSPECTOR} / {:REPORTER_AND_REVIEWER_LABEL} {:REPORTER}, {:REVIEWER}";
}
