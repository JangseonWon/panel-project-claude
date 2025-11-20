package com.greencross.lims.client.snv;

import com.greencross.lims.dto.Sheet;
import com.greencross.lims.sheet.Data;
import jsinterop.base.JsPropertyMap;

public class ColumnSetBasic implements ColumnSet {
    private boolean value = true;

    @Override
    public String name() {
        return "Basic";
    }

    @Override
    public Sheet.ColumnDefinition[] columns() {
        return new Sheet.ColumnDefinition[] {
            new Sheet.ColumnDefinition().width(50).readonly(true).id("exon").name("Exon").type(Sheet.ColumnDefinition.ColumnType.STRING).styleText(new Sheet.StyleText().align(Sheet.Alignment.LEFT)),
            new Sheet.ColumnDefinition().width(50).readonly(true).id("effect").name("Effect").type(Sheet.ColumnDefinition.ColumnType.STRING).styleText(new Sheet.StyleText().align(Sheet.Alignment.CENTER)),
            new Sheet.ColumnDefinition().width(50).readonly(true).id("genotype").name("Genotype").type(Sheet.ColumnDefinition.ColumnType.STRING).styleText(new Sheet.StyleText().align(Sheet.Alignment.CENTER).font("JetBrains Mono").fontSize(11)),
            new Sheet.ColumnDefinition().width(50).readonly(true).id("depth").name("Depth").type(Sheet.ColumnDefinition.ColumnType.NUMERIC).styleText(new Sheet.StyleText().align(Sheet.Alignment.RIGHT).font("JetBrains Mono").fontSize(11)),
            new Sheet.ColumnDefinition().width(80).readonly(true).id("vaf").name("VAF").type(Sheet.ColumnDefinition.ColumnType.NUMERIC).numberFormat("0.00000").styleText(new Sheet.StyleText().align(Sheet.Alignment.RIGHT).font("JetBrains Mono").fontSize(11)),
            new Sheet.ColumnDefinition().width(50).readonly(true).id("filter").name("Filter").type(Sheet.ColumnDefinition.ColumnType.STRING).styleText(new Sheet.StyleText().align(Sheet.Alignment.LEFT)),
            new Sheet.ColumnDefinition().width(50).readonly(true).id("sanger").name("iSanger").type(Sheet.ColumnDefinition.ColumnType.STRING).styleText(new Sheet.StyleText().align(Sheet.Alignment.CENTER)),
            new Sheet.ColumnDefinition().width(200).readonly(true).id("mim.disease").name("MIM_disease").type(Sheet.ColumnDefinition.ColumnType.TEXT).styleText(new Sheet.StyleText().align(Sheet.Alignment.LEFT)),
            new Sheet.ColumnDefinition().width(80).readonly(true).id("mim.inheritance").name("Inheritance").type(Sheet.ColumnDefinition.ColumnType.STRING).styleText(new Sheet.StyleText().align(Sheet.Alignment.LEFT)),
            new Sheet.ColumnDefinition().width(80).readonly(true).id("region_info").name("Region info").type(Sheet.ColumnDefinition.ColumnType.STRING).styleText(new Sheet.StyleText().align(Sheet.Alignment.LEFT))
        };
    }

    @Override
    public Data map(JsPropertyMap<Object> map, Data data) {
        data.put("chr", (String)NullReplace.replaceWithBlankIfNull(map.get("chrom")))
            .put("pos", (String)NullReplace.replaceWithBlankIfNull(map.get("pos")))
            .put("pos_", (String)NullReplace.replaceWithBlankIfNull(map.get("chrom") + ":" + Long.parseLong((String)map.get("pos"))))
            .put("ref", (String)NullReplace.replaceWithBlankIfNull(map.get("ref")))
            .put("alt", (String)NullReplace.replaceWithBlankIfNull(map.get("alt")))
            .put("tier",(String)NullReplace.replaceWithBlankIfNull(map.get("tier")))
            .put("class", ColumnSet.unwrap(map.get("class")))
            .put("gene", ColumnSet.unwrap(map.get("gene.refgene")))
            .put("exon", ColumnSet.unwrap(map.get("exon_in_mane")))
            .put("effect", ColumnSet.unwrap(map.get("effect_level")))
            .put("genotype", ColumnSet.unwrap(map.get("genotype")))
            .put("depth", ColumnSet.unwrap(map.get("depth")))
            .put("vaf", ColumnSet.unwrap(map.get("vaf")))
            .put("filter", ColumnSet.unwrap(map.get("filter")))
            .put("sanger", ColumnSet.unwrap(map.get("insilico_sanger")))
            .put("mim.disease", ColumnSet.unwrap(map.get("mim.disease")))
            .put("mim.inheritance", ColumnSet.unwrap(map.get("mim.inheritance")))
            .put("region_info", ColumnSet.unwrap(map.get("region_info")));
        return data;
    }
    @Override
    public boolean select() {
        return value;
    }

    @Override
    public void select(boolean value) {
        this.value = value;
    }
}
