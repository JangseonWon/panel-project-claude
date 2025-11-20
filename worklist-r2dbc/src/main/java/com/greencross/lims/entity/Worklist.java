package com.greencross.lims.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("worklist")
@RequiredArgsConstructor
public class Worklist {
	@Id
	private final WorklistPK pk;
//	@ForeignKey(optional = false)
//	private Sheet sheet;
	@Column("worklist")
	private Integer worklistId;


	public enum WorklistState {
		CREATE, CLOSE, CANCEL
	}
}
