package com.greencross.lims.report.single;

import com.gcgenome.lims.report.TextBlock;
import com.gcgenome.lims.report.func.Painter;
import com.gcgenome.lims.test.single.TestInfo;

import static com.gcgenome.lims.report.func.AlignHorizontal.CENTER;
import static com.gcgenome.lims.report.func.AlignVertical.MIDDLE;

public class SingleWithMlpaPage extends SingleGenePage {
	public SingleWithMlpaPage(SingleGeneTemplate template, Painter<SingleGeneTemplate, SingleGeneDto> footer, Painter<SingleGeneTemplate, SingleGeneDto> sign, Painter<SingleGeneTemplate, SingleGeneDto> page) {
		super(template, footer, sign, page);
	}
	@Override
	protected Painter<SingleGeneTemplate, SingleGeneDto> variant() {
		return (stream, template, dto) -> {
			stream.saveGraphicsState();
			float y = 651;
			stream.setNonStrokingColor(colorPrimary)
					.setLineWidth(0.35f).setStrokingColor(colorPrimaryLine)
					.addRect(60,y,473, 20).fill()
					.line(60, y-20, 533, y-20).stroke();
			stream.paragraph(63, y+7, 115, new TextBlock(styleHeader, template.lblTestPerformed()));
			stream.paragraph(63, y-13, 500, new TextBlock(styleValue, template.testInfo().name()));

			y -= 46;
			stream.addRect(60, y,473, 20).fill().line(60, y-20, 533, y-20).stroke();
			stream.paragraph(63, y+7, 115, new TextBlock(styleHeader, template.lblReasonForRR()));
			stream.paragraph(63, y-13, 470, new TextBlock(styleValue, dto.reasonFR()));

			y -= 46;
			stream.addRect(60,y,65, 20).fill().line(60, y+20, 533, y+20).line(60, y, 533, y).stroke();
			stream.paragraph(63, y+7, 115, new TextBlock(styleHeader, template.lblResult()));
			stream.paragraph(135, y+7, 115,  new TextBlock(styleValue.clone().color(template.resultToColor(dto.result())), template.resultToString(dto.result())));
			stream = resultTable(dto).paint(stream, template, dto);
			y -= 55;
			cursorY(y);
			stream.restoreGraphicsState();
			if(dto.result() == SingleGeneDto.Result.N) stream = variantTableNegative().paint(stream, template, dto);
			else stream = variantTable(dto.report()).paint(stream, template, dto);
			stream = interpretation(dto.report()).paint(stream, template, dto);
			return stream;
		};
	}
	@Override
	protected Painter<SingleGeneTemplate, SingleGeneDto> method(TestInfo info) {
		return (stream, template, dto) -> {
			float y = cursorY();
			float height = 70;
			if(y - height < 130) {
				stream = newPage(stream);
				header().and(sign()).paint(stream, template, dto);
				y = cursorY();
			}
			y -= 40;
			stream.saveGraphicsState();
			float heightPenetrance = 0;
			if(info.penetrance() != null && !info.penetrance().trim().isEmpty()) heightPenetrance = 8 + stream.height(283,  new TextBlock(styleValue, info.penetrance()));
			stream.setNonStrokingColor(colorGray).addRect(60, y-71-heightPenetrance,180, 71+heightPenetrance).fill();
			stream.setLineWidth(0.5f).setStrokingColor(colorSecondaryLine)
					.line(240, y-71, 240, y).stroke();

			stream.setNonStrokingColor(colorPrimary).addRect(60, y,473, 20).fill();
			stream.paragraph(63, y+7, 500, new TextBlock(styleHeader, template.lblMethods()));

			y -= 20; stream.line(60, y, 533, y).paragraph(63, y+7, 180, new TextBlock(styleHeaderInfo, template.lblMethodList()[0]));
			stream.paragraph(250, y+7, 300, new TextBlock(styleValue, info.specimen()));
			y -= 20; stream.line(60, y, 533, y).paragraph(63, y+7, 180, new TextBlock(styleHeaderInfo, template.lblMethodList()[1]));
			stream.paragraph(250, y+7, 300, new TextBlock(styleValue, info.target()));
			y -= 20; stream.paragraph(63, y+7, 180, new TextBlock(styleHeaderInfo, template.lblMethodList()[2]));
			y = stream.paragraph(250, y+7, 283, new TextBlock(styleValue, info.method().replace(",",",\n")));
			y -= 7; stream.setLineWidth(0.5f).setStrokingColor(colorSecondaryLine).line(60, y, 533, y).stroke();
			if(info.penetrance() != null && !info.penetrance().trim().isEmpty()) {
				float top = y;
				y -= 20; stream.paragraph(63, y+7, 180, new TextBlock(styleHeaderInfo, template.lblMethodList()[3]));
				y = stream.paragraph(250, y+7, 283, new TextBlock(styleValue, info.penetrance()));
				y -= 7;
				stream.setLineWidth(0.5f).setStrokingColor(colorSecondaryLine)
						.line(60, y, 533, y)
						.line(240, y, 240, top).stroke();
			}
			stream.stroke();
			cursorY(y);
			stream.restoreGraphicsState();
			return stream;
		};
	}
	private Painter<SingleGeneTemplate, SingleGeneDto> variantTableNegative() {
		return (stream, template, dto) -> {
			stream.saveGraphicsState();
			float y = cursorY();
			y -= 10;
			stream.setNonStrokingColor(colorSecondary)
					.addRect(60, y-20,473, 20)
					.fill();
			y -= 10;
			stream.paragraph(80, y, 115, CENTER, MIDDLE, new TextBlock(styleHeaderTable, template.lblGene()[0]));
			stream.paragraph(140, y, 115, CENTER, MIDDLE, new TextBlock(styleHeaderTable, template.lblGene()[1]));
			stream.paragraph(220, y, 115, CENTER, MIDDLE, new TextBlock(styleHeaderTable, template.lblGene()[2]));
			stream.paragraph(295, y, 115, CENTER, MIDDLE, new TextBlock(styleHeaderTable, template.lblGene()[3]));
			stream.paragraph(375, y, 115, CENTER, MIDDLE, new TextBlock(styleHeaderTable, template.lblGene()[4]));
			stream.paragraph(455, y, 115, CENTER, MIDDLE, new TextBlock(styleHeaderTable, template.lblGene()[5]));
			stream.paragraph(507, y, 115, CENTER, MIDDLE, new TextBlock(styleHeaderTable, template.lblGene()[6]));
			y -= 25;
			stream.paragraph(295, y, 500, CENTER, new TextBlock(styleValueTable, template.lblNoDisease()));
			y -= 10;
			stream.setLineWidth(0.5f).setStrokingColor(colorPrimaryLine).line(60, y, 533, y).stroke();
			y -= 10;
			cursorY(y);
			stream.restoreGraphicsState();
			return stream;
		};
	}
	private Painter<SingleGeneTemplate, SingleGeneDto> resultTable(SingleGeneDto value) {
		return (stream, template, dto) -> {
			float y = cursorY();
			y -= 124;
			stream.saveGraphicsState();

			stream.setNonStrokingColor(colorGray).addRect(60, y-40,180, 40).fill();
			stream.setLineWidth(0.5f).setStrokingColor(colorSecondaryLine).line(240, y-40, 240, y).stroke();
			String sequencingResult = template.formatSequencingResult(value);
			y -= 20; stream.line(60, y, 533, y).paragraph(63, y+7, 180, new TextBlock(styleHeaderInfo, template.lblIndividualResults()[0]));
			stream.paragraph(250, y+7, 300, new TextBlock(styleValue.clone().color(template.individualResultToColor(sequencingResult)), sequencingResult));
			String mlpaResult = template.formatMlpaResult(value);
			y -= 20; stream.line(60, y, 533, y).paragraph(63, y+7, 180, new TextBlock(styleHeaderInfo, template.lblIndividualResults()[1]));
			stream.paragraph(250, y+7, 300, new TextBlock(styleValue.clone().color(template.individualResultToColor(mlpaResult)), mlpaResult));

			stream.stroke();
			cursorY(y);
			stream.restoreGraphicsState();
			return stream;
		};
	}
}
