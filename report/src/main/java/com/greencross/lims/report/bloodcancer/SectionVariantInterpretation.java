package com.greencross.lims.report.bloodcancer;

import com.gcgenome.lims.report.TextBlock;
import com.gcgenome.lims.report.TextStyle;
import com.gcgenome.lims.report.func.AlignHorizontal;
import com.gcgenome.lims.report.func.PDPageContentStreamPageAccessible;
import com.gcgenome.lims.report.func.Painter;

import java.awt.*;
import java.io.IOException;

import static com.gcgenome.lims.report.func.AlignHorizontal.CENTER;
import static com.gcgenome.lims.report.func.AlignVertical.MIDDLE;

public class SectionVariantInterpretation implements Painter<BloodCancerTemplate, BloodCancerDto> {
    @Override
    public PDPageContentStreamPageAccessible paint(PDPageContentStreamPageAccessible stream, BloodCancerTemplate template, BloodCancerDto dto) throws IOException {
        BloodCancerResource resource = template.resource();
        Color colorPrimary = resource.colorPrimary();
        Color colorSecondary = resource.colorSecondary();
        Color colorGray = resource.colorGray();
        TextStyle styleHeaderTitle = new TextStyle().color(resource.colorText()).fonts(resource.fontHeader(), resource.fontText(), resource.fontDefault()).fontSize(9).justify(true).paragraph(false);
        TextStyle styleHeader3 = new TextStyle().color(colorPrimary).fonts(resource.fontTitle()).fontSize(11);
        TextStyle styleText = new TextStyle().color(resource.colorText()).fonts(resource.fontText(), resource.fontDefault()).fontSize(7).justify(true).paragraph(true);

        float y = stream.cursorY();
        stream.saveGraphicsState();
        y -= 30;
        stream.paragraph(60, y, 120, new TextBlock(styleHeader3, template.lblTestVariantCategorizations()));
        y -= 20;

        stream.setNonStrokingColor(colorGray).addRect(60, y + 10, 473, -20).fill();
        float fontSize = "KOKR".equalsIgnoreCase(template.testInfo().i18n()) ? 9 : 7;
        stream.paragraph(66, y, 470, MIDDLE, new TextBlock(styleHeader3.clone().fontSize(fontSize), template.lblTestVariantCategorizationDetails()));
        y -= 10;
        stream.setLineWidth(1).setStrokingColor(colorSecondary)
                .line(60, y, 533, y)
                .line(60, y + 20, 533, y + 20).stroke();
        stream.setNonStrokingColor(colorGray).addRect(60, y - 1, 60, -160).fill();

        y -= 20;
        TextStyle styleHeader = styleHeaderTitle.clone().paragraph(false).justify(false);
        stream.setLineWidth(0.5f);
        for(BloodCancerDto.Tier tier: BloodCancerDto.Tier.values()) {
            if(tier!=BloodCancerDto.Tier.Tier1) stream.setStrokingColor(Color.WHITE).line(60, y+20, 120, y+20).stroke()
                    .setStrokingColor(colorGray).line(120, y+20, 533, y+20).stroke();
            stream.paragraph(90, y, 60, CENTER, MIDDLE, new TextBlock(styleHeader, template.lblTier(tier)));
            stream.paragraph(130, y, 120, MIDDLE, new TextBlock(styleText, template.lblTierSignificanceCategory(tier)));
            stream.paragraph(260, y, 270, MIDDLE, new TextBlock(styleText, template.lblTierSignificanceDetails(tier)));
            y -= 40;
        }
        stream.setLineWidth(1).setStrokingColor(colorSecondary).line(60, y+19, 533, y+19).stroke();

        stream.setNonStrokingColor(colorGray).addRect(60, y+10, 473, -20).fill();
        stream.paragraph(65, y, 470, MIDDLE, new TextBlock(styleHeader3.fontSize(9), template.lblEvidenceLevel()));
        y -= 10;
        stream.setLineWidth(1).setStrokingColor(colorSecondary)
                .line(60, y, 533, y)
                .line(60, y+20, 533, y+20).stroke();
        stream.setNonStrokingColor(colorGray).addRect(60, y-1, 60, -160).fill();
        y -= 20;
        stream.setLineWidth(0.5f);
        for(BloodCancerDto.EvidenceLevel evidenceLevel: BloodCancerDto.EvidenceLevel.values()) {
            if(evidenceLevel!=BloodCancerDto.EvidenceLevel.LevelA)
                stream.setStrokingColor(Color.WHITE).line(60, y+20, 120, y+20).stroke()
                        .setStrokingColor(colorGray).line(120, y+20, 533, y+20).stroke();
            stream.paragraph(90, y, 60, CENTER, MIDDLE, new TextBlock(styleHeader, template.lblEvidenceLevel(evidenceLevel)));
            stream.paragraph(130, y, 400, MIDDLE, new TextBlock(styleText, template.lblEvidenceLevelDetails(evidenceLevel)));
            y -= 40;
        }
        stream.setLineWidth(1).setStrokingColor(colorSecondary).line(60, y+19, 533, y+19).stroke();

        stream.setNonStrokingColor(colorGray).addRect(60, y+10, 473, -20).fill();
        stream.paragraph(65, y, 470, MIDDLE, new TextBlock(styleHeader3.clone().fontSize(9), template.lblReference()));
        y -= 10;
        stream.setLineWidth(1).setStrokingColor(colorSecondary)
                .line(60, y, 533, y)
                .line(60, y+20, 533, y+20).stroke();
        TextStyle styleTextReference = styleText.clone().justify(true).paragraph(true);
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
