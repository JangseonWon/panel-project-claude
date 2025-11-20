package com.greencross.lims.entity;

import com.greencross.lims.entity.readOnly.Request;
import com.greencross.lims.entity.readOnly.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(schema="panel", name = "file")
@Data
@Accessors(fluent = true)
public class File {
	@EmbeddedId
	private FilePK pk;
	@CreatedDate
	@Column(name="create_at", nullable = false, updatable = false)
	private LocalDateTime createTime = LocalDateTime.now();
	@CreatedBy
	@ManyToOne
	@JoinColumn(name="create_user")
	private User<?> user;
	@Column
	private String name;
	@Column
	private String extension;
	@Column
	private long size;
	@Column
	private String path;
	@ManyToOne
	@JoinColumns({
			@JoinColumn(name="sample", referencedColumnName="sample", insertable=false, updatable=false),
			@JoinColumn(name="service", referencedColumnName="service", insertable=false, updatable=false)
	})@Setter(AccessLevel.NONE)
	private Request request;
	@Column(insertable=false, updatable=false)
	private Integer sequence;

	@Embeddable
	@Data
	@Accessors(fluent = true)
	public static class FilePK implements Serializable {
		@Column(name="sample", nullable=false, updatable=false)
		private Long sample;
		@Column(name="service", length=8, nullable=false, updatable=false)
		private String service;
		@Column(name="sequence", nullable=false, updatable=false)
		private int sequence;
		public FilePK() {}
		@Builder
		public FilePK(Long sample, String service, int sequence) {
			this.sample = sample;
			this.service = service;
			this.sequence = sequence;
		}
	}
}
