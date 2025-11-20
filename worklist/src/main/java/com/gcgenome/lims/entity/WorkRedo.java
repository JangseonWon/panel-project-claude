package com.gcgenome.lims.entity;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DiscriminatorFormula;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorFormula("concat(case when consumed then 'EX-' else 'QUEUE-' end, sheet::varchar)")
@Table(name = "queue", indexes = {
		@Index(columnList="sheet")
		, @Index(columnList="service")
		, @Index(columnList="user")
}) @Data
@Accessors(fluent = true)
public abstract class WorkRedo implements Serializable {
	@EmbeddedId
	private QueueItemPK pk;
	@Column(insertable=false, updatable=false)
	private String serial;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns({
		@JoinColumn(name="sample", referencedColumnName="sample"),
		@JoinColumn(name="service", referencedColumnName="service")})
	private Request request;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="sheet", referencedColumnName="id", insertable=false, updatable=false)
	private com.gcgenome.lims.entity.WorklistTemplate sheet;
	@ManyToOne
	@JoinColumn(name="\"user\"", referencedColumnName="id")
	private User<?> user;
	@Column(name="create_time")
	private LocalDateTime createTime = LocalDateTime.now();
	@Column(name="consumed")
	private boolean consumed = false;

	public WorkRedo sheet(WorklistTemplate work) {
		assert work != null;
		if(pk == null) pk = new QueueItemPK();
		pk.sheet(work.id());
		this.sheet = work;
		return this;
	}

	@Embeddable
	@Data
	@Accessors(fluent = true)
	public static class QueueItemPK implements Serializable {
		@Column(name="sheet", nullable=false, updatable=false)
		private UUID sheet;
		@Column(name="serial", length=8, nullable=false, updatable=false)
		private String serial;
		public QueueItemPK() {}

		@Builder
		public QueueItemPK(UUID sheet, String serial) {
			this.sheet = sheet;
			this.serial = serial;
		}
	}
}
