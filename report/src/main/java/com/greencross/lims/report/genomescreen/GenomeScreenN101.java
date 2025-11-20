package com.greencross.lims.report.genomescreen;

import com.greencross.lims.report.SectionBarcode;
import com.greencross.lims.report.SectionLDT;
import com.gcgenome.lims.report.TextBlock;
import com.gcgenome.lims.report.TextStyle;
import com.greencross.lims.report.builder.LogoType;
import com.gcgenome.lims.report.func.PDPageContentStreamPageAccessible;
import com.gcgenome.lims.report.func.Painter;
import com.greencross.lims.report.genomescreen.kokr.SectionGeneListKoKr;
import com.greencross.lims.report.genomescreen.kokr.SectionHeaderKoKr;

import java.awt.*;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static com.gcgenome.lims.report.func.AlignHorizontal.CENTER;
import static com.gcgenome.lims.report.func.AlignHorizontal.JUSTIFY;
import static com.gcgenome.lims.report.func.AlignVertical.MIDDLE;

public class GenomeScreenN101 extends GenomeScreenPageBuilder<GenomeScreenTemplateN101<GenomeScreenResource>> {
	private final Painter<GenomeScreenTemplate<GenomeScreenResource>, GenomeScreenDto> barcode = new SectionBarcode<>();
	private final Painter<GenomeScreenTemplate<GenomeScreenResource>, GenomeScreenDto> ldt;
	private final Painter<GenomeScreenTemplate<GenomeScreenResource>, GenomeScreenDto> sign;
	private final Painter<GenomeScreenTemplate<GenomeScreenResource>, GenomeScreenDto> footer;
	private final Painter<GenomeScreenTemplate<GenomeScreenResource>, GenomeScreenDto> page;

	private final Painter<GenomeScreenTemplate<GenomeScreenResource>, GenomeScreenDto> title = new SectionTitle();
	private final Painter<GenomeScreenTemplate<GenomeScreenResource>, GenomeScreenDto> header = new SectionHeaderKoKr();
	private final Painter<GenomeScreenTemplate<GenomeScreenResource>, GenomeScreenDto> result = new SectionMyResult();
	private final Painter<GenomeScreenTemplate<GenomeScreenResource>, GenomeScreenDto> summary = new SectionSummary();
	private final Painter<GenomeScreenTemplate<GenomeScreenResource>, GenomeScreenDto> empty = (s, t, d)->newPage(s);
	private final Painter<GenomeScreenTemplate<GenomeScreenResource>, GenomeScreenDto> positive;
	private final Painter<GenomeScreenTemplate<GenomeScreenResource>, GenomeScreenDto> details;
	private final Painter<GenomeScreenTemplate<GenomeScreenResource>, GenomeScreenDto> guide;
	private final Painter<GenomeScreenTemplate<GenomeScreenResource>, GenomeScreenDto> genes;
	private final Painter<GenomeScreenTemplate<GenomeScreenResource>, GenomeScreenDto> limitations;
	private final Painter<GenomeScreenTemplate<GenomeScreenResource>, GenomeScreenDto> infos;
	private final Painter<GenomeScreenTemplate<GenomeScreenResource>, GenomeScreenDto> references;
	private final Painter<GenomeScreenTemplate<GenomeScreenResource>, GenomeScreenDto> clinicalMeanings;
	public GenomeScreenN101(GenomeScreenTemplateN101 template, GenomeScreenDto dto, LogoType logoType,
							Painter<GenomeScreenTemplate<GenomeScreenResource>, GenomeScreenDto> sign,
							Painter<GenomeScreenTemplate<GenomeScreenResource>, GenomeScreenDto> footer,
							Painter<GenomeScreenTemplate<GenomeScreenResource>, GenomeScreenDto> page) {
		super(template, dto);
		this.ldt = new SectionLDT<>(logoType, 85);
		this.sign = sign;
		this.footer = footer;
		this.page = page;
		positive = new SectionPositive(empty.and(template()).and(result)) {
			@Override
			public PDPageContentStreamPageAccessible paint(PDPageContentStreamPageAccessible stream, GenomeScreenTemplate<GenomeScreenResource> template, GenomeScreenDto dto) throws IOException {
				var resource = template.resource();
				List<GenomeScreenDto.Variant> pv = dto.variants().values().stream().flatMap(Collection::stream).filter(v->"PV".equals(v.clazz()) || "LPV".equals(v.clazz())).collect(Collectors.toList());
				List<GenomeScreenDto.Variant> vus = dto.variants().values().stream().flatMap(Collection::stream).filter(v->"VUS".equals(v.clazz())).collect(Collectors.toList());
				List<GenomeScreenDto.Variant> bv = dto.variants().values().stream().flatMap(Collection::stream).filter(v->"BV".equals(v.clazz()) || "LBV".equals(v.clazz())).collect(Collectors.toList());
				if(!pv.isEmpty()) stream = GenomeScreenN101.this.table(stream, template, pv);
				stream = interpretation(stream, template, dto);
				if(!vus.isEmpty() || !bv.isEmpty()) {
					stream = newPage.paint(stream, template, dto);
					var y = stream.cursorY();
					y -= 20;
					TextStyle styleHeader = new TextStyle().color(Color.WHITE).fonts(resource.fontHeader(), resource.fontDefault()).fontSize(10).paragraph(true);
					stream.setNonStrokingColor(resource.colorPrimary()).rect(50, y, 497, HEADER_HEIGHT).fill();
					stream.paragraph(55, y-HEADER_HEIGHT/2, 497, MIDDLE, new TextBlock(styleHeader, "기타 변이"));
					stream.cursorY(y - HEADER_HEIGHT - 10);
				}
				if(!vus.isEmpty()) {
					var y = stream.cursorY();
					TextStyle styleHeader = new TextStyle().color(resource.colorText()).fonts(resource.fontHeader(), resource.fontDefault()).fontSize(10).paragraph(true);
					TextStyle styleValue = new TextStyle().color(resource.colorText()).fonts(resource.fontText(), resource.fontDefault()).fontSize(9).paragraph(true);
					stream.paragraph(50, y-HEADER_HEIGHT/2, 497, MIDDLE, new TextBlock(styleHeader, "의미를 알 수 없는 변이(Variant of Uncertain Significance; VUS)"));
					stream.cursorY(y - HEADER_HEIGHT);
					stream = GenomeScreenN101.this.table(stream, template, vus);
					y = stream.cursorY();
					y = stream.paragraph(50, y, 497, JUSTIFY, new TextBlock(styleValue,
						"의미를 알 수 없는 변이(Variant of Uncertain Significance; VUS)는 변이의 병원성에 대해서 뒷받침할 정보가 " +
								"충분하지 않거나 상충되는 정보로 인해 변이와 질병의 관련성이 확실하지 않은 변이입니다. 의미가 불확실한 변이와 암과의 연관성을 밝히고자 하는 노력이 " +
								"지속되고 있으며, 추가적인 연구 결과가 보고됨에 따라 질환과 연관성이 있다고 밝혀질 가능성이 있습니다. 귀하에서 발견된 변이와 질병과의 연관성이 새롭게 " +
								"발견될 경우, 검사를 시행한 주치의를 통해 새로운 정보가 전달됩니다."));
					stream.cursorY(y - 20);
				}
				if(!bv.isEmpty()) {
					var y = stream.cursorY();
					TextStyle styleHeader = new TextStyle().color(resource.colorText()).fonts(resource.fontHeader(), resource.fontDefault()).fontSize(10).paragraph(true);
					TextStyle styleValue = new TextStyle().color(resource.colorText()).fonts(resource.fontText(), resource.fontDefault()).fontSize(9).paragraph(true);
					stream.paragraph(50, y-HEADER_HEIGHT/2, 497, MIDDLE, new TextBlock(styleHeader, "준양성 변이(Likely Benign Variant; LBV) / 양성 변이(Benign Variant; BV)"));
					stream.cursorY(y - HEADER_HEIGHT);
					stream = GenomeScreenN101.this.table(stream, template, bv);
					y = stream.cursorY();
					y = stream.paragraph(50, y, 497, JUSTIFY, new TextBlock(styleValue,
								"준양성 변이(Likely Benign Variant; LBV) 혹은 양성 변이(Benign Variant; BV)는 질병과의 관련성이 매우 낮거나 관련성이 없는 변이로 " +
										"해당 변이들은 질병에 큰 영향을 미치지 않는 변이입니다."));
					stream.cursorY(y - 20);
				}
				if(pv.isEmpty()) return stream;
				mode = 1;
				stream = newPage.paint(stream, template, dto);
				for(var disease: template.diseases())
					L: for(var sub: disease.subs()) {
						for(var gene: sub.genes()) {
							if(dto.variants().containsKey(gene)) {
								stream = value(stream, template, dto, sub);
								continue L;
							}
						}
					}
				return stream;
			}
		};
		details = new SectionDetails(empty.and(template()).and(result));
		guide = new SectionTestGuide(empty.and(template()));
		genes = new SectionGeneListKoKr(empty.and(template()));
		limitations = new SectionLimitation();
		infos = new SectionTestInfo(empty.and(template()));
		references = new SectionReference(empty.and(template()));
		clinicalMeanings = new SectionClinicalMeanings(empty.and(template()));
	}
	@Override
	public Painter<GenomeScreenTemplate<GenomeScreenResource>, GenomeScreenDto> template() {
		return title.and((stream, template, dto)->{
			stream.font(template.resource().fontDefault());
			return stream;
		}).and(header).and(footer).and(sign);
	}
	@Override
	public Painter<GenomeScreenTemplate<GenomeScreenResource>, GenomeScreenDto> pages() {
		return template().and(barcode).and(result).and(summary).and(positive)
		.and((s, t, d)->newPage(s)).and(template()).and(result).and(details)
		.and((s, t, d)->newPage(s)).and(template()).and(guide)
		.and((s, t, d)->newPage(s)).and(template()).and(genes).and(limitations)
		.and((s, t, d)->newPage(s)).and(template()).and(infos)
		.and((s, t, d)->newPage(s)).and(template()).and(infos).and(clinicalMeanings).and(ldt).and(page);
	}

	protected PDPageContentStreamPageAccessible table(PDPageContentStreamPageAccessible stream, GenomeScreenTemplate<GenomeScreenResource> template, List<GenomeScreenDto.Variant> variants) throws IOException {
		stream.saveGraphicsState();
		var resource = template.resource();
		var y = stream.cursorY();
		TextStyle styleTableHeader = new TextStyle().color(Color.WHITE).fonts(resource.fontHeader(), resource.fontDefault()).fontSize(8);
		TextStyle styleValue = new TextStyle().color(resource.colorText()).fonts(resource.fontText(), resource.fontDefault()).fontSize(8).justify(true).paragraph(true);
		stream.setNonStrokingColor(resource.colorPrimary())
			  .addRect(50, y-20,497, 20)
			  .fill();
		y -= 10;
		stream.paragraph(100, y, 115, CENTER, MIDDLE, new TextBlock(styleTableHeader, template.lblVariantTable()[0]));
		stream.paragraph(200, y, 115, CENTER, MIDDLE, new TextBlock(styleTableHeader, template.lblVariantTable()[1]));
		stream.paragraph(310, y, 115, CENTER, MIDDLE, new TextBlock(styleTableHeader, template.lblVariantTable()[2]));
		stream.paragraph(420, y, 115, CENTER, MIDDLE, new TextBlock(styleTableHeader, template.lblVariantTable()[3]));
		stream.paragraph(500, y, 115, CENTER, MIDDLE, new TextBlock(styleTableHeader, template.lblVariantTable()[4]));
		y -= 2;
		for(var variant: variants) {
			y -= 16;
			stream.paragraph(100, y, 115, CENTER, MIDDLE, new TextBlock(styleValue, variant.gene()));
			stream.paragraph(200, y, 115, CENTER, MIDDLE, new TextBlock(styleValue, variant.dnaChange()));
			stream.paragraph(310, y, 115, CENTER, MIDDLE, new TextBlock(styleValue, variant.predictedAa()));
			stream.paragraph(420, y, 115, CENTER, MIDDLE, new TextBlock(styleValue, variant.zygosity()));
			stream.paragraph(500, y, 115, CENTER, MIDDLE, new TextBlock(styleValue, variant.clazz()));
		}
		y -= 10;
		stream.setStrokingColor(Color.DARK_GRAY).setLineWidth(0.25f).line(50, y, 547, y).stroke();
		y -= 10;
		stream.restoreGraphicsState();
		return stream.cursorY(y);
	}
}
