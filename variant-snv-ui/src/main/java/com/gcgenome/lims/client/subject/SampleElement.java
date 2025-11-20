package com.gcgenome.lims.client.subject;

import com.gcgenome.lims.api.SnvApi;
import com.gcgenome.lims.client.SectionElement;
import com.gcgenome.lims.client.SubjectElement;
import com.gcgenome.lims.dto.Query;
import com.gcgenome.lims.dto.RequestSnv;
import com.gcgenome.lims.ui.IconElement;
import com.gcgenome.lims.util.DataTransformUtil;
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

import static org.jboss.elemento.Elements.div;

public class SampleElement extends SubjectElement<SampleElement> {
	public static SampleElement build(String id) {
		return new SampleElement(id, div());
	}
	private final HTMLContainerBuilder<HTMLDivElement> _this;
	private final Column[] columns = new Column[]{
			ColumnBuilder.link("Sample", data->{
				if(data.get("Sample") == null) return null;
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
			ColumnBuilder.string("Test").name("Test").readOnly(true).build(),
			ColumnBuilder.string("Name").name("Name").readOnly(true).build(),
			ColumnBuilder.string("Sex").name("Sex").readOnly(true).horizontal("center").build(),
			ColumnBuilder.string("Inst").name("Inst").readOnly(true).horizontal("center").build(),
			ColumnBuilder.string("Serial").name("Analysis").readOnly(true).build(),
			ColumnBuilder.string("Zygosity").name("Zygosity").readOnly(true).horizontal("center").build(),
			ColumnBuilder.string("Class").name("Class").readOnly(true).horizontal("center").build(),
	};
	private final SheetElement.SheetConfiguration config = SheetElement.builder()
															   .autoColSize(false)
															   .autoRowSize(false)
															   .rowHeaders(false)
															   .manualColumnMove(true)
															   .manualColumnResize(true)
			        										   .columns(columns)
															   .data(new Data[] {})
															   .stretchH("all");
	private final SheetElement sheet = config.build();
	private final Query query = new Query().sortBy("sample").asc(false);
	private final PageElement page = PageElement.instance().show(10).idx(0L).style("display: flex;padding-left: 16px;list-style: none;border-top: 1px solid #AAA;");
	private final SectionElement section = SectionElement.build(IconElement.icon(IconElement.Type.Light, "fa-clipboard"), "Reported");
	private SampleElement(String id, HTMLContainerBuilder<HTMLDivElement> e) {
		super(id, e);
		_this = e;
		query.limit(page.show()).page((int) page.page());
		page.onValueChange(evt->{
			query.limit(page.show()).page((int)page.page());
			update();
		});
		e.add(section).add(div().style("padding-bottom: 15px; border-top: 1px solid #AAA; margin-right: 20px; margin-left: 20px;")
								.add(div().style("overflow: hidden;min-height: 100px;").add(sheet))
								.add(page));
	}

	@Override
	public void initialize() {
		update();
	}
	private void update() {
		SnvApi.samples(id, query).then(response->{
			page.total(Long.parseLong(response.headers.get("X-Total-Count")));
			return response.json();
		}).then(json->{
			if(json!=null) {
				RequestSnv[] cast = (RequestSnv[])json;
				values(cast);
				// Scheduler.get().scheduleDeferred(()->((HTMLElement)sheet.element().parentElement).style.paddingBottom = null);
			} else ((HTMLElement)sheet.element().parentElement).style.paddingBottom = CSSProperties.PaddingBottomUnionType.of("13px");
			return null;
		});
	}

	private void values(RequestSnv[] requests) {
		Data[] data = Arrays.stream(requests).map(this::map).toArray(Data[]::new);
		sheet.values(data);
	}

	private Data map(RequestSnv request) {
		return Data.create(request.sample() + "&" + request.serviceCode())
					   .initialize("Sample", DataTransformUtil.formatSampleId(request.sample()))
					   .initialize("Test", request.serviceName())
					   .initialize("Name", request.patientName())
					   .initialize("Sex", request.patientSex())
					   .initialize("Inst", request.customerName())
					   .initialize("Serial", request.serial())
					   .initialize("Class", request.tier());
	}

	@Override
	public SampleElement that() {
		return this;
	}
}
