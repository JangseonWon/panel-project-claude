package com.greencross.lims.report.tmp;

import com.greencross.lims.report.HasServiceCode;
import com.greencross.lims.report.builder.AbstractReportDto;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
public class S051Dto extends AbstractReportDto implements HasServiceCode {
	private String code;
	private String header;
	private String result;
	private String summary;
	private String interpretation;
	/*public enum Result {
		DETECTED("Detected"), NOT_DETECTED("Not Detected");
		static Result from(String str) {
			if("Detected".equalsIgnoreCase(str)) return DETECTED;
			else if("Not Detected".equalsIgnoreCase(str)) return NOT_DETECTED;
			else return null;
		}
		private final String display;
		Result(String display) {
			this.display = display;
		}
		@Override
		public String toString() {
			return display;
		}
	}*/
}
