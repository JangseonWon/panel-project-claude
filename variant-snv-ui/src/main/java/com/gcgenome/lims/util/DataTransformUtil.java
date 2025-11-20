package com.gcgenome.lims.util;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.regexp.shared.RegExp;
import lombok.experimental.UtilityClass;

import java.util.Date;

@UtilityClass
public class DataTransformUtil {
	public String formatSampleId(Long id) {
		if(id == null) return null;
		String cast = String.valueOf(id);
		if(cast.length() ==15) return cast.substring(0, 8) + "-" + cast.substring(8, 11) + "-" + cast.substring(11);
		return cast;
	}
	private DateTimeFormat DEFAULT_DATE_FORMAT = DateTimeFormat.getFormat("yyyy-MM-dd");
	public String formatDate(Long epoch) {
		if(epoch == null) return null;
		return DEFAULT_DATE_FORMAT.format(new Date(epoch));
	}
	private DateTimeFormat DEFAULT_DATETIME_FORMAT = DateTimeFormat.getFormat("yyyy-MM-dd HH:mm:ss");
	public static String formatDateTime(Long epoch) {
		return epoch == null ? null : DEFAULT_DATETIME_FORMAT.format(new Date(epoch));
	}
	private final static RegExp PATIENT_CODE_WITH_SEX = RegExp.compile("^(\\d{6})(-)*[0-9*]{1,7}");
	private final static RegExp PATIENT_CODE_WITHOUT_SEX = RegExp.compile("^(\\d{6})$");
	public Date toBirth(String patientCode) {
		if(patientCode == null) return null;
		patientCode = patientCode.trim();
		if(PATIENT_CODE_WITH_SEX.test(patientCode)) {
			char sex = patientCode.charAt(6);
			DateTimeFormat dtf = DateTimeFormat.getFormat("yyyyMMdd");
			if (sex == '9' || sex == '0') return dtf.parse("18" + patientCode.trim().substring(0, 6));
			else if (sex == '1' || sex == '2') return dtf.parse("19" + patientCode.trim().substring(0, 6));
			else if (sex == '3' || sex == '4') return dtf.parse("20" + patientCode.trim().substring(0, 6));
			else if (sex == '5' || sex == '6') return dtf.parse("19" + patientCode.trim().substring(0, 6));
			else if (sex == '7' || sex == '8') return dtf.parse("20" + patientCode.trim().substring(0, 6));
			throw new RuntimeException("Unsupported Sex code in:{" + patientCode + "}, code=" + sex);
		} else if(PATIENT_CODE_WITHOUT_SEX.test(patientCode)) {
			DateTimeFormat dtf = DateTimeFormat.getFormat("yyyyMMdd");
			Date date = dtf.parse("20"+patientCode);
			if(date.after(new Date())) return dtf.parse("19"+patientCode);
			else return date;
		} else return null;
	}
	public String toSex(String patientCode) {
		if(patientCode == null) return null;
		patientCode = patientCode.trim();
		if(PATIENT_CODE_WITH_SEX.test(patientCode)) {
			char sex = patientCode.charAt(6);
			return (sex - '0') % 2 == 1 ? "M" : "F";
		} else return null;
	}
	public static String formatPrecision(Double value, int precision) {
		if(value == null) return null;
		StringBuilder sb = new StringBuilder("0.");
		for(int i = 0; i < precision; ++i) sb.append("0");
		NumberFormat nf = NumberFormat.getFormat(sb.toString());
		return nf.format(value);
	}
}
