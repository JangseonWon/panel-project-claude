package com.greencross.lims.dto.interpretation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.experimental.Accessors;


@Getter
@Accessors(fluent = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@SuppressWarnings("unchecked")
abstract public class BaseVariant<V extends BaseVariant<V>> {
    private String snv;
    private String analysis;
    private String gene;
    private String hgvsc;
    private String hgvsp;
    @JsonProperty("origin_hgvsc")
    private String originHgvsc;
    @JsonProperty("origin_hgvsp")
    private String originHgvsp;
    private String zygosity;
    private String disease;
    private String inheritance;
    @JsonProperty("class")
    private String clazz;

    public V snv(String snv) {
        this.snv = snv;
        return (V)this;
    }

    public V analysis(String analysis) {
        this.analysis = analysis;
        return (V)this;
    }

    public V gene(String gene) {
        this.gene = gene;
        return (V)this;
    }

    public V hgvsc(String hgvsc) {
        this.hgvsc = hgvsc;
        return (V)this;
    }

    public V hgvsp(String hgvsp) {
        this.hgvsp = hgvsp;
        return (V)this;
    }

    public V zygosity(String zygosity) {
        this.zygosity = zygosity;
        return (V)this;
    }

    public V disease(String disease) {
        this.disease = disease;
        return (V)this;
    }

    public V inheritance(String inheritance) {
        this.inheritance = inheritance;
        return (V)this;
    }

    public V clazz(String clazz) {
        this.clazz = clazz;
        return (V)this;
    }
}
