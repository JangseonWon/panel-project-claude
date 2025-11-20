package com.greencross.lims.dto.interpretation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(fluent=true)
@JsonIgnoreProperties(ignoreUnknown = true)
public final class Hereditary {
    private String interpretation;
    private Variant[] variants;
    private Gene[] genes;
    @Setter
    @Getter
    @Accessors(fluent=true)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class Variant {
        private String snv;
        private String analysis;
        private String gene;
        private String hgvsc;
        private String hgvsp;
        private String zygosity;
        @JsonProperty("class")
        private String clazz;
    }
    @Setter
    @Getter
    @Accessors(fluent=true)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class Gene {
        private String name;
        private Boolean value;
    }
}
