package com.greencross.lims.service.interpretation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gcgenome.lims.test.sanger.TestInfo;
import com.greencross.lims.dto.interpretation.InterpretationParam;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.stream.Collectors;

@Component
public class Sanger implements Interpretable {
	private Interpretable[] deletages = new Interpretable[]{
			new SangerKoKr(), new SangerEnUs()
	};
	private final ObjectMapper om;
	public Sanger(ObjectMapper om) {
		this.om = om;
	}

	@Override
	public boolean chk(long sample, String service) {
		return Arrays.stream(deletages).anyMatch(d->d.chk(sample, service));
	}

	@Override
	public Mono<Object> interpret(long sample, String service, InterpretationParam param) {
		return Arrays.stream(deletages).filter(d->d.chk(sample, service)).map(d->d.interpret(sample, service, param)).findFirst().get();
	}

	@Override
	@Transactional(readOnly = true)
	public Object negative(long sample, String service) {
		return Arrays.stream(deletages).filter(d->d.chk(sample, service)).map(d->d.negative(sample, service)).findFirst().get();
	}

	private final class SangerKoKr implements Interpretable {
		private static final String POSITIVE_FORMAT = "%g 유전자의 %d (%a)변이가 본 검사 대상자에서 %z로 검출되었습니다.";
		private static final String NEGATIVE_FORMAT = "%g 유전자의 %d (%a)변이가 본 검사 대상자에서 검출되지 않았습니다.";
		private static final String PROBAND_ABSENT_NOTE = "*Note : 본 검사는 Proband 검체 없이 진행되었으므로, 결과가 음성일 경우 allele drop-out 으로 인한 위음성 가능성을 완전히 배제할 수 없습니다.";

		@Override
		public boolean chk(long sample, String service) {
			if(service==null || service.length() > 4) return false;
			for(TestInfo test: TestInfo.TESTS) if(test.code().equalsIgnoreCase(service)) return true;
			return false;
		}

		@Override
		public Mono<Object> interpret(long sample, String service, InterpretationParam param) {
			var prev = om.convertValue(param.json(), com.greencross.lims.dto.interpretation.Sanger.class);
			String interpretation = Arrays.stream(prev.variants())
					.map(v -> {
						String fmt = "Detected".equalsIgnoreCase(v.result()) ? POSITIVE_FORMAT : NEGATIVE_FORMAT;
						return fmt.replace("%g", v.gene())
								.replace("%d", v.hgvsc())
								.replace("%a", v.hgvsp())
								.replace("%z", zygosityFull(v.zygosity()));
					}).collect(
							Collectors.joining("\r\n\r\n", "", Boolean.TRUE.equals(prev.hasProbandSpecimen()) ? "" : "\r\n\r\n" + PROBAND_ABSENT_NOTE));
			prev.interpretation(interpretation);
			return Mono.just(prev);
		}

		@Override
		public Object negative(long sample, String service) {
			var prev = new com.greencross.lims.dto.interpretation.Sanger();
			String interpretation = Arrays.stream(prev.variants())
					.map(v -> {
						String fmt = NEGATIVE_FORMAT;
						return fmt.replace("%g", v.gene())
								.replace("%d", v.hgvsc())
								.replace("%a", v.hgvsp())
								.replace("%z", zygosityFull(v.zygosity()));
					}).collect(
							Collectors.joining("\r\n\r\n", "", Boolean.TRUE.equals(prev.hasProbandSpecimen()) ? "" : "\r\n\r\n" + PROBAND_ABSENT_NOTE));
			prev.interpretation(interpretation);
			return Mono.just(prev);
		}
	}
	private final class SangerEnUs implements Interpretable {
		private static final String POSITIVE_FORMAT = "A variant(%d) was detected on the %g in this family member.";
		private static final String NEGATIVE_FORMAT = "No variant(%d) was detected on the %g in this family member.";
		private static final String PROBAND_ABSENT_NOTE = "*Note: This test was conducted in the absence of a proband sample; therefore, in the case of a negative result, the possibility of a false negative due to allele drop-out cannot be entirely excluded.";
		@Override
		public boolean chk(long sample, String service) {
			if(service!=null && service.length() > 4 && service.startsWith("O"))
				for(TestInfo test: TestInfo.TESTS) if(test.code().equalsIgnoreCase(service)) return true;
			return false;
		}

		@Override
		public Mono<Object> interpret(long sample, String service, InterpretationParam param) {
			var prev = om.convertValue(param.json(), com.greencross.lims.dto.interpretation.Sanger.class);
			String interpretation = Arrays.stream(prev.variants())
					.map(v -> {
						String fmt = "Detected".equalsIgnoreCase(v.result()) ? POSITIVE_FORMAT : NEGATIVE_FORMAT;
						return fmt.replace("%g", v.gene())
								.replace("%d", v.hgvsc())
								.replace("%a", v.hgvsp())
								.replace("%z", zygosityFull(v.zygosity()));
					}).collect(
							Collectors.joining("\r\n\r\n", "", Boolean.TRUE.equals(prev.hasProbandSpecimen()) ? "" : "\r\n\r\n" + PROBAND_ABSENT_NOTE));
			prev.interpretation(interpretation);
			return Mono.just(prev);
		}
		@Override
		public Object negative(long sample, String service) {
			var prev = new com.greencross.lims.dto.interpretation.Sanger();
			var interpretation = new com.greencross.lims.dto.interpretation.Sanger()
					.interpretation(NEGATIVE_FORMAT +
							Collectors.joining("\r\n\r\n", "", Boolean.TRUE.equals(prev.hasProbandSpecimen()) ? "" : "\r\n\r\n" + PROBAND_ABSENT_NOTE));
			return interpretation;
		}
	}
	public static String zygosityFull(String abbr) {
		if("-".equals(abbr)) return abbr;
		if("Hom".equalsIgnoreCase(abbr)) return "Homozygote";
		if("Het".equalsIgnoreCase(abbr)) return "Heterozygote";
		if("Hem".equalsIgnoreCase(abbr)) return "Hemizygote";
		return abbr;
	}
}
