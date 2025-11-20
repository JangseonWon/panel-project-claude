package com.greencross.lims.report.sanger;

import com.gcgenome.lims.report.func.PageBuilder;
import com.gcgenome.lims.report.func.Painter;
import com.gcgenome.lims.test.sanger.TestInfo;
import com.greencross.lims.dto.interpretation.Sanger;
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
import com.greencross.lims.report.sanger.enus.HeaderEnUs;
import com.greencross.lims.report.sanger.enus.SangerResourceEnUs;
import com.greencross.lims.report.sanger.enus.SangerTemplateEnUs;
import com.greencross.lims.report.sanger.kokr.HeaderKoKr;
import com.greencross.lims.report.sanger.kokr.SangerResourceKoKr;
import com.greencross.lims.report.sanger.kokr.SangerTemplateKoKr;
import org.apache.commons.lang.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Base64;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.greencross.lims.report.ReportUtil.*;

@Component
public class SangerReportBuilder implements ReportFactory<Sanger> {
    private SangerDto dto;
    private TestInfo test;
    private SangerTemplate template;

    @Override
    public Class<Sanger> clazz() {
        return Sanger.class;
    }

    @Override
    public boolean match(Request request) {
        for (TestInfo test : TestInfo.TESTS) if (test.code().equalsIgnoreCase(request.pk().service())) return true;
        return false;
    }

    private TestInfo info(String service) {
        for (TestInfo test : TestInfo.TESTS) if (test.code().equalsIgnoreCase(service)) return test;
        return null;
    }

    @Override
    public byte[] build(Request request, Sanger value, Class<Sanger> clazz) throws IOException {
        PDDocument doc = new PDDocument();
        test = info(request.pk().service());
        dto = dto(test, request, value);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        SangerHeader header = null;
        Painter<SangerTemplate, SangerDto> ldt = null;
        Painter<SangerTemplate, SangerDto> footer = null;
        Painter<SangerTemplate, SangerDto> sign = null;
        Painter<SangerTemplate, SangerDto> pn = null;
        LogoType logoType;

        if ("KOKR".equalsIgnoreCase(test.i18n())) {
            template = new SangerTemplateKoKr(new SangerResourceKoKr(doc, dto), test);
            header = new HeaderKoKr(template);
            if (isLabsRequest(request.sample())) {
				if(request.pk().service() == "S022") {
					logoType = LogoType.DEPENDENT;
					ldt = new SectionLDT<>(logoType, 120);
					sign = new SectionSign<>(80);
					footer = new com.greencross.lims.report.kokr.SectionFooterLabs<>();
					pn = new SectionPage<>(539, 80, template.resource().fontDefault());
				} else {
					logoType = LogoType.DEPENDENT;
					ldt = new SectionLDT<>(logoType, 120);
					sign = new SectionSign<>(80);
					footer = new com.greencross.lims.report.kokr.SectionFooterGenomeLabs<>();
					pn = new SectionPage<>(539, 80, template.resource().fontDefault());
				}
            } else {
                logoType = LogoType.INDEPENDENT;
                ldt = new SectionLDT<>(logoType, 90);
                sign = new SectionSign<>(65);
                footer = new com.greencross.lims.report.kokr.SectionFooterGenome<>();
                pn = new SectionPage<>(565, 65, template.resource().fontDefault());
            }
        } else if ("ENUS".equalsIgnoreCase(test.i18n())) {
            template = new SangerTemplateEnUs(new SangerResourceEnUs(doc, dto), test);
            header = new HeaderEnUs(template);
			if (isLabsRequest(request.sample())) {
				if(request.pk().service() == "S022") {
					logoType = LogoType.DEPENDENT;
					ldt = new SectionLDT<>(logoType, 100);
					footer = new com.greencross.lims.report.enus.SectionFooterLabs<>();
					sign = new com.greencross.lims.report.enus.SectionSign<>(60);
					pn = new SectionPage<>(539, 60, template.resource().fontDefault());
				} else {
					logoType = LogoType.DEPENDENT;
					ldt = new SectionLDT<>(logoType, 100);
					footer = new com.greencross.lims.report.enus.SectionFooterGenomeLabs<>();
					sign = new com.greencross.lims.report.enus.SectionSign<>(60);
					pn = new SectionPage<>(539, 60, template.resource().fontDefault());
				}
            } else {
                logoType = LogoType.INDEPENDENT;
                ldt = new SectionLDT<>(logoType, 100);
                footer = new com.greencross.lims.report.enus.SectionFooterGenome<>();
                sign = new com.greencross.lims.report.enus.SectionSign<>(65);
                pn = new SectionPage<>(565, 65, template.resource().fontDefault());
            }
        }
        new PageBuilder<>(template, dto).add(new SangerPage(template, header, ldt, footer, sign, pn).page()).build().save(baos);
        return baos.toByteArray();
    }

    @Override
    public String buildLongFormText(Request request) {
        test = info(request.service().id());
        String header = String.format(VARIANT_TABLE_FORMAT, StringUtils.center(VARIANT_TABLE_HEADER[0], 12), StringUtils.center(VARIANT_TABLE_HEADER[1], 15),
                StringUtils.center(VARIANT_TABLE_HEADER[2], 20), StringUtils.center(VARIANT_TABLE_HEADER[3], 6),
                StringUtils.center(VARIANT_TABLE_HEADER[4], 12), StringUtils.center(VARIANT_TABLE_HEADER[5], 15));
        StringBuilder sb = new StringBuilder();
        if (dto.variants() != null && dto.variants().length > 0) {
            for (SangerDto.Variant variant : dto.variants()) {
                sb.append(String.format(VARIANT_TABLE_FORMAT, StringUtils.center(variant.gene(), 12), StringUtils.center(variant.dnaChange(), 15),
                        StringUtils.center(variant.predictedAa(), 20), StringUtils.center(variant.zygosity(), 6),
                        StringUtils.center(variant.clazz(), 12), StringUtils.center(variant.result().toFormattedString(), 15)));
            }
            sb.append("----------------------------------------------------------------------------------------------------\r\n");
        } else sb.append(StringUtils.center("No identified variant", 100)).append("\r\n")
                .append("----------------------------------------------------------------------------------------------------\r\n");
        String[][] labelNamePairs = HasSign.getSanitizedContributorsLabelsAndNames(template, test);
        return TEXT_TEMPLATE.replace("{:NAME}", test.name())
                .replace("{:HEADER}", header)
                .replace("{:VARIANTS}", sb.toString())
                .replace("{:INTERPRETATION}", dto.interpretation())
                .replace("{:SPECIMEN}", test.specimen())
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
    public String buildShortFormText(Request request) {
        test = info(request.pk().service());
        String header = String.format(VARIANT_TABLE_SHORT_FORMAT, VARIANT_TABLE_HEADER[0], VARIANT_TABLE_HEADER[1],
                VARIANT_TABLE_HEADER[2], VARIANT_TABLE_HEADER[3],
                VARIANT_TABLE_HEADER[4], VARIANT_TABLE_HEADER[5]);
        StringBuilder sb = new StringBuilder();
        if (dto.variants() != null && dto.variants().length > 0) {
            for (SangerDto.Variant variant : dto.variants()) {
                sb.append(String.format(VARIANT_TABLE_SHORT_FORMAT, variant.gene(), variant.dnaChange(),
                        variant.predictedAa(), variant.zygosity(),
                        variant.clazz(), variant.result().toFormattedString()));
            }
        } else sb.append(StringUtils.center("No identified variant", 100)).append("\r\n");
        String[][] labelNamePairs = HasSign.getSanitizedContributorsLabelsAndNames(template, test);
        String result = TEXT_SHORT_TEMPLATE.replace("{:NAME}", test.name())
                .replace("{:HEADER}", header)
                .replace("{:VARIANTS}", sb.toString())
                .replace("{:INTERPRETATION}", dto.interpretation())
                .replace("{:SPECIMEN}", test.specimen())
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

    private static final Pattern BIRTHDAY_PATTERN = Pattern.compile("^\\d{6}$");
    private static final Pattern BIRTHDAY_PATTERN2 = Pattern.compile("^(\\d{6})(-)*(\\d)$");

    private SangerDto dto(TestInfo test, Request request, Sanger cast) {
        Objects.requireNonNull(request.sample(), "Sample cannot be null");
        Objects.requireNonNull(request.sample().patient(), "Patient cannot be null");

        SangerDto dto = new SangerDto();
        Sample sample = request.sample();
        Patient patient = sample.patient();

        String patientCode = patient.code();
        if (patient.code() != null && BIRTHDAY_PATTERN.matcher(patient.code()).find())
            patientCode = patient.code() + "-*******";
        else if (patient.code() != null && BIRTHDAY_PATTERN2.matcher(patient.code()).find()) {
            Matcher m = BIRTHDAY_PATTERN2.matcher(patient.code());
            m.find();
            patientCode = m.group(1) + "-" + m.group(3) + "******";
        }
        if (cast.variants() != null)
            dto.variants(Arrays.stream(cast.variants()).map(SangerReportBuilder::map).toArray(SangerDto.Variant[]::new));
        dto.code(request.pk().service())
                .interpretation(cast.interpretation())
                .inspector(cast.inspector())
                .reporter(cast.reporter())
                .reviewer(cast.reviewer())
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
        request.customInfos().stream().filter(i -> "TA0023".equals(i.code())).findFirst().ifPresent(barcode -> dto.barcode(barcode.value()));
        request.customInfos().stream().filter(i -> "TA0028".equals(i.code())).findFirst().ifPresent(relation -> dto.relation(relation.value()));
        return dto;
    }

    private static SangerDto.Variant map(Sanger.Variant dto) {
        return new SangerDto.Variant().gene(dto.gene())
                .dnaChange(dto.hgvsc())
                .predictedAa(dto.hgvsp())
                .zygosity(dto.zygosity())
                .result(null)
                .clazz(dto.clazz())
                .result(SangerDto.Result.from(dto.result()))
                .img(decode(dto.sanger()));
    }

    private static byte[] decode(String encode) {
        encode = encode.substring(encode.indexOf(",") + 1);
        return Base64.getDecoder().decode(encode);
    }

    private static final String VARIANT_TABLE_FORMAT = "%1$-15s%2$-10s%3$-10s%4$-10s%5$-15s%6$-12s\r\n";
    private static final String[] VARIANT_TABLE_HEADER = new String[]{
            "Gene",
            "DNA change",
            "AA change",
            "Zygosity",
            "Class",
            "Result"
    };
    private static final String TEXT_TEMPLATE =
            "◆ {:NAME}\r\n" +
                    "\r\n" +
                    "◇ Identified variation(s) : \r\n" +
                    "----------------------------------------------------------------------------------------------------\r\n" +
                    "{:HEADER}" +
                    "----------------------------------------------------------------------------------------------------\r\n" +
                    "{:VARIANTS}" +
                    "\r\n" +
                    "◇ 결과해석\r\n" +
                    "{:INTERPRETATION}\r\n" +
                    "\r\n" +
                    "\r\n" +
                    "◇ 검사방법\r\n" +
                    "----------------------------------------------------------------------------------------------------\r\n" +
                    "Specimen: {:SPECIMEN}\r\n" +
                    "Methods: {:METHOD}\r\n" +
                    "----------------------------------------------------------------------------------------------------\r\n" +
                    "\r\n" +
                    "◇ 비고\r\n" +
                    "{:LIMITATIONS}\r\n" +
                    "\r\n" +
                    "◇ 참고문헌\r\n" +
                    "{:REFERENCES}\r\n" +
                    "\r\n" +
                    "{:INSPECTOR_LABEL} {:INSPECTOR} / {:REPORTER_AND_REVIEWER_LABEL} {:REPORTER}, {:REVIEWER}";
    private static final String VARIANT_TABLE_SHORT_FORMAT = "%s\t%s\t%s\t%s\t%s\t%s\r\n";
    private static final String TEXT_SHORT_TEMPLATE =
            "◆ {:NAME}\r\n" +
                    "\r\n" +
                    "◇ Identified variation(s) : \r\n" +
                    "{:HEADER}" +
                    "{:VARIANTS}" +
                    "\r\n" +
                    "◇ 결과해석\r\n" +
                    "{:INTERPRETATION}\r\n" +
                    "\r\n" +
                    "\r\n" +
                    "◇ 검사방법\r\n" +
                    "Specimen: {:SPECIMEN}\r\n" +
                    "Methods: {:METHOD}\r\n" +
                    "\r\n" +
                    "◇ 비고\r\n" +
                    "{:LIMITATIONS}\r\n" +
                    "\r\n" +
                    "◇ 참고문헌\r\n" +
                    "{:REFERENCES}\r\n" +
                    "\r\n" +
                    "{:INSPECTOR_LABEL} {:INSPECTOR} / {:REPORTER_AND_REVIEWER_LABEL} {:REPORTER}, {:REVIEWER}";
}
