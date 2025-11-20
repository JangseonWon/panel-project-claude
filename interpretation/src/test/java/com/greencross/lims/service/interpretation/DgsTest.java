package com.greencross.lims.service.interpretation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.greencross.lims.dto.interpretation.InterpretationParam;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
@SpringBootTest
class DgsTest {
    @Autowired
    Dgs dgs;

    @Autowired
    ObjectMapper objectMapper;

    String testParams = "{\"disease\":{\"MYH2\":[{\"full_name\":\"Proximal myopathy and ophthalmoplegia\",\"abbreviation\":\"PMAO\",\"inheritance\":[\"AD;AR\"]}]},\"previous\":{\"result\":\"INCONCLUSIVE\",\"result_text\":\"의뢰 사유와 관련 가능성이 불분명한 변이가 발견되었습니다.\",\"clinical_information\":\"\",\"variants\":[{\"snv\":\"hg19:17:010428822:C:A\",\"analysis\":\"d0ccf612-c7b4-4390-8a38-1f36591671cc:22DGS026:001\",\"gene\":\"MYH2\",\"hgvsc\":\"c.4483G>T\",\"hgvsp\":\"p.Glu1495Ter\",\"zygosity\":\"Het\",\"disease\":\"PMAO\",\"inheritance\":\"AD;AR\",\"class\":\"LPV\"},{\"snv\":\"hg19:17:010447093:C:T\",\"analysis\":\"d0ccf612-c7b4-4390-8a38-1f36591671cc:22DGS026:001\",\"gene\":\"MYH2\",\"hgvsc\":\"c.676G>A\",\"hgvsp\":\"p.Ala226Thr\",\"zygosity\":\"Het\",\"disease\":\"PMAO\",\"inheritance\":\"AD;AR\",\"class\":\"VUS\"}],\"abbreviation_reference\":\"NM_017534.6(MYH2)\",\"abbreviation_disease\":\"PMAO, Proximal myopathy and ophthalmoplegia\",\"abbreviation\":\"AD;AR, Autosomal dominant; Het, Heterozygous; LPV, Likely Pathogenic Variant; VUS, Variant of Uncertain Significance\",\"interpretation\":\"[2023.01.06]\\nDGS 분석 결과, MYH2 유전자에서 Likely Pathogenic Variant (LPV)가 발견되었습니다.\\nMYH2 유전자에서 Variant of Uncertain Significance (VUS)가 발견되었습니다.\\n\\n\\nMYH2 유전자의 c.4483G>T (p.Glu1495Ter) 변이는 일반 인구집단(gnomAD, KRGDB)에서 보고된 바 없는 매우 드문 변이입니다.\\nMYH2 유전자는 Proximal myopathy and ophthalmoplegia 관련 유전자로 <?> 유전 양상을 보입니다.\\n\\nMYH2 유전자의 c.676G>A (p.Ala226Thr) 변이는 일반 인구집단(gnomAD, KRGDB)에서 보고된 바 없는 매우 드문 변이로, in-silico prediction (SIFT, PolyPhen, MutationTaster)에서는 Deleterious로 예측되었습니다.\\nMYH2 유전자는 Proximal myopathy and ophthalmoplegia 관련 유전자로 <?> 유전 양상을 보입니다.\",\"recommendation\":\"\",\"mean_depth\":\"NaN\",\"coverage\":\"NaN\",\"inspector\":\"이명근\",\"checker1\":\"\",\"comment1\":\"\",\"checker2\":\"\",\"comment2\":\"\",\"consent_incidental_findings\":false,\"incidental_findings\":null,\"revision\":null}}";
    @Test
    void testInterpretation() throws JsonProcessingException {
        Object result = dgs.interpret(202210319322400L, "N127", objectMapper.readValue(testParams, InterpretationParam.class)).block();
        System.out.println(objectMapper.writeValueAsString(result));
    }
}