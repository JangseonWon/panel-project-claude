package com.greencross.lims.report.genomescreen;

import com.gcgenome.lims.report.TextBlock;
import com.gcgenome.lims.report.TextStyle;
import com.gcgenome.lims.report.func.PDPageContentStreamPageAccessible;
import com.gcgenome.lims.report.func.Painter;
import lombok.val;

import java.awt.*;
import java.io.IOException;
import java.util.Arrays;

import static com.gcgenome.lims.report.func.AlignHorizontal.CENTER;
import static com.gcgenome.lims.report.func.AlignVertical.MIDDLE;

public class SectionGeneList implements Painter<GenomeScreenTemplate<GenomeScreenResource>, GenomeScreenDto> {
	private final static float HEADER_HEIGHT = 20;
	private final static float WIDTH_CATEGORY = 230;
	private final static float WIDTH_DISEASE = 220;
	private final static float WIDTH_GENE = 497 - WIDTH_CATEGORY - WIDTH_DISEASE;
	private final static float START_CATEGORY = 50;
	private final static float START_DISEASE = START_CATEGORY + WIDTH_CATEGORY;
	private final static float START_GENE = START_DISEASE + WIDTH_DISEASE;
	private final Painter<GenomeScreenTemplate<GenomeScreenResource>, GenomeScreenDto> newPage;
	private boolean init = false;
	public SectionGeneList(Painter<GenomeScreenTemplate<GenomeScreenResource>, GenomeScreenDto> newPage) {
		this.newPage = newPage.and((s, t, d)->header(s, t));
	}
	@Override
	public PDPageContentStreamPageAccessible paint(PDPageContentStreamPageAccessible stream, GenomeScreenTemplate<GenomeScreenResource> template, GenomeScreenDto dto) throws IOException {
		stream = header(stream, template);
		for(val disease: template.diseases()) stream = table(stream, template, dto, disease);
		return stream;
	}
	private PDPageContentStreamPageAccessible header(PDPageContentStreamPageAccessible stream, GenomeScreenTemplate<GenomeScreenResource> template) throws IOException {
		stream.saveGraphicsState();
		var resource = template.resource();
		var y = stream.cursorY();
		y -= 20;
		TextStyle styleHeader = new TextStyle().color(Color.WHITE).fonts(resource.fontHeader(), resource.fontDefault()).fontSize(10).paragraph(true);
		stream.setNonStrokingColor(resource.colorPrimary()).rect(50, y, 497, HEADER_HEIGHT).fill();
		stream.paragraph(55, y-HEADER_HEIGHT/2, 497, MIDDLE, new TextBlock(styleHeader, template.lblGeneListTitle()));
		y -= HEADER_HEIGHT;
		y -= 0.5f;
		if(!init) {
			y -= 14.5f;
			TextStyle styleText = new TextStyle().color(resource.colorText()).fonts(resource.fontHeader(), resource.fontDefault()).fontSize(10);
			y = stream.paragraph(50, y, 497, new TextBlock(styleText, template.lblGeneListInfo()));
			y -= 5;
			init = true;
		}
		stream.setNonStrokingColor(resource.colorPrimary()).rect(START_CATEGORY, y, WIDTH_CATEGORY+WIDTH_DISEASE+WIDTH_GENE, 15).fill();
		TextStyle styleTableHeader = new TextStyle().color(Color.WHITE).fonts(resource.fontHeader(), resource.fontDefault()).fontSize(8);
		stream.paragraph(START_CATEGORY+(WIDTH_CATEGORY+WIDTH_DISEASE)/2, y-7.5f, WIDTH_CATEGORY+WIDTH_DISEASE, CENTER, MIDDLE, new TextBlock(styleTableHeader, template.lblDetailsDiseaseName()));
		stream.paragraph(START_GENE+WIDTH_GENE/2, y-7.5f, WIDTH_GENE, CENTER, MIDDLE, new TextBlock(styleTableHeader, template.lblDetailsGene()));
		y -= 15;
		stream.restoreGraphicsState();
		return stream.cursorY(y);
	}
	private PDPageContentStreamPageAccessible table(PDPageContentStreamPageAccessible stream, GenomeScreenTemplate<GenomeScreenResource> template, GenomeScreenDto dto, GenomeScreenTemplate.Disease disease) throws IOException {
		var y = stream.cursorY();
		var resource = template.resource();
		TextStyle styleTableValue = new TextStyle().color(resource.colorText()).fonts(resource.fontHeader(), resource.fontDefault()).fontSize(8);
		var tmp = stream;
		float height = (float) Arrays.stream(disease.subs())
							 .mapToDouble(s->{
								 var k = 0.0;
								 try { k = tmp.height(WIDTH_CATEGORY, new TextBlock(styleTableValue, template.diseaseSubName(s)));} catch(Exception ignore){}
								 return Math.max(s.genes().length*15, k);
							 }).sum();
		if(y - height < 110) {
			stream = newPage.paint(stream, template, dto);
			y = stream.cursorY();
		}
		stream.saveGraphicsState();
		stream.setLineWidth(0.25f);
		for(GenomeScreenTemplate.DiseaseSub sub: disease.subs()) {
			float h = Math.max(sub.genes().length * 15, stream.height(WIDTH_CATEGORY, new TextBlock(styleTableValue, template.diseaseSubName(sub))));
			float rowPerGene = h / sub.genes().length;
			var startY = y;
			for(int i = 0; i < sub.genes().length; ++i) {
				if(i < sub.genes().length-1) stream.setStrokingColor(Color.GRAY).line(START_GENE, y - rowPerGene, 547, y - rowPerGene).stroke();
				stream.paragraph(START_GENE+WIDTH_GENE/2, y-rowPerGene/2, WIDTH_GENE, CENTER, MIDDLE, new TextBlock(styleTableValue, sub.genes()[i].name()));
				y -= rowPerGene;
			}
			var middle = y - (y - startY)/2;
			stream.paragraph(START_DISEASE+WIDTH_DISEASE/2, middle, WIDTH_CATEGORY, CENTER, MIDDLE, new TextBlock(styleTableValue, template.diseaseSubName(sub)));

			stream.setStrokingColor(Color.DARK_GRAY).line(START_DISEASE, y, 547, y).stroke();
		}
		var middle = y + height/2;
		stream.paragraph(START_CATEGORY+WIDTH_CATEGORY/2, middle, WIDTH_CATEGORY, CENTER, MIDDLE, new TextBlock(styleTableValue, template.diseaseName(disease)));
		stream.setStrokingColor(Color.DARK_GRAY).line(START_CATEGORY, y, 547, y).stroke();
		stream.restoreGraphicsState();
		return stream.cursorY(y);
	}
}
