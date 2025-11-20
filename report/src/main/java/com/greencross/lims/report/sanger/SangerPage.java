package com.greencross.lims.report.sanger;

import com.gcgenome.lims.report.TextBlock;
import com.gcgenome.lims.report.TextStyle;
import com.gcgenome.lims.report.func.Page;
import com.gcgenome.lims.report.func.Painter;
import com.gcgenome.lims.test.sanger.TestInfo;
import com.greencross.lims.report.SectionBarcode;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.awt.*;
import java.text.NumberFormat;

import static com.gcgenome.lims.report.func.AlignHorizontal.CENTER;
import static com.gcgenome.lims.report.func.AlignHorizontal.JUSTIFY;
import static com.gcgenome.lims.report.func.AlignVertical.MIDDLE;

public class SangerPage extends Page<SangerTemplate> {
	private final Painter<SangerTemplate, SangerDto> barcode = new SectionBarcode<>();
	private final Painter<SangerTemplate, SangerDto> ldt;
	private final Painter<SangerTemplate, SangerDto> footer;
	private final Painter<SangerTemplate, SangerDto> sign;
	private final Painter<SangerTemplate, SangerDto> pn;
	private final SangerHeader header;
	private Color colorPrimary;
	private Color colorPrimaryLine;
	private Color colorSecondaryLine;
	private Color colorGray;
	private TextStyle styleHeaderTitle;
	private TextStyle styleHeader;			// Test Performed, Reason for referral, ...
	private TextStyle styleHeaderTable;		// 변이 테이블의 헤더
	private TextStyle styleHeaderInfo;		// Info 페이지의 항목 헤더
	private TextStyle styleValue;
	private TextStyle styleValueTable;
	private TextStyle styleText;
	private TextStyle styleTextSub;
	public SangerPage(SangerTemplate template, SangerHeader header, Painter<SangerTemplate, SangerDto> ldt, Painter<SangerTemplate, SangerDto> footer, Painter<SangerTemplate, SangerDto> sign, Painter<SangerTemplate, SangerDto> pn) {
		super(template);
		this.header = header;
		this.ldt = ldt;
		this.footer = footer;
		this.sign = sign;
		this.pn = pn;
	}
	public Painter<SangerTemplate, SangerDto> page() {
		return initialize().and(barcode)
				.and(header.initialize().and(header.header()))
				.and(footer).and(sign)
				.and(variant())
				.and(interpretation())
				.and(testInfo())
				.and(ldt)
				.and(pn);
	}
	private Painter<SangerTemplate, SangerDto> initialize() {
		return (stream, template, dto) -> {
			SangerResource resource = template.resource();
			colorPrimary = resource.colorPrimary();
			colorPrimaryLine = resource.colorPrimaryLine();
			colorSecondaryLine = resource.colorSecondaryLine();
			colorGray = resource.colorGray();
			styleHeaderTitle = new TextStyle().color(resource.colorText()).fonts(resource.fontHeader(), resource.fontText(), resource.fontDefault()).fontSize(9).justify(true).paragraph(false);
			styleHeader = styleHeaderTitle.clone().color(resource.colorTextWithPrimary()).fontSize(9).justify(false);
			styleHeaderTable = styleHeaderTitle.clone().color(resource.colorTextWithSecondary()).fontSize(8).justify(false);
			styleHeaderInfo = new TextStyle().color(resource.colorText()).fonts(resource.fontHeader(), resource.fontTitle(), resource.fontDefault()).fontSize(9);
			styleValue = new TextStyle().color(resource.colorText()).fonts(resource.fontText(), resource.fontDefault()).fontSize(9).paragraph(false);
			styleValueTable = new TextStyle().color(resource.colorText()).fonts(resource.fontText(), resource.fontDefault()).fontSize(8).paragraph(false);
			styleText = new TextStyle().color(resource.colorText()).fonts(resource.fontText(), resource.fontDefault()).fontSize(8).justify(true).paragraph(true);
			styleTextSub = new TextStyle().color(resource.colorText()).fonts(resource.fontText(), resource.fontDefault()).fontSize(8).justify(false).paragraph(true);
			stream.font(template.resource().fontDefault());
			return stream;
		};
	}
	private Painter<SangerTemplate, SangerDto> variant() {
		return (stream, template, dto) -> {
			stream.saveGraphicsState();
			float y = header.cursorY();
			y -= 30;
			stream.setNonStrokingColor(colorPrimary).addRect(60,y-7,473, 20).fill();
			stream.paragraph(63, y, 115, new TextBlock(styleHeader, template.lblResult()));
			cursorY(y);
			stream.restoreGraphicsState();
			if(dto.variants()!=null) for(int i = 0; i < dto.variants().length; ++i) stream = variant(dto.variants()[i], i).paint(stream, template, dto);
			return stream;
		};
	}
	private Painter<SangerTemplate, SangerDto> variant(SangerDto.Variant variant, int i) {
		return (stream, template, dto) -> {
			PDImageXObject img = PDImageXObject.createFromByteArray(template.resource().doc(), variant.img(), String.format("[Attached file %d]", (i+1)));
			float widthImg = img.getWidth()*0.75f;
			float heightImg = img.getHeight()*0.75f;
			float y = cursorY();
			float height = 77.5f + heightImg;
			if(y - height < 130) {
				stream = newPage(stream);
				header.header().and(footer).and(sign).paint(stream, template, dto);
				y = header.cursorY();
				y -= 30;
			} else y -= 7.5f;
			stream.saveGraphicsState();
			stream.setNonStrokingColor(new Color(214, 220, 229))
				  .addRect(60, y-20,473, 20)
				  .fill();
			y -= 10;
			TextStyle styleHeaderTable = this.styleHeaderTable.clone().color(template.resource().colorText());
			stream.paragraph(80, y, 115, CENTER, MIDDLE, new TextBlock(styleHeaderTable, template.lblGene()[0]));
			stream.paragraph(160, y, 115, CENTER, MIDDLE, new TextBlock(styleHeaderTable, template.lblGene()[1]));
			stream.paragraph(260, y, 115, CENTER, MIDDLE, new TextBlock(styleHeaderTable, template.lblGene()[2]));
			stream.paragraph(345, y, 115, CENTER, MIDDLE, new TextBlock(styleHeaderTable, template.lblGene()[3]));
			stream.paragraph(425, y, 115, CENTER, MIDDLE, new TextBlock(styleHeaderTable, template.lblGene()[4]));
			stream.paragraph(505, y, 115, CENTER, MIDDLE, new TextBlock(styleHeaderTable, template.lblGene()[5]));

			y -= 21;
			float ny = y;
			ny = Math.min(ny, stream.paragraph(80, y, 50, CENTER, new TextBlock(styleValueTable, variant.gene())));
			ny = Math.min(ny, stream.paragraph(160, y, 90, CENTER, new TextBlock(styleValueTable, variant.dnaChange())));
			ny = Math.min(ny, stream.paragraph(260, y, 92.5f, CENTER, new TextBlock(styleValueTable, variant.predictedAa())));
			ny = Math.min(ny, stream.paragraph(345, y, 82.5f, CENTER, new TextBlock(styleValueTable, variant.zygosity())));
			ny = Math.min(ny, stream.paragraph(425, y, 80, CENTER, new TextBlock(styleValueTable, variant.clazz())));
			ny = Math.min(ny, stream.paragraph(505, y, 90, CENTER, new TextBlock(styleValueTable, variant.result().toFormattedString())));

			y = ny-10;
			stream.setLineWidth(0.5f).setStrokingColor(colorPrimaryLine).line(60, y, 533, y).stroke();
			y -= 10;
			float x = 60+(473 - widthImg)/2.0f;
			stream.drawImage(img, x, y-heightImg, widthImg, heightImg);
			y -= heightImg + 10;
			stream.line(60, y, 533, y).stroke();
			cursorY(y);
			stream.restoreGraphicsState();
			return stream;
		};
	}
	private Painter<SangerTemplate, SangerDto> interpretation() {
		return (stream, template, dto) -> {
			stream.saveGraphicsState();
			float y = cursorY();
			TextBlock contentBlock = new TextBlock(styleText, dto.interpretation());
			float height = 100 + stream.height(460, contentBlock);
			if (y - height < 135) {
				stream = newPage(stream);
				header.header().and(footer).and(sign).paint(stream, template, dto);
				y = header.cursorY();
			}
			y -= 20;
			stream.setNonStrokingColor(colorPrimary).addRect(60,y-7,473, 20).fill();
			stream.paragraph(63, y, 115, new TextBlock(styleHeader, template.lblInterpretation()));
			y -= 20;
			y = stream.paragraph(60, y, 460, new TextBlock(styleText, dto.interpretation()));
			y -= 10;
			stream.setLineWidth(0.35f).setStrokingColor(colorPrimaryLine).line(60, y, 533, y).stroke();
			cursorY(y);
			stream.restoreGraphicsState();
			return stream;
		};
	}
	private Painter<SangerTemplate, SangerDto> testInfo() {
		return (stream, template, dto) -> method(template.testInfo())
					   .and(limitation(template.testInfo()))
					   .and(references(template.testInfo()))
					   .paint(stream, template, dto);
	}
	private Painter<SangerTemplate, SangerDto> method(TestInfo info) {
		return (stream, template, dto) -> {
			stream.saveGraphicsState();
			float y = cursorY();
			float height = 60;
			if(y - height < 130) {
				stream = newPage(stream);
				header.header().and(footer).and(sign).paint(stream, template, dto);
				y = header.cursorY();
			}
			y -= 30;
			stream.setNonStrokingColor(colorGray).addRect(60, y-40,180, 40).fill();	// 회색박스 먼저
			stream.setLineWidth(0.5f).setStrokingColor(colorSecondaryLine)
				  .line(240, y-40, 240, y).stroke();
			stream.setNonStrokingColor(colorPrimary).addRect(60, y,473, 20).fill();
			stream.paragraph(63, y+7, 500, new TextBlock(styleHeader, template.lblMethods()));
			y -= 20; stream.line(60, y, 533, y).paragraph(63, y+7, 180, new TextBlock(styleHeaderInfo, template.lblMethodList()[0]));
					 stream.paragraph(250, y+7, 300, new TextBlock(styleValue, info.specimen()));
			y -= 20; stream.line(60, y, 533, y).paragraph(63, y+7, 180, new TextBlock(styleHeaderInfo, template.lblMethodList()[1]));
					 stream.paragraph(250, y+7, 300, new TextBlock(styleValue, info.method()));
			stream.stroke();
			cursorY(y);
			stream.restoreGraphicsState();
			return stream;
		};
	}
	private Painter<SangerTemplate, SangerDto> limitation(TestInfo info) {
		return (stream, template, dto) -> {
			stream.saveGraphicsState();
			float y = cursorY();
			float height = 60;
			if(y - height < 110) {
				stream = newPage(stream);
				header.header().and(footer).and(sign).paint(stream, template, dto);
				y = header.cursorY();
			}
			y -= 30;
			stream.setNonStrokingColor(colorPrimary).addRect(60, y,473, 20).fill();
			stream.paragraph(63, y+7, 500, new TextBlock(styleHeader, template.lblLimitation()));
			y -= 3;
			for(int i=0; i < info.limitations().length; ++i) {
				y -= 13;
				stream.paragraph(60, y, 7, JUSTIFY, new TextBlock(styleTextSub, NumberFormat.getInstance().format(i+1) + "."));
				y = stream.paragraph(69, y, 461, JUSTIFY, new TextBlock(styleTextSub, info.limitations()[i]));
			}
			cursorY(y);
			stream.restoreGraphicsState();
			return stream;
		};
	}
	private Painter<SangerTemplate, SangerDto> references(TestInfo info) {
		return (stream, template, dto) -> {
			stream.saveGraphicsState();
			float y = cursorY();
			float height =50;
			if(y - height < 110) {
				stream = newPage(stream);
				header.header().and(footer).and(sign).paint(stream, template, dto);
				y = header.cursorY();
			}
			y -= 30;
			stream.setNonStrokingColor(colorPrimary).addRect(60, y,473, 20).fill();
			stream.paragraph(63, y+7, 500, new TextBlock(styleHeader, template.lblReferences()));
			y -= 3;
			for(int i=0; i < info.references().length; ++i) {
				y -= 13;
				stream.paragraph(60, y, 7, JUSTIFY, new TextBlock(styleTextSub, NumberFormat.getInstance().format(i+1) + "."));
				y = stream.paragraph(69, y, 461, JUSTIFY, new TextBlock(styleTextSub, info.references()[i]));
			}
			y -= 8;
			stream.setLineWidth(0.5f).setStrokingColor(colorSecondaryLine).line(60, y, 533, y).stroke();
			cursorY(y);
			stream.restoreGraphicsState();
			return stream;
		};
	}
}
