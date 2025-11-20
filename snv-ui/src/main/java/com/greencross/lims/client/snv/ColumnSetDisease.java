package com.greencross.lims.client.snv;

import com.greencross.lims.dto.Sheet;
import com.greencross.lims.sheet.Data;
import jsinterop.base.JsPropertyMap;

public class ColumnSetDisease implements ColumnSet {
    private boolean value = true;
    @Override
    public String name() {
        return "Disease";
    }

    @Override
    public Sheet.ColumnDefinition[] columns() {
        return new Sheet.ColumnDefinition[] {
           new Sheet.ColumnDefinition().width(80).readonly(true).id("spliceai_ds_max").name("SpliceAI_DS_Max").type(Sheet.ColumnDefinition.ColumnType.NUMERIC).numberFormat("0.000").styleText(new Sheet.StyleText().align(Sheet.Alignment.RIGHT).font("JetBrains Mono").fontSize(11)),
           new Sheet.ColumnDefinition().width(50).readonly(true).id("splicing_distance").name("Splicing_distance").type(Sheet.ColumnDefinition.ColumnType.NUMERIC).numberFormat("0").styleText(new Sheet.StyleText().align(Sheet.Alignment.RIGHT).font("JetBrains Mono").fontSize(11)),
           new Sheet.ColumnDefinition().width(200).readonly(true).id("disease_description").name("Disease_description").type(Sheet.ColumnDefinition.ColumnType.TEXT).styleText(new Sheet.StyleText().align(Sheet.Alignment.LEFT)),
           new Sheet.ColumnDefinition().width(200).readonly(true).id("clinvar.clndbn").name("ClinVar Disease").type(Sheet.ColumnDefinition.ColumnType.STRING).styleText(new Sheet.StyleText().align(Sheet.Alignment.LEFT)),
           new Sheet.ColumnDefinition().width(200).readonly(true).id("hgmd.web.literature").name("HGMD_Web_Literature").type(Sheet.ColumnDefinition.ColumnType.TEXT).styleText(new Sheet.StyleText().align(Sheet.Alignment.LEFT)),
           new Sheet.ColumnDefinition().width(200).readonly(true).id("hgmd.codon.disease").name("HGMD_Codon_Disease").type(Sheet.ColumnDefinition.ColumnType.STRING).styleText(new Sheet.StyleText().align(Sheet.Alignment.LEFT)),
        };
    }
    @Override
    public Data map(JsPropertyMap<Object> map, Data data) {
        data.put("splicing_distance", ColumnSet.unwrap(map.get("splicing_distance")))
                .put("disease_description", ColumnSet.unwrap(map.get("disease_description")))
                .put("clinvar.clndbn", ColumnSet.unwrap(map.get("clinvar.clndbn")))
                .put("hgmd.codon.disease",ColumnSet.unwrap(map.get("hgmd.codon.disease")))
                .put("hgmd.web.literature", ColumnSet.unwrap(map.get("hgmd.web.literature")))
                .put("spliceai_ds_max", (String)map.get("spliceai_ds_max"));
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
