package com.greencross.lims.entity;

import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
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
	@Type(type= "com.greencross.lims.entity.udt.MapConverter2")
	private Map<String, Object> value;
	@ToString.Exclude
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "interpretation")
	@OrderBy("create_at DESC")
	@Setter(AccessLevel.NONE)
	private List<Report> reports;

	@Transient
	private Request request;

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
	/*@PrePersist
	@PreUpdate
	protected void update() {
		System.out.println("User:" + user);
		if(user!=null) lastModifiedBy = user.id();
	}*/
}
