package com.gcgenome.lims.client;

import com.gcgenome.lims.client.collapse.*;
import com.gcgenome.lims.client.collapse.tmp.BallondorCollapseElement;
import com.gcgenome.lims.client.collapse.tmp.N159CollapseElement;
import com.gcgenome.lims.client.collapse.tmp.S051CollapseElement;
import com.gcgenome.lims.test.tmp.N159;
import com.gcgenome.lims.test.tmp.S051;
import lombok.experimental.UtilityClass;

import java.util.Arrays;

@UtilityClass
public class CollapseElementFactory {
	public CollapseElement<?> create(String id, long sample, String service) {
		if(com.gcgenome.lims.test.panel.TestInfo.ON001.code().equalsIgnoreCase(service)
		|| com.gcgenome.lims.test.panel.TestInfo.ON040.code().equalsIgnoreCase(service))	return PanelEnUsCollapseElement.build(id, sample, service);
		if(Arrays.stream(com.gcgenome.lims.test.panel.TestInfo.TESTS).anyMatch(t->t.code().equalsIgnoreCase(service)))		return PanelCollapseElement.build(id, sample, service);
		if(Arrays.stream(com.gcgenome.lims.test.single.TestInfo.TESTS).filter(t->t.category() != com.gcgenome.lims.test.single.TestInfo.Category.WITH_MLPA).anyMatch(t->t.code().equalsIgnoreCase(service)))		return SingleCollapseElement.build(id, sample, service);
		if(Arrays.stream(com.gcgenome.lims.test.geneplus.TestInfo.TESTS_ETC).anyMatch(t->t.code().equalsIgnoreCase(service)))	return GenePlusCollapseElement.build(id, sample, service);
		if(Arrays.stream(com.gcgenome.lims.test.geneplus.TestInfo.TESTS_BRCA).anyMatch(t->t.code().equalsIgnoreCase(service)))return GenePlusCollapseElement.build(id, sample, service);
		if(Arrays.stream(com.gcgenome.lims.test.wes.TestInfo.TESTS).anyMatch(t->t.code().equalsIgnoreCase(service)))			return WesCollapseElement.build(id, sample, service);
		if(Arrays.stream(com.gcgenome.lims.test.wes.TestWithSingleInfo.TESTS).anyMatch(t->t.code().equalsIgnoreCase(service)))return WesCollapseElement.build(id, sample, service);
		if(Arrays.stream(com.gcgenome.lims.test.dgs.TestInfo.TESTS).anyMatch(t->t.code().equalsIgnoreCase(service)))			return DgsCollapseElement.build(id, sample, service);
		if(Arrays.stream(com.gcgenome.lims.test.genomescreen.TestInfo.TESTS).anyMatch(t->t.code().equalsIgnoreCase(service)))	return GenomeScreenCollapseElement.build(id, sample, service);
		if(Arrays.stream(com.gcgenome.lims.test.hrd.TestInfo.TESTS).anyMatch(t->t.code().equalsIgnoreCase(service)))			return HrdCollapseElement.build(id, sample, service);
		if(Arrays.stream(com.gcgenome.lims.test.sanger.TestInfo.TESTS).anyMatch(t->t.code().equalsIgnoreCase(service)))			return SangerCollapseElement.build(id, sample, service);

		if(N159.builder().build().code().equalsIgnoreCase(service))		return N159CollapseElement.build(id, sample, service);
		if(S051.instance.code().equalsIgnoreCase(service))				return S051CollapseElement.build(id, sample, service);
		if(Arrays.stream(com.gcgenome.lims.test.tmp.Ballondor.TESTS).anyMatch(t->t.code().equalsIgnoreCase(service)))				return BallondorCollapseElement.build(id, sample, service);
		return DefaultCollapseElement.build(sample, service);
	}
}
