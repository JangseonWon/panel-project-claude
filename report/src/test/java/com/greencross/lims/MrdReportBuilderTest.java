package com.greencross.lims;

import com.gcgenome.lims.dto.interpretation.Mrd;
import com.gcgenome.lims.test.mrd.TestInfo;
import com.greencross.lims.entity.Patient;
import com.greencross.lims.entity.Request;
import com.greencross.lims.entity.Sample;
import com.greencross.lims.entity.Service;
import com.greencross.lims.report.mrd.MrdReportBuilder;
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
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.stream.Stream;

import static com.gcgenome.lims.dto.interpretation.Mrd.Result.DETECTED;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Testcontainers
class MrdReportBuilderTest {
    @InjectMocks
    private MrdReportBuilder reportBuilder;
    @Mock
    private Request request;
    private Mrd interpretation;

    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd");

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
        when(request.pk()).thenReturn(new Request.RequestPK(202504179710000L, testInfo.code()));
        when(request.sample()).thenReturn(mock(Sample.class));
        when(request.sample().id()).thenReturn(202504179710000L);
        when(request.dateRequest()).thenReturn(LocalDate.of(2025,4,17));
        when(request.service()).thenReturn(mock(Service.class));
        when(request.service().id()).thenReturn(testInfo.code());
        when(request.sample().remark()).thenReturn("202504179710000");
        when(request.sample().patient()).thenReturn(mock(Patient.class));
        when(request.sample().patient().customerName2()).thenReturn("부속거래처");
        when(request.sample().patient().customerName()).thenReturn("Gclabs");
        when(request.sample().patient().code()).thenReturn("결과지검증");
    }

    private void setResult(TestInfo testInfo) {
        switch (testInfo.code()) {
            case "N152":
                interpretation = new Mrd()
                        .cancerType("-")
                        .histories(new Mrd.MrdHistory[]{
                                new Mrd.MrdHistory().date(DTF.format(LocalDate.now())).inputDna(244)
                                        .results(new Mrd.MrdGeneResult[] {
                                        new Mrd.MrdGeneResult().gene("TRB").result(Mrd.Result.DETECTED)
                                                .target(new Mrd.MrdCloneResult().readDepth(60311).clonalDepth(174))
                                                .lqic(new Mrd.MrdCloneResult().readDepth(18562)),
                                        new Mrd.MrdGeneResult().gene("TRG").result(Mrd.Result.DETECTED)
                                                .target(new Mrd.MrdCloneResult().readDepth(70311).clonalDepth(174))
                                                .lqic(new Mrd.MrdCloneResult().readDepth(18562))
                                }),
                                new Mrd.MrdHistory().date(DTF.format(LocalDate.now().minusMonths(1))).inputDna(241)
                                        .results(new Mrd.MrdGeneResult[] {
                                        new Mrd.MrdGeneResult().gene("TRB").result(Mrd.Result.DETECTED)
                                                .target(new Mrd.MrdCloneResult().readDepth(247712).clonalDepth(0))
                                                .lqic(new Mrd.MrdCloneResult().readDepth(4274)),
                                        new Mrd.MrdGeneResult().gene("TRG").result(Mrd.Result.DETECTED)
                                                .target(new Mrd.MrdCloneResult().readDepth(247712).clonalDepth(0))
                                                .lqic(new Mrd.MrdCloneResult().readDepth(4274))
                                }),
                                new Mrd.MrdHistory().date(DTF.format(LocalDate.now().minusMonths(2))).inputDna(240)
                                        .results(new Mrd.MrdGeneResult[] {
                                        new Mrd.MrdGeneResult().gene("TRB").result(Mrd.Result.DETECTED)
                                                .target(new Mrd.MrdCloneResult().readDepth(729841).clonalDepth(611526))
                                                .lqic(new Mrd.MrdCloneResult().readDepth(3520)),
                                        new Mrd.MrdGeneResult().gene("TRG").result(Mrd.Result.DETECTED)
                                                .target(new Mrd.MrdCloneResult().readDepth(729841).clonalDepth(611526))
                                                .lqic(new Mrd.MrdCloneResult().readDepth(3520))
                                })
                        });
                break;
            case "N150", "N151":
                interpretation = new Mrd()
                        .cancerType("-")
                        .histories(new Mrd.MrdHistory[]{
                                new Mrd.MrdHistory().date(DTF.format(LocalDate.now())).inputDna(244)
                                        .results(new Mrd.MrdGeneResult[]{
                                        new Mrd.MrdGeneResult().gene("IGH").result(Mrd.Result.DETECTED)
                                                .target(new Mrd.MrdCloneResult().readDepth(60311).clonalDepth(174))
                                                .lqic(new Mrd.MrdCloneResult().readDepth(18562)),
                                        new Mrd.MrdGeneResult().gene("IGK").result(Mrd.Result.DETECTED)
                                                .target(new Mrd.MrdCloneResult().readDepth(70311).clonalDepth(174))
                                                .lqic(new Mrd.MrdCloneResult().readDepth(18562))
                                }),
                                new Mrd.MrdHistory().date(DTF.format(LocalDate.now().minusMonths(1))).inputDna(241)
                                        .results(new Mrd.MrdGeneResult[]{
                                        new Mrd.MrdGeneResult().gene("IGH").result(Mrd.Result.DETECTED)
                                                .target(new Mrd.MrdCloneResult().readDepth(247712).clonalDepth(0))
                                                .lqic(new Mrd.MrdCloneResult().readDepth(4274)),
                                        new Mrd.MrdGeneResult().gene("IGK").result(Mrd.Result.DETECTED)
                                                .target(new Mrd.MrdCloneResult().readDepth(247712).clonalDepth(0))
                                                .lqic(new Mrd.MrdCloneResult().readDepth(4274))
                                }),
                                new Mrd.MrdHistory().date(DTF.format(LocalDate.now().minusMonths(2))).inputDna(240)
                                        .results(new Mrd.MrdGeneResult[]{
                                        new Mrd.MrdGeneResult().gene("IGH").result(DETECTED)
                                                .target(new Mrd.MrdCloneResult().readDepth(729841).clonalDepth(611526))
                                                .lqic(new Mrd.MrdCloneResult().readDepth(3520)),
                                        new Mrd.MrdGeneResult().gene("IGK").result(DETECTED)
                                                .target(new Mrd.MrdCloneResult().readDepth(729841).clonalDepth(611526))
                                                .lqic(new Mrd.MrdCloneResult().readDepth(3520))
                                })
                        });
                break;
        }

    }

}
