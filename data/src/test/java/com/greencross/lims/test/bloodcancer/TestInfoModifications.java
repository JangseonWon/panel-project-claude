package com.greencross.lims.test.bloodcancer;

import com.gcgenome.lims.test.bloodcancer.TestInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.gcgenome.lims.test.bloodcancer.TestInfo.*;
import static org.assertj.core.api.Assertions.assertThat;

class TestInfoModifications {

    List<TestInfo> testTargets = List.of(N064,N065,N082,N094,N083,N093,N157,N104,N105);

    @Test
    @DisplayName("Sequencing 설정 잘 되었는지 확인")
    void testSequencing(){
        testTargets.forEach(target -> {
            assertThat(target.sequencing()).isEqualTo("Sequencing by synthesis (Illumina)");
        });
    }

    @Test
    @DisplayName("pipeline 설정 잘 되었는지 확인")
    void testPipeline(){
        testTargets.forEach(target -> {
            assertThat(target.pipeline()).isEqualTo("BI_HEM.v.1.0 (Alignment: BWA, Variant calling: VarScan2_GATK)");
        });
    }

    @Test
    @DisplayName("genes에 version 없는지 확인")
    void testGenesHasNoVersions(){
        testTargets.forEach(target -> {
            Arrays.stream(target.genesEssential()).forEach(gene -> assertThat(gene.reference()).doesNotContain("."));
            Arrays.stream(target.genesSelective()).forEach(gene -> assertThat(gene.reference()).doesNotContain("."));
        });
    }

    @Test
    @DisplayName("N064, N065 테스트")
    void testN064AndN065(){
        List.of(N064, N065).forEach(target -> {
            assertThat(target.method()).isEqualTo("Hybridization with oligonucleotide probes (HEMA v.2301.1)");
            Gene KDM6A = Arrays.stream(target.genesSelective()).filter(gene -> gene.name().equals("KDM6A")).findFirst().get();
            assertThat(KDM6A.reference()).isEqualTo("NM_021140");
        });
    }

    @Test
    @DisplayName("N082, N094 테스트")
    void testN082AndN094(){
        List.of(N082, N094).forEach(target -> {
            assertThat(target.method()).isEqualTo("Hybridization with oligonucleotide probes (ALL v.2301.1)");
            Gene PDGFRB = Arrays.stream(target.genesSelective()).filter(gene -> gene.name().equals("PDGFRB")).findFirst().orElse(null);
            Optional<Gene> CRLF2 = Arrays.stream(target.genesSelective()).filter(gene -> gene.name().equals("CRLF2")).findFirst();
            assertThat(CRLF2.isEmpty()).isTrue();
            assertThat(PDGFRB).isNotNull();
            assertThat(PDGFRB.exon()).isEqualTo("12, 14, 18");
            assertThat(PDGFRB.reference()).isEqualTo("NM_002609");
        });
    }

    @Test
    @DisplayName("N083, N093, N157 테스트")
    void testN083_N093_N157(){
        List.of(N083, N093, N157).forEach(target -> {
            assertThat(target.method()).isEqualTo("Hybridization with oligonucleotide probes (LYM v.2301.1)");
        });
    }

    @Test
    @DisplayName("N104, N105 테스트")
    void testN104AndN105(){
        List.of(N104, N105).forEach(target -> {
            assertThat(target.method()).isEqualTo("Hybridization with oligonucleotide probes (MM v.2301.1)");
            Optional<Gene> CRLF2 = Arrays.stream(target.genesSelective()).filter(gene -> gene.name().equals("CRLF2")).findFirst();
            Optional<Gene> PTEN = Arrays.stream(target.genesSelective()).filter(gene -> gene.name().equals("PTEN")).findFirst();
            Optional<Gene> ZFHX4 = Arrays.stream(target.genesSelective()).filter(gene -> gene.name().equals("ZFHX4")).findFirst();
            assertThat(CRLF2.isEmpty()).isTrue();
            assertThat(PTEN.isEmpty()).isTrue();
            assertThat(ZFHX4.isEmpty()).isTrue();
        });
    }
}