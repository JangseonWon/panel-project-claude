package com.greencross.lims.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent=true)
public class Sample {
    private long id;
    private String sampleType;
    private String remark;
    private long barcode;

    private String patientName;
    private String patientCode;
    private String customerName;
    private String mrn;
    private String patientSex;
    private long dateSampling;
}
