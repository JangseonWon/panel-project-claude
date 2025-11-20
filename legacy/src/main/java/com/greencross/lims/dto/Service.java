package com.greencross.lims.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent=true)
public final class Service {
    private String title;
    private String order;
    private String prefix;
    private Page[] children;
}