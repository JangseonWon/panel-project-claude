package com.gcgenome.lims.client;

import com.gcgenome.lims.client.expand.*;
import com.gcgenome.lims.client.expand.tmp.BallondorExpandElement;
import com.gcgenome.lims.client.expand.tmp.N159ExpandElement;
import com.gcgenome.lims.client.expand.tmp.S051ExpandElement;
import com.gcgenome.lims.test.geneplus.TestInfo;
import com.gcgenome.lims.test.tmp.N159;
import com.gcgenome.lims.test.tmp.S051;
import elemental2.dom.DomGlobal;
import jsinterop.base.JsPropertyMap;
import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.HashSet;

@UtilityClass
public class ExpandElementFactory {
	public ExpandElement<?> create(String id, long sample, JsPropertyMap<?> service) {
		DomGlobal.console.log(service);
		String code = (String) service.get("code");
		if(com.gcgenome.lims.test.panel.TestInfo.ON001.code().equalsIgnoreCase(code) || com.gcgenome.lims.test.panel.TestInfo.ON040.code().equalsIgnoreCase(code)) return CancerEnUsExpandElement.build(id, sample, code);
		if(Arrays.stream(com.gcgenome.lims.test.panel.TestInfo.TESTS).filter(t->t.category()!=com.gcgenome.lims.test.panel.TestInfo.Category.CANCER).anyMatch(t->t.code().equalsIgnoreCase(code))) 		return PanelExpandElement.build(id, sample, code);
		if(Arrays.stream(com.gcgenome.lims.test.panel.TestInfo.TESTS).filter(t->t.category()==com.gcgenome.lims.test.panel.TestInfo.Category.CANCER).anyMatch(t->t.code().equalsIgnoreCase(code))) 		return CancerExpandElement.build(id, sample, code);
		if(Arrays.stream(com.gcgenome.lims.test.single.TestInfo.TESTS).filter(t->t.category()==com.gcgenome.lims.test.single.TestInfo.Category.CANCER || t.category()== com.gcgenome.lims.test.single.TestInfo.Category.CANCER_WITH_RD_REPORT).anyMatch(t->t.code().equalsIgnoreCase(code))) {
			if("S061".equalsIgnoreCase(code)) return SingleExpandElement.buildWithBenign(id, sample, code);
			if("S062".equalsIgnoreCase(code)) return SingleExpandElement.buildWithBenign(id, sample, code);
			if("S063".equalsIgnoreCase(code)) return SingleExpandElement.buildWithBenign(id, sample, code);
			return BrcaExpandElement.build(id, sample, code);
		}
		if(Arrays.stream(com.gcgenome.lims.test.single.TestInfo.TESTS).filter(t->t.category()==com.gcgenome.lims.test.single.TestInfo.Category.BRCA).anyMatch(t->t.code().equalsIgnoreCase(code))) {
			return BrcaExpandElement.build(id, sample, code);
		}
		if(Arrays.stream(com.gcgenome.lims.test.single.TestInfo.TESTS).filter(t->t.category() != com.gcgenome.lims.test.single.TestInfo.Category.WITH_MLPA).anyMatch(t->t.code().equalsIgnoreCase(code))) {
			if("S065".equalsIgnoreCase(code)) return SingleExpandElement.buildWithBenign(id, sample, code);
			else return SingleExpandElement.build(id, sample, code);
		}
		if(Arrays.stream(com.gcgenome.lims.test.geneplus.TestInfo.TESTS_ETC).anyMatch(t->t.code().equalsIgnoreCase(code)))	return GenePlusExpandElement.build(id, sample, code);
		if(Arrays.stream(com.gcgenome.lims.test.geneplus.TestInfo.TESTS_BRCA).anyMatch(t->t.code().equalsIgnoreCase(code)))	return GenePlusExpandElement.build(id, sample, code);
		if(Arrays.stream(com.gcgenome.lims.test.wes.TestInfo.TESTS).anyMatch(t->t.code().equalsIgnoreCase(code))) 			return WesExpandElement.build(id, sample, service);
		if(Arrays.stream(com.gcgenome.lims.test.wes.TestWithSingleInfo.TESTS).anyMatch(t->t.code().equalsIgnoreCase(code)))	return WesWithSingleGeneExpandElement.build(id, sample, code);
		if(Arrays.stream(com.gcgenome.lims.test.dgs.TestInfo.TESTS).anyMatch(t->t.code().equalsIgnoreCase(code)))			return WesExpandElement.build(id, sample, service);
		if(Arrays.stream(TestInfo.TESTS_BRCA).anyMatch(t->t.code().equalsIgnoreCase(code)))									return GenomeScreenExpandElement.build(id, sample, code, new HashSet<>(Arrays.asList("P", "LP", "PV", "LPV", "VUS", "LBV", "BV")));
		if(Arrays.stream(com.gcgenome.lims.test.genomescreen.TestInfo.TESTS).anyMatch(t->t.code().equalsIgnoreCase(code)))	return GenomeScreenExpandElement.build(id, sample, code, new HashSet<>(Arrays.asList("P", "LP", "PV", "LPV")));
		if(Arrays.stream(com.gcgenome.lims.test.hrd.TestInfo.TESTS).anyMatch(t->t.code().equalsIgnoreCase(code)))			return HrdExpandElement.build(id, sample, code);
		if(Arrays.stream(com.gcgenome.lims.test.sanger.TestInfo.TESTS).anyMatch(t->t.code().equalsIgnoreCase(code)))			return SangerExpandElement.build(id, sample, code);
		if(N159.builder().build().code().equalsIgnoreCase(code)) return N159ExpandElement.build(id, sample, code);
		if(S051.instance.code().equalsIgnoreCase(code))			return S051ExpandElement.build(id, sample, code);
		if(Arrays.stream(com.gcgenome.lims.test.tmp.Ballondor.TESTS).anyMatch(t->t.code().equalsIgnoreCase(code)))			return BallondorExpandElement.build(id, sample, code);
		return DefaultExpandElement.build(sample, code);
	}
}
