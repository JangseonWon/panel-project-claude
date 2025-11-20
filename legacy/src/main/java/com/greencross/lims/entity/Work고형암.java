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
@DiscriminatorValue("7c5f6a86-3c57-45d0-a5f3-d85e0e127cdb")
@Data
@ToString(callSuper=true)
@EqualsAndHashCode(callSuper=true)
@Accessors(fluent = true)
public class Work고형암 extends Work {
	@Embeddable
	public static class Work고형암PK extends WorkPK {
		public Work고형암PK() {}
		public Work고형암PK(int worklist, long sample, String service) {
			super(UUID.fromString("7c5f6a86-3c57-45d0-a5f3-d85e0e127cdb"), worklist, sample, service);
		}
	}
}
