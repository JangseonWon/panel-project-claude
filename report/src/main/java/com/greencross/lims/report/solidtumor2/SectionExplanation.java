package com.greencross.lims.report.solidtumor2;

import com.gcgenome.lims.report.TextBlock;
import com.gcgenome.lims.report.TextStyle;
import com.gcgenome.lims.report.func.AlignHorizontal;
import com.gcgenome.lims.report.func.PDPageContentStreamPageAccessible;
import com.gcgenome.lims.report.func.Painter;

import java.awt.*;
import java.io.IOException;

import static com.gcgenome.lims.report.func.AlignHorizontal.CENTER;
import static com.gcgenome.lims.report.func.AlignVertical.MIDDLE;

public class SectionExplanation implements Painter<SolidTumorTemplate, SolidTumorDto> {
    @Override
    public PDPageContentStreamPageAccessible paint(PDPageContentStreamPageAccessible stream, SolidTumorTemplate template, SolidTumorDto dto) throws IOException {
        float y = stream.cursorY();
        stream.saveGraphicsState();
        var resource = template.resource();

        stream.paragraph(60, y, 120, new TextBlock(resource.styleHeader3(), template.lblTestVariantCategorizations()));
        y -= 10;

        TextBlock lblDetails = new TextBlock(resource.styleHeader3().clone().fontSize(9), template.lblTestVariantCategorizationDetails());
        float height = stream.height(470, lblDetails);
        stream.setNonStrokingColor(resource.colorGray()).addRect(60, y, 473, -height-10).fill()
                .setLineWidth(1).setStrokingColor(resource.colorSecondary())
                .line(60, y, 533, y)
                .line(60, y-height-10, 533, y-height-10).stroke();
        y = stream.paragraph(63, y-(height/2)-5, 470, MIDDLE, lblDetails);
        y -= 8;
        stream.setNonStrokingColor(resource.colorGray()).addRect(60, y-1, 60, -160).fill();
        y -= 20;
        TextStyle styleHeader = resource.styleHeaderTitle().clone().paragraph(false).justify(false);
        stream.setLineWidth(0.5f);
        for(SolidTumorDto.Tier tier: SolidTumorDto.Tier.values()) {
            if(tier!= SolidTumorDto.Tier.Tier1) stream.setStrokingColor(Color.WHITE).line(60, y+20, 120, y+20).stroke()
                    .setStrokingColor(resource.colorGray()).line(120, y+20, 533, y+20).stroke();
            stream.paragraph(90, y, 60, CENTER, MIDDLE, new TextBlock(styleHeader, template.lblTier(tier)));
            stream.paragraph(130, y, 120, MIDDLE, new TextBlock(resource.styleText(), template.lblTierSignificanceCategory(tier)));
            stream.paragraph(260, y, 270, MIDDLE, new TextBlock(resource.styleText(), template.lblTierSignificanceDetails(tier)));
            y -= 40;
        }
        stream.setLineWidth(1).setStrokingColor(resource.colorSecondary()).line(60, y+19, 533, y+19).stroke();

        stream.setNonStrokingColor(resource.colorGray()).addRect(60, y+10, 473, -20).fill();
        stream.paragraph(63, y, 470, MIDDLE, new TextBlock(resource.styleHeader3().clone().fontSize(9), template.lblEvidenceLevel()));
        y -= 10;
        stream.setLineWidth(1).setStrokingColor(resource.colorSecondary())
                .line(60, y, 533, y)
                .line(60, y+20, 533, y+20).stroke();
        stream.setNonStrokingColor(resource.colorGray()).addRect(60, y-1, 60, -160).fill();
        y -= 20;
        stream.setLineWidth(0.5f);
        for(SolidTumorDto.EvidenceLevel evidenceLevel: SolidTumorDto.EvidenceLevel.values()) {
            if(evidenceLevel!= SolidTumorDto.EvidenceLevel.LevelA)
                stream.setStrokingColor(Color.WHITE).line(60, y+20, 120, y+20).stroke()
                        .setStrokingColor(resource.colorGray()).line(120, y+20, 533, y+20).stroke();
            stream.paragraph(90, y, 60, CENTER, MIDDLE, new TextBlock(styleHeader, template.lblEvidenceLevel(evidenceLevel)));
            stream.paragraph(130, y, 400, MIDDLE, new TextBlock(resource.styleText(), template.lblEvidenceLevelDetails(evidenceLevel)));
            y -= 40;
        }
        stream.setLineWidth(1).setStrokingColor(resource.colorSecondary()).line(60, y+19, 533, y+19).stroke();

        stream.setNonStrokingColor(resource.colorGray()).addRect(60, y+10, 473, -20).fill();
        stream.paragraph(63, y, 470, MIDDLE, new TextBlock(resource.styleHeader3().clone().fontSize(9), template.lblReference()));
        y -= 10;
        stream.setLineWidth(1).setStrokingColor(resource.colorSecondary())
                .line(60, y, 533, y)
                .line(60, y+20, 533, y+20).stroke();
        TextStyle styleTextReference = resource.styleText().clone().justify(true).paragraph(true);
        for(String reference: template.lblReferences()) {
            y -= 15;
            stream.paragraph(63, y+1, 10, new TextBlock(styleTextReference.clone().fontSize(4), "●"));
            y = stream.paragraph(70, y, 460, AlignHorizontal.JUSTIFY, new TextBlock(styleTextReference, reference));
        }
        stream.cursorY(y);
        stream.restoreGraphicsState();
        return stream;
    }
}
