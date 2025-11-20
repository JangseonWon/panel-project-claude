package com.gcgenome.lims.client;

import com.gcgenome.lims.dto.Page;
import elemental2.dom.CSSProperties;
import elemental2.dom.HTMLDivElement;
import elemental2.dom.HTMLElement;
import net.sayaya.ui.*;
import org.jboss.elemento.Elements;
import org.jboss.elemento.EventType;
import org.jboss.elemento.HTMLContainerBuilder;

import static org.jboss.elemento.Elements.div;

public class PageElement extends HTMLElementBuilder<HTMLDivElement, PageElement>  {
    private final CheckBoxElement iptAgreement = CheckBoxElement.checkBox(false).text("동의서").style("font-size: 0.8em;height: 25px; margin-right: 12px;").enabled(false);
    private final CheckBoxElement iptReferral = CheckBoxElement.checkBox(false).text("의뢰서").style("font-size: 0.8em;height: 25px; margin-right: 12px;").enabled(false);

    public static PageElement build(long sample, Page page) {
        return new PageElement(div(), sample, page);
    }
    private PageElement(HTMLContainerBuilder<HTMLDivElement> e, long sample, Page page) {
        super(e.css("page"));
        e.add(iptAgreement).add(iptReferral);
        if(page.agreement()!=null) iptAgreement.value(page.agreement());
        if(page.referral()!=null)  iptReferral.value(page.referral());
        String color = "transparent";
        if(page.referral()!=null && page.referral()) color = "#FF6F61";
        this.element().style.background = color;
        e.on(EventType.click, evt->{
            ViewElement view = ViewElement.build(sample, page.index());
            ButtonElementText close = ButtonElement.outline().text("Close");
            Dialog dialog = Dialog.confirmation("Page View", close);
            HTMLElement surface = (HTMLElement) dialog.element().getElementsByClassName("mdc-dialog__surface").item(0);
            surface.style.maxWidth = CSSProperties.MaxWidthUnionType.of("1250px");
            close.onClick(evt2->{
                dialog.close();
                dialog.element().remove();
            });
            dialog.add(view);
            Elements.body().add(dialog);
            dialog.open();
        });
    }
    @Override
    public PageElement that() {
        return this;
    }
}
