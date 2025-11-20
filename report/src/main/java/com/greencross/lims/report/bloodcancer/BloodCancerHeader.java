package com.greencross.lims.report.bloodcancer;

import com.gcgenome.lims.report.func.PDPageContentStreamPageAccessible;
import com.gcgenome.lims.report.func.Painter;

import java.io.IOException;

public interface BloodCancerHeader extends Painter<BloodCancerTemplate, BloodCancerDto> {
    PDPageContentStreamPageAccessible paint(PDPageContentStreamPageAccessible stream, BloodCancerTemplate template, BloodCancerDto dto) throws IOException;
    default String dashIfEmpty(String str) {
        if(str == null || str.trim().isEmpty()) return "-";
        else return str.trim();
    }
}
