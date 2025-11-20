package com.greencross.lims;

import com.gcgenome.lims.test.solidtumor2.TestInfo;
import com.greencross.lims.dto.interpretation.SolidTumor2;
import com.greencross.lims.entity.Patient;
import com.greencross.lims.entity.Request;
import com.greencross.lims.entity.Sample;
import com.greencross.lims.entity.Service;
import com.greencross.lims.report.solidtumor2.SolidTumor2ReportBuilder;
import com.greencross.lims.util.PdfUtils;
import com.greencross.lims.util.ResultSetter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Testcontainers
class SolidTumor2ReportBuilderTest {
    @InjectMocks
    private SolidTumor2ReportBuilder reportBuilder;
    @Mock
    private Request request;
    private SolidTumor2 interpretation;

    @Container
    private static final PostgreSQLContainer<?> POSTGRESQL_CONTAINER = new PostgreSQLContainer<>("postgres:15.3-alpine3.18")
            .withDatabaseName("lims")
            .withUsername("panel")
            .withPassword("s3cret");

    @DynamicPropertySource
    public static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRESQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRESQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", POSTGRESQL_CONTAINER::getPassword);
        registry.add("spring.datasource.driver-class-name", POSTGRESQL_CONTAINER::getDriverClassName);
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @TestFactory
    @DisplayName("PDF 및 서술형 결과지 생성 테스트 (양성)")
    Stream<DynamicTest> t1() {
        return createDynamicTests(this::setPositiveResult);
    }

    @TestFactory
    @DisplayName("PDF 및 서술형 결과지 생성 테스트 (음성)")
    Stream<DynamicTest> t2() {
        return createDynamicTests(this::setNegativeResult);
    }

    private Stream<DynamicTest> createDynamicTests(ResultSetter<TestInfo> resultSetter) {
        return Arrays.stream(TestInfo.TESTS).flatMap(testInfo -> {
            setupRequestMocks(testInfo);
            resultSetter.setResult(testInfo);
            return Stream.of(
                    DynamicTest.dynamicTest(String.format("[%s] PDF", testInfo.code()), () -> verifyPdfReport(testInfo)),
                    DynamicTest.dynamicTest(String.format("[%s] Long Form Text", testInfo.code()), () -> verifyLongFormText(testInfo)),
                    DynamicTest.dynamicTest(String.format("[%s] Short Form Text", testInfo.code()), () -> verifyShortFormText(testInfo))
            );
        });
    }


    private void verifyPdfReport(TestInfo testInfo) throws IOException {
        byte[] pdfBytes = reportBuilder.build(request, interpretation);
        PDDocument doc = PdfUtils.byteArrayToPDDocument(pdfBytes);

        assertNotNull(pdfBytes);
        PdfUtils.saveAndOpenPdf(doc, testInfo.code());
        doc.close();
    }

    private void verifyLongFormText(TestInfo testInfo) throws IOException {
        String result = reportBuilder.buildLongFormText(request);

        if(testInfo.i18n().equalsIgnoreCase("KOKR")) {
            assertNotNull(result);
            PdfUtils.saveLongFormText(result, testInfo.code());
        } else assertNull(result);
        System.out.println(result);
    }

    private void verifyShortFormText(TestInfo testInfo) {
        String result = reportBuilder.buildShortFormText(request);

        assertNull(result);
        System.out.println(result);
    }

    private void setupRequestMocks(TestInfo testInfo) {
        when(request.pk()).thenReturn(new Request.RequestPK(202504171710000L, testInfo.code()));
        when(request.sample()).thenReturn(mock(Sample.class));
        when(request.sample().id()).thenReturn(202504171710000L);
        when(request.dateRequest()).thenReturn(LocalDate.of(2025,4,17));
        when(request.service()).thenReturn(mock(Service.class));
        when(request.service().id()).thenReturn(testInfo.code());
        when(request.sample().remark()).thenReturn("202504171710000");
        when(request.sample().patient()).thenReturn(mock(Patient.class));
        when(request.sample().patient().customerCode2()).thenReturn("customerCode");
        when(request.sample().patient().customerName()).thenReturn("지놈직거래처");
        when(request.sample().patient().customerName2()).thenReturn("결과지검증");
        when(request.sample().patient().code()).thenReturn("결과지검증");
    }

    private void setPositiveResult(TestInfo testInfo) {
        interpretation= new SolidTumor2();
        interpretation.cancerCategory("Lung").cancerType("Non-Small Cell Lung Cancer");
        interpretation.variants(new SolidTumor2.Variant[]{
                new SolidTumor2.Variant().kind("SNV").tier(SolidTumor2.Tier.Tier1).gene("EGFR").hgvsc("c.2235_2249del").hgvsp("p.Glu746_Ala750del").vaf("3.0").depth(1935).significance("Therapeutic").interpretation("EGFR 유전자에서 c.2235_2249del (p.Glu746_Ala750del) 변이가 검출되었습니다. EGFR 유전자의 Exon 19 in-frame deletion은 Oncogenic으로 분류되어 있습니다. Non-Small Cell Lung Cancer에서 EGFR 유전자의 Exon 19 deletion은 Afatinib, Osimertinib, Dacomitinib, Gefitinib, Erlotinib, Erlotinib + Ramucirumab의 적응증입니다."),
                new SolidTumor2.Variant().kind("SNV").tier(SolidTumor2.Tier.Tier2).gene("TP53").hgvsc("c.371G>A").hgvsp("p.His124Glu").vaf("0.1").depth(1465).interpretation("TP53 유전자에서 c.371G>A (p.His124Glu) 변이가 검출되었습니다. TP53 유전자의 H124E 변이는 Oncogenic으로 분류되어 있습니다. 현재까지 본 암종과 관련하여 공인된 표적 치료제는 없습니다."),
                new SolidTumor2.Variant().kind("CNV").tier(SolidTumor2.Tier.Tier2).gene("TERT").type("amplification").copyNumber("13.5 copy number gain").interpretation("TERT 유전자에서 amplification이 검출되었습니다. TERT 유전자의 amplification은 Oncogenic으로 분류되어 있습니다. 현재까지 본 암종과 관련하여 공인된 표적 치료제는 없습니다.\n"),
                new SolidTumor2.Variant().kind("FUSION").tier(SolidTumor2.Tier.Tier3).gene("CDKN2A").fusion("~~FFSD").vaf("3.0").depth(1935).readCount("777")
        });
        interpretation.hypermutability(new SolidTumor2.Hypermutability().tmb("13.5/MB").msi("MSS").msiScore(0.2));
        interpretation.qc(new SolidTumor2.Qc()
                .snvQc("PASS").cnvQc("FAIL").msiQc("PASS").rnaQc("PASS").purity(83.0).msaf(3.0)
                .interpretation("분석 결과 CNV QC 기준을 만족하지 못하여 CNV 결과는 보고되지 않습니다. 결과 해석에 참조하시기 바랍니다.")
                .pctExonOver100X(95.0)
                .pctExonOver1000X(90.0)
                .medianExonCoverage(2169.0)
                .mad(0.042)
                .mbc(24.3)
                .usableMsi(123.0)
                .onTargetReads(13939111.0)
                .medianCvOver500X(0.51)
        );
        interpretation.method(new SolidTumor2.Method()
                .region(testInfo.region())
                .panel(testInfo.panel())
                .limitations(testInfo.limitations()!=null?testInfo.limitations():new String[]{"ENUS Sample Limitations"})
                .method(testInfo.method())
                .sequencing(testInfo.sequencing())
                .pipeline(testInfo.pipeline())
                .reference(testInfo.reference())
                .immunotherapyInfo(testInfo.immunotherapyInfo())
                .qcInfo(testInfo.qcInfo())
                .fusionInfo(testInfo.fusionInfo())
                .geneSets(Stream.of(
                        new SolidTumor2.GeneSet().label("small_variants").genes(testInfo.smallVariants()!=null?testInfo.smallVariants():new String[]{"ENUS Sample small variants"}),
                        new SolidTumor2.GeneSet().label("copy_number_variants").genes(testInfo.copyNumberVariants()!=null?testInfo.copyNumberVariants():new String[]{"ENUS Sample copy number variants"}),
                        new SolidTumor2.GeneSet().label("fusion_variants").genes(testInfo.fusions()!=null?testInfo.fusions():new String[]{"ENUS Sample fusions"})
                ).toArray(SolidTumor2.GeneSet[]::new))
        );
    }

    private void setNegativeResult(TestInfo testInfo) {
        interpretation= new SolidTumor2();
        interpretation.cancerCategory("Lung").cancerType("Non-Small Cell Lung Cancer");
        interpretation.variants(new SolidTumor2.Variant[0]);
        interpretation.hypermutability(new SolidTumor2.Hypermutability().tmb("13.5/MB").msi("MSS").msiScore(0.2));
        interpretation.qc(new SolidTumor2.Qc()
                .snvQc("PASS").cnvQc("FAIL").msiQc("PASS").rnaQc("PASS").purity(83.0).msaf(3.0)
                .interpretation("분석 결과 CNV QC 기준을 만족하지 못하여 CNV 결과는 보고되지 않습니다. 결과 해석에 참조하시기 바랍니다.")
                .pctExonOver100X(95.0)
                .pctExonOver1000X(90.0)
                .medianExonCoverage(2169.0)
                .mad(0.042)
                .mbc(24.3)
                .usableMsi(123.0)
                .onTargetReads(13939111.0)
                .medianCvOver500X(0.51)
        );
        interpretation.method(new SolidTumor2.Method()
                .region(testInfo.region())
                .panel(testInfo.panel())
                .limitations(testInfo.limitations()!=null?testInfo.limitations():new String[]{"ENUS Sample Limitations"})
                .method(testInfo.method())
                .sequencing(testInfo.sequencing())
                .pipeline(testInfo.pipeline())
                .reference(testInfo.reference())
                .immunotherapyInfo(testInfo.immunotherapyInfo())
                .qcInfo(testInfo.qcInfo())
                .fusionInfo(testInfo.fusionInfo())
                .geneSets(Stream.of(
                        new SolidTumor2.GeneSet().label("small_variants").genes(testInfo.smallVariants()!=null?testInfo.smallVariants():new String[]{"ENUS Sample small variants"}),
                        new SolidTumor2.GeneSet().label("copy_number_variants").genes(testInfo.copyNumberVariants()!=null?testInfo.copyNumberVariants():new String[]{"ENUS Sample copy number variants"}),
                        new SolidTumor2.GeneSet().label("fusion_variants").genes(testInfo.fusions()!=null?testInfo.fusions():new String[]{"ENUS Sample fusions"})
                ).toArray(SolidTumor2.GeneSet[]::new))
        );
    }
}
