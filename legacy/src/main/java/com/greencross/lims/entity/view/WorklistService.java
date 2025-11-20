package com.greencross.lims.entity.view;

import com.greencross.lims.entity.ServiceGroup;
import com.greencross.lims.entity.Worklist;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Immutable
@Subselect("SELECT DISTINCT w.sheet, w.worklist, s.id, s.name, s.group FROM service s, work w WHERE w.service=s.id")
@Entity
@Getter
@Accessors(fluent = true)
public class WorklistService {
	@EmbeddedId
	private WorklistServiceGroupPK pk;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns({
			@JoinColumn(name="sheet", referencedColumnName="sheet", insertable=false, updatable=false)
			, @JoinColumn(name="worklist", referencedColumnName="worklist", insertable=false, updatable=false)})
	private Worklist worklist;
	@Column(name="id", length=8, insertable=false, updatable=false)
	private String id;
	@Column(name="name", length=64)
	private String name;
	@ManyToOne
	@JoinColumn(name="\"group\"", referencedColumnName="id")
	private ServiceGroup group;
	@Embeddable
	@Data
	@Accessors(fluent = true)
	public static class WorklistServiceGroupPK implements Serializable {
		@Column(name="sheet", nullable=false, updatable=false)
		private UUID sheet;
		@Column(name="worklist", nullable=false, updatable=false)
		private Integer worklist;
		@Column(name="id", nullable=false, updatable=false)
		private String id;
	}
}
