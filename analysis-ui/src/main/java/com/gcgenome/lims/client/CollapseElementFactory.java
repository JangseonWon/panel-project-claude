package com.gcgenome.lims.client;

import com.gcgenome.lims.client.collapse.*;
import lombok.experimental.UtilityClass;

import java.util.Arrays;

@UtilityClass
public class CollapseElementFactory {
	public CollapseElement<?> create(String id, long sample, String service) {
		if(Arrays.stream(com.gcgenome.lims.test.panel.TestInfo.TESTS).anyMatch(t->t.code().equalsIgnoreCase(service)))		return PanelCollapseElement.build(id, sample, service);
		if(Arrays.stream(com.gcgenome.lims.test.single.TestInfo.TESTS).anyMatch(t->t.code().equalsIgnoreCase(service)))		return SingleCollapseElement.build(id, sample, service);
		if(Arrays.stream(com.gcgenome.lims.test.geneplus.TestInfo.TESTS_ETC).anyMatch(t->t.code().equalsIgnoreCase(service)))	return GenePlusCollapseElement.build(id, sample, service);
		if(Arrays.stream(com.gcgenome.lims.test.geneplus.TestInfo.TESTS_BRCA).anyMatch(t->t.code().equalsIgnoreCase(service)))return GenePlusCollapseElement.build(id, sample, service);
		if(Arrays.stream(com.gcgenome.lims.test.wes.TestInfo.TESTS).anyMatch(t->t.code().equalsIgnoreCase(service)))			return WesCollapseElement.build(id, sample, service);
		if(Arrays.stream(com.gcgenome.lims.test.wes.TestWithSingleInfo.TESTS).anyMatch(t->t.code().equalsIgnoreCase(service)))return WesCollapseElement.build(id, sample, service);
		if(Arrays.stream(com.gcgenome.lims.test.dgs.TestInfo.TESTS).anyMatch(t->t.code().equalsIgnoreCase(service)))			return DgsCollapseElement.build(id, sample, service);
		//if(Arrays.stream(com.gcgenome.lims.test.bloodcancer.TestInfo.TESTS).anyMatch(t->t.code().equalsIgnoreCase(service)))	return BloodCancerCollapseElement.build(id, sample, service);
		if(Arrays.stream(com.gcgenome.lims.test.solidtumor2.TestInfo.TESTS).anyMatch(t->t.code().equalsIgnoreCase(service)))	return SolidTumor2CollapseElement.build(id, sample, service);
		if(Arrays.stream(com.gcgenome.lims.test.genomescreen.TestInfo.TESTS).anyMatch(t->t.code().equalsIgnoreCase(service)))	return GenomeScreenCollapseElement.build(id, sample, service);
		//if(Arrays.stream(com.gcgenome.lims.test.hrd.TestInfo.TESTS).anyMatch(t->t.code().equalsIgnoreCase(service)))			return HrdCollapseElement.build(id, sample, service);
		//if(Arrays.stream(com.gcgenome.lims.test.hrd.TestInfo.TESTS).anyMatch(t->t.code().equalsIgnoreCase(service)))			return HrdCollapseElement.build(id, sample, service);
		//if(Arrays.stream(com.gcgenome.lims.test.mrd.TestInfo.SCREEN_TESTS).anyMatch(t->t.code().equalsIgnoreCase(service)))	return MrdScreenCollapseElement.build(id, sample, service);
		//if(Arrays.stream(com.gcgenome.lims.test.mrd.TestInfo.TESTS).anyMatch(t->t.code().equalsIgnoreCase(service)))			return MrdCollapseElement.build(sample, service);
		return DefaultCollapseElement.build(sample, service);
	}
}
