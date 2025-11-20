package com.greencross.lims.report;

import com.gcgenome.report.versions.cassandra.HasPdf;
import lombok.Data;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.nio.ByteBuffer;
import java.time.LocalDateTime;
import java.util.UUID;

@Table("file")
@Data
@Accessors(fluent = true)
public class ReportFile implements HasPdf {
	@PrimaryKey
	@Column
	private UUID id = UUID.randomUUID();
	@Column("create_time")
	private LocalDateTime createTime = LocalDateTime.now();
	@Column
	private String name;
	@Column
	private String extension;
	@Column
	private Long size;
	@Column
	private Long sample;
	@Column
	private String service;
	@Column("bytes")
	private ByteBuffer data;

	@NotNull
	@Override
	public byte[] getPdf() {
		return data.array();
	}
}
