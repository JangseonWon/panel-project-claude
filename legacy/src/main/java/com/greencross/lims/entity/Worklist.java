package com.greencross.lims.entity;

import com.greencross.lims.entity.view.WorklistService;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DiscriminatorFormula;
import org.hibernate.annotations.Formula;
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
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorFormula("sheet")
@Table(name = "worklist", indexes = {
		@Index(columnList="\"user\"")
})
@Data
@Accessors(fluent = true)
public class  Worklist implements Serializable {
	@EmbeddedId
	private WorklistPK pk;
	@Column(name="worklist", columnDefinition="integer", insertable=false, updatable=false)
	private Integer worklist;
	@CreatedDate
	@Column(name="create_time", nullable = false, updatable = false)
	private LocalDateTime createTime = LocalDateTime.now();
	@LastModifiedDate
	@Column(name="last_modify_at")
	private LocalDateTime lastModifyAt;
	@LastModifiedBy
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="last_modify_user")
	private User<?> lastModifiedBy;
	@CreatedBy
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="\"user\"", referencedColumnName="id")
	private User<?> user;
	@Column(name="title", length=128)
	private String title;
	@Column(name="state")
	@Enumerated(EnumType.STRING)
	private WorklistState state = WorklistState.CREATE;
	@Column(name="value", columnDefinition="jsonb")
	@Type(type="com.greencross.lims.entity.udt.MapConverter")
	private Map<UUID, String> value;
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "worklist")
	private List<Work> recepts;
	@Formula("(SELECT count(*) FROM work w WHERE w.sheet=sheet AND w.worklist=worklist)")
	private long workCnt;
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "worklist")
	private List<WorklistService> services;
	public Worklist worklist(Integer worklist) {
		assert worklist != null;
		if(pk == null) pk = new WorklistPK();
		pk.worklist(worklist);
		this.worklist = worklist;
		return this;
	}
	public List<Service> services() {
		if(this.services == null) return List.of();
		return services.stream().map(s->new Service().id(s.id()).name(s.name()).group(s.group())).collect(Collectors.toList());
	}
	@Embeddable
	@Data
	@Accessors(fluent = true)
	public static class WorklistPK implements Serializable {
		@Column(name="sheet", nullable=false, updatable=false)
		private UUID sheet;
		@Column(name="worklist", columnDefinition="integer", nullable=false, updatable=false)
		private Integer worklist;
		public WorklistPK() {}
		@Builder
		public WorklistPK(UUID sheet, Integer worklist) {
			this.worklist = worklist;
			this.sheet = sheet;
		}
	}

	public enum WorklistState {
		CREATE, CLOSE, CANCEL
	}
}
