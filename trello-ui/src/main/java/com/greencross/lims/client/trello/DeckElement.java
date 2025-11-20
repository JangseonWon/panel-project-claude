package com.greencross.lims.client.trello;

import com.google.gwt.core.client.Scheduler;
import com.greencross.lims.api.RouteApi;
import com.greencross.lims.dto.Issue;
import elemental2.dom.*;
import net.sayaya.ui.HTMLElementBuilder;
import net.sayaya.ui.event.HasValueChangeHandlers;
import org.gwtproject.event.shared.HandlerRegistration;
import org.jboss.elemento.EventType;
import org.jboss.elemento.HtmlContentBuilder;

import java.util.*;

import static org.jboss.elemento.Elements.*;

public class DeckElement extends HTMLElementBuilder<HTMLDivElement, DeckElement> implements HasValueChangeHandlers<Issue[]>, HasValueChangeHandlers.ValueChangeEventListener<Issue> {
	static DeckElement build(String prefix, List<Issue> children) {
		return new DeckElement(prefix, children, div());
	}
	private final HtmlContentBuilder<HTMLAnchorElement> parent = a().style("font-weight: bold;");
	private final HtmlContentBuilder<HTMLAnchorElement> cnt = a("#", "_self").css("total");
	private final HtmlContentBuilder<HTMLLabelElement> emergency = label().css("emergency");
	private final HtmlContentBuilder<HTMLDivElement> _this;
	private final HtmlContentBuilder<HTMLDivElement> deck;
	private final HtmlContentBuilder<HTMLDivElement> opened;
	private final HtmlContentBuilder<HTMLDivElement> closeButton = div().css("close-button").add(div().add("-"));
	private final List<IssueElement> issueElements = new LinkedList<>();
	private DeckElement(String prefix, List<Issue> children, HtmlContentBuilder<HTMLDivElement> e) {
		super(e.css("deck"));
		_this = e;
		deck = div();
		opened = div().add(closeButton);
		int cursor = 0;
		for(Issue i: children) {
			IssueElement child = IssueElement.build(i);
			issueElements.add(child);
			opened.add(child);
			child.element().style.top = cursor + "px";
			cursor -= 137;
			child.onValueChange(this);
		}
		if(prefix != null) {
			parent.element().href = prefix + "/" + children.get(0).parent();
			parent.element().target = "_self";
			parent.add(children.get(0).title());
		}
		parent.on(EventType.click, evt->{
			evt.preventDefault();
			evt.stopPropagation();
			RouteApi.location(prefix + "/" + children.get(0).parent(), true, false);
		});
		cnt.add(children.size() + " Sample" + (children.size() > 1?"s":""));
		cnt.on(EventType.click, evt->{
			evt.preventDefault();
			evt.stopPropagation();
			open();
		});
		long day = 24L * 60 * 60 * 1000;
		long today = new Date().getTime();
		long emergencyCnt = children.stream().filter(a->a.dateDue()!=null).filter(a->a.dateDue() < today + 3*day).count();
		if(emergencyCnt > 0) emergency.add(emergencyCnt + " Emergenc" + (emergencyCnt > 1?"ies":"y"));
		closeButton.on(EventType.click, evt->{
			stack();
			fire(ValueChangeEvent.event(evt, value()));
		});
		layout();
	}
	private void layout() {
		HtmlContentBuilder<HTMLDivElement> front = div().css("card", "top");
		HtmlContentBuilder<HTMLDivElement> back1 = div().css("card", "stacked");
		HtmlContentBuilder<HTMLDivElement> back2 = div().css("card", "stacked");
		HtmlContentBuilder<HTMLDivElement> table = div().style("padding: 15px;");
		_this.add(deck.style("position: relative; transition: all 300ms ease 0s, box-shadow 0ms ease 0s;")
					  .add(front.add(table)))
			 .add(opened.style("position: relative; transition: all 300ms ease 0s, box-shadow 0ms ease 0s; display: none;"));
		if(issueElements.size() > 1) deck.add(back1);
		if(issueElements.size() > 2) deck.add(back2);
		table.add(parent)
			 .add(cnt)
			 .add(emergency);
	}
	private void stack() {
		deck.element().style.opacity = CSSProperties.OpacityUnionType.of("1");
		opened.element().style.opacity = CSSProperties.OpacityUnionType.of("0");
		int cursor = 0;
		for(IssueElement child: issueElements) {
			child.element().style.top = cursor + "px";
			child.unselect();
			cursor -= 137;
		}
		Scheduler.get().scheduleFixedDelay(()->{
			deck.element().style.display = "";
			opened.element().style.display = "none";
			return false;
		}, 310);
	}
	private void open() {
		opened.element().style.display = "";
		deck.element().style.display = "none";
		deck.element().style.opacity = CSSProperties.OpacityUnionType.of("0");
		opened.element().style.opacity = CSSProperties.OpacityUnionType.of("1");
		Scheduler.get().scheduleDeferred(()->{
			for(IssueElement child: issueElements) child.style("top: 0");
		});
	}
	@Override
	public DeckElement that() {
		return this;
	}

	@Override
	public Issue[] value() {
		return issueElements.stream().map(IssueElement::value).filter(Objects::nonNull).toArray(Issue[]::new);
	}
	private final Set<ValueChangeEventListener<Issue[]>> handlers = new HashSet<>();
	@Override
	public HandlerRegistration onValueChange(ValueChangeEventListener<Issue[]> valueChangeEventListener) {
		handlers.add(valueChangeEventListener);
		return ()->handlers.remove(valueChangeEventListener);
	}
	@Override
	public void handle(ValueChangeEvent<Issue> valueChangeEvent) {
		fire(ValueChangeEvent.event(valueChangeEvent.event(), value()));
	}
	private void fire(ValueChangeEvent<Issue[]> evt) {
		for(ValueChangeEventListener<Issue[]> handler: handlers) handler.handle(evt);
	}
}
