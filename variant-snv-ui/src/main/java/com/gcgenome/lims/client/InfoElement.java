package com.gcgenome.lims.client;

import com.gcgenome.lims.client.subject.*;
import elemental2.dom.HTMLDivElement;
import jsinterop.base.JsPropertyMap;
import net.sayaya.ui.HTMLElementBuilder;
import org.jboss.elemento.HTMLContainerBuilder;

import static org.jboss.elemento.Elements.div;

public class InfoElement extends HTMLElementBuilder<HTMLDivElement, InfoElement> {
	public static InfoElement build(String id, JsPropertyMap snv) {
		return new InfoElement(id, snv, div());
	}
	private InterpretationElement interpretationElement;
	private SampleElement sampleElement;
	private RequestElement requestElement;
	private ClassElement classElement;
	private FreqElement freqElement;
	private PredictionElement predElement;
	private DiseaseElement diseaseElement;
	private CommentsElement commentsElement;
	private InfoElement(String id, JsPropertyMap snv, HTMLContainerBuilder<HTMLDivElement> e) {
		super(e.style("min-height: calc(100% - 135px);\n" +
											"    background: rgb(255, 255, 255);\n" +
											"    overflow: auto;\n" +
											"    display: flex;\n" +
											"    flex-flow: column nowrap;\n" +
											"    align-content: stretch;\n" +
											"    align-items: stretch; padding-bottom: 20px;"));
		interpretationElement = InterpretationElement.build(id, snv);
		sampleElement = SampleElement.build(id);
		requestElement = RequestElement.build(id);
		classElement = ClassElement.build(id);
		freqElement = FreqElement.build(id, snv);
		predElement = PredictionElement.build(id, snv);
		diseaseElement = DiseaseElement.build(id, snv);
		commentsElement = CommentsElement.build(id, snv);
		e.add(classElement);
		e.add(interpretationElement);
		e.add(sampleElement);
		e.add(requestElement);
		e.add(freqElement);
		e.add(predElement);
		e.add(diseaseElement);
		e.add(commentsElement);
		sampleElement.initialize();
		requestElement.initialize();
		interpretationElement.initialize();
		classElement.initialize();
		freqElement.initialize();
		predElement.initialize();
		diseaseElement.initialize();
		commentsElement.initialize();
	}

	@Override
	public InfoElement that() {
		return this;
	}
}
