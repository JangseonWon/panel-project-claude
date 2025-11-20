package com.greencross.lims.service.interpretation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.greencross.lims.dto.interpretation.InterpretationParam;
import com.greencross.lims.service.InterpretationDAO;
import com.gcgenome.lims.test.hrd.TestInfo;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Arrays;

@Component
public class Hrd implements Interpretable {
	private final InterpretationDAO dao;
	private final ObjectMapper om;
	public Hrd(InterpretationDAO dao, ObjectMapper om) {
		this.dao = dao;
		this.om = om;
	}

	@Override
	public boolean chk(long sample, String service) {
		for(TestInfo test: TestInfo.TESTS) if(test.code().equalsIgnoreCase(service)) return true;
		return false;
	}

	@Override
	public Mono<Object> interpret(long sample, String service, InterpretationParam param) {
		var interpretation = om.convertValue(param.json(), com.greencross.lims.dto.interpretation.Hrd.class);
		int score = interpretation.giScore();
		if(score >= 42) interpretation.gi("Positive");
		else interpretation.gi("Negative");
		if(interpretation.results() == null) interpretation.results(new com.greencross.lims.dto.interpretation.Hrd.GeneResult[]{
				new com.greencross.lims.dto.interpretation.Hrd.GeneResult().tier("BRCA").result("Negative").interpretation("HRD 검사에서 BRCA 변이는 발견되지 않았습니다."),
				new com.greencross.lims.dto.interpretation.Hrd.GeneResult().tier("TIER1").result("Negative").interpretation("HRD 검사에서 Tier 1 변이는 발견되지 않았습니다."),
				new com.greencross.lims.dto.interpretation.Hrd.GeneResult().tier("TIER2").result("Negative").interpretation("HRD 검사에서 Tier 2 변이는 발견되지 않았습니다.")
		}); else {
			for(var result: interpretation.results()) {
				if(result.variants()!=null && result.variants().length > 0) {
					if (Arrays.stream(result.variants()).anyMatch(v -> !"VUS".equalsIgnoreCase(v.clazz()))) result.result("Positive");
					else result.result("Negative");
					interpretation.interpretation("---------- 소견 작성 ----------");
				} else {
					String tier = null;
					if("BRCA".equalsIgnoreCase(result.tier())) tier = "BRCA";
					else if("TIER1".equalsIgnoreCase(result.tier())) tier = "Tier 1";
					else if("TIER2".equalsIgnoreCase(result.tier())) tier = "Tier 2";
					result.result("Negative").interpretation("HRD 검사에서 " + tier + " 변이는 발견되지 않았습니다.");
				}
			}
		}
		if("POSITIVE".equalsIgnoreCase(interpretation.gi())) {
			interpretation.interpretation("상동재조합결핍(Homologous Recombination Deficiency, HRD)에 의해 생성된 3종류의 유전체 상처(genomic scar)를 전체 유전체에서 종합 분석한 결과 양성입니다.\r\n" +
												  "\r\n" +
												  "-3종류의 유전체 상처는 이형접합소실(Loss of heterozygosity; LOH), 텔로미어 대립 불균형(Telomeric allelic imbalance; TAI), 광범위한 전이(Large-scale " +
												  "transition; LST)로 각각의 수치를 통합한 점수가 42점 이상인 경우, 유전체 불안정성 양성으로 분류됩니다.");
		} else interpretation.interpretation("상동재조합결핍(Homologous Recombination Deficiency, HRD)에 의해 생성된 3종류의 유전체 상처(genomic scar)를 전체 유전체에서 종합 분석한 결과 음성입니다.\r\n" +
													 "\r\n" +
													 "-3종류의 유전체 상처는 이형접합소실(Loss of heterozygosity; LOH), 텔로미어 대립 불균형(Telomeric allelic imbalance; TAI), 광범위한 전이(Large-scale " +
													 "transition; LST)로 각각의 수치를 통합한 점수가 42점 이상인 경우, 유전체 불안정성 양성으로 분류됩니다.");
		return Mono.just(interpretation);
	}

	@Override
	public Object negative(long sample, String service) {
		var interpretation = new com.greencross.lims.dto.interpretation.Hrd();
		interpretation.gi("Negative").giScore(0).snv("PASS").cnv("PASS").results(new com.greencross.lims.dto.interpretation.Hrd.GeneResult[]{
				new com.greencross.lims.dto.interpretation.Hrd.GeneResult().tier("BRCA").result("Negative").interpretation("HRD 검사에서 BRCA 변이는 발견되지 않았습니다."),
				new com.greencross.lims.dto.interpretation.Hrd.GeneResult().tier("TIER1").result("Negative").interpretation("HRD 검사에서 Tier 1 변이는 발견되지 않았습니다."),
				new com.greencross.lims.dto.interpretation.Hrd.GeneResult().tier("TIER2").result("Negative").interpretation("HRD 검사에서 Tier 2 변이는 발견되지 않았습니다.")
		}).interpretation("상동재조합결핍(Homologous Recombination Deficiency, HRD)에 의해 생성된 3종류의 유전체 상처(genomic scar)를 전체 유전체에서 종합 분석한 결과 음성입니다.\r\n" +
						  "\r\n" +
						  "-3종류의 유전체 상처는 이형접합소실(Loss of heterozygosity; LOH), 텔로미어 대립 불균형(Telomeric allelic imbalance; TAI), 광범위한 전이(Large-scale " +
							"transition; LST)로 각각의 수치를 통합한 점수가 42점 이상인 경우, 유전체 불안정성 양성으로 분류됩니다.");
		return interpretation;
	}
}
