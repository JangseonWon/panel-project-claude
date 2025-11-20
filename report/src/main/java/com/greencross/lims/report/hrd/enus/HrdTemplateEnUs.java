package com.greencross.lims.report.hrd.enus;

import com.gcgenome.lims.test.hrd.TestInfo;
import com.greencross.lims.report.builder.LogoType;
import com.greencross.lims.report.builder.Sex;
import com.greencross.lims.report.hrd.HrdDto;
import com.greencross.lims.report.hrd.HrdResource;
import com.greencross.lims.report.hrd.HrdTemplate;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@Accessors(fluent = true)
public class HrdTemplateEnUs implements HrdTemplate {
	private static final DateTimeFormatter DTF 			= DateTimeFormatter.ofPattern("yyyy-MM-dd");
	private final String lblMedicalInstitution			= "";
	private final String lblMedicalRecordNumber			= "";
	private final String lblRequestNumber				= "";
	private final String lblPatientName					= "";
	private final String lblPatientCode					= "";
	private final String lblAgeSex						= "";
	private final String lblSpecimenType				= "";
	private final String lblWardDepartment				= "";
	private final String lblCollectionDate				= "";
	private final String lblPatientInfo					= "";
	private final String lblPhysician					= "";
	private final String lblReceiptReportDate			= "";

	private final String lblSummary                     = "OVERALL SUMMARY";
	private final String lblHrd                         = "HRD";
	private final String lblBrca                        = "BRCA Mutations";

	private final String lblQc						    = "QUALITY CONTROL";
	private final String lblDetails						= "TEST RESULT";
	private final String lblGi		    				= "Genomic Instability";
	private final String lblGiScore						= "Genomic Instability Score";
	private final String lblLohTitle					= "Loss of Heterozygosity";
	private final String lblTaiTitle					= "Telomeric Allelic Imbalance";
	private final String lblLstTitle					= "Large Scale Transition";
	private final String lblLoh							= "Loss of one normal copy of\ngene or a group of genes";
	private final String lblTai							= "Telomeric region located at each\nend of a chromosome has copy\nnumber other than 2.";
	private final String lblLst							= "Multiple large scale\nchromosomal breaks";

	public String tmplResultByTier(String tier) {
		if("BRCA".equals(tier)) return "BRCA Mutation";
		else if("TIER1".equals(tier)) return "Tier 1";
		else if("TIER2".equals(tier)) return "Tier 2";
		else throw new RuntimeException();
	}
	private final String lblTestInfoTitle				= "TEST PERFORMED";
	private final String lblTestInfo    				= "This assay uses next generation sequencing (NGS) technology to detect homologous recombination deficiency based on analysis of genomic instability and BRCA1/BRCA2 gene mutation";
	private final String lblHrdInfoTitle				= "1. Understanding Homologous Recombination Deficiency (HRD)";
	private final String lblHrdInfo    			    	= "What is Homologous recombination deficiency (HRD)?\n" +
															"Normally, when DNA damage occurs in a cell, the damage is repaired through a DNA repair process. However, in tumor cells, the DNA damage is not properly repaired and the cell continues to divide. A case in which DNA repair process does not occur due to problem in homologous recombination function or mutation in BRCA gene is referred to as Homologous Recombination Deficiency (HRD). Tumors with HRD are particularly susceptible to specific targeted cancer therapies such as the PARP inhibitors.";
	private final String lblParpInfoTitle				= "2. PARP inhibitor";
	private final String lblParpInfo    				= "PARP inhibitors are most notable anticancer drugs used in ovarian cancer. They suppress the activity of PARP protein, which is responsible for DNA repair process. In normal cells with proper DNA repair process, PARP inhibitor poses no lethality. But in tumor cells with non-functioning repair process, it poses lethal effects. PARP inhibitor shows unique effectiveness in tumors with homologous recombination deficiency such as BRCA1/BRCA2 mutation.";
	private final String lblTestLimitation				= "3. LIMITATION";
	private final String[] lblLimitations				= new String[]{
			"This assay uses sequencing analysis to detects mutation and genomic instability of BRCA1 and BRCA2 genes, and cannot detect mutations in other genes or regions not covered by this test.",
			"The limit of detection (LOD) for SNV and small indel in BRCA1, BRCA2 genes is approximately 20%.",
			"Certain target regions may have lower coverage.",
			"This test does not distinguish between germline and somatic variants. If the variant allele frequency of the mutation is close to 50% or 100%, the possibility of germline variant cannot be eliminated."
	};
	private final String lblTestReferences				= "4. REFERENCES";
	private final String[] lblReferences				= new String[]{
			"Br J Cancer. 2018 Nov;119(11):1401-1409.",
			"Mol Cancer Res. 2018 Jul;16(7):1103-1111.",
			"N Engl J Med. 2019 Dec 19;381(25):2391-2402.",
			"N Engl J Med. 2019 Dec 19;381(25):2416-2428."
	};
	private final String lblTestGeneInfo				= "5. GENE INFORMATION";
	private final String lblGeneEssential = "Essential Gene";
	private final String lblGeneAdditional = "Additional Gene";
	private final HrdResource resource;
	private final LogoType logoType;
	private final TestInfo testInfo;
	@Override
	public String resultToString(HrdDto.Result result) {
		switch (result) {
			case P: return "Positive";
			case N: return "Negative";
			default: return "-";
		}
	}
	public HrdTemplateEnUs(HrdResource resource, TestInfo testInfo, LogoType logoType) {
		this.resource = resource;
		this.logoType = logoType;
		this.testInfo = testInfo;
	}
	public final HrdResource resource() {
		return resource;
	}

	@Override
	public String date(LocalDate date) {
		if(date == null) return null;
		return DTF.format(date);
	}
	@Override
	public String date(LocalDateTime date) {
		if(date == null) return null;
		return DTF.format(date);
	}
	@Override
	public final String sex(Sex sex) {
		if(sex == null) return "-";
		switch(sex) {
			case M: return "Male";
			case F: return "Female";
			default: return "-";
		}
	}
	public final LogoType logoType() {
		return this.logoType;
	}
}
