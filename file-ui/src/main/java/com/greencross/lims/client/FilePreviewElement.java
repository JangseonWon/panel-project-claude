package com.greencross.lims.client;

import com.greencross.lims.api.FileApi;
import com.greencross.lims.dto.File;
import elemental2.dom.*;
import net.sayaya.ui.HTMLElementBuilder;
import net.sayaya.ui.event.HasSelectionChangeHandlers;
import org.gwtproject.event.shared.HandlerRegistration;
import org.jboss.elemento.EventType;
import org.jboss.elemento.HtmlContentBuilder;
import org.jboss.elemento.InputBuilder;

import java.util.*;

import static org.jboss.elemento.Elements.*;

public class FilePreviewElement extends HTMLElementBuilder<HTMLDivElement, FilePreviewElement> implements HasSelectionChangeHandlers<File[]> {
	public static FilePreviewElement instance(long sample, String service) {
		return new FilePreviewElement(div(), sample, service);
	}
	private final HtmlContentBuilder<HTMLDivElement> _this;
	private final HTMLElement empty = div().css("empty-label")
			.add(i().css("material-icons").style("font-size: 6rem;transform: translate(-50%, calc(-50% - 3rem));").textContent("cloud_upload"))
			.add(h(3).style(
					"text-align: center;font-size: 2.92rem; line-height: 110%;margin: 1.9466666667rem 0 1.168rem 0;white-space: nowrap;" +
					"transform: translate(-50%, calc(-50% - 6rem));").textContent("Drop file here"))
			.element();
	private final InputBuilder<HTMLInputElement> input = input("file").name("file");
	private final HTMLFormElement form = form().style("display: none;").add(input).element();
	private final FilePreviewItemBagElement bag = new FilePreviewItemBagElement(div());
	private ContentState contentState = ContentState.CLOSE;
	private InputState inputState = InputState.NORMAL;
	private final long sample;
	private final String service;
	private FilePreviewElement(HtmlContentBuilder<HTMLDivElement> e, long sample, String service) {
		super(e);
		this.sample = sample;
		this.service = service;
		_this = e.css("preview2").add(bag).add(empty).add(form);
		e.on(EventType.dragleave, evt->{
			evt.stopPropagation();
			evt.preventDefault();
			inputState = InputState.NORMAL;
			updateStyle();
		});
		e.on(EventType.dragover, evt-> {
			evt.stopPropagation();
			evt.preventDefault();
			inputState = InputState.OVER;
			updateStyle();
		});
		e.on(EventType.drop, evt->{
			evt.stopPropagation();
			evt.preventDefault();
			evt.stopImmediatePropagation();
			drop(evt);
			inputState = InputState.NORMAL;
			updateStyle();
		});
		updateStyle();
	}
	private void drop(DragEvent event) {
		input.element().files = event.dataTransfer.files;
		FileApi.upload(sample, service, new FormData(form))
			   .last(e->FileApi.list(sample, service).last(files->update(files)));
		updateStyle();
	}
	public FilePreviewElement update(File[] files) {
		contentState = ContentState.NOT_EMPTY;
		bag.clear();
		if(files!=null && files.length > 0) Arrays.stream(files).map(FilePreviewItemElement::instance)
												  .peek(e->e.state(FilePreviewItemElement.PreviewItemState.LOADED))
												  .forEach(bag::add);
		else contentState = ContentState.EMPTY;
		updateStyle();
		return that();
	}
	public FilePreviewElement delete(File file) {
		bag.delete(file);
		return that();
	}
	private void updateStyle() {
		switch(inputState) {
			case NORMAL:    ncss("preview-over");break;
			case OVER:      css("preview-over"); break;
		}
		switch(contentState) {
			case CLOSE:     css("preview-closed"); ncss("preview-empty"); break;
			case EMPTY:     ncss("preview-closed"); css("preview-empty"); break;
			case NOT_EMPTY: ncss("preview-closed", "preview-empty"); break;
		}
	}
	@Override
	public FilePreviewElement that() {
		return this;
	}

	@Override
	public File[] selection() {
		return bag.selection();
	}

	@Override
	public HandlerRegistration onSelectionChange(SelectionChangeEventListener<File[]> selectionChangeEventListener) {
		return bag.onSelectionChange(selectionChangeEventListener);
	}

	private enum InputState {
		NORMAL, OVER
	}
	private enum ContentState {
		CLOSE, EMPTY, NOT_EMPTY
	}
	private final static class FilePreviewItemBagElement extends HTMLElementBuilder<HTMLDivElement, FilePreviewItemBagElement> implements HasSelectionChangeHandlers<File[]> {
		private final HtmlContentBuilder<HTMLDivElement> _this;
		private final Map<String, FilePreviewItemElement> map = new HashMap<>();
		private final Set<FilePreviewItemElement> selected = new HashSet<>();
		private FilePreviewItemBagElement(HtmlContentBuilder<HTMLDivElement> e) {
			super(e.css("bag"));
			_this = e;
		}
		public FilePreviewItemBagElement add(FilePreviewItemElement child) {
			_this.add(child);
			child.onStateChange(evt->{
				if(evt.state() == FilePreviewItemElement.PreviewItemState.SELECTED) selected.add(child);
				else selected.remove(child);
				this.element().dispatchEvent(new CustomEvent("change"));
			});
			map.put(id(child.file()), child);
			return that();
		}
		public FilePreviewItemBagElement delete(File file) {
			get(id(file)).ifPresent(elem->delete(elem));
			return that();
		}
		public FilePreviewItemBagElement delete(FilePreviewItemElement child) {
			child.element().remove();
			map.remove(id(child.file()));
			return that();
		}
		private String id(File file) {
			return file.sample() + "/" + file.service() + "/" + file.sequence();
		}
		public Optional<FilePreviewItemElement> get(String id) {
			return Optional.ofNullable(map.get(id));
		}
		public FilePreviewItemBagElement remove(File file) {
			String id = id(file);
			if(map.containsKey(id)) {
				map.get(id).element().remove();
				map.remove(id);
			}
			selected.removeIf(sel -> id(sel.file()).equals(id));
			return that();
		}
		public FilePreviewItemBagElement clear() {
			element().innerHTML = "";
			map.clear();
			selected.clear();
			this.element().dispatchEvent(new CustomEvent("change"));
			return that();
		}
		public boolean isEmpty() {
			return map.isEmpty();
		}
		@Override
		public FilePreviewItemBagElement that() {
			return this;
		}

		@Override
		public File[] selection() {
			return selected.stream().map(FilePreviewItemElement::file).toArray(File[]::new);
		}

		@Override
		public HandlerRegistration onSelectionChange(SelectionChangeEventListener<File[]> selectionChangeEventListener) {
			return onSelectionChange(this.element, selectionChangeEventListener);
		}
	}
}
