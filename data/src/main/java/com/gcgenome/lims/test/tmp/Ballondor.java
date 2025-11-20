package com.gcgenome.lims.test.tmp;

import com.gcgenome.lims.test.HasCode;
import com.gcgenome.lims.test.HasGenes;
import com.gcgenome.lims.test.I18N;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
@Builder
public class Ballondor implements HasCode, HasGenes, I18N {
    private final String code;
    private final String name;
    private final String title;
    @Builder.Default
    private final String disease = "Waldenstrom Macroglobulinemia (WM)";
    @Builder.Default
    private final String method = "Droplet Digital PCR";
    @Builder.Default
    private final String target;
    @Builder.Default
    private final String resolution = "VAF ≥0.1%";
    @Builder.Default
    private final boolean isNationalInsuranceTest = true;
    private final String[] hgvsc;
    private final String hgvsp;
    private final String gene;
    @Builder.Default
    private final String i18n = "KOKR";
    public final static Ballondor T033 = Ballondor.builder().code("T033")
            .name("BALLONDOR Study (MYD88)")
            .title("BALLONDOR Study (MYD88) 결과보고서")
            .target("L265P at MYD88 on 3p22.2")
            .gene("MYD88")
            .hgvsc(new String[]{"c.764T>C"})
            .hgvsp("p.Leu265Pro").build();
    public final static Ballondor T034 = Ballondor.builder().code("T034")
            .name("BALLONDOR Study (CXCR4)")
            .title("BALLONDOR Study (CXCR4) 결과보고서")
            .target("S338X at CXCR4 on 2p22.1")
            .gene("CXCR4")
            .hgvsc(new String[] {"c.1013C>A", "c.1013C>G"})
            .hgvsp("p.Ser342Ter").build();
    @Override
    public String[] genes() {
        return new String[] { gene };
    }
    public static Ballondor[] TESTS = new Ballondor[] {
            T033, T034
    };
    @Getter
    public enum SampleType {
        SERUM("Serum", "cfDNA isolated from Serum"),
        WB("WB", "Genomic DNA isolated from Whole Blood"),
        PLASMA("Plasma", "cfDNA isolated from Plasma"),
        _24H_URINE("24h Urine", "Genomic DNA isolated from 24h Urine"),
        RANDOM_URINE("Random Urine", "Genomic DNA isolated from Random Urine"),
        SPUTUM("Sputum", "Genomic DNA isolated from Sputum"),
        STOOL("Stool", "Genomic DNA isolated from Stool"),
        AF("AF", "Genomic DNA isolated from Amniotic Fluid"),
        ASCITIC_FLUID("Ascitic Fluid", "Genomic DNA isolated from Ascitic Fluid"),
        FLUID_OTHER("Fluid (Other)", "Genomic DNA isolated from Fluid (Other)"),
        BM("BM", "Genomic DNA isolated from Bone Marrow"),
        BRONCHIAL_BRUSH("Bronchial Brush", "Genomic DNA isolated from Bronchial Brush"),
        BRONCHIAL_WASHING("Bronchial Washing", "Genomic DNA isolated from Bronchial Washing"),
        CSF("CSF", "Genomic DNA isolated from CSF,(Cerebrospinal Fluid)"),
        CVS("CVS", "Genomic DNA isolated from CVS,(Chorionic Villi Sampling)"),
        DUODENAL_JUICE("Duodenal Juice", "Genomic DNA isolated from Duodenal Juice"),
        GASTRIC_JUICE("Gastric Juice", "Genomic DNA isolated from Gastric Juice"),
        JOINT_FLUID("Joint Fluid", "Genomic DNA isolated from Joint Fluid"),
        CERVICO_VAGINAL_SMEAR_("Cervico-vaginal smear ", "Genomic DNA isolated from Cervico-vaginal smear "),
        PERICARDIAL_FLUID("Pericardial Fluid", "Genomic DNA isolated from Pericardial Fluid"),
        PERIPHERAL_BLOOD("Peripheral Blood", "Genomic DNA isolated from Peripheral Blood"),
        PLEURAL_FLUID("Pleural Fluid", "Genomic DNA isolated from Pleural Fluid"),
        PUS("Pus", "Genomic DNA isolated from Pus"),
        SALIVA("Saliva", "Genomic DNA isolated from Saliva"),
        SEMEN("Semen", "Genomic DNA isolated from Semen"),
        BREATH("Breath", "Genomic DNA isolated from Breath"),
        FFPE("FFPE", "Genomic DNA isolated from 파라핀 Block"),
        SLIDE("Slide", "Genomic DNA isolated from Slide"),
        세침_천자_흡인검체("세침(천자)흡인검체", "Genomic DNA isolated from 세침(천자)흡인검체"),
        TISSUE("Tissue", "Genomic DNA isolated from Tissue"),
        VAGINAL_DISCHARGE("Vaginal Discharge", "Genomic DNA isolated from Vaginal Discharge"),
        ABORTUS("Abortus", "Genomic DNA isolated from Abortus"),
        스카치테이프_SLIDE("스카치테이프 Slide", "Genomic DNA isolated from 스카치테이프 Slide"),
        환자("환자", "Genomic DNA isolated from 환자"),
        SERUM_24H_URINE("Serum+24h Urine", "Genomic DNA isolated from Serum+24h Urine"),
        SERUM_RANDOM_URINE("Serum+Random Urine", "Genomic DNA isolated from Serum+Random Urine"),
        전용용기("전용용기", "Genomic DNA isolated from 전용용기"),
        환부_SWAB("환부 Swab", "Genomic DNA isolated from 환부 Swab"),
        제단백액_NAF_PLASMA("제단백액+NaF Plasma", "Genomic DNA isolated from 제단백액+NaF Plasma"),
        CORD_BLOOD("Cord Blood", "Genomic DNA isolated from Cord Blood"),
        _24H_STOOL("24h Stool", "Genomic DNA isolated from 24h Stool"),
        BAL_BRONCHOALVEOLAR_LAVAGE("BAL,(Bronchoalveolar Lavage)", "Genomic DNA isolated from BAL,(Bronchoalveolar Lavage)"),
        DNA("DNA", "Genomic DNA isolated from DNA"),
        세포주("세포주", "Genomic DNA isolated from 세포주"),
        ENDOMETRIUM_SMEAR("Endometrium smear", "Genomic DNA isolated from Endometrium smear"),
        VOIDED_URINE("Voided urine", "Genomic DNA isolated from Voided urine"),
        STONE("Stone", "Genomic DNA isolated from Stone"),
        SERUM_CSF("Serum+CSF", "Genomic DNA isolated from Serum+CSF"),
        PLATELET_FREE_PLASMA("Platelet Free Plasma", "Genomic DNA isolated from Platelet Free Plasma"),
        PLATELET("Platelet", "Genomic DNA isolated from Platelet"),
        PROSTATIC_JUICE("Prostatic Juice", "Genomic DNA isolated from Prostatic Juice"),
        BILE_JUICE("Bile Juice", "Genomic DNA isolated from Bile Juice"),
        바이러스_전용_배지("바이러스 전용 배지", "Genomic DNA isolated from 바이러스 전용 배지"),
        CATHETER_TIP("Catheter Tip", "Genomic DNA isolated from Catheter Tip"),
        SERUM_STOOL("Serum+Stool", "Genomic DNA isolated from Serum+Stool"),
        THROAT_SWAB("Throat Swab", "Genomic DNA isolated from Throat Swab"),
        PUS_OPEN("Pus (Open)", "Genomic DNA isolated from Pus (Open)"),
        PUS_CLOSED("Pus (Closed)", "Genomic DNA isolated from Pus (Closed)"),
        WOUND("Wound", "Genomic DNA isolated from Wound"),
        CERVIX_SWAB("Cervix Swab", "Genomic DNA isolated from Cervix Swab"),
        RECTAL_SWAB("Rectal Swab", "Genomic DNA isolated from Rectal Swab"),
        OTHERS("Others", "Genomic DNA isolated from Others"),
        비인후흡입액("비인후흡입액", "Genomic DNA isolated from 비인후흡입액"),
        비인후_인후도찰물("비인후/인후도찰물", "Genomic DNA isolated from 비인후/인후도찰물"),
        정수액("정수액", "Genomic DNA isolated from 정수액"),
        HAIR("Hair", "Genomic DNA isolated from Hair"),
        URINE_PAPER("Urine Paper", "Genomic DNA isolated from Urine Paper"),
        BLOOD_PAPER("Blood Paper", "Genomic DNA isolated from Blood Paper"),
        THYROID_ASPIRATION("Thyroid Aspiration", "Genomic DNA isolated from Thyroid Aspiration"),
        LYMPH_NODE_ASPIRATION("Lymph Node Aspiration", "Genomic DNA isolated from Lymph Node Aspiration"),
        BREAST_ASPIRATION("Breast Aspiration", "Genomic DNA isolated from Breast Aspiration"),
        OVARIAN_CYST_ASPIRATION("Ovarian Cyst Aspiration", "Genomic DNA isolated from Ovarian Cyst Aspiration"),
        CULDOCENTESIS_FLUID("Culdocentesis Fluid", "Genomic DNA isolated from Culdocentesis Fluid"),
        천자흡인검체("천자흡인검체", "Genomic DNA isolated from 천자흡인검체"),
        균주("균주", "Genomic DNA isolated from 균주"),
        투석액("투석액", "Genomic DNA isolated from 투석액"),
        유리체("유리체", "Genomic DNA isolated from 유리체"),
        구강_SWAB("구강 Swab", "Genomic DNA isolated from 구강 Swab"),
        TISSUE_DNA("Tissue DNA", "Tissue DNA"),
        CTC_DNA("CTC DNA", "CTC DNA"),
        CF_DNA("CF DNA", "CF DNA"),
        RNA("RNA", "Genomic DNA isolated from RNA"),
        PCR_PRODUCT("PCR Product", "Genomic DNA isolated from PCR Product"),
        UNSTAINED_SLIDE("Unstained slide", "Genomic DNA isolated from Unstained slide");
        private String id;
        private String info;
        SampleType(String id, String info) {
            this.id = id;
            this.info = info;
        }
    }
}
