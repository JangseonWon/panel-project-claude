package com.greencross.lims.test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;

@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FunctionalTests {
    @Autowired
    private WebTestClient client;


    private String autoInterpret(String url, String body){
        List<String> responses = client.post()
                .uri(url)
                .header("X-USER-ID", "221943")
                .header("Content-Type", "application/json")
                .body(Mono.just(body), String.class)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .returnResult(String.class)
                .getResponseBody().collectList().block();
        assert responses != null;
        return String.join("", responses);
    }
    @Test
    public void autoInterpret1() {
        String body = "{\"previous\":{\"result\":\"NEGATIVE\",\"result_text\":\"의뢰 사유와 관련된 변이는 발견되지 않았습니다.\",\"clinical_information\":\"1. 질환 발생 연령 : 0세\\n2. 임상정보 : atrial septal defect, vantricular septal defect, hypoglycemia, IUGR, premature birth\\n3. 진단 : IUGR, preterm, hypoglycemia\\n4. 가족력 : 없음\",\"variants\":[],\"abbreviation_reference\":\"\",\"abbreviation_disease\":\"\",\"abbreviation\":\"\",\"interpretation\":\"DGS 검사 결과 임상적으로 의미 있는 변이가 발견되지 않았습니다.\\n이 검사는 유전자의 coding exon과 intron 영역을 포함한 전체 유전체 영역을 분석하며, splicing에 영향을 미치지 않을 것으로 예측되는 synonymous variant 및 intronic variant는 보고하지 않습니다. 또한 일부 결실/중복 및 높은 상동성, 낮은 Coverage, 낮은 염기서열 품질을 보이는 영역에서 변이 검출에 한계가 있습니다. 검사의 기술적 한계에 대한 자세한 내용은 아래 검사의 한계를 참조하십시오.\",\"recommendation\":\"\",\"mean_depth\":\"25.7\",\"coverage\":\"93.7\",\"inspector\":\"이명근\",\"checker1\":\"이새미\",\"comment1\":\"\",\"checker2\":\"설창안\",\"comment2\":\"\",\"consent_incidental_findings\":true,\"incidental_findings\":{\"variants\":[],\"abbreviation_reference\":\"\",\"abbreviation_disease\":\"\",\"abbreviation\":\"\",\"interpretation\":\"ACMG에서 권고한 73개 유전자에서 (Likely) Pathogenic Variant는 발견되지 않았습니다.\"},\"revision\":null}}";
        String response = autoInterpret("/samples/202207079320101/services/N127/auto-interpret", body);
        System.out.println(response);
    }

    @Test
    public void autoInterpret2() {
        String body = "{\"disease\":{\"KCNQ2\":[{\"full_name\":\"Developmental and epileptic encephalopathy 7\",\"abbreviation\":\"DAEE7\",\"inheritance\":[\"AD\"]},{\"full_name\":\"Seizures, benign neonatal, 1\",\"abbreviation\":\"SBN1\",\"inheritance\":[\"AD\"]}]},\"previous\":{\"result\":\"INCONCLUSIVE\",\"result_text\":\"의뢰 사유와 관련 가능성이 불분명한 변이가 발견되었습니다.\",\"clinical_information\":\"본인-발달장애, seizure, impression-1. developmental and epileptic encephalopathy, 2. global developmental disorder/ 여동생-febrile seizure, febrile status epilepticus (지속되는 발열 동반 atonic seizures)/ 모-seizure, impression-1. r/o provoked seizure due to gestaticual  eclampsia (첫 경련), 2. r/o epilepsy\",\"variants\":[{\"snv\":\"hg19:20:062065231:T:G\",\"analysis\":\"d0ccf612-c7b4-4390-8a38-1f36591671cc:22DGS017:004\",\"gene\":\"KCNQ2\",\"hgvsc\":\"c.1049A>C\",\"hgvsp\":\"p.Asn350Thr\",\"zygosity\":\"Het\",\"disease\":\"DAEE7, SBN1\",\"inheritance\":\"AD\",\"class\":\"VUS\"}],\"abbreviation_reference\":\"NM_172107.4(KCNQ2)\",\"abbreviation_disease\":\"DAEE7, Developmental and epileptic encephalopathy 7; SBN1, Seizures, benign neonatal, 1\",\"abbreviation\":\"AD, Autosomal dominant; Het, Heterozygous; VUS, Variant of Uncertain Significance\",\"interpretation\":\"[2022.09.20]\\nDGS 분석 결과, KCNQ2 유전자에서 Variant of Uncertain Significance (VUS)가 발견되었습니다.\\n\\nKCNQ2 유전자의 c.1049A>C (p.Asn350Thr) 변이는 일반 인구집단(gnomAD, KRGDB)에서 보고된 바 없는 매우 드문 변이로, in-silico prediction (SIFT, PolyPhen, MutationTaster)에서는 Deleterious로 예측되었습니다.\\nKCNQ2 유전자는 Developmental and epileptic encephalopathy 7, Seizures, benign neonatal, 1 관련 유전자로 상염색체 우성 유전 양상을 보입니다.\\n\\n참고로 상기 변이는 이전 WES에서 depth가 낮아 변이 calling이 되지 않은 것으로 보입니다.\",\"recommendation\":\"Clinical correlation이 권장됩니다.\",\"mean_depth\":\"29.45\",\"coverage\":\"94.8\",\"inspector\":\"이명근\",\"checker1\":\"설창안\",\"comment1\":\"\",\"checker2\":\"이새미\",\"comment2\":\"\",\"consent_incidental_findings\":true,\"incidental_findings\":{\"variants\":[],\"abbreviation_reference\":\"\",\"abbreviation_disease\":\"\",\"abbreviation\":\"\",\"interpretation\":\"ACMG에서 권고한 73개 유전자에서 (Likely) Pathogenic Variant는 발견되지 않았습니다.\"},\"revision\":null}}";
        String response = autoInterpret("/samples/202207089714911/services/N127/auto-interpret", body);
        System.out.println(response);
    }

}
