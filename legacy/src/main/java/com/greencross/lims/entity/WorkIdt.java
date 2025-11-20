package com.greencross.lims.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import java.util.UUID;

@Entity
@DiscriminatorValue("e4da9064-9edb-442b-ac34-44d7c1778529")
@Data
@ToString(callSuper=true)
@EqualsAndHashCode(callSuper=true)
@Accessors(fluent = true)
public class WorkIdt extends Work {
	@Embeddable
	public static class WorkIdtPK extends WorkPK {
		public WorkIdtPK() {}
		public WorkIdtPK(int worklist, long sample, String service) {
			super(UUID.fromString("e4da9064-9edb-442b-ac34-44d7c1778529"), worklist, sample, service);
		}
	}
}
