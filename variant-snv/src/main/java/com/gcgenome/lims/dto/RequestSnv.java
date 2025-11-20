package com.gcgenome.lims.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;

@Data
@Accessors(fluent=true)
public final class RequestSnv {
	@JsonProperty("date_request")
	private Long dateRequest;
	@JsonProperty("date_start")
	private Long dateStart;
	@JsonProperty("date_due")
	private Long dateDue;
	private Integer tat;
	private Request.Service service;
	@JsonProperty("patient_code")
	private String patientCode;
	@JsonProperty("patient_name")
	private String patientName;
	@JsonProperty("patient_sex")
	private String patientSex;
	@JsonProperty("customer_name")
	private String customerName;
	private String mrn;
	private Long sample;
	@JsonProperty("sample_type")
	private String sampleType;
	private String remark;
	private Long barcode;
	private Map<String, String> values;

	private String batch;
	private Integer row;
	private String serial;
	private String tier;
	private String genotype;
	private Double vaf;
	private Double depth;
	private Variant variant;
}
