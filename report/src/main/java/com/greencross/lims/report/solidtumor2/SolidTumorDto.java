package com.greencross.lims.report.solidtumor2;

import com.greencross.lims.report.HasServiceCode;
import com.greencross.lims.report.builder.AbstractReportDto;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(fluent = true)
public class SolidTumorDto extends AbstractReportDto implements HasServiceCode {
    private String code;
    private String cancerCategory;
    private String cancerType;
    private String relation; //해외용 relation 데이터
    private Variant[] variants;
    private Hypermutability hypermutability;
    private Qc qc;
    private Method method;
    @Data
    @Accessors(fluent = true)
    public static class Variant {
        private Tier tier;
        private String snv;
        private String analysis;
        private String gene;
        private String hgvsc;
        private String hgvsp;
        private Object vaf;
        private Integer depth;
        private String significance;
        private String interpretation;
        private String kind;
        public Variant vaf(String vaf) {
            try {
                Double v = Double.parseDouble(vaf);
                this.vaf = v;
            } catch(Exception ignore){
                this.vaf = vaf;
            }
            return this;
        }
    }
    @Data
    @Accessors(fluent = true)
    public static class Method {
        private String region;
        private String panel;
        private String method;
        private String sequencing;
        private String pipeline;
        private String reference;
        private List<GeneSet> geneSets;
        private String immunotherapyInfo;
        private String qcInfo;
        private String fusionInfo;
        private List<String> limitations;
    }
    @Data
    @Accessors(fluent = true)
    public static class GeneSet {
        private String label;
        private List<String> genes;
    }
    public enum Tier {
        Tier1, Tier2, Tier3, Tier4
    }
    public enum EvidenceLevel {
        LevelA, LevelB, LevelC, LevelD
    }
    @Data
    @Accessors(fluent = true)
    public static final class Hypermutability {
        private String tmb;
        private String msi;
        private Double msiScore;
    }
    @Data
    @Accessors(fluent = true)
    public static final class Qc {
        private String snvQc;
        private String cnvQc;
        private String msiQc;
        private String rnaQc;
        private String interpretation;
        private Double purity;
        private Double pctExonOver100X;
        private Double pctExonOver1000X;
        private Double medianExonCoverage;
        private Double mad;
        private Double mbc;
        private Double usableMsi;
        private Double onTargetReads;
        private Double medianCvOver500X;
        private Double msaf;
    }
}
