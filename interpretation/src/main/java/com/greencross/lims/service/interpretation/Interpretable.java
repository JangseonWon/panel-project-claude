package com.greencross.lims.service.interpretation;

import com.greencross.lims.dto.interpretation.InterpretationParam;
import reactor.core.publisher.Mono;

public interface Interpretable {
	boolean chk(long sample, String service);
	Mono<Object> interpret(long sample, String service, InterpretationParam param);
	Object negative(long sample, String service);
}
