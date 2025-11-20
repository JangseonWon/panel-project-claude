package com.greencross.lims.client;

import com.greencross.lims.api.FileApi;
import com.greencross.lims.dto.File;
import com.greencross.lims.dto.Message;
import com.greencross.lims.ui.IconElement;
import elemental2.core.Global;
import elemental2.dom.DomGlobal;
import elemental2.dom.HTMLDivElement;
import net.sayaya.ui.ButtonElement;
import net.sayaya.ui.HTMLElementBuilder;
import org.jboss.elemento.HtmlContentBuilder;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static org.jboss.elemento.Elements.div;
import static org.jboss.elemento.Elements.span;

public class ExpandElementImpl extends HTMLElementBuilder<HTMLDivElement, ExpandElementImpl> implements ExpandElement<HTMLDivElement> {
	public static ExpandElementImpl build(String id, long sample, String service) {
		return new ExpandElementImpl(div(), id, sample, service);
	}
	private final Section info = new Section(IconElement.icon(IconElement.Type.Light, "fa-file-plus"), "File");
	private final FilePreviewElement elemFilePreview;
	private final HtmlContentBuilder<HTMLDivElement> controller = div().css("controller");
	private final ButtonElement btnHide = ButtonElement.outline().css("button").before(IconElement.icon("close")).text("Close");
	private final ButtonElement btnDelete = ButtonElement.outline().css("button").before(IconElement.icon("delete")).text("Delete").enabled(false);
	private final String id;
	private final long sample;
	private final String service;
	private ExpandElementImpl(HtmlContentBuilder<HTMLDivElement> e, String id, long sample, String service) {
		super(e);
		this.id = id;
		this.sample = sample;
		this.service = service;
		elemFilePreview = FilePreviewElement.instance(sample, service);
		e.css("work").style("height: 100vh;")
		 .add(info)
		 .add(div().css("layout").add(elemFilePreview.css("layout-item")))
		 .add(controller.add(span().style("margin-left: 10px;").add(btnDelete).add(btnHide)));

		btnHide.onClick(evt->fireStateChangeEvent());
		elemFilePreview.onSelectionChange(evt->btnDelete.enabled(evt.selection()!=null && evt.selection().length>0));
		btnDelete.onClick(evt->{
			for(File file: elemFilePreview.selection()) FileApi.delete(file.sample(), file.service(), file.name()).last(f->elemFilePreview.delete(file));
			btnDelete.enabled(false);
		});
	}

	public void update() {
		Message msg = new Message().id(id).type(Message.MessageType.STRETCH);
		DomGlobal.window.parent.postMessage(Global.JSON.stringify(msg), "*");
		FileApi.list(sample, service).last(files->elemFilePreview.update(files));
	}

	@Override
	public ExpandElementImpl that() {
		return this;
	}

	private final Set<StateChangeEventListener<WindowState>> listeners = new HashSet<>();
	@Override
	public Collection<StateChangeEventListener<WindowState>> listeners() {
		return listeners;
	}

	@Override
	public WindowState state() {
		return WindowState.COLLAPSE;
	}
}
