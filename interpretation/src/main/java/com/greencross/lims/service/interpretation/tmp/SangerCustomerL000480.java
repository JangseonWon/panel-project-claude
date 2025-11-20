package com.greencross.lims.service.interpretation.tmp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.greencross.lims.dto.interpretation.InterpretationParam;
import com.greencross.lims.entity.Sample;
import com.greencross.lims.service.interpretation.Sanger;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.stream.Collectors;

// 한국혈우재단
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SangerCustomerL000480 extends Sanger {
	@PersistenceContext
	private EntityManager em;
	private final Postfix[] postfixes = Postfix.values();
	private final ObjectMapper om;
	public SangerCustomerL000480(ObjectMapper om) {
		super(om);
		this.om = om;
	}

	@Override
	@Transactional(readOnly = true)
	public boolean chk(long sample, String service) {
		var patient = em.find(Sample.class, sample).patient();
		if(!"L000480".equals(patient.customerCode()) && !"L000480".equals(patient.customerCode2())) return false;
		return super.chk(sample, service);
	}

	@Override
	@Transactional(readOnly = true)
	public Mono<Object> interpret(long sample, String service, InterpretationParam param) {
		var prev = om.convertValue(param.json(), com.greencross.lims.dto.interpretation.Sanger.class);
		var patient = em.find(Sample.class, sample).patient();
		String interpretation = Arrays.stream(prev.variants())
									  .map(v->{
										  if(v!=null && v.gene()!=null) v.gene(v.gene().trim());
										  String fmt = "Detected".equalsIgnoreCase(v.result())?POSITIVE_FORMAT:NEGATIVE_FORMAT;
										  StringBuilder sb = new StringBuilder(
												  fmt.replace("%p", patient.name())
													 .replace("%m", patient.mrn())
													 .replace("%g", v.gene())
													 .replace("%d", v.hgvsc())
													 .replace("%a", v.hgvsp())
													 .replace("%z", zygosityFull(v.zygosity())));
										  Arrays.stream(postfixes)
												.filter(p->p.gene.equalsIgnoreCase(v.gene())).findFirst()
												.ifPresent(postfix->sb.append("\r\n\r\n").append(postfix.postfix));
										  return sb.toString();
									  }).collect(Collectors.joining("\r\n\r\n"));
		prev.interpretation(interpretation);
		return Mono.just(prev);
	}

	private static final String POSITIVE_FORMAT = "Proband 이름(Proband 등록번호)님에서 발견되었던 %g 유전자의 %d (%a)변이가 본 검사 대상자에서 %z로 검출되었습니다.";
	private static final String NEGATIVE_FORMAT = "Proband 이름(Proband 등록번호)님에서 발견되었던 %g 유전자의 %d (%a) 변이가 본 검사 대상자에서 검출되지 않았습니다.";
	private enum Postfix {
		F8("F8", "[임상정보] FHx of Hemophilia A, FVIII: OO%"),
		F9("F9", "[임상정보] FHx of Hemophilia B, FIX: OO%"),
		vWF("vWF", "[임상정보] 혈액형 OO형; vWF Ag OOO %; vWF activity OOO %");
		private final String gene;
		private final String postfix;
		Postfix(String gene, String postfix) {
			this.gene = gene;
			this.postfix = postfix;
		}
	}
}
