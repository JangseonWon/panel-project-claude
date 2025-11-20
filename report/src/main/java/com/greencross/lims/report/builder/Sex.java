package com.greencross.lims.report.builder;

import com.greencross.lims.entity.Patient;

public enum Sex {
	M, F;

	public static Sex from(Patient.Sex sex) {
		if(sex == Patient.Sex.M) return M;
		else if(sex == Patient.Sex.F) return F;
		else return null;
	}
}
