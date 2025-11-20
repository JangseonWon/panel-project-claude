package com.greencross.lims.report.solidtumor2;

import com.gcgenome.lims.report.func.PDPageContentStreamPageAccessible;
import com.gcgenome.lims.report.func.Painter;

import java.io.IOException;

public interface SectionQc extends Painter<SolidTumorTemplate, SolidTumorDto> {
    PDPageContentStreamPageAccessible qc(PDPageContentStreamPageAccessible stream, SolidTumorTemplate template, SolidTumorDto dto) throws IOException;
}
