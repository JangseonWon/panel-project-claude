package com.greencross.lims;

import com.gcgenome.lims.test.wes.TestInfo;
import com.gcgenome.lims.test.wes.TestWithSingleInfo;
import com.greencross.lims.dto.interpretation.Des;
import com.greencross.lims.entity.Patient;
import com.greencross.lims.entity.Request;
import com.greencross.lims.entity.Sample;
import com.greencross.lims.entity.Service;
import com.greencross.lims.report.wes.WesWithSingleGeneReportBuilder;
import com.greencross.lims.util.PdfUtils;
import com.greencross.lims.util.ResultSetter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
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
public class WesSingleReportBuilderTest {
	@Autowired
	private WesWithSingleGeneReportBuilder reportBuilder;
	@Mock
	private Request request;
	private com.greencross.lims.dto.interpretation.Des interpretation;
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
		return Arrays.stream(TestWithSingleInfo.TESTS).flatMap(testInfo -> {
			setupRequestMocks(testInfo);
			resultSetter.setResult(testInfo);
			return Stream.of(
					DynamicTest.dynamicTest(String.format("[%s] PDF", testInfo.code()), () -> verifyPdfReport(testInfo)),
					DynamicTest.dynamicTest(String.format("[%s] Long Form Text", testInfo.code()), this::verifyLongFormText),
					DynamicTest.dynamicTest(String.format("[%s] Short Form Text", testInfo.code()), this::verifyShortFormText)
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

	private void verifyLongFormText() {
		String result = reportBuilder.buildLongFormText(request);

		assertNull(result);
	}

	private void verifyShortFormText() {
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
		when(request.sample().patient().customerName2()).thenReturn("샘플결과지");
		when(request.sample().patient().customerName()).thenReturn("지놈직거래처");
		when(request.sample().patient().code()).thenReturn("patientCode");
	}
	private void setPositiveResult(TestInfo testInfo) {
		setSingleResult();

		setWesResult();
	}

	private void setSingleResult() {
		interpretation = new Des().result("POSITIVE")
				.reasonForReferral("-")
				.resultText("질환 관련성이 있는 변이가 발견되었습니다.")
				.abbreviationReference("NM_000132.3(F8)")
				.abbreviationDisease("MIM:306700, Hemophilia A")
				.abbreviation("Het, heterozygote; AD, autosomal dominant; VUS, variant of uncertain significance")
				.interpretation("Hereditary Non-Polyposis Colon Cancer(HNPCC)의 원인 유전자인 MLH1 유전자의 모든 exon과 인접 intron의 모든 염기서열을 분석한 결과 46번째 위치한 염기서열 G가 C로 치환되어 16번째 아미노산인 Valine이 Leucine으로 치환될 것으로 예상되는 VUS인 c.46G>C가 발견되었습니다. MLH1유전자의 c.46G>C 변이는 gnomAD에서 보고된 전체 인종의 minor allele frequency는 0.00041%이며, 한국인 인구집단(KRGDB)에서는 0.23%인 변이로 Clinvar에서 Uncertain significance로 분류되어 있습니다. In-silico prediction (SIFT, Polyphen2, MutationTaster) 에서는 Deleterious하다고 예측되었습니다.")
				.variants(new Des.Variant[]{
								new Des.Variant().gene("F8").hgvsc("c.1492G>A").hgvsp("p.(Gly498Arg)").zygosity("Hem").disease("MIM:306700").inheritance("XLR").clazz("PV")
				});
	}

	private void setWesResult() {
		interpretation.incidentalFindings(new Des().result("POSITIVE")
				.resultText("VUS가 발견되었습니다.")
				.reasonForReferral("R/O 유전성 운동실조증")
				.abbreviation("AD, autosomal dominant; Het, heterozygous; VUS, variant of uncertain significance")
				.abbreviationDisease("DCMP, dilated cardiomyopathy; HCMP, hypertrophic cardiomyopathy")
				.variants(new Des.Variant[]{
						new Des.Variant().gene("MYBPC3").hgvsc("c.713G>A").hgvsp("p.(Arg238His)").zygosity("Het").disease("HCMP/DCMP").inheritance("AD,AD, Somatic mutation").clazz("VUS"),
						new Des.Variant().gene("FBN1").hgvsc("C.5431G>A").hgvsp("p.(Glu1811Lys)").zygosity("Het").disease("MFS").inheritance("AD").clazz("VUS")
				}).abbreviation("Het= Heterozygote; AR= Autosomal recessive; VUS= Variant of uncertain significance; AD= Autosomal dominant; ")
				.interpretation("유전자 패널 분석 결과, POLG 유전자에서 Variant of Uncertain Significance(VUS)가 발견되었습니다.\n" +
								"POLG 유전자의 c.2890C>T (p.Arg964Cys) 변이는 전체 인종(gnomAD)의 minor allele frequency는 0.070%이며, " +
								"한국인 인구집단(KRGDB)에서는 0.32%인 변이로 in-silico prediction (SIFT, Polyphen2, MutationTaster)에서는 Deleterious하다고 예측되었고, " +
								"ClinVar에서 Conflicting interpretations of pathogenicity?로 분류하고 있습니다(Variation ID: 206537). " +
								"이 변이는 mitochondrial toxicity induced by anti-HIV treatment 환자에서 처음 보고되었고(PMID: 17436221), " +
								"이후 여러 문헌에서 보고된 바 있으나 아직까지 pathogenicity가 불분명하여 VUS로 분류하였습니다(PMID: 18546365, 19364868, 19762913, 21880868, 24091540). " +
								"POLG는 progressive external ophthalmoplegia (PEO), Alpers syndrome, POLG deficiency 등과 관련된 유전자로 상염색체 열성 또는 우성 유전양상을 보입니다." +
								"Clinical correlation이 권장됩니다.\n")
				.consentIncidentalFindings(false)
				.meanDepth("365.88").coverage("100.0").reporter("설창안").reviewer("이청화")
		);
	}
}
