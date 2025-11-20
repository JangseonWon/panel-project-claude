package com.greencross.lims.report.bloodcancer.kokr;

import com.greencross.lims.report.bloodcancer.AllTemplate;
import com.greencross.lims.report.bloodcancer.BloodCancerResource;
import com.greencross.lims.report.builder.LogoType;
import com.gcgenome.lims.test.bloodcancer.TestInfo;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
public class ALLTemplateKoKr extends BloodCancerTemplateKoKr implements AllTemplate {
    public ALLTemplateKoKr(BloodCancerResource resource, TestInfo testInfo, LogoType logoType) {
        super(resource, testInfo, logoType);
    }

    private final String lblPanelSResult			= "패널 결과";
    private final String lblPharmacogeneResult			= "약물유전자 결과";

    @Override
    public String lblPanelSResult() {
        return lblPanelSResult;
    }

    @Override
    public String lblPharmacogeneResult() {
        return lblPharmacogeneResult;
    }
}
