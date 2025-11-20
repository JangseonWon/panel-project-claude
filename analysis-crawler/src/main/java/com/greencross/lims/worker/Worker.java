package com.greencross.lims.worker;

import com.greencross.lims.entity.AnalysisFile;

import java.io.File;

public interface Worker {
	String name();
	boolean chk(File file);
	String batch(File file);
	void process(AnalysisFile entity, File file) throws Exception;
}
