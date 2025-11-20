package com.greencross.lims.report.kokr;

import com.gcgenome.lims.report.Template;
import com.gcgenome.lims.report.TextBlock;
import com.gcgenome.lims.report.TextStyle;
import com.gcgenome.lims.report.func.PDPageContentStreamPageAccessible;
import com.gcgenome.lims.report.func.Painter;
import com.greencross.lims.report.HasSign;
import com.greencross.lims.report.builder.AbstractReportDto;
import com.greencross.lims.report.builder.Util;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.gcgenome.lims.report.func.AlignVertical.MIDDLE;

public class SectionSign<T extends Template<? extends HasSign>, D extends AbstractReportDto> implements Painter<T, D> {
	private final float y;
	public SectionSign(float y) {
		this.y = y;
	}
	private final Map<HasSign.Person, PDImageXObject> signs = new HashMap<>();
	private final static float SIGN_WIDTH_MAX = 40;
	private final static float SIGN_HEIGHT_MAX = 15;
	@Override
	public PDPageContentStreamPageAccessible paint(PDPageContentStreamPageAccessible stream, T template, D dto) throws IOException {
		TextStyle style = template.resource().stylePerson();
		float widthTotal = 0;
		for(var label: template.resource().labels()) {
			TextBlock block = new TextBlock(style, label.label());
			widthTotal += block.width();
			widthTotal += 5;
			for(var person: label.persons()) {
				block = new TextBlock(style, person.nameKoWithTitle() + "(" + person.license() + ")");
				if(!signs.containsKey(person)) signs.put(person, PDImageXObject.createFromFileByContent(person.sign(), template.resource().doc()));
				widthTotal += block.width() + SIGN_WIDTH_MAX + 10;
			}
			widthTotal += 20;
		}

		stream.saveGraphicsState();
		float x = 297.5f - widthTotal/2;
		for(var label: template.resource().labels()) {
			TextBlock block = new TextBlock(style, label.label());
			stream.paragraph(x, y+SIGN_HEIGHT_MAX/2, 120, MIDDLE, block);
			x += block.width();
			x += 5;
			for(var person: label.persons()) {
				block = new TextBlock(style, person.nameKoWithTitle() + "(" + person.license() + ")");
				var sign = signs.get(person);
				Util.icon(stream, sign, x + block.width() + 2, y+SIGN_HEIGHT_MAX, SIGN_WIDTH_MAX, SIGN_HEIGHT_MAX);
				stream.paragraph(x, y+SIGN_HEIGHT_MAX/2, block.width(), MIDDLE, block);
				x += block.width() + SIGN_WIDTH_MAX + 10;
			}
			x += 20;
		}
		stream.restoreGraphicsState();
		return stream;
	}
}

