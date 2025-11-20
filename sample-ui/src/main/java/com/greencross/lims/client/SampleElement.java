package com.greencross.lims.client;

import com.greencross.lims.dto.Sample;
import com.greencross.lims.util.DataTransformUtil;
import elemental2.dom.HTMLDivElement;
import elemental2.dom.HTMLLabelElement;
import elemental2.dom.HTMLTableElement;
import net.sayaya.ui.HTMLElementBuilder;
import org.jboss.elemento.HTMLContainerBuilder;

import java.util.Date;

import static org.jboss.elemento.Elements.*;

public class SampleElement extends HTMLElementBuilder<HTMLDivElement, SampleElement> {
	public static SampleElement build(Sample dto) {
		return new SampleElement(dto, div());
	}
	private final HTMLContainerBuilder<HTMLTableElement> table = table().style("width: 60%;");
	private final HTMLContainerBuilder<HTMLLabelElement> patientName = label();
	private final HTMLContainerBuilder<HTMLLabelElement> id = label().css("id");
	private final HTMLContainerBuilder<HTMLLabelElement> birth = label();
	private final HTMLContainerBuilder<HTMLLabelElement> sex = label();
	private final HTMLContainerBuilder<HTMLLabelElement> customer = label();
	private final HTMLContainerBuilder<HTMLLabelElement> mrn = label();
	private final HTMLContainerBuilder<HTMLLabelElement> lblSex = label().css("label").add("Sex:");
	private final HTMLContainerBuilder<HTMLDivElement> _this;
	private SampleElement(Sample dto, HTMLContainerBuilder<HTMLDivElement> e) {
		super(e.css("sample").style("height: 112px; border-bottom-left-radius: 0;"));
		_this = e.add(table);
		table.add(colgroup().add(col().style("width: 20%;")).add(col().style("width: 30%;"))
							.add(col().style("width: 20%;")).add(col().style("width: 30%;")))
			 .add(thead().add(tr().add(th().add(label().css("label").add("Sample ID:"))).add(th().attr("colspan", "2").add(id.add(DataTransformUtil.formatSampleId(dto.id()))))))
			 .add(tbody().add(tr().add(td().add(label().css("label").add("Patient Name:"))).add(td().attr("colspan", "2").style("text-align: left;").add(patientName.add(dto.patientName()))))
						 .add(tr().add(td().add(label().css("label").add("Birthday:"))).add(td().add(birth)).add(td().add(lblSex)).add(td().add(sex)))
						 .add(tr().add(td().add(label().css("label").add("Institution:"))).add(td().add(customer)).add(td().add(label().css("label").add("MRN:"))).add(td().add(mrn))));
		try {
			Date b = DataTransformUtil.toBirth(dto.patientCode());
			if (b != null) birth.add(DataTransformUtil.formatDate(b.getTime()));
		} catch(Exception ignore){
			birth.add("-");
		}
		if(dto.patientSex()!=null) this.sex.add(dto.patientSex());
		else {
			lblSex.style("color: #AD1747");
			this.sex.style("color: #AD1747");
			this.sex.add("-");
		}
		if(dto.customerName()!=null) customer.add(dto.customerName());
		mrn.add(dto.mrn()!=null?dto.mrn():"-");
	}

	@Override
	public SampleElement that() {
		return this;
	}
}
