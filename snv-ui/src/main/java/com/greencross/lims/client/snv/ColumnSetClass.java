package com.greencross.lims.client.snv;

import com.greencross.lims.dto.Sheet;
import com.greencross.lims.sheet.Data;
import jsinterop.base.JsPropertyMap;

public class ColumnSetClass implements ColumnSet {
    private boolean value = true;
    @Override
    public String name() {
        return "Classification";
    }

    @Override
    public Sheet.ColumnDefinition[] columns() {
        return new Sheet.ColumnDefinition[] {
            new Sheet.ColumnDefinition().width(150).readonly(true).id("clinvar.class").name("ClinVar Class").type(Sheet.ColumnDefinition.ColumnType.TEXT).styleText(new Sheet.StyleText().align(Sheet.Alignment.LEFT)),
            new Sheet.ColumnDefinition().width(80).readonly(true).id("clinvarid").name("ClinVar ID").type(Sheet.ColumnDefinition.ColumnType.LINK).styleText(new Sheet.StyleText().align(Sheet.Alignment.CENTER))
                .href("https://www.ncbi.nlm.nih.gov/clinvar/variation/[\"clinvarid\"]/"),
            new Sheet.ColumnDefinition().width(180).readonly(true).id("clinvar.updated_class").name("ClinVar Class(Updated)").type(Sheet.ColumnDefinition.ColumnType.STRING).styleText(new Sheet.StyleText().align(Sheet.Alignment.LEFT)),
            new Sheet.ColumnDefinition().width(180).readonly(true).id("clinvar.updated_review").name("ClinVar Review(Updated)").type(Sheet.ColumnDefinition.ColumnType.STRING).styleText(new Sheet.StyleText().align(Sheet.Alignment.LEFT)),
            new Sheet.ColumnDefinition().width(150).readonly(true).id("so").name("Seq Ontology").type(Sheet.ColumnDefinition.ColumnType.STRING).styleText(new Sheet.StyleText().align(Sheet.Alignment.LEFT)),
            new Sheet.ColumnDefinition().width(150).readonly(true).id("intervar.class").name("InterVar").type(Sheet.ColumnDefinition.ColumnType.STRING).styleText(new Sheet.StyleText().align(Sheet.Alignment.LEFT)),
            new Sheet.ColumnDefinition().width(100).readonly(true).id("intervar.evidence.pv").name("InterVar PV").type(Sheet.ColumnDefinition.ColumnType.STRING).styleText(new Sheet.StyleText().align(Sheet.Alignment.LEFT)),
            new Sheet.ColumnDefinition().width(100).readonly(true).id("intervar.evidence.bv").name("InterVar BV").type(Sheet.ColumnDefinition.ColumnType.STRING).styleText(new Sheet.StyleText().align(Sheet.Alignment.LEFT)),
            new Sheet.ColumnDefinition().width(80).readonly(true).id("hgmd.tag").name("HGMD Tag").type(Sheet.ColumnDefinition.ColumnType.STRING).styleText(new Sheet.StyleText().align(Sheet.Alignment.LEFT)),
            new Sheet.ColumnDefinition().width(100).readonly(true).id("hgmd.pmid").name("HGMD PMID").type(Sheet.ColumnDefinition.ColumnType.TEXT).styleText(new Sheet.StyleText().align(Sheet.Alignment.LEFT)),
            new Sheet.ColumnDefinition().width(80).readonly(true).id("hgmd.web.tag").name("HGMD Web Tag").type(Sheet.ColumnDefinition.ColumnType.STRING).styleText(new Sheet.StyleText().align(Sheet.Alignment.LEFT)),
            new Sheet.ColumnDefinition().width(80).readonly(true).id("qual").name("Qual").type(Sheet.ColumnDefinition.ColumnType.NUMERIC).numberFormat("0.00").styleText(new Sheet.StyleText().align(Sheet.Alignment.RIGHT).font("JetBrains Mono").fontSize(11)),
        };
    }
    @Override
    public Data map(JsPropertyMap<Object> map, Data data) {
        data.put("clinvar.class",ColumnSet.unwrap(map.get("clinvar.class")))
            .put("clinvarid", ColumnSet.unwrap(map.get("clinvar.id")))
            .put("clinvar.updated_class", ColumnSet.unwrap(map.get("clinvar.updated_class")))
            .put("clinvar.updated_review", ColumnSet.unwrap(map.get("clinvar.updated_review")))
            .put("intervar.class", ColumnSet.unwrap(map.get("intervar.class")))
            .put("intervar.evidence.pv", ColumnSet.unwrap(map.get("intervar.evidence.pv")))
            .put("intervar.evidence.bv", ColumnSet.unwrap(map.get("intervar.evidence.bv")))
            .put("hgmd.tag", ColumnSet.unwrap(map.get("hgmd.tag")))
            .put("hgmd.pmid", ColumnSet.unwrap(map.get("hgmd.pmid")))
            .put("hgmd.web.tag", ColumnSet.unwrap(map.get("hgmd.web.tag")))
            .put("so", ColumnSet.unwrap(map.get("so_term")))
            //.put("bic", unwrap(map.get("bic")))
            //.put("koncord_class", unwrap(map.get("koncord_class")))
           // .put("lovd_class", unwrap(map.get("lovd_class")))
            .put("qual", ColumnSet.unwrap(map.get("qual")));
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
