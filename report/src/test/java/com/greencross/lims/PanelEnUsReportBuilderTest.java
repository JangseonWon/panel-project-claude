package com.greencross.lims;

import com.gcgenome.lims.test.panel.TestInfo;
import com.greencross.lims.dto.interpretation.PanelTest;
import com.greencross.lims.entity.Patient;
import com.greencross.lims.entity.Request;
import com.greencross.lims.entity.Sample;
import com.greencross.lims.entity.Service;
import com.greencross.lims.report.panel.enus.HereditaryReportBuilderEnUs;
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
public class PanelEnUsReportBuilderTest {
    @InjectMocks
    private HereditaryReportBuilderEnUs reportBuilder;
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

    private Stream<DynamicTest> createDynamicTests(ResultSetter<TestInfo> resultSetter) {
        return Arrays.stream(new TestInfo[]{TestInfo.ON001, TestInfo.ON040})
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
    private void verifyLongFormText(TestInfo testInfo) {
        String result = reportBuilder.buildLongFormText(request);

        assertNull(result);
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
        when(request.sample().patient().customerName2()).thenReturn("customerCode");
        when(request.sample().patient().customerName()).thenReturn("Gclabs");
        when(request.sample().patient().name()).thenReturn("patientName");
        when(request.sample().patient().sex()).thenReturn(Patient.Sex.F);
        when(request.sample().patient().code()).thenReturn("patientCode");
    }
    private void setPositiveResult(TestInfo testInfo) {
        interpretation = new PanelTest()
                .result("INCONCLUSIVE").coverage("300").meanDepth("99")
        .variants(new PanelTest.Variant[]{
                new PanelTest.Variant().gene("PALB2").hgvsc("c.1054G>C").hgvsp("p.Glu352Gln").zygosity("Het").disease("BCs").inheritance("AD").clazz("VUS"),
                new PanelTest.Variant().gene("TP53").hgvsc("c.406del").hgvsp("p.Ile136Ter").zygosity("Het").disease("GIST").inheritance("AD").clazz("LPV")
        })
        .resultText("Variant of uncertain significance in the PALB2 gene was found.\n" +
                        "Additionally, likely pathogenic variant in the SDHB gene was found.")
                .abbreviationReference("NM_024675.3(PALB2);NM_003000.2(SDHB);")
                .abbreviationDisease("BCs=Susceptibility to breast cancer;GIST= Gastrointestinal stromal tumor;")
                .abbreviation("Het=Heterozygote; AD=Autosomal dominant; VUS=Variant of uncertain; LPV= Likely pathogenic variant;")
                .interpretation("A Heterozygous variant (c.1054G>C;p.Glu352Gln) in the PALB2 gene was detected. This variant has been classified as a " +
                        "Likely benign(2);Uncertain significance(3) in Clinvar and as a possible pathological mutation in HGMD. This variant has " +
                        "not been reported in the South Asian population database, while minor allele frequency (MAF) of the variant was " +
                        "estimated to be 0.000061 (gnomAD_all). Taken together, this PALB2 variant can be classified as VUS. Pathogenic variants " +
                        "in PALB2 are known to cause Breast cancer, Fanconi anemia, Pancreatic cancer, etc.\n\n" +
                        "As further analysis, a Heterozygous variant (c.406del;p.Ile136Ter) in the SDHB gene was detected. This variant has been " +
                        "classified as a deleterious mutation in HGMD and this variant has not been reported in the population database " +
                        "(gnomAD). Taken together, this SDHB variant can be classified as Likely pathogenic. Pathogenic variants in SDHB are " +
                        "known to cause Gastrointestinal stromal tumor, Paraganglioma and gastric stromal sarcoma, Paragangliomas 4, Pheochromocytoma, " +
                        "etc.\n\n" +
                        "This test analyzes the coding exons and adjacent intron areas of the genes included in the panel and cannot detect " +
                        "exon deletions/duplications, and copy number variants including genomic rearrangements etc. For more information " +
                        "on the technical limitations of a test, see the limits of the test below.");
    }
}
