package com.greencross.lims.entity;

import com.greencross.lims.trans.LocalDateTimeToEpoch;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(schema="panel", name = "report")
@Data
@Accessors(fluent = true)
@NoArgsConstructor
public class Report {
	@EmbeddedId
	private ReportPK pk;
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
	@Column(name="publish_at")
	private LocalDateTime publishAt;
	@Column(name="file")
	private UUID file;
	@Column(name="name")
	private String name;
	@Column(name="size")
	private int size;
	@Column(name="etc")
	private String etc;
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="sample", referencedColumnName="sample", insertable=false, updatable=false),
		@JoinColumn(name="service", referencedColumnName="service", insertable=false, updatable=false)
	})@Setter(AccessLevel.NONE)
	private Interpretation interpretation;
	public LocalDateTime createAt() {
		return LocalDateTimeToEpoch.map(pk().createAt);
	}
	@Embeddable
	@Data
	@Accessors(fluent = true)
	public static class ReportPK implements Serializable {
		@Column(name="sample", nullable=false, updatable=false)
		private Long sample;
		@Column(name="service", length=8, nullable=false, updatable=false)
		private String service;
		@Column(name="create_at", nullable=false, updatable=false)
		private long createAt;
		public ReportPK() {}
		@Builder
		public ReportPK(Long sample, String service, long createAt) {
			this.sample = sample;
			this.service = service;
			this.createAt = createAt;
		}
	}
}
