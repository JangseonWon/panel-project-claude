package com.greencross.lims.report.genomescreen;

import com.gcgenome.lims.report.TextBlock;
import com.gcgenome.lims.report.TextStyle;
import com.gcgenome.lims.report.func.PDPageContentStreamPageAccessible;
import com.gcgenome.lims.report.func.Painter;

import java.awt.*;
import java.io.IOException;
import java.util.function.Supplier;

import static com.gcgenome.lims.report.func.AlignHorizontal.JUSTIFY;
import static com.gcgenome.lims.report.func.AlignHorizontal.RIGHT;
import static com.gcgenome.lims.report.func.AlignVertical.MIDDLE;

public class SectionSummaryRiskScreen implements Painter<RiskScreenTemplate<RiskScreenResource>, GenomeScreenWithRiskScreenDto> {
	public SectionSummaryRiskScreen(Supplier<Painter<RiskScreenTemplate<RiskScreenResource>, GenomeScreenWithRiskScreenDto>> table) {
		this.table = table.get();
	}
	@Override
	public PDPageContentStreamPageAccessible paint(PDPageContentStreamPageAccessible stream, RiskScreenTemplate<RiskScreenResource> template, GenomeScreenWithRiskScreenDto dto) throws IOException {
		return info().and(summary())
					 .and(table)
					 .and(interpretation())
					 .paint(stream, template, dto);
	}
	private final static float HEADER_HEIGHT = 20;
	private Painter<RiskScreenTemplate<RiskScreenResource>, GenomeScreenWithRiskScreenDto> info() {
		return (stream, template, dto) -> {
			stream.saveGraphicsState();
			var resource = template.resource();
			var y = stream.cursorY();
			y -= 20;
			TextStyle styleHeader = new TextStyle().color(Color.WHITE).fonts(resource.fontHeader(), resource.fontDefault()).fontSize(10).paragraph(true);
			TextStyle styleValue = new TextStyle().color(resource.colorText()).fonts(resource.fontText(), resource.fontDefault()).fontSize(8).justify(true).paragraph(true);
			TextBlock blockInfo = new TextBlock(styleValue, template.lblSummaryInfoRiskScreen());
			stream.setNonStrokingColor(resource.colorPrimary()).rect(50, y, 497, HEADER_HEIGHT).fill();
			// Util.icon(stream, resource.imgIcons()[0], 50, y, HEADER_HEIGHT, HEADER_HEIGHT);
			stream.paragraph(55, y-HEADER_HEIGHT/2, 497, MIDDLE, new TextBlock(styleHeader, template.lblSummaryTestIntroTitle()));
			stream.paragraph(250, y-HEADER_HEIGHT/2, 497, MIDDLE, new TextBlock(styleValue.clone().color(Color.WHITE), template.lblSummaryTestIntroTitleInfoRiskScreen()));
			y -= HEADER_HEIGHT;
			y -= 15;
			y = stream.paragraph(50, y, 497, JUSTIFY, blockInfo);
			y -= 10;
			stream.restoreGraphicsState();
			return stream.cursorY(y);
		};
	}
	private Painter<RiskScreenTemplate<RiskScreenResource>, GenomeScreenWithRiskScreenDto> summary() {
		return (stream, template, dto) -> {
			stream.saveGraphicsState();
			var resource = template.resource();
			var y = stream.cursorY();
			y -= 30;
			TextStyle styleHeader = new TextStyle().color(Color.WHITE).fonts(resource.fontHeader(), resource.fontDefault()).fontSize(10).paragraph(true);
			stream.setNonStrokingColor(resource.colorPrimary()).rect(50, y, 497, HEADER_HEIGHT).fill();
			//Util.icon(stream, resource.imgIcons()[7], 50, y, HEADER_HEIGHT, HEADER_HEIGHT);
			stream.paragraph(55, y-HEADER_HEIGHT/2, 497, MIDDLE, new TextBlock(styleHeader, template.lblSummaryTitle()));
			y -= HEADER_HEIGHT;
			y -= 2;
			stream.restoreGraphicsState();
			return stream.cursorY(y);
		};
	}
	private Painter<RiskScreenTemplate<RiskScreenResource>, GenomeScreenWithRiskScreenDto> table;
	private Painter<RiskScreenTemplate<RiskScreenResource>, GenomeScreenWithRiskScreenDto> interpretation() {
		return (stream, template, dto) -> {
			stream.saveGraphicsState();
			var resource = template.resource();
			var y = stream.cursorY();
			y -= 10;
			stream.setLineWidth(0.75f).setLineDashPattern(new float[] {1, 2}, 2)
				  .setStrokingColor(resource.colorDarkGray()).rect(50, y, 495, 80)
				  .stroke();
			y -= 13;
			TextStyle styleValue = new TextStyle().color(resource.colorText()).fonts(resource.fontText(), resource.fontDefault()).fontSize(8).paragraph(true);
			for(var disease: template.riskscreens()) {
				String interpretation = template.summary(disease, dto);
				if(interpretation == null || interpretation.trim().isEmpty()) continue;
				stream.paragraph(60, y, 497, RIGHT, new TextBlock(styleValue, "▶"));
				y = stream.paragraph(65, y, 475, JUSTIFY, new TextBlock(styleValue, interpretation));
				y -= 13;
			}
			stream.restoreGraphicsState();
			return stream.cursorY(y);
		};
	}
}
