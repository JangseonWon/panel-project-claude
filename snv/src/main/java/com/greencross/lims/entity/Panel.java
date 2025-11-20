package com.greencross.lims.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(schema="panel", name = "panel")
@Data
@Accessors(fluent = true)
public class Panel {
	@Id
	private String id;
	private String name;
	@ElementCollection
	@CollectionTable(schema="panel", name = "panel_genes", joinColumns = @JoinColumn(name = "panel"), indexes = {@Index(columnList="panel")})
	private List<Gene> genes;

	public List<Gene> genes() {
		if(genes == null) return List.of();
		else return genes;
	}
}
