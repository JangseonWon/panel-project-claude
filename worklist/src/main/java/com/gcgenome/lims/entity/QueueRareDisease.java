package com.gcgenome.lims.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import java.util.UUID;

@Entity
@DiscriminatorValue("QUEUE-5fd630a8-f9fc-4a17-872a-5b3e82929d6a")
@Data
@ToString(callSuper=true)
@EqualsAndHashCode(callSuper=true)
@Accessors(fluent = true)
public class QueueRareDisease extends WorkRedo {
	@Embeddable
	public static class QueueRareDiseasePK extends QueueItemPK {
		public QueueRareDiseasePK() {}
		public QueueRareDiseasePK(String serial) {
			super(UUID.fromString("5fd630a8-f9fc-4a17-872a-5b3e82929d6a"), serial);
		}
	}
}
