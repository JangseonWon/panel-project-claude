package com.greencross.lims;

import com.gcgenome.lims.report.func.Painter;
import com.gcgenome.lims.test.genomescreen.TestInfo;
import com.gcgenome.lims.test.genomescreen.TestWithRiskScreen;
import com.greencross.lims.report.SectionPage;
import com.greencross.lims.report.builder.LogoType;
import com.greencross.lims.report.builder.Sex;
import com.greencross.lims.report.genomescreen.*;
import com.greencross.lims.report.genomescreen.enus.*;
import com.greencross.lims.report.genomescreen.kokr.*;
import com.greencross.lims.report.kokr.SectionFooterGenome;
import com.greencross.lims.report.kokr.SectionFooterGenomeLabs;
import com.greencross.lims.report.kokr.SectionSign;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class GenomeScreenReportTest {
	public static void main(String[] args) throws IOException {
//		for(var info:TestInfo.TESTS) {
//			System.out.println(info);
//			PDDocument doc = build(null, info);
//			doc.save(new File(info.code() + ".pdf"));
//			Desktop.getDesktop().open(new File(info.code() + ".pdf"));
//		}
		PDDocument doc = build(null, TestInfo.TESTS[3]);
			doc.save(new File("ON090" + ".pdf"));
			Desktop.getDesktop().open(new File("ON090" + ".pdf"));
	}
	public static PDDocument build(Object obj, TestInfo test) throws IOException {
		LogoType type = LogoType.DEPENDENT;
		if(Objects.requireNonNull(test).isNationalInsuranceTest()) type = LogoType.DEPENDENT;
		else type = LogoType.ASSOCIATED;
		return builder(test, type, (GenomeScreenDto) new GenomeScreenWithRiskScreenDto().barcode("AA-4-493")).build();
	}
	private static GenomeScreenPageBuilder builder(TestInfo test, LogoType logoType, GenomeScreenDto dto) throws IOException {
		PDDocument doc = new PDDocument();
		Map<GenomeScreenTemplate.Gene, List<GenomeScreenDto.Variant>> values = new HashMap<>();
		Map<GenomeScreenTemplate.DiseaseSub, Map<GenomeScreenTemplate.Gene, Boolean>> diseases = new HashMap<>();
		GenomeScreenDto.Variant var = new GenomeScreenDto.Variant().gene("CDH1").dnaChange("c.713G>A").predictedAa("p.(Arg238His)").zygosity("Het").clazz("PV");
		dto.code(test.code())
				.interpretation("")
				.medicalInstitution("GC 지놈")
				.patientName("홍*순")
				.medicalRecordNumber("01028469")
				.collectionDate(LocalDate.of(2022, 4, 3))
				.requestNumber("20200704-941-1800")
				.birthDate(LocalDate.of(2000,7,3))
				.sex(Sex.F)
				.specimenType("Whole Blood")
				.patientInfo("-")
				.patientCode("-")
				.physician("-")
				.ward("-")
				.department("-")
				.receiptDate(LocalDate.of(2022,4,4))
				.reportDate(LocalDate.of(2022,4,10))
				.barcode("0020-096-2185549181");
		Painter<GenomeScreenTemplate<GenomeScreenResource>, GenomeScreenDto> sign = new SectionSign<>(65);
		Painter<GenomeScreenTemplate<GenomeScreenResource>, GenomeScreenDto> footer = new SectionFooterGenomeLabs<>();
		Painter<GenomeScreenTemplate<GenomeScreenResource>, GenomeScreenDto> page;
		if(TestWithRiskScreen.N087 == test || TestWithRiskScreen.J020 == test || TestWithRiskScreen.ON087 == test) {
			var resource = new GenomeScreenResourceN087KoKr(doc);
			var template = new GenomeScreenTemplateN087KoKr(resource, test);
			page = new SectionPage<>(547, 65, resource.fontDefault());
			for(var disease: template.diseases()) for(var sub: disease.subs()) {
				Map<GenomeScreenTemplate.Gene, Boolean> m = new HashMap<>();
				for(var gene: sub.genes()) {
					//if(RandomUtils.nextBoolean()) values.put(gene, List.of(var));
					m.put(gene, false);
				}
				diseases.put(sub, m);
			}
			var genotypes = new HashMap<RiskScreenTemplate.Snv, String>();
			genotypes.put(GenomeScreenTemplateN087.SnvRiskScreenN087.APOE_, "e2e2");
			genotypes.put(GenomeScreenTemplateN087.SnvRiskScreenN087.MTHFR_c677, "CC");
			genotypes.put(GenomeScreenTemplateN087.SnvRiskScreenN087.MTHFR_c1298, "AA");
			genotypes.put(GenomeScreenTemplateN087.SnvRiskScreenN087.NOTCH3_c1630, "CC");
			genotypes.put(GenomeScreenTemplateN087.SnvRiskScreenN087.RNF213_c14429, "GG");
			dto.summary("수검자의 뇌졸중 지놈 스크린 검사 결과\n" +
					"유전성 유전성 뇌졸중과 관련된 유전자에서\n" +
					"병원성 변이(PV)가 발견되지 않았습니다.");
			return new GenomeScreenN087(template, ((GenomeScreenWithRiskScreenDto)dto).variantsRiskScreen(genotypes).variants(values).diseases(diseases), logoType, sign, footer, page);
		} else if(TestWithRiskScreen.N088 == test || TestWithRiskScreen.J021 == test || TestWithRiskScreen.ON088 == test) {
			var resource = new GenomeScreenResourceN088KoKr(doc);
			var template = new GenomeScreenTemplateN088KoKr(resource, test);
			page = new SectionPage<>(547, 65, resource.fontDefault());
			for(var disease: template.diseases()) for(var sub: disease.subs()) {
				Map<GenomeScreenTemplate.Gene, Boolean> m = new HashMap<>();
				for(var gene: sub.genes()) {
					//if(RandomUtils.nextBoolean()) values.put(gene, List.of(var));
					m.put(gene, false);
				}
				diseases.put(sub, m);
			}
			var genotypes = new HashMap<RiskScreenTemplate.Snv, String>();
			genotypes.put(GenomeScreenTemplateN088.SnvRiskScreenN088.APOE_, "e3e3");
			genotypes.put(GenomeScreenTemplateN088.SnvRiskScreenN088.APOA5_553, "GG");
			genotypes.put(GenomeScreenTemplateN088.SnvRiskScreenN088.APOA5_56, "CC");
			genotypes.put(GenomeScreenTemplateN088.SnvRiskScreenN088.COQ2_c779_1022, "CC");
			dto.summary("수검자의 고지혈증 지놈 스크린 검사 결과\n" +
					"유전성 고지혈증과 관련된 유전자에서\n" +
					"병원성 변이(PV)가 발견되지 않았습니다.");
			return new GenomeScreenN088(template, ((GenomeScreenWithRiskScreenDto)dto).variantsRiskScreen(genotypes).variants(values).diseases(diseases), logoType, sign, footer, page);
		} else if(TestInfo.N089 == test || TestInfo.J019 == test || TestInfo.N075 == test || TestInfo.N112 == test) {
			var resource = new GenomeScreenResourceN089KoKr(test, doc);
			var template = new GenomeScreenTemplateN089KoKr(resource, test);
			page = new SectionPage<>(547, 65, resource.fontDefault());
			for(var disease: template.diseases()) for(var sub: disease.subs()) {
				Map<GenomeScreenTemplate.Gene, Boolean> m = new HashMap<>();
				for(var gene: sub.genes()) {
					//if(RandomUtils.nextBoolean()) values.put(gene, List.of(var));
					m.put(gene, false);
				}
				diseases.put(sub, m);
			}
			dto.summary("수검자의 심장 돌연사 지놈 스크린 검사 결과\n" +
					"유전성 심장 돌연사와 관련된 유전자에서\n" +
					"병원성 변이(PV)가 발견되지 않았습니다.");
			return new GenomeScreenN089(template, dto.variants(values).diseases(diseases), new SectionHeaderKoKr(), logoType, sign, footer, page, SectionGeneListKoKr::new);
		} else if(TestInfo.ON089 == test) {
			var resource = new GenomeScreenResourceN089EnUs(doc);
			var template = new GenomeScreenTemplateN089EnUs(resource, test);
			sign = new com.greencross.lims.report.enus.SectionSign<>(65);
			footer = new com.greencross.lims.report.enus.SectionFooterGenome<>();
			page = new SectionPage<>(565, 65, resource.fontDefault());
			for(var disease: template.diseases()) for(var sub: disease.subs()) {
				Map<GenomeScreenTemplate.Gene, Boolean> m = new HashMap<>();
				for(var gene: sub.genes()) {
					//if(RandomUtils.nextBoolean()) values.put(gene, List.of(var));
					m.put(gene, false);
				}
				diseases.put(sub, m);
			}
			dto.diseases(diseases);
			return new GenomeScreenN089(template, dto.variants(values), new SectionHeaderEnUs(), logoType, sign, footer, page, SectionGeneList::new);
		} else if(TestInfo.N185 == test) {
			var resource = new GenomeScreenResourceN089KoKr(test, doc);
			var template = new GenomeScreenTemplateN185KoKr(resource, test);
			page = new SectionPage<>(547, 65, resource.fontDefault());
			for(var disease: template.diseases()) for(var sub: disease.subs()) {
				Map<GenomeScreenTemplate.Gene, Boolean> m = new HashMap<>();
				for(var gene: sub.genes()) {
					//if(RandomUtils.nextBoolean()) values.put(gene, List.of(var));
					m.put(gene, false);
				}
				diseases.put(sub, m);
			}
			dto.diseases(diseases);
			return new GenomeScreenN185(template, dto.variants(values), new SectionHeaderKoKr(), logoType, sign, footer, page, SectionGeneListKoKr::new);
		} else if(TestInfo.N090 == test || TestInfo.J018 == test) {
			var resource = new GenomeScreenResourceN090KoKr(doc);
			var template = new GenomeScreenTemplateN090KoKr(resource, test);
			page = new SectionPage<>(547, 65, resource.fontDefault());
			for(var disease: template.diseases()) for(var sub: disease.subs()) {
				Map<GenomeScreenTemplate.Gene, Boolean> t = new HashMap<>();
				for(var gene: sub.genes()) {
					// boolean pos = RandomUtils.nextBoolean();
					if(gene.name().equals("CDH1")) values.put(gene, List.of(var));
					t.put(gene, gene.name().equals("CDH1"));
				}
				diseases.put(sub, t);
			}
			dto.diseases(diseases);
			return new GenomeScreenN090(template, dto.variants(values), new SectionHeaderKoKr(), logoType, sign, footer, page, SectionGeneListKoKr::new);
		} else if(TestInfo.N074 == test) {
			var resource = new GenomeScreenResourceN090KoKr(doc);
			var template = new GenomeScreenTemplateN074KoKr(resource, test);
			page = new SectionPage<>(547, 65, resource.fontDefault());
			for(var disease: template.diseases()) for(var sub: disease.subs()) {
				Map<GenomeScreenTemplate.Gene, Boolean> t = new HashMap<>();
				for(var gene: sub.genes()) {
					// boolean pos = RandomUtils.nextBoolean();
					// if(false) values.put(gene, List.of(var, var, var));
					t.put(gene, false);
				}
				diseases.put(sub, t);
			}
			dto.diseases(diseases);
			return new GenomeScreenN090(template, dto.variants(values), new SectionHeaderKoKr(), logoType, sign, footer, page, SectionGeneListKoKr::new);
		} else if(TestInfo.ON090 == test) {
			var resource = new GenomeScreenResourceN090EnUs(doc);
			var template = new GenomeScreenTemplateN090EnUs(resource, test);
			sign = new com.greencross.lims.report.enus.SectionSign<>(65);
			footer = new com.greencross.lims.report.enus.SectionFooterGenomeLabs<>();
			page = new SectionPage<>(565, 65, resource.fontDefault());
			for(var disease: template.diseases()) for(var sub: disease.subs()) {
				Map<GenomeScreenTemplate.Gene, Boolean> m = new HashMap<>();
				for(var gene: sub.genes()) {
					/*if("MLH1".equals(gene.name())) {
						values.put(gene, List.of(var));
						m.put(gene, true);
					} else */m.put(gene, false);
				}
				diseases.put(sub, m);
			}
			dto.diseases(diseases);
			dto.summary("수검자의 심장 돌연사 지놈 스크린 검사 결과\n" +
					"유전성 심장 돌연사와 관련된 유전자에서\n" +
					"병원성 변이(PV)가 발견되지 않았습니다.");
			return new GenomeScreenN090(template, dto.variants(values), new SectionHeaderEnUs(), logoType, sign, footer, page, SectionGeneList::new);
		} else if(TestInfo.N101 == test || TestInfo.N111 == test) {
			var resource = new GenomeScreenResourceN101KoKr(doc);
			var template = new GenomeScreenTemplateN101KoKr(resource, test);
			page = new SectionPage<>(547, 65, resource.fontDefault());
			for(var disease: template.diseases()) for(var sub: disease.subs()) {
				Map<GenomeScreenTemplate.Gene, Boolean> m = new HashMap<>();
				for(var gene: sub.genes()) {
					//if(RandomUtils.nextBoolean()) values.put(gene, List.of(var));
					m.put(gene, false);
				}
				diseases.put(sub, m);
			}
			dto.diseases(diseases);
			dto.summary("수검자의 ~~");
			return new GenomeScreenN101(template, dto.variants(values), logoType, sign, footer, page);
		} else return null;
	}
}
