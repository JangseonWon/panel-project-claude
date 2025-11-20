package com.gcgenome.lims.test.tmp;

import com.gcgenome.lims.test.HasCode;
import com.gcgenome.lims.test.I18N;
import com.gcgenome.lims.test.MayBeNationalInsurance;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
@Builder
public class N159 implements HasCode, MayBeNationalInsurance, I18N {
	@Builder.Default
	private final String code = "N159";
	private final String panel = "클론성 조혈증 패널 검사";
	@Builder.Default
	private final String title = "클론성 조혈증 패널 검사(연구 검사) 결과보고서";
	@Builder.Default
	private final boolean isNationalInsuranceTest = false;
	@Builder.Default
	private final String i18n = "KOKR";
}
