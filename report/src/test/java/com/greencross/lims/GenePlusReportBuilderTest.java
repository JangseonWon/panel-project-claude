package com.greencross.lims;

import com.gcgenome.lims.test.geneplus.TestInfo;
import com.greencross.lims.dto.interpretation.PanelTest;
import com.greencross.lims.entity.Patient;
import com.greencross.lims.entity.Request;
import com.greencross.lims.entity.Sample;
import com.greencross.lims.entity.Service;
import com.greencross.lims.report.geneplus.GenePlusReportBuilder;
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
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Testcontainers
class GenePlusReportBuilderTest {
    @InjectMocks
    private GenePlusReportBuilder reportBuilder;
    @Mock
    private Request request;
    private PanelTest interpretation;

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
        return Stream.concat(Stream.of(TestInfo.TESTS_BRCA), Stream.of(TestInfo.TESTS_ETC)).flatMap(testInfo -> {
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

        assertNotNull(result);
        PdfUtils.saveLongFormText(result, testInfo.code());
        System.out.println(result);
    }

    private void verifyShortFormText(TestInfo testInfo) throws IOException {
        String result = reportBuilder.buildShortFormText(request);

        assertNotNull(result);
        PdfUtils.saveShortFormText(result, testInfo.code());
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
        interpretation = new PanelTest().reasonForReferral("-").result("POSITIVE")
                .resultText("Gene1 유전자에서 변이가 발견되었습니다.")
                .variants(new PanelTest.Variant[]{
                        new PanelTest.Variant().gene("Gene1").hgvsc("c.1865A>C").hgvsp("p.Asp622Ala").zygosity("Het").disease("143980").inheritance("AD, AR").clazz("LPV")
                }).abbreviation("Het, heterozygote; AD, autosomal dominamt; AR, autosomal recessive; LPV, likely pathogenic variant")
                .interpretation("Gene1 유전자의 모든 exon과 인접 intron의 염기서열을 분석한 결과, Likely Pathogenic Variant (LPV)가 발견되었습니다.\n" +
                        "\n" +
                        "Gene1 유전자의 c.1865A>C (p.Asp622Ala) 변이는 일반 인구집단(gnomAD, KRGDB)에서 보고된 바 없는 매우 드문 변이로 in-silico prediction (SIFT, Polyphen2, MutationTaster)에서 Deleterious하다고 예측되었고, ClinVar에서 LPV로 분류되어 있습니다(ID: 252093).\n" +
                        "\n" +
                        "이 변이는 familial hypercholesterolemia (FH) 환자에서 보고되었으며(PMID: 16159606), 동일한 아미노산이 다른 아미노산으로 대체되는 변이(p.Asp622Asn, p/Asp622Gly)도 보고되었습니다(PMID: 15701167, 19318025, 25525159, 30526649).\n" +
                        "\n" +
                        "Clinical correlation 및 가족검사가 권장됩니다.\n" +
                        "\n" +
                        "[추가소견]\n" +
                        "Gene2, Gene3, Gene4 유전자의 모든 exon과 인접 intron의 염기서열을 분석한 결과, 질환 관련 변이는 발견되지 않았습니다.")
                .meanDepth("706").coverage("99");
    }
    private void setNegativeResult(TestInfo testInfo) {
         interpretation = new PanelTest().reasonForReferral("R/O "+testInfo.referralDefault()).result("NEGATIVE")
                .resultText("질환과 관련된 변이는 발견되지 않았습니다.")
                .interpretation(testInfo.gene() + " 유전자의 모든 exon과 인접 intron의 염기서열을 분석한 결과, 질환 관련 변이는 발견되지 않았습니다.");
    }
}
