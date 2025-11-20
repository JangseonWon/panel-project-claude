package com.gcgenome.lims.entity;

import com.gcgenome.lims.service.LocalDateTimeToEpoch;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(schema="panel", name = "snv_comment")
@Data
@Accessors(fluent = true)
public class SnvComment {
	@EmbeddedId
	private SnvCommentPK pk;
	@Column(name="snv", insertable=false, updatable=false)
	private String snv;
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
	@Column
	private String comment;

	@Embeddable
	@Data
	@Accessors(fluent = true)
	public static class SnvCommentPK implements Serializable {
		@Column(name="snv", nullable=false, updatable=false)
		private String snv;
		@Column(name="create_at", nullable=false, updatable=false)
		private long createTime = LocalDateTimeToEpoch.map(LocalDateTime.now());
	}
}
