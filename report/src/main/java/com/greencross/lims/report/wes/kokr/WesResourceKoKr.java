package com.greencross.lims.report.wes.kokr;

import com.greencross.lims.report.HasSign;
import com.greencross.lims.report.builder.AbstractReportDto;
import com.greencross.lims.report.wes.WesDto;
import com.greencross.lims.report.wes.WesResource;
import com.greencross.lims.report.wes.WesWithSingleDto;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.font.PDFont;

import java.awt.*;
import java.io.File;
import java.io.IOException;

@Data
@Accessors(fluent = true)
public class WesResourceKoKr implements WesResource {
	private final File resource = new File("/data/lims/resources");
	private final PDDocument doc;
	private final PDFont fontDefault;
	private final PDFont fontTitle;
	private final PDFont fontHeader;
	private final PDFont fontText;
	private Color colorPrimary = new Color(219, 230, 240);
	private Color colorSecondary = new Color(55, 95, 146);
	private Color colorPrimaryLine = new Color(130, 155, 187);
	private Color colorSecondaryLine = new Color(130, 155, 187);
	private Color colorGray = Color.decode("0xEFEFEF");
	private Color colorText = Color.decode("0x484848");
	private Color colorTextWithPrimary = colorText();
	private Color colorTextWithSecondary = Color.WHITE;
	private final HasSign.Person inspector;
	private final HasSign.Person reporter;
	private final HasSign.Person reviewer;

	public WesResourceKoKr(PDDocument doc, AbstractReportDto dto) throws IOException {
		this.doc = doc;
		WesDto wesDto = extractWesDto(dto);
		fontDefault = font(new File(resource, "/font/NanumBarunGothic.ttf"));
		fontTitle = font(new File(resource, "/font/GC140.ttf"));
		fontHeader = font(new File(resource, "/font/OpenSans-Bold.ttf"));
		fontText = font(new File(resource, "/font/OpenSans-Regular.ttf"));
		inspector = WesResourceKoKr.this.person("류해인") ;
		reporter = person(wesDto.reporter());
		reviewer = person(wesDto.reviewer());
	}
	@Override
	public SignLabel[] labels() {
		return new SignLabel[] {
				new SignLabel("검사자:", inspector),
				new SignLabel("보고자/검토자:", reporter, reviewer)
		};
	}
	private WesDto extractWesDto(AbstractReportDto dto) {
		if (dto instanceof WesWithSingleDto wesWithSingleDto) {
			return wesWithSingleDto.wes();
		} else if (dto instanceof WesDto wesDto) {
			return wesDto;
		}
		throw new IllegalArgumentException("Unsupported DTO type: " + dto.getClass().getSimpleName());
	}
}
