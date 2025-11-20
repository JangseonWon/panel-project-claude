package com.greencross.lims.entity;


import lombok.Getter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

@Entity
@Immutable
@Subselect("select w.sheet, w.work, w.create_time, u.name as user, w.title, w.state, w.value,\n" +
				   "       r.serial, r.sample, s.patient, p.name as patient_name\n" +
				   "from worklist w, \"user\" u, recept r, sample s, patient p\n" +
				   "where w.sheet='5fd630a8-f9fc-4a17-872a-5b3e82929d6a' and " +
				   "w.user=u.id and\n" +
				   "        w.work=r.work and r.sheet=w.sheet and\n" +
				   "        r.sample=s.id and s.patient=p.id")
@Getter
@Accessors(fluent = true)
public class WorklistRareDiseaseView {
	@EmbeddedId
	private com.greencross.lims.entity.Worklist.WorklistPK pk;
	@Column(name="work", columnDefinition="integer", insertable=false, updatable=false)
	private Integer work;
	@Column(name="create_time")
	private LocalDate createTime;
	@Column(name="user")
	private String user;
	@Column(name="title")
	private String title;
	@Column(name="state")
	private String state;
	@Column(name="value", columnDefinition="jsonb")
	@Type(type="com.greencross.lims.entity.udt.MapConverter")
	private Map<UUID, String> value;
	@Column(name="serial")
	private String serial;
	@Column(name="sample")
	private Long sample;
	@Column(name="patient")
	private String patient;
	@Column(name="patient_name")
	private String patientName;
}
