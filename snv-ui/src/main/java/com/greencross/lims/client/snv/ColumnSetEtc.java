package com.greencross.lims.client.snv;

import com.greencross.lims.dto.Sheet;
import com.greencross.lims.sheet.Data;
import jsinterop.base.JsPropertyMap;

public class ColumnSetEtc implements ColumnSet {
    private boolean value = false;

    @Override
    public String name() {
        return "etc.";
    }

    @Override
    public Sheet.ColumnDefinition[] columns() {
        return new Sheet.ColumnDefinition[] {
            new Sheet.ColumnDefinition().readonly(true).id("filter").name("Filter").type(Sheet.ColumnDefinition.ColumnType.STRING).styleText(new Sheet.StyleText().align(Sheet.Alignment.LEFT)),
            new Sheet.ColumnDefinition().readonly(true).id("sanger").name("Sanger(in-silico)").type(Sheet.ColumnDefinition.ColumnType.STRING).styleText(new Sheet.StyleText().align(Sheet.Alignment.LEFT))
        };
    }

    @Override
    public Data map(JsPropertyMap<Object> map, Data data) {
        data.put("chr", (String)NullReplace.replaceWithBlankIfNull(map.get("chrom")))
                .put("pos", (String)NullReplace.replaceWithBlankIfNull(map.get("pos")))
                .put("ref", (String)NullReplace.replaceWithBlankIfNull(map.get("ref")))
                .put("alt", (String)NullReplace.replaceWithBlankIfNull(map.get("alt")))
                .put("gene", (String)NullReplace.replaceWithBlankIfNull(map.get("gene.refgene")))
                .put("hgvsc", ColumnSet.unwrap(map.get("hgvsc_in_mane")))
                .put("hgvsp", ColumnSet.unwrap(map.get("hgvsp_in_mane")))
                .put("exon", ColumnSet.unwrap(map.get("exon_in_mane")))
                .put("so", ColumnSet.unwrap(map.get("so_term")))
                .put("effect", (String)NullReplace.replaceWithBlankIfNull(map.get("effect_level")))
                .put("genotype", (String)NullReplace.replaceWithBlankIfNull(map.get("genotype")))
                .put("depth", (String)NullReplace.replaceWithBlankIfNull(map.get("depth")))
                .put("vaf", (String)NullReplace.replaceWithBlankIfNull(map.get("vaf")))
                .put("filter", (String)NullReplace.replaceWithBlankIfNull(map.get("filter")))
                .put("sanger", (String)NullReplace.replaceWithBlankIfNull(map.get("insilico_sanger")));
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
