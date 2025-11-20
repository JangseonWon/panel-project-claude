package com.gcgenome.lims.client;

import com.gcgenome.lims.api.ReferralApi;
import elemental2.dom.HTMLIFrameElement;
import elemental2.dom.URL;
import net.sayaya.ui.HTMLElementBuilder;
import org.jboss.elemento.HTMLContainerBuilder;

import static org.jboss.elemento.Elements.iframe;

public class ViewElement extends HTMLElementBuilder<HTMLIFrameElement, ViewElement> {
	public static ViewElement build(long sample, int page) {
		return new ViewElement(iframe(), sample, page);
	}
	private final HTMLContainerBuilder<HTMLIFrameElement> pdf;
	private ViewElement(HTMLContainerBuilder<HTMLIFrameElement> pdf, long sample, int page) {
		super(pdf.style("width: 1000px; height: 76vh; border: 1px solid #ddd;"));
		this.pdf = pdf;
		ReferralApi.download(sample, page).then(blob->{pdf.element().setAttribute("src", URL.createObjectURL(blob) + "#navpanes=0&view=fitH");
            return null;
        });
	}
	@Override
	public ViewElement that() {
		return this;
	}
}
