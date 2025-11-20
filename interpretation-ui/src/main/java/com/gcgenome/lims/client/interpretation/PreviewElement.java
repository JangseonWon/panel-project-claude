package com.gcgenome.lims.client.interpretation;

import com.gcgenome.lims.ui.IconElement;
import elemental2.dom.Blob;
import elemental2.dom.HTMLDivElement;
import elemental2.dom.HTMLIFrameElement;
import elemental2.dom.URL;
import net.sayaya.ui.ButtonElement;
import net.sayaya.ui.HTMLElementBuilder;
import org.jboss.elemento.HTMLContainerBuilder;

import static org.jboss.elemento.Elements.*;

public class PreviewElement extends HTMLElementBuilder<HTMLDivElement, PreviewElement> {
	public static PreviewElement build(Blob pdf) {
		return new PreviewElement(div(), pdf);
	}
	private final HTMLContainerBuilder<HTMLIFrameElement> pdf = iframe().style("width: 100%; height: calc(100% - 60px); border: 1px solid #ddd;");
	private final HTMLContainerBuilder<HTMLDivElement> controller = div().css("controller");
	private final ButtonElement btnClose = ButtonElement.outline().css("button").before(IconElement.icon("undo")).text("Cancel");
	private final ButtonElement btnSave = ButtonElement.outline().css("button").before(IconElement.icon("description")).text("Confirm");
	private final HTMLContainerBuilder<HTMLDivElement> container = div().add(pdf).add(controller.add(span().add(btnClose).add(btnSave)));
	private final HTMLContainerBuilder<HTMLDivElement> _this;
	private Callback<Void> onConfirm;
	private PreviewElement(HTMLContainerBuilder<HTMLDivElement> e, Blob blob) {
		super(e.css("preview"));
		_this = e.add(container);
		pdf.element().setAttribute("src", URL.createObjectURL(blob));
		btnClose.onClick(evt->this.element().remove());
		btnSave.onClick(evt->{
			btnSave.enabled(false);
			onConfirm.onSuccess(null);
		});
	}
	public PreviewElement onConfirm(Callback<Void> onConfirm) {
		this.onConfirm = onConfirm;
		return that();
	}
	@Override
	public PreviewElement that() {
		return this;
	}
}
