package com.greencross.lims.client.trello;

import com.greencross.lims.api.WindowApi;
import com.greencross.lims.dto.Issue;
import com.greencross.lims.util.DataTransformUtil;
import elemental2.dom.*;
import net.sayaya.ui.CheckBoxElement;
import net.sayaya.ui.HTMLElementBuilder;
import net.sayaya.ui.event.HasValueChangeHandlers;
import org.gwtproject.event.shared.HandlerRegistration;
import org.jboss.elemento.EventType;
import org.jboss.elemento.HtmlContentBuilder;

import java.util.Date;

import static org.jboss.elemento.Elements.*;

public class IssueElement extends HTMLElementBuilder<HTMLDivElement, IssueElement> implements HasValueChangeHandlers<Issue> {
	static IssueElement build(Issue child) {
		return new IssueElement(child, div());
	}
	private final CheckBoxElement checkBox = CheckBoxElement.checkBox(false).style("position: absolute;right: -5px;top: 0px;");
	private final HtmlContentBuilder<HTMLAnchorElement> sample;
	private final HtmlContentBuilder<HTMLLabelElement> serviceCode = label();
	private final HtmlContentBuilder<HTMLLabelElement> serviceName = label();
	private final HtmlContentBuilder<HTMLAnchorElement> patient;
	private final HtmlContentBuilder<HTMLLabelElement> birth = label();
	private final HtmlContentBuilder<HTMLLabelElement> sex = label();
	private final HtmlContentBuilder<HTMLLabelElement> dateRequest = label();
	private final HtmlContentBuilder<HTMLLabelElement> dateDue = label();
	private final HtmlContentBuilder<HTMLLabelElement> customer = label();
	private final HtmlContentBuilder<HTMLLabelElement> mrn = label();
	private final HtmlContentBuilder<HTMLTableElement> table;
	private final HtmlContentBuilder<HTMLDivElement> _this;
	private final Issue issue;
	private IssueElement(Issue issue, HtmlContentBuilder<HTMLDivElement> e) {
		super(e.css("card", "open"));
		_this = e;
		this.issue = issue;
		sample = a("sample.html#" + issue.sample(), "sample").css("id").add(DataTransformUtil.formatSampleId(issue.sample()));
		sample.on(EventType.click, evt->{
			evt.preventDefault();
			evt.stopPropagation();
			DomGlobal.console.log("Sample click");
			WindowApi.open("sample.html#" + issue.sample(), "sample", null, true);
		});
		patient = a().add(issue.patientName());
		table = table().add(colgroup().add(col().style("width: 110px;")))
					   .add(thead().add(tr().add(th().attr("colspan", "2").add(div().add(sample).add(checkBox)))))
					   .add(tbody().add(tr().add(td().attr("colspan", "2").style("text-align: left;").add(patient)))
								   .add(tr().add(td().add(birth)).add(td().add(sex)))
								   .add(tr().add(td().attr("colspan", "2").style("text-align: left;").add(serviceName)))
								   .add(tr().add(td().add(dateRequest)).add(td().add(customer)))
								   .add(tr().add(td().add(dateDue)).add(td().add(mrn))));
		_this.add(table);
		serviceCode.add(issue.service());
		//serviceName.add(issue.serviceName());
		try {
			Date b = DataTransformUtil.toBirth(issue.patientCode());
			if (b != null) birth.add(DataTransformUtil.formatDate(b.getTime()));
		} catch(Exception ignore){
			birth.add("-");
		}
		String sex = DataTransformUtil.toSex(issue.patientCode());
		this.sex.add(sex!=null?sex:"-");
		if(issue.dateRequest()!=null) dateRequest.add(DataTransformUtil.formatDate(issue.dateRequest()) + " Req");
		if(issue.dateDue()!=null) dateDue.add(DataTransformUtil.formatDate(issue.dateDue()) + " Due");
		if(issue.customerName()!=null) customer.add(issue.customerName());
		mrn.add(issue.mrn()!=null?issue.mrn():"-");

		long day = 24L * 60 * 60 * 1000;
		long today = new Date().getTime();
		if(issue.dateDue()!=null && issue.dateDue() < today + 3*day) e.css("emergency-issue");
		checkBox.onValueChange(evt->{
			if(evt.value()) select();
			else unselect();
		});
	}
	public void select() {
		checkBox.value(true);
		this.css("card-selected");
	}
	public void unselect() {
		checkBox.value(false);
		this.ncss("card-selected");
	}
	private boolean isSelected() {
		return checkBox.value();
	}
	@Override
	public IssueElement that() {
		return this;
	}

	@Override
	public Issue value() {
		if(isSelected()) return issue;
		return null;
	}

	@Override
	public HandlerRegistration onValueChange(ValueChangeEventListener<Issue> valueChangeEventListener) {
		return onValueChange(checkBox.element(), valueChangeEventListener);
	}
}
