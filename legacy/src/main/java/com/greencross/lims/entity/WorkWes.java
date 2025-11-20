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
@DiscriminatorValue("0e0d4be9-b829-42e3-8525-8fdb70c55953")
@Data
@ToString(callSuper=true)
@EqualsAndHashCode(callSuper=true)
@Accessors(fluent = true)
public class WorkWes extends Work {
	@Embeddable
	public static class WorkWesPK extends WorkPK {
		public WorkWesPK() {}
		public WorkWesPK(int worklist, long sample, String service) {
			super(UUID.fromString("0e0d4be9-b829-42e3-8525-8fdb70c55953"), worklist, sample, service);
		}
	}
}
