package com.greencross.lims.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(schema = "panel", name = "interpretation", indexes = {
		@Index(columnList = "value")
})@Data
@Accessors(fluent = true)
public class Interpretation {
	@EmbeddedId
	private InterpretationPK pk;
	@CreatedDate
	@Column(name="create_at", nullable = false, updatable = false)
	private LocalDateTime createTime = LocalDateTime.now();
	@CreatedBy
	@ManyToOne
	@JoinColumn(name="create_user")
	private User<?> user;
	@LastModifiedDate
	@Column(name="last_modify_at")
	private LocalDateTime lastModifyAt;
	@LastModifiedBy
	@ManyToOne
	@JoinColumn(name="last_modify_user")
	private User<?> lastModifiedBy;
	@Column(name="value", columnDefinition="jsonb")
	@Type(com.greencross.lims.entity.udt.MapConverter2.class)
	private Map<String, Object> value;
	@Column(name="publish_at")
	private LocalDateTime publishAt;
	@ToString.Exclude
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "interpretation")
	@OrderBy("create_at DESC")
	@Setter(AccessLevel.NONE)
	private List<Report> reports;
	@ManyToOne
	@JoinColumns({
			@JoinColumn(name="sample", referencedColumnName="sample", insertable=false, updatable=false),
			@JoinColumn(name="service", referencedColumnName="service", insertable=false, updatable=false)
	})@Setter(AccessLevel.NONE)
	private Request request;

	public Request request() {
		return request;
	}
	@Embeddable
	@Data
	@Accessors(fluent = true)
	public static class InterpretationPK implements Serializable {
		@Column(name="sample", nullable=false, updatable=false)
		private Long sample;
		@Column(name="service", length=8, nullable=false, updatable=false)
		private String service;
		public InterpretationPK() {}
		@Builder
		public InterpretationPK(Long sample, String service) {
			this.sample = sample;
			this.service = service;
		}
	}
}
