package com.gcgenome.lims.client;

import elemental2.dom.HTMLAudioElement;
import elemental2.dom.HTMLSourceElement;

import static org.jboss.elemento.Elements.audio;
import static org.jboss.elemento.Elements.source;

public class Effect {
	private final HTMLSourceElement source = source().element();
	private final HTMLAudioElement audio = audio().add(source).element();
	private Effect(String url) {
		source.src = url;
		// String extension = url.substring(url.length()-3).toLowerCase();
		if(url.toLowerCase().endsWith("wav")) source.type = "audio/wav";
		else if(url.toLowerCase().endsWith("ogg")) source.type = "audio/ogg";
	}
	public void play() {
		audio.pause();
		audio.currentTime = 0;
		audio.play();
	}

	public static Effect SUCCESS = new Effect("./sound/notification_high-intensity.ogg");
	public static Effect FAILURE = new Effect("./sound/alert_error-03.ogg");
}
