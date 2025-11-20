package com.gcgenome.lims.test.wes;

import com.gcgenome.lims.test.HasCode;
import com.gcgenome.lims.test.MayBeNationalInsurance;
import com.gcgenome.lims.test.I18N;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public class TestInfo implements HasCode, MayBeNationalInsurance, I18N {
	private final String code;
	private final String title;
	private final String panel;
	private final String[] methods;
	private final boolean isNationalInsuranceTest;
	private final String i18n;
	private final String limitation;
	@Builder
	protected TestInfo(String code, String title, String panel, String[] methods, Boolean isNationalInsuranceTest, String i18n, String limitation) {
		this.code = code;
		this.title = title;
		if(panel!=null) this.panel = panel;
		else this.panel = "WES(Sequence analysis of whole exome of human genes)";
		if(methods!=null) this.methods = methods;
		else this.methods = new String[]{
				"검사 대상자의 말초혈액 백혈구 DNA를 추출한 후 xGen™ Exome Hybridization Panel 을 이용하여 전체 exon을 capture하여 Novaseq 6000DX 장비의 2 x 150 paired end reads 방법으로 염기서열분석을 시행하였습니다. GRCh37/hg19 표준염기서열과 비교하여 질환 관련 변이를 확인하였으며, 자체 설정한 생물정보학적 분석 기준에 따라 Sanger sequencing을 생략할 수 있습니다.",
				"검사에서 발견된 변이는 2015 ACMG/AMP guidelines (Richards et al., 2015)에 따라 \"pathogenic\", \"likely pathogenic\", \"uncertain significance\", \"likely benign\", \"benign\" 의 다섯가지 카테고리로 분류되며, 환자의 임상 증상과 관련된 Pathogenic variant 및 Likely pathogenic variant에 대해서 주로 보고하고 있으며, 의미가 불분명한 변이(VUS)에 대해서는 질환 관련성이 낮다고 판단될 경우 판독자 재량에 따라 보고되지 않을 수도 있습니다. 추후 관련 문헌과 데이터베이스의 추가적인 연구 결과에 따라 해당 변이의 분류가 변경될 수 있습니다."
		};
		if(isNationalInsuranceTest!=null) this.isNationalInsuranceTest = isNationalInsuranceTest;
		else this.isNationalInsuranceTest = false;
		if(i18n!=null) this.i18n = i18n;
		else this.i18n = "KOKR";
		if(limitation!=null) this.limitation = limitation;
		else this.limitation = "본 검사로 검출에 제한이 있는 유전적 이상의 존재 가능성을 배제할 수 없으므로, 환자에서 질환 관련 변이가 검출되지 않았더라도 유전 질환 가능성을 배제할 수 없습니다. 또한 현재의 지식으로는 인식이 불가능한 유전자 변이가 존재할 가능성도 있으며 본 검사는 현재까지 연구용 검사입니다. 환자의 질환 관련 변이의 genome 영역이 capture 되지 않거나 낮은 품질로 인해 충분히 염기서열분석이 되지 않을 수 있습니다. 다인자성 질환과 반복염기서열의 증가, 비정상적 DNA 메틸화 및 기타 기전에 의한 유전 질환은 본 검사에서 확인이 어렵습니다. 또한 본 검사는 모자이시즘, 염색체 구조적 이상 및 20bp 이상의 삽입 및 결손은 정확한 검출이 어렵습니다. 일부 유전자들은 최적의 데이터 결과를 얻기 어려운 염기서열 특징(예: 반복서열, 상동성, 높은 GC 함량 등)을 가지고 있으며 해당 영역의 변이는 안정적으로 검출되지 못할 수 있습니다.";
	}
	public static final TestInfo T001 = TestInfo.builder()
			.code("T001")
			.title("Whole Exome Sequencing Test 결과보고서")
			.methods(new String[]{
					"검사 대상자의 말초혈액 백혈구 DNA를 추출한 후 MGIEasy Exome Capture V5 (MGI)을 이용하여 전체 exon을 capture하여 DNBSEQ- G400 (MGI) 또는 DNBSEQ-T7 (MGI) 장비의 2 x 100 paired end reads 방법으로 염기서열분석을 시행하였습니다. GRCh37/hg19 표준염기서열과 비교하여 질환 관련 변이를 확인하였으며, 자체 설정한 생물정보학적 분석 기준에 따라 Sanger sequencing을 생략할 수 있습니다.",
					"검사에서 발견된 변이는 2015 ACMG/AMP guidelines (Richards et al., 2015)에 따라 \"pathogenic\", \"likely pathogenic\", \"uncertain significance\", \"likely benign\", \"benign\" 의 다섯가지 카테고리로 분류되며, 환자의 임상 증상과 관련된 Pathogenic variant 및 Likely pathogenic variant에 대해서 주로 보고하고 있으며, 의미가 불분명한 변이(VUS)에 대해서는 질환 관련성이 낮다고 판단될 경우 판독자 재량에 따라 보고되지 않을 수도 있습니다. 추후 관련 문헌과 데이터베이스의 추가적인 연구 결과에 따라 해당 변이의 분류가 변경될 수 있습니다."
			}).build();
	public static final TestInfo T016 = TestInfo.builder()
												.code("T016")
												.title("Whole Exome Sequencing (WES) Report")
												.build();
	public static final TestInfo T023 = TestInfo.builder()
												.code("T023")
												.title("Whole Exome Sequencing Test Report")
												.methods(new String[] {
														"Genomic DNA was extracted from EDTA whole blood and we captured all the exons of human genes using MGIEasy Exome Capture V5 (MGI). Sequencing was " +
																"performed on DNBSEQ-G400 (MGI) or DNBSEQ-T7 (MGI) platform generating 2 × 100 bp paired-end reads. The DNA sequence reads were aligned to reference " +
																"sequence based on public human genome build GRCh37/UCSC hg19. Using a in-house bioinformatics pipeline, data were filtered and analysed to identify " +
																"sequence variants.",
														"Sequence variants were classified based on the ACMG/AMP guidelines (Richards et al., 2015). Reported results are focused on pathogenic and likely " +
																"pathogenic variants in genes related to the phenotype of proband, while variants of uncertain significance are only rarely reported at our discretion. " +
																"Depending on the results of additional studies in the literature and databases, the classification of the variant may change. Variants that pass " +
																"internal QC criteria are not validated by Sanger sequencing."})
												.limitation(
														"The absence of definitive pathogenic findings does not rule out the diagnosis of a genetic disorder as some genetic abnormalities may be undetectable " +
																"with this test. It is possible that the genomic region where a disease-causing variant exists in the proband was not captured or sufficiently " +
																"sequenced with low quality. Additionally, multifactorial disorders and some types of genetic disorders due to nucleotide repeat expansion/contraction, " +
																"abnormal DNA methylation, and other mechanisms may not be detectable with this test. This test also cannot reliably detect mosaicism, chromosomal " +
																"aberrations, and deletions/insertions of 20 bp or more. Some genes have inherent sequence properties (for example: repeats, homology, high GC content, " +
																"rare polymorphisms) that may result in suboptimal data, and variants in those regions may not be reliably identified.")
												.i18n("ENUS").build();
	public static final TestInfo R2300101 = TestInfo.builder()				// '23/02/16 기준 T001과 동일
			.code("R2300101")
			.title("Whole Exome Sequencing Test 결과보고서")
			.build();
	public static final TestInfo N134 = TestInfo.builder()
			.code("N134")
			.title("WES Trio(Proband) 결과보고서")
			.methods(new String[]{
					"검사 대상자의 말초혈액 백혈구 DNA를 추출한 후 MGIEasy Exome Capture V5 (MGI)을 이용하여 전체 exon을 capture하여 DNBSEQ- G400 (MGI) 또는 DNBSEQ-T7 (MGI) 장비의 2 x 100 paired end reads 방법으로 염기서열분석을 시행하였습니다. GRCh37/hg19 표준염기서열과 비교하여 질환 관련 변이를 확인하였으며, 자체 설정한 생물정보학적 분석 기준에 따라 Sanger sequencing을 생략할 수 있습니다.",
					"검사에서 발견된 변이는 2015 ACMG/AMP guidelines (Richards et al., 2015)에 따라 \"pathogenic\", \"likely pathogenic\", \"uncertain significance\", \"likely benign\", \"benign\" 의 다섯가지 카테고리로 분류되며, 환자의 임상 증상과 관련된 Pathogenic variant 및 Likely pathogenic variant에 대해서 주로 보고하고 있으며, 의미가 불분명한 변이(VUS)에 대해서는 질환 관련성이 낮다고 판단될 경우 판독자 재량에 따라 보고되지 않을 수도 있습니다. 추후 관련 문헌과 데이터베이스의 추가적인 연구 결과에 따라 해당 변이의 분류가 변경될 수 있습니다."
			})
			.build();
	public static final TestInfo N135 = TestInfo.builder()
			.code("N135")
			.title("WES Trio(부) 결과보고서")
			.methods(new String[]{
					"검사 대상자의 말초혈액 백혈구 DNA를 추출한 후 MGIEasy Exome Capture V5 (MGI)을 이용하여 전체 exon을 capture하여 DNBSEQ- G400 (MGI) 또는 DNBSEQ-T7 (MGI) 장비의 2 x 100 paired end reads 방법으로 염기서열분석을 시행하였습니다. GRCh37/hg19 표준염기서열과 비교하여 질환 관련 변이를 확인하였으며, 자체 설정한 생물정보학적 분석 기준에 따라 Sanger sequencing을 생략할 수 있습니다.",
					"검사에서 발견된 변이는 2015 ACMG/AMP guidelines (Richards et al., 2015)에 따라 \"pathogenic\", \"likely pathogenic\", \"uncertain significance\", \"likely benign\", \"benign\" 의 다섯가지 카테고리로 분류되며, 환자의 임상 증상과 관련된 Pathogenic variant 및 Likely pathogenic variant에 대해서 주로 보고하고 있으며, 의미가 불분명한 변이(VUS)에 대해서는 질환 관련성이 낮다고 판단될 경우 판독자 재량에 따라 보고되지 않을 수도 있습니다. 추후 관련 문헌과 데이터베이스의 추가적인 연구 결과에 따라 해당 변이의 분류가 변경될 수 있습니다."
			})
			.build();
	public static final TestInfo N136 = TestInfo.builder()
			.code("N136")
			.title("WES Trio(모) 결과보고서")
			.methods(new String[]{
					"검사 대상자의 말초혈액 백혈구 DNA를 추출한 후 MGIEasy Exome Capture V5 (MGI)을 이용하여 전체 exon을 capture하여 DNBSEQ- G400 (MGI) 또는 DNBSEQ-T7 (MGI) 장비의 2 x 100 paired end reads 방법으로 염기서열분석을 시행하였습니다. GRCh37/hg19 표준염기서열과 비교하여 질환 관련 변이를 확인하였으며, 자체 설정한 생물정보학적 분석 기준에 따라 Sanger sequencing을 생략할 수 있습니다.",
					"검사에서 발견된 변이는 2015 ACMG/AMP guidelines (Richards et al., 2015)에 따라 \"pathogenic\", \"likely pathogenic\", \"uncertain significance\", \"likely benign\", \"benign\" 의 다섯가지 카테고리로 분류되며, 환자의 임상 증상과 관련된 Pathogenic variant 및 Likely pathogenic variant에 대해서 주로 보고하고 있으며, 의미가 불분명한 변이(VUS)에 대해서는 질환 관련성이 낮다고 판단될 경우 판독자 재량에 따라 보고되지 않을 수도 있습니다. 추후 관련 문헌과 데이터베이스의 추가적인 연구 결과에 따라 해당 변이의 분류가 변경될 수 있습니다."
			})
			.build();
	public static final TestInfo N208 = TestInfo.builder()					// T023
			.code("N208")
			.title("Whole Exome Sequencing Test 결과보고서")
			.build();
	public static final TestInfo N209 = TestInfo.builder()					// T001
			.code("N209")
			.title("WES_Trio(Proband) 결과보고서")
			.build();
	public static final TestInfo N210 = TestInfo.builder()					// N134
			.code("N210")
			.title("WES_Trio(부) 결과보고서")
			.build();
	public static final TestInfo N211 = TestInfo.builder()					// N135
			.code("N211")
			.title("WES_Trio(모) 결과보고서")
			.build();
	public static final TestInfo OT001 = TestInfo.builder()
			.code("OT001")
			.title("Whole Exome Sequencing Test Report")
			.methods(new String[] {
					"Genomic DNA was extracted from EDTA whole blood and we captured all the exons of human genes using MGIEasy Exome Capture V5 (MGI). Sequencing was " +
							"performed on DNBSEQ-G400 (MGI) or DNBSEQ-T7 (MGI) platform generating 2 × 100 bp paired-end reads. The DNA sequence reads were aligned to reference " +
							"sequence based on public human genome build GRCh37/UCSC hg19. Using a in-house bioinformatics pipeline, data were filtered and analysed to identify " +
							"sequence variants.",
					"Sequence variants were classified based on the ACMG/AMP guidelines (Richards et al., 2015). Reported results are focused on pathogenic and likely " +
							"pathogenic variants in genes related to the phenotype of proband, while variants of uncertain significance are only rarely reported at our discretion. " +
							"Depending on the results of additional studies in the literature and databases, the classification of the variant may change. Variants that pass " +
							"internal QC criteria are not validated by Sanger sequencing."})
			.limitation(
					"The absence of definitive pathogenic findings does not rule out the diagnosis of a genetic disorder as some genetic abnormalities may be undetectable " +
							"with this test. It is possible that the genomic region where a disease-causing variant exists in the proband was not captured or sufficiently " +
							"sequenced with low quality. Additionally, multifactorial disorders and some types of genetic disorders due to nucleotide repeat expansion/contraction, " +
							"abnormal DNA methylation, and other mechanisms may not be detectable with this test. This test also cannot reliably detect mosaicism, chromosomal " +
							"aberrations, and deletions/insertions of 20 bp or more. Some genes have inherent sequence properties (for example: repeats, homology, high GC content, " +
							"rare polymorphisms) that may result in suboptimal data, and variants in those regions may not be reliably identified.")
			.i18n("ENUS").build();
	public static final TestInfo ON134 = TestInfo.builder()
			.code("ON134")
			.title("WES Trio(Proband) Test Report")
			.methods(new String[] {
					"Genomic DNA was extracted from EDTA whole blood and we captured all the exons of human genes using MGIEasy Exome Capture V5 (MGI). Sequencing was " +
							"performed on DNBSEQ-G400 (MGI) or DNBSEQ-T7 (MGI) platform generating 2 × 100 bp paired-end reads. The DNA sequence reads were aligned to reference " +
							"sequence based on public human genome build GRCh37/UCSC hg19. Using a in-house bioinformatics pipeline, data were filtered and analysed to identify " +
							"sequence variants.",
					"Sequence variants were classified based on the ACMG/AMP guidelines (Richards et al., 2015). Reported results are focused on pathogenic and likely " +
							"pathogenic variants in genes related to the phenotype of proband, while variants of uncertain significance are only rarely reported at our discretion. " +
							"Depending on the results of additional studies in the literature and databases, the classification of the variant may change. Variants that pass " +
							"internal QC criteria are not validated by Sanger sequencing."})
			.limitation(
					"The absence of definitive pathogenic findings does not rule out the diagnosis of a genetic disorder as some genetic abnormalities may be undetectable " +
							"with this test. It is possible that the genomic region where a disease-causing variant exists in the proband was not captured or sufficiently " +
							"sequenced with low quality. Additionally, multifactorial disorders and some types of genetic disorders due to nucleotide repeat expansion/contraction, " +
							"abnormal DNA methylation, and other mechanisms may not be detectable with this test. This test also cannot reliably detect mosaicism, chromosomal " +
							"aberrations, and deletions/insertions of 20 bp or more. Some genes have inherent sequence properties (for example: repeats, homology, high GC content, " +
							"rare polymorphisms) that may result in suboptimal data, and variants in those regions may not be reliably identified.")
			.i18n("ENUS").build();
	public static final TestInfo ON135 = TestInfo.builder()
			.code("ON135")
			.title("WES Trio(Father) Test Report")
			.methods(new String[] {
					"Genomic DNA was extracted from EDTA whole blood and we captured all the exons of human genes using MGIEasy Exome Capture V5 (MGI). Sequencing was " +
							"performed on DNBSEQ-G400 (MGI) or DNBSEQ-T7 (MGI) platform generating 2 × 100 bp paired-end reads. The DNA sequence reads were aligned to reference " +
							"sequence based on public human genome build GRCh37/UCSC hg19. Using a in-house bioinformatics pipeline, data were filtered and analysed to identify " +
							"sequence variants.",
					"Sequence variants were classified based on the ACMG/AMP guidelines (Richards et al., 2015). Reported results are focused on pathogenic and likely " +
							"pathogenic variants in genes related to the phenotype of proband, while variants of uncertain significance are only rarely reported at our discretion. " +
							"Depending on the results of additional studies in the literature and databases, the classification of the variant may change. Variants that pass " +
							"internal QC criteria are not validated by Sanger sequencing."})
			.limitation(
					"The absence of definitive pathogenic findings does not rule out the diagnosis of a genetic disorder as some genetic abnormalities may be undetectable " +
							"with this test. It is possible that the genomic region where a disease-causing variant exists in the proband was not captured or sufficiently " +
							"sequenced with low quality. Additionally, multifactorial disorders and some types of genetic disorders due to nucleotide repeat expansion/contraction, " +
							"abnormal DNA methylation, and other mechanisms may not be detectable with this test. This test also cannot reliably detect mosaicism, chromosomal " +
							"aberrations, and deletions/insertions of 20 bp or more. Some genes have inherent sequence properties (for example: repeats, homology, high GC content, " +
							"rare polymorphisms) that may result in suboptimal data, and variants in those regions may not be reliably identified.")
			.i18n("ENUS").build();
	public static final TestInfo ON136 = TestInfo.builder()
			.code("ON136")
			.title("WES Trio(Mother) Test Report")
			.methods(new String[] {
					"Genomic DNA was extracted from EDTA whole blood and we captured all the exons of human genes using MGIEasy Exome Capture V5 (MGI). Sequencing was " +
							"performed on DNBSEQ-G400 (MGI) or DNBSEQ-T7 (MGI) platform generating 2 × 100 bp paired-end reads. The DNA sequence reads were aligned to reference " +
							"sequence based on public human genome build GRCh37/UCSC hg19. Using a in-house bioinformatics pipeline, data were filtered and analysed to identify " +
							"sequence variants.",
					"Sequence variants were classified based on the ACMG/AMP guidelines (Richards et al., 2015). Reported results are focused on pathogenic and likely " +
							"pathogenic variants in genes related to the phenotype of proband, while variants of uncertain significance are only rarely reported at our discretion. " +
							"Depending on the results of additional studies in the literature and databases, the classification of the variant may change. Variants that pass " +
							"internal QC criteria are not validated by Sanger sequencing."})
			.limitation(
					"The absence of definitive pathogenic findings does not rule out the diagnosis of a genetic disorder as some genetic abnormalities may be undetectable " +
							"with this test. It is possible that the genomic region where a disease-causing variant exists in the proband was not captured or sufficiently " +
							"sequenced with low quality. Additionally, multifactorial disorders and some types of genetic disorders due to nucleotide repeat expansion/contraction, " +
							"abnormal DNA methylation, and other mechanisms may not be detectable with this test. This test also cannot reliably detect mosaicism, chromosomal " +
							"aberrations, and deletions/insertions of 20 bp or more. Some genes have inherent sequence properties (for example: repeats, homology, high GC content, " +
							"rare polymorphisms) that may result in suboptimal data, and variants in those regions may not be reliably identified.")
			.i18n("ENUS").build();
	public static final TestInfo ON201 = TestInfo.builder()	// OT001
			.code("ON201")
			.title("Whole Exome Sequencing Test Report")
			.methods(new String[] {
					"Genomic DNA was extracted from EDTA whole blood and we captured all the exons of human genes using xGen™ Exome Hybridization Panel. Sequencing was performed on  Novaseq 6000DX platform generating 2 x 150 paired end reads. The DNA sequence reads were aligned to reference sequence based on public human genome build GRCh37/UCSC hg19. Using a in-house bioinformatics pipeline, data were filtered and analysed to identify sequence variants.",
					"Sequence variants were classified based on the ACMG/AMP guidelines (Richards et al., 2015). Reported results are focused on pathogenic and likely pathogenic variants in genes related to the phenotype of proband, while variants of uncertain significance are only rarely reported at our discretion. Depending on the results of additional studies in the literature and databases, the classification of the variant may change. Variants that pass internal QC criteria are not validated by Sanger sequencing."
			}).limitation(
					"The absence of definitive pathogenic findings does not rule out the diagnosis of a genetic disorder as some genetic abnormalities may be undetectable " +
							"with this test. It is possible that the genomic region where a disease-causing variant exists in the proband was not captured or sufficiently " +
							"sequenced with low quality. Additionally, multifactorial disorders and some types of genetic disorders due to nucleotide repeat expansion/contraction, " +
							"abnormal DNA methylation, and other mechanisms may not be detectable with this test. This test also cannot reliably detect mosaicism, chromosomal " +
							"aberrations, and deletions/insertions of 20 bp or more. Some genes have inherent sequence properties (for example: repeats, homology, high GC content, " +
							"rare polymorphisms) that may result in suboptimal data, and variants in those regions may not be reliably identified.")
			.i18n("ENUS").build();
	public static final TestInfo ON209 = TestInfo.builder()  	// ON134
			.code("ON209")
			.title("WES_Trio(Proband) Test Report")
			.methods(new String[] {
					"Genomic DNA was extracted from EDTA whole blood and we captured all the exons of human genes using xGen™ Exome Hybridization Panel. Sequencing was performed on  Novaseq 6000DX platform generating 2 x 150 paired end reads. The DNA sequence reads were aligned to reference sequence based on public human genome build GRCh37/UCSC hg19. Using a in-house bioinformatics pipeline, data were filtered and analysed to identify sequence variants.",
					"Sequence variants were classified based on the ACMG/AMP guidelines (Richards et al., 2015). Reported results are focused on pathogenic and likely pathogenic variants in genes related to the phenotype of proband, while variants of uncertain significance are only rarely reported at our discretion. Depending on the results of additional studies in the literature and databases, the classification of the variant may change. Variants that pass internal QC criteria are not validated by Sanger sequencing."
			}).limitation(
					"The absence of definitive pathogenic findings does not rule out the diagnosis of a genetic disorder as some genetic abnormalities may be undetectable " +
							"with this test. It is possible that the genomic region where a disease-causing variant exists in the proband was not captured or sufficiently " +
							"sequenced with low quality. Additionally, multifactorial disorders and some types of genetic disorders due to nucleotide repeat expansion/contraction, " +
							"abnormal DNA methylation, and other mechanisms may not be detectable with this test. This test also cannot reliably detect mosaicism, chromosomal " +
							"aberrations, and deletions/insertions of 20 bp or more. Some genes have inherent sequence properties (for example: repeats, homology, high GC content, " +
							"rare polymorphisms) that may result in suboptimal data, and variants in those regions may not be reliably identified.")
			.i18n("ENUS").build();
	public static final TestInfo ON210 = TestInfo.builder()
			.code("ON210")
			.title("WES_Trio(Father) Test Report")
			.methods(new String[] {
					"Genomic DNA was extracted from EDTA whole blood and we captured all the exons of human genes using xGen™ Exome Hybridization Panel. Sequencing was performed on  Novaseq 6000DX platform generating 2 x 150 paired end reads. The DNA sequence reads were aligned to reference sequence based on public human genome build GRCh37/UCSC hg19. Using a in-house bioinformatics pipeline, data were filtered and analysed to identify sequence variants.",
					"Sequence variants were classified based on the ACMG/AMP guidelines (Richards et al., 2015). Reported results are focused on pathogenic and likely pathogenic variants in genes related to the phenotype of proband, while variants of uncertain significance are only rarely reported at our discretion. Depending on the results of additional studies in the literature and databases, the classification of the variant may change. Variants that pass internal QC criteria are not validated by Sanger sequencing."
			}).limitation(
					"The absence of definitive pathogenic findings does not rule out the diagnosis of a genetic disorder as some genetic abnormalities may be undetectable " +
							"with this test. It is possible that the genomic region where a disease-causing variant exists in the proband was not captured or sufficiently " +
							"sequenced with low quality. Additionally, multifactorial disorders and some types of genetic disorders due to nucleotide repeat expansion/contraction, " +
							"abnormal DNA methylation, and other mechanisms may not be detectable with this test. This test also cannot reliably detect mosaicism, chromosomal " +
							"aberrations, and deletions/insertions of 20 bp or more. Some genes have inherent sequence properties (for example: repeats, homology, high GC content, " +
							"rare polymorphisms) that may result in suboptimal data, and variants in those regions may not be reliably identified.")
			.i18n("ENUS").build();
	public static final TestInfo ON211 = TestInfo.builder()
			.code("ON211")
			.title("WES_Trio(Mother) Test Report")
			.methods(new String[] {
					"Genomic DNA was extracted from EDTA whole blood and we captured all the exons of human genes using xGen™ Exome Hybridization Panel. Sequencing was performed on  Novaseq 6000DX platform generating 2 x 150 paired end reads. The DNA sequence reads were aligned to reference sequence based on public human genome build GRCh37/UCSC hg19. Using a in-house bioinformatics pipeline, data were filtered and analysed to identify sequence variants.",
					"Sequence variants were classified based on the ACMG/AMP guidelines (Richards et al., 2015). Reported results are focused on pathogenic and likely pathogenic variants in genes related to the phenotype of proband, while variants of uncertain significance are only rarely reported at our discretion. Depending on the results of additional studies in the literature and databases, the classification of the variant may change. Variants that pass internal QC criteria are not validated by Sanger sequencing."
			}).limitation(
					"The absence of definitive pathogenic findings does not rule out the diagnosis of a genetic disorder as some genetic abnormalities may be undetectable " +
							"with this test. It is possible that the genomic region where a disease-causing variant exists in the proband was not captured or sufficiently " +
							"sequenced with low quality. Additionally, multifactorial disorders and some types of genetic disorders due to nucleotide repeat expansion/contraction, " +
							"abnormal DNA methylation, and other mechanisms may not be detectable with this test. This test also cannot reliably detect mosaicism, chromosomal " +
							"aberrations, and deletions/insertions of 20 bp or more. Some genes have inherent sequence properties (for example: repeats, homology, high GC content, " +
							"rare polymorphisms) that may result in suboptimal data, and variants in those regions may not be reliably identified.")
			.i18n("ENUS").build();
	public static final TestInfo[] TESTS = new TestInfo[] {
			T001,
			T016, T023,
			N134, N135, N136,
			N208, N209, N210, N211,
			ON134, ON135, ON136,
			ON201, ON209, ON210, ON211,
			R2300101
	};
}
