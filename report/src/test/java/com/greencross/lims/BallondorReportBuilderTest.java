package com.greencross.lims;

import com.gcgenome.lims.test.tmp.Ballondor;
import com.greencross.lims.entity.Patient;
import com.greencross.lims.entity.Request;
import com.greencross.lims.entity.Sample;
import com.greencross.lims.entity.Service;
import com.greencross.lims.report.tmp.BallondorReportBuilder;
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
class BallondorReportBuilderTest {
	@InjectMocks
	private BallondorReportBuilder reportBuilder;
	@Mock
	private Request request;
	private com.gcgenome.lims.dto.interpretation.tmp.BallondorDto interpretation;
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
	private Stream<DynamicTest> createDynamicTests(ResultSetter<Ballondor> resultSetter) {
		return Arrays.stream(Ballondor.TESTS).flatMap(testInfo -> {
			setupRequestMocks(testInfo);
			resultSetter.setResult(testInfo);
			return Stream.of(
					DynamicTest.dynamicTest(String.format("[%s] PDF", testInfo.code()), () -> verifyPdfReport(testInfo)),
					DynamicTest.dynamicTest(String.format("[%s] Long Form Text", testInfo.code()), () -> verifyLongFormText()),
					DynamicTest.dynamicTest(String.format("[%s] Short Form Text", testInfo.code()), () -> verifyShortFormText())
			);
		});
	}

	private void verifyPdfReport(Ballondor testInfo) throws IOException {
		byte[] pdfBytes = reportBuilder.build(request, interpretation);
		PDDocument doc = PdfUtils.byteArrayToPDDocument(pdfBytes);

		assertNotNull(pdfBytes);
		PdfUtils.saveAndOpenPdf(doc, testInfo.code());
		doc.close();
	}
	private void verifyLongFormText() {
		String result = reportBuilder.buildLongFormText(request);

		assertNull(result);
	}

	private void verifyShortFormText() {
		String result = reportBuilder.buildShortFormText(request);

		assertNull(result);
	}

	private void setupRequestMocks(Ballondor testInfo) {
		when(request.pk()).thenReturn(new Request.RequestPK(123L, testInfo.code()));
		when(request.sample()).thenReturn(mock(Sample.class));
		when(request.sample().id()).thenReturn(202408081710000L);
		when(request.service()).thenReturn(mock(Service.class));
		when(request.service().id()).thenReturn(testInfo.code());
		when(request.sample().remark()).thenReturn("123");
		when(request.sample().patient()).thenReturn(mock(Patient.class));
		when(request.sample().patient().customerCode2()).thenReturn("customerCode");
		when(request.sample().patient().customerName()).thenReturn("customerName");
		when(request.sample().patient().code()).thenReturn("patientCode");
	}

	private void setPositiveResult(Ballondor testInfo){
		interpretation = new com.gcgenome.lims.dto.interpretation.tmp.BallondorDto()
				.result("Detected")
				.summary("MYD88 gene mutation이 검출되었습니다.")
				.vaf("3.85")
				.hgvsc("hgvs.c")
				.sample("R-VRD 유도항암화학요법 종료 후");
	}
	private void setNegativeResult(Ballondor testInfo) {
		interpretation = new com.gcgenome.lims.dto.interpretation.tmp.BallondorDto()
				.result("Not Detected")
				.summary("MYD88 gene mutation이 검출되지 않았습니다.")
				.vaf("-")
				.sample("스크리닝");
	}
}
