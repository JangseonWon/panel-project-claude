package com.greencross.lims.report.sanger;

import com.greencross.lims.report.HasServiceCode;
import com.greencross.lims.report.builder.AbstractReportDto;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
@Accessors(fluent = true)
public class SangerDto extends AbstractReportDto implements HasServiceCode {
	private String code;
	// Page1
	private Variant[] variants;
	private String interpretation;
	private String inspector;
	private String reporter;
	private String reviewer;
	private String relation; //해외용 relation 데이터
	public enum Result {
		DETECTED, NOT_DETECTED, HYPHEN;
		private static final Map<String, Result> STRING_TO_ENUM = Arrays.stream(Result.values())
				.collect(Collectors.toMap(Result::toFormattedString, Function.identity()));
		static Result from(String str) {
			if (STRING_TO_ENUM.containsKey(str)) {
				return STRING_TO_ENUM.get(str);
			} else {
				throw new IllegalArgumentException(String.format("유효하지 않은 Result입니다. (원인: %s)", str));
			}
		}
		String toFormattedString() {
			if(this.equals(DETECTED)) return "Detected";
			else if(this.equals(NOT_DETECTED)) return "Not Detected";
			else if(this.equals(HYPHEN)) return "-";
			else return null;
		}
	}
	@Data
	@Accessors(fluent = true)
	public static final class Variant {
		private String gene;
		private String dnaChange;
		private String predictedAa;
		private String zygosity;
		private Result result;
		private String clazz;
		private byte[] img;
	}
}
