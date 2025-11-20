package com.greencross.lims.service.interpretation.tmp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.greencross.lims.dto.interpretation.InterpretationParam;
import com.gcgenome.lims.dto.interpretation.tmp.BallondorDto;
import com.greencross.lims.entity.Sample;
import com.greencross.lims.service.InterpretationDAO;
import com.greencross.lims.service.interpretation.Interpretable;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Arrays;

@Component
public class Ballondor implements Interpretable {
	private final InterpretationDAO dao;
	private final ObjectMapper om;
	public Ballondor(InterpretationDAO dao, ObjectMapper om) {
		this.dao = dao;
		this.om = om;
	}

	@Override
	public boolean chk(long sample, String service) {
		return Arrays.stream(com.gcgenome.lims.test.tmp.Ballondor.TESTS).anyMatch(t->t.code().equals(service));
	}

	@Override
	public Mono<Object> interpret(long sample, String service, InterpretationParam param) {
		BallondorDto interpretation = om.convertValue(param.json(), BallondorDto.class);
		if("Not Detected".equalsIgnoreCase(interpretation.result())) interpretation.summary(SUMMARY_NEGATIVE).hgvsc(null).vaf("-");
		else if("Detected".equalsIgnoreCase(interpretation.result())) interpretation.summary(SUMMARY_POSITIVE);
		com.gcgenome.lims.test.tmp.Ballondor.SampleType type = findType(sample);
		return Mono.just(interpretation.sample(type!=null?type.info():""));
	}

	private com.gcgenome.lims.test.tmp.Ballondor.SampleType findType(long sample) {
		String _t = dao.em().find(Sample.class, sample).sampleType();
		if(_t == null) return null;
		String type = _t.trim();
		return Arrays.stream(com.gcgenome.lims.test.tmp.Ballondor.SampleType.values()).filter(t->t.id().equalsIgnoreCase(type)).findFirst().orElse(null);
	}
	@Override
	public Object negative(long sample, String service) {
		var interpretation = new BallondorDto();
		interpretation.result("Not Detected").summary(SUMMARY_NEGATIVE).hgvsc(null).vaf("-");
		com.gcgenome.lims.test.tmp.Ballondor.SampleType type = findType(sample);
		return interpretation.sample(type!=null?type.info():"");
	}
	private static final String SUMMARY_POSITIVE = " gene mutation이 검출되었습니다.";
	private static final String SUMMARY_NEGATIVE = " gene mutation이 검출되지 않았습니다.";
}
