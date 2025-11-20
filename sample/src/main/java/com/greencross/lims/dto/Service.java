package com.greencross.lims.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent=true)
public class Service {
    private String id;
    private String name;
    private String group;
    private String groupColor;
    private String order;
}
