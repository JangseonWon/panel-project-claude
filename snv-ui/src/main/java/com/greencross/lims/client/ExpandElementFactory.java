package com.greencross.lims.client;

import com.greencross.lims.client.expand.*;
import lombok.experimental.UtilityClass;

import java.util.Arrays;

@UtilityClass
public class ExpandElementFactory {
	public ExpandElement<?> create(String id, long sample, String service) {
		if(Arrays.stream(com.greencross.lims.test.panel.TestInfo.TESTS).anyMatch(t->t.code().equalsIgnoreCase(service))) 				return PanelExpandElement.build(id, sample, service);
		if(Arrays.stream(com.greencross.lims.test.single.TestInfo.TESTS).anyMatch(t->t.code().equalsIgnoreCase(service)))				return SingleGeneExpandElement.build(id, sample, service);
		if(Arrays.stream(com.greencross.lims.test.geneplus.TestInfo.TESTS_ETC).anyMatch(t->t.code().equalsIgnoreCase(service)))			return GenePlusExpandElement.build(id, sample, service);
		if(Arrays.stream(com.greencross.lims.test.geneplus.TestInfo.TESTS_BRCA).anyMatch(t->t.code().equalsIgnoreCase(service)))		return GenePlusExpandElement.build(id, sample, service);
		if(Arrays.stream(com.greencross.lims.test.des.TestInfo.TESTS).anyMatch(t->t.code().equalsIgnoreCase(service)))					return DesExpandElement.build(id, sample, service);
		if(Arrays.stream(com.greencross.lims.test.dgs.TestInfo.TESTS).anyMatch(t->t.code().equalsIgnoreCase(service)))					return DgsExpandElement.build(id, sample, service);
		if(Arrays.stream(com.greencross.lims.test.wes.TestInfo.TESTS).anyMatch(t->t.code().equalsIgnoreCase(service)))					return DgsExpandElement.build(id, sample, service);
		if(Arrays.stream(com.greencross.lims.test.wes.TestWithSingleInfo.TESTS).anyMatch(t->t.code().equalsIgnoreCase(service)))		return DgsExpandElement.build(id, sample, service);
		if(Arrays.stream(com.greencross.lims.test.genomescreen.TestInfo.TESTS_BRCA).anyMatch(t->t.code().equalsIgnoreCase(service)))	return GenomeScreenExpandElement.build(id, sample, service);
		if(Arrays.stream(com.greencross.lims.test.genomescreen.TestInfo.TESTS_CANCER).anyMatch(t->t.code().equalsIgnoreCase(service)))	return GenomeScreenExpandElement.build(id, sample, service);
		if(Arrays.stream(com.greencross.lims.test.genomescreen.TestInfo.TESTS_RD).anyMatch(t->t.code().equalsIgnoreCase(service)))		return GenomeScreenExpandElement.build(id, sample, service);
		//if(Arrays.stream(com.greencross.lims.test.dgs.TestInfo.TESTS).anyMatch(t->t.code().equalsIgnoreCase(service)))			return DgsCollapseElement.build(sample, service);
		//if(Arrays.stream(com.greencross.lims.test.bloodcancer.TestInfo.TESTS).anyMatch(t->t.code().equalsIgnoreCase(service)))	return BloodCancerCollapseElement.build(sample, service);
		//if(Arrays.stream(com.greencross.lims.test.hrd.TestInfo.TESTS).anyMatch(t->t.code().equalsIgnoreCase(service)))			return HrdCollapseElement.build(sample, service);
		//if(Arrays.stream(com.greencross.lims.test.mrd.TestInfo.TESTS).anyMatch(t->t.code().equalsIgnoreCase(service)))			return MrdCollapseElement.build(sample, service);
		return DefaultExpandElement.build(sample, service);
	}
}
