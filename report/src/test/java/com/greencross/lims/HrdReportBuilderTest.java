package com.greencross.lims;

import com.gcgenome.lims.test.hrd.TestInfo;
import com.greencross.lims.dto.interpretation.Hrd;
import com.greencross.lims.entity.Patient;
import com.greencross.lims.entity.Request;
import com.greencross.lims.entity.Sample;
import com.greencross.lims.entity.Service;
import com.greencross.lims.report.hrd.HrdReportBuilder;
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
import java.util.Arrays;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Testcontainers
class HrdReportBuilderTest {
    @InjectMocks
    private HrdReportBuilder reportBuilder;
    @Mock
    private Request request;
    private Hrd interpretation;

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

    @TestFactory
    @DisplayName("PDF 및 서술형 결과지 생성 테스트 (Fail)")
    Stream<DynamicTest> t3() {
        return createDynamicTests(this::setFailResult);
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

    private void verifyLongFormText(TestInfo testInfo) {
        String result = reportBuilder.buildLongFormText(request);

        assertNotNull(result);
        System.out.println(result);
    }

    private void verifyShortFormText(TestInfo testInfo) {
        String result = reportBuilder.buildShortFormText(request);

        assertNull(result);
    }

    private void setupRequestMocks(TestInfo testInfo) {
        when(request.pk()).thenReturn(new Request.RequestPK(123L, testInfo.code()));
        when(request.sample()).thenReturn(mock(Sample.class));
        when(request.sample().id()).thenReturn(202408081710000L);
        when(request.service()).thenReturn(mock(Service.class));
        when(request.service().id()).thenReturn(testInfo.code());
        when(request.sample().remark()).thenReturn("123");
        when(request.sample().patient()).thenReturn(mock(Patient.class));
        when(request.sample().patient().customerCode2()).thenReturn("customerCode");
        when(request.sample().patient().customerName()).thenReturn("Gclabs");
        when(request.sample().patient().code()).thenReturn("patientCode");
        when(request.sample().patient().customerName2()).thenReturn("customerName2");
    }

    private void setPositiveResult(TestInfo testInfo) {
        interpretation = new Hrd()
                .cancerType("Ovarian cancer")
                .gi("Positive").giScore(78)
                .brca("Positive")
                .interpretation("상동재조합결핍(Homologous Recombination Deficiency, HRD)에 의해 생성된 3종류의 유전체 상처(genomic scar)를 전체 유전체에서 종합 분석한 결과 양성입니다.\n\n-3종류의 유전체 상처는 이형접합소실(Loss of heterozygosity; LOH), 텔로미어 대립 불균형(Telomeric allelic imbalance; TAI), 광범위한 전이(Large-scale transition; LST)로 각각의 수치를 통합한 점수가 42점 이상인 경우, 유전체 불안정성 양성으로 분류됩니다.")
                .results(new Hrd.GeneResult[]{
                        new Hrd.GeneResult().tier("BRCA").variants(new Hrd.Variant[] {
                                new Hrd.Variant().gene("BRCA2").hgvsc("c.5980C>T").dna(null).hgvsp("p.Gln1994Ter").protein(null).vaf(32.0).depth(514).cosmicId("-"),
                                new Hrd.Variant().gene("BRCA2").hgvsc("c.9097del").dna(null).hgvsp("p.Thr3033LeufsTer29").protein(null).vaf(32.0).depth(514).cosmicId("-")
                        }).interpretation("HRD 검사에서 BRCA 변이가 발견되었습니다."),
                        new Hrd.GeneResult().tier("Tier1").variants(new Hrd.Variant[] {
                                new Hrd.Variant().gene("BRCA2").hgvsc("c.5980C>T").dna(null).hgvsp("p.Gln1994Ter").protein(null).vaf(32.0).depth(514).cosmicId("-"),
                                new Hrd.Variant().gene("BRCA2").hgvsc("c.9097del").dna(null).hgvsp("p.Thr3033LeufsTer29").protein(null).vaf(32.0).depth(514).cosmicId("-")
                        }).interpretation("BRCA2 유전자에서 c.5980C>T (p.Gln1994Ter, Q1994*) 변이가 발견되었습니다. BRCA2 유전자의 Truncation 변이는 Likely Loss-of-function 기전의 Likely Oncogenic 변이로 분류되어 있습니다. Ovarian Cancer에서 BRCA2 유전자의 Truncation 변이는 Olaparib, Rucaparib, Prednisone의 적응증입니다.\n" +
                                "BRCA2 유전자에서 c.9097del (p.Thr3033LeufsTer29, T3033Lfs*29) 변이가 발견되었습니다. BRCA2 유전자의 Truncation 변이는 Likely Loss-of-function 기전의 Likely Oncogenic 변이로 분류되어 있습니다. Ovarian Cancer에서 BRCA2 유전자의 Truncation 변이는 Olaparib, Rucaparib, Prednisone의 적응증입니다."),
                        new Hrd.GeneResult().tier("Tier2").variants(new Hrd.Variant[] {})
                                .interpretation("HRD 검사에서 Tier 2 변이는 발견되지 않았습니다.")
                }).snv("Pass").cnv("Pass");
    }

    private void setNegativeResult(TestInfo testInfo) {
        interpretation = new Hrd()
                .cancerType("Ovarian cancer")
                .gi("Negative").giScore(17)
                .brca("Negative")
                .interpretation("상동재조합결핍(Homologous Recombination Deficiency, HRD)에 의해 생성된 3종류의 유전체 상처(genomic scar)를 전체 유전체에서 종합 분석한 결과 음성입니다.\n\n-3종류의 유전체 상처는 이형접합소실(Loss of heterozygosity; LOH), 텔로미어 대립 불균형(Telomeric allelic imbalance; TAI), 광범위한 전이(Large-scale transition; LST)로 각각의 수치를 통합한 점수가 42점 이상인 경우, 유전체 불안정성 양성으로 분류됩니다.")
                .results(new Hrd.GeneResult[]{
                        new Hrd.GeneResult().tier("BRCA").variants(new Hrd.Variant[] {}).interpretation("HRD 검사에서 BRCA 변이는 발견되지 않았습니다."),
                        new Hrd.GeneResult().tier("Tier1").variants(new Hrd.Variant[] {}).interpretation("HRD 검사에서 Tier 1 변이는 발견되지 않았습니다."),
                        new Hrd.GeneResult().tier("Tier2").variants(new Hrd.Variant[] {}).interpretation("HRD 검사에서 Tier 2 변이는 발견되지 않았습니다.")
                }).snv("Pass").cnv("Pass");
    }
    private void setFailResult(TestInfo testInfo) {
        interpretation = new Hrd()
                .cancerType("검사 취소")
                .gi("-").giScore(0)
                .brca("-")
                .interpretation("")
                .results(new Hrd.GeneResult[]{
                        new Hrd.GeneResult().tier("BRCA").variants(new Hrd.Variant[] {}).interpretation(""),
                        new Hrd.GeneResult().tier("Tier1").variants(new Hrd.Variant[] {}).interpretation(""),
                        new Hrd.GeneResult().tier("Tier2").variants(new Hrd.Variant[] {}).interpretation("")
                }).snv("-").cnv("-");
    }
}
