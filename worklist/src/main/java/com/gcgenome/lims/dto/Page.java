package com.gcgenome.lims.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent=true)
public final class Page {
    private String icon;
    @JsonProperty("icon_type")
    private String iconType;
    private String title;
    private String uri;
    private String order;
}
