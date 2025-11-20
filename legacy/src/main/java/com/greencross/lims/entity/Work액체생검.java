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
@DiscriminatorValue("95df9a03-3248-4298-87be-40846585c331")
@Data
@ToString(callSuper=true)
@EqualsAndHashCode(callSuper=true)
@Accessors(fluent = true)
public class Work액체생검 extends Work {
	@Embeddable
	public static class WorkRareDiseasePK extends WorkPK {
		public WorkRareDiseasePK() {}
		public WorkRareDiseasePK(int worklist, long sample, String service) {
			super(UUID.fromString("95df9a03-3248-4298-87be-40846585c331"), worklist, sample, service);
		}
	}
}
