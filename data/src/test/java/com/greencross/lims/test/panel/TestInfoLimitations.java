package com.greencross.lims.test.panel;

import com.gcgenome.lims.test.panel.TestInfo;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.gcgenome.lims.test.panel.TestInfo.*;
import static org.assertj.core.api.Assertions.assertThat;

class TestInfoLimitations {

    List<TestInfo> targets = List.of(N037,N038,N039,N040,N041,N042,N043,N044,N045,N046,N047,N048,N049,N050,N051,N052,N053,N054,N055,N062,N067,N071,N072,N073,N076,N078,N079,N080,N081,N084,N095,N096,N097,N098,N099,N100,N106,N107,N108,N110,N119,N122,N123,N130,N148,N149,N176,N180,N181,N182,N183,N184, N187, N188, N189, N190, N191, N192, N193, N194);
    String limitation = "본 검사는 염기서열분석법으로 시행되었으며, 검사에 포함된 유전자들의 coding exon과 인접 intron 영역을 분석하고, single exon deletion/duplication, deep intronic mutation, repeat expansion, imprinting defect, genomic rearrangement, low-level mosaicism 및 염색체 레벨의 copy number variation 검출은 제한적입니다. Target region이 capture되지 않았을 가능성도 있으며, homologous region이 존재하는 유전자 혹은 exon의 경우 변이 검출의 정확도가 떨어질 수 있습니다. Confirmatory Sanger sequencing은 자체 설정한 생물정보학적 분석 기준에 따라 생략할 수 있습니다. 검사에서 발견된 변이는 2015 ACMG/AMP guideline (Genet Med 2015;17:405-24)에 따라 \"pathogenic\", \"likely pathogenic\", \"uncertain significance\", \"likely benign\", \"benign\" 의 다섯가지 카테고리로 분류되며, \"likely benign\"과 \"benign\"에 해당하는 변이는 보고하지 않습니다. 관련 문헌과 데이터베이스의 추가적인 연구 결과에 따라 해당 변이의 분류가 변경될 수 있습니다.";
    @Test
    void testLimitations(){
        targets.forEach(target -> assertThat(target.limitation()).isEqualTo(limitation));
    }
}

