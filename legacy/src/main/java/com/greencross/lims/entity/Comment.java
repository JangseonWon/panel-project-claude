package com.greencross.lims.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "comment", indexes = {
	@Index(columnList = "sample, service")
})
@Data
@Accessors(fluent = true)
public class Comment implements Serializable {
	@EmbeddedId
	private CommentPK pk = new CommentPK();
	@Column(name="create_time", insertable=false, updatable=false)
	private LocalDateTime createTime;
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumns({
		@JoinColumn(name="sample", referencedColumnName="sample", insertable=false, updatable=false),
		@JoinColumn(name="service", referencedColumnName="service", insertable=false, updatable=false)})
	private Issue issue;
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumns({
		@JoinColumn(name="sample", referencedColumnName="sample", insertable=false, updatable=false),
		@JoinColumn(name="service", referencedColumnName="service", insertable=false, updatable=false)})
	private Request request;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="sample", referencedColumnName="id", insertable=false, updatable=false)
	private Sample sample;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="service", referencedColumnName="id", insertable=false, updatable=false)
	private Service service;
	@ManyToOne
	@JoinColumn(name="\"user\"", referencedColumnName="id")
	private User<?> user;
	@Column(name="content")
	private String content;

	@Embeddable
	@Data
	@Accessors(fluent = true)
	public static class CommentPK implements Serializable {
		@Column(name="sample", nullable=false, updatable=false)
		private Long sample;
		@Column(name="service", length=8, nullable=false, updatable=false)
		private String service;
		@Column(name="create_time", nullable=false, updatable=false)
		private LocalDateTime createTime = LocalDateTime.now();
	}
}
