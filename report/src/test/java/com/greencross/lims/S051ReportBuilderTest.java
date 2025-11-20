package com.greencross.lims;

import com.gcgenome.lims.dto.interpretation.tmp.S051Dto;
import com.gcgenome.lims.test.tmp.S051;
import com.greencross.lims.entity.Patient;
import com.greencross.lims.entity.Request;
import com.greencross.lims.entity.Sample;
import com.greencross.lims.entity.Service;
import com.greencross.lims.report.tmp.S051ReportBuilder;
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
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Testcontainers
class S051ReportBuilderTest {
    @InjectMocks
    private S051ReportBuilder reportBuilder;
    @Mock
    private Request request;
    private S051Dto interpretation;

    @Container
    private static final PostgreSQLContainer<?> POSTGRESQL_CONTAINER = new PostgreSQLContainer<>("postgres:15.3-alpine3.18")
            .withDatabaseName("lims")
            .withUsername("panel")
            .withPassword("s3cret");

    @DynamicPropertySource
    private static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRESQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRESQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", POSTGRESQL_CONTAINER::getPassword);
        registry.add("spring.datasource.driver-class-name", POSTGRESQL_CONTAINER::getDriverClassName);
    }

    @BeforeEach
    public void setUp() {
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

    private Stream<DynamicTest> createDynamicTests(ResultSetter<S051> resultSetter) {
        S051 testInfo = S051.instance;
        setupRequestMocks(testInfo);
        resultSetter.setResult(testInfo);
        return Stream.of(
                    DynamicTest.dynamicTest(String.format("[%s] PDF", testInfo.code()), () -> verifyPdfReport(testInfo)),
                    DynamicTest.dynamicTest(String.format("[%s] Long Form Text", testInfo.code()), () -> verifyLongFormText(testInfo)),
                    DynamicTest.dynamicTest(String.format("[%s] Short Form Text", testInfo.code()), () -> verifyShortFormText(testInfo))
            );
    }

    private void verifyPdfReport(S051 testInfo) throws IOException {
        byte[] pdfBytes = reportBuilder.build(request, interpretation);
        PDDocument doc = PdfUtils.byteArrayToPDDocument(pdfBytes);

        assertNotNull(pdfBytes);
        PdfUtils.saveAndOpenPdf(doc, testInfo.code());
        doc.close();
    }

    private void verifyLongFormText(S051 testInfo) throws IOException {
        String result = reportBuilder.buildLongFormText(request);

        assertNotNull(result);
        System.out.println(result);
    }

    private void verifyShortFormText(S051 testInfo) throws IOException {
        String result = reportBuilder.buildShortFormText(request);
        assertNotNull(result);
        System.out.println(result);
    }

    private void setupRequestMocks(S051 testInfo) {
        when(request.pk()).thenReturn(new Request.RequestPK(123L, testInfo.code()));
        when(request.sample()).thenReturn(mock(Sample.class));
        when(request.sample().id()).thenReturn(202408081710000L);
        when(request.service()).thenReturn(mock(Service.class));
        when(request.service().id()).thenReturn(testInfo.code());
        when(request.sample().remark()).thenReturn("123");
        when(request.sample().patient()).thenReturn(mock(Patient.class));
        when(request.sample().patient().customerName2()).thenReturn("customerCode");
        when(request.sample().patient().customerName()).thenReturn("Gclabs");
        when(request.sample().patient().code()).thenReturn("patientCode");
    }
    private void setPositiveResult(S051 testInfo){
        interpretation = new S051Dto()
                .result("Detected")
                .header("[FLT3-ITD mutation 중간 보고 입니다.]")
                .summary("[Allele burden (size): ~6.7% (21 bp)]")
                .interpretation("Fragment length analysis 결과, FLT3-ITD mutation이 검출되었습니다.\n\n* Fragment length analysis와 NGS의 검사법 차이로 인해 FLT3-ITD mutant allele ratio에 차이가 있을 수 있습니다. Fragment size가 긴 경우 fragment analysis 및 NGS 모두에서 정확한 mutant allele의 proportion의 계산이 어려울 수 있습니다.\n\n* FLT3-ITD mutant의 allele burden이 너무 낮은 경우, PCR inhibitor가 존재하는 경우, insertion size가 너무 작거나 큰 경우에는 fragment length analysis에서 위음성으로 나타날 수 있습니다.\n\n* FLT3-ITD mutant의 insertion site, insertion length, number of individual clones, allelic ratio 등 최종 결과는 급성골수성백혈병 유전자 패널(NGS) 결과 보고서를 참조하시기 바랍니다.");
    }
    private void setNegativeResult(S051 testInfo){
        interpretation = new S051Dto()
                .result("Not Detected")
                .header("[FLT3-ITD mutation 중간 보고 입니다.]")
                .summary("")
                .interpretation("Fragment length analysis 결과, FLT3-ITD mutation이 검출되지 않았습니다.\n\n* Fragment length analysis와 NGS의 검사법 차이로 인해 FLT3-ITD mutant allelic ratio에 차이가 있을 수 있습니다. Fragment size가 긴 경우 fragment analysis 및 NGS 모두에서 정확한 mutant allele의 proportion의 계산이 어려울 수 있습니다.\n\n* FLT3-ITD mutant의 allelic ratio가 너무 낮은 경우, PCR inhibitor가 존재하는 경우, insertion size가 너무 작거나 큰 경우에는 fragment length analysis에서 위음성으로 나타날 수 있습니다.\n\n* FLT3-ITD mutant의 insertion site, insertion length, number of individual clones, allelic ratio 등 최종 결과는 급성골수성백혈병 유전자 패널(NGS) 결과 보고서를 참조하시기 바랍니다.\n\n* 본 검사의 detection limit는 allelic ratio 0.01입니다.");
    }
}
