package com.gcgenome.lims.test;

public interface HasGene extends HasGenes {
	String gene();
	default String[] genes() {
		return new String[] { gene() };
	}
}
