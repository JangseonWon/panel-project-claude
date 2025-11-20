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
@DiscriminatorValue("5fd630a8-f9fc-4a17-872a-5b3e82929d6a")
@Data
@ToString(callSuper=true)
@EqualsAndHashCode(callSuper=true)
@Accessors(fluent = true)
public final class WorklistRareDisease extends Worklist {
	public WorklistRareDisease worklist(Integer worklist) {
		assert worklist != null;
		if(pk() == null) pk(new WorklistRareDiseasePK(worklist));
		super.worklist(worklist);
		return this;
	}
	@Embeddable
	public static class WorklistRareDiseasePK extends WorklistPK {
		public WorklistRareDiseasePK() {}
		public WorklistRareDiseasePK(Integer work) {
			super(UUID.fromString("5fd630a8-f9fc-4a17-872a-5b3e82929d6a"), work);
		}
	}
}
