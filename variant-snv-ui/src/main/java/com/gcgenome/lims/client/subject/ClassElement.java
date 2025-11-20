package com.gcgenome.lims.client.subject;

import com.gcgenome.lims.api.ConsensualClassApi;
import com.gcgenome.lims.client.SectionElement;
import com.gcgenome.lims.client.SubjectElement;
import com.gcgenome.lims.dto.SnvConsensualClass;
import com.gcgenome.lims.ui.IconElement;
import com.gcgenome.lims.util.DataTransformUtil;
import elemental2.dom.HTMLDivElement;
import elemental2.dom.HTMLLabelElement;
import elemental2.promise.Promise;
import net.sayaya.ui.ButtonElement;
import net.sayaya.ui.DropDownElement;
import net.sayaya.ui.ListElement;
import net.sayaya.ui.TextFieldElement;
import org.jboss.elemento.HTMLContainerBuilder;

import static org.jboss.elemento.Elements.div;
import static org.jboss.elemento.Elements.label;

public class ClassElement extends SubjectElement<ClassElement> {
	public static ClassElement build(String id) {
		return new ClassElement(id, div());
	}
	private final SectionElement section = SectionElement.build(IconElement.icon(IconElement.Type.Light, "fa-tag"), "Consensual Class");
	private final ListElement options = ListElement.singleLineList()
												   .add(ListElement.singleLine().label(""))
												   .add(ListElement.singleLine().label("Pathogenic"))
												   .add(ListElement.singleLine().label("Likely Pathogenic"))
												   .add(ListElement.singleLine().label("VUS"))
												   .add(ListElement.singleLine().label("Likely Benign"))
												   .add(ListElement.singleLine().label("Benign"))
												   .add(ListElement.singleLine().label("False Positive"));
	private final DropDownElement elemClass = DropDownElement.outlined(options).css("input").text("Class");
	private final TextFieldElement.TextFieldOutlined<String> elemComment = TextFieldElement.textBox().outlined().css("input").style("width: 800px;").text("Comment");
	private final ButtonElement history = ButtonElement.outline().css("button").text("History").before(IconElement.icon(IconElement.Type.Light, "fa-history"));
	private final ButtonElement save = ButtonElement.outline().css("button").text("Save").before(IconElement.icon(IconElement.Type.Light, "fa-save"));
	private final HTMLLabelElement lblLastEditor = label("Last edited by ").element();
	private final HTMLLabelElement iptLastEditor = label().style("margin-left: 10px; margin-right: 10px;").element();
	private final HTMLLabelElement lblLastEditTime = label("on").element();
	private final HTMLLabelElement iptLastEditTime = label().style("margin-left: 10px; margin-right: 10px;").element();
	private final HTMLContainerBuilder<HTMLDivElement> controller = div().style("display: flex;align-items: center;margin-top: 10px;margin-left: 20px;margin-right: 20px;justify-content: space-between;")
																	   .add(div().add(lblLastEditor).add(iptLastEditor).add(lblLastEditTime).add(iptLastEditTime))
																	   .add(div().add(history).add(save));
	protected ClassElement(String id, HTMLContainerBuilder<HTMLDivElement> e) {
		super(id, e);
		e.add(section).add(div().style("display: flex; justify-content: center;align-items: center;").add(elemClass).add(elemComment)).add(controller);
		save.onClick(evt->save());
	}

	private String clazz = null;
	@Override
	public void initialize() {
		ConsensualClassApi.findLast(id).then(this::update);
	}

	public void save() {
		if(elemClass.value().trim().equals(clazz)) ConsensualClassApi.save(id, elemComment.value()).then(this::update);
		else ConsensualClassApi.save(id, elemClass.value(), elemComment.value()).then(this::update);
	}

	private Promise<Void> update(SnvConsensualClass rs) {
		if(rs!=null) {
			clazz = rs.clazz();
			elemClass.select(rs.clazz());
			elemComment.value(rs.comment());
			iptLastEditor.innerHTML = rs.lastModifyBy();
			iptLastEditTime.innerHTML = DataTransformUtil.formatDate(rs.lastModifyAt());
		} else {
			clazz = null;
			elemClass.select("");
			elemComment.value("");
			iptLastEditor.innerHTML = "-";
			iptLastEditTime.innerHTML = "-";
		}
		return null;
	}
	@Override
	public ClassElement that() {
		return this;
	}
}
