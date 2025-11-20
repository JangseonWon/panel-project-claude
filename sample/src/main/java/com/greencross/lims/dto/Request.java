package com.greencross.lims.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;

@Data
@Accessors(fluent=true)
public final class Request {
    private long sample;
    private String sampleType;
    private long barcode;
    private Service service;
    private long dateRequest;
    private long dateStart;
    private long dateDue;
    private Map<String, String> values;
    private String remark;

    private String patientName;
    private String patientCode;
    private String customerName;
    private String mrn;
    private String patientSex;
    private long dateSampling;
    private long tat;
}
