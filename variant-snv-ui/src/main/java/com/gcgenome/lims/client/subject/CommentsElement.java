package com.gcgenome.lims.client.subject;

import com.gcgenome.lims.api.CommentApi;
import com.gcgenome.lims.client.SectionElement;
import com.gcgenome.lims.client.SubjectElement;
import com.gcgenome.lims.dto.Comment;
import com.gcgenome.lims.ui.IconElement;
import com.gcgenome.lims.util.DataTransformUtil;
import elemental2.dom.HTMLDivElement;
import elemental2.dom.HTMLLabelElement;
import elemental2.promise.Promise;
import jsinterop.base.JsPropertyMap;
import net.sayaya.ui.ButtonElement;
import net.sayaya.ui.HTMLElementBuilder;
import net.sayaya.ui.TextAreaElement;
import org.jboss.elemento.HTMLContainerBuilder;

import static org.jboss.elemento.Elements.div;
import static org.jboss.elemento.Elements.label;

public class CommentsElement extends SubjectElement<CommentsElement> {
	public static CommentsElement build(String id, JsPropertyMap snv) {
		return new CommentsElement(id, div());
	}
	private final SectionElement section = SectionElement.build(IconElement.icon(IconElement.Type.Light, "fa-comments"), "Comment");
	private final ButtonElement add = ButtonElement.outline().css("button").text("Add").before(IconElement.icon(IconElement.Type.Light, "fa-comment"));
	private final HTMLContainerBuilder<HTMLDivElement> controller = div().style("display: flex;justify-content: flex-end;align-items: center;margin-right: 20px;")
																	   .add(add);
	private final HTMLContainerBuilder<HTMLDivElement> comments = div().style("overflow: hidden; padding-bottom: 15px; min-height: 7px; border-top: 1px solid #AAA; margin-right: 20px; margin-left: 20px;");
	protected CommentsElement(String id, HTMLContainerBuilder<HTMLDivElement> e) {
		super(id, e);
		e.add(section)
		 .add(comments)
		 .add(controller);
		add.onClick(this::append);
	}

	@Override
	public void initialize() {
		CommentApi.find(id).then(comments->{
			for(Comment comment: comments) create().update(comment).then(s-> {
				this.comments.add(s.state(State.VIEW));
				return null;
			});
			return null;
		});
	}
	private void append(Object evt) {
		CommentElement child = create().state(State.EDIT);
		comments.add(child);
	}
	@Override
	public CommentsElement that() {
		return this;
	}

	public CommentElement create() {
		return new CommentElement(div());
	}
	public CommentElement build(Comment comment) {
		return new CommentElement(div());
	}
	public class CommentElement extends HTMLElementBuilder<HTMLDivElement, CommentElement> {
		private final HTMLContainerBuilder<HTMLDivElement> _this;
		private final ButtonElement btnEdit = ButtonElement.outline().css("button").text("Edit").before(IconElement.icon(IconElement.Type.Light, "fa-edit"));
		private final ButtonElement btnSave = ButtonElement.outline().css("button").text("Save").before(IconElement.icon(IconElement.Type.Light, "fa-save"));
		private final ButtonElement btnDelete = ButtonElement.outline().css("button").text("Delete").before(IconElement.icon(IconElement.Type.Light, "fa-times-circle"));
		private final TextAreaElement<String> iptComment = TextAreaElement.textBox().outlined().style("width:100%;");
		private final HTMLLabelElement iptLastEditTime = label().element();
		private final HTMLContainerBuilder<HTMLDivElement> controller = div().style("display: flex;justify-content: flex-end;align-items: center;margin-bottom: 10px;");
		private final HTMLContainerBuilder<HTMLDivElement> header = div().style("display: flex;align-items: center;margin-top: 10px;justify-content: space-between;")
																		   .add(iptLastEditTime).add(controller);
		private Comment value;
		public CommentElement(HTMLContainerBuilder<HTMLDivElement> e) {
			super(e);
			_this = e.style("margin-top: 10px;");
			btnEdit.onClick(evt->state(State.EDIT));
			btnSave.onClick(this::save);
			btnDelete.onClick(this::delete);
			_this.add(header).add(iptComment);
		}
		private Promise<CommentElement> update(Comment comment) {
			this.value = comment;
			iptLastEditTime.innerHTML = DataTransformUtil.formatDateTime(comment.lastModifyAt());
			iptComment.text(comment.createBy()).value(comment.comment());
			return Promise.resolve(that());
		}
		public CommentElement state(State state) {
			if(state == State.VIEW) viewmode();
			else if(state == State.EDIT) editmode();
			return that();
		}
		private void viewmode() {
			iptComment.readOnly(true);
			controller.add(btnEdit).add(btnDelete);
			btnSave.element().remove();
			add.element().style.display = null;
		}
		private void editmode() {
			iptComment.readOnly(false);
			btnEdit.element().remove();
			btnDelete.element().remove();
			controller.add(btnSave);
			add.element().style.display = "none";
		}
		private void save(Object evt) {
			if(value==null) CommentApi.save(id, iptComment.value()).then(this::update);
			else CommentApi.save(id, value.createAt(), iptComment.value()).then(this::update);
			state(State.VIEW);
		}
		private void delete(Object evt) {
			CommentApi.delete(id, value.createAt()).then(r->{
				this.element().remove();
				return null;
			});
		}
		@Override
		public CommentElement that() {
			return this;
		}
	}
	private enum State {
		VIEW, EDIT
	}
}
