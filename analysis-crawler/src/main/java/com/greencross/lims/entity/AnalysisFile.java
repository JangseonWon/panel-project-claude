package com.greencross.lims.entity;

import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(schema="panel",name = "analysis_file")
@Data
@Accessors(fluent = true)
public class AnalysisFile {
	@Id
	@Column
	private UUID id;
	@LastModifiedDate
	@Column(name="last_modify_at")
	private LocalDateTime lastModifyAt;
	@CreatedDate
	@Column(name="create_at", nullable = false, updatable = false)
	private LocalDateTime createAt;
	@Column(name="process_at")
	private LocalDateTime processAt;
	private String path;
	private String name;
	private long size;
}
