package com.greencross.lims;

import com.gcgenome.lims.test.panel.TestInfo;
import com.greencross.lims.dto.interpretation.PanelTest;
import com.greencross.lims.entity.Patient;
import com.greencross.lims.entity.Request;
import com.greencross.lims.entity.Sample;
import com.greencross.lims.entity.Service;
import com.greencross.lims.report.panels.PanelReportBuilder;
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

import static com.gcgenome.lims.test.panel.TestInfo.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Testcontainers
class PanelReportBuilderTest {
    @InjectMocks
    private PanelReportBuilder reportBuilder;
    @Mock
    private Request request;
    private PanelTest interpretation;
    private TestInfo[] printList = new TestInfo[] {
            ON122
    };

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
//        return Arrays.stream(printList)
        return Arrays.stream(TestInfo.TESTS)
                .filter(t -> !t.code().equalsIgnoreCase("ON001") && !t.code().equalsIgnoreCase("ON040"))
                .flatMap(testInfo -> {
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
        when(request.sample().patient().customerName()).thenReturn("Gclabs");
        when(request.sample().patient().customerName2()).thenReturn("결과지검증");
        when(request.sample().patient().code()).thenReturn("결과지검증");
    }

    private void setPositiveResult(TestInfo testInfo) {
        interpretation = new PanelTest()
                .reasonForReferral("R/O " + testInfo.referralDefault())
                .result("INCONCLUSIVE")
                .resultText("Gene1, Gene2 유전자에서 VUS가 발견되었습니다.")
                .variants(new PanelTest.Variant[]{
                        new PanelTest.Variant().gene("Gene1").hgvsc("c.934-2A>G").hgvsp("p.(?)").zygosity("Het").disease("FAP2").inheritance("AR").clazz("VUS"),
                        new PanelTest.Variant().gene("Gene2").hgvsc("c.94-4A>G").hgvsp("p.(?)").zygosity("Het").disease("MIM:114480").inheritance("AD").clazz("VUS")
                }).abbreviation("Het= Heterozygote; AR= Autosomal recessive; VUS= Variant of uncertain significance; AD= Autosomal dominant; ")
                .interpretation("Adenomas, multiple colorectal의 원인 유전자인 Gene1에서 934번째 염기서열로부터 upstream 방향으로 2번째 염기인 A가 G로 치환되는 VUS variant인 c.934-2A>G 변이가 발견되었습니다. Gene1 유전자의 c.934-2A>G 변이는 gnomAD에서 보고된 전체 인구집단의 minor allele frequency는 0.11%이며, 한국인 인구집단(KRGDB)에서는 1.5%인 변이로 Clinvar에서 Submitter마다 서로 다르게 분류한 바 있습니다[Likely benign(3),Likely_pathogenic(2),Pathogenic(1),Uncertain_significance(5)].\n" +
                        "또한, Breast cancer, early-onset, susceptibility to의 원인 유전자인 Gene2에서 94번째 염기서열로부터 upstream 방향으로 4번째 염기인 A가 G로 치환되는 VUS variant인 c.94-4A>G 변이가 발견되었습니다. Gene2 유전자의 c.94-4A>G 변이는 gnomAD에서 보고된 전체 인구집단의 minor allele frequency는 0.0020%이며, 한국인 인구집단(KRGDB)에서는 0.045%인 변이로 Clinvar에서 Submitter마다 서로 다르게 분류한 바 있습니다[Likely benign(1),Uncertain_significance(1)].")
                .addendum(new PanelTest().result("POSSITIVE")
                        .resultText("Gene1, Gene2 유전자에서 VUS가 발견되었습니다.")
                        .variants(new PanelTest.Variant[]{
                                new PanelTest.Variant().gene("Gene3").hgvsc("c.181C>A").hgvsp("p.Gln61Lys").zygosity("Het").disease("TTT").inheritance("AR").clazz("LPV")
                        }).abbreviation("Het= Heterozygote; AR= Autosomal recessive; LPV= Likely Pathogenic Variant;")
                        .interpretation("추가로 분석된 Gene3 유전자 c.181C>A (p.Gln61Lys) 변이는 기존에 hematologic malignancy를 포함한 다양한 암종에서 보고된 바 있으며(N=1405, COSM580), Q61 부위는 hotspot mutation 부위에 해당하며, Q61K 변이는 oncogenic으로 분류되어 있습니다. Gene3 유전자는 RAS/MAPK pathway에 속하는 유전자로 Gene3 mutation은 KRAS mutation 다음으로 multiple myeloma에서 가장 흔하게 발견됩니다(~20%). Gene3 mutation은 prognostically neutral하다고 보고되어 있으며, MyDRUG study를 통해 Gene3 mutation을 가진 multiple myeloma에 대해 MEK inhibitor의 clinical trial이 진행되고 있습니다(NCT03732703)."))
                .meanDepth("365.88").coverage("100.0");
    }

    private void setNegativeResult(TestInfo testInfo) {
        interpretation = new PanelTest()
                .reasonForReferral("R/O " + testInfo.referralDefault())
                .result("NEGATIVE")
                .resultText("질환과 관련된 변이는 발견되지 않았습니다.")
                .interpretation(testInfo.name().replace("검사","") + " 분석 결과, 질환과 관련된 변이가 발견되지 않았습니다.\n" +
                        "\n" +
                        "본 검사는 검사에 포함된 유전자들의 coding exon과 인접 intron 영역을 분석하며, deep intronic variant, exon deletion/duplication, genomic rearrangement를 포함한 copy number variant는 검출할 수 없습니다. 검사의 기술적 한계에 대한 자세한 내용은 아래 검사의 한계를 참조하십시오.")
                .meanDepth("385.81").coverage("100");
    }
}
