package com.greencross.lims.report.genomescreen;

import com.gcgenome.lims.report.TextBlock;
import com.gcgenome.lims.report.TextStyle;
import com.greencross.lims.report.builder.Util;
import com.gcgenome.lims.report.func.PDPageContentStreamPageAccessible;
import com.gcgenome.lims.report.func.Painter;
import org.apache.pdfbox.pdmodel.graphics.state.PDExtendedGraphicsState;

import java.awt.*;
import java.io.IOException;

import static com.gcgenome.lims.report.func.AlignHorizontal.CENTER;
import static com.gcgenome.lims.report.func.AlignHorizontal.RIGHT;
import static com.gcgenome.lims.report.func.AlignVertical.BOTTOM;
import static com.gcgenome.lims.report.func.AlignVertical.MIDDLE;

public class SectionLifestyleRiskScreen implements Painter<RiskScreenTemplate<RiskScreenResource>, GenomeScreenWithRiskScreenDto> {
	private final static float HEADER_HEIGHT = 20;
	@Override
	public PDPageContentStreamPageAccessible paint(PDPageContentStreamPageAccessible stream, RiskScreenTemplate<RiskScreenResource> template, GenomeScreenWithRiskScreenDto dto) throws IOException {
		stream = header(stream, template);
		stream = table(stream, template, dto);
		return stream;
	}
	private PDPageContentStreamPageAccessible header(PDPageContentStreamPageAccessible stream, RiskScreenTemplate<RiskScreenResource> template) throws IOException {
		stream.saveGraphicsState();
		var resource = template.resource();
		var y = stream.cursorY();
		y -= 20;
		TextStyle styleHeader = new TextStyle().color(Color.WHITE).fonts(resource.fontHeader(), resource.fontDefault()).fontSize(10).paragraph(true);
		TextStyle styleValue = new TextStyle().color(Color.WHITE).fonts(resource.fontText(), resource.fontDefault()).fontSize(8).justify(true).paragraph(true);
		stream.setNonStrokingColor(resource.colorPrimary()).rect(50, y, 497, HEADER_HEIGHT).fill();
		// Util.icon(stream, resource.imgIcons()[3], 50, y, HEADER_HEIGHT, HEADER_HEIGHT);
		stream.paragraph(55, y-HEADER_HEIGHT/2, 497, MIDDLE, new TextBlock(styleHeader, template.lblLifestyleTitle()));
		stream.paragraph(250, y-HEADER_HEIGHT/2, 297, MIDDLE, new TextBlock(styleValue, template.lblLifestyleInfo()));
		y -= HEADER_HEIGHT;
		stream.cursorY(y);
		stream.restoreGraphicsState();
		return stream.cursorY(y);
	}

	private final static float RISK_FACTOR_WIDTH = 180;
	private final static float RISK_FACTOR_HEIGHT = 40;
	private final static float RISK_FACTOR_VALUE_WIDTH = 497-RISK_FACTOR_WIDTH;
	private final static float RISK_FACTOR_VALUE_CENTER = 50+RISK_FACTOR_WIDTH+RISK_FACTOR_VALUE_WIDTH/2;
	private PDPageContentStreamPageAccessible table(PDPageContentStreamPageAccessible stream, RiskScreenTemplate<RiskScreenResource> template, GenomeScreenWithRiskScreenDto dto) throws IOException {
		stream.saveGraphicsState();
		var resource = template.resource();
		var y = stream.cursorY();
		y -= 25;
		TextStyle styleHeader = new TextStyle().color(resource.colorText()).fonts(resource.fontHeader(), resource.fontDefault()).fontSize(9).paragraph(true);
		TextStyle styleValue = new TextStyle().color(Color.WHITE).fonts(resource.fontText(), resource.fontDefault()).fontSize(12).justify(true).paragraph(true);
		stream.paragraph(50, y, 497, BOTTOM, new TextBlock(styleHeader, template.lblLifestyleInfo2()));
		y -= 5;
		int n = template.riskfactors().length;
		stream.setNonStrokingColor(resource.colorGray()).setLineWidth(0.25f)
			  .rect(50, y, 497, RISK_FACTOR_HEIGHT*2)
			  .rect(50, y-RISK_FACTOR_HEIGHT*2, RISK_FACTOR_WIDTH, RISK_FACTOR_HEIGHT*n).fill();
		Color color = null;
		if(template.isLifestyleResultPositive(dto)) color = resource.colorWarn();
		else color = resource.colorSafe();
		stream.paragraph(45+RISK_FACTOR_WIDTH, y-RISK_FACTOR_HEIGHT/2+5, RISK_FACTOR_WIDTH, RIGHT, MIDDLE, new TextBlock(styleHeader.clone().fontSize(12), template.lblLifestyleDisease()));
		stream.paragraph(55, y-RISK_FACTOR_HEIGHT/2-5, RISK_FACTOR_WIDTH, MIDDLE, new TextBlock(styleHeader.clone().fontSize(12), template.lblLifestyle()));
		stream.paragraph(RISK_FACTOR_VALUE_CENTER, y-RISK_FACTOR_HEIGHT/2, 497, CENTER, MIDDLE, new TextBlock(styleHeader.clone().fontSize(12), template.lblLifestyleResultTitle()));
		stream.paragraph(50+RISK_FACTOR_WIDTH/2, y-RISK_FACTOR_HEIGHT-RISK_FACTOR_HEIGHT/2, RISK_FACTOR_WIDTH, CENTER, MIDDLE, new TextBlock(styleHeader.clone().fontSize(12), template.lblLifestyleResultHeader(dto)));
		stream.paragraph(RISK_FACTOR_VALUE_CENTER, y-RISK_FACTOR_HEIGHT-RISK_FACTOR_HEIGHT/2, 497, CENTER, MIDDLE, new TextBlock(styleHeader.clone().color(color).fontSize(12), template.lblLifestyleResultValue(dto)));
		for(int i = 0; i < n; ++i) {
			RiskScreenTemplate.RiskFactor risk = template.riskfactors()[i];
			float alpha = (float) (risk.risk() / 3);
			stream.saveGraphicsState();
			PDExtendedGraphicsState graphicsState = new PDExtendedGraphicsState();
			graphicsState.setNonStrokingAlphaConstant(alpha);
			stream.setGraphicsStateParameters(graphicsState);
			stream.setNonStrokingColor(resource.colorWarn())
				  .rect(50+RISK_FACTOR_WIDTH, y-RISK_FACTOR_HEIGHT*(2+i), RISK_FACTOR_VALUE_WIDTH, RISK_FACTOR_HEIGHT)
				  .fill();
			stream.restoreGraphicsState();
			stream.setNonStrokingColor(resource.colorText()).setNonStrokingColor(Color.WHITE)
				  .rect(55, y-RISK_FACTOR_HEIGHT*(2+i)-RISK_FACTOR_HEIGHT/2+6, 12, 12).fill()
				  .rect(55, y-RISK_FACTOR_HEIGHT*(2+i)-RISK_FACTOR_HEIGHT/2+6, 12, 12).stroke();
			Util.icon(stream, resource.icon(risk), 73, y-RISK_FACTOR_HEIGHT*(2+i)-RISK_FACTOR_HEIGHT/2+13, 26, 26);
			stream.paragraph(104, y-RISK_FACTOR_HEIGHT*(2+i)-RISK_FACTOR_HEIGHT/2, 497, MIDDLE, new TextBlock(styleHeader.clone().fontSize(12), template.toString(risk)));
			stream.paragraph(RISK_FACTOR_VALUE_CENTER, y-RISK_FACTOR_HEIGHT*(2+i)-RISK_FACTOR_HEIGHT/2, 497, CENTER, MIDDLE, new TextBlock(styleValue, template.risk(risk.risk())));
		}
		stream.setStrokingColor(resource.colorDarkGray()).setLineWidth(0.25f)
			  .rect(50, y, 497, RISK_FACTOR_HEIGHT*(2+n))
			  .line(50+RISK_FACTOR_WIDTH, y, 50+RISK_FACTOR_WIDTH, y-RISK_FACTOR_HEIGHT*(2+n))
			  .line(50, y-RISK_FACTOR_HEIGHT, 547, y-RISK_FACTOR_HEIGHT)
			  .line(50, y, 50+RISK_FACTOR_WIDTH, y-RISK_FACTOR_HEIGHT);
		for(int i = 0; i < n; ++i) stream.line(50, y-RISK_FACTOR_HEIGHT*(2+i), 547, y-RISK_FACTOR_HEIGHT*(2+i));
		stream.stroke();
		/*y -= HEADER_HEIGHT;
		y -= 20;*/
		stream.cursorY(y);
		stream.restoreGraphicsState();
		return stream.cursorY(y);
	}
}
