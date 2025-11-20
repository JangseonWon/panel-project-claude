package com.gcgenome.lims.trans;

import lombok.experimental.UtilityClass;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

@UtilityClass
public class LocalDateToEpoch {
	public LocalDate map(long epoch) {
		return LocalDate.ofInstant(Instant.ofEpochMilli(epoch), ZoneId.systemDefault());
	}
	public long map(LocalDate date) {
		return date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
	}
}
