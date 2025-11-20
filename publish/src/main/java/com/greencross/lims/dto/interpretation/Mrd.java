package com.greencross.lims.dto.interpretation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.greencross.lims.dto.interpretation.MrdScreen;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Arrays;

@Setter
@Getter
@Accessors(fluent=true)
@JsonIgnoreProperties(ignoreUnknown = true)
public final class Mrd {
    // For MRD
    private String cancerType;
    private MrdHistory[] histories;

    // For MRD-Screen
    private String date;
    private Double inputDna;
    private MrdScreen.MrdScreenGeneResult[] results;

    // Common
    private String mutationRate;

    public com.greencross.lims.dto.interpretation.Mrd inputDna(Integer inputDna) {
        if(inputDna == null) this.inputDna = null;
        else this.inputDna = inputDna +0.0;
        return this;
    }

    public Integer inputDna() {
        if(inputDna==null) {
            if(histories!=null && histories.length > 0) return last().inputDna();
            else return null;
        } else return inputDna.intValue();
    }

    public MrdHistory last() {
        return histories[0];
    }

    @Setter
    @Getter
    @Accessors(fluent=true)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class MrdHistory {
        private String date;
        private Double inputDna;
        private MrdGeneResult[] results;

        public MrdHistory inputDna(int inputDna) {
            this.inputDna = inputDna +0.0;
            return this;
        }

        public Integer inputDna() {
            if(inputDna==null) return null;
            else return inputDna.intValue();
        }

        public long nucleatedCells() {
            return Math.round(inputDna/6.5 * 1000);
        }

        public double pctClonalNucelatedCells(String gene) {
            return Arrays.stream(results).filter(r->gene.equalsIgnoreCase(r.gene())).findAny().get().equivalent() / (double) nucleatedCells();
        }
    }
    @Setter
    @Getter
    @Accessors(fluent=true)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class MrdGeneResult {
        private String gene;
        private MrdCloneResult target;
        private MrdCloneResult lqic;
        private String result;
        private String interpretation;

        public MrdGeneResult result(Result result) {
            this.result = result!=null?result.name():null;
            return this;
        }

        public Result result() {
            if(result==null) return null;
            else return Result.valueOf(result);
        }

        public long bCells() {
            return Math.round(target.readDepth() * 100.0 / lqic.readDepth() - 100);
        }

        public long equivalent() {
            return Math.round(target.clonalDepth() * 100.0 / lqic.readDepth());
        }

        public float pctClonalBCells() {
            return equivalent() / (float) bCells();
        }
    }
    @Setter
    @Getter
    @Accessors(fluent=true)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class MrdCloneResult {
        private Double readDepth;
        private Double clonalDepth;

        public MrdCloneResult readDepth(long readDepth) {
            this.readDepth = readDepth +0.0;
            return this;
        }

        public MrdCloneResult clonalDepth(long clonalDepth) {
            this.clonalDepth = clonalDepth +0.0;
            return this;
        }

        public Long readDepth() {
            if(readDepth==null) return null;
            else return readDepth.longValue();
        }

        public Long clonalDepth() {
            if(clonalDepth==null) return null;
            else return clonalDepth.longValue();
        }
    }
    public enum Result {
        DETECTED, NOT_DETECTED, NA, CUSTOM
    }
}