package com.greencross.lims;

import com.gcgenome.lims.test.single.TestInfo;
import com.greencross.lims.dto.interpretation.PanelTest;
import com.greencross.lims.entity.Patient;
import com.greencross.lims.entity.Request;
import com.greencross.lims.entity.Sample;
import com.greencross.lims.entity.Service;
import com.greencross.lims.report.single.SingleGeneReportBuilder;
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
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Testcontainers
class SingleGeneReportBuilderTest {
    @InjectMocks
    private SingleGeneReportBuilder reportBuilder;
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
        return Stream.concat(
                createDynamicTests(this::setPositiveResult, List.of(TestInfo.Category.ETC, TestInfo.Category.BRCA, TestInfo.Category.CANCER, TestInfo.Category.CANCER_WITH_RD_REPORT)),
                createDynamicTests(this::setPositiveResultWithMlpa, List.of(TestInfo.Category.WITH_MLPA))
        );
    }

    @TestFactory
    @DisplayName("PDF 및 서술형 결과지 생성 테스트 (음성)")
    Stream<DynamicTest> t2() {
        return Stream.concat(
                createDynamicTests(this::setNegativeResult, List.of(TestInfo.Category.ETC, TestInfo.Category.BRCA, TestInfo.Category.CANCER, TestInfo.Category.CANCER_WITH_RD_REPORT)),
                createDynamicTests(this::setNegativeResultWithMlpa, List.of(TestInfo.Category.WITH_MLPA))
        );

    }
    private Stream<DynamicTest> createDynamicTests(ResultSetter<TestInfo> resultSetter, List<TestInfo.Category> categories) {
        return Arrays.stream(TestInfo.TESTS)
                .filter(testInfo -> categories.contains(testInfo.category()))
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
        when(request.sample().remark()).thenReturn("202504179710000");
        when(request.sample().patient()).thenReturn(mock(Patient.class));
        when(request.sample().patient().customerCode2()).thenReturn("customerCode");
        when(request.sample().patient().customerName()).thenReturn("지놈직거래처");
        when(request.sample().patient().customerName2()).thenReturn("결과지검증");
        when(request.sample().patient().code()).thenReturn("결과지검증");
    }

    private void setPositiveResult(TestInfo testInfo) {
        interpretation = new PanelTest().reasonForReferral("-").result("POSITIVE")
                .resultText(String.format("%s 유전자에서 변이가 발견되었습니다.", testInfo.gene()))
                .variants(new PanelTest.Variant[]{
                        new PanelTest.Variant().gene(testInfo.gene()).hgvsc("c.1865A>C").hgvsp("p.Asp622Ala").zygosity("Het").disease("143980").inheritance("AD, AR").clazz("LPV")
                })
                .abbreviation("Het, heterozygote; AD, autosomal dominamt; AR, autosomal recessive; LPV, likely pathogenic variant")
                .abbreviationReference("NM_000441.2")
                .abbreviationDisease("DFNB4, Deafness, autosomal recessive 4; PS, Pendred syndrome")
                .interpretation(String.format(
                        "%s 유전자의 모든 exon과 인접 intron의 염기서열을 분석한 결과, Likely Pathogenic Variant (LPV)가 발견되었습니다.\n" +
                                "\n" +
                                "%s 유전자의 c.1865A>C (p.Asp622Ala) 변이는 일반 인구집단(gnomAD, KRGDB)에서 보고된 바 없는 매우 드문 변이로 in-silico prediction (SIFT, Polyphen2, MutationTaster)에서 Deleterious하다고 예측되었고, ClinVar에서 LPV로 분류되어 있습니다(ID: 252093).\n" +
                                "\n" +
                                "이 변이는 familial hypercholesterolemia (FH) 환자에서 보고되었으며(PMID: 16159606), 동일한 아미노산이 다른 아미노산으로 대체되는 변이(p.Asp622Asn, p/Asp622Gly)도 보고되었습니다(PMID: 15701167, 19318025, 25525159, 30526649).\n" +
                                "\n" +
                                "Clinical correlation 및 가족검사가 권장됩니다.\n" +
                                "\n" +
                                "[추가소견]\n" +
                                "APOB, LDLRAP1, PCSK9 유전자의 모든 exon과 인접 intron의 염기서열을 분석한 결과, 질환 관련 변이는 발견되지 않았습니다.",
                        testInfo.gene(), testInfo.gene()))
                .meanDepth("706").coverage("99");
    }
    private void setNegativeResult(TestInfo testInfo) {
        interpretation = new PanelTest().reasonForReferral("-").result("NEGATIVE")
                .resultText("질환과 관련된 변이는 발견되지 않았습니다.")
                .interpretation(String.format("%s 유전자 검사 분석 결과, 질환과 관련된 변이는 발견되지 않았습니다.\n\n" +
                                "본 검사는 검사에 포함된 유전자들의 coding exon과 인접 intron 영역을 분석하며, deep intronic variant, exon deletion/duplication, genomic rearrangement를 포함한 copy number variant 검출은 제한적입니다. 또한, TRDN|NM_006073|exon_6 영역은 GC 함량이 높거나, 염기서열 상동성이 높은 부위로서 본 검사법으로 변이를 검출하는데 한계가 있습니다. 검사의 기술적 한계에 대한 자세한 내용은 검사의 한계를 참조하십시오.",
                        testInfo.gene()));
    }

    private void setPositiveResultWithMlpa(TestInfo testInfo) {
        interpretation = new PanelTest()
                .reasonForReferral("R/O Neurofibromatosis")
                .result("POSITIVE")
                .variants(new PanelTest.Variant[]{
                        new PanelTest.Variant().gene(testInfo.gene()).hgvsc("c.1865A>C").hgvsp("p.Asp622Ala").zygosity("Het").disease("143980").inheritance("AD").clazz("VUS"),
                        new PanelTest.Variant().gene(testInfo.gene()).hgvsc("c.5980C>T").hgvsp("p.Gln1994Ter").zygosity("Het").disease("143980").inheritance("AD").clazz("PV")
                })
                .abbreviation("")
                .abbreviationReference("NM_000267.3 (NF1)")
                .abbreviationDisease("Neurofibromatosis, type 1")
                .interpretation(String.format("%s 유전자 검사 분석 결과, Pathogenic Variant (PV)가 발견되었습니다.\n" +
                                "%s 유전자의 c.1021C>T (p.Arg341Ter) 변이는 일반 인구집단(gnomAD, KRGDB)에서 보고된 바 없는 매우 드문 변이입니다. 이 변이는Premature stop codon을 생성할 것으로 예측되는 Nonsense 변이로서 기존에 NF 환자에서 보고된 바 있으며(PMID: 19249154,  25525159, 29130639,  31273341,  32724039,  33067351,  8379998), ClinVar에서 PV로 분류되어 있습니다(ID: 3292).\n" +
                                "%s 유전자는 NF 관련 유전자로 AD 유전 양상을 보입니다.\n" +
                                "Clinical correlation 및 필요하다고 판단될 경우 가족검사가 권장됩니다.\n" +
                                "\n" +
                                "%s 유전자의 exon deletion/duplication을 확인하기 위해 MLPA 검사를 시행한 결과, exons 1-2번의Heterozygous deletion이 관찰되었습니다.",
                        testInfo.gene(), testInfo.gene(), testInfo.gene(), testInfo.gene()))
                .addendum(new PanelTest().result("POSITIVE")
                        .variants(new PanelTest.Variant[]{
                                new PanelTest.Variant().gene("Gene").hgvsc("c.181C>A").hgvsp("p.Gln61Lys").zygosity("Het").disease("TTT").inheritance("AR").clazz("LPV")
                        })
                        .abbreviation("Het= Heterozygote; AR= Autosomal recessive; LPV= Likely Pathogenic Variant;")
                        .interpretation("추가로 분석된 Gene 유전자 c.181C>A (p.Gln61Lys) 변이는 기존에 hematologic malignancy를 포함한 다양한 암종에서 보고된 바 있으며(N=1405, COSM580), Q61 부위는 hotspot mutation 부위에 해당하며, Q61K 변이는 oncogenic으로 분류되어 있습니다. Gene3 유전자는 RAS/MAPK pathway에 속하는 유전자로 Gene3 mutation은 KRAS mutation 다음으로 multiple myeloma에서 가장 흔하게 발견됩니다(~20%). Gene3 mutation은 prognostically neutral하다고 보고되어 있으며, MyDRUG study를 통해 Gene3 mutation을 가진 multiple myeloma에 대해 MEK inhibitor의 clinical trial이 진행되고 있습니다(NCT03732703)."))
                .mlpaResult(new PanelTest.Mlpa().result("Detected").exons("exons 1-2").delDup("deletion"));
    }

    private void setNegativeResultWithMlpa(TestInfo testInfo) {
        interpretation = new PanelTest()
                .reasonForReferral("R/O Neurofibromatosis")
                .result("NEGATIVE")
                .variants(new PanelTest.Variant[0])
                .interpretation("NF1 유전자 검사 분석 결과, 질환과 관련된 변이는 발견되지 않았습니다.\n\n" +
                        "본 검사는 검사에 포함된 유전자들의 coding exon과 인접 intron 영역을 분석하며, deep intronic variant, exon deletion/duplication, genomic rearrangement를 포함한 copy number variant 검출은 제한적입니다. 또한, TRDN|NM_006073|exon_6 영역은 GC 함량이 높거나, 염기서열 상동성이 높은 부위로서 본 검사법으로 변이를 검출하는데 한계가 있습니다. 검사의 기술적 한계에 대한 자세한 내용은 검사의 한계를 참조하십시오.")
                .mlpaResult(new PanelTest.Mlpa().result("Not Detected"));
    }
}
