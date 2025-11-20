package com.greencross.lims.report.wes;

import com.gcgenome.lims.report.func.Painter;
import com.greencross.lims.report.SectionBarcode;
import com.greencross.lims.report.SectionRevision;
import com.greencross.lims.report.single.SingleGeneDto;
import com.greencross.lims.report.single.SingleGenePage;
import com.greencross.lims.report.single.SingleGeneTemplate;
import com.greencross.lims.report.wes.kokr.WesHeaderKoKr;

public class WesWithSingleGenePage {
	private final SingleGenePageWes singleGenePage;
	private final SingleGeneTemplate template1;
	private WesPageSingle wesPage;
	private final WesTemplate template2;
	private final Painter<SingleGeneTemplate, SingleGeneDto> footer;
	private final Painter<SingleGeneTemplate, SingleGeneDto> sign;
	private final Painter<WesTemplate, WesDto> ldt;
	private final Painter<SingleGeneTemplate, SingleGeneDto> page;

	public WesWithSingleGenePage(SingleGeneTemplate template1, Painter<SingleGeneTemplate, SingleGeneDto> footer, Painter<SingleGeneTemplate, SingleGeneDto> sign, Painter<SingleGeneTemplate, SingleGeneDto> page, WesTemplate template2, Painter<WesTemplate, WesDto> ldt) {
		this.template1 = template1;
		this.template2 = template2;
		this.footer = footer;
		this.sign = sign;
		this.ldt = ldt;
		this.page = page;
		singleGenePage = new SingleGenePageWes(template1, footer, sign);
	}
	public Painter<WesTemplate, WesWithSingleDto> page() {
		Painter<WesTemplate, WesWithSingleDto> wrapper = (s, t, d) -> singleGenePage.page().paint(s, template1, d.single());
		Painter<WesTemplate, WesWithSingleDto> wrapper2 = (s, t, d) -> {
			Painter<WesTemplate, WesDto>  footer2 = (s2, t2, d2) -> footer.paint(s2, template1, d.single());
			Painter<WesTemplate, WesDto>  sign2 = (s2, t2, d2) -> sign.paint(s2, template1, d.single());
			wesPage = new WesPageSingle(template2, d.single(), ldt, footer2, sign2);
			return wesPage.page().paint(s, template2, d.wes());
		};
		Painter<WesTemplate, WesWithSingleDto> page2 = (s, t, d) -> page.paint(s, template1, d.single());
		return wrapper.and(wrapper2).and(page2);
	}
	private static final class SingleGenePageWes extends SingleGenePage {
		public SingleGenePageWes(SingleGeneTemplate template, Painter<SingleGeneTemplate, SingleGeneDto> footer, Painter<SingleGeneTemplate, SingleGeneDto> sign) {
			super(template, footer, sign, (stream, template3, dto) -> stream);
		}
		@Override
		public Painter<SingleGeneTemplate, SingleGeneDto> page() {
			return initialize().and(barcode)
					.and(header())
					.and(sign())
					.and(variant())
					.and(testInfo());
		}
	}
	private final class WesPageSingle extends WesPage {
		private final Painter<WesTemplate, WesDto> barcode = new SectionBarcode<>();
		private final Painter<WesTemplate, WesDto> revision = new SectionRevision<>();
		private final WesHeader header;
		private final Painter<WesTemplate, WesDto> footer;
		private final Painter<WesTemplate, WesDto> sign;
		private final Painter<WesTemplate, WesDto> ldt;
		public WesPageSingle(WesTemplate template, SingleGeneDto single, Painter<WesTemplate, WesDto> ldt, Painter<WesTemplate, WesDto> footer, Painter<WesTemplate, WesDto> pn) {
			this(template, new WesHeaderKoKr(template), ldt, footer, (s, t, d)->singleGenePage.sign().paint(s, template1, single), pn);
		}
		private WesPageSingle(WesTemplate template, WesHeader header, Painter<WesTemplate, WesDto> ldt, Painter<WesTemplate, WesDto> footer, Painter<WesTemplate, WesDto> sign, Painter<WesTemplate, WesDto> pn) {
			super(template, header, ldt, footer, sign, pn);
			this.header = header;
			this.footer = footer;
			this.sign = sign;
			this.ldt = ldt;
		}
		@Override
		public Painter<WesTemplate, WesDto> page() {
			return initialize().and((s, t, d) -> newPage(s))
							   .and(header.initialize()).and(barcode).and(revision)
							   .and(header.header())
							   .and(footer).and(sign)
							   .and(variant())
							   .and(incidentalFindings())
							   .and(testInfo())
							   .and(ldt);
		}
	}
}
