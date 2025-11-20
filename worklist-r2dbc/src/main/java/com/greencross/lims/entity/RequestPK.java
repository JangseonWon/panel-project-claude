package com.greencross.lims.entity;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
public class RequestPK {
	private Long sample;
	private String service;
}
