package com.greencross.lims.client;

import elemental2.dom.HTMLElement;
import net.sayaya.ui.event.HasStateChangeHandlers;
import org.jboss.elemento.IsElement;

public interface ExpandElement<E extends HTMLElement> extends IsElement<E>, HasStateChangeHandlers<WindowState> {
	default void update() {}
}
