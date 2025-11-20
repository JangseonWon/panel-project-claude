package com.greencross.lims.report.mrd;

import com.gcgenome.lims.report.TextBlock;
import com.gcgenome.lims.report.TextStyle;
import com.gcgenome.lims.report.func.AlignHorizontal;
import com.gcgenome.lims.report.func.Page;
import com.gcgenome.lims.report.func.Painter;
import com.greencross.lims.report.SectionBarcode;
import com.greencross.lims.report.SectionLDT;
import com.greencross.lims.report.builder.LogoType;

import java.awt.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;

import static com.gcgenome.lims.report.func.AlignHorizontal.CENTER;
import static com.gcgenome.lims.report.func.AlignHorizontal.RIGHT;
import static com.gcgenome.lims.report.func.AlignVertical.MIDDLE;

public class MrdPage extends Page<MrdTemplate> {
	private final Painter<MrdTemplate, MrdDto> barcode = new SectionBarcode<>();
	private final Painter<MrdTemplate, MrdDto> ldt;
	private final Painter<MrdTemplate, MrdDto> footer;
	private final Painter<MrdTemplate, MrdDto> sign;
	private final Painter<MrdTemplate, MrdDto> page;
	private Color colorPrimary;
	private Color colorSecondary;
	private Color colorGray;
	private TextStyle styleTitle;
	private TextStyle styleHeaderTitle;
	private TextStyle styleValueTitle;
	private TextStyle styleHeader;
	private TextStyle styleHeader2;
	private TextStyle styleHeader3;
	private TextStyle styleHeader4;
	private TextStyle styleValue;
	private TextStyle styleText;
	private TextStyle styleTextSub;
	private final int bottom;
	public MrdPage(MrdTemplate template, Painter<MrdTemplate, MrdDto> footer, Painter<MrdTemplate, MrdDto> sign, Painter<MrdTemplate, MrdDto> page) {
		super(template);
		this.footer = footer;
		this.sign = sign;
		this.page = page;
		if(template.logoType() == LogoType.INDEPENDENT) bottom = 90;
		else if(template.logoType() == LogoType.DEPENDENT) bottom = 120;
		else bottom = 90;
		ldt = new SectionLDT<>(template.logoType(), bottom);
	}
	public Painter<MrdTemplate, MrdDto> page() {
		return initialize().and(barcode)
				.and(header())
				.and(sign())
				.and(summary())
				.and(details())
				.and(info())
				.and(ldt)
				.and(page);
	}
	private Painter<MrdTemplate, MrdDto> initialize() {
		return (stream, template, dto) -> {
			MrdResource resource = template.resource();
			colorPrimary = resource.colorPrimary();
			colorSecondary = resource.colorSecondary();
			colorGray = resource.colorGray();
			styleTitle = new TextStyle().color(resource.colorPrimary()).fonts(resource.fontTitle(), resource.fontDefault()).fontSize(17).paragraph(false);
			styleHeaderTitle = new TextStyle().color(resource.colorText()).fonts(resource.fontHeader(), resource.fontText(), resource.fontDefault()).fontSize(9).justify(true).paragraph(false);
			styleValueTitle = new TextStyle().color(resource.colorText()).fonts(resource.fontText(), resource.fontDefault()).fontSize(7).paragraph(false);
			styleHeader = styleHeaderTitle.clone().color(resource.colorTextWithPrimary()).fontSize(9).justify(false);
			styleHeader2 = new TextStyle().color(resource.colorTextWithPrimary()).fonts(resource.fontHeader(), resource.fontTitle(), resource.fontDefault()).fontSize(13);
			styleHeader3 = new TextStyle().color(colorPrimary).fonts(resource.fontHeader(), resource.fontTitle(), resource.fontDefault()).fontSize(11);
			styleHeader4 = new TextStyle().color(resource.colorText()).fonts(resource.fontHeader(), resource.fontTitle(), resource.fontDefault()).fontSize(9);
			styleValue = new TextStyle().color(resource.colorText()).fonts(resource.fontText(), resource.fontDefault()).fontSize(9).paragraph(false);
			styleText = new TextStyle().color(resource.colorText()).fonts(resource.fontText(), resource.fontDefault()).fontSize(9).justify(true).paragraph(true);
			styleTextSub = new TextStyle().color(resource.colorText()).fonts(resource.fontText(), resource.fontDefault()).fontSize(7).justify(true).paragraph(true);
			stream.font(template.resource().fontDefault());
			return stream;
		};
	}

	private Painter<MrdTemplate, MrdDto> header() {
		return (stream, template, dto)->{
			stream.saveGraphicsState();
			stream.drawImage(template.resource().medal(), 47, 770,23,38);
			stream.paragraph(80, 773, 500, new TextBlock(styleTitle, template.testInfo().name()));
			stream.setNonStrokingColor(colorGray)
				  .setLineWidth(0.25f).setStrokingColor(colorSecondary)
				  .addRect(60,677.5f,65, 63)
				  .addRect(211,677.5f,75, 63)
				  .addRect(374,677.5f,75, 63)
				  .fill();

			float y = 731;
			stream.paragraph(62, y, 60, new TextBlock(styleHeaderTitle, template.lblMedicalInstitution()));
			stream.paragraph(214, y, 69, new TextBlock(styleHeaderTitle, template.lblMedicalRecordNumber()));
			stream.paragraph(377, y, 69, new TextBlock(styleHeaderTitle, template.lblRequestNumber()));
			stream.paragraph(130, y+2.5f, 83, MIDDLE, new TextBlock(styleValueTitle, dashIfEmpty(dto.medicalInstitution())));
			stream.paragraph(290, y, 88, new TextBlock(styleValueTitle, dashIfEmpty(dto.medicalRecordNumber())));
			y = stream.paragraph(452, y, 100, new TextBlock(styleValueTitle, dashIfEmpty(dto.requestNumber())));
			stream.line(60, y-5, 533, y-5);
			y -= Math.max(styleHeaderTitle.fontSize(), styleValueTitle.fontSize()) * 1.8f;

			stream.paragraph(62, y, 60, new TextBlock(styleHeaderTitle, template.lblPatientName()));
			stream.paragraph(214, y, 69, new TextBlock(styleHeaderTitle, template.lblPatientCode()));
			stream.paragraph(377, y, 69, new TextBlock(styleHeaderTitle, template.lblAgeSex()));
			stream.paragraph(130, y, 86, new TextBlock(styleValueTitle, dashIfEmpty(dto.patientName())));
			stream.paragraph(290, y, 88, new TextBlock(styleValueTitle, dashIfEmpty(dto.patientCode())));
			y = stream.paragraph(452, y, 100,
								 new TextBlock(styleValueTitle, dashIfEmpty(template.age(dto.age(),  dto.birthDate(), dto.collectionDate()))),
								 new TextBlock(styleValueTitle, " / "),
								 new TextBlock(styleValueTitle, dashIfEmpty(template.sex(dto.sex()))));
			stream.line(60, y-5, 533, y-5);
			y -= Math.max(styleHeaderTitle.fontSize(), styleValueTitle.fontSize()) * 1.8f;

			stream.paragraph(62, y, 60, new TextBlock(styleHeaderTitle, template.lblSpecimenType()));
			stream.paragraph(214, y, 69, new TextBlock(styleHeaderTitle, template.lblWardDepartment()));
			stream.paragraph(377, y, 69, new TextBlock(styleHeaderTitle, template.lblCollectionDate()));
			stream.paragraph(130, y, 86, new TextBlock(styleValueTitle, dashIfEmpty(dto.specimenType())));
			stream.paragraph(290, y, 88,
							 new TextBlock(styleValueTitle, dashIfEmpty(dto.ward())),
							 new TextBlock(styleValueTitle, " / "),
							 new TextBlock(styleValueTitle, dashIfEmpty(dto.department())));
			y = stream.paragraph(452, y, 100, new TextBlock(styleValueTitle, dashIfEmpty(template.date(dto.collectionDate()))));
			stream.line(60, y-5, 533, y-5);
			y -= Math.max(styleHeaderTitle.fontSize(), styleValueTitle.fontSize()) * 1.8f;

			stream.paragraph(62, y, 60, new TextBlock(styleHeaderTitle, template.lblPatientInfo()));
			stream.paragraph(214, y, 69, new TextBlock(styleHeaderTitle, template.lblPhysician()));
			stream.paragraph(377, y, 69, new TextBlock(styleHeaderTitle, template.lblReceiptReportDate()));
			stream.paragraph(130, y, 86, new TextBlock(styleValueTitle, dashIfEmpty(dto.patientInfo())));
			stream.paragraph(290, y, 88, new TextBlock(styleValueTitle, dashIfEmpty(dto.physician())));
			y = stream.paragraph(452, y, 100,
								 new TextBlock(styleValueTitle, dashIfEmpty(template.date(dto.receiptDate()))),
								 new TextBlock(styleValueTitle, " / "),
								 new TextBlock(styleValueTitle, dashIfEmpty(template.date(dto.reportDate()))));
			stream.line(60, y-5, 533, y-5).stroke();
			stream.restoreGraphicsState();
			cursorY(y);
			return stream;
		};
	}
	private Painter<MrdTemplate, MrdDto> summary() {
		return (stream, template, dto)->{
			stream.saveGraphicsState();
			float y = cursorY();
			y -= 30;
			stream.setNonStrokingColor(colorGray)
				  .addRect(60, y,65, 20).fill()
				  .setLineWidth(0.25f).setStrokingColor(colorSecondary)
				  .line(60, y+0.1f, 533, y+0.1f)
				  .line(60, y+20, 533, y+20)
				  .stroke();
			stream.paragraph(62, y+8, 60, new TextBlock(styleHeaderTitle, template.lblCancerType()));
			y = stream.paragraph(130, y+8, 300, new TextBlock(styleValue, dto.cancerType()));
			y -= 30;

			stream.setNonStrokingColor(colorPrimary).addRect(60, y-6,473, 20).fill();
			stream.paragraph(65, y, 60, new TextBlock(styleHeader2, template.lblSummary()));
			y -= 35;

			stream.addRect(60, y,473, 30).fill()
				  .setLineWidth(0.25f).setStrokingColor(colorSecondary)
				  .line(60, y+28.1f, 533, y+28.1f).stroke()
				  .setNonStrokingColor(colorPrimary);
			stream.paragraph(90, y+14, 60, CENTER, MIDDLE, new TextBlock(styleHeader, template.lblGene()));
			stream.paragraph(183, y+14, 200, CENTER, MIDDLE, new TextBlock(styleHeader, template.lblResultDetected()));
			stream.paragraph(303, y+14, 200, CENTER, MIDDLE, new TextBlock(styleHeader, template.lblClonalCellNumberTotalBCells()));
			y = stream.paragraph(453, y+14, 200, CENTER, MIDDLE, new TextBlock(styleHeader, template.lblClonalCellNumberTotalNucleatedCells()));
			y += 4;
			NumberFormat fmtPct = NumberFormat.getInstance();
			fmtPct.setMaximumFractionDigits(3);
			for(int i = 0; i < template.testInfo().genes().length; ++i) {
				y -= 20;
				String gene = template.testInfo().genes()[i];
				MrdDto.MrdDtoGeneResult result = dto.last().results().get(gene);
				stream.paragraph(90, y, 60, CENTER, MIDDLE, new TextBlock(styleValue, gene));
				stream.paragraph(183, y, 200, CENTER, MIDDLE, new TextBlock(styleValue, template.toString(result.result())));
				stream.paragraph(303, y, 200, CENTER, MIDDLE, new TextBlock(styleValue, fmtPct.format(dto.last().results().get(gene).pctClonalBCells()*100)));
				stream.paragraph(453, y, 200, CENTER, MIDDLE, new TextBlock(styleValue, fmtPct.format(dto.last().pctClonalNucelatedCells(gene)*100)));
			}
			y -= 8;
			stream.setLineWidth(1).line(60, y, 533, y).stroke();
			if(dto.mutationRate()!=null && !dto.mutationRate().isEmpty()) {
				String tmp = dto.mutationRate();
				try { tmp = fmtPct.format(Double.parseDouble(dto.mutationRate())) + "%"; } catch(Exception ignore) {}
				stream.paragraph(62, y-10, 500, MIDDLE, new TextBlock(styleValue, template.lblMutationRate()), new TextBlock(styleValue, tmp));
			}

			cursorY(y);
			stream.restoreGraphicsState();
			return stream;
		};
	}
	private Painter<MrdTemplate, MrdDto> details() {
		return (stream, template, dto)->{
			stream.saveGraphicsState();
			float y = cursorY();
			y -= 45;
			stream.setNonStrokingColor(colorPrimary).addRect(60, y-5,473, 20).fill()
				  .setLineWidth(0.25f).setStrokingColor(colorSecondary)
				  .line(60, y-4.6f, 533, y-4.6f).stroke()
				  .setNonStrokingColor(colorPrimary);
			stream.paragraph(65, y+2, 60, new TextBlock(styleHeader2, template.lblDetails()));
			cursorY(y);
			stream.restoreGraphicsState();
			for(String gene: template.testInfo().genes()) stream = details(gene).and(graph(gene)).and(interpretation(gene)).paint(stream, template, dto);
			return stream;
		};
	}
	private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	private Painter<MrdTemplate, MrdDto> details(String gene) {
		return (stream, template, dto)->{
			float y = cursorY();
			float height = 121 + 19*dto.histories().length;
			if(y - height < bottom+50)  {
				stream = newPage(stream);
				header().and(sign()).paint(stream, template, dto);
				y = cursorY();
				y -= 20;
			}
			y -= 30;
			stream.saveGraphicsState();
			stream.setLineWidth(0.25f).setNonStrokingColor(colorPrimary).addRect(60, y,100, 25).fill()
				  .setStrokingColor(colorSecondary)
				  .line(60, y+25.25f, 533, y+25.25f).stroke();
			stream.paragraph(110, y+8, 120, CENTER, new TextBlock(styleHeader, template.tmplMrdHistoryByGene().replace("%g", gene)));
			// String interpretation = dto.last()!=null&&dto.last().results().get(gene).target().clonalDepth()>0?template.tmplInterpretationPositive():template.tmplInterpretationNegative();
			String interpretation = template.tmplInterpretation(dto.last().results().get(gene).result()).replace("%g", gene);
			stream.paragraph(345, y+8, 400, CENTER, new TextBlock(styleHeader3, interpretation));
			y -= 40;
			stream.addRect(60, y,473, 40).fill();
			y += 20;
			stream.paragraph(80, y, 50, CENTER, MIDDLE, new TextBlock(styleHeader, template.lblHistoryTableNo()));
			stream.paragraph(135, y, 76, CENTER, MIDDLE, new TextBlock(styleHeader, template.lblHistoryTableDate()));
			stream.paragraph(203, y, 76, CENTER, MIDDLE, new TextBlock(styleHeader, template.tmplHistoryTableTotalReadDepth().replace("%g", gene)));
			stream.paragraph(273, y, 75, CENTER, MIDDLE, new TextBlock(styleHeader, template.tmplHistoryTableClonalReadDepth().replace("%g", gene)));
			stream.paragraph(348, y, 96, CENTER, MIDDLE, new TextBlock(styleHeader, template.lblCloneTableEquivalent()));
			stream.paragraph(425, y, 96, CENTER, MIDDLE, new TextBlock(styleHeader, template.lblHistoryTableDepthGenePct()));
			stream.paragraph(500.5f, y, 95, CENTER, MIDDLE, new TextBlock(styleHeader, template.lblHistoryTableDepthNucelatedPct()));

			y -= 10;
			NumberFormat decimals = NumberFormat.getInstance();
			NumberFormat percentage = new DecimalFormat("0.000");
			percentage.setMaximumFractionDigits(3);
			if(dto.histories()!=null) for(int i = dto.histories().length-1; i >= 0; --i) {
				y -= 20;
				MrdDto.MrdHistory row = dto.histories()[i];
				MrdDto.MrdDtoGeneResult result = row.results().get(gene);
				stream.paragraph(80, y, 50, CENTER, MIDDLE, new TextBlock(styleValue, row.no()));
				stream.paragraph(135, y, 76, CENTER, MIDDLE, new TextBlock(styleValue, row.date().format(dtf)));
				stream.paragraph(223, y, 76, RIGHT, MIDDLE, new TextBlock(styleValue, decimals.format(result.target().readDepth())));
				stream.paragraph(293, y, 75, RIGHT, MIDDLE, new TextBlock(styleValue, decimals.format(result.target().clonalDepth())));
				stream.paragraph(363, y, 86, RIGHT, MIDDLE, new TextBlock(styleValue, decimals.format(result.equivalent())));
				stream.paragraph(445, y, 86, RIGHT, MIDDLE, new TextBlock(styleValue, percentage.format(result.pctClonalBCells()*100)));
				stream.paragraph(530, y, 85, RIGHT, MIDDLE, new TextBlock(styleValue, percentage.format(row.pctClonalNucelatedCells(gene)*100)));
			}
			y -= 8;
			stream.line(60, y, 533, y)
				  .line(60, y-5, 533, y-5)
				  .line(60, y-25, 533, y-25).stroke();
			y -= 15;
			stream.paragraph(80, y, 50, CENTER, MIDDLE, new TextBlock(styleValue, dto.last().no()));
			stream.paragraph(130, y, 200, MIDDLE, new TextBlock(styleValue, "Total " + template.testInfo().cell() + " Count*"));
			stream.paragraph(240, y, 100, MIDDLE, new TextBlock(styleValue, NumberFormat.getInstance().format(dto.last().results().get(gene).bCells())));
			stream.paragraph(350, y, 200, MIDDLE, new TextBlock(styleValue, "Total Nucleated Cell Count***"));
			stream.paragraph(485, y, 100, MIDDLE, new TextBlock(styleValue, NumberFormat.getInstance().format(dto.last().nucleatedCells())));
			y -= 20;

			stream.paragraph(65, y, 473, new TextBlock(styleTextSub, template.lblComments()[0]));
			y -= 10;
			stream.paragraph(65, y, 473, new TextBlock(styleTextSub, template.lblComments()[1]));
			y -= 10;
			stream.paragraph(65, y, 473, new TextBlock(styleTextSub, template.lblComments()[2]));
			y-= 10;
			cursorY(y);
			stream.restoreGraphicsState();
			return stream;
		};
	}
	private Painter<MrdTemplate, MrdDto> graph(String gene) {
		final float HEIGHT_ROW = 15;
		final float HEIGHT_GRAPH = 7*HEIGHT_ROW;
		return (stream, template, dto)->{
			float y = cursorY();
			if(y - HEIGHT_GRAPH < bottom+50)  {
				stream = newPage(stream);
				header().and(sign()).paint(stream, template, dto);
				y = cursorY();
				y -= 20;
			}
			stream.saveGraphicsState();
			stream.setLineWidth(0.25f).setStrokingColor(colorSecondary)
				  .rect(90, y, 443, HEIGHT_GRAPH).stroke()
				  .setStrokingColor(colorGray)
				  .line(90, y-HEIGHT_ROW, 533, y-HEIGHT_ROW)
				  .line(90, y-HEIGHT_ROW*2, 533, y-HEIGHT_ROW*2)
				  .line(90, y-HEIGHT_ROW*3, 533, y-HEIGHT_ROW*3)
				  .line(90, y-HEIGHT_ROW*4, 533, y-HEIGHT_ROW*4)
				  .line(90, y-HEIGHT_ROW*5, 533, y-HEIGHT_ROW*5)
				  .line(90, y-HEIGHT_ROW*6, 533, y-HEIGHT_ROW*6)
				  .line(90, y-HEIGHT_ROW*7, 533, y-HEIGHT_ROW*7).stroke();
			float w = (533-90) / dto.histories().length;
			stream.paragraph(87, y-3, w, RIGHT, MIDDLE, new TextBlock(styleTextSub, "1000(%)"));
			stream.paragraph(87, y-HEIGHT_ROW, w, RIGHT, MIDDLE, new TextBlock(styleTextSub, "100"));
			stream.paragraph(87, y-HEIGHT_ROW*2, w, RIGHT, MIDDLE, new TextBlock(styleTextSub, "10"));
			stream.paragraph(87, y-HEIGHT_ROW*3, w, RIGHT, MIDDLE, new TextBlock(styleTextSub, "1"));
			stream.paragraph(87, y-HEIGHT_ROW*4, w, RIGHT, MIDDLE, new TextBlock(styleTextSub, "0.1"));
			stream.paragraph(87, y-HEIGHT_ROW*5, w, RIGHT, MIDDLE, new TextBlock(styleTextSub, "0.01"));
			stream.paragraph(87, y-HEIGHT_ROW*6, w, RIGHT, MIDDLE, new TextBlock(styleTextSub, "0.001"));
			stream.paragraph(87, y-HEIGHT_ROW*7, w, RIGHT, MIDDLE, new TextBlock(styleTextSub, "0"));
			float x = 90 - w/2;
			stream.setLineWidth(1.0f).setStrokingColor(template.resource().colorGraphLinePrimary());
			for(MrdDto.MrdHistory h: dto.histories()) {
				x += w;
				double v1 = h.results().get(gene).pctClonalBCells();
				double v2 = h.pctClonalNucelatedCells(gene);
				float p1 = (float)(v1>0?Math.max((Math.log10(v1) - 1) * HEIGHT_ROW, -HEIGHT_GRAPH):-HEIGHT_GRAPH);
				float p2 = (float)(v2>0?Math.max((Math.log10(v2) - 1) * HEIGHT_ROW, -HEIGHT_GRAPH):-HEIGHT_GRAPH);
				stream.paragraph(x, y-HEIGHT_GRAPH-12, w, CENTER, MIDDLE, new TextBlock(styleTextSub, h.no() + "\n" + dtf.format(h.date())));
				stream.setNonStrokingColor(template.resource().colorGraphLinePrimary()).circle(x, y+p1, 2).fill()
					  .setNonStrokingColor(template.resource().colorGraphLineSecondary()).circle(x, y+p2, 2).fill();
			}
			x = 90 - w/2;
			stream.setLineWidth(1.0f).setStrokingColor(template.resource().colorGraphLinePrimary());
			for(int i = 0; i < dto.histories().length; ++i) {
				x += w;
				MrdDto.MrdHistory h = dto.histories()[i];
				double v1 = h.results().get(gene).pctClonalBCells();
				float p1 = (float)(v1>0?Math.max((Math.log10(v1) - 1) * HEIGHT_ROW, -HEIGHT_GRAPH):-HEIGHT_GRAPH);
				if(i==0) stream.moveTo(x, y+p1);
				else stream.lineTo(x, y+p1);
			}
			stream.stroke();

			x = 90 - w/2;
			stream.setStrokingColor(template.resource().colorGraphLineSecondary());
			for(int i = 0; i < dto.histories().length; ++i) {
				x += w;
				MrdDto.MrdHistory h = dto.histories()[i];
				double v2 = h.pctClonalNucelatedCells(gene);
				float p2 = (float)(v2>0?Math.max((Math.log10(v2) - 1) * HEIGHT_ROW, -HEIGHT_GRAPH):-HEIGHT_GRAPH);
				if(i==0) stream.moveTo(x, y+p2);
				else stream.lineTo(x, y+p2);
			}
			stream.stroke();

			stream.setStrokingColor(template.resource().colorGraphLinePrimary());
			stream.setNonStrokingColor(template.resource().colorGraphLinePrimary()).circle(375, y-7, 2).fill()
				  .line(370, y-7,  380, y-7).stroke();
			stream.paragraph(385, y-7, 200, MIDDLE, new TextBlock(styleTextSub, template.lblClonalCellNumberTotalBCells().replace("\n", " ")));
			stream.setStrokingColor(template.resource().colorGraphLineSecondary());
			stream.setNonStrokingColor(template.resource().colorGraphLineSecondary()).circle(375, y-17, 2).fill()
				  .line(370, y-17, 380, y-17).stroke();
			stream.paragraph(385, y-17, 200, MIDDLE, new TextBlock(styleTextSub, template.lblClonalCellNumberTotalNucleatedCells().replace("\n", " ")));
			y -= HEIGHT_GRAPH;
			y -= 20;
			cursorY(y);
			stream.restoreGraphicsState();
			return stream;
		};
	}
	private Painter<MrdTemplate, MrdDto> interpretation(String gene) {
		return (stream, template, dto) -> {
			MrdDto.MrdDtoGeneResult result = dto.last().results().get(gene);
			if (result.interpretation() == null || result.interpretation().isEmpty()) return stream;
			stream.saveGraphicsState();
			float y = cursorY();
			if (y < bottom+60) {
				stream.restoreGraphicsState();
				stream = newPage(stream);
				header().and(sign()).paint(stream, template, dto);
				y = cursorY();
				stream.saveGraphicsState();
			}
			y -= 30;
			stream.setNonStrokingColor(colorPrimary).addRect(60, y - 7, 473, 20).fill();
			stream.paragraph(63, y, 115, new TextBlock(styleHeader, template.lblInterpretation()));
			y -= 20;
			String[] paragraphs = result.interpretation().split("\n", -1);
			for (String p : paragraphs) {
				TextBlock block = new TextBlock(styleText, p + "\n\n");
				float height = stream.height(473, block);
				if (y - height < 100) {
					stream.restoreGraphicsState();
					stream = newPage(stream);
					header().and(sign()).paint(stream, template, dto);
					y = cursorY() - 30;
					stream.setNonStrokingColor(colorPrimary).addRect(60, y - 7, 473, 20).fill();
					stream.paragraph(63, y, 115, new TextBlock(styleHeader, template.lblInterpretation()));
					y -= 20;
					stream.saveGraphicsState();
				}
				y = stream.paragraph(60, y, 473, block);
			}
			y -= 6;
			stream.setLineWidth(0.35f).setStrokingColor(colorSecondary).line(60, y, 533, y).stroke();
			cursorY(y - 20);
			stream.restoreGraphicsState();
			return stream;
		};
	}
	private Painter<MrdTemplate, MrdDto> info() {
		return method().and(qc()).and(limitation());
	}
	private Painter<MrdTemplate, MrdDto> method() {
		return (stream, template, dto)->{
			stream = newPage(stream);
			header().and(sign()).paint(stream, template, dto);
			float y = cursorY();

			stream.saveGraphicsState();
			y -= 30;
			stream.setNonStrokingColor(colorPrimary).addRect(60, y-6,473, 20).fill();
			stream.paragraph(65, y, 120, new TextBlock(styleHeader2, template.lblTestInfo()));
			y -= 20;
			stream.paragraph(60, y, 120, new TextBlock(styleHeader3, template.lblTestMethod()));
			y -= 5;
			stream.setLineWidth(1).setStrokingColor(colorSecondary)
				  .line(60, y, 533, y).stroke()
				  .setNonStrokingColor(template.resource().colorGray())
				  .addRect(60, y-1, 200, -100).fill();
			y-= 15;	stream.setStrokingColor(template.resource().colorGray())
							 .setLineWidth(0.25f).line(260, y-8, 533, y-8).stroke()
							 .paragraph(70, y, 200, new TextBlock(styleHeader4, template.lblTestMethodEnrichment()));
			stream.paragraph(280, y, 230, new TextBlock(styleValue, template.testInfo().method()));
			y-= 20;	stream.line(260, y-8, 533, y-8).stroke()
							 .paragraph(70, y, 200, new TextBlock(styleHeader4, template.lblTestPipeline()));
			stream.paragraph(280, y, 230, new TextBlock(styleValue, template.testInfo().pipeline()));
			y-= 20;	stream.line(260, y-8, 533, y-8).stroke()
							 .paragraph(70, y, 200, new TextBlock(styleHeader4, template.lblTestPanel()));
			stream.paragraph(280, y, 230, new TextBlock(styleValue, template.testInfo().panel()));
			y-= 20;	stream.line(260, y-8, 533, y-8).stroke()
							 .paragraph(70, y, 200, new TextBlock(styleHeader4, template.lblTestSequencing()));
			stream.paragraph(280, y, 230, new TextBlock(styleValue, template.testInfo().sequencing()));
			y-= 20;	stream.setLineWidth(1).setStrokingColor(colorSecondary)
							 .line(60, y-7, 533, y-7).stroke()
							 .paragraph(70, y, 200, new TextBlock(styleHeader4, template.lblTestReference()));
			stream.paragraph(280, y, 230, new TextBlock(styleValue, template.testInfo().reference()));
			cursorY(y);
			stream.restoreGraphicsState();
			return stream;
		};
	}
	private Painter<MrdTemplate, MrdDto> qc() {
		return (stream, template, dto)->{
			float y = cursorY();
			stream.saveGraphicsState();
			y -= 30;
			stream.paragraph(60, y, 120, new TextBlock(styleHeader3, template.lblTestQc()));
			y -= 5;
			stream.setLineWidth(1).setStrokingColor(colorSecondary)
				  .line(60, y, 533, y)
				  .line(60, y-42-template.testInfo().genes().length*40, 533, y-42-template.testInfo().genes().length*40).stroke().stroke()
				  .setNonStrokingColor(template.resource().colorGray())
				  .addRect(60, y-1, 100, -40-template.testInfo().genes().length*40)
				  .addRect(296.5f, y-1, 100, -40-template.testInfo().genes().length*40).fill();

			y-= 20; stream.paragraph(70, y-4, 100, new TextBlock(styleHeader4, template.lblInputDna()));
			stream.paragraph(250, y-4, 200, RIGHT, new TextBlock(styleValue, NumberFormat.getInstance().format(dto.last().inputDna())));
			stream.paragraph(306.5f, y+8, 100, new TextBlock(styleHeader4, template.lblTotalNucleatedCells()));
			stream.paragraph(487, y-4, 200, RIGHT, new TextBlock(styleValue,NumberFormat.getInstance().format(dto.last().nucleatedCells())));
			stream.setStrokingColor(template.resource().colorGray()).setLineWidth(0.25f);
			for(String gene: template.testInfo().genes()) {
				stream.line(60, y-22, 533, y-22).stroke();
				y -= 40;
				stream.paragraph(70, y + 4, 90, new TextBlock(styleHeader4, template.tmplTotalDepth().replace("%g", gene)));
				stream.paragraph(250, y - 4, 200, RIGHT, new TextBlock(styleValue, NumberFormat.getInstance().format(dto.last().results().get(gene).target().readDepth())));
				stream.paragraph(306.5f, y + 8, 100, new TextBlock(styleHeader4, template.tmplTotalBCell().replace("%g", gene)));
				stream.paragraph(487, y - 4, 200, RIGHT, new TextBlock(styleValue, NumberFormat.getInstance().format(dto.last().results().get(gene).bCells())));
			}
			y -= 32;
			stream.paragraph(65, y, 473, new TextBlock(styleTextSub, template.lblQcComments()[0]));
			y -= 10;
			stream.paragraph(65, y, 473, new TextBlock(styleTextSub, template.lblQcComments()[1]));
			cursorY(y);
			stream.restoreGraphicsState();
			return stream;
		};
	}
	private Painter<MrdTemplate, MrdDto> limitation() {
		return (stream, template, dto)->{
			float y = cursorY();
			stream.saveGraphicsState();
			y -= 30;
			stream.paragraph(60, y, 120, new TextBlock(styleHeader3, template.lblTestLimitation()));
			y -= 5;
			stream.setLineWidth(1).setStrokingColor(colorSecondary)
				  .line(60, y, 533, y).stroke().stroke();
			TextStyle styleTextLimitation = styleValue.clone().justify(true).paragraph(true);
			for(String limitation: template.lblLimitations()) {
				y -= 15;
				stream.paragraph(63, y+1, 10, new TextBlock(styleTextLimitation.clone().fontSize(4), "●"));
				y = stream.paragraph(70, y, 460, AlignHorizontal.JUSTIFY, new TextBlock(styleTextLimitation, limitation));
			}
			y -= 30;
			cursorY(y);
			stream.restoreGraphicsState();
			return stream;
		};
	}
	private Painter<MrdTemplate, MrdDto> sign() {
		return footer.and(sign);
	}

	private static String dashIfEmpty(String str) {
		if(str == null || str.trim().isEmpty()) return "-";
		else return str.trim();
	}
}
