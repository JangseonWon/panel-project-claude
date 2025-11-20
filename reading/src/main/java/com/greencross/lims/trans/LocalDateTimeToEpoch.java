package com.greencross.lims.trans;

import lombok.experimental.UtilityClass;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@UtilityClass
public class LocalDateTimeToEpoch {
	public LocalDateTime map(long epoch) {
		return LocalDateTime.ofInstant(Instant.ofEpochMilli(epoch), ZoneId.systemDefault());
	}
	public Long map(LocalDateTime time) {
		if(time == null) return null;
		return time.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
	}
}
