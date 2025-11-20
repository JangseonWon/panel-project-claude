package com.greencross.lims.service.interpretation.tmp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.greencross.lims.dto.interpretation.InterpretationParam;
import com.gcgenome.lims.dto.interpretation.tmp.N159Dto;
import com.greencross.lims.service.interpretation.Interpretable;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class N159 implements Interpretable {
	private final ObjectMapper om;
	private final com.gcgenome.lims.test.tmp.N159 test = com.gcgenome.lims.test.tmp.N159.builder().build();
	public N159(ObjectMapper om) {
		this.om = om;
	}

	@Override
	public boolean chk(long sample, String service) {
		return test.code().equalsIgnoreCase(service);
	}

	@Override
	public Mono<Object> interpret(long sample, String service, InterpretationParam param) {
		N159Dto interpretation = om.convertValue(param.json(), N159Dto.class);
		if("NEGATIVE".equalsIgnoreCase(interpretation.result())) interpretation.resultText(NEGATIVE_RESULT).interpretation(NEGATIVE_INTERPRETATION);
		else if("POSITIVE".equalsIgnoreCase(interpretation.result())) interpretation.resultText(POSITIVE_RESULT).interpretation(POSITIVE_INTERPRETATION);
		return Mono.just(interpretation);
	}

	@Override
	public Object negative(long sample, String service) {
		var interpretation = new N159Dto();
		interpretation.result("NEGATIVE").resultText(NEGATIVE_RESULT).interpretation(NEGATIVE_INTERPRETATION);
		return interpretation;
	}

	private static final String NEGATIVE_RESULT = "클론성 조혈증이 검출되지 않았습니다.";
	private static final String NEGATIVE_INTERPRETATION = "수검자의 클론성 조혈증 지놈 스크린 검사 결과 클론성 조혈증이 검출되지 않았습니다.\n" +
														  "\n" +
														  "클론성 조혈증이 검출된 경우 혈액암의 위험이 증가할 뿐 아니라 체내 염증 상태를 증가 시켜 각종 심혈관 질환, " +
														  "자가면역질환, 암의 발생 등을 증가시키는 것으로 알려졌습니다.\n" +
														  "\n" +
														  "그러나 클론성 조혈증이 검출되지 않았다고 해서 혈액암 및 각종 심혈관 질환, 암 등의 위험성이 감소하는 것은 아닙니다. " +
														  "생활 습관, 환경, 다른 유전적 요인 등이 질환의 발병에 영향을 미칠 수 있음으로, 지속적인 꾸준한 건강관리가 권고됩니다.";
	private static final String POSITIVE_RESULT = "클론성 조혈증이 검출되었습니다.";
	private static final String POSITIVE_INTERPRETATION = "클론성 조혈증은 체내 염증 반응 증가를 통해, 동맥경화, 정맥 혈전 등으로 인한 각종 심혈관 질환 위험도를 증가시키고 " +
														  "사망 위험도(all-cause mortality)를 높입니다. 주된 사망 요인인 심혈관 질환 이외에도 클론성 조혈증은 체내 염증과 관련된 " +
														  "자가면역질환, 암, 당뇨, 류마티스성 관절염, 잦은 감염 등 여러 질환과도 연관되어 있습니다.\n" +
														  "\n" +
														  "클론성 조혈증 진행 억제와 면역력 향상을 위해 운동, 건강한 식습관, 금연, 금주 등 생활습관 개선이 권장됩니다.\n" +
														  "\n" +
														  "또한 동맥경화의 조기 진단 및 심혈관 질환 예방을 위해 의료진 상담 및 정기적인 건강검진을 통한 관리가 권고됩니다.\n" +
														  "\n" +
														  "또한 클론성 조혈증이 검출될 경우 혈액암의 위험도가 증가하나 평균적으로 클론성 조혈증 양성일 때 혈액암으로 진행할 가능성은 " +
														  "1년에 0.5%~1% 정도로 알려져 있습니다. 이는 클론성 조혈증 양성의 대부분은 혈액암으로 진행하지 않음을 의미합니다. 그러나 CBC " +
														  "등 정기적인 혈액검사를 통해 추적관찰 하실 것이 권고됩니다.\n" +
														  "\n" +
														  "필요한 경우 의료진과 상담하십시오.\n";
}
