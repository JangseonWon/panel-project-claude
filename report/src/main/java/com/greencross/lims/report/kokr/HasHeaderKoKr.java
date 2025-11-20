package com.greencross.lims.report.kokr;

import org.apache.pdfbox.pdmodel.font.PDFont;

import java.awt.*;

public interface HasHeaderKoKr {
    Color colorGray();
    Color colorText();

    PDFont fontHeader();
    PDFont fontHeaderValue();
    PDFont fontDefault();
}
