package com.greencross.lims.client.trello;

import com.google.gwt.core.client.Scheduler;
import com.greencross.lims.api.IssueApi;
import com.greencross.lims.api.ProgressApi;
import com.greencross.lims.api.RouteApi;
import com.greencross.lims.client.AbstractScene;
import com.greencross.lims.client.Router;
import com.greencross.lims.dto.Issue;
import com.greencross.lims.dto.Query;
import com.greencross.lims.ui.IconElement;
import elemental2.core.JsDate;
import elemental2.dom.DomGlobal;
import elemental2.dom.HTMLDivElement;
import elemental2.dom.HTMLElement;
import elemental2.dom.HTMLLabelElement;
import net.sayaya.ui.BreadcumbElement;
import net.sayaya.ui.ButtonElement;
import net.sayaya.ui.ButtonElementToggle;
import net.sayaya.ui.TextFieldElement;
import org.jboss.elemento.HtmlContentBuilder;
import org.jboss.elemento.IsElement;

import java.util.*;
import java.util.stream.Collectors;

import static org.jboss.elemento.Elements.div;
import static org.jboss.elemento.Elements.label;

public class TrelloElement extends AbstractScene<TrelloElement> {
	private final HtmlContentBuilder<HTMLLabelElement> title = label().add("Trello");
	private final BreadcumbElement breadcumb = BreadcumbElement.home(IconElement.icon(IconElement.Type.Regular, "fa-home").style("font-size: 18px;"), evt->{
		RouteApi.location("", true, false);
	}).splitter(IconElement.icon(IconElement.Type.Light, "fa-chevron-double-right").style("font-size: 18px;").element())
	.add("패널검사", evt->{
		evt.preventDefault();
		evt.stopPropagation();
	}).add("Trello", evt->{
		evt.preventDefault();
		evt.stopPropagation();
		Router.location("", true);
	});
	private final TrackElement[] tracks = new TrackElement[]{
			TrackElement.build("등록", "#F645D6C0"),
			TrackElement.build("검사 시작", "#00D068C0"),
			TrackElement.build("시퀀싱", "#B747F6C0"),
			TrackElement.build("BI 분석 완료", "#5A8CF4C0"),
			TrackElement.build("판독 완료", "#39BEDAC0"),
			TrackElement.build("완료", "#FFCD00C0")
	};
	private final HtmlContentBuilder<HTMLDivElement> div = div().css("background");
	// private final ButtonElement discard = ButtonElement.outline().css("button").text("Discard").before(IconElement.icon(IconElement.Type.Solid, "fa-window-close")).enabled(false);
	private final TextFieldElement<JsDate> iptDateFrom = TextFieldElement.dateBox().outlined().css("button").style("width: 155px;border-right: 0px !important;").text("from").value(yesterday());
	private final TextFieldElement<JsDate> iptDateTo = TextFieldElement.dateBox().outlined().css("button").style("width: 155px;").text("to").value(new JsDate());
	private final ButtonElement btnSearch = ButtonElement.outline().css("button").text("Search").before(IconElement.icon(IconElement.Type.Light, "fa-search")).style("display: inline-block;");
	private JsDate dateFrom = iptDateFrom.value();
	private JsDate dateTo = iptDateTo.value();
	private final ButtonElementToggle btnBRCA		= ButtonElement.toggle().css("button").text("BRCA").value(false);
	private final ButtonElementToggle btnCancer		= ButtonElement.toggle().css("button").text("Cancer").value(false);
	private final ButtonElementToggle btnRare		= ButtonElement.toggle().css("button").text("RD").value(false);
	private final ButtonElementToggle btnHema		= ButtonElement.toggle().css("button").text("HEMA").value(false);
	private final ButtonElementToggle btnSolid		= ButtonElement.toggle().css("button").text("ST").value(false);
	private final ButtonElementToggle btnWes		= ButtonElement.toggle().css("button").text("WES").value(false);
	private final ButtonElementToggle btnDes		= ButtonElement.toggle().css("button").text("DES").value(false);
	private final ButtonElementToggle btnDgs		= ButtonElement.toggle().css("button").text("DGS").value(false);
	private final ButtonElementToggle btnHrd		= ButtonElement.toggle().css("button").text("HRD").value(false);
	private final Set<DeckElement> decks = new HashSet<>();
	public TrelloElement(Query query) {
		super(query);
		for(TrackElement track: tracks) div.add(track);
		iptDateFrom.onValueChange(evt->{
			if(!chk()) iptDateFrom.value(dateFrom);
			else dateFrom = iptDateFrom.value();
		});
		iptDateTo.onValueChange(evt->{
			if(!chk()) iptDateTo.value(dateTo);
			else dateTo = iptDateTo.value();
		});
		Scheduler.get().scheduleDeferred(()->((HTMLElement)(btnSearch.element().parentElement)).style.display = "flex");
	}
	private boolean chk() {
		if(duration(iptDateTo.value(), iptDateFrom.value()) > 180) {
			DomGlobal.window.alert("날짜 구간은 최대 180주입니다.");
			return false;
		}
		return true;
	}
	private int duration(JsDate to, JsDate from) {
		double milli = to.valueOf() - from.valueOf();
		return (int)(milli / (60*60*1000*24));
	}
	private static JsDate yesterday() {
		JsDate today = new JsDate();
		JsDate yesterday = new JsDate(today);
		yesterday.setDate(yesterday.getDate()-60);
		yesterday.setHours(0, 0, 0, 0);
		return yesterday;
	}
	@Override
	protected IconElement icon() {
		return IconElement.icon(IconElement.Type.Brands, "fa-trello");
	}
	@Override
	protected HtmlContentBuilder<HTMLLabelElement> title() {
		return title;
	}
	@Override
	protected BreadcumbElement breadcumb() {
		return breadcumb;
	}
	@Override
	protected IsElement<?>[][] controls() {
		return new IsElement<?>[][]{
				new IsElement<?>[] {iptDateFrom},
				new IsElement<?>[] {label("~").style("line-height: 36px; margin-left: 2px; margin-right: 2px;")},
				new IsElement<?>[] {iptDateTo, btnSearch},
				new IsElement<?>[] {btnBRCA, btnCancer, btnRare, btnHema, btnSolid, btnWes, btnDes, btnDgs, btnHrd}
		};
	}
	@Override
	protected IsElement<?>[] contents() {
		return new IsElement<?>[] {div};
	}
	private void update(Query query) {
		decks.clear();
		for(TrackElement track: tracks) track.clear();

		Query proxy = new Query().sortBy("sample").asc(true).limit(10000);
		List<Query.Filter> filters = new LinkedList<>();
		if(query.filters()!=null) for(Query.Filter filter: query.filters()) filters.add(filter);
		filters.add(new Query.Filter().key("from").value(String.valueOf(iptDateFrom.value().getTime())));
		filters.add(new Query.Filter().key("to").value(String.valueOf(iptDateTo.value().getTime())));
		filters.add(new Query.Filter().key("registered").value("false"));
		filters.add(new Query.Filter().key("canceled").value("false"));
		filters.add(new Query.Filter().key("deleted").value("false"));
		IssueApi.search(proxy.filters(filters.toArray(new Query.Filter[0]))).last(this::update);
	}
	private void update(Issue[] issues) {
		List<Issue> empty = Arrays.stream(issues).filter(issue->issue.state() == null).collect(Collectors.toList());
		for(DeckElement deck: map(null, empty)) tracks[0].add(deck);
		Map<String, List<Issue>> groupingBy = Arrays.stream(issues).filter(issue->issue.state() != null).collect(Collectors.groupingBy(Issue::state));
		for(String key: groupingBy.keySet()) {
			String prefix = stateToPrefix(key);
			int idx = stateToTrack(key);
			DeckElement[] decks = map(prefix, groupingBy.get(key));
			for(DeckElement deck: decks) {
				tracks[idx].add(deck);
				this.decks.add(deck);
				deck.onValueChange(evt->{
					Issue[] selected = selected();
					// if(selected == null || selected.length <= 0) discard.enabled(false);
					// else discard.enabled(true);
				});
			}
		}
	}
	@Override
	public void update() {
		update(query());
	}
	private Issue[] selected() {
		return decks.stream().map(DeckElement::value).filter(Objects::nonNull).flatMap(Arrays::stream).toArray(Issue[]::new);
	}
	private static String stateToPrefix(String sheet) {
		if("5fd630a8-f9fc-4a17-872a-5b3e82929d6a".equals(sheet)) return "패널검사/Worklist";
		if("471a0005-6fef-4bcd-88fd-60b37b52d94a".equals(sheet)) return "패널검사/DNA";
		if("7f0495b6-5ce0-498b-8c7b-6c3d78f941ad".equals(sheet)) return "패널검사/Library";
		if("114cfcf5-2bdb-4c71-bfa2-97f54a45c1f2".equals(sheet)) return "패널검사/Sequencing";
		else return null;
	}
	private static int stateToTrack(String sheet) {
		if("검사 시작".equals(sheet))	return 1;
		if("시퀀싱".equals(sheet))		return 2;
		if("BI 분석 완료".equals(sheet))	return 3;
		if("판독 완료".equals(sheet))	return 4;
		if("완료".equals(sheet))			return 5;
		else return 0;
	}
	public static DeckElement[] map(String prefix, List<Issue> issues) {
		Map<Integer, List<Issue>> groups = issues.stream().collect(Collectors.groupingBy(Issue::parent));
		return groups.keySet().stream().sorted().map(groups::get).map(list->DeckElement.build(prefix, list)).toArray(DeckElement[]::new);
	}
	@Override
	public TrelloElement that() {
		return this;
	}
}
