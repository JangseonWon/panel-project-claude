package com.greencross.lims.webhook;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
@AllArgsConstructor
public class VariantCountRequest {
    private String sample;
    private String service;
    private String batchRow;
    private String index;
}
