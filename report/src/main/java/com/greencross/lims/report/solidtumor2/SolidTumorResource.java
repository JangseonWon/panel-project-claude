package com.greencross.lims.report.solidtumor2;

import com.gcgenome.lims.report.Resource;
import com.gcgenome.lims.report.TextStyle;
import com.greencross.lims.report.HasSign;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.awt.*;

public interface SolidTumorResource extends Resource, HasSign {
    PDFont fontDefault();
    PDFont fontTitle();
    PDFont fontHeader();
    PDFont fontText();

    default Color colorPrimary() {
        return Color.decode("0x2F5597");
    }
    default Color colorSecondary() {
        return Color.decode("0xC0D0EB");
    }
    default Color colorGray() {
        return Color.decode("0xEFEFEF");
    }
    default Color colorRed() { return new Color(135,51,61); }
    default Color colorText() {
        return Color.decode("0x484848");
    }
    default Color colorTextWithPrimary() {
        return Color.WHITE;
    }
    default Color colorTextWithSecondary() {
        return colorText();
    }

    default TextStyle styleHeaderTitle() { return new TextStyle().color(colorText()).fonts(fontHeader(), fontText(), fontDefault()).fontSize(9).paragraph(false); }
    default TextStyle styleHeader() { return new TextStyle().color(colorTextWithPrimary()).fonts(fontHeader(), fontText(), fontDefault()).fontSize(9).justify(false); }
    default TextStyle styleHeader2() { return new TextStyle().color(colorTextWithPrimary()).fonts(fontHeader(), fontTitle(), fontDefault()).fontSize(12); }
    default TextStyle styleHeader3() { return new TextStyle().color(colorPrimary()).fonts(fontTitle(), fontDefault()).fontSize(11); }
    default TextStyle styleHeader4() { return new TextStyle().color(colorText()).fonts(fontHeader(), fontTitle(), fontDefault()).fontSize(9); }

    default TextStyle styleValue() { return new TextStyle().color(colorText()).fonts(fontText(), fontDefault()).fontSize(7).paragraph(false); }
    default TextStyle styleValueTable() { return new TextStyle().color(colorText()).fonts(fontText(), fontDefault()).fontSize(8).paragraph(false); }
    default TextStyle styleText() { return new TextStyle().color(colorText()).fonts(fontText(), fontDefault()).fontSize(7).justify(true).paragraph(true); }
    PDImageXObject medal();
}
