package com.greencross.lims.client.snv;

import com.google.gwt.i18n.client.NumberFormat;
import com.greencross.lims.dto.Sheet;
import com.greencross.lims.sheet.Data;
import elemental2.core.JsRegExp;
import elemental2.core.RegExpResult;
import elemental2.dom.DomGlobal;
import jsinterop.base.JsPropertyMap;

public class ColumnSetFreq implements ColumnSet {
    private boolean value = true;
    @Override
    public String name() {
        return "Frequency";
    }

    @Override
    public Sheet.ColumnDefinition[] columns() {
        return new Sheet.ColumnDefinition[] {
                //new Sheet.ColumnDefinition().width(80).readonly(true).id("popfreqmax_gnomad&krgdb").name("PopFreqMax").type(Sheet.ColumnDefinition.ColumnType.NUMERIC).numberFormat("0.000000").styleText(new Sheet.StyleText().align(Sheet.Alignment.RIGHT).font("JetBrains Mono").fontSize(11)),
                new Sheet.ColumnDefinition().width(80).readonly(true).id("gnomad_total_af").name("gnomAD Total AF").type(Sheet.ColumnDefinition.ColumnType.NUMERIC).numberFormat("0.000000").styleText(new Sheet.StyleText().align(Sheet.Alignment.RIGHT).font("JetBrains Mono").fontSize(11)),
                new Sheet.ColumnDefinition().width(80).readonly(true).id("gnomad_exome_all").name("gnomAD_exomes_AF_ALL").type(Sheet.ColumnDefinition.ColumnType.NUMERIC).numberFormat("0.000000").styleText(new Sheet.StyleText().align(Sheet.Alignment.RIGHT).font("JetBrains Mono").fontSize(11)),
                new Sheet.ColumnDefinition().width(80).readonly(true).id("gnomad_exomes_af_eas").name("gnomAD_exomes_AF_EAS").type(Sheet.ColumnDefinition.ColumnType.NUMERIC).numberFormat("0.000000").styleText(new Sheet.StyleText().align(Sheet.Alignment.RIGHT).font("JetBrains Mono").fontSize(11)),
                new Sheet.ColumnDefinition().width(80).readonly(true).id("gnomad_genomes_af_eas").name("gnomAD_genomes_AF_EAS").type(Sheet.ColumnDefinition.ColumnType.NUMERIC).numberFormat("0.000000").styleText(new Sheet.StyleText().align(Sheet.Alignment.RIGHT).font("JetBrains Mono").fontSize(11)),
                new Sheet.ColumnDefinition().width(80).readonly(true).id("gnomad_exomes_af_eas_kor").name("gnomAD_exomes_AF_KOR").type(Sheet.ColumnDefinition.ColumnType.NUMERIC).numberFormat("0.000000").styleText(new Sheet.StyleText().align(Sheet.Alignment.RIGHT).font("JetBrains Mono").fontSize(11)),
                new Sheet.ColumnDefinition().width(80).readonly(true).id("krgdb_af").name("KRG_DB_AF").type(Sheet.ColumnDefinition.ColumnType.NUMERIC).numberFormat("0.000000").styleText(new Sheet.StyleText().align(Sheet.Alignment.RIGHT).font("JetBrains Mono").fontSize(11)),
                new Sheet.ColumnDefinition().width(80).readonly(true).id("wes300_af").name("WES300_freq").type(Sheet.ColumnDefinition.ColumnType.NUMERIC).numberFormat("0.000000").styleText(new Sheet.StyleText().align(Sheet.Alignment.RIGHT).font("JetBrains Mono").fontSize(11)),
                new Sheet.ColumnDefinition().width(80).readonly(true).id("wes_als312_af").name("WES_ALS312_AF").type(Sheet.ColumnDefinition.ColumnType.NUMERIC).numberFormat("0.000000").styleText(new Sheet.StyleText().align(Sheet.Alignment.RIGHT).font("JetBrains Mono").fontSize(11)),
                new Sheet.ColumnDefinition().width(80).readonly(true).id("dbsnp").name("dbSNP").type(Sheet.ColumnDefinition.ColumnType.LINK).href("https://www.ncbi.nlm.nih.gov/snp/[\"dbsnp\"]").styleText(new Sheet.StyleText().align(Sheet.Alignment.LEFT)),
        };
    }

    private final static JsRegExp CHK_NUMBER = new JsRegExp("^\\d*(\\.\\d*)?$");
    private static String toString(NumberFormat NF, Object value) throws RuntimeException {
        if(value == null) return null;
        else if(value instanceof Long) return NF.format((Long)value);
        else if(value instanceof Integer) return NF.format((Integer)value);
        else if(value instanceof Double) return NF.format((Double)value);
        else if(value instanceof String) {
            String cast = (String)value;
            cast = cast.trim();
            RegExpResult chkNumber = CHK_NUMBER.exec(cast);
            if(chkNumber != null) return NF.format(Double.parseDouble(cast));
            else return NF.format(Double.parseDouble(cast));
        } else throw new RuntimeException();
    }

    @Override
    public Data map(JsPropertyMap<Object> map, Data data) {
        DomGlobal.console.log(map.get("gnomad.exome.all"));
        String t = (String)map.get("gnomad.exome.all");
        DomGlobal.console.log(toString(NumberFormat.getFormat("0.000000"), t));
        data.put("gnomad_total_af", (String)map.get("gnomad.total"))
                .put("gnomad_exome_all", (String)map.get("gnomad.exome.all"))
                .put("gnomad_exomes_af_eas", (String)map.get("gnomad.exome.eas"))
                .put("gnomad_genomes_af_eas", (String)map.get("gnomad.genome.eas"))
                .put("gnomad_exomes_af_eas_kor", (String)map.get("gnomad.exomes.eas_kor"))
                .put("krgdb_af", (String)map.get("krgdb_af"))
                .put("wes300_af", (String)map.get("wes300_af"))
                .put("wes_als312_af", (String)map.get("wes_als312_af"))
                .put("dbsnp", (String)NullReplace.replaceWithBlankIfNull(map.get("dbsnp")));
        if(data.get("krgdb_af") == null) data.initialize("krgdb_af", (String)map.get("krg_db_1100"));
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