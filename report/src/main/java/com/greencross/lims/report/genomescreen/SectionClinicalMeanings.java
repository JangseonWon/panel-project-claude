package com.greencross.lims.report.genomescreen;

import com.gcgenome.lims.report.TextBlock;
import com.gcgenome.lims.report.TextStyle;
import com.greencross.lims.report.builder.Util;
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

public class SectionClinicalMeanings implements Painter<GenomeScreenTemplate<GenomeScreenResource>, GenomeScreenDto> {
	private final static float HEADER_HEIGHT = 20;
	private final Painter<GenomeScreenTemplate<GenomeScreenResource>, GenomeScreenDto> newPage;
	private Integer mode = null;
	public SectionClinicalMeanings(Painter<GenomeScreenTemplate<GenomeScreenResource>, GenomeScreenDto> newPage) {
		this.newPage = newPage.and((s, t, d)->header(s, t, d));
	}
	@Override
	public PDPageContentStreamPageAccessible paint(PDPageContentStreamPageAccessible stream, GenomeScreenTemplate<GenomeScreenResource> template, GenomeScreenDto dto) throws IOException {
		stream = header(stream, template, dto);
		stream = tier1(stream, template);
		stream = tier2(stream, template, dto);
		stream = tier3(stream, template, dto);
		return stream;
	}
	private PDPageContentStreamPageAccessible header(PDPageContentStreamPageAccessible stream, GenomeScreenTemplate<GenomeScreenResource> template, GenomeScreenDto dto) throws IOException {
		stream.saveGraphicsState();
		var resource = template.resource();
		var y = stream.cursorY();
		y -= 20;
		TextStyle styleHeader = new TextStyle().color(Color.WHITE).fonts(resource.fontHeader(), resource.fontDefault()).fontSize(10).paragraph(true);
		TextStyle styleValue = new TextStyle().color(resource.colorText()).fonts(resource.fontText(), resource.fontDefault()).fontSize(10).justify(true).paragraph(true);
		stream.setNonStrokingColor(resource.colorPrimary()).rect(50, y, 497, HEADER_HEIGHT).fill();
		//Util.icon(stream, resource.imgIcons()[4], 50, y, HEADER_HEIGHT, HEADER_HEIGHT);
		stream.paragraph(55, y-HEADER_HEIGHT/2, 497, MIDDLE, new TextBlock(styleHeader, template.lblClinicalMeanings()));
		y -= HEADER_HEIGHT;
		y -= 2;
		stream.cursorY(y);
		if(mode == null) y = stream.paragraph(50, y-18, 497, JUSTIFY, new TextBlock(styleValue, template.lblClinicalMeaningInfo())) - 10;
		else if(mode == 1) y = tier1Header(stream, template).cursorY();
		else if(mode == 2) y = tier2Header(stream, template).cursorY();
		else if(mode == 3) y = tier3Header(stream, template, dto).cursorY();
		stream.restoreGraphicsState();
		return stream.cursorY(y);
	}
	private PDPageContentStreamPageAccessible tier1Header(PDPageContentStreamPageAccessible stream, GenomeScreenTemplate<GenomeScreenResource> template) throws IOException {
		mode = 1;
		stream.saveGraphicsState();
		var resource = template.resource();
		var y = stream.cursorY();
		TextStyle styleHeader = new TextStyle().color(Color.WHITE).fonts(resource.fontHeader(), resource.fontDefault()).fontSize(10);
		TextBlock block = new TextBlock(styleHeader, template.lblClinicalMeanings(GenomeScreenTemplate.Tier.Tier1));
		var height = stream.height(487, block) + 5;
		stream.setNonStrokingColor(resource.colorPrimary()).rect(50, y, 497, height).fill();
		//stream.paragraph(55, y-10, 10, new TextBlock(styleHeader, "▶"));
		stream.paragraph(55, y-height/2, 487, MIDDLE, block);
		y -= height;
		stream.restoreGraphicsState();
		return stream.cursorY(y);
	}
	private static final int GENE_PER_ROW = 10;
	private static final float GENE_WIDTH = 497f / GENE_PER_ROW;
	private PDPageContentStreamPageAccessible tier1(PDPageContentStreamPageAccessible stream, GenomeScreenTemplate<GenomeScreenResource> template) throws IOException {
		List<GenomeScreenTemplate.Gene> genes = Arrays.stream(template.diseases())
													  .map(d->d.subs()).flatMap(Arrays::stream)
													  .map(d->d.genes()).flatMap(Arrays::stream)
													  .filter(g->g.tier() == GenomeScreenTemplate.Tier.Tier1)
													  .distinct().sorted()
													  .collect(Collectors.toList());
		if(genes.isEmpty()) return stream;
		mode = 1;
		stream = tier1Header(stream, template);
		var resource = template.resource();
		var y = stream.cursorY();
		stream.saveGraphicsState();
		TextStyle styleHeader = new TextStyle().color(resource.colorPrimary()).fonts(resource.fontScientific(), resource.fontDefault()).fontSize(8);
		for(int i = 0; i < genes.size(); ++i) {
			if(i % GENE_PER_ROW == 0) y -= 10;
			float x = 50 + (i % GENE_PER_ROW)*GENE_WIDTH + GENE_WIDTH/2;
			stream.paragraph(x, y, GENE_WIDTH, CENTER, MIDDLE, new TextBlock(styleHeader, genes.get(i).name()));
		}
		y -= 10;
		stream.setStrokingColor(resource.colorDarkGray()).setLineWidth(0.25f).line(50, y, 547, y).stroke();
		y -= 10;
		stream.restoreGraphicsState();
		return stream.cursorY(y);
	}
	private PDPageContentStreamPageAccessible tier2Header(PDPageContentStreamPageAccessible stream, GenomeScreenTemplate<GenomeScreenResource> template) throws IOException {
		mode = 2;
		stream.saveGraphicsState();
		var resource = template.resource();
		var y = stream.cursorY();
		TextStyle styleHeader = new TextStyle().color(Color.WHITE).fonts(resource.fontHeader(), resource.fontDefault()).fontSize(10);
		TextBlock block = new TextBlock(styleHeader, template.lblClinicalMeanings(GenomeScreenTemplate.Tier.Tier2));
		var height = stream.height(487, block) + 5;
		stream.setNonStrokingColor(resource.colorPrimary()).rect(50, y, 497, height).fill();
		//stream.paragraph(55, y-10, 10, new TextBlock(styleHeader, "▶"));
		stream.paragraph(55, y-height/2, 487, MIDDLE, block);
		y -= height;
		stream.restoreGraphicsState();
		return stream.cursorY(y);
	}
	private static final float GENE_WIDTH_TIER2 = 80;
	private PDPageContentStreamPageAccessible tier2(PDPageContentStreamPageAccessible stream, GenomeScreenTemplate<GenomeScreenResource> template, GenomeScreenDto dto) throws IOException {
		List<GenomeScreenTemplate.Gene> genes = Arrays.stream(template.diseases())
													  .map(GenomeScreenTemplate.Disease::subs).flatMap(Arrays::stream)
													  .map(GenomeScreenTemplate.DiseaseSub::genes).flatMap(Arrays::stream)
													  .filter(g->g.tier() == GenomeScreenTemplate.Tier.Tier2)
													  .distinct().sorted()
													  .collect(Collectors.toList());
		if(genes.isEmpty()) return stream;
		mode = 2;
		stream = tier2Header(stream, template);
		stream.saveGraphicsState();
		stream.restoreGraphicsState();
		for(int i = 0; i < genes.size(); ++i) stream = tier2(stream, template, dto, genes.get(i));
		return stream.cursorY(stream.cursorY()-10);
	}
	private PDPageContentStreamPageAccessible tier2(PDPageContentStreamPageAccessible stream, GenomeScreenTemplate<GenomeScreenResource> template, GenomeScreenDto dto, GenomeScreenTemplate.Gene gene) throws IOException {
		var resource = template.resource();
		var y = stream.cursorY();
		TextStyle styleHeader = new TextStyle().color(resource.colorPrimary()).fonts(resource.fontScientific(), resource.fontDefault()).fontSize(8);
		TextStyle styleReference = new TextStyle().color(resource.colorText()).fonts(resource.fontText(), resource.fontDefault()).fontSize(7);
		TextStyle styleValue = new TextStyle().color(resource.colorText()).fonts(resource.fontText(), resource.fontDefault()).fontSize(7);
		String info = template.clinicalMeaning(gene);
		TextBlock meaning = new TextBlock(styleValue, info);
		float height = 20 + stream.height(497-GENE_WIDTH_TIER2-10, meaning);
		if(y - height < 110) {
			stream = newPage.paint(stream, template, dto);
			y = stream.cursorY();
		}
		float middle = y - height/2;
		stream.saveGraphicsState();
		stream.paragraph(50 + GENE_WIDTH_TIER2/2, middle, GENE_WIDTH_TIER2, CENTER, MIDDLE, new TextBlock(styleHeader, gene.name()));
		Util.italic(stream, 50 + GENE_WIDTH_TIER2 + 5, y - 12, new TextBlock(styleReference, template.clinicalReference(gene)));
		stream.paragraph(50 + GENE_WIDTH_TIER2 + 5, y - 25, 497-GENE_WIDTH_TIER2-10, JUSTIFY, meaning);
		stream.setStrokingColor(resource.colorDarkGray()).setLineWidth(0.25f)
			  .line(50 + GENE_WIDTH_TIER2, y-15, 547, y-15)
			  .line(50, y-height, 547, y-height).stroke();
		y -= height;
		stream.restoreGraphicsState();
		return stream.cursorY(y);
	}
	private PDPageContentStreamPageAccessible tier3Header(PDPageContentStreamPageAccessible stream, GenomeScreenTemplate<GenomeScreenResource> template, GenomeScreenDto dto) throws IOException {
		mode = 3;
		var resource = template.resource();
		var y = stream.cursorY();
		TextStyle styleHeader = new TextStyle().color(Color.WHITE).fonts(resource.fontHeader(), resource.fontDefault()).fontSize(10);
		TextStyle styleHeader2 = new TextStyle().color(Color.WHITE).fonts(resource.fontHeader(), resource.fontDefault()).fontSize(8);
		TextBlock block1 = new TextBlock(styleHeader, template.lblClinicalMeanings(GenomeScreenTemplate.Tier.Tier3).split("\n")[0]);
		TextBlock block2 = new TextBlock(styleHeader2, template.lblClinicalMeanings(GenomeScreenTemplate.Tier.Tier3).split("\n")[1]);
		var height = stream.height(487, block1) + stream.height(487, block2) + 5;
		List<GenomeScreenTemplate.Gene> genes = Arrays.stream(template.diseases())
				.map(GenomeScreenTemplate.Disease::subs).flatMap(Arrays::stream)
				.map(GenomeScreenTemplate.DiseaseSub::genes).flatMap(Arrays::stream)
				.filter(g->g.tier() == GenomeScreenTemplate.Tier.Tier3)
				.distinct().sorted()
				.collect(Collectors.toList());
		if(y - height + Math.ceil(genes.size()/(double)GENE_PER_ROW)*10 < 110) {
			stream = newPage.paint(stream, template, dto);
			y = stream.cursorY();
		} else {
			stream.saveGraphicsState();
			stream.setNonStrokingColor(resource.colorPrimary()).rect(50, y, 497, height).fill();
			// stream.paragraph(55, y-10, 10, new TextBlock(styleHeader, "▶"));
			stream.paragraph(55, y - stream.height(487, block1) / 2 - 1, 487, MIDDLE, block1);
			stream.paragraph(55, y - stream.height(487, block1) - 3 - stream.height(487, block2) / 2, 487, MIDDLE, block2);
			y -= height;
			stream.restoreGraphicsState();
		}
		return stream.cursorY(y);
	}
	private PDPageContentStreamPageAccessible tier3(PDPageContentStreamPageAccessible stream, GenomeScreenTemplate<GenomeScreenResource> template, GenomeScreenDto dto) throws IOException {
		List<GenomeScreenTemplate.Gene> genes = Arrays.stream(template.diseases())
													  .map(GenomeScreenTemplate.Disease::subs).flatMap(Arrays::stream)
													  .map(GenomeScreenTemplate.DiseaseSub::genes).flatMap(Arrays::stream)
													  .filter(g->g.tier() == GenomeScreenTemplate.Tier.Tier3)
													  .distinct().sorted()
													  .collect(Collectors.toList());
		if(genes.isEmpty()) return stream;
		mode = 3;
		stream = tier3Header(stream, template, dto);
		var resource = template.resource();
		var y = stream.cursorY();
		stream.saveGraphicsState();
		TextStyle styleHeader = new TextStyle().color(resource.colorPrimary()).fonts(resource.fontScientific(), resource.fontDefault()).fontSize(8);
		for(int i = 0; i < genes.size(); ++i) {
			if(i % GENE_PER_ROW == 0) y -= 10;
			float x = 50 + (i % GENE_PER_ROW)*GENE_WIDTH + GENE_WIDTH/2;
			stream.paragraph(x, y, GENE_WIDTH, CENTER, MIDDLE, new TextBlock(styleHeader, genes.get(i).name()));
		}
		y -= 10;
		stream.setStrokingColor(resource.colorDarkGray()).setLineWidth(0.25f).line(50, y, 547, y).stroke();
		y -= 10;
		stream.restoreGraphicsState();
		return stream.cursorY(y);
	}
}
