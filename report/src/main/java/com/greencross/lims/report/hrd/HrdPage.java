package com.greencross.lims.report.hrd;

import com.gcgenome.lims.report.TextBlock;
import com.gcgenome.lims.report.TextStyle;
import com.gcgenome.lims.report.func.AlignHorizontal;
import com.gcgenome.lims.report.func.AlignVertical;
import com.gcgenome.lims.report.func.Page;
import com.gcgenome.lims.report.func.Painter;
import com.greencross.lims.report.SectionBarcode;
import com.greencross.lims.report.SectionLDT;
import com.greencross.lims.report.builder.LogoType;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.awt.*;
import java.text.NumberFormat;
import java.util.function.Function;

public class HrdPage extends Page<HrdTemplate> {
	private final Painter<HrdTemplate, HrdDto> barcode = new SectionBarcode<>();
	private final Painter<HrdTemplate, HrdDto> header;
	private final Painter<HrdTemplate, HrdDto> ldt;
	private final Painter<HrdTemplate, HrdDto> footer;
	private final Painter<HrdTemplate, HrdDto> sign;
	private final Painter<HrdTemplate, HrdDto> page;
	private final Painter<HrdTemplate, HrdDto> sectionGene;
	private final Painter<HrdTemplate, HrdDto> empty = (s, t, d)->newPage(s);
	private Color colorPrimary;
	private Color colorSecondary;
	private Color colorGray;
	private TextStyle styleHeaderTitle;
	private TextStyle styleHeader;
	private TextStyle styleHeader2;
	private TextStyle styleHeader3;
	private TextStyle styleValue;
	private TextStyle styleText;
	private TextStyle styleTextSub;
	public HrdPage(HrdTemplate template, Painter<HrdTemplate, HrdDto> header, Painter<HrdTemplate, HrdDto> footer, Painter<HrdTemplate, HrdDto> sign, Painter<HrdTemplate, HrdDto> page) {
		super(template);
		this.header = header;
		this.footer = footer;
		this.sign = sign;
		this.page = page;
		if(template.logoType() == LogoType.INDEPENDENT) ldt = new SectionLDT<>(template.logoType(), 100);
		else if(template.logoType() == LogoType.DEPENDENT) ldt = new SectionLDT<>(template.logoType(), 120);
		else ldt = new SectionLDT<>(template.logoType(), 90);

		sectionGene = new SectionGene(empty.and(template()));
	}
	public Painter<HrdTemplate, HrdDto> template() {
		return header.and(footer).and(sign);
	}
	public Painter<HrdTemplate, HrdDto> page() {
		return initialize().and(barcode)
				.and(header).and(footer).and(sign)
				.and(summary())
				.and(qc())
				.and(details())
				.and(genes())
				.and(info())
				.and(empty.and(template())).and(sectionGene)
				.and(ldt)
				.and(page);
	}
	private Painter<HrdTemplate, HrdDto> initialize() {
		return (stream, template, dto) -> {
			HrdResource resource = template.resource();
			colorPrimary = resource.colorPrimary();
			colorSecondary = resource.colorSecondary();
			colorGray = resource.colorGray();
			styleHeaderTitle = new TextStyle().color(resource.colorText()).fonts(resource.fontHeader(), resource.fontText(), resource.fontDefault()).fontSize(9).justify(true).paragraph(false);
			styleHeader = styleHeaderTitle.clone().color(resource.colorTextWithPrimary()).fontSize(9).justify(false);
			styleHeader2 = new TextStyle().color(resource.colorTextWithPrimary()).fonts(resource.fontHeader(), resource.fontTitle(), resource.fontDefault()).fontSize(13);
			styleHeader3 = new TextStyle().color(colorPrimary).fonts(resource.fontTitle()).fontSize(11);
			styleValue = new TextStyle().color(resource.colorText()).fonts(resource.fontText(), resource.fontDefault()).fontSize(9).paragraph(false);
			styleText = new TextStyle().color(resource.colorText()).fonts(resource.fontText(), resource.fontDefault()).fontSize(9).justify(true).paragraph(true);
			styleTextSub = new TextStyle().color(colorPrimary).fonts(resource.fontText(), resource.fontDefault()).fontSize(7).justify(true).paragraph(true);
			stream.font(template.resource().fontDefault());
			return stream;
		};
	}
	private Painter<HrdTemplate, HrdDto> summary() {
		return (stream, template, dto)->{
			float y = stream.cursorY();
			stream.saveGraphicsState();
			y -= 30;
			stream.setNonStrokingColor(colorGray)
					.addRect(60, y,65, 20).fill()
					.setLineWidth(0.25f).setStrokingColor(colorSecondary)
					.line(60, y+0.1f, 533, y+0.1f)
					.line(60, y+20, 533, y+20)
					.stroke();
			stream.paragraph(62, y+7, 60, new TextBlock(styleHeaderTitle, template.lblCancerType()));
			y = stream.paragraph(130, y+7, 400, new TextBlock(styleValue, dto.cancerType()));
			y -= 30;
			stream.setNonStrokingColor(colorPrimary).addRect(60, y-5,473, 20).fill();
			stream.paragraph(65, y+2, 60, new TextBlock(styleHeader2, template.lblSummary()));

			y-= 20;
			stream.setLineWidth(0.25f)
					.addRect(60, y-45, 118.5f, 60)
					.addRect(296.5f, y-45,118.5f, 60)
					.setNonStrokingColor(colorGray).fill()
					.line(296.5f, y-15, 533, y-15)
					.line(178f, y-45, 178f, y+15)
					.line(296.5f, y-45, 296.5f, y+15)
					.line(415f, y-45, 414.5f, y+15)
					.setStrokingColor(colorPrimary).stroke();
			Color colorNormal = template.resource().colorText();
			Color colorAlert = template.resource().colorRed();

			HrdDto.Result resultSummary = HrdDto.Result.N;
			if(dto.gi() == HrdDto.Result.P) resultSummary = HrdDto.Result.P;
			if(dto.brcaResult() == HrdDto.Result.P) resultSummary = HrdDto.Result.P;
			if(dto.gi() == HrdDto.Result.F) resultSummary = HrdDto.Result.F;
			HrdDto.Result resultBrcaSummary = HrdDto.Result.N;
			if(dto.brcaResult() == HrdDto.Result.P) resultBrcaSummary = HrdDto.Result.P;
			if(dto.gi() == HrdDto.Result.F) resultBrcaSummary = HrdDto.Result.F;

			stream.paragraph(119.25f, y-15, 100, AlignHorizontal.CENTER, AlignVertical.MIDDLE, new TextBlock(styleHeader3, template.lblHrd()));
			stream.paragraph(237.75f, y-15, 100, AlignHorizontal.CENTER, AlignVertical.MIDDLE,
					new TextBlock(styleValue.clone().color(resultSummary==HrdDto.Result.P?colorAlert:colorNormal), template.resultToString(resultSummary)));
			stream.paragraph(356.25f, y, 100, AlignHorizontal.CENTER, AlignVertical.MIDDLE, new TextBlock(styleHeader3, template.lblGi()));
			stream.paragraph(474.75f, y, 100, AlignHorizontal.CENTER, AlignVertical.MIDDLE,
					new TextBlock(styleValue.clone().color(dto.gi()==HrdDto.Result.P?colorAlert:colorNormal), template.resultToString(dto.gi())));
			y-= 30;
			stream.paragraph(356.25f, y, 100, AlignHorizontal.CENTER, AlignVertical.MIDDLE, new TextBlock(styleHeader3, template.lblBrca()));
			stream.paragraph(474.75f, y, 100, AlignHorizontal.CENTER, AlignVertical.MIDDLE,
					new TextBlock(styleValue.clone().color(resultBrcaSummary==HrdDto.Result.P?colorAlert:colorNormal), template.resultToString(resultBrcaSummary)));
			y-= 10;

			stream.cursorY(y);
			stream.restoreGraphicsState();
			return stream;
		};
	}
	private Painter<HrdTemplate, HrdDto> qc() {
		return (stream, template, dto)->{
			stream.saveGraphicsState();
			float y = stream.cursorY();
			y-= 20;
			stream.setNonStrokingColor(colorPrimary).addRect(60, y-5,473, 20).fill();
			stream.paragraph(65, y+2, 60, new TextBlock(styleHeader2, template.lblQc()));
			y-= 20;
			stream.setLineWidth(0.25f)
					.addRect(60, y-15, 118.5f, 30)
					.addRect(296.5f, y-15,118.5f, 30)
					.setNonStrokingColor(colorGray).fill()
					.line(178f, y-15, 178f, y+15)
					.line(296.5f, y-15, 296.5f, y+15)
					.line(415f, y-15, 414.5f, y+15)
					.setStrokingColor(colorPrimary).stroke();
			TextStyle styleValue = this.styleValue.clone().color(template.resource().colorText());
			stream.paragraph(119.25f, y-3, 100, AlignHorizontal.CENTER, new TextBlock(styleHeader3, template.lblQcSnv()));
			stream.paragraph(356.25f, y-3, 100, AlignHorizontal.CENTER, new TextBlock(styleHeader3, template.lblQcCnv()));
			stream.paragraph(237.75f, y-3, 100, AlignHorizontal.CENTER, new TextBlock(styleValue, dto.snv()));
			stream.paragraph(474.75f, y-3, 100, AlignHorizontal.CENTER, new TextBlock(styleValue, dto.cnv()));
			y-= 10;
			stream.cursorY(y);
			stream.restoreGraphicsState();
			return stream;
		};
	}
	private Painter<HrdTemplate, HrdDto> details() {
		return (stream, template, dto)->{
			HrdResource resource = template.resource();
			stream.saveGraphicsState();
			float y = stream.cursorY();
			y-= 20;
			stream.setNonStrokingColor(colorPrimary).addRect(60, y-25,473, 40).fill();
			stream.paragraph(65, y+2, 120, new TextBlock(styleHeader2, template.lblDetails()));
			y-= 20;
			stream.setLineWidth(0.25f)
					.line(60, y+15, 533, y+15)
					.line(296.5f, y+15, 296.5f, y-5)
					.setStrokingColor(Color.white).stroke()
					.line(60, y-25, 533, y-25)
					.line(296.5f, y-25, 296.5f, y-5)
					.setStrokingColor(colorPrimary).stroke();
			stream.paragraph(178, y+2, 200, AlignHorizontal.CENTER, new TextBlock(styleHeader, template.lblGi()));
			stream.paragraph(415f, y+2, 200, AlignHorizontal.CENTER, new TextBlock(styleHeader, template.lblGiScore()));
			y-= 20;
			Color colorText = template.resource().colorText();
			if(dto.gi() == HrdDto.Result.P) colorText = template.resource().colorRed();
			TextStyle styleValue = this.styleValue.clone().color(colorText);
			stream.paragraph(178, y+2, 200, AlignHorizontal.CENTER, new TextBlock(styleValue, template.resultToString(dto.gi())));
			if(dto.gi() != HrdDto.Result.F) stream.paragraph(415f, y+2, 200, AlignHorizontal.CENTER, new TextBlock(styleValue, dto.giScore()!=null?NumberFormat.getInstance().format(dto.giScore()):"-"));
			else stream.paragraph(415f, y+2, 200, AlignHorizontal.CENTER, new TextBlock(styleValue, "-"));

			PDImageXObject img = null;
			if (dto.gi() == HrdDto.Result.P) img = template.resource().gene1();
			if (dto.gi() == HrdDto.Result.N) img = template.resource().gene2();
			if(img != null) {
				y -= 130;
				stream.drawImage(img, 116,y,395,115);
				stream.setNonStrokingColor(Color.WHITE).rect(437, y+40, 50, -50).fill();
				stream.paragraph(462, y+75, 50, AlignHorizontal.CENTER,
						new TextBlock(styleTextSub.clone().fontSize(12), template.lblGi()));

				stream.paragraph(462, y+42, 50, AlignHorizontal.CENTER,
						new TextBlock(styleTextSub.clone().fontSize(12).color((dto.gi() == HrdDto.Result.P)?template.resource().colorRed():template.resource().colorText()),
								template.resultToString(dto.gi())));

				y -= 8;
				stream.paragraph(167, y+2, 150, AlignHorizontal.CENTER, new TextBlock(styleTextSub.clone().color(resource.colorText()).fontSize(8), template.lblLohTitle() + "\n"), new TextBlock(styleTextSub, template.lblLoh()));
				stream.paragraph(262, y+2, 150, AlignHorizontal.CENTER, new TextBlock(styleTextSub.clone().color(resource.colorText()).fontSize(8), template.lblTaiTitle() + "\n"), new TextBlock(styleTextSub, template.lblTai()));
				stream.paragraph(355, y+2, 150, AlignHorizontal.CENTER, new TextBlock(styleTextSub.clone().color(resource.colorText()).fontSize(8), template.lblLstTitle() + "\n"), new TextBlock(styleTextSub, template.lblLst()));
				y -= 40;
			}
			y -= 20;
			stream.paragraph(65, y, 473, new TextBlock(styleText.clone().color(template.resource().colorRed()), template.lblInterpretation()));
			stream.line(60, y+14, 533, y+14)
					.line(60, y-6, 533, y-6).stroke();
			y -= 17;
			y = stream.paragraph(60, y, 473, new TextBlock(styleText, dto.interpretation()));
			y-= 20;
			stream.cursorY(y);
			stream.line(60, 140, 533, 140).stroke();
			stream.restoreGraphicsState();
			return stream;
		};
	}
	private Painter<HrdTemplate, HrdDto> genes() {
		return (stream, template, dto)->{
			stream = newPage(stream);
			header.and(footer).and(sign).paint(stream, template, dto);
			stream.cursorY(stream.cursorY()-15);
			if(dto.gi() == HrdDto.Result.F) return stream;
			for(String tier: template.testInfo().tiers()) {
				Function<HrdDto.HrdDtoGeneResult, TextBlock> summary = null;
				if("BRCA".equals(tier)) summary = result -> new TextBlock(styleValue.clone()
						.color(dto.brcaResult()== HrdDto.Result.P?template.resource().colorRed():template.resource().colorText()),
						template.resultToString(dto.brcaResult()));
				else summary = result ->new TextBlock(styleValue.clone().color(template.resource().colorText()), result.variants()!=null?String.valueOf(result.variants().length):"0");
				stream = details(tier, template.tmplResultByTier(tier), summary).paint(stream, template, dto);
			}
			return stream;
		};
	}
	private Painter<HrdTemplate, HrdDto> details(String tier, String label, Function<HrdDto.HrdDtoGeneResult, TextBlock> summary) {
		return (stream, template, dto)->{
			float y = stream.cursorY();
			String newFormattedTier = tier.matches(".*\\d+.*") ? tier.toLowerCase().replaceFirst(".", Character.toString(tier.charAt(0)).toUpperCase()) : tier;
			HrdDto.HrdDtoGeneResult result = dto.details().getOrDefault(newFormattedTier, dto.details().get(tier));
			float height = 35 + 19*(result!=null && result.variants()!=null?result.variants().length:1);
			if(y - height < 140)  {
				stream = newPage(stream);
				header.and(footer).and(sign).paint(stream, template, dto);
				y = stream.cursorY();
				y -= 20;
			}
			y -= 30;
			stream.saveGraphicsState();
			stream.setLineWidth(0.25f).setStrokingColor(colorPrimary).setNonStrokingColor(colorPrimary)
					.addRect(60, y,100, 25).fill()
					.line(60, y+25.25f, 533, y+25.25f).setStrokingColor(colorSecondary).stroke()
					.line(60, y+0.25f, 533, y+0.25f).setStrokingColor(Color.WHITE).stroke();
			stream.paragraph(110, y+10, 120, AlignHorizontal.CENTER, new TextBlock(styleHeader, label));
			if(result!=null) stream.paragraph(170, y+9, 400, summary.apply(result));
			y -= 20;
			stream.addRect(60, y,473, 20).setStrokingColor(colorPrimary).fill();
			stream.paragraph(70, y+8, 20, AlignHorizontal.CENTER, new TextBlock(styleHeader, template.lblVariantTableNo()));
			stream.paragraph(114, y+8, 68, AlignHorizontal.CENTER, new TextBlock(styleHeader, template.lblVariantTableGene()));
			stream.paragraph(198, y+8, 100, AlignHorizontal.CENTER, new TextBlock(styleHeader, template.lblVariantTableDNA()));
			stream.paragraph(308, y+8, 120, AlignHorizontal.CENTER, new TextBlock(styleHeader, template.lblVariantTableProtein()));
			stream.paragraph(393, y+8, 50, AlignHorizontal.CENTER, new TextBlock(styleHeader, template.lblVariantTableVaf()));
			stream.paragraph(443, y+8, 50, AlignHorizontal.CENTER, new TextBlock(styleHeader, template.lblVariantTableDepth()));
			stream.paragraph(500.5f, y+8, 65, AlignHorizontal.CENTER, new TextBlock(styleHeader, template.lblVariantTableCosmic()));
			NumberFormat decimals = NumberFormat.getInstance();
			NumberFormat percentage = NumberFormat.getInstance();
			percentage.setMaximumFractionDigits(2);
			if(result!=null && result.variants()!=null && result.variants().length > 0) for(int i = 0; i < result.variants().length; ++i) {
				y -= 20;
				float hm = 9999;
				hm = Math.min(hm, stream.paragraph(70, y+8, 20, AlignHorizontal.CENTER, new TextBlock(styleValue, decimals.format((i+1)))));
				hm = Math.min(hm, stream.paragraph(114, y+8, 68, AlignHorizontal.CENTER, new TextBlock(styleValue, result.variants()[i].gene())));
				hm = Math.min(hm, stream.paragraph(198, y+8, 100, AlignHorizontal.CENTER, new TextBlock(styleValue, result.variants()[i].hgvsc()!=null?result.variants()[i].hgvsc():result.variants()[i].dna())));
				hm = Math.min(hm, stream.paragraph(308, y+8, 120, AlignHorizontal.CENTER, new TextBlock(styleValue, result.variants()[i].hgvsp()!=null?result.variants()[i].hgvsp():result.variants()[i].protein())));
				hm = Math.min(hm, stream.paragraph(393, y+8, 50, AlignHorizontal.CENTER, new TextBlock(styleValue, result.variants()[i].vaf()!=null?decimals.format(result.variants()[i].vaf()):"-")));
				hm = Math.min(hm, stream.paragraph(443, y+8, 50, AlignHorizontal.CENTER, new TextBlock(styleValue, result.variants()[i].depth()!=null?decimals.format(result.variants()[i].depth()):"-")));
				hm = Math.min(hm, stream.paragraph(500.5f, y+8, 65, AlignHorizontal.CENTER, new TextBlock(styleValue, result.variants()[i].cosmic())));
				y = hm-8;
			} else {
				y -= 20;
				stream.paragraph(296.5f, y+8, 473, AlignHorizontal.CENTER, new TextBlock(styleValue, "No variant"));
			}
			stream.setLineWidth(1).line(60, y, 533, y).stroke();
			stream.cursorY(y);
			stream.restoreGraphicsState();
			stream = interpretation(result.interpretation()).paint(stream, template, dto);
			return stream;
		};
	}
	protected Painter<HrdTemplate, HrdDto> interpretation(String interpretation) {
		return (stream, template, dto) -> {
			stream.saveGraphicsState();
			float y = stream.cursorY();
			y -= 15;
			stream.paragraph(65, y, 473, new TextBlock(styleText.clone().color(template.resource().colorRed()), template.lblInterpretation()));
			stream.setLineWidth(0.25f).setStrokingColor(colorPrimary).line(60, y-6, 533, y-6).stroke();
			y -= 20;

			String[] paragraphs = interpretation.split("\n", -1);
			for(String p: paragraphs) {
				if(!p.equals(paragraphs[paragraphs.length-1])) p += "\n\n";
				TextBlock block = new TextBlock(styleText, p);
				float height = stream.height(473, block);
				if(y - height < 110) {
					stream.restoreGraphicsState();
					stream = newPage(stream);
					header.and(footer).and(sign).paint(stream, template, dto);
					y = stream.cursorY()-30;
					stream.paragraph(65, y, 473, new TextBlock(styleText.clone().color(template.resource().colorRed()), template.lblInterpretation()));
					stream.setLineWidth(0.25f).setStrokingColor(colorPrimary).line(60, y-6, 533, y-6).stroke();
					y -= 20;
					stream.saveGraphicsState();
				}
				y = stream.paragraph(60, y, 473, block);
			}
			y -= 10;
			stream.line(60, y-12, 533, y-10).stroke();
			stream.cursorY(y-22);
			stream.restoreGraphicsState();
			return stream;
		};
	}
	private Painter<HrdTemplate, HrdDto> info() {
		return method().and(hrd()).and(parp()).and(limitation()).and(references());
	}
	private Painter<HrdTemplate, HrdDto> method() {
		return (stream, template, dto)->{
			float y = stream.cursorY();
			float height = 80;
			if(y - height < 140)  {
				stream = newPage(stream);
				header.and(footer).and(sign).paint(stream, template, dto);
				y = stream.cursorY();
			}
			y -= 30;
			stream.saveGraphicsState();
			stream.setNonStrokingColor(colorPrimary).addRect(60, y-5,473, 20).fill();
			y = stream.paragraph(65, y+2, 473, new TextBlock(styleHeader2, template.lblTestInfoTitle()));
			y -= 20;
			y = stream.paragraph(60, y+2, 473, new TextBlock(styleText, template.lblTestInfo()));
			y -= 10;
			stream.cursorY(y);
			stream.restoreGraphicsState();
			return stream;
		};
	}
	private Painter<HrdTemplate, HrdDto> hrd() {
		return (stream, template, dto)->{
			float y = stream.cursorY();
			float height = 75 + stream.height(473, new TextBlock(styleText, template.lblHrdInfo()));
			if(y - height < 80)  {
				stream = newPage(stream);
				header.and(footer).and(sign).paint(stream, template, dto);
				y = stream.cursorY();
			}
			y -= 20;
			stream.saveGraphicsState();
			stream.paragraph(60, y, 473, new TextBlock(styleHeader3, template.lblHrdInfoTitle()));
			y -= 5;
			stream.setLineWidth(1).setStrokingColor(colorSecondary).line(60, y, 533, y).stroke().stroke();
			y -= 18;
			y = stream.paragraph(60, y+2, 473, new TextBlock(styleText, template.lblHrdInfo()));
			y-= 10;
			stream.cursorY(y);
			stream.restoreGraphicsState();
			return stream;
		};
	}
	private Painter<HrdTemplate, HrdDto> parp() {
		return (stream, template, dto)->{
			float y = stream.cursorY();
			float height = 75 + stream.height(473, new TextBlock(styleText, template.lblParpInfo()));
			if(y - height < 80)  {
				stream = newPage(stream);
				header.and(footer).and(sign).paint(stream, template, dto);
				y = stream.cursorY();
			}
			y -= 20;
			stream.saveGraphicsState();
			stream.paragraph(60, y, 473, new TextBlock(styleHeader3, template.lblParpInfoTitle()));
			y -= 5;
			stream.setLineWidth(1).setStrokingColor(colorSecondary).line(60, y, 533, y).stroke().stroke();
			y -= 18;
			y = stream.paragraph(60, y+2, 473, new TextBlock(styleText, template.lblParpInfo()));
			y -= 10;
			stream.cursorY(y);
			stream.restoreGraphicsState();
			return stream;
		};
	}
	private Painter<HrdTemplate, HrdDto> limitation() {
		return (stream, template, dto)->{
			float y = stream.cursorY();
			float height = 75;
			for(String limitation: template.lblLimitations()) height += stream.height(460, new TextBlock(styleText, limitation));
			if(y - height < 80)  {
				stream = newPage(stream);
				header.and(footer).and(sign).paint(stream, template, dto);
				y = stream.cursorY();
			}
			y -= 20;
			stream.saveGraphicsState();
			stream.paragraph(60, y, 120, new TextBlock(styleHeader3, template.lblTestLimitation()));
			y -= 5;
			stream.setLineWidth(1).setStrokingColor(colorSecondary)
					.line(60, y, 533, y).stroke().stroke();
			for(String limitation: template.lblLimitations()) {
				y -= 18;
				stream.paragraph(63, y+1.5f, 10, new TextBlock(styleText.clone().fontSize(4), "●"));
				y = stream.paragraph(70, y, 460, new TextBlock(styleText, limitation));
			}
			y -= 10;
			stream.cursorY(y);
			stream.restoreGraphicsState();
			return stream;
		};
	}
	private Painter<HrdTemplate, HrdDto> references() {
		return (stream, template, dto)->{
			float y = stream.cursorY();
			float height = 151;     // 나중에 template.lblHrdInfo()의 높이를 계산해서 동적으로 세팅되도록 변경할 것
			if(y - height < 80)  {
				stream = newPage(stream);
				header.and(footer).and(sign).paint(stream, template, dto);
				y = stream.cursorY();
			}
			y -= 20;
			stream.saveGraphicsState();
			stream.paragraph(60, y, 120, new TextBlock(styleHeader3, template.lblTestReferences()));
			y -= 5;
			stream.setLineWidth(1).setStrokingColor(colorSecondary)
					.line(60, y, 533, y).stroke().stroke();
			for(int i = 0; i < template.lblReferences().length; ++i) {
				y -= 15;
				String limitation = template.lblReferences()[i];
				stream.paragraph(60, y, 10, new TextBlock(styleText, String.valueOf(i+1) + ". "));
				y = stream.paragraph(70, y, 460, AlignHorizontal.JUSTIFY, new TextBlock(styleText, limitation));
			}
			y -= 10;
			stream.cursorY(y);
			stream.restoreGraphicsState();
			return stream;
		};
	}
}