package com.greencross.lims.service.report.genomescreen;

public interface GenomeScreenTemplate {
	interface Disease {
		DiseaseSub[] subs();
	}
	interface DiseaseSub {
		Gene[] genes();
	}
	interface Gene {
		String name();
		String transcript();
		Tier tier();
	}
	enum Tier {
		Tier1, Tier2, Tier3
	}
}
