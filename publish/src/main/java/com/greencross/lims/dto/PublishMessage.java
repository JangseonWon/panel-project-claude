package com.greencross.lims.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDate;

@Setter
@Getter
@Accessors(fluent=true)
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class PublishMessage {
	private String institutionName;
	private String departmentName;
	private String wardName;
	private String serviceName;
	private String patientName;
	private String sex;
	private LocalDate birth;
	private String mrn;
	private String info;
	private final String institution;
	private String physician;
	private final long sample;
	private final String service;
	private final Object interpretation;
	private final byte[] report;
	private Object customInfos;
}