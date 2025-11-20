package com.greencross.lims.report.bloodcancer.enus;

import com.greencross.lims.report.bloodcancer.AllTemplate;
import com.greencross.lims.report.bloodcancer.BloodCancerResource;
import com.greencross.lims.report.builder.LogoType;
import com.gcgenome.lims.test.bloodcancer.TestInfo;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
public class ALLTemplateEnUs extends BloodCancerTemplateEnUs implements AllTemplate {
    public ALLTemplateEnUs(BloodCancerResource resource, TestInfo testInfo, LogoType logoType) {
        super(resource, testInfo, logoType);
    }
    private final String lblPanelSResult			= "Panel Result";
    private final String lblPharmacogeneResult			= "Pharmacogene Result";

    @Override
    public String lblPanelSResult() {
        return lblPanelSResult;
    }

    @Override
    public String lblPharmacogeneResult() {
        return lblPharmacogeneResult;
    }
}
