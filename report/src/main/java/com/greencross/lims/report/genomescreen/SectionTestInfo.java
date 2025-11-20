package com.greencross.lims.report.genomescreen;

import com.gcgenome.lims.report.TextBlock;
import com.gcgenome.lims.report.TextStyle;
import com.gcgenome.lims.report.func.PDPageContentStreamPageAccessible;
import com.gcgenome.lims.report.func.Painter;

import java.awt.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.gcgenome.lims.report.func.AlignHorizontal.CENTER;
import static com.gcgenome.lims.report.func.AlignHorizontal.JUSTIFY;
import static com.gcgenome.lims.report.func.AlignVertical.MIDDLE;

public class SectionTestInfo implements Painter<GenomeScreenTemplate<GenomeScreenResource>, GenomeScreenDto> {
	private final static float HEADER_HEIGHT = 20;
	private final Painter<GenomeScreenTemplate<GenomeScreenResource>, GenomeScreenDto> newPage;
	public SectionTestInfo(Painter<GenomeScreenTemplate<GenomeScreenResource>, GenomeScreenDto> newPage) {
		this.newPage = newPage.and((s, t, d)->header(s, t));
	}
	@Override
	public PDPageContentStreamPageAccessible paint(PDPageContentStreamPageAccessible stream, GenomeScreenTemplate<GenomeScreenResource> template, GenomeScreenDto dto) throws IOException {
		stream = header(stream, template);
		stream = testInfo(stream, template);
		stream = ngsInfo(stream, template);
		stream = geneInfo(stream, template);
		return stream;
	}
	private PDPageContentStreamPageAccessible header(PDPageContentStreamPageAccessible stream, GenomeScreenTemplate<GenomeScreenResource> template) throws IOException {
		stream.saveGraphicsState();
		var resource = template.resource();
		var y = stream.cursorY();
		y -= 20;
		TextStyle styleHeader = new TextStyle().color(Color.WHITE).fonts(resource.fontHeader(), resource.fontDefault()).fontSize(10).paragraph(true);
		stream.setNonStrokingColor(resource.colorPrimary()).rect(50, y, 497, HEADER_HEIGHT).fill();
		// Util.icon(stream, resource.imgIcons()[2], 50, y, HEADER_HEIGHT, HEADER_HEIGHT);
		stream.paragraph(55, y-HEADER_HEIGHT/2, 497, MIDDLE, new TextBlock(styleHeader, template.lblTestInfoTitle()));
		y -= HEADER_HEIGHT;
		y -= 15;
		stream.restoreGraphicsState();
		return stream.cursorY(y);
	}
	private final static float LABEL_WIDTH = 150;
	private PDPageContentStreamPageAccessible testInfo(PDPageContentStreamPageAccessible stream, GenomeScreenTemplate<GenomeScreenResource> template) throws IOException {
		var y = stream.cursorY();
		var resource = template.resource();
		stream.saveGraphicsState();
		TextStyle styleHeader = new TextStyle().color(Color.WHITE).fonts(resource.fontHeader(), resource.fontDefault()).fontSize(10).paragraph(true);
		TextStyle styleValue = new TextStyle().color(resource.colorText()).fonts(resource.fontText(), resource.fontDefault()).fontSize(10).paragraph(true);
		stream.setNonStrokingColor(resource.colorPrimary())
			 .rect(50, y, LABEL_WIDTH, HEADER_HEIGHT)
			 .fill()
			 .setNonStrokingColor(resource.colorGray())
			 .rect(50+LABEL_WIDTH, y, 497-LABEL_WIDTH, HEADER_HEIGHT)
			 .fill();
		stream.paragraph(50+LABEL_WIDTH/2, y-HEADER_HEIGHT/2, LABEL_WIDTH, CENTER, MIDDLE, new TextBlock(styleHeader, template.lblTestInfoSpecimen()));
		stream.paragraph(50+LABEL_WIDTH+10, y-HEADER_HEIGHT/2, 497-LABEL_WIDTH, MIDDLE, new TextBlock(styleValue, template.testInfoSpecimen()));
		y -= HEADER_HEIGHT + 10;
		stream.setNonStrokingColor(resource.colorPrimary())
			 .rect(50, y, LABEL_WIDTH, HEADER_HEIGHT)
			 .fill()
			 .setNonStrokingColor(resource.colorGray())
			 .rect(50+LABEL_WIDTH, y, 497-LABEL_WIDTH, HEADER_HEIGHT)
			 .fill();
		stream.paragraph(50+LABEL_WIDTH/2, y-HEADER_HEIGHT/2, LABEL_WIDTH, CENTER, MIDDLE, new TextBlock(styleHeader, template.lblTestInfoMethod()));
		stream.paragraph(50+LABEL_WIDTH+10, y-HEADER_HEIGHT/2, 497-LABEL_WIDTH, MIDDLE, new TextBlock(styleValue, template.testInfoMethod()));
		y -= 30;
		stream.restoreGraphicsState();
		return stream.cursorY(y);
	}
	private PDPageContentStreamPageAccessible ngsInfo(PDPageContentStreamPageAccessible stream, GenomeScreenTemplate<GenomeScreenResource> template) throws IOException {
		var y = stream.cursorY();
		var resource = template.resource();
		stream.saveGraphicsState();
		TextStyle styleHeader = new TextStyle().color(resource.colorPrimary()).fonts(resource.fontHeader(), resource.fontDefault()).fontSize(10);
		TextStyle styleValue = new TextStyle().color(resource.colorText()).fonts(resource.fontText(), resource.fontDefault()).fontSize(10).justify(true).paragraph(true);
		y -= 10;
		stream.paragraph(50, y, 497, new TextBlock(styleHeader, template.lblTestInfoNgs()));
		y = stream.paragraph(50, y-15, 497, JUSTIFY, new TextBlock(styleValue, template.testInfoNgs()));
		y -= 5;
		stream.restoreGraphicsState();
		return stream.cursorY(y);
	}
	private final static float GENE_HEADER_WIDTH = 60;
	private final static float GENE_VALUE_WIDTH = 497/3.0f - GENE_HEADER_WIDTH;
	private final static float GENE_ROW_HEIGHT = 20;
	private PDPageContentStreamPageAccessible geneInfo(PDPageContentStreamPageAccessible stream, GenomeScreenTemplate<GenomeScreenResource> template) throws IOException {
		var y = stream.cursorY();
		var resource = template.resource();
		stream.saveGraphicsState();
		y -= 10;
		TextStyle styleHeader = new TextStyle().color(Color.WHITE).fonts(resource.fontHeader(), resource.fontDefault()).fontSize(10).paragraph(true);
		stream.setNonStrokingColor(resource.colorPrimary())
			  .rect(50, y, 497, HEADER_HEIGHT)
			  .fill();
		stream.paragraph(297.5f, y-HEADER_HEIGHT/2, 497, CENTER, MIDDLE, new TextBlock(styleHeader, template.lblReferenceTranscriptTitle()));
		y -= HEADER_HEIGHT;
		List<GenomeScreenTemplate.Gene> genes = Arrays.stream(template.diseases())
													  .map(d->d.subs()).flatMap(Arrays::stream)
													  .map(d->d.genes()).flatMap(Arrays::stream).distinct().sorted()
													  .collect(Collectors.toList());
		int row = genes.size()/3;
		int idx2 = row;
		if(genes.size()%3 > 0) idx2++;
		int idx3 = idx2+row;
		if(genes.size()%3 > 1) idx3++;
		stream.setNonStrokingColor(resource.colorGray())
			  .rect(50, y, GENE_HEADER_WIDTH, HEADER_HEIGHT*idx2)
			  .rect(50+GENE_HEADER_WIDTH+GENE_VALUE_WIDTH, y, GENE_HEADER_WIDTH, HEADER_HEIGHT*(idx3-idx2))
			  .rect(50+GENE_HEADER_WIDTH*2+GENE_VALUE_WIDTH*2, y, GENE_HEADER_WIDTH, HEADER_HEIGHT*row)
			  .fill();
		TextStyle styleValue = new TextStyle().color(resource.colorText()).fonts(resource.fontScientific(), resource.fontDefault()).fontSize(8).justify(true).paragraph(true);
		stream.setStrokingColor(Color.DARK_GRAY).setLineWidth(0.25f);
		for(int i = 0; i < idx2; ++i) {
			var gene1 = genes.get(i);
			var gene2 = (idx2+i < idx3)?genes.get(idx2+i):null;
			var gene3 = (idx3+i < genes.size())?genes.get(idx3+i):null;
			var x = 50;
			stream.paragraph(x+GENE_HEADER_WIDTH/2, y-10, GENE_HEADER_WIDTH, CENTER, MIDDLE, new TextBlock(styleValue, gene1.name()));						x += GENE_HEADER_WIDTH;
			stream.paragraph(x+GENE_VALUE_WIDTH/2, y-10, GENE_VALUE_WIDTH, CENTER, MIDDLE, new TextBlock(styleValue, gene1.transcript()));					x += GENE_VALUE_WIDTH;
			if(gene2!=null) stream.paragraph(x+GENE_HEADER_WIDTH/2, y-10, GENE_HEADER_WIDTH, CENTER, MIDDLE, new TextBlock(styleValue, gene2.name()));		x += GENE_HEADER_WIDTH;
			if(gene2!=null) stream.paragraph(x+GENE_VALUE_WIDTH/2, y-10, GENE_VALUE_WIDTH, CENTER, MIDDLE, new TextBlock(styleValue, gene2.transcript()));	x += GENE_VALUE_WIDTH;
			if(gene3!=null) stream.paragraph(x+GENE_HEADER_WIDTH/2, y-10, GENE_HEADER_WIDTH, CENTER, MIDDLE, new TextBlock(styleValue, gene3.name()));		x += GENE_HEADER_WIDTH;
			if(gene3!=null) stream.paragraph(x+GENE_VALUE_WIDTH/2, y-10, GENE_VALUE_WIDTH, CENTER, MIDDLE, new TextBlock(styleValue, gene3.transcript()));
			stream.line(50, y, 547, y).stroke();
			y -= HEADER_HEIGHT;
		}
		stream.line(50, y, 547, y).stroke();
		stream.paragraph(50, y-10, 497, new TextBlock(styleValue, template.lblSangerIgnored()));
		stream.restoreGraphicsState();
		return stream.cursorY(y);
	}
}
