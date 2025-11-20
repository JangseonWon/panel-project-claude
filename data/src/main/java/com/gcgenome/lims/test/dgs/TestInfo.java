package com.gcgenome.lims.test.dgs;

import com.gcgenome.lims.test.HasCode;
import com.gcgenome.lims.test.MayBeNationalInsurance;
import com.gcgenome.lims.test.I18N;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
@Builder
public class TestInfo implements HasCode, MayBeNationalInsurance, I18N {
	private final String code;
	private final String title;
	private final String panel;
	@Builder.Default
	private final String[] methods = METHOD_KOKR;
	private final static String[] METHOD_KOKR = new String[] {
			"검사 대상자의 EDTA 전혈에서 DNA를 추출한 후 일루미나의 NovaSeq6000을 이용하여 paired-end reads 방법으로 염기서열 분석을 시행하였습니다. GRCh37/UCSC hg19 표준염기서열과 비교하여 질환 관련 변이를 확인하였습니다. 자체 설정한 생물학적 분석 기준에 따라, 서열 변이를 확인하기 위해 정보가 분류 및 분석되었습니다. CNV calling은 parliament2 파이프라인을 기반으로 합니다.",
			"변이 검출 및 해석은 검사에 포함된 유전자들의 coding exon과 인접 intron 영역을 주로 분석하나, 특정 질환 후보 유전자의 경우나 상염색체 열성 유전 질환에서 하나의 Pathogenic variant가 발견된 경우 두번째 변이를 찾기 위해 전체 유전자 영역으로 확장될 수 있습니다. 검사에서 발견된 변이는 ACMG/AMP guidelines(Richards et al, 2015)에 따라 분류됩니다. 환자의 임상 증상과 관련된 Phathogenic variant 및 Likely pathogenic variant에 대해서 주로 보고하고 있으며, 의미가 불분명한 번이(VUS)에 대해서는 질환 관련성이 낮다고 판단될 경우 판독자 재량에 따라 보고되지 않을 수도 있습니다. 내부 QC기준에 따라 Sanger sequencing을 생략할 수 있습니다."
	};
	private final static String[] METHOD_ENUS = new String[]{
			"Genomic DNA was extracted from EDTA whole blood and sequenced with paired-end reads on Illumina NovaSeq 6000 system. The DNA sequence reads were aligned to reference sequence based on public human genome build GRCh37/UCSC hg19. Using a in-house bioinformatics pipeline, data were filtered and analysed to identify sequence variants. CNV calling is based on parliament2 pipeline.",
			"Evaluation is focused on coding exons along with flanking +/-20 intronic bases, however extended to the complete gene region for candidate genes or in search for a second previously described variant in autosomal recessive inheritance pattern. Sequence variants were classified based on the ACMG/AMP guidelines (Richards et al., 2015). Reported results are focused on pathogenic and likely pathogenic variants in genes related to the phenotype of proband, while variants of uncertain significance are only rarely reported at our discretion. Variants that pass internal QC criteria are not validated by Sanger sequencing."
	};
	@Builder.Default
	private final boolean isNationalInsuranceTest = false;
	@Builder.Default
	private final String i18n = "KOKR";
	@Builder.Default
	private final String limitation = LIMITATION_KOKR;
	private final static String LIMITATION_KOKR = "본 검사로 검출에 제한이 있는 유전적 이상의 존재 가능성을 배제할 수 없으므로, 환자에서 질환 관련 변이가 검출되지 않았더라도 유전질환 가능성을 배제할 수 없습니다. 또한 현재의 지식으로는 인식이 불가능한 유전자 변이가 존재할 가능성도 있으며 본 검사는 현재까지 연구용 검사입니다. 환자의 질환 관련 변이의 genome 영역이 capture 되지 않거나 낮은 품질로 인해 충분히 염기서열분석이 되지 않을 수도 있습니다. 다인자성 질환과 반복염기서열의 증가, 비정상적 DNA 메틸화 및 기타 기전에 의한 유전 질환은 본 검사에서 검출되지 않을 수 있습니다. 또한 본 검사는 모자이시즘, 염색체 구조적 이상 및 20bp 이상의 삽입 및 결손은 정확한 검출이 어려울 수 있습니다. 일부 유전자들은 최적의 데이터 결과를 얻기 어려운 염기서열 특징(예: 반복서열, 상동성, 높은 GC함량 등)을 가지고 있으며 해당 영역의 변이는 안정적으로 검출되지 못할 수 있습니다.";
	private final static String LIMITATION_ENUS = "The absence of definitive pathogenic findings does not rule out the diagnosis of a genetic disorder as some genetic abnormalities may be undetectable with this test. It is possible that the genomic region where a disease-causing variant exists in the proband was not captured or sufficiently sequenced with low quality. Additionally, multifactorial disorders and some types of genetic disorders due to nucleotide repeat expansion/contraction, abnormal DNA methylation, and other mechanisms may not be detectable with this test. This test also cannot reliably detect mosaicism, chromosomal aberrations, and deletions/insertions of 20 bp or more. Some genes have inherent sequence properties (for example: repeats, homology, high GC content, rare polymorphisms) that may result in suboptimal data, and variants in those regions may not be reliably identified.";
	public static final TestInfo N127 = TestInfo.builder()
												.code("N127")
												.title("Diagnostic Genome Sequencing Test 결과보고서")
												.panel("DGS (Diagnostic Genome Sequencing)")
												.isNationalInsuranceTest(false).build();
	public static final TestInfo N137 = TestInfo.builder()
												.code("N137")
												.title("DGS Trio(Proband) 결과보고서")
												.panel("DGS (Diagnostic Genome Sequencing)")
												.isNationalInsuranceTest(false).build();
	public static final TestInfo N138 = TestInfo.builder()
												.code("N138")
												.title("DGS Trio(부) 결과보고서")
												.panel("DGS (Diagnostic Genome Sequencing)")
												.isNationalInsuranceTest(false).build();
	public static final TestInfo N139 = TestInfo.builder()
												.code("N139")
												.title("DGS Trio(모) 결과보고서")
												.panel("DGS (Diagnostic Genome Sequencing)")
												.isNationalInsuranceTest(false).build();
	public static final TestInfo ON127 = TestInfo.builder()
												.code("ON127")
												.title("Diagnostic Genome Sequencing Test Report")
												.methods(METHOD_ENUS)
												.panel("DGS (Diagnostic Genome Sequencing)")
												.limitation(LIMITATION_ENUS)
												.isNationalInsuranceTest(false).i18n("ENUS").build();
	public static final TestInfo ON137 = TestInfo.builder()
												.code("ON137")
												.title("DGS Trio(Proband) Test Report")
												.methods(METHOD_ENUS)
												.panel("DGS (Diagnostic Genome Sequencing)")
												.limitation(LIMITATION_ENUS)
												.isNationalInsuranceTest(false).i18n("ENUS").build();
	public static final TestInfo ON138 = TestInfo.builder()
												 .code("ON138")
												 .title("DGS Trio(Father) Test Report")
												 .methods(METHOD_ENUS)
												 .panel("DGS (Diagnostic Genome Sequencing)")
												 .limitation(LIMITATION_ENUS)
												 .isNationalInsuranceTest(false).i18n("ENUS").build();
	public static final TestInfo ON139 = TestInfo.builder()
												 .code("ON139")
												 .title("DGS Trio(Mother) Test Report")
												 .methods(METHOD_ENUS)
												 .panel("DGS (Diagnostic Genome Sequencing)")
												 .limitation(LIMITATION_ENUS)
												 .isNationalInsuranceTest(false).i18n("ENUS").build();
	public static final TestInfo[] TESTS = new TestInfo[] {
			N127,
			N137, N138, N139,
			ON127,
			ON137, ON138, ON139
	};
}
