package com.greencross.lims.worker;

import com.gcgenome.lims.test.HasGenes;
import com.gcgenome.lims.test.genomescreen.TestInfo;
import com.gcgenome.lims.test.wes.TestWithSingleInfo;
import lombok.experimental.UtilityClass;

import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class TestUtil {
	private Map<String, HasGenes> tests = new HashMap<>();
	private void initialze(Map<String, HasGenes> tests) {
		tests.clear();
		for(com.gcgenome.lims.test.panel.TestInfo t: com.gcgenome.lims.test.panel.TestInfo.TESTS) tests.put(t.code(), t);
		for(com.gcgenome.lims.test.bloodcancer.TestInfo t: com.gcgenome.lims.test.bloodcancer.TestInfo.TESTS) tests.put(t.code(), t);
		for(com.gcgenome.lims.test.single.TestInfo t: com.gcgenome.lims.test.single.TestInfo.TESTS) tests.put(t.code(), t);
		for(TestWithSingleInfo t: TestWithSingleInfo.TESTS) tests.put(t.code(), t.single());
		for(com.gcgenome.lims.test.geneplus.TestInfo t: com.gcgenome.lims.test.geneplus.TestInfo.TESTS_BRCA) tests.put(t.code(), t);
		for(com.gcgenome.lims.test.geneplus.TestInfo t: com.gcgenome.lims.test.geneplus.TestInfo.TESTS_ETC) tests.put(t.code(), t);
		for(TestInfo t: TestInfo.TESTS) tests.put(t.code(), t);
	}
	public HasGenes findTest(String code) {
		if(tests.isEmpty()) initialze(tests);
		if(tests.containsKey(code)) return tests.get(code);
		return null;
	}
}
