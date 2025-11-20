package com.greencross.lims.report.hrd.kokr;

import com.gcgenome.lims.test.hrd.TestInfo;
import com.greencross.lims.report.builder.LogoType;
import com.greencross.lims.report.builder.Sex;
import com.greencross.lims.report.hrd.HrdDto;
import com.greencross.lims.report.hrd.HrdResource;
import com.greencross.lims.report.hrd.HrdTemplate;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@Accessors(fluent = true)
public class HrdTemplateKoKr implements HrdTemplate {
	private static final DateTimeFormatter DTF 			= DateTimeFormatter.ofPattern("yyyy-MM-dd");
	private final String lblMedicalInstitution			= "의뢰기관";
	private final String lblMedicalRecordNumber			= "등록번호";
	private final String lblRequestNumber				= "접수번호";
	private final String lblPatientName					= "성명";
	private final String lblPatientCode					= "주민번호";
	private final String lblAgeSex						= "나이/성별";
	private final String lblSpecimenType				= "검체종류";
	private final String lblWardDepartment				= "병동/진료과";
	private final String lblCollectionDate				= "검체채취일";
	private final String lblPatientInfo					= "임상정보/기타";
	private final String lblPhysician					= "주치의";
	private final String lblReceiptReportDate			= "접수일/보고일";

	private final String lblSummary                     = "결과 요약";
	private final String lblHrd                         = "HRD\n(상동재조합결핍)";
	private final String lblBrca                        = "BRCA 변이";

	private final String lblQc						    = "QC 결과";
	private final String lblDetails						= "상세 결과";
	private final String lblGi		    				= "유전체 불안정성";
	private final String lblGiScore						= "유전체 불안정성 점수";
	private final String lblLohTitle					= "이형접합소실";
	private final String lblTaiTitle					= "텔로미어 대립 불균형";
	private final String lblLstTitle					= "광범위한 전이";
	private final String lblLoh							= "염색체의 일부분에서\n이형 접합의\n특성을 상실한 상태";
	private final String lblTai							= "염색체 끝 부분의\n텔로미어 지역의 유전체\n복제수가 2개가 아닌 상태";
	private final String lblLst							= "염색체의 넓은 지역에서\n전이가 다발적으로\n일어난 상태";

	public String tmplResultByTier(String tier) {
		if("BRCA".equals(tier)) return "BRCA 변이";
		else if("TIER1".equals(tier)) return "Tier 1";
		else if("TIER2".equals(tier)) return "Tier 2";
		else throw new RuntimeException();
	}
	private final String lblTestInfoTitle				= "검사 정보";
	private final String lblTestInfo    				= "본 검사는 최신 유전자 분석 기법인 차세대 염기서열 분석을 기반으로 유전체 불안정성과 BRCA 유전자 변이를 확인하여 상동재조합결핍 여부를 검사합니다.";
	private final String lblHrdInfoTitle				= "1. 상동재조합결핍(Homologous recombination deficiency; HRD)이란?";
	private final String lblHrdInfo    			    	=   "일반적으로 세포에서 DNA 손상이 일어나게 되면, DNA 복구 과정을 통해 DNA 손상을 복구합니다. " +
															"암세포에서는 이러한 DNA 손상이 제대로 복구되지 않고 계속 분열하게 됩니다. " +
															"DNA 복구 과정 중 BRCA 유전자 이상 등과 같이 상동 재조합 기능에 문제가 생겨 DNA 복구가 일어나지 않는 경우를 상동재조합결핍(HRD)이라고 하며, " +
															"상동재조합결핍에 의한 종양은 PARP 억제제와 같은 특정 약물을 이용한 치료 효과가 좋다는 특징이 있습니다.";
	private final String lblParpInfoTitle				= "2. PARP 억제제(PARP inhibitor)";
	private final String lblParpInfo    				=   "PARP 억제제는 난소암에 이용되는 대표적인 항암제로 DNA 복구 과정에 관여하는 PARP 단백질을 억제시키는 항암제로 DNA 복구 능력이 있는 정상 세포에서는 " +
															"독성이 나타나지 않지만 DNA 복구 능력이 정상적이지 않은 암세포에서 특이적으로 작용하는 항암제입니다. " +
															"이러한 PARP 억제제는 특히 BRCA 유전자 이상과 같은 상동재조합결핍이 나타난 종양에서 특히 효과적으로 나타납니다.";
	private final String lblTestLimitation				= "3. 검사의 한계";
	private final String[] lblLimitations				= new String[]{
			"본 검사는 염기서열분석법으로 시행되었으며, BRCA1, BRCA2 유전자의 변이와 유전체 불안정성 지표를 검사하며, 검사에 포함되지 않은 영역에 존재하는 변이는 검출할 수 없습니다.",
			"BRCA1,BRCA2 유전자의 SNV 및 small indel 검출 한계는 약 5%입니다.",
			"일부 target region은 coverage가 떨어질 가능성이 있습니다.",
			"본 검사로 germline 변이와 somatic 변이를 감별할 수 없으며 variant allele frequency가 50% 혹은 100%에 가까운 경우 germline variant의 가능성을 배제할 수 없습니다."
	};
	private final String lblTestReferences				= "4. REFERENCES";
	private final String[] lblReferences				= new String[]{
			"Br J Cancer. 2018 Nov;119(11):1401-1409.",
			"Mol Cancer Res. 2018 Jul;16(7):1103-1111.",
			"N Engl J Med. 2019 Dec 19;381(25):2391-2402.",
			"N Engl J Med. 2019 Dec 19;381(25):2416-2428."
	};
	private final String lblTestGeneInfo				= "5. 유전자 정보";
	private final String lblGeneEssential = "필수 유전자 목록";
	private final String lblGeneAdditional = "추가 유전자 목록";
	private final HrdResource resource;
	private final LogoType logoType;
	private final TestInfo testInfo;
	@Override
	public String resultToString(HrdDto.Result result) {
		switch (result) {
			case P: return "양성";
			case N: return "음성";
			default: return "-";
		}
	}
	public HrdTemplateKoKr(HrdResource resource, TestInfo testInfo, LogoType logoType) {
		this.resource = resource;
		this.logoType = logoType;
		this.testInfo = testInfo;
	}
	public final HrdResource resource() {
		return resource;
	}

	@Override
	public String date(LocalDate date) {
		if(date == null) return null;
		return DTF.format(date);
	}
	@Override
	public String date(LocalDateTime date) {
		if(date == null) return null;
		return DTF.format(date);
	}
	@Override
	public final String sex(Sex sex) {
		if(sex == null) return "-";
		switch(sex) {
			case M: return "남";
			case F: return "여";
			default: return "-";
		}
	}
}
