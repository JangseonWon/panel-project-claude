package com.greencross.lims.report.wes;

import com.gcgenome.lims.report.func.PageBuilder;
import com.gcgenome.lims.report.func.Painter;
import com.gcgenome.lims.test.wes.TestInfo;
import com.gcgenome.lims.test.wes.TestWithSingleInfo;
import com.greencross.lims.dto.interpretation.Des;
import com.greencross.lims.dto.interpretation.PanelTest;
import com.greencross.lims.entity.Request;
import com.greencross.lims.report.ReportFactory;
import com.greencross.lims.report.SectionLDT;
import com.greencross.lims.report.SectionPage;
import com.greencross.lims.report.builder.LogoType;
import com.greencross.lims.report.kokr.SectionSign;
import com.greencross.lims.report.single.SingleGeneDto;
import com.greencross.lims.report.single.SingleGeneReportBuilder;
import com.greencross.lims.report.single.SingleGeneTemplate;
import com.greencross.lims.report.single.kokr.SingleGeneResourceKoKr;
import com.greencross.lims.report.single.kokr.SingleGeneTemplateKoKr;
import com.greencross.lims.report.wes.kokr.WesResourceKoKr;
import com.greencross.lims.report.wes.kokr.WesTemplateKoKr;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

import static com.greencross.lims.report.ReportUtil.isLabsRequest;

@Component
@RequiredArgsConstructor
public class WesWithSingleGeneReportBuilder implements ReportFactory<Des> {
	private final SingleGeneReportBuilder singleGeneReportBuilder;
	private final WesReportBuilder wesReportBuilder;
	@Override
	public Class<Des> clazz() {
		return Des.class;
	}
	private TestWithSingleInfo info(String service) {
		for(TestWithSingleInfo test: TestWithSingleInfo.TESTS) if(test.code().equalsIgnoreCase(service)) return test;
		return null;
	}
	@Override
	public boolean match(Request request) {
		for(TestInfo test: TestWithSingleInfo.TESTS) if(test.code().equalsIgnoreCase(request.pk().service())) return true;
		return false;
	}
	@Override
	public byte[] build(Request request, Des value, Class<Des> clazz) throws IOException {
		PDDocument doc = new PDDocument();
		var test = info(request.pk().service());
		WesWithSingleDto dto = dto(test, request, value);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		Painter<SingleGeneTemplate, SingleGeneDto> footer = null;
		Painter<SingleGeneTemplate, SingleGeneDto> sign = null;
		Painter<SingleGeneTemplate, SingleGeneDto> page = null;
		Painter<WesTemplate, WesDto> ldt = null;
		LogoType logoType;
		SingleGeneTemplate singleGeneTemplate;
		WesTemplate wesTemplate = new WesTemplateKoKr(new WesResourceKoKr(doc, dto), test);

		if(isLabsRequest(request.sample())) {
			logoType = LogoType.DEPENDENT;
			ldt = new SectionLDT<>(logoType, 120);
			sign = new SectionSign<>(80);
			footer = new com.greencross.lims.report.kokr.SectionFooterLabs<>();
			singleGeneTemplate = new SingleGeneTemplateKoKr(new SingleGeneResourceKoKr(test.single(), doc), test.single(), logoType);
			page = new SectionPage<>(547, 80, singleGeneTemplate.resource().fontDefault());
		} else {
			logoType = LogoType.INDEPENDENT;
			ldt = new SectionLDT<>(logoType, 90);
			sign = new SectionSign<>(65);
			footer = new com.greencross.lims.report.kokr.SectionFooterGenome<>();
			singleGeneTemplate = new SingleGeneTemplateKoKr(new SingleGeneResourceKoKr(test.single(), doc), test.single(), logoType);
			page = new SectionPage<>(547, 65, singleGeneTemplate.resource().fontDefault());
		}

		new PageBuilder<>(wesTemplate, dto).add(new WesWithSingleGenePage(singleGeneTemplate, footer, sign, page, wesTemplate, ldt).page()).build().save(baos);
		return baos.toByteArray();
	}

	@Override
	public String buildLongFormText(Request request) {
		return null;
	}

	@Override
	public String buildShortFormText(Request request) {
		return null;
	}

	private WesWithSingleDto dto(TestWithSingleInfo test, Request request, Des value) {
		SingleGeneDto single = singleGeneReportBuilder.dto(test.single(), request, desToSingle(value));
		WesDto des = wesReportBuilder.dto(test, request, value.incidentalFindings());
		return new WesWithSingleDto().single(single).wes(des);
	}
	private static PanelTest desToSingle(Des dto) {
		return new PanelTest().result(dto.result())
																	 .resultText(dto.resultText())
																	 .reasonForReferral(dto.reasonForReferral())
																	 .abbreviationReference(dto.abbreviationReference())
																	 .abbreviationDisease(dto.abbreviationDisease())
																	 .abbreviation(dto.abbreviation())
																	 .interpretation(dto.interpretation())
																	 .meanDepth(dto.meanDepth())
																	 .coverage(dto.coverage())
																	 .variants(Arrays.stream(dto.variants())
																					 .map(WesWithSingleGeneReportBuilder::desToSingle)
																					 .toArray(PanelTest.Variant[]::new));
	}
	private static PanelTest.Variant desToSingle(Des.Variant dto) {
		return new PanelTest.Variant().gene(dto.gene())
																			 .hgvsc(dto.hgvsc())
																			 .hgvsp(dto.hgvsp())
																			 .zygosity(dto.zygosity())
																			 .disease(dto.disease())
																			 .inheritance(dto.inheritance())
																			 .clazz(dto.clazz());
	}
}
