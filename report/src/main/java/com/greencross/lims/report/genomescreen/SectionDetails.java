package com.greencross.lims.report.genomescreen;

import com.gcgenome.lims.report.TextBlock;
import com.gcgenome.lims.report.TextStyle;
import com.greencross.lims.report.builder.Util;
import com.gcgenome.lims.report.func.PDPageContentStreamPageAccessible;
import com.gcgenome.lims.report.func.Painter;
import lombok.val;

import java.awt.*;
import java.io.IOException;
import java.util.Arrays;

import static com.gcgenome.lims.report.func.AlignHorizontal.CENTER;
import static com.gcgenome.lims.report.func.AlignHorizontal.JUSTIFY;
import static com.gcgenome.lims.report.func.AlignVertical.MIDDLE;

public class SectionDetails implements Painter<GenomeScreenTemplate<GenomeScreenResource>, GenomeScreenDto> {
	private final static float HEADER_HEIGHT = 20;
	private final Painter<GenomeScreenTemplate<GenomeScreenResource>, GenomeScreenDto> newPage;
	public SectionDetails(Painter<GenomeScreenTemplate<GenomeScreenResource>, GenomeScreenDto> newPage) {
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
		stream.paragraph(55, y-HEADER_HEIGHT/2, 497, MIDDLE, new TextBlock(styleHeader, template.lblDetailsTitle()));
		y -= HEADER_HEIGHT;
		y -= 15;
		stream.restoreGraphicsState();
		return stream.cursorY(y);
	}
	private PDPageContentStreamPageAccessible table(PDPageContentStreamPageAccessible stream, GenomeScreenTemplate<GenomeScreenResource> template, GenomeScreenDto dto, GenomeScreenTemplate.Disease disease) throws IOException {
		var y = stream.cursorY();
		var resource = template.resource();
		TextStyle styleValue = new TextStyle().color(resource.colorText()).fonts(resource.fontText(), resource.fontDefault()).fontSize(10).justify(true).paragraph(true);
		TextStyle styleTableValue = new TextStyle().color(resource.colorText()).fonts(resource.fontHeader(), resource.fontDefault()).fontSize(8);
		TextBlock blockInfo = new TextBlock(styleValue, template.diseaseInfo(disease));
		var st = stream;
		float diseaseNameHeight = (float) Arrays.stream(disease.subs()).map(template::diseaseSubName).mapToDouble(name->{try {
			return st.height(230, new TextBlock(styleTableValue, name));
		} catch(Exception e) {
			return 0;
		}}).sum();
		float tableHeight = 30 + Math.max(Arrays.stream(disease.subs()).map(s->s.genes()).flatMap(Arrays::stream).count()*15, diseaseNameHeight);
		float height = 15+stream.height(497, blockInfo)+5+ tableHeight +10;
		if(y - height < 130) {
			stream = newPage.paint(stream, template, dto);
			y = stream.cursorY();
		}
		stream.saveGraphicsState();
		TextStyle styleHeader = new TextStyle().color(resource.colorPrimary()).fonts(resource.fontHeader(), resource.fontDefault()).fontSize(10);

		y -= 15;
		stream.paragraph(50, y, 497, new TextBlock(styleHeader, template.diseaseName(disease)));
		if(blockInfo.text()!=null && !blockInfo.text().trim().isEmpty()) y = stream.paragraph(50, y-15, 497, JUSTIFY, blockInfo);
		y -= 5;
		stream.setNonStrokingColor(resource.colorPrimary()).rect(50, y, 497, 30).fill()
			  .setStrokingColor(Color.WHITE).setLineWidth(0.25f)
			  .line(447, y - 15, 547, y - 15)
			  .line(497, y - 15, 497, y - 30)
			  .stroke();
		TextStyle styleTableHeader = new TextStyle().color(Color.WHITE).fonts(resource.fontHeader(), resource.fontDefault()).fontSize(8);
		stream.paragraph(165, y-15, 230, CENTER, MIDDLE, new TextBlock(styleTableHeader, template.lblDetailsDiseaseName()));
		stream.paragraph(361, y-15, 212, CENTER, MIDDLE, new TextBlock(styleTableHeader, template.lblDetailsGene()));
		stream.paragraph(497, y-7.5f, 100, CENTER, MIDDLE, new TextBlock(styleTableHeader, template.lblDetailsPathogenicVariant()));
		stream.paragraph(472, y-21f, 50, 0.8F, CENTER, MIDDLE, new TextBlock(styleTableHeader, template.lblDetailsPositive()));
		stream.paragraph(522, y-21f, 50, 0.8F, CENTER, MIDDLE, new TextBlock(styleTableHeader, template.lblDetailsNegative()));
		y -= 30;
		float rectSize = 10;
		float rectRadius = rectSize/2;
		if(Arrays.stream(disease.subs()).map(s->s.genes()).flatMap(Arrays::stream).count() > 1) {
			for(GenomeScreenTemplate.DiseaseSub sub: disease.subs()) {
				var startY = y;
				for(int i = 0; i < sub.genes().length; ++i) {
					if(i < sub.genes().length-1) stream.setStrokingColor(Color.GRAY).line(280, y - 15, 547, y - 15).stroke();
					stream.paragraph(361, y-7.5f, 212, CENTER, MIDDLE, new TextBlock(styleTableValue, sub.genes()[i].name()));
					stream.setStrokingColor(Color.DARK_GRAY).rect(472-rectRadius, y-7.5f+rectRadius, rectSize, rectSize).stroke();
					stream.setStrokingColor(Color.DARK_GRAY).rect(522-rectRadius, y-7.5f+rectRadius, rectSize, rectSize).stroke();
					boolean value = dto.diseases().get(sub).get(sub.genes()[i]);
					Util.icon(stream, resource.imgCheck(), (value?472:522)-rectRadius, y-7.5f+rectRadius, rectSize, rectSize);
					y -= 15;
				}
				var middle = y - (y - startY)/2;
				stream.paragraph(165, middle, 230, CENTER, MIDDLE, new TextBlock(styleTableValue, template.diseaseSubName(sub)));
				stream.setStrokingColor(Color.DARK_GRAY).line(50, y, 547, y).stroke();
			}
		} else {
			var sub = disease.subs()[0];
			var startY = y;
			float rowHeight = diseaseNameHeight + 7;
			for(int i = 0; i < sub.genes().length; ++i) {
				if(i < sub.genes().length-1) stream.setStrokingColor(Color.GRAY).line(280, y - 15, 547, y - rowHeight).stroke();
				stream.paragraph(361, y-rowHeight/2, 212, CENTER, MIDDLE, new TextBlock(styleTableValue, sub.genes()[i].name()));
				stream.setStrokingColor(Color.DARK_GRAY).rect(472-rectRadius, y-rowHeight/2+rectRadius, rectSize, rectSize).stroke();
				stream.setStrokingColor(Color.DARK_GRAY).rect(522-rectRadius, y-rowHeight/2+rectRadius, rectSize, rectSize).stroke();
				boolean value = dto.diseases().get(sub).get(sub.genes()[i]);
				Util.icon(stream, resource.imgCheck(), (value?472:522)-rectRadius, y-rowHeight/2+rectRadius, rectSize, rectSize);
			}
			var middle = startY - rowHeight/2;
			stream.paragraph(165, middle, 230, CENTER, MIDDLE, new TextBlock(styleTableValue, template.diseaseSubName(sub)));
			y -= rowHeight;
			stream.setStrokingColor(Color.DARK_GRAY).line(50, y, 547, y).stroke();
		}

		y -= 10;
		stream.restoreGraphicsState();
		return stream.cursorY(y);
	}
}
