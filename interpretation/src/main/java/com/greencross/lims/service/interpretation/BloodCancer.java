package com.greencross.lims.service.interpretation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.greencross.lims.dto.interpretation.BloodCancer.Result;
import com.greencross.lims.dto.interpretation.BloodCancer.Tier;
import com.greencross.lims.dto.interpretation.InterpretationParam;
import com.greencross.lims.service.InterpretationDAO;
import com.gcgenome.lims.test.bloodcancer.TestInfo;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Arrays;

@Component
public class BloodCancer implements Interpretable {
	private final InterpretationDAO dao;
	private final ObjectMapper om;
	public BloodCancer(InterpretationDAO dao, ObjectMapper om) {
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
		TestInfo test = Arrays.stream(TestInfo.TESTS).filter(t->t.code().equalsIgnoreCase(service)).findFirst().get();
		com.greencross.lims.dto.interpretation.BloodCancer interpretation = om.convertValue(param.json(), com.greencross.lims.dto.interpretation.BloodCancer.class);
		interpretation.cancerType("R/O " + test.referralDefault());

		Arrays.stream(interpretation.results())
			  .forEach(result->{
				  if(result.variants()!=null && result.variants().length > 0) {
					  result.interpretation("-------- 소견 입력 ---------");
				  } else {
					  if(result.tier() == Tier.Tier1) result.interpretation(NEGATIVE_FORMAT_TIER1);
					  else if(result.tier() == Tier.Tier2) result.interpretation(NEGATIVE_FORMAT_TIER2);
					  else if(result.tier() == Tier.Tier3) result.interpretation(NEGATIVE_FORMAT_TIER3);
				  }
			  });
		return Mono.just(interpretation);
	}

	private static final String NEGATIVE_FORMAT_TIER1 = "Tier 1 (Strong clinical significance) 에 해당하는 변이가 발견되지 않았습니다.";
	private static final String NEGATIVE_FORMAT_TIER2 = "Tier 2 (Potential clinical significance) 에 해당하는 변이가 발견되지 않았습니다.";
	private static final String NEGATIVE_FORMAT_TIER3 = "Tier 3 (Unknown clinical significance) 에 해당하는 변이가 발견되지 않았습니다.";

	@Override
	public Object negative(long sample, String service) {
		TestInfo test = Arrays.stream(TestInfo.TESTS).filter(t->t.code().equalsIgnoreCase(service)).findFirst().get();
		var interpretation = new com.greencross.lims.dto.interpretation.BloodCancer();
		interpretation.cancerType("R/O " + test.referralDefault()).results(new Result[] {
			new Result().tier(Tier.Tier1).interpretation(NEGATIVE_FORMAT_TIER1),
			new Result().tier(Tier.Tier2).interpretation(NEGATIVE_FORMAT_TIER2),
			new Result().tier(Tier.Tier3).interpretation(NEGATIVE_FORMAT_TIER3)
		});
		return interpretation;
	}
}
