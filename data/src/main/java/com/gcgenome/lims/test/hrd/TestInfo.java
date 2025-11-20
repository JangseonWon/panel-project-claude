package com.gcgenome.lims.test.hrd;

import com.gcgenome.lims.test.HasCode;
import com.gcgenome.lims.test.I18N;
import com.gcgenome.lims.test.MayBeNationalInsurance;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
@Builder
public class TestInfo implements HasCode, MayBeNationalInsurance, I18N {
	private final String code;
	private final String name;
	@Builder.Default
	private final boolean isNationalInsuranceTest = false;
	private String[] tiers;
	@Builder.Default
	private final String i18n = "KOKR";
	private final Gene[] genesEssential;
	private final Gene[] genesAdditional;
	@Data
	@Accessors(fluent = true)
	public static class Gene {
		private final String name;
		private final String symbol;
		private final String exon;
		private final String reference;
		@Builder
		public Gene(String name, String symbol, String exon, String reference) {
			this.name = name;
			if(symbol == null) this.symbol = name;
			else this.symbol = symbol;
			this.exon = exon;
			this.reference = reference;
		}
		@Builder
		public Gene(String name, String exon, String reference) {
			this.name = name;
			this.symbol = name;
			this.exon = exon;
			this.reference = reference;
		}
	}
	public static final TestInfo N140 = TestInfo.builder()
			.code("N140")
			.name("상동 재조합 결핍 검사(그린플랜 HRD)")
			.tiers(new String[] {"BRCA", "TIER1", "TIER2"})
			.genesEssential(new Gene[] {
					Gene.builder().name("BRCA1").exon("All coding region").reference("NM_007294").build(),
					Gene.builder().name("BRCA2").exon("All coding region").reference("NM_000059").build(),
			}).genesAdditional(new Gene[] {
					Gene.builder().name("ATM").exon("All coding region").reference("NM_000051").build(),
					Gene.builder().name("BARD1").exon("All coding region").reference("NM_000465").build(),
					Gene.builder().name("BRIP1").exon("All coding region").reference("NM_032043").build(),
					Gene.builder().name("CDK12").exon("All coding region").reference("NM_016507").build(),
					Gene.builder().name("CHEK1").exon("All coding region").reference("NM_001114121").build(),
					Gene.builder().name("CHEK2").exon("All coding region").reference("NM_007194").build(),
					Gene.builder().name("FANCC").exon("All coding region").reference("NM_000136").build(),
					Gene.builder().name("FANCD2").exon("All coding region").reference("NM_033084").build(),
					Gene.builder().name("FANCE").exon("All coding region").reference("NM_021922").build(),
					Gene.builder().name("FANCF").exon("All coding region").reference("NM_022725").build(),
					Gene.builder().name("FANCG").exon("All coding region").reference("NM_004629").build(),
					Gene.builder().name("FANCI").exon("All coding region").reference("NM_001113378").build(),
					Gene.builder().name("FANCL").exon("All coding region").reference("NM_018062").build(),
					Gene.builder().name("FANCM").exon("All coding region").reference("NM_020937").build(),
					Gene.builder().name("MRE11").exon("All coding region").reference("NM_005591").build(),
					Gene.builder().name("NBN").exon("All coding region").reference("NM_002485").build(),
					Gene.builder().name("PALB2").exon("All coding region").reference("NM_024675").build(),
					Gene.builder().name("PPP2R2A").exon("All coding region").reference("NM_002717").build(),
					Gene.builder().name("RAD50").exon("All coding region").reference("NM_005732").build(),
					Gene.builder().name("RAD51").exon("All coding region").reference("NM_002875").build(),
					Gene.builder().name("RAD51B").exon("All coding region").reference("NM_133509").build(),
					Gene.builder().name("RAD51C").exon("All coding region").reference("NM_058216").build(),
					Gene.builder().name("RAD51D").exon("All coding region").reference("NM_002878").build(),
					Gene.builder().name("RAD52").exon("All coding region").reference("NM_134424").build(),
					Gene.builder().name("RAD54L").exon("All coding region").reference("NM_003579").build(),
					Gene.builder().name("XRCC2").exon("All coding region").reference("NM_005431").build()
			}).isNationalInsuranceTest(false).build();
	public static final TestInfo ON140 = TestInfo.builder()
			.code("ON140")
			.name("Homologous Recombination Deficiency Test(Green Plan) Report")
			.tiers(new String[] {"BRCA", "TIER1", "TIER2"})
			.i18n("ENUS")
			.genesEssential(new Gene[] {
					Gene.builder().name("BRCA1").exon("All coding region").reference("NM_007294").build(),
					Gene.builder().name("BRCA2").exon("All coding region").reference("NM_000059").build(),
			}).genesAdditional(new Gene[] {
					Gene.builder().name("ATM").exon("All coding region").reference("NM_000051").build(),
					Gene.builder().name("BARD1").exon("All coding region").reference("NM_000465").build(),
					Gene.builder().name("BRIP1").exon("All coding region").reference("NM_032043").build(),
					Gene.builder().name("CDK12").exon("All coding region").reference("NM_016507").build(),
					Gene.builder().name("CHEK1").exon("All coding region").reference("NM_001114121").build(),
					Gene.builder().name("CHEK2").exon("All coding region").reference("NM_007194").build(),
					Gene.builder().name("FANCC").exon("All coding region").reference("NM_000136").build(),
					Gene.builder().name("FANCD2").exon("All coding region").reference("NM_033084").build(),
					Gene.builder().name("FANCE").exon("All coding region").reference("NM_021922").build(),
					Gene.builder().name("FANCF").exon("All coding region").reference("NM_022725").build(),
					Gene.builder().name("FANCG").exon("All coding region").reference("NM_004629").build(),
					Gene.builder().name("FANCI").exon("All coding region").reference("NM_001113378").build(),
					Gene.builder().name("FANCL").exon("All coding region").reference("NM_018062").build(),
					Gene.builder().name("FANCM").exon("All coding region").reference("NM_020937").build(),
					Gene.builder().name("MRE11").exon("All coding region").reference("NM_005591").build(),
					Gene.builder().name("NBN").exon("All coding region").reference("NM_002485").build(),
					Gene.builder().name("PALB2").exon("All coding region").reference("NM_024675").build(),
					Gene.builder().name("PPP2R2A").exon("All coding region").reference("NM_002717").build(),
					Gene.builder().name("RAD50").exon("All coding region").reference("NM_005732").build(),
					Gene.builder().name("RAD51").exon("All coding region").reference("NM_002875").build(),
					Gene.builder().name("RAD51B").exon("All coding region").reference("NM_133509").build(),
					Gene.builder().name("RAD51C").exon("All coding region").reference("NM_058216").build(),
					Gene.builder().name("RAD51D").exon("All coding region").reference("NM_002878").build(),
					Gene.builder().name("RAD52").exon("All coding region").reference("NM_134424").build(),
					Gene.builder().name("RAD54L").exon("All coding region").reference("NM_003579").build(),
					Gene.builder().name("XRCC2").exon("All coding region").reference("NM_005431").build()
			}).isNationalInsuranceTest(false).build();
	public static final TestInfo[] TESTS = new TestInfo[] {N140, ON140};
}
