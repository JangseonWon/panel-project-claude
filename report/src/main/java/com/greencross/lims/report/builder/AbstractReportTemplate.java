package com.greencross.lims.report.builder;

import com.gcgenome.lims.report.Resource;
import com.gcgenome.lims.report.Template;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

public interface AbstractReportTemplate<R extends Resource> extends Template<R> {
	String date(LocalDate date);
	String date(LocalDateTime date);
	default String age(Integer age, LocalDate birth, LocalDate sampling) {
		if(age !=null)		return String.valueOf(age);
		if(birth == null)	return "-";
		LocalDate target = sampling;
		if(sampling == null) target = LocalDate.now();
		return String.valueOf(Period.between(birth, target).getYears());
	}
	String sex(Sex sex);
	String lblMedicalInstitution();
	String lblMedicalRecordNumber();
	String lblRequestNumber();
	String lblPatientName();
	String lblPatientCode();
	String lblAgeSex();
	String lblSpecimenType();
	String lblWardDepartment();
	String lblCollectionDate();
	String lblPatientInfo();
	String lblPhysician();
	String lblReceiptReportDate();
}
