package com.gcgenome.lims.test.genomescreen;

import com.gcgenome.lims.test.HasCode;
import com.gcgenome.lims.test.HasGenes;
import com.gcgenome.lims.test.I18N;
import com.gcgenome.lims.test.MayBeNationalInsurance;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.util.Arrays;
import java.util.HashSet;
import java.util.stream.Stream;

@Data
@Accessors(fluent = true)
@SuperBuilder(toBuilder = true)
public class TestInfo implements HasCode, HasGenes, MayBeNationalInsurance, I18N {
	@Data
	@Accessors(fluent = true)
	@SuperBuilder(toBuilder = true)
	public static class Gene {
		private String gene;
		private String code;
	}
	@Data
	@Accessors(fluent = true)
	@SuperBuilder(toBuilder = true)
	public static class Disease {
		private String name;
		private Gene[] genes;
	}
	private final String code;
	private final String name;
	private final String i18n;
	private final Disease[] diseases;
	// private final Gene[] cores;
	@Override
	public String[] genes() {
		HashSet<String> genes = new HashSet<>();
		if(diseases!=null) for(Disease disease: diseases) if(disease.genes!=null) for(Gene gene: disease.genes()) genes.add(gene.gene());
		return genes.toArray(new String[0]);
	}
	@Builder.Default
	private final String summaryCode = null;
	private final String interpretationCode;
	@Builder.Default
	private final boolean isNationalInsuranceTest = false;
	public static final TestInfo N074 = TestInfo.builder()
			.code("N074")
			.summaryCode("N074005")
			.interpretationCode("N074270")
			.name("암유전자 패널검사 (강북삼성)")
			.i18n("KOKR")
			.diseases(new Disease[]{
					Disease.builder().name("유전성 유방암 난소암 증후군").genes(new Gene[]{
							Gene.builder().code("N074010").gene("BRCA1").build(),
							Gene.builder().code("N074020").gene("BRCA2").build()
					}).build(),
					Disease.builder().name("리 프라우메니 증후군").genes(new Gene[]{
							Gene.builder().code("N074030").gene("TP53").build()}).build(),
					Disease.builder().name("포이츠 제거스 증후군").genes(new Gene[]{
							Gene.builder().code("N074040").gene("STK11").build()}).build(),
					Disease.builder().name("린치 증후군").genes(new Gene[]{
							Gene.builder().code("N074050").gene("MLH1").build(),
							Gene.builder().code("N074060").gene("MSH2").build(),
							Gene.builder().code("N074070").gene("MSH6").build(),
							Gene.builder().code("N074080").gene("PMS2").build()}).build(),
					Disease.builder().name("가족성 선종성 용종증").genes(new Gene[]{Gene.builder().code("N074090").gene("APC").build()}).build(),
					Disease.builder().name("MUTYH 연관 용종증").genes(new Gene[]{Gene.builder().code("N074100").gene("MUTYH").build()}).build(),
					Disease.builder().name("연소성 용종증 증후군").genes(new Gene[]{
							Gene.builder().code("N074110").gene("BMPR1A").build(),
							Gene.builder().code("N074120").gene("SMAD4").build()}).build(),
					Disease.builder().name("폰히펠 린다우 증후군").genes(new Gene[]{Gene.builder().code("N074130").gene("VHL").build()}).build(),
					Disease.builder().name("제1형 다발성 내분비선종증").genes(new Gene[]{Gene.builder().code("N074140").gene("MEN1").build()}).build(),
					Disease.builder().name("제2형 다발성 내분비선종증").genes(new Gene[]{Gene.builder().code("N074150").gene("RET").build()}).build(),
					Disease.builder().name("PTEN 과오종 증후군").genes(new Gene[]{Gene.builder().code("N074160").gene("PTEN").build()}).build(),
					Disease.builder().name("망막모세포종").genes(new Gene[]{Gene.builder().code("N074170").gene("RB1").build()}).build(),
					Disease.builder().name("유전성 부신경절종 갈색세포종").genes(new Gene[]{
							Gene.builder().code("N074180").gene("SDHD").build(),
							Gene.builder().code("N074190").gene("SDHAF2").build(),
							Gene.builder().code("N074200").gene("SDHC").build(),
							Gene.builder().code("N074210").gene("SDHB").build()}).build(),
					Disease.builder().name("결절성 경화증").genes(new Gene[]{
							Gene.builder().code("N074220").gene("TSC1").build(),
							Gene.builder().code("N074230").gene("TSC2").build()}).build(),
					Disease.builder().name("WT1 연관 윌름스 종양").genes(new Gene[]{Gene.builder().code("N074240").gene("WT1").build()}).build(),
					Disease.builder().name("제2형 신경섬유종증").genes(new Gene[]{Gene.builder().code("N074250").gene("NF2").build()}).build()
			}).build();
	public static final TestInfo N075 = TestInfo.builder()
			.code("N075")
			.summaryCode("N075005")
			.interpretationCode("N075008")
			.name("심장유전자 패널검사 (강북삼성)")
			.i18n("KOKR")
			.diseases(new Disease[]{
					Disease.builder().name("엘러스-단로스 증후군").genes(new Gene[]{Gene.builder().code("N075010").gene("COL3A1").build()}).build(),
					Disease.builder().name("마르판 증후군").genes(new Gene[]{Gene.builder().code("N075020").gene("FBN1").build()}).build(),
					Disease.builder().name("로이-디에츠 증후군").genes(new Gene[]{
							Gene.builder().code("N075060").gene("TGFBR1").build(),
							Gene.builder().code("N075070").gene("TGFBR2").build(),
							Gene.builder().code("N075030").gene("SMAD3").build(),
							Gene.builder().code("N075040").gene("TGFB2").build(),
							Gene.builder().code("N075050").gene("TGFB3").build()}).build(),
					Disease.builder().name("가족성 흉부대동맥류와 박리증").genes(new Gene[]{
							Gene.builder().code("N075020").gene("FBN1").build(),
							Gene.builder().code("N075030").gene("SMAD3").build(),
							Gene.builder().code("N075060").gene("TGFBR1").build(),
							Gene.builder().code("N075070").gene("TGFBR2").build(),
							Gene.builder().code("N075072").gene("ACTA2").build(),
							Gene.builder().code("N075074").gene("MYH11").build(),
							Gene.builder().code("N075076").gene("MYLK").build(),}).build(),
					Disease.builder().name("비후성 심근병증").genes(new Gene[]{
							Gene.builder().code("N075125").gene("MYBPC3").build(),
							Gene.builder().code("N075130").gene("MYH7").build(),
							Gene.builder().code("N075140").gene("TNNT2").build(),
							Gene.builder().code("N075150").gene("TNNI3").build(),
							Gene.builder().code("N075160").gene("TPM1").build(),
							Gene.builder().code("N075170").gene("MYL3").build(),
							Gene.builder().code("N075200").gene("MYL2").build(),
							Gene.builder().code("N075120").gene("CSRP3").build(),
							Gene.builder().code("N075190").gene("PRKAG2").build()}).build(),
					Disease.builder().name("확장성 심근병증").genes(new Gene[]{
							Gene.builder().code("N075180").gene("ACTC1").build(),
							Gene.builder().code("N075130").gene("MYH7").build(),
							Gene.builder().code("N075210").gene("LMNA").build(),
							Gene.builder().code("N075202").gene("BAG3").build(),
							Gene.builder().code("N075204").gene("DES").build(),}).build(),
					Disease.builder().name("파브리병").genes(new Gene[]{Gene.builder().code("N075220").gene("GLA").build()}).build(),
					Disease.builder().name("부정맥 유발성 우심실 심근병증").genes(new Gene[]{
							Gene.builder().code("N075230").gene("PKP2").build(),
							Gene.builder().code("N075240").gene("DSP").build(),
							Gene.builder().code("N075250").gene("DSC2").build(),
							Gene.builder().code("N075260").gene("TMEM43").build(),
							Gene.builder().code("N075270").gene("DSG2").build()}).build(),
					Disease.builder().name("애머리 드라이푸스 증후군").genes(new Gene[]{
							Gene.builder().code("N075272").gene("EMD").build(),
							Gene.builder().code("N075274").gene("FHL1").build()}).build(),
					Disease.builder().name("카테콜아민성 다형성 심실성 빈맥").genes(new Gene[]{Gene.builder().code("N075080").gene("RYR2").build()}).build(),
					Disease.builder().name("심장 긴간격 증후군").genes(new Gene[]{
							Gene.builder().code("N075090").gene("KCNQ1").build(),
							Gene.builder().code("N075100").gene("KCNH2").build(),
							Gene.builder().code("N075110").gene("SCN5A").build()}).build(),
					Disease.builder().name("브루가다 증후군").genes(new Gene[]{Gene.builder().code("N075110").gene("SCN5A").build()}).build(),
					Disease.builder().name("가족성 고콜레스테롤혈증").genes(new Gene[]{
							Gene.builder().code("N075280").gene("LDLR").build(),
							Gene.builder().code("N075290").gene("APOB").build(),
							Gene.builder().code("N075300").gene("PCSK9").build()}).build(),
					Disease.builder().name("고호모시스테인혈전증").genes(new Gene[]{Gene.builder().code("N075310").gene("CBS").build()}).build(),
					Disease.builder().name("동맥비틀림증후군").genes(new Gene[]{Gene.builder().code("N075320").gene("SLC2A10").build()}).build()
			}).build();
	public static final TestInfo N089 = TestInfo.builder()
			.code("N089")
			.summaryCode("N089005")
			.interpretationCode("N089008")
			.name("심장 돌연사 지놈 스크린 / 검진")
			.i18n("KOKR")
			.diseases(new Disease[]{
					Disease.builder().name("엘러스-단로스 증후군").genes(new Gene[]{Gene.builder().code("N089010").gene("COL3A1").build()}).build(),
					Disease.builder().name("마르판 증후군").genes(new Gene[]{Gene.builder().code("N089020").gene("FBN1").build()}).build(),
					Disease.builder().name("로이-디에츠 증후군").genes(new Gene[]{
							Gene.builder().code("N089060").gene("TGFBR1").build(),
							Gene.builder().code("N089070").gene("TGFBR2").build(),
							Gene.builder().code("N089030").gene("SMAD3").build(),
							Gene.builder().code("N089040").gene("TGFB2").build(),
							Gene.builder().code("N089050").gene("TGFB3").build()}).build(),
					Disease.builder().name("가족성 흉부대동맥류와 박리증").genes(new Gene[]{
							Gene.builder().code("N089020").gene("FBN1").build(),
							Gene.builder().code("N089030").gene("SMAD3").build(),
							Gene.builder().code("N089060").gene("TGFBR1").build(),
							Gene.builder().code("N089070").gene("TGFBR2").build(),
							Gene.builder().code("N089072").gene("ACTA2").build(),
							Gene.builder().code("N089074").gene("MYH11").build(),
							Gene.builder().code("N089076").gene("MYLK").build(),}).build(),
					Disease.builder().name("비후성 심근병증").genes(new Gene[]{
							Gene.builder().code("N089125").gene("MYBPC3").build(),
							Gene.builder().code("N089130").gene("MYH7").build(),
							Gene.builder().code("N089140").gene("TNNT2").build(),
							Gene.builder().code("N089150").gene("TNNI3").build(),
							Gene.builder().code("N089160").gene("TPM1").build(),
							Gene.builder().code("N089170").gene("MYL3").build(),
							Gene.builder().code("N089200").gene("MYL2").build(),
							Gene.builder().code("N089120").gene("CSRP3").build(),
							Gene.builder().code("N089190").gene("PRKAG2").build()}).build(),
					 Disease.builder().name("확장성 심근병증").genes(new Gene[]{
							 Gene.builder().code("N089180").gene("ACTC1").build(),
							 Gene.builder().code("N089130").gene("MYH7").build(),
							 Gene.builder().code("N089210").gene("LMNA").build(),
							 Gene.builder().code("N089202").gene("BAG3").build(),
							 Gene.builder().code("N089204").gene("DES").build(),}).build(),
					Disease.builder().name("파브리병").genes(new Gene[]{Gene.builder().code("N089220").gene("GLA").build()}).build(),
					Disease.builder().name("부정맥 유발성 우심실 심근병증").genes(new Gene[]{
							Gene.builder().code("N089230").gene("PKP2").build(),
							Gene.builder().code("N089240").gene("DSP").build(),
							Gene.builder().code("N089250").gene("DSC2").build(),
							Gene.builder().code("N089260").gene("TMEM43").build(),
							Gene.builder().code("N089270").gene("DSG2").build()}).build(),
					Disease.builder().name("애머리 드라이푸스 증후군").genes(new Gene[]{
							Gene.builder().code("N089272").gene("EMD").build(),
							Gene.builder().code("N089274").gene("FHL1").build()}).build(),
					Disease.builder().name("카테콜아민성 다형성 심실성 빈맥").genes(new Gene[]{Gene.builder().code("N089080").gene("RYR2").build()}).build(),
					Disease.builder().name("심장 긴간격 증후군").genes(new Gene[]{
							Gene.builder().code("N089090").gene("KCNQ1").build(),
							Gene.builder().code("N089100").gene("KCNH2").build(),
							Gene.builder().code("N089110").gene("SCN5A").build()}).build(),
					Disease.builder().name("브루가다 증후군").genes(new Gene[]{Gene.builder().code("N089110").gene("SCN5A").build()}).build(),
					Disease.builder().name("가족성 고콜레스테롤혈증").genes(new Gene[]{
							Gene.builder().code("N089280").gene("LDLR").build(),
							Gene.builder().code("N089290").gene("APOB").build(),
							Gene.builder().code("N089300").gene("PCSK9").build()}).build(),
					Disease.builder().name("고호모시스테인혈전증").genes(new Gene[]{Gene.builder().code("N089310").gene("CBS").build()}).build(),
					Disease.builder().name("동맥비틀림증후군").genes(new Gene[]{Gene.builder().code("N089320").gene("SLC2A10").build()}).build()
			}).build();
	public static final TestInfo N090 = TestInfo.builder() // Cancer
			.code("N090")
			.summaryCode("N090005")
			.interpretationCode("N090007")
			.name("암 지놈 스크린 / 검진")
			.i18n("KOKR")
			.diseases(new Disease[]{
					Disease.builder().name("유전성 유방암 난소암 증후군").genes(new Gene[]{
							Gene.builder().code("N090010").gene("BRCA1").build(),
							Gene.builder().code("N090020").gene("BRCA2").build()}).build(),
					Disease.builder().name("유방암 감수성").genes(new Gene[]{
							Gene.builder().code("N090021").gene("ATM").build(),
							Gene.builder().code("N090022").gene("CDH1").build(),
							Gene.builder().code("N090023").gene("CHEK2").build(),
							Gene.builder().code("N090024").gene("NBN").build(),
							Gene.builder().code("N090025").gene("NF1").build(),
							Gene.builder().code("N090026").gene("PALB2").build()}).build(),
					Disease.builder().name("난소암 감수성").genes(new Gene[]{
							Gene.builder().code("N090027").gene("BRIP1").build(),
							Gene.builder().code("N090028").gene("RAD51C").build(),
							Gene.builder().code("N090029").gene("RAD51D").build()}).build(),
					Disease.builder().name("전립선암 감수성").genes(new Gene[]{
							Gene.builder().code("N090021").gene("ATM").build(),
							Gene.builder().code("N090023").gene("CHEK2").build(),
							Gene.builder().code("N090060").gene("MLH1").build(),
							Gene.builder().code("N090070").gene("MSH2").build(),
							Gene.builder().code("N090080").gene("MSH6").build(),
							Gene.builder().code("N090026").gene("PALB2").build(),
							Gene.builder().code("N090085").gene("PMS2").build()}).build(),
					Disease.builder().name("리 프라우메니 증후군").genes(new Gene[]{Gene.builder().code("N090030").gene("TP53").build()}).build(),
					Disease.builder().name("위암").genes(new Gene[]{Gene.builder().code("N090022").gene("CDH1").build()}).build(),
					Disease.builder().name("포이츠 제거스 증후군").genes(new Gene[]{Gene.builder().code("N090040").gene("STK11").build()}).build(),
					Disease.builder().name("린치 증후군").genes(new Gene[]{
							Gene.builder().code("N090050").gene("EPCAM").build(),
							Gene.builder().code("N090060").gene("MLH1").build(),
							Gene.builder().code("N090070").gene("MSH2").build(),
							Gene.builder().code("N090080").gene("MSH6").build(),
							Gene.builder().code("N090085").gene("PMS2").build()}).build(),
					Disease.builder().name("가족성 선종성 용종증").genes(new Gene[]{Gene.builder().code("N090090").gene("APC").build()}).build(),
					Disease.builder().name("MUTYH 연관 용종증").genes(new Gene[]{Gene.builder().code("N090100").gene("MUTYH").build()}).build(),
					Disease.builder().name("연소성 용종증 증후군").genes(new Gene[]{
							Gene.builder().code("N090110").gene("BMPR1A").build(),
							Gene.builder().code("N090120").gene("SMAD4").build()}).build(),
					Disease.builder().name("폰히펠 린다우 증후군").genes(new Gene[]{Gene.builder().code("N090130").gene("VHL").build()}).build(),
					Disease.builder().name("제1형 다발성 내분비선종증").genes(new Gene[]{Gene.builder().code("N090140").gene("MEN1").build()}).build(),
					Disease.builder().name("제2형 다발성 내분비선종증").genes(new Gene[]{Gene.builder().code("N090150").gene("RET").build()}).build(),
					Disease.builder().name("PTEN 과오종 증후군").genes(new Gene[]{Gene.builder().code("N090160").gene("PTEN").build()}).build(),
					Disease.builder().name("망막모세포종").genes(new Gene[]{Gene.builder().code("N090170").gene("RB1").build()}).build(),
					Disease.builder().name("유전성 부신경절종 갈색세포종").genes(new Gene[]{
							Gene.builder().code("N090180").gene("SDHD").build(),
							Gene.builder().code("N090190").gene("SDHAF2").build(),
							Gene.builder().code("N090200").gene("SDHC").build(),
							Gene.builder().code("N090210").gene("SDHB").build()}).build(),
					Disease.builder().name("결절성 경화증").genes(new Gene[]{
							Gene.builder().code("N090220").gene("TSC1").build(),
							Gene.builder().code("N090230").gene("TSC2").build()}).build(),
					Disease.builder().name("WT1 연관 윌름스 종양").genes(new Gene[]{Gene.builder().code("N090240").gene("WT1").build()}).build(),
					Disease.builder().name("제2형 신경섬유종증").genes(new Gene[]{Gene.builder().code("N090250").gene("NF2").build()}).build()
			}).build();
	public static final TestInfo N101 = TestInfo.builder()
			.code("N101")
			.summaryCode("N101040")
			.interpretationCode("N101050")
			.name("캔서 진 스크린(건협)")
			.i18n("KOKR")
			.diseases(new Disease[]{
					Disease.builder().name("유전성 유방암 난소암 증후군").genes(new Gene[]{
							Gene.builder().code("N101010").gene("BRCA1").build(),
							Gene.builder().code("N101020").gene("BRCA2").build()}).build(),
					Disease.builder().name("리 프라우메니 증후군").genes(new Gene[]{Gene.builder().code("N101030").gene("TP53").build()}).build()
			}).build();
	public static final TestInfo N111 = TestInfo.builder()
			.code("N111")
			.summaryCode("N111040")
			.interpretationCode("N111050")
			.name("캔서 진 스크린(건협 임직원)")
			.i18n("KOKR")
			.diseases(new Disease[]{
					Disease.builder().name("유전성 유방암 난소암 증후군").genes(new Gene[]{
							Gene.builder().code("N111010").gene("BRCA1").build(),
							Gene.builder().code("N111020").gene("BRCA2").build()}).build(),
					Disease.builder().name("리 프라우메니 증후군").genes(new Gene[]{Gene.builder().code("N111030").gene("TP53").build()}).build()
			}).build();
	public static final TestInfo N112 = TestInfo.builder()
			.code("N112")
			.summaryCode("N112005")
			.interpretationCode("N112010")
			.name("심장 돌연사 지놈 스크린(건협 임직원)")
			.i18n("KOKR")
			.diseases(new Disease[]{
					Disease.builder().name("엘러스-단로스 증후군").genes(new Gene[]{Gene.builder().code("N112010").gene("COL3A1").build()}).build(),
					Disease.builder().name("마르판 증후군").genes(new Gene[]{Gene.builder().code("N112020").gene("FBN1").build()}).build(),
					Disease.builder().name("로이-디에츠 증후군").genes(new Gene[]{
							Gene.builder().code("N112060").gene("TGFBR1").build(),
							Gene.builder().code("N112070").gene("TGFBR2").build(),
							Gene.builder().code("N112030").gene("SMAD3").build(),
							Gene.builder().code("N112040").gene("TGFB2").build(),
							Gene.builder().code("N112050").gene("TGFB3").build()}).build(),
					Disease.builder().name("가족성 흉부대동맥류와 박리증").genes(new Gene[]{
							Gene.builder().code("N112020").gene("FBN1").build(),
							Gene.builder().code("N112030").gene("SMAD3").build(),
							Gene.builder().code("N112060").gene("TGFBR1").build(),
							Gene.builder().code("N112070").gene("TGFBR2").build(),
							Gene.builder().code("N112072").gene("ACTA2").build(),
							Gene.builder().code("N112074").gene("MYH11").build(),
							Gene.builder().code("N112076").gene("MYLK").build(),}).build(),
					Disease.builder().name("비후성 심근병증").genes(new Gene[]{
							Gene.builder().code("N112125").gene("MYBPC3").build(),
							Gene.builder().code("N112130").gene("MYH7").build(),
							Gene.builder().code("N112140").gene("TNNT2").build(),
							Gene.builder().code("N112150").gene("TNNI3").build(),
							Gene.builder().code("N112160").gene("TPM1").build(),
							Gene.builder().code("N112170").gene("MYL3").build(),
							Gene.builder().code("N112200").gene("MYL2").build(),
							Gene.builder().code("N112120").gene("CSRP3").build(),
							Gene.builder().code("N112190").gene("PRKAG2").build()}).build(),
					Disease.builder().name("확장성 심근병증").genes(new Gene[]{
							Gene.builder().code("N112180").gene("ACTC1").build(),
							Gene.builder().code("N112130").gene("MYH7").build(),
							Gene.builder().code("N112210").gene("LMNA").build(),
							Gene.builder().code("N112202").gene("BAG3").build(),
							Gene.builder().code("N112204").gene("DES").build(),}).build(),
					Disease.builder().name("파브리병").genes(new Gene[]{Gene.builder().code("N112220").gene("GLA").build()}).build(),
					Disease.builder().name("부정맥 유발성 우심실 심근병증").genes(new Gene[]{
							Gene.builder().code("N112230").gene("PKP2").build(),
							Gene.builder().code("N112240").gene("DSP").build(),
							Gene.builder().code("N112250").gene("DSC2").build(),
							Gene.builder().code("N112260").gene("TMEM43").build(),
							Gene.builder().code("N112270").gene("DSG2").build()}).build(),
					Disease.builder().name("애머리 드라이푸스 증후군").genes(new Gene[]{
							Gene.builder().code("N112272").gene("EMD").build(),
							Gene.builder().code("N112274").gene("FHL1").build()}).build(),
					Disease.builder().name("카테콜아민성 다형성 심실성 빈맥").genes(new Gene[]{Gene.builder().code("N112030").gene("RYR2").build()}).build(),
					Disease.builder().name("심장 긴간격 증후군").genes(new Gene[]{
							Gene.builder().code("N112031").gene("KCNQ1").build(),
							Gene.builder().code("N112032").gene("KCNH2").build(),
							Gene.builder().code("N112033").gene("SCN5A").build()}).build(),
					Disease.builder().name("브루가다 증후군").genes(new Gene[]{Gene.builder().code("N112033").gene("SCN5A").build()}).build(),
					Disease.builder().name("가족성 고콜레스테롤혈증").genes(new Gene[]{
							Gene.builder().code("N112280").gene("LDLR").build(),
							Gene.builder().code("N112290").gene("APOB").build(),
							Gene.builder().code("N112300").gene("PCSK9").build()}).build(),
					Disease.builder().name("고호모시스테인혈전증").genes(new Gene[]{Gene.builder().code("N112310").gene("CBS").build()}).build(),
					Disease.builder().name("동맥비틀림증후군").genes(new Gene[]{Gene.builder().code("N112320").gene("SLC2A10").build()}).build()
			}).build();
	public static final TestInfo N185 = TestInfo.builder()
			.code("N185")
			.summaryCode("N185005")
			.interpretationCode("N185008")
			.name("심장 돌연사 지놈 스크린(건협)")
			.i18n("KOKR")
			.diseases(new Disease[]{
					Disease.builder().name("엘러스-단로스 증후군").genes(new Gene[]{Gene.builder().code("N185010").gene("COL3A1").build()}).build(),
					Disease.builder().name("마르판 증후군").genes(new Gene[]{Gene.builder().code("N185020").gene("FBN1").build()}).build(),
					Disease.builder().name("로이-디에츠 증후군").genes(new Gene[]{
							Gene.builder().code("N185060").gene("TGFBR1").build(),
							Gene.builder().code("N185070").gene("TGFBR2").build(),
							Gene.builder().code("N185030").gene("SMAD3").build(),
							Gene.builder().code("N185040").gene("TGFB2").build(),
							Gene.builder().code("N185050").gene("TGFB3").build()}).build(),
					Disease.builder().name("가족성 흉부대동맥류와 박리증").genes(new Gene[]{
							Gene.builder().code("N185020").gene("FBN1").build(),
							Gene.builder().code("N185030").gene("SMAD3").build(),
							Gene.builder().code("N185060").gene("TGFBR1").build(),
							Gene.builder().code("N185070").gene("TGFBR2").build(),
							Gene.builder().code("N185072").gene("ACTA2").build(),
							Gene.builder().code("N185074").gene("MYH11").build(),
							Gene.builder().code("N185076").gene("MYLK").build(),}).build(),
					Disease.builder().name("비후성 심근병증").genes(new Gene[]{
							Gene.builder().code("N185125").gene("MYBPC3").build(),
							Gene.builder().code("N185130").gene("MYH7").build(),
							Gene.builder().code("N185140").gene("TNNT2").build(),
							Gene.builder().code("N185150").gene("TNNI3").build(),
							Gene.builder().code("N185160").gene("TPM1").build(),
							Gene.builder().code("N185170").gene("MYL3").build(),
							Gene.builder().code("N185200").gene("MYL2").build(),
							Gene.builder().code("N185120").gene("CSRP3").build(),
							Gene.builder().code("N185190").gene("PRKAG2").build()}).build(),
					Disease.builder().name("확장성 심근병증").genes(new Gene[]{
							Gene.builder().code("N185180").gene("ACTC1").build(),
							Gene.builder().code("N185130").gene("MYH7").build(),
							Gene.builder().code("N185210").gene("LMNA").build(),
							Gene.builder().code("N185202").gene("BAG3").build(),
							Gene.builder().code("N185204").gene("DES").build(),}).build(),
					Disease.builder().name("파브리병").genes(new Gene[]{Gene.builder().code("N185220").gene("GLA").build()}).build(),
					Disease.builder().name("부정맥 유발성 우심실 심근병증").genes(new Gene[]{
							Gene.builder().code("N185230").gene("PKP2").build(),
							Gene.builder().code("N185240").gene("DSP").build(),
							Gene.builder().code("N185250").gene("DSC2").build(),
							Gene.builder().code("N185260").gene("TMEM43").build(),
							Gene.builder().code("N185270").gene("DSG2").build()}).build(),
					Disease.builder().name("애머리 드라이푸스 증후군").genes(new Gene[]{
							Gene.builder().code("N185272").gene("EMD").build(),
							Gene.builder().code("N185274").gene("FHL1").build()}).build(),
					Disease.builder().name("카테콜아민성 다형성 심실성 빈맥").genes(new Gene[]{Gene.builder().code("N185080").gene("RYR2").build()}).build(),
					Disease.builder().name("심장 긴간격 증후군").genes(new Gene[]{
							Gene.builder().code("N185090").gene("KCNQ1").build(),
							Gene.builder().code("N185100").gene("KCNH2").build(),
							Gene.builder().code("N185110").gene("SCN5A").build()}).build(),
					Disease.builder().name("브루가다 증후군").genes(new Gene[]{Gene.builder().code("N185110").gene("SCN5A").build()}).build(),
					Disease.builder().name("가족성 고콜레스테롤혈증").genes(new Gene[]{
							Gene.builder().code("N185280").gene("LDLR").build(),
							Gene.builder().code("N185290").gene("APOB").build(),
							Gene.builder().code("N185300").gene("PCSK9").build()}).build(),
					Disease.builder().name("고호모시스테인혈전증").genes(new Gene[]{Gene.builder().code("N185310").gene("CBS").build()}).build(),
					Disease.builder().name("동맥비틀림증후군").genes(new Gene[]{Gene.builder().code("N185320").gene("SLC2A10").build()}).build()
			}).build();
	public static final TestInfo ON089 = TestInfo.builder()
			.code("ON089")
			.summaryCode("ON089005")
			.interpretationCode("ON089010")
			.name("심장 돌연사 지놈 스크린 / 검진")
			.i18n("ENUS")
			.diseases(new Disease[]{
					Disease.builder().name("엘러스-단로스 증후군").genes(new Gene[]{Gene.builder().code("ON089020").gene("COL3A1").build()}).build(),
					Disease.builder().name("마르판 증후군").genes(new Gene[]{Gene.builder().code("ON089021").gene("FBN1").build()}).build(),
					Disease.builder().name("로이-디에츠 증후군").genes(new Gene[]{
							Gene.builder().code("ON089025").gene("TGFBR1").build(),
							Gene.builder().code("ON089026").gene("TGFBR2").build(),
							Gene.builder().code("ON089022").gene("SMAD3").build(),
							Gene.builder().code("ON089023").gene("TGFB2").build(),
							Gene.builder().code("ON089024").gene("TGFB3").build()}).build(),
					Disease.builder().name("가족성 흉부대동맥류와 박리증").genes(new Gene[]{
							Gene.builder().code("ON089021").gene("FBN1").build(),
							Gene.builder().code("ON089022").gene("SMAD3").build(),
							Gene.builder().code("ON089025").gene("TGFBR1").build(),
							Gene.builder().code("ON089026").gene("TGFBR2").build(),
							Gene.builder().code("ON089027").gene("ACTA2").build(),
							Gene.builder().code("ON089028").gene("MYH11").build(),
							Gene.builder().code("ON089029").gene("MYLK").build(),}).build(),
					Disease.builder().name("비후성 심근병증").genes(new Gene[]{
							Gene.builder().code("ON089035").gene("MYBPC3").build(),
							Gene.builder().code("ON089036").gene("MYH7").build(),
							Gene.builder().code("ON089037").gene("TNNT2").build(),
							Gene.builder().code("ON089038").gene("TNNI3").build(),
							Gene.builder().code("ON089039").gene("TPM1").build(),
							Gene.builder().code("ON089040").gene("MYL3").build(),
							Gene.builder().code("ON089043").gene("MYL2").build(),
							Gene.builder().code("ON089034").gene("CSRP3").build(),
							Gene.builder().code("ON089042").gene("PRKAG2").build()}).build(),
					Disease.builder().name("확장성 심근병증").genes(new Gene[]{
							Gene.builder().code("ON089041").gene("ACTC1").build(),
							Gene.builder().code("ON089036").gene("MYH7").build(),
							Gene.builder().code("ON089046").gene("LMNA").build(),
							Gene.builder().code("ON089044").gene("BAG3").build(),
							Gene.builder().code("ON089045").gene("DES").build(),}).build(),
					Disease.builder().name("파브리병").genes(new Gene[]{Gene.builder().code("ON089047").gene("GLA").build()}).build(),
					Disease.builder().name("부정맥 유발성 우심실 심근병증").genes(new Gene[]{
							Gene.builder().code("ON089048").gene("PKP2").build(),
							Gene.builder().code("ON089049").gene("DSP").build(),
							Gene.builder().code("ON089050").gene("DSC2").build(),
							Gene.builder().code("ON089051").gene("TMEM43").build(),
							Gene.builder().code("ON089052").gene("DSG2").build()}).build(),
					Disease.builder().name("애머리 드라이푸스 증후군").genes(new Gene[]{
							Gene.builder().code("ON089053").gene("EMD").build(),
							Gene.builder().code("ON089054").gene("FHL1").build()}).build(),
					Disease.builder().name("카테콜아민성 다형성 심실성 빈맥").genes(new Gene[]{Gene.builder().code("ON089030").gene("RYR2").build()}).build(),
					Disease.builder().name("심장 긴간격 증후군").genes(new Gene[]{
							Gene.builder().code("ON089031").gene("KCNQ1").build(),
							Gene.builder().code("ON089032").gene("KCNH2").build(),
							Gene.builder().code("ON089033").gene("SCN5A").build()}).build(),
					Disease.builder().name("브루가다 증후군").genes(new Gene[]{Gene.builder().code("ON089033").gene("SCN5A").build()}).build(),
					Disease.builder().name("가족성 고콜레스테롤혈증").genes(new Gene[]{
							Gene.builder().code("ON089055").gene("LDLR").build(),
							Gene.builder().code("ON089056").gene("APOB").build(),
							Gene.builder().code("ON089057").gene("PCSK9").build()}).build(),
					Disease.builder().name("고호모시스테인혈전증").genes(new Gene[]{Gene.builder().code("ON089058").gene("CBS").build()}).build(),
					Disease.builder().name("동맥비틀림증후군").genes(new Gene[]{Gene.builder().code("ON089059").gene("SLC2A10").build()}).build()})
			.build();
	public static final TestInfo ON090 = TestInfo.builder() // Cancer
			.code("ON090")
			.summaryCode("ON090005")
			.interpretationCode("ON090010")
			.i18n("ENUS")
			.name("암 지놈 스크린 / 검진")
			.diseases(new Disease[]{
					Disease.builder().name("유전성 유방암 난소암 증후군").genes(new Gene[]{
							Gene.builder().code("ON090020").gene("BRCA1").build(),
							Gene.builder().code("ON090021").gene("BRCA2").build()}).build(),
					Disease.builder().name("유방암 감수성").genes(new Gene[]{
							Gene.builder().code("ON090022").gene("ATM").build(),
							Gene.builder().code("ON090023").gene("CDH1").build(),
							Gene.builder().code("ON090024").gene("CHEK2").build(),
							Gene.builder().code("ON090025").gene("NBN").build(),
							Gene.builder().code("ON090026").gene("NF1").build(),
							Gene.builder().code("ON090027").gene("PALB2").build()}).build(),
					Disease.builder().name("난소암 감수성").genes(new Gene[]{
							Gene.builder().code("ON090028").gene("BRIP1").build(),
							Gene.builder().code("ON090029").gene("RAD51C").build(),
							Gene.builder().code("ON090030").gene("RAD51D").build()}).build(),
					Disease.builder().name("전립선암 감수성").genes(new Gene[]{
							Gene.builder().code("ON090022").gene("ATM").build(),
							Gene.builder().code("ON090024").gene("CHEK2").build(),
							Gene.builder().code("ON090034").gene("MLH1").build(),
							Gene.builder().code("ON090035").gene("MSH2").build(),
							Gene.builder().code("ON090036").gene("MSH6").build(),
							Gene.builder().code("ON090027").gene("PALB2").build(),
							Gene.builder().code("ON090037").gene("PMS2").build()}).build(),
					Disease.builder().name("리 프라우메니 증후군").genes(new Gene[]{Gene.builder().code("ON090031").gene("TP53").build()}).build(),
					Disease.builder().name("위암").genes(new Gene[]{Gene.builder().code("ON090022").gene("CDH1").build()}).build(),
					Disease.builder().name("포이츠 제거스 증후군").genes(new Gene[]{Gene.builder().code("ON090032").gene("STK11").build()}).build(),
					Disease.builder().name("린치 증후군").genes(new Gene[]{
							Gene.builder().code("ON090033").gene("EPCAM").build(),
							Gene.builder().code("ON090034").gene("MLH1").build(),
							Gene.builder().code("ON090035").gene("MSH2").build(),
							Gene.builder().code("ON090036").gene("MSH6").build(),
							Gene.builder().code("ON090037").gene("PMS2").build()}).build(),
					Disease.builder().name("가족성 선종성 용종증").genes(new Gene[]{Gene.builder().code("ON090038").gene("APC").build()}).build(),
					Disease.builder().name("MUTYH 연관 용종증").genes(new Gene[]{Gene.builder().code("ON090039").gene("MUTYH").build()}).build(),
					Disease.builder().name("연소성 용종증 증후군").genes(new Gene[]{
							Gene.builder().code("ON090040").gene("BMPR1A").build(),
							Gene.builder().code("ON090041").gene("SMAD4").build()}).build(),
					Disease.builder().name("폰히펠 린다우 증후군").genes(new Gene[]{Gene.builder().code("ON090042").gene("VHL").build()}).build(),
					Disease.builder().name("제1형 다발성 내분비선종증").genes(new Gene[]{Gene.builder().code("ON090043").gene("MEN1").build()}).build(),
					Disease.builder().name("제2형 다발성 내분비선종증").genes(new Gene[]{Gene.builder().code("ON090044").gene("RET").build()}).build(),
					Disease.builder().name("PTEN 과오종 증후군").genes(new Gene[]{Gene.builder().code("ON090045").gene("PTEN").build()}).build(),
					Disease.builder().name("망막모세포종").genes(new Gene[]{Gene.builder().code("ON090046").gene("RB1").build()}).build(),
					Disease.builder().name("유전성 부신경절종 갈색세포종").genes(new Gene[]{
							Gene.builder().code("ON090047").gene("SDHD").build(),
							Gene.builder().code("ON090048").gene("SDHAF2").build(),
							Gene.builder().code("ON090049").gene("SDHC").build(),
							Gene.builder().code("ON090050").gene("SDHB").build()}).build(),
					Disease.builder().name("결절성 경화증").genes(new Gene[]{
							Gene.builder().code("ON090051").gene("TSC1").build(),
							Gene.builder().code("ON090052").gene("TSC2").build()}).build(),
					Disease.builder().name("WT1 연관 윌름스 종양").genes(new Gene[]{Gene.builder().code("ON090053").gene("WT1").build()}).build(),
					Disease.builder().name("제2형 신경섬유종증").genes(new Gene[]{Gene.builder().code("ON090054").gene("NF2").build()}).build()
			}).build();
	public static final TestInfo J018 = N090.toBuilder()
			.code("J018")
			.name("암 지놈 스크린 / 검진 (아이메드)")
			.build();
	public static final TestInfo J019 = N089.toBuilder()
			.code("J019")
			.name("심장 돌연사 지놈 스크린 / 검진 (아이메드)")
			.build();
	public static final TestInfo[] TESTS_BRCA = new TestInfo[] {
			N101, N111
	};
	public static final TestInfo[] TESTS_CANCER = new TestInfo[] {
			N074, N090, J018, ON090
	};
	public static final TestInfo[] TESTS_RD = new TestInfo[] {
			N075, TestWithRiskScreen.N087, TestWithRiskScreen.J020, TestWithRiskScreen.N088, TestWithRiskScreen.J021, N089, J019, N112, N185,
			TestWithRiskScreen.ON087, TestWithRiskScreen.ON088, ON089
	};
	public static final TestInfo[] TESTS = Stream.concat(Stream.concat(Arrays.stream(TestInfo.TESTS_CANCER),
					Arrays.stream(TestInfo.TESTS_BRCA)),
			Arrays.stream(TestInfo.TESTS_RD)).toArray(TestInfo[]::new);
	public interface RiskScreen extends HasCode, HasGenes, MayBeNationalInsurance {
		Genotype[] genotypes();
	}
	@Data
	@Accessors(fluent = true)
	@Builder
	public static class Genotype {
		private String gene;
		private String pos;
		private String code;
		private String[] types;
	}
}
