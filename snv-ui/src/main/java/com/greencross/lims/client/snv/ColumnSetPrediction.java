package com.greencross.lims.client.snv;

import com.greencross.lims.dto.Sheet;
import com.greencross.lims.sheet.Data;
import jsinterop.base.JsPropertyMap;

public class ColumnSetPrediction implements ColumnSet {
    private boolean value = true;
    @Override
    public String name() {
        return "Prediction";
    }

    @Override
    public Sheet.ColumnDefinition[] columns() {
        return new Sheet.ColumnDefinition[] {
                new Sheet.ColumnDefinition().width(80).readonly(true).id("sift.pred").name("SIFT_pred").type(Sheet.ColumnDefinition.ColumnType.STRING).styleText(new Sheet.StyleText().align(Sheet.Alignment.LEFT)),
                new Sheet.ColumnDefinition().width(80).readonly(true).id("sift.score").name("SIFT_score").type(Sheet.ColumnDefinition.ColumnType.STRING).styleText(new Sheet.StyleText().align(Sheet.Alignment.LEFT)),
                new Sheet.ColumnDefinition().width(80).readonly(true).id("polyphen.pred").name("PolyPhen_pred").type(Sheet.ColumnDefinition.ColumnType.STRING).styleText(new Sheet.StyleText().align(Sheet.Alignment.LEFT)),
                new Sheet.ColumnDefinition().width(80).readonly(true).id("polyphen.score").name("PolyPhen_score").type(Sheet.ColumnDefinition.ColumnType.STRING).styleText(new Sheet.StyleText().align(Sheet.Alignment.LEFT)),
                new Sheet.ColumnDefinition().width(80).readonly(true).id("mutationtaster.pred").name("MutationTaster_pred").type(Sheet.ColumnDefinition.ColumnType.STRING).styleText(new Sheet.StyleText().align(Sheet.Alignment.LEFT)),
                new Sheet.ColumnDefinition().width(80).readonly(true).id("mutationtaster.score").name("MutationTaster_score").type(Sheet.ColumnDefinition.ColumnType.STRING).styleText(new Sheet.StyleText().align(Sheet.Alignment.LEFT)),
                new Sheet.ColumnDefinition().width(80).readonly(true).id("ada_score").name("ada_score").type(Sheet.ColumnDefinition.ColumnType.STRING).styleText(new Sheet.StyleText().align(Sheet.Alignment.LEFT)),
                new Sheet.ColumnDefinition().width(80).readonly(true).id("cadd.pred").name("CADD_pred").type(Sheet.ColumnDefinition.ColumnType.STRING).styleText(new Sheet.StyleText().align(Sheet.Alignment.LEFT)),
                new Sheet.ColumnDefinition().width(80).readonly(true).id("dann.score").name("DANN_score").type(Sheet.ColumnDefinition.ColumnType.STRING).styleText(new Sheet.StyleText().align(Sheet.Alignment.LEFT)),
                new Sheet.ColumnDefinition().width(80).readonly(true).id("fathmm-mkl_coding.score").name("fathmm-MKL_coding_score").type(Sheet.ColumnDefinition.ColumnType.STRING).styleText(new Sheet.StyleText().align(Sheet.Alignment.LEFT)),
                new Sheet.ColumnDefinition().width(80).readonly(true).id("fathmm-mkl_coding.pred").name("fathmm-MKL_coding_pred").type(Sheet.ColumnDefinition.ColumnType.STRING).styleText(new Sheet.StyleText().align(Sheet.Alignment.LEFT)),
                new Sheet.ColumnDefinition().width(80).readonly(true).id("gerp++.rs").name("GERP++_RS").type(Sheet.ColumnDefinition.ColumnType.STRING).styleText(new Sheet.StyleText().align(Sheet.Alignment.LEFT)),
                new Sheet.ColumnDefinition().width(80).readonly(true).id("gerp++.gt2").name("GERP++gt2").type(Sheet.ColumnDefinition.ColumnType.STRING).styleText(new Sheet.StyleText().align(Sheet.Alignment.LEFT)),
                new Sheet.ColumnDefinition().width(80).readonly(true).id("phylop20way_mammalian").name("phyloP20way_mammalian").type(Sheet.ColumnDefinition.ColumnType.STRING).styleText(new Sheet.StyleText().align(Sheet.Alignment.LEFT)),
                new Sheet.ColumnDefinition().width(80).readonly(true).id("phastcons20way_mammalian").name("phastCons20way_mammalian").type(Sheet.ColumnDefinition.ColumnType.STRING).styleText(new Sheet.StyleText().align(Sheet.Alignment.LEFT)),
                new Sheet.ColumnDefinition().width(80).readonly(true).id("siphy_29way_logodds").name("SiPhy_29way_logOdds").type(Sheet.ColumnDefinition.ColumnType.STRING).styleText(new Sheet.StyleText().align(Sheet.Alignment.LEFT)),
                new Sheet.ColumnDefinition().width(80).readonly(true).id("apogee.pred").name("APOGEE").type(Sheet.ColumnDefinition.ColumnType.STRING).styleText(new Sheet.StyleText().align(Sheet.Alignment.LEFT)),
                new Sheet.ColumnDefinition().width(80).readonly(true).id("apogee.score").name("APOGEE Score").type(Sheet.ColumnDefinition.ColumnType.STRING).styleText(new Sheet.StyleText().align(Sheet.Alignment.LEFT))
        };
    }
    /*

rf_score
Codons
HGVSc
HGVSp
Exon
Qual
Strand
Region_info
rmsk
Function_description
n_mis|exp_mis
n_lof|exp_lof
mis_z
lof_z
pLI
gnomAD_exomes_AF_sas
ExAC_AF_EAS
AF_1000g_EAS
AF_1000g_SAS
ExAC_AF_SAS
FATHMM_score
FATHMM_pred
CADD_raw
CADD_phred
LRT_score
LRT_pred

     */
    @Override
    public Data map(JsPropertyMap<Object> map, Data data) {
        data.put("sift.pred", ColumnSet.unwrap(map.get("sift.pred")))
            .put("sift.score",ColumnSet.unwrap(map.get("sift.score")))
            .put("polyphen.pred", ColumnSet.unwrap(map.get("polyphen.pred")))
            .put("polyphen.score", ColumnSet.unwrap(map.get("polyphen.score")))
            .put("mutationtaster.pred", ColumnSet.unwrap(map.get("mutationtaster.pred")))
            .put("mutationtaster.score", ColumnSet.unwrap(map.get("mutationtaster.score")))
            .put("ada_score", (String)NullReplace.replaceWithBlankIfNull(map.get("ada_score")))
            .put("cadd.pred", (String)NullReplace.replaceWithBlankIfNull(map.get("cadd.pred")))
            .put("dann.score", (String)NullReplace.replaceWithBlankIfNull(map.get("dann.score")))
            .put("fathmm-mkl_coding.score", (String)NullReplace.replaceWithBlankIfNull(map.get("fathmm-mkl_coding.score")))
            .put("fathmm-mkl_coding.pred", ColumnSet.unwrap(map.get("fathmm-mkl_coding.pred")))
            .put("gerp++.rs", (String)NullReplace.replaceWithBlankIfNull(map.get("gerp++.rs")))
            .put("gerp++.gt2", (String)NullReplace.replaceWithBlankIfNull(map.get("gerp++.gt2")))
            .put("phylop20way_mammalian", (String)NullReplace.replaceWithBlankIfNull(map.get("phylop20way_mammalian")))
            .put("phastcons20way_mammalian", (String)NullReplace.replaceWithBlankIfNull(map.get("phastcons20way_mammalian")))
            .put("siphy_29way_logodds", (String)NullReplace.replaceWithBlankIfNull(map.get("siphy_29way_logodds")))
            .put("apogee.pred", (String)NullReplace.replaceWithBlankIfNull(map.get("apogee.pred")))
            .put("apogee.score", (String)NullReplace.replaceWithBlankIfNull(map.get("apogee.score")));
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
