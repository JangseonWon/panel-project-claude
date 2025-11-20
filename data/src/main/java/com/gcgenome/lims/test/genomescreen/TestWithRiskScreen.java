package com.gcgenome.lims.test.genomescreen;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@Accessors(fluent = true)
@SuperBuilder(builderMethodName = "builder2", toBuilder = true)
public class TestWithRiskScreen extends TestInfo implements TestInfo.RiskScreen {
	private Genotype[] genotypes;
	public static final TestWithRiskScreen N087 = TestWithRiskScreen.builder2()
			.code("N087")
			.summaryCode("N087005")
			.interpretationCode("N087008")
			.name("뇌졸중 지놈 스크린 / 검진")
			.i18n("KOKR")
			.diseases(new Disease[] {
					Disease.builder().name("베타 지중해혈증").genes(new Gene[]{Gene.builder().code("N087010").gene("HBB").build()}).build(),
					Disease.builder().name("고호모시스테인혈증").genes(new Gene[]{Gene.builder().code("N087020").gene("CBS").build()}).build(),
					Disease.builder().name("파브리병").genes(new Gene[]{Gene.builder().code("N087030").gene("GLA").build()}).build(),
					Disease.builder().name("탄력섬유거짓황색종").genes(new Gene[]{Gene.builder().code("N087040").gene("ABCC6").build()}).build(),
					Disease.builder().name("카다실").genes(new Gene[]{Gene.builder().code("N087050").gene("NOTCH3").build()}).build(),
					Disease.builder().name("카라실").genes(new Gene[]{Gene.builder().code("N087060").gene("HTRA1").build()}).build(),
					Disease.builder().name("백색질 형성장애 동반 망막 혈관병증").genes(new Gene[]{Gene.builder().code("N087070").gene("TREX1").build()}).build(),
					Disease.builder().name("심장 긴간격 증후군").genes(new Gene[]{
							Gene.builder().code("N087080").gene("KCNQ1").build(),
							Gene.builder().code("N087090").gene("KCNJ2").build(),
							Gene.builder().code("N087100").gene("SCN5A").build()}).build(),
					Disease.builder().name("혈관성 엘러스 단로스 증후군").genes(new Gene[]{Gene.builder().code("N087110").gene("COL3A1").build()}).build(),
					Disease.builder().name("마르판 증후군").genes(new Gene[]{Gene.builder().code("N087120").gene("FBN1").build()}).build(),
					Disease.builder().name("결절성 다발동맥염").genes(new Gene[]{Gene.builder().code("N087130").gene("ADA2").build()}).build(),
					Disease.builder().name("동맥 비틀림 증후군").genes(new Gene[]{Gene.builder().code("N087140").gene("SLC2A10").build()}).build(),
					Disease.builder().name("가족성 편마비 편두통").genes(new Gene[]{
							Gene.builder().code("N087150").gene("CACNA1A").build(),
							Gene.builder().code("N087160").gene("ATP1A2").build(),
							Gene.builder().code("N087170").gene("SCN1A").build()}).build(),
					Disease.builder().name("모야모야병").genes(new Gene[]{
							Gene.builder().code("N087180").gene("RNF213").build(),
							Gene.builder().code("N087190").gene("ACTA2").build(),
							Gene.builder().code("N087200").gene("GUCY1A1").build()}).build(),
					Disease.builder().name("아밀로이드 뇌 혈관병증").genes(new Gene[]{
							Gene.builder().code("N087210").gene("APP").build(),
							Gene.builder().code("N087220").gene("CST3").build(),
							Gene.builder().code("N087230").gene("ITM2B").build()}).build(),
					Disease.builder().name("뇌 소혈관질환").genes(new Gene[]{Gene.builder().code("N087240").gene("COL4A1").build()}).build(),
					Disease.builder().name("뇌구멍증").genes(new Gene[]{Gene.builder().code("N087250").gene("COL4A2").build()}).build(),
					Disease.builder().name("가족성 고콜레스테롤혈증").genes(new Gene[]{
							Gene.builder().code("N087260").gene("LDLR").build(),
							Gene.builder().code("N087270").gene("APOB").build(),
							Gene.builder().code("N087280").gene("PCSK9").build(),
							Gene.builder().code("N087290").gene("APOE").build()}).build(),
					Disease.builder().name("FactorVLeiden혈전증").genes(new Gene[]{Gene.builder().code("N087300").gene("F5").build()}).build(),
					Disease.builder().name("프로트롬빈 관련 혈전증").genes(new Gene[]{Gene.builder().code("N087310").gene("F2").build()}).build(),
					Disease.builder().name("항트롬빈 결핍 혈전증").genes(new Gene[]{Gene.builder().code("N087320").gene("SERPINC1").build()}).build(),
					Disease.builder().name("단백질C 결핍 혈전증").genes(new Gene[]{Gene.builder().code("N087330").gene("PROC").build()}).build(),
					Disease.builder().name("단백질S 결핍 혈전증").genes(new Gene[]{Gene.builder().code("N087340").gene("PROS1").build()}).build()
			}).genotypes(new Genotype[] {
					Genotype.builder().gene("APOE").pos("").code("N087350").types(new String[] {"e2e2", "e2e3", "e2e4", "e3e3", "e3e4", "e4e4"}).build(),
					Genotype.builder().gene("MTHFR").pos("c.677").code("N087360").types(new String[] {"CC", "CT", "TT"}).build(),
					Genotype.builder().gene("MTHFR").pos("c.1298").code("N087370").types(new String[] {"AA", "AC", "CC"}).build(),
					Genotype.builder().gene("RNF213").pos("c.14429").code("N087380").types(new String[] {"GG", "GA", "AA"}).build(),
					Genotype.builder().gene("NOTCH3").pos("c.1630").code("N087390").types(new String[] {"CC", "CT", "TT"}).build()
			}).build();
	public static final TestWithRiskScreen N088 = TestWithRiskScreen.builder2()
			.code("N088")
			.summaryCode("N088005")
			.interpretationCode("N088007")
			.i18n("KOKR")
			.name("고지혈증 지놈 스크린 / 검진")
			.diseases(new Disease[] {
					Disease.builder().name("가족성 고콜레스테롤혈증").genes(new Gene[]{
							Gene.builder().code("N088010").gene("APOB").build(),
							Gene.builder().code("N088020").gene("APOA2").build(),
							Gene.builder().code("N088030").gene("LDLR").build(),
							Gene.builder().code("N088040").gene("LDLRAP1").build(),
							Gene.builder().code("N088050").gene("PCSK9").build(),
							Gene.builder().code("N088060").gene("STAP1").build()}).build(),
					Disease.builder().name("뇌건황색종증").genes(new Gene[]{Gene.builder().code("N088070").gene("CYP27A1").build()}).build(),
					Disease.builder().name("시토스테롤혈증").genes(new Gene[]{
							Gene.builder().code("N088080").gene("ABCG5").build(),
							Gene.builder().code("N088090").gene("ABCG8").build()}).build(),
					Disease.builder().name("고알파지질단백혈증").genes(new Gene[]{
							Gene.builder().code("N088100").gene("APOC3").build(),
							Gene.builder().code("N088110").gene("CETP").build(),
							Gene.builder().code("N088120").gene("SCARB1").build()}).build(),
					Disease.builder().name("스타틴 부작용").genes(new Gene[]{Gene.builder().code("N088130").gene("SLCO1B1").build()}).build(),
					Disease.builder().name("저베타지질단백혈증").genes(new Gene[]{
							Gene.builder().code("N088140").gene("MTTP").build(),
							Gene.builder().code("N088010").gene("APOB").build(),
							Gene.builder().code("N088160").gene("SAR1B").build(),
							Gene.builder().code("N088170").gene("ANGPTL3").build()}).build(),
					Disease.builder().name("저알파지질단백혈증").genes(new Gene[]{
							Gene.builder().code("N088180").gene("ABCA1").build(),
							Gene.builder().code("N088190").gene("APOA1").build(),
							Gene.builder().code("N088200").gene("LCAT").build()}).build(),
					Disease.builder().name("이상베타지질단백혈증").genes(new Gene[]{Gene.builder().code("N088210").gene("APOE").build()}).build(),
					Disease.builder().name("복합 고지질혈증").genes(new Gene[]{
							Gene.builder().code("N088300").gene("LPL").build(),
							Gene.builder().code("N088230").gene("LIPA").build(),
							Gene.builder().code("N088240").gene("LIPC").build()}).build(),
					Disease.builder().name("가족성 지질단백 지질분해효소 결핍증").genes(new Gene[]{
							Gene.builder().code("N088260").gene("APOC2").build(),
							Gene.builder().code("N088270").gene("APOA5").build(),
							Gene.builder().code("N088280").gene("GPIHBP1").build(),
							Gene.builder().code("N088290").gene("LMF1").build(),
							Gene.builder().code("N088300").gene("LPL").build()}).build(),
					Disease.builder().name("고중성지방혈증").genes(new Gene[]{
							Gene.builder().code("N088310").gene("CREB3L3").build(),
							Gene.builder().code("N088320").gene("CYP7A1").build(),
							Gene.builder().code("N088330").gene("GPD1").build(),
							Gene.builder().code("N088280").gene("GPIHBP1").build()}).build(),
					Disease.builder().name("알스트롬 증후군").genes(new Gene[]{Gene.builder().code("N088350").gene("ALMS1").build()}).build(),
			}).genotypes(new Genotype[] {
					Genotype.builder().gene("APOE").pos("").code("N088360").types(new String[] {"e2e2", "e2e3", "e2e4", "e3e3", "e3e4", "e4e4"}).build(),
					Genotype.builder().gene("APOA5").pos("c.553").code("N088370").types(new String[] {"GG", "GT", "TT"}).build(),
					Genotype.builder().gene("APOA5").pos("c.56").code("N088380").types(new String[] {"CC", "CG", "GG"}).build(),
					Genotype.builder().gene("COQ2").pos("c.779-1022").code("N088390").types(new String[] {"GG", "GC", "CC"}).build()
			}).build();
	public static final TestWithRiskScreen ON087 = TestWithRiskScreen.builder2()
			.code("ON087")
			.summaryCode("N087005")
			.interpretationCode("N087008")
			.name("뇌졸중 지놈 스크린 / 검진")
			.i18n("ENUS")
			.diseases(new Disease[] {
					Disease.builder().name("베타 지중해혈증").genes(new Gene[]{Gene.builder().code("N087010").gene("HBB").build()}).build(),
					Disease.builder().name("고호모시스테인혈증").genes(new Gene[]{Gene.builder().code("N087020").gene("CBS").build()}).build(),
					Disease.builder().name("파브리병").genes(new Gene[]{Gene.builder().code("N087030").gene("GLA").build()}).build(),
					Disease.builder().name("탄력섬유거짓황색종").genes(new Gene[]{Gene.builder().code("N087040").gene("ABCC6").build()}).build(),
					Disease.builder().name("카다실").genes(new Gene[]{Gene.builder().code("N087050").gene("NOTCH3").build()}).build(),
					Disease.builder().name("카라실").genes(new Gene[]{Gene.builder().code("N087060").gene("HTRA1").build()}).build(),
					Disease.builder().name("백색질 형성장애 동반 망막 혈관병증").genes(new Gene[]{Gene.builder().code("N087070").gene("TREX1").build()}).build(),
					Disease.builder().name("심장 긴간격 증후군").genes(new Gene[]{
							Gene.builder().code("N087080").gene("KCNQ1").build(),
							Gene.builder().code("N087090").gene("KCNJ2").build(),
							Gene.builder().code("N087100").gene("SCN5A").build()}).build(),
					Disease.builder().name("혈관성 엘러스 단로스 증후군").genes(new Gene[]{Gene.builder().code("N087110").gene("COL3A1").build()}).build(),
					Disease.builder().name("마르판 증후군").genes(new Gene[]{Gene.builder().code("N087120").gene("FBN1").build()}).build(),
					Disease.builder().name("결절성 다발동맥염").genes(new Gene[]{Gene.builder().code("N087130").gene("ADA2").build()}).build(),
					Disease.builder().name("동맥 비틀림 증후군").genes(new Gene[]{Gene.builder().code("N087140").gene("SLC2A10").build()}).build(),
					Disease.builder().name("가족성 편마비 편두통").genes(new Gene[]{
							Gene.builder().code("N087150").gene("CACNA1A").build(),
							Gene.builder().code("N087160").gene("ATP1A2").build(),
							Gene.builder().code("N087170").gene("SCN1A").build()}).build(),
					Disease.builder().name("모야모야병").genes(new Gene[]{
							Gene.builder().code("N087180").gene("RNF213").build(),
							Gene.builder().code("N087190").gene("ACTA2").build(),
							Gene.builder().code("N087200").gene("GUCY1A1").build()}).build(),
					Disease.builder().name("아밀로이드 뇌 혈관병증").genes(new Gene[]{
							Gene.builder().code("N087210").gene("APP").build(),
							Gene.builder().code("N087220").gene("CST3").build(),
							Gene.builder().code("N087230").gene("ITM2B").build()}).build(),
					Disease.builder().name("뇌 소혈관질환").genes(new Gene[]{Gene.builder().code("N087240").gene("COL4A1").build()}).build(),
					Disease.builder().name("뇌구멍증").genes(new Gene[]{Gene.builder().code("N087250").gene("COL4A2").build()}).build(),
					Disease.builder().name("가족성 고콜레스테롤혈증").genes(new Gene[]{
							Gene.builder().code("N087260").gene("LDLR").build(),
							Gene.builder().code("N087270").gene("APOB").build(),
							Gene.builder().code("N087280").gene("PCSK9").build(),
							Gene.builder().code("N087290").gene("APOE").build()}).build(),
					Disease.builder().name("FactorVLeiden혈전증").genes(new Gene[]{Gene.builder().code("N087300").gene("F5").build()}).build(),
					Disease.builder().name("프로트롬빈 관련 혈전증").genes(new Gene[]{Gene.builder().code("N087310").gene("F2").build()}).build(),
					Disease.builder().name("항트롬빈 결핍 혈전증").genes(new Gene[]{Gene.builder().code("N087320").gene("SERPINC1").build()}).build(),
					Disease.builder().name("단백질C 결핍 혈전증").genes(new Gene[]{Gene.builder().code("N087330").gene("PROC").build()}).build(),
					Disease.builder().name("단백질S 결핍 혈전증").genes(new Gene[]{Gene.builder().code("N087340").gene("PROS1").build()}).build()
			}).genotypes(new Genotype[] {
					Genotype.builder().gene("APOE").pos("").code("N087350").types(new String[] {"e2e2", "e2e3", "e2e4", "e3e3", "e3e4", "e4e4"}).build(),
					Genotype.builder().gene("MTHFR").pos("c.677").code("N087360").types(new String[] {"CC", "CT", "TT"}).build(),
					Genotype.builder().gene("MTHFR").pos("c.1298").code("N087370").types(new String[] {"AA", "AC", "CC"}).build(),
					Genotype.builder().gene("RNF213").pos("c.14429").code("N087380").types(new String[] {"GG", "GA", "AA"}).build(),
					Genotype.builder().gene("NOTCH3").pos("c.1630").code("N087390").types(new String[] {"CC", "CT", "TT"}).build()
			}).build();
	public static final TestWithRiskScreen ON088 = TestWithRiskScreen.builder2()
			.code("ON088")
			.summaryCode("N088005")
			.interpretationCode("N088007")
			.i18n("ENUS")
			.name("고지혈증 지놈 스크린 / 검진")
			.diseases(new Disease[] {
					Disease.builder().name("가족성 고콜레스테롤혈증").genes(new Gene[]{
							Gene.builder().code("N088010").gene("APOB").build(),
							Gene.builder().code("N088020").gene("APOA2").build(),
							Gene.builder().code("N088030").gene("LDLR").build(),
							Gene.builder().code("N088040").gene("LDLRAP1").build(),
							Gene.builder().code("N088050").gene("PCSK9").build(),
							Gene.builder().code("N088060").gene("STAP1").build()}).build(),
					Disease.builder().name("뇌건황색종증").genes(new Gene[]{Gene.builder().code("N088070").gene("CYP27A1").build()}).build(),
					Disease.builder().name("시토스테롤혈증").genes(new Gene[]{
							Gene.builder().code("N088080").gene("ABCG5").build(),
							Gene.builder().code("N088090").gene("ABCG8").build()}).build(),
					Disease.builder().name("고알파지질단백혈증").genes(new Gene[]{
							Gene.builder().code("N088100").gene("APOC3").build(),
							Gene.builder().code("N088110").gene("CETP").build(),
							Gene.builder().code("N088120").gene("SCARB1").build()}).build(),
					Disease.builder().name("스타틴 부작용").genes(new Gene[]{Gene.builder().code("N088130").gene("SLCO1B1").build()}).build(),
					Disease.builder().name("저베타지질단백혈증").genes(new Gene[]{
							Gene.builder().code("N088140").gene("MTTP").build(),
							Gene.builder().code("N088010").gene("APOB").build(),
							Gene.builder().code("N088160").gene("SAR1B").build(),
							Gene.builder().code("N088170").gene("ANGPTL3").build()}).build(),
					Disease.builder().name("저알파지질단백혈증").genes(new Gene[]{
							Gene.builder().code("N088180").gene("ABCA1").build(),
							Gene.builder().code("N088190").gene("APOA1").build(),
							Gene.builder().code("N088200").gene("LCAT").build()}).build(),
					Disease.builder().name("이상베타지질단백혈증").genes(new Gene[]{Gene.builder().code("N088210").gene("APOE").build()}).build(),
					Disease.builder().name("복합 고지질혈증").genes(new Gene[]{
							Gene.builder().code("N088300").gene("LPL").build(),
							Gene.builder().code("N088230").gene("LIPA").build(),
							Gene.builder().code("N088240").gene("LIPC").build()}).build(),
					Disease.builder().name("가족성 지질단백 지질분해효소 결핍증").genes(new Gene[]{
							Gene.builder().code("N088260").gene("APOC2").build(),
							Gene.builder().code("N088270").gene("APOA5").build(),
							Gene.builder().code("N088280").gene("GPIHBP1").build(),
							Gene.builder().code("N088290").gene("LMF1").build(),
							Gene.builder().code("N088300").gene("LPL").build()}).build(),
					Disease.builder().name("고중성지방혈증").genes(new Gene[]{
							Gene.builder().code("N088310").gene("CREB3L3").build(),
							Gene.builder().code("N088320").gene("CYP7A1").build(),
							Gene.builder().code("N088330").gene("GPD1").build(),
							Gene.builder().code("N088280").gene("GPIHBP1").build()}).build(),
					Disease.builder().name("알스트롬 증후군").genes(new Gene[]{Gene.builder().code("N088350").gene("ALMS1").build()}).build(),
			}).genotypes(new Genotype[] {
					Genotype.builder().gene("APOE").pos("").code("N088360").types(new String[] {"e2e2", "e2e3", "e2e4", "e3e3", "e3e4", "e4e4"}).build(),
					Genotype.builder().gene("APOA5").pos("c.553").code("N088370").types(new String[] {"GG", "GT", "TT"}).build(),
					Genotype.builder().gene("APOA5").pos("c.56").code("N088380").types(new String[] {"CC", "CG", "GG"}).build(),
					Genotype.builder().gene("COQ2").pos("c.779-1022").code("N088390").types(new String[] {"GG", "GC", "CC"}).build()
			}).build();
	public static final TestInfo J020 = N087.toBuilder()
			.code("J020")
			.name("뇌졸중 지놈 스크린 / 검진 (아이메드)")
			.build();
	public static final TestInfo J021 = N088.toBuilder()
			.code("J021")
			.name("고지혈증 지놈 스크린 / 검진 (아이메드)")
			.build();
	public static final TestWithRiskScreen[] TESTS = new TestWithRiskScreen[] {
			N087, N088, ON087, ON088
	};
}
