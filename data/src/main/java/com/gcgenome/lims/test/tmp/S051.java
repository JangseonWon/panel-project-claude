package com.gcgenome.lims.test.tmp;

import com.gcgenome.lims.test.HasCode;
import com.gcgenome.lims.test.HasGene;
import com.gcgenome.lims.test.I18N;
import com.gcgenome.lims.test.MayBeNationalInsurance;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
@Builder
public class S051 implements HasCode, HasGene, MayBeNationalInsurance, I18N {
	@Builder.Default
	private final String code = "S051";
	@Builder.Default
	private final String name = "FLT3-ITD [Fragment analysis]";
	@Builder.Default
	private final String title = "FLT3-ITD [Fragment analysis] 결과보고서";
	@Builder.Default
	private final String method = "PCR & Fragment length analysis";
	@Builder.Default
	private final String gene = "FLT3 유전자";
	@Builder.Default
	private final boolean isNationalInsuranceTest = true;
	@Builder.Default
	private final String i18n = "KOKR";

	public final static S051 instance = S051.builder().build();
}
