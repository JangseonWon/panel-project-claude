package com.greencross.lims;

import com.gcgenome.lims.test.bloodcancer.TestInfo;
import com.greencross.lims.dto.interpretation.BloodCancer;
import com.greencross.lims.entity.Patient;
import com.greencross.lims.entity.Request;
import com.greencross.lims.entity.Sample;
import com.greencross.lims.entity.Service;
import com.greencross.lims.report.bloodcancer.BloodCancerReportBuilder;
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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Testcontainers
class BloodCancerReportBuilderTest {
    @InjectMocks
    private BloodCancerReportBuilder reportBuilder;
    @Mock
    private Request request;
    private BloodCancer interpretation;

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

        if (testInfo.i18n().equalsIgnoreCase("KOKR")) {
            assertNotNull(result);
            PdfUtils.saveLongFormText(result, testInfo.code());
        } else {
            assertNull(result);
        }
        System.out.println(result);
    }

    private void verifyShortFormText(TestInfo testInfo) throws IOException {
        String result = reportBuilder.buildShortFormText(request);

        if (testInfo.i18n().equalsIgnoreCase("KOKR")) {
            assertNotNull(result);
            PdfUtils.saveShortFormText(result, testInfo.code());
        } else {
            assertNull(result);
        }
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
//        when(request.sample().patient().customerName2()).thenReturn("결과지검증");
        when(request.sample().patient().customerName()).thenReturn("결과지검증");
        when(request.sample().patient().code()).thenReturn("결과지검증");
    }

    private void setPositiveResult(TestInfo testInfo) {
        interpretation = new BloodCancer();
        interpretation.cancerType("R/O " + testInfo.referralDefault());
        interpretation.results(new BloodCancer.Result[]{
                new BloodCancer.Result().tier(BloodCancer.Tier.Tier1)
                        .variants(new BloodCancer.Variant[]{
                                new BloodCancer.Variant().gene("Gene1").hgvsc("c.2077C>T").hgvsp("p.Arg693Ter").vaf(51).depth(583).cosmic("COSM51388"),
                                new BloodCancer.Variant().gene("Gene2").hgvsc("c.4160A>G").hgvsp("p.Asn1387Ser").vaf(45).depth(1438).cosmic("COSM4766124")
                        }).interpretation("Gene1 유전자의 c.2077C>T (p.Arg693Ter) 기존에 hematologic malignancy를 포함한 다양한 암종에서 보고된 바 있으며 (N=119, COSM51388), Gene1 유전자의 truncating mutation은 likely oncogenic으로 분류되어 있습니다. Gene1 유전자는 epigenetic regulator의 일종으로, AML의 약 18.18%, AML-MRC의 약 50%에서 이상이 발견됩니다. AML 및 AML-MRC 모두에서 cytogenetic group에 상관없이 poor prognosis와 연관됩니다.\n\n" +
                        "Gene2 유전자의 c.4160A>G (p.Asn1387Ser) 기존에 hematologic malignancy에서 주로 보고된 바 있으며 (N=3, COSM4766124), N1387A 변이가 likely oncogenic (loss-of-function)으로 분류되어 있고, N1387 부위는 highly conserved되어 있는 enzyme activity에 중요한 역할을 하는 부위로 N1387S 변이 또한 oncogenic할 가능성이 있습니다(Cell 2013;155:1545-55). Gene2 유전자는 DNA demethylase에 관여하는 tumor suppressor의 일종으로 AML의 약 16.98%에서 이상이 발견되고 poor prognosis와 연관되어 있습니다. 특히, normal karyotype AML에서 FLT3-ITD mutation을 동반하지 않으면서 NPM1 mutation을 동반한 경우이거나 60세 이하에서 FLT3-ITD mutation을 동반한 경우 Gene2 mutation은 Gene2 wildtype에 비해 worse prognosis와 연관되어 있습니다."),
                new BloodCancer.Result().tier(BloodCancer.Tier.Tier2)
                        .interpretation("Tier 2 (Potential clinical significance) 에 해당하는 변이가 발견되지 않았습니다."),
                new BloodCancer.Result().tier(BloodCancer.Tier.Tier3)
                        .variants(new BloodCancer.Variant[]{
                                new BloodCancer.Variant().gene("Gene1").hgvsc("c.2082T>A").hgvsp("p.His694Gln").vaf(94).depth(856).cosmic("-"),
                                new BloodCancer.Variant().gene("Gene2").hgvsc("c.4864C>T").hgvsp("p.Arg1622Cys").vaf(51).depth(856).cosmic("COSM4993254")
                        }).interpretation("Gene1 유전자의 c.2082T>A (p.His694Gln) 변이는 기존에 hematologic malignancy를 포함한 암 조직에서 보고된 바 없으며, H694Q 변이의 oncogenicity에 대해 알려져 있지 않아 임상적 의미가 불분명합니다.\n\n" +
                        "Gene2 유전자의 c.4864C>T (p.Arg1622Cys) 기존에 hematologic malignancy에서는 보고된 바 없고 그 외 암종에서 보고된 바 있으나(N=2, COSM4993254), R1622C 변이의 oncogenicity에 대해 알려져 있지 않아 임상적 의미가 불분명합니다.")
        }).qcDna("Pass").qcLibrary("Pass").qcSequencing("Pass").meanDepth("1025X").coverage("99.9%");
        if(testInfo.referralDefault().equalsIgnoreCase("ALL")) {
            interpretation.drugPhenotypes(new BloodCancer.DrugPhenotype[]{
                    new BloodCancer.DrugPhenotype().gene("NUDT15").diplotype("*1/*2").alleleStatus("Normal function/No function").phenotype("Intermediate Metabolizer").result("Abnormal/Priority/High Risk").interpretation("이 결과는 환자가 정상 기능의 allele (normal function allele) 하나와 기능이 없는 allele (no function allele) 하나를 가지고 있음을 의미하며, 유전자형 결과에 기초하여 이 환자는 NUDT15 Intermediate Metabolizer (중간 대사자)로 예측됩니다. 이 환자는 NUDT15에 의해 대사되는 약물(예: 티오퓨린)에 대한 부작용의 위험이 있을 수 있으며, 부적절한 약물 반응을 피하기 위해 NUDT15에 의해 대사되는 약물에 대해 용량 조절 또는 대체 치료제가 필요할 수 있습니다. 자세한 내용은 CPIC 가이드라인을 참조하십시오(https://cpicpgx.org/)."),
                    new BloodCancer.DrugPhenotype().gene("TPMT").diplotype("*1/*2").alleleStatus("Normal function/Uncertain Function").phenotype("Indeterminate").result("None").interpretation("이 결과는 환자가 정상 기능의 allele (normal function allele) 하나와 기능이 알려지지 않은 allele (uncertain function allele) 하나를 가지고 있음을 의미하며, 유전자형 결과에 기초하여 이 환자는 TPMT phenotype을 예측할 수 없습니다.")
            });
        }
    }

    private void setNegativeResult(TestInfo testInfo) {
        interpretation = new BloodCancer();
        interpretation.cancerType("R/O " + testInfo.referralDefault());
        interpretation.results(new BloodCancer.Result[]{
                new BloodCancer.Result().tier(BloodCancer.Tier.Tier1)
                        .interpretation("Tier 1 (Strong clinical significance) 에 해당하는 변이가 발견되지 않았습니다."),
                new BloodCancer.Result().tier(BloodCancer.Tier.Tier2)
                        .interpretation("Tier 2 (Potential clinical significance) 에 해당하는 변이가 발견되지 않았습니다."),
                new BloodCancer.Result().tier(BloodCancer.Tier.Tier3)
                        .interpretation("Tier 3 (Unknown clinical significance) 에 해당하는 변이가 발견되지 않았습니다.")
        }).qcDna("Pass").qcLibrary("Pass").qcSequencing("Pass").meanDepth("1025X").coverage("99.9%");
        if(testInfo.referralDefault().equalsIgnoreCase("ALL")) {
            interpretation.drugPhenotypes(new BloodCancer.DrugPhenotype[]{
                    new BloodCancer.DrugPhenotype().gene("NUDT15").diplotype("*1/*2").alleleStatus("Normal function/No function").phenotype("Intermediate Metabolizer").result("Abnormal/Priority/High Risk").interpretation("이 결과는 환자가 정상 기능의 allele (normal function allele) 하나와 기능이 없는 allele (no function allele) 하나를 가지고 있음을 의미하며, 유전자형 결과에 기초하여 이 환자는 NUDT15 Intermediate Metabolizer (중간 대사자)로 예측됩니다. 이 환자는 NUDT15에 의해 대사되는 약물(예: 티오퓨린)에 대한 부작용의 위험이 있을 수 있으며, 부적절한 약물 반응을 피하기 위해 NUDT15에 의해 대사되는 약물에 대해 용량 조절 또는 대체 치료제가 필요할 수 있습니다. 자세한 내용은 CPIC 가이드라인을 참조하십시오(https://cpicpgx.org/)."),
                    new BloodCancer.DrugPhenotype().gene("TPMT").diplotype("*1/*2").alleleStatus("Normal function/Uncertain Function").phenotype("Indeterminate").result("None").interpretation("이 결과는 환자가 정상 기능의 allele (normal function allele) 하나와 기능이 알려지지 않은 allele (uncertain function allele) 하나를 가지고 있음을 의미하며, 유전자형 결과에 기초하여 이 환자는 TPMT phenotype을 예측할 수 없습니다.")
            });
        }
    }
}
