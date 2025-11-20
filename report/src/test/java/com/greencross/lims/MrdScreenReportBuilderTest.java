package com.greencross.lims;

import com.gcgenome.lims.test.mrd.TestInfo;
import com.greencross.lims.dto.interpretation.MrdScreen;
import com.greencross.lims.entity.Patient;
import com.greencross.lims.entity.Request;
import com.greencross.lims.entity.Sample;
import com.greencross.lims.entity.Service;
import com.greencross.lims.report.mrd.MrdScreenReportBuilder;
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Testcontainers
class MrdScreenReportBuilderTest {
    @InjectMocks
    private MrdScreenReportBuilder reportBuilder;
    @Mock
    private Request request;
    private MrdScreen interpretation;

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
    @DisplayName("PDF 및 서술형 결과지 생성 테스트")
    Stream<DynamicTest> t1() {
        return createDynamicTests(this::setResult);
    }

    private Stream<DynamicTest> createDynamicTests(ResultSetter<TestInfo> resultSetter) {
        return Arrays.stream(TestInfo.SCREEN_TESTS).flatMap(testInfo -> {
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

    private void setResult(TestInfo testInfo) {
        switch (testInfo.code()) {
            case "N144", "N145":
                interpretation = new MrdScreen()
                        .results(new MrdScreen.MrdScreenGeneResult[]{
                                new MrdScreen.MrdScreenGeneResult().gene("IGH").depthTotal(729841L).lengthLqic(298L).depthLqic(3520L)
                                        .clones(new MrdScreen.MrdScreenCloneResult[]{
                                        new MrdScreen.MrdScreenCloneResult().regionV("IGHV3-13_01").regionJ("IGHJ6_02").length(292).depth(530311L)
                                                .sequence("GCCTCTGGATTCACCTTCAGTAGCTACGACATGCACTGGGTCCGCCAAGCTACAGGAAAAGGTCTGGAGTGGGTCTCAGCTATTGGTACTGCTGGTGACACATACTATCCAGGCTCCGTGAAGGGCCGATTCACCATCTCCAGAGAAAATGCCAAGAACTCCTTGTATCTTCAAATGAACAGCCTGAGAGCCGGGGACACGGCTGTGTATTACTGTGCAAGAGGGTATTACGATATTTTGACTGGTTATTATACTACTACTACGGTATGGACGTCTGGGGCCAAGGGACCAC"),
                                        new MrdScreen.MrdScreenCloneResult().regionV("IGHV5-51_01").regionJ("IGHJ4_02").length(274).depth(81215L)
                                                .sequence("GTTCTGGATACAGCTTTACCAGCTACTGGATCGGCTGGGTGCGCCAGATGCCCGGGAAAGGCCTGGAGTGGATGGGGATCATCTATCCTGGTGACTCTGATACCAGATACAGCCCGTCCTTCCAAGGCCAGGTCACCATCTCAGCCGACAAGTCCATCAGCACCGCCTACCTGCAGTGGAGCAGCCTGAAGGCCTCGGACACCGCCATGTATTACTGTGCGAGACATAAGGAGAAGTACCAGCTGCTATCGGACTACTGGGGCCAGGGAACCCT")
                                }),
                                new MrdScreen.MrdScreenGeneResult().gene("IGK").depthTotal(729841L).lengthLqic(298L).depthLqic(3520L)
                                        .clones(new MrdScreen.MrdScreenCloneResult[]{new MrdScreen.MrdScreenCloneResult().regionV("IGKV3-13_01").regionJ("IGKJ6_02").length(292).depth(530311L)
                                        .sequence("GCCTCTGGATTCACCTTCAGTAGCTACGACATGCACTGGGTCCGCCAAGCTACAGGAAAAGGTCTGGAGTGGGTCTCAGCTATTGGTACTGCTGGTGACACATACTATCCAGGCTCCGTGAAGGGCCGATTCACCATCTCCAGAGAAAATGCCAAGAACTCCTTGTATCTTCAAATGAACAGCCTGAGAGCCGGGGACACGGCTGTGTATTACTGTGCAAGAGGGTATTACGATATTTTGACTGGTTATTATACTACTACTACGGTATGGACGTCTGGGGCCAAGGGACCAC")
                                })
                        }).cancerType("-")
                        .inputDna(240)
                        .somaticMutations(new MrdScreen.SomaticMutation[]{
                                new MrdScreen.SomaticMutation().clone("Clone 1").hyperMutation("Inconclusive (In-frame or No stop codon: N)"),
                                new MrdScreen.SomaticMutation().clone("Clone 2").hyperMutation("No presence of somatic hypermutation (Mutation rate: TEXT%)").mutationRate("54.23")
                        });
                break;
            case "N146":
                interpretation = new MrdScreen()
                        .results(new MrdScreen.MrdScreenGeneResult[]{
                                new MrdScreen.MrdScreenGeneResult().gene("TRB").depthTotal(729841L).lengthLqic(298L).depthLqic(3520L)
                                        .clones(new MrdScreen.MrdScreenCloneResult[]{
                                        new MrdScreen.MrdScreenCloneResult().regionV("Db1").regionJ("Jb1-2").length(210).depth(530311L)
                                                .sequence("GGAGGTGAGAAGGAAGCCCCCGGCCTGGTCCATACCCCACCACCAACTTGCATAATGGGGGGTGATGTCACCCACCCTCCACTCCCCTCAAAGGAGCAGCTGCTCTGGTGGTCTCTCCCAGGCTCTGGGGGCGGACCCATGGGAGGGGCTGTTTTTGTACAAAGCTGTAACATTGTGGGGACAGTAAGCTATGGCTACACCTTCGGTTCG"),
                                        new MrdScreen.MrdScreenCloneResult().regionV("Vb11-3").regionJ("Jb2-3").length(214).depth(81215L)
                                                .sequence("CCCGGAGCTTCTGATTCGATATGAGAATGAGGAAGCAGTAGACGATTCACAGTTGCCTAAGGATCGATTTTCTGCAGAGAGGCTCAAAGGAGTAGACTCCACTCTCAAGATCCAGCCTGCAGAGCTTGGGGACTCGGCCGTGTATCTCTGTGCCAGCAGCTTAGGGGGACCAAGCGAGGACAGTGATTGGGATACGCAGTATTTTGGCCCAGGC")
                                }),
                                new MrdScreen.MrdScreenGeneResult().gene("TRG").depthTotal(729841L).lengthLqic(298L).depthLqic(3520L)
                                        .clones(new MrdScreen.MrdScreenCloneResult[]{
                                        new MrdScreen.MrdScreenCloneResult().regionV("Vg8").regionJ("Jg1/2").length(292).depth(530311L)
                                                .sequence("GGAATCAGTCGAGAAAAGTATCATACTTATGCAAGCACAGGGAAGAGCCTTAAATTTATACTGGAAAATCTAATTGAACGTGACTCTGGGGTCTATTACTGTGCCACCTGGGTGGGCTTATAAGAAACTCTTTGGCAGTG")
                                })
                        }).cancerType("-")
                        .inputDna(240)
                        .somaticMutations(new MrdScreen.SomaticMutation[]{
                                new MrdScreen.SomaticMutation().clone("Clone 1").hyperMutation("Inconclusive (In-frame or No stop codon: N)"),
                                new MrdScreen.SomaticMutation().clone("Clone 2").hyperMutation("No presence of somatic hypermutation (Mutation rate: TEXT%)").mutationRate("54.23")
                        });
                break;
        }
    }

}
