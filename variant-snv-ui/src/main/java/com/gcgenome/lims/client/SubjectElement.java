package com.gcgenome.lims.client;

import elemental2.dom.HTMLDivElement;
import net.sayaya.ui.HTMLElementBuilder;
import org.jboss.elemento.HTMLContainerBuilder;

public abstract class SubjectElement<T extends SubjectElement<T>> extends HTMLElementBuilder<HTMLDivElement, T> {
	protected final String id;
	protected SubjectElement(String id, HTMLContainerBuilder<HTMLDivElement> e) {
		super(e);
		this.id = id;
	}
	public abstract void initialize();
}
