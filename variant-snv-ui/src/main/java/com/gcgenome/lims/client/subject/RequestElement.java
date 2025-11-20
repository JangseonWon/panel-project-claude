package com.gcgenome.lims.client.subject;

import com.gcgenome.lims.api.SnvApi;
import com.gcgenome.lims.client.SectionElement;
import com.gcgenome.lims.client.SubjectElement;
import com.gcgenome.lims.dto.Query;
import com.gcgenome.lims.dto.Snv;
import com.gcgenome.lims.ui.IconElement;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.user.client.Window;
import elemental2.dom.CSSProperties;
import elemental2.dom.HTMLDivElement;
import elemental2.dom.HTMLElement;
import net.sayaya.ui.PageElement;
import net.sayaya.ui.chart.Column;
import net.sayaya.ui.chart.Data;
import net.sayaya.ui.chart.SheetElement;
import net.sayaya.ui.chart.column.ColumnBuilder;
import org.jboss.elemento.HTMLContainerBuilder;

import java.util.Arrays;
import java.util.Comparator;

import static org.jboss.elemento.Elements.div;

public class RequestElement extends SubjectElement<RequestElement> {
	public static RequestElement build(String id) {
		return new RequestElement(id, div());
	}
	private final HTMLContainerBuilder<HTMLDivElement> _this;
	private final Column[] columns = new Column[]{
			ColumnBuilder.link("Sample", data->{
				if(data==null || data.get("Sample") == null) return null;
				String sample = data.get("Sample");
				if(sample!=null && !sample.isEmpty()) return "../sample.html#" + sample.replace("-", "");
				else return null;
			}).onClick(data->{
				if(data.get("Sample") == null) return;
				String sample = data.get("Sample");
				if(sample!=null && !sample.isEmpty()) {
					String url = "../sample.html#" + sample.replace("-", "");
					Window.open(url, "_blank", null);
					// WindowApi.open(url, "_blank", null, false);
				}
			}).readOnly(true).name("Sample").target("sample").font("JetBrains Mono").horizontal("center").build(),
			ColumnBuilder.string("Analysis").name("Analysis").readOnly(true).build(),
			ColumnBuilder.string("Depth").name("Depth").readOnly(true).build(),
			ColumnBuilder.string("VAF").name("VAF").readOnly(true).build(),
			ColumnBuilder.string("Genotype").name("Genotype").readOnly(true).build()
	};
	private final SheetElement.SheetConfiguration config = SheetElement.builder()
															   .rowHeaders(false)
															   .autoColSize(true)
															   .autoRowSize(false)
															   .manualColumnMove(true)
															   .manualColumnResize(true)
															   .columns(columns)
															   .data(new Data[] {})
															   .stretchH("all");
	private final SheetElement sheet = config.build();
	private final Query query = new Query().sortBy("create_at").asc(false);
	private final PageElement page = PageElement.instance().show(10).idx(0L).style("display: flex;padding-left: 16px;list-style: none;border-top: 1px solid #AAA;");
	private final SectionElement section = SectionElement.build(IconElement.icon(IconElement.Type.Light, "fa-dna"), "SNV called");
	private RequestElement(String id, HTMLContainerBuilder<HTMLDivElement> e) {
		super(id, e);
		_this = e;
		query.limit(page.show()).page((int) page.page());
		page.onValueChange(evt->{
			query.limit(page.show()).page((int)page.page());
			update();
		});
		e.add(section).add(div().style("padding-bottom: 15px; border-top: 1px solid #AAA; margin-right: 20px; margin-left: 20px;")
								.add(div().style("overflow: hidden;min-height: 7px;").add(sheet))
								.add(page));
	}

	@Override
	public void initialize() {
		((HTMLElement)sheet.element().parentElement).style.paddingBottom = CSSProperties.PaddingBottomUnionType.of("13px");
		update();
	}
	private void update() {
		SnvApi.requests(id, query).then(response -> {
			page.total(Long.parseLong(response.headers.get("X-Total-Count")));
			return response.json();
		}).then(json->{
			if(json!=null) {
				Snv[] cast = (Snv[])json;
				values(cast);
				Scheduler.get().scheduleDeferred(()->((HTMLElement)sheet.element().parentElement).style.paddingBottom = null);
			}
			return null;
		});
	}

	private void values(Snv[] requests) {
		Comparator<Snv> cmp = Comparator.comparing(Snv::createAt);
		Data[] data = Arrays.stream(requests).sorted(cmp.reversed()).map(this::map).toArray(Data[]::new);
		sheet.values(data);
	}

	private static final RegExp FORMAT_SAMPLE_ID = RegExp.compile("(\\d{8}-\\d{3}-\\d{4})");
	private Data map(Snv request) {
		Data data = Data.create(request.analysis());
		MatchResult m = FORMAT_SAMPLE_ID.exec(request.analysis());
		if(m!=null) data.initialize("Sample", m.getGroup(1));
		return data.initialize("Analysis", request.analysis())
				   .initialize("Genotype", request.genotype())
				   .initialize("VAF", request.vaf())
				   .initialize("Depth", request.depth());
	}

	@Override
	public RequestElement that() {
		return this;
	}
}
