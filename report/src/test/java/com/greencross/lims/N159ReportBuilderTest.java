package com.greencross.lims;

import com.gcgenome.lims.test.tmp.N159;
import com.greencross.lims.entity.Patient;
import com.greencross.lims.entity.Request;
import com.greencross.lims.entity.Sample;
import com.greencross.lims.entity.Service;
import com.greencross.lims.report.tmp.N159ReportBuilder;
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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Testcontainers
public class N159ReportBuilderTest {
	@InjectMocks
	private N159ReportBuilder reportBuilder;
	@Mock
	private Request request;
	private com.greencross.lims.dto.interpretation.tmp.N159 interpretation;
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
	private Stream<DynamicTest> createDynamicTests(ResultSetter<N159> resultSetter) {
		N159 testInfo = com.gcgenome.lims.test.tmp.N159.builder().build();
		setupRequestMocks(testInfo);
		resultSetter.setResult(testInfo);
		return Stream.of(
				DynamicTest.dynamicTest(String.format("[%s] PDF", testInfo.code()), () -> verifyPdfReport(testInfo)),
				DynamicTest.dynamicTest(String.format("[%s] Long Form Text", testInfo.code()), () -> verifyLongFormText()),
				DynamicTest.dynamicTest(String.format("[%s] Short Form Text", testInfo.code()), () -> verifyShortFormText())
		);
	}

	private void verifyPdfReport(N159 testInfo) throws IOException {
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
	private void setupRequestMocks(N159 testInfo) {
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
	private void setPositiveResult(N159 testInfo){
		interpretation = new com.greencross.lims.dto.interpretation.tmp.N159()
				.result("POSITIVE")
				.resultText("클론성 조혈증이 검출되었습니다.")
				.interpretation("클론성 조혈증은 체내 염증 반응 증가를 통해, 동맥경화, 정맥 혈전 등으로 인한 각종 심혈관 질환 위험도를 증가시키고 사망 위험도(all-cause mortality)를 높입니다. 주된 사망 요인인 심혈관 질환 이외에도 클론성 조혈증은 체내 염증과 관련된 자가면역질환, 암, 당뇨, 류마티스성 관절염, 잦은 감염 등 여러 질환과도 연관되어 있습니다.\n" +
						"\n" +
						"클론성 조혈증 진행 억제와 면역력 향상을 위해 운동, 건강한 식습관, 금연, 금주 등 생활습관 개선이 권장됩니다.\n" +
						"\n" +
						"또한 동맥경화의 조기 진단 및 심혈관 질환 예방을 위해 의료진 상담 및 정기적인 건강검진을 통한 관리가 권고됩니다.\n" +
						"\n" +
						"또한 클론성 조혈증이 검출될 경우 혈액암의 위험도가 증가하나 평균적으로 클론성 조혈증 양성일 때 혈액암으로 진행할 가능성은 1년에 0.5%~1% 정도로 알려져 있습니다. 이는 클론성 조혈증 양성의 대부분은 혈액암으로 진행하지 않음을 의미합니다. 그러나 CBC 등 정기적인 혈액검사를 통해 추적관찰 하실 것이 권고됩니다.\n" +
						"\n" +
						"필요한 경우 의료진과 상담하십시오.\n");
	}
	private void setNegativeResult(N159 testInfo){
		interpretation = new com.greencross.lims.dto.interpretation.tmp.N159()
				.result("NEGATIVE")
				.resultText("클론성 조혈증이 검출되지 않았습니다.")
				.interpretation("수검자의 클론성 조혈증 지놈 스크린 검사 결과 클론성 조혈증이 검출되지 않았습니다.\n" +
						"\n" +
						"클론성 조혈증이 검출된 경우 혈액암의 위험이 증가할 뿐 아니라 체내 염증 상태를 증가 시켜 각종 심혈관 질환, 자가면역질환, 암의 발생 등을 증가시키는 것으로 알려졌습니다.\n" +
						"\n" +
						"그러나 클론성 조혈증이 검출되지 않았다고 해서 혈액암 및 각종 심혈관 질환, 암 등의 위험성이 감소하는 것은 아닙니다. 생활 습관, 환경, 다른 유전적 요인 등이 질환의 발병에 영향을 미칠 수 있음으로, 지속적인 꾸준한 건강관리가 권고됩니다.");
	}
}
