package com.greencross.lims.entity;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "sample", indexes = {
		@Index(columnList = "patient"),
		@Index(columnList = "barcode desc"),
		@Index(columnList = "remark")
})
@Data
@Accessors(fluent = true)
public class Sample {
	@Id
	@Column
	private long id;	// Sample Barcode ID
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "patient", referencedColumnName = "id")
	private Patient patient;
	@Column(name="sample_type", length=32, columnDefinition="varchar(32)")
	private String sampleType;
	@Column(name="sample_type2", length=32, columnDefinition="varchar(32)")
	private String sampleType2;
	@Column(name="sample_type3", length=32, columnDefinition="varchar(32)")
	private String sampleType3;
	@Column(name="remark", length=64, columnDefinition="varchar(64)")
	private String remark;
	@Column(name="barcode", columnDefinition="bigint")
	private Long barcode;
	@Column(name="id_lis")
	private Long idLis;

	@ToString.Exclude
	@OneToMany(fetch = FetchType.LAZY, mappedBy="sample")
	private List<Request> requests;
	@ToString.Exclude
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "sample")
	@Where(clause="sheet='471a0005-6fef-4bcd-88fd-60b37b52d94a'")
	private List<AnalysisDna> dna;
	@ToString.Exclude
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "sample")
	@Where(clause ="sheet='471a0005-6fef-4bcd-88fd-60b37b52d94a'")
	private List<AnalysisLibrary> library;
	@ToString.Exclude
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "sample")
	@Where(clause ="sheet='114cfcf5-2bdb-4c71-bfa2-97f54a45c1f2'")
	private List<AnalysisSequencing> sequencing;
}
