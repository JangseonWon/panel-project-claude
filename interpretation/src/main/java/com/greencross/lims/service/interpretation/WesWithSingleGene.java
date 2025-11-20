package com.greencross.lims.service.interpretation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gcgenome.lims.test.single.TestInfo;
import com.gcgenome.lims.test.wes.SingleGeneTest;
import com.gcgenome.lims.test.wes.TestWithSingleInfo;
import com.greencross.lims.dto.interpretation.InterpretationParam;
import com.greencross.lims.dto.interpretation.PanelTest;
import com.greencross.lims.service.InterpretationDAO;
import com.greencross.lims.service.SnvDAO;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Map;

@Component
public class WesWithSingleGene implements Interpretable {
	private final Single single;
	private final Wes des;
	private final ObjectMapper om;
	public WesWithSingleGene(InterpretationDAO dao, SnvDAO snvRepo, ObjectMapper om) {
		this.om = om;
		this.single = new Single(dao, snvRepo, om) {
			@Override
			public boolean chk(long sample, String service) {
				for(TestInfo test: SingleGeneTest.TESTS) if(test.code().equalsIgnoreCase(service)) return true;
				return false;
			}
			protected TestInfo test(String service) {
				return Arrays.stream(SingleGeneTest.TESTS).filter(t->t.code().equalsIgnoreCase(service)).findFirst().get();
			}
		};
		this.des = new Wes(dao, snvRepo, om) {
			@Override
			public boolean chk(long sample, String service) {
				for(TestWithSingleInfo test: TestWithSingleInfo.TESTS) if(test.code().equalsIgnoreCase(service)) return true;
				return false;
			}
		};
	}
	@Override
	public boolean chk(long sample, String service) {
		for(TestWithSingleInfo test: TestWithSingleInfo.TESTS) if(test.code().equalsIgnoreCase(service)) return true;
		return false;
	}

	@Override
	@Transactional(readOnly = true)
	public Mono<Object> interpret(long sample, String service, InterpretationParam param) {
		com.greencross.lims.dto.interpretation.Des interpretation = om.convertValue(param.json(), com.greencross.lims.dto.interpretation.Des.class);
		Map interpretation2 = om.convertValue(interpretation.incidentalFindings(), Map.class);
		Mono<com.greencross.lims.dto.interpretation.Des> single = this.single.interpret(sample, service, param)
																			 .map(obj->(PanelTest)obj)
																			 .map(this::singleToDes);
		InterpretationParam param2 = new InterpretationParam().disease(param.disease()).suffix(param.suffix()).json(interpretation2);
		Mono<com.greencross.lims.dto.interpretation.Des> des = this.des.interpret(sample, service, param2)
				.map(obj->(com.greencross.lims.dto.interpretation.Des)obj);
		return Mono.zip(single, des).map(s->s.getT1().incidentalFindings(s.getT2()));
	}

	@Override
	public Object negative(long sample, String service) {
		PanelTest single = (PanelTest) this.single.negative(sample, service);
		com.greencross.lims.dto.interpretation.Des des = (com.greencross.lims.dto.interpretation.Des) this.des.negative(sample, service);
		return singleToDes(single).incidentalFindings(des);
	}
	private com.greencross.lims.dto.interpretation.Des singleToDes(PanelTest dto) {
		com.greencross.lims.dto.interpretation.Des.Variant[] variants = null;
		if(dto.variants()!=null) variants = Arrays.stream(dto.variants()).map(this::singleToDes).toArray(com.greencross.lims.dto.interpretation.Des.Variant[]::new);
		return new com.greencross.lims.dto.interpretation.Des().result(dto.result())
				.resultText(dto.resultText())
				.reasonForReferral(dto.reasonForReferral())
				.abbreviationReference(dto.abbreviationReference())
				.abbreviationDisease(dto.abbreviationDisease())
				.abbreviation(dto.abbreviation())
				.interpretation(dto.interpretation())
				.meanDepth(dto.meanDepth())
				.coverage(dto.coverage())
				.variants(variants);
	}
	private com.greencross.lims.dto.interpretation.Des.Variant singleToDes(PanelTest.Variant dto) {

		return new com.greencross.lims.dto.interpretation.Des.Variant().snv(dto.snv())
				.analysis(dto.analysis())
				.gene(dto.gene())
				.hgvsc(dto.hgvsc())
				.hgvsp(dto.hgvsp())
				.zygosity(dto.zygosity())
				.disease(dto.disease())
				.inheritance(dto.inheritance())
				.clazz(dto.clazz());
	}
}
