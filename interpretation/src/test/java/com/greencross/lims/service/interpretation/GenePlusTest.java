package com.greencross.lims.service.interpretation;

import com.greencross.lims.dto.interpretation.PanelTest;
import org.junit.jupiter.api.Test;

class GenePlusTest {

    @Test
    void summary() {
        GenePlus genePlus = new GenePlus(null, null, null);
        PanelTest.Variant var1 = new PanelTest.Variant();
        PanelTest.Variant var2 = new PanelTest.Variant();
        PanelTest.Variant var3 = new PanelTest.Variant();
        PanelTest.Variant var4 = new PanelTest.Variant();
        PanelTest.Variant var5 = new PanelTest.Variant();
        PanelTest.Variant var6 = new PanelTest.Variant();
        PanelTest.Variant var7 = new PanelTest.Variant();
        var1.clazz("VUS").gene("BRCA1");
        var2.clazz("PV").gene("BRCA2");
        var3.clazz("PV").gene("BRCA3");
        var4.clazz("LPV").gene("BRCA4");
        var5.clazz("LPV").gene("Test");
        var6.clazz("VUS").gene("Test");
        var7.clazz("PV").gene("BRCA1");
        //System.out.println(genePlus.summary(List.of(var1, var2, var3, var4, var5, var6, var7)));
    }
}