package com.gcgenome.lims.client;

import com.gcgenome.lims.api.ReferralApi;
import com.gcgenome.lims.dto.Message;
import com.gcgenome.lims.dto.Page;
import com.gcgenome.lims.ui.IconElement;
import elemental2.core.Global;
import elemental2.dom.*;
import net.sayaya.ui.HTMLElementBuilder;
import org.jboss.elemento.EventType;
import org.jboss.elemento.HTMLContainerBuilder;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static org.jboss.elemento.Elements.*;

public class CollapseElement2 extends HTMLElementBuilder<HTMLDivElement, CollapseElement2> implements CollapseElement<HTMLDivElement> {
	public static CollapseElement2 build(String id, long sample) {
		return new CollapseElement2(div(), id, sample);
	}
	private final IconElement icon = IconElement.icon(IconElement.Type.Light, "fa-clipboard-list");
	private final HTMLContainerBuilder<HTMLElement> title = span().css("mdc-list-item__primary-text").add("Request");
	private final HTMLContainerBuilder<HTMLElement> info = span().css("mdc-list-item__secondary-text").add("접수된 검사 정보를 열람합니다.");
	private final HTMLContainerBuilder<HTMLElement> meta = span().css("mdc-list-item__meta").style("margin-right: 40px;");
	private final HTMLContainerBuilder<HTMLLabelElement> referral = label();
	private final HTMLContainerBuilder<HTMLLabelElement> agreement = label();
	private final HTMLContainerBuilder<HTMLTableElement> summary = table().style("text-align: right; font-size: var(--mdc-typography-caption-font-size, 0.75rem);")
			.add(tr().add(td().style("color: var(--mdc-theme-text-secondary-on-background, rgba(0, 0, 0, 0.54));")
					.add("의뢰서: ")).add(td().add(referral)))
			.add(tr().add(td().style("color: var(--mdc-theme-text-secondary-on-background, rgba(0, 0, 0, 0.54));")
					.add("동의서: ")).add(td().add(agreement)));
	private final HTMLContainerBuilder<HTMLDivElement> _this;
	private final String id;
	private final long sample;
	private CollapseElement2(HTMLContainerBuilder<HTMLDivElement> e, String id, long sample) {
		super(e.css("work-summary")
				.style("margin-left: 16px;margin-right: 16px;display: flex; flex-direction: row; " +
						"flex-wrap: wrap; align-content: space-between; justify-content: flex-start; " +
						"align-items: center; cursor: pointer;"));
		this.id = id;
		this.sample = sample;
		_this = e;
		layout();
		this.on(EventType.click, evt->fireStateChangeEvent());
	}
	@Override
	public void update() {
		Message msg = Message.builder().id(id).type(Message.MessageType.COLLAPSE).param("64px").build();
		DomGlobal.window.parent.postMessage(Global.JSON.stringify(msg), "*");
		meta.element().innerHTML="";
		ReferralApi.pages(sample).then(pages->{
			if(pages!=null && pages.length > 0) {
				boolean hasReferral = false;
				boolean hasAgreement = false;
				for(Page page: pages) {
					if(page.referral()) hasReferral = true;
					if(page.agreement()) hasAgreement = true;
				}
				referral.textContent(hasReferral?"OK":"Required");
				if(hasReferral) referral.style("color:#007B5F;font-weight: bold;");
				else referral.style("color:#AD1747;font-weight: bold;");
				agreement.textContent(hasAgreement?"OK":"Required");
				if(hasAgreement) agreement.style("color:#007B5F;font-weight: bold;");
				else agreement.style("color:#AD1747;font-weight: bold;");
				meta.add(summary);
			} else meta.add(label("No documents"));
			return null;
		});
	}
	private void layout() {
		_this.add(span().css("mdc-list-item__graphic").style("height: auto; align-self: unset;").add(icon))
				.add(div().css("mdc-list-item__text").style("margin-bottom: 10px;").add(title).add(info))
				.add(meta);
	}

	@Override
	public CollapseElement2 that() {
		return this;
	}

	private final Set<StateChangeEventListener<WindowState>> listeners = new HashSet<>();
	@Override
	public Collection<StateChangeEventListener<WindowState>> listeners() {
		return listeners;
	}

	@Override
	public WindowState state() {
		return WindowState.FULLSCREEN;
	}
}
