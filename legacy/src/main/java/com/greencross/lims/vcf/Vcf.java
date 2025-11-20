package com.greencross.lims.vcf;

import com.greencross.lims.entity.Analysis;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "vcf", indexes = {
    @Index(columnList="sheet, batch, row"),
    @Index(columnList="variant"),
    @Index(columnList="tier")
})
@Data
@Accessors(fluent = true)
public class Vcf {
    @EmbeddedId
    private VCFPK pk = new VCFPK();
    @Column(name="tier", length=5)
    private String tier;
    private String genotype;
    private Double vaf;
    private Long depth;

    @JoinColumns({
            @JoinColumn(name="sheet", referencedColumnName="sheet", insertable=false, updatable=false)
            , @JoinColumn(name="batch", referencedColumnName="batch", insertable=false, updatable=false)
            , @JoinColumn(name="row", referencedColumnName="row", insertable=false, updatable=false)
    })@ManyToOne(fetch = FetchType.LAZY)
    private Analysis analysis;

    public Vcf analysis(Analysis analysis) {
        // this.analysis = analysis;
        pk.sheet(analysis.pk().sheet()).batch(analysis.pk().batch()).row(analysis.row());
        return this;
    }
    /*public Vcf variant(Variant variant) {
        pk.variant(variant.id());
        return this;
    }*/
    @Embeddable
    @Data
    @Accessors(fluent = true)
    public static class VCFPK implements Serializable {
        @Column(name="sheet", columnDefinition="uuid", nullable=false, updatable=false)
        private UUID sheet;
        @Column(name="batch", nullable=false, updatable=false)
        private Integer batch;
        @Column(name="row", nullable=false, updatable=false)
        private Integer row;
        @Column(name="variant", nullable=false, updatable=false)
        private String variant;

        public VCFPK() {}
        public VCFPK(UUID sheet, Integer batch, Integer row) {
            this.sheet = sheet;
            this.batch = batch;
            this.row = row;
        }
    }
}
