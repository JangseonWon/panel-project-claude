package com.greencross.lims.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;

@Setter
@Getter
@Accessors(fluent=true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Analysis implements Serializable {
	private String batch;
	private int row;
	private String file;
	private String panel;
	private long createAt;
	private Long lastModifyAt;
	private String serial;
	private String editor;
	private Map<String, Object> values;
	private String result;
	private Long sample;
	private String serviceName;
	private String patientName;
	private String service;
	private String state;
	private Object interpretation;
}
