package com.greencross.lims.report.mrd;

import com.gcgenome.lims.report.TextBlock;
import com.gcgenome.lims.report.TextStyle;
import com.gcgenome.lims.report.func.AlignHorizontal;
import com.gcgenome.lims.report.func.Page;
import com.gcgenome.lims.report.func.Painter;
import com.gcgenome.lims.report.func.WhiteSpace;
import com.greencross.lims.report.SectionBarcode;
import com.greencross.lims.report.SectionLDT;
import com.greencross.lims.report.builder.LogoType;

import java.awt.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import static com.gcgenome.lims.report.func.AlignHorizontal.RIGHT;
import static com.gcgenome.lims.report.func.AlignVertical.MIDDLE;

public class MrdScreenPage extends Page<MrdScreenTemplate> {
	private final Painter<MrdScreenTemplate, MrdScreenDto> barcode = new SectionBarcode<>();
	private final Painter<MrdScreenTemplate, MrdScreenDto> ldt;
	private final Painter<MrdScreenTemplate, MrdScreenDto> footer;
	private final Painter<MrdScreenTemplate, MrdScreenDto> sign;
	private final Painter<MrdScreenTemplate, MrdScreenDto> page;
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
	public MrdScreenPage(MrdScreenTemplate template, Painter<MrdScreenTemplate, MrdScreenDto> footer, Painter<MrdScreenTemplate, MrdScreenDto> sign, Painter<MrdScreenTemplate, MrdScreenDto> page) {
		super(template);
		this.footer = footer;
		this.sign = sign;
		this.page = page;
		if(template.logoType() == LogoType.INDEPENDENT) bottom = 90;
		else if(template.logoType() == LogoType.DEPENDENT) bottom = 120;
		else bottom = 90;
		ldt = new SectionLDT<>(template.logoType(), bottom);
	}

	public Painter<MrdScreenTemplate, MrdScreenDto> page() {
		return initialize().and(barcode)
				.and(header())
				.and(sign())
				.and(summary())
				.and(details())
				.and(info())
				.and(ldt)
				.and(page);
	}
	private Painter<MrdScreenTemplate, MrdScreenDto> initialize() {
		return (stream, template, dto) -> {
			MrdScreenResource resource = template.resource();
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
	private Painter<MrdScreenTemplate, MrdScreenDto> header() {
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
								 new TextBlock(styleValueTitle, dashIfEmpty(template.age(dto.age(), dto.birthDate(), dto.collectionDate()))),
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
	private Painter<MrdScreenTemplate, MrdScreenDto> summary() {
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
			stream.paragraph(90, y+12, 60, AlignHorizontal.CENTER, new TextBlock(styleHeader, template.lblGene()));
			stream.paragraph(233, y+12, 200, AlignHorizontal.CENTER, new TextBlock(styleHeader, template.lblTotalClone()));
			y = stream.paragraph(403, y+17, 200, AlignHorizontal.CENTER, new TextBlock(styleHeader, template.lblClonalCellNumber()));
			NumberFormat fmtPct = NumberFormat.getInstance();
			fmtPct.setMaximumFractionDigits(2);
			fmtPct.setMinimumFractionDigits(2);
			for(int i = 0; i < template.testInfo().genes().length; ++i) {
				y -= 20;
				String gene = template.testInfo().genes()[i];
				MrdScreenDto.MrdScreenDtoGeneResult result = dto.results().get(gene);
				stream.paragraph(90, y, 60, AlignHorizontal.CENTER, new TextBlock(styleValue, gene));
				stream.paragraph(233, y, 200, AlignHorizontal.CENTER, new TextBlock(styleValue, String.valueOf(result.clones().length)));
				stream.paragraph(403, y, 200, AlignHorizontal.CENTER, new TextBlock(styleValue, fmtPct.format(result.totalClonalCells())));
			}
			y -= 8;
			stream.setLineWidth(1).line(60, y, 533, y).stroke();
			if(dto.somaticMutations()!=null && !dto.somaticMutations().isEmpty()) {
				y -= 10;
				stream.paragraph(62, y, 500, MIDDLE, new TextBlock(styleValue, template.lblSomaticHypermutationStatus()));
				for(var mutation: dto.somaticMutations()) {
					y -= 12;
					String clone = mutation.clone();
					String status = mutation.hyperMutation();
					String rate = mutation.mutationRate();
					if(rate!=null && status.contains("TEXT")) status = status.replace("TEXT", rate);
					stream.paragraph(62, y, 500, MIDDLE, new TextBlock(styleValue, clone + ": " + status));
				}
			}
			cursorY(y);
			stream.restoreGraphicsState();
			return stream;
		};
	}
	private Painter<MrdScreenTemplate, MrdScreenDto> details() {
		return (stream, template, dto)->{
			stream.saveGraphicsState();
			float y = cursorY();
			y -= 30;

			stream.setNonStrokingColor(colorPrimary).addRect(60, y-5,473, 20).fill()
				  .setLineWidth(0.25f).setStrokingColor(colorSecondary)
				  .line(60, y-4.6f, 533, y-4.6f).stroke()
				  .setNonStrokingColor(colorPrimary);
			stream.paragraph(65, y+2, 60, new TextBlock(styleHeader2, template.lblDetails()));
			cursorY(y);
			stream.restoreGraphicsState();
			for(String gene: template.testInfo().genes()) {
				stream = details(gene).and(sequences(gene)).and(interpretation(gene)).paint(stream, template, dto);
			}
			return stream;
		};
	}
	private Painter<MrdScreenTemplate, MrdScreenDto> details(String gene) {
		return (stream, template, dto)->{
			float y = cursorY();
			MrdScreenDto.MrdScreenDtoGeneResult result = dto.results().get(gene);
			float height = 55 + 19*result.clones().length;
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
			stream.paragraph(110, y+8, 120, AlignHorizontal.CENTER, new TextBlock(styleHeader, template.tmplCountClonesByGene().replace("%g", gene)));
			String interpretation = result.clones()!=null&&result.clones().length>0?template.tmplInterpretationPositive():template.tmplInterpretationNegative();
			interpretation = interpretation.replace("%g", gene).replace("%d", String.valueOf(result.clones()!=null?result.clones().length:0));
			stream.paragraph(345, y+7, 400, AlignHorizontal.CENTER, new TextBlock(styleHeader3, interpretation));
			y -= 30;
			stream.addRect(60, y,473, 30).fill();
			stream.paragraph(70, y+13, 20, AlignHorizontal.CENTER, new TextBlock(styleHeader, template.lblCloneTableNo()));
			stream.paragraph(113, y+13, 76, AlignHorizontal.CENTER, new TextBlock(styleHeader, template.lblCloneTableRegionV()));
			stream.paragraph(189, y+13, 76, AlignHorizontal.CENTER, new TextBlock(styleHeader, template.lblCloneTableRegionJ()));
			stream.paragraph(256.5f, y+13, 75, AlignHorizontal.CENTER, new TextBlock(styleHeader, template.lblCloneTableLength()));
			stream.paragraph(334.5f, y+18, 75, AlignHorizontal.CENTER, new TextBlock(styleHeader, template.tmplCloneTableDepth().replace("%g", gene)));
			stream.paragraph(415, y+18, 96, AlignHorizontal.CENTER, new TextBlock(styleHeader, template.lblCloneTableEquivalent()));
			stream.paragraph(500.5f, y+18, 65, AlignHorizontal.CENTER, new TextBlock(styleHeader, template.tmplCloneTableDepthPct().replace("%g", gene)));
			y -= 5;

			NumberFormat decimals = NumberFormat.getInstance();
			NumberFormat percentage = new DecimalFormat("0.00");
			percentage.setMaximumFractionDigits(2);
			if(result.clones()!=null && result.clones().length > 0) for(int i = 0; i < result.clones().length; ++i) {
				y -= 20;
				stream.paragraph(70, y+8, 20, AlignHorizontal.CENTER, new TextBlock(styleValue, result.clones()[i].no()));
				stream.paragraph(113, y+8, 76, AlignHorizontal.CENTER, new TextBlock(styleValue, result.clones()[i].regionV()));
				stream.paragraph(189, y+8, 76, AlignHorizontal.CENTER, new TextBlock(styleValue, result.clones()[i].regionJ()));
				stream.paragraph(270, y+8, 75, RIGHT, new TextBlock(styleValue, decimals.format(result.clones()[i].length())));
				stream.paragraph(355, y+8, 75, RIGHT, new TextBlock(styleValue, decimals.format(result.clones()[i].depth())));
				stream.paragraph(440, y+8, 86, RIGHT, new TextBlock(styleValue, decimals.format(result.clones()[i].equivalent(result.lqic().depth()))));
				stream.paragraph(530, y+8, 65, RIGHT, new TextBlock(styleValue, percentage.format(result.clones()[i].coverage(result.lqic().depth(), result.bCells()))));
			} else {
				y -= 20;
				stream.paragraph(297.5f, y+8, 500, AlignHorizontal.CENTER, new TextBlock(styleValue, template.lblEmptyResult()));
			}
			stream.line(60, y, 533, y)
				  .line(60, y-5, 533, y-5)
				  .line(60, y-25, 533, y-25)
				  .stroke();
			y -= 18;
			stream.paragraph(80, y, 200, new TextBlock(styleValue, template.tmplTotalDepth().replace("%g", gene)));
			stream.paragraph(220, y, 100, new TextBlock(styleValue, NumberFormat.getInstance().format(dto.results().get(gene).depth())));
			stream.paragraph(370, y, 200, new TextBlock(styleValue, "Total " + template.testInfo().cell() + " Count*"));
			stream.paragraph(485, y, 100, new TextBlock(styleValue, NumberFormat.getInstance().format(dto.results().get(gene).bCells())));
			y -= 17;
			stream.paragraph(65, y, 473, new TextBlock(styleTextSub, template.lblComments()[0]));
			y -= 10;
			stream.paragraph(65, y, 473, new TextBlock(styleTextSub, template.lblComments()[1]));
			cursorY(y);
			stream.restoreGraphicsState();
			return stream;
		};
	}
	private Painter<MrdScreenTemplate, MrdScreenDto> sequences(String gene) {
		return (stream, template, dto)->{
			MrdScreenDto.MrdScreenDtoGeneResult result = dto.results().get(gene);
			if(result.clones() == null || result.clones().length <= 0) {
				cursorY(cursorY() - 30);
				return stream;
			}

			TextStyle styleSequence = styleValue.clone().fontSize(8);
			float y = cursorY();
			float height = stream.height(315, WhiteSpace.BREAK_ALL, new TextBlock(styleSequence, result.lqic().sequence()));
			if(result.clones()!=null) for(int i = 0; i < result.clones().length; ++i) {
				height += 5 + stream.height(315, WhiteSpace.BREAK_ALL, new TextBlock(styleSequence, result.clones()[i].sequence()));
			}
			height -= 30;
			if(y - height < bottom+60) {
				stream = newPage(stream);
				header().and(sign()).paint(stream, template, dto);
				y = cursorY();
			}
			stream.saveGraphicsState();
			y -= 30;

			stream.setLineWidth(0.25f).setNonStrokingColor(colorPrimary).setStrokingColor(colorSecondary)
				  .addRect(60, y,473, 20).fill();

			stream.paragraph(70, y+8, 20, AlignHorizontal.CENTER, new TextBlock(styleHeader, template.lblCloneTableNo()));
			stream.paragraph(113, y+8, 76, AlignHorizontal.CENTER, new TextBlock(styleHeader, template.lblCloneTableRegionV()));
			stream.paragraph(189, y+8, 76, AlignHorizontal.CENTER, new TextBlock(styleHeader, template.lblCloneTableRegionJ()));
			stream.paragraph(377, y+8, 315, AlignHorizontal.CENTER, new TextBlock(styleHeader, template.tmplClonalSequence().replace("%g", gene)));
			NumberFormat percentage = new DecimalFormat("0.00");
			percentage.setMaximumFractionDigits(2);
			if(result.clones()!=null) for(int i = 0; i < result.clones().length; ++i) {
				y -= 25;
				stream.paragraph(70, y+8, 20, AlignHorizontal.CENTER, new TextBlock(styleValue, result.clones()[i].no()));
				stream.paragraph(113, y+8, 76, AlignHorizontal.CENTER, new TextBlock(styleValue, result.clones()[i].regionV()));
				stream.paragraph(189, y+8, 76, AlignHorizontal.CENTER, new TextBlock(styleValue, result.clones()[i].regionJ()));
				y = stream.paragraph(220, y+10, 315,  WhiteSpace.BREAK_ALL, new TextBlock(styleSequence, result.clones()[i].sequence()));
			}
			y -= 8;
			stream.line(60, y, 533, y).stroke();
			y -= 10;
			cursorY(y);
			stream.restoreGraphicsState();
			return stream;
		};
	}
	private Painter<MrdScreenTemplate, MrdScreenDto> interpretation(String gene) {
		return (stream, template, dto) -> {
			MrdScreenDto.MrdScreenDtoGeneResult result = dto.results().get(gene);
			if(result.interpretation() == null || result.interpretation().isEmpty()) return stream;
			stream.saveGraphicsState();
			float y = cursorY();
			if(y < bottom+60) {
				stream.restoreGraphicsState();
				stream = newPage(stream);
				header().and(sign()).paint(stream, template, dto);
				y = cursorY() - 30;
				stream.saveGraphicsState();
			}
			stream.setNonStrokingColor(colorPrimary).addRect(60,y-7,473, 20).fill();
			stream.paragraph(63, y, 115, new TextBlock(styleHeader, template.lblInterpretation()));
			y -= 20;
			// y = stream.paragraph(60, y, 473, new TextBlock(styleText, report.interpretation()));
			String[] paragraphs = result.interpretation().split("\n", -1);
			for(String p: paragraphs) {
				TextBlock block = new TextBlock(styleText, p + "\n\n");
				float height = stream.height(473, block);
				if(y - height < bottom+10) {
					stream.restoreGraphicsState();
					stream = newPage(stream);
					header().and(sign()).paint(stream, template, dto);
					y = cursorY() - 30;
					stream.setNonStrokingColor(colorPrimary).addRect(60,y-7,473, 20).fill();
					stream.paragraph(63, y, 115, new TextBlock(styleHeader, template.lblInterpretation()));
					y -= 20;
					stream.saveGraphicsState();
				}
				y = stream.paragraph(60, y, 473, block);
			}
			y -= 6;
			stream.setLineWidth(0.35f).setStrokingColor(colorSecondary).line(60, y, 533, y).stroke();
			cursorY(y-20);
			stream.restoreGraphicsState();
			return stream;
		};
	}

	private Painter<MrdScreenTemplate, MrdScreenDto> info() {
		return method().and(qc()).and(limitation());
	}
	private Painter<MrdScreenTemplate, MrdScreenDto> method() {
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
	private Painter<MrdScreenTemplate, MrdScreenDto> qc() {
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
					stream.paragraph(250, y-4, 200, RIGHT, new TextBlock(styleValue, NumberFormat.getInstance().format(dto.inputDna())));
					stream.paragraph(306.5f, y+8, 100, new TextBlock(styleHeader4, template.lblTotalNucleatedCells()));
					stream.paragraph(487, y-4, 200, RIGHT, new TextBlock(styleValue,NumberFormat.getInstance().format(dto.nucleatedCells())));
			stream.setStrokingColor(template.resource().colorGray()).setLineWidth(0.25f);
			for(String gene: template.testInfo().genes()) {
				stream.line(60, y-22, 533, y-22).stroke();
				y -= 40;
				stream.paragraph(70, y + 4, 90, new TextBlock(styleHeader4, template.tmplTotalDepth().replace("%g", gene)));
				stream.paragraph(250, y - 4, 200, RIGHT, new TextBlock(styleValue, NumberFormat.getInstance().format(dto.results().get(gene).depth())));
				stream.paragraph(306.5f, y + 8, 100, new TextBlock(styleHeader4, template.tmplTotalBCell().replace("%g", gene)));
				stream.paragraph(487, y - 4, 200, RIGHT, new TextBlock(styleValue, NumberFormat.getInstance().format(dto.results().get(gene).bCells())));
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
	private Painter<MrdScreenTemplate, MrdScreenDto> limitation() {
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
	private Painter<MrdScreenTemplate, MrdScreenDto> sign() {
		return footer.and(sign);
	}

	private static String dashIfEmpty(String str) {
		if(str == null || str.trim().isEmpty()) return "-";
		else return str.trim();
	}
}
