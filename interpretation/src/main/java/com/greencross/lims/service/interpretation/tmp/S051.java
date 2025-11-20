package com.greencross.lims.service.interpretation.tmp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.greencross.lims.dto.interpretation.InterpretationParam;
import com.gcgenome.lims.dto.interpretation.tmp.S051Dto;
import com.greencross.lims.service.interpretation.Interpretable;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class S051 implements Interpretable {
	private final ObjectMapper om;
	private final com.gcgenome.lims.test.tmp.S051 test = com.gcgenome.lims.test.tmp.S051.instance;
	public S051(ObjectMapper om) {
		this.om = om;
	}

	@Override
	public boolean chk(long sample, String service) {
		return test.code().equalsIgnoreCase(service);
	}

	@Override
	public Mono<Object> interpret(long sample, String service, InterpretationParam param) {
		S051Dto interpretation = om.convertValue(param.json(), S051Dto.class).header(HEADER);
		if("Not Detected".equalsIgnoreCase(interpretation.result())) interpretation.interpretation(NEGATIVE_INTERPRETATION);
		else if("Detected".equalsIgnoreCase(interpretation.result())) interpretation.interpretation(POSITIVE_INTERPRETATION);
		return Mono.just(interpretation);
	}

	@Override
	public Object negative(long sample, String service) {
		var interpretation = new S051Dto();
		interpretation.result("Not Detected").header(HEADER).interpretation(NEGATIVE_INTERPRETATION);
		return interpretation;
	}

	private static final String HEADER = "[FLT3-ITD mutation 중간 보고 입니다.]";
	private static final String NEGATIVE_INTERPRETATION = "Fragment length analysis 결과, FLT3-ITD mutation이 검출되지 않았습니다.\n\n" +
														  "* Fragment length analysis와 NGS의 검사법 차이로 인해 FLT3-ITD mutant allelic ratio에 차이가 있을 수 있습니다. " +
														  "Fragment size가 긴 경우 fragment analysis 및 NGS 모두에서 정확한 mutant allele의 proportion의 계산이 어려울 수 " +
														  "있습니다.\n\n" +
														  "* FLT3-ITD mutant의 allelic ratio가 너무 낮은 경우, PCR inhibitor가 존재하는 경우, insertion size가 너무 작거나 " +
														  "큰 경우에는 fragment length analysis에서 위음성으로 나타날 수 있습니다.\n\n" +
														  "* FLT3-ITD mutant의 insertion site, insertion length, number of individual clones, allelic ratio 등 최종 " +
														  "결과는 급성골수성백혈병 유전자 패널(NGS) 결과 보고서를 참조하시기 바랍니다.\n\n" +
			 											  "* 본 검사의 detection limit는 allelic ratio 0.01입니다.";
	private static final String POSITIVE_INTERPRETATION = "Fragment length analysis 결과, FLT3-ITD mutation이 검출되었습니다.\n\n" +
			"* Fragment length analysis와 NGS의 검사법 차이로 인해 FLT3-ITD mutant allelic ratio에 차이가 있을 수 있습니다. " +
			"Fragment size가 긴 경우 fragment analysis 및 NGS 모두에서 정확한 mutant allele의 proportion의 계산이 어려울 수 " +
			"있습니다.\n\n" +
			"* FLT3-ITD mutant의 allelic ratio가 너무 낮은 경우, PCR inhibitor가 존재하는 경우, insertion size가 너무 작거나 " +
			"큰 경우에는 fragment length analysis에서 위음성으로 나타날 수 있습니다.\n\n" +
			"* FLT3-ITD mutant의 insertion site, insertion length, number of individual clones, allelic ratio 등 최종 " +
			"결과는 급성골수성백혈병 유전자 패널(NGS) 결과 보고서를 참조하시기 바랍니다.\n\n" +
			"* 본 검사의 detection limit는 allelic ratio 0.01입니다.";
}
