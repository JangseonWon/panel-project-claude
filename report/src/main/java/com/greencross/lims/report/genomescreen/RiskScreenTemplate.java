package com.greencross.lims.report.genomescreen;

import com.gcgenome.lims.report.TextBlock;
import com.greencross.lims.report.builder.AbstractReportTemplate;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public interface RiskScreenTemplate<R extends RiskScreenResource> extends AbstractReportTemplate<R> {
	interface DiseaseRiskScreen {
		DiseaseSubRiskScreen[] subs();
		default boolean isPositive(GenomeScreenWithRiskScreenDto dto) {
			return Arrays.stream(subs()).anyMatch(s->s.isPositive(dto));
		}
	}
	interface DiseaseSubRiskScreen {
		GeneRiskScreen[] genes();
		default boolean isPositive(GenomeScreenWithRiskScreenDto dto) {
			return Arrays.stream(genes()).anyMatch(g->isPositive(g, dto));
		}
		default boolean isPositive(GeneRiskScreen gene, GenomeScreenWithRiskScreenDto dto) {
			return Arrays.stream(gene.snvs()).anyMatch(s->isPositive(s, dto.variantsRiskScreen().get(s)));
		}
		List<Type> types(Snv snv);
		default Type type(Snv snv, String genotype) {
			String sorted = sort(genotype);
			return types(snv).stream().filter(t->sorted.equalsIgnoreCase(sort(t.genotype))).findFirst().orElse(null);
		}
		default boolean isPositive(Snv snv, String genotype) {
			return type(snv, genotype).isPositive;
		}
		default double risk(GeneRiskScreen gene, GenomeScreenWithRiskScreenDto dto) {
			return Arrays.stream(gene.snvs()).map(s->{
				String genotype = dto.variantsRiskScreen().get(s);
				return risk(s, genotype);
			}).reduce(1.0, Math::max);
		}
		default double risk(Snv snv, String genotype) {
			return type(snv, genotype).risk;
		}
	}
	interface GeneRiskScreen {
		String name();
		Tier tier();
		Snv[] snvs();
	}
	static String sort(String k) {
		if(k == null) return null;
		char[] arr = k.toCharArray();
		Arrays.sort(arr);
		return new String(arr);
	}
	@Getter
	@Setter
	@Accessors(fluent = true)
	class Type {
		final String genotype;
		final boolean isPositive;
		final double risk;
		final double percentage;
		boolean lessThen = false;
		private Type(String genotype, boolean isPositive, double risk, double percentage) {
			this.genotype = genotype;
			this.isPositive = isPositive;
			this.risk = risk;
			this.percentage = percentage;
		}
		static Type of(String genotype, boolean isPositive, double risk, double percentage) {
			return new Type(genotype, isPositive, risk, percentage);
		}
		static Type of(String genotype, double percentage) {
			return new Type(genotype, false, 1, percentage);
		}
		static Type of(String genotype, double risk, double percentage) {
			return new Type(genotype, risk > 1, risk, percentage);
		}
	}
	interface Snv {
		String pos();
	}
	enum Tier {
		Tier1, Tier2, Tier3
	}
	String lblSummaryTestIntroTitle();
	String lblSummaryTestIntroTitleInfoRiskScreen();
	String lblSummaryInfoRiskScreen();
	String lblSummaryTitle();
	DiseaseRiskScreen[] riskscreens();
	String diseaseRiskScreenName(DiseaseRiskScreen disease);
	default String diseaseRiskScreenInfo(DiseaseRiskScreen disease) { return null; }
	String diseaseSubRiskScreenName(DiseaseSubRiskScreen disease);
	String diseaseSubRiskPositive();
	String diseaseSubRiskNegative();
	String risk(double coefficient);
	default String summary(DiseaseRiskScreen disease, GenomeScreenWithRiskScreenDto dto) {
		if(disease.isPositive(dto)) return summaryPositive(disease);
		else return summaryNegative(disease);
	}
	String summaryPositive(DiseaseRiskScreen disease);
	String summaryNegative(DiseaseRiskScreen disease);
	default List<String> interpretation(DiseaseRiskScreen disease, GenomeScreenWithRiskScreenDto dto) {
		return Arrays.stream(disease.subs())
					 .map(DiseaseSubRiskScreen::genes)
					 .flatMap(Arrays::stream).distinct()
					 .flatMap(gene->Arrays.stream(gene.snvs())
										  .filter(v->dto.variantsRiskScreen().containsKey(v))
										  .map(v->interpretation(disease, gene, v, dto.variantsRiskScreen().get(v)))
										  .filter(Objects::nonNull)
										  .flatMap(List::stream))
					 .distinct().collect(Collectors.toList());
	}
	List<String> interpretation(DiseaseRiskScreen disease, GeneRiskScreen gene, Snv v, String genotype);
	default List<String> recommendation(DiseaseRiskScreen disease, GenomeScreenWithRiskScreenDto dto) {
		List<String> recommendation = Arrays.stream(disease.subs())
											.map(DiseaseSubRiskScreen::genes)
											.flatMap(Arrays::stream).distinct()
											.flatMap(gene->Arrays.stream(gene.snvs())
																 .filter(v->dto.variantsRiskScreen().containsKey(v))
																 .map(v->recommendation(disease, gene, v, dto.variantsRiskScreen().get(v)))
																 .filter(Objects::nonNull)
																 .flatMap(List::stream))
											.distinct().collect(Collectors.toList());
		if(recommendation.isEmpty()) return recommendationNegative(disease);
		else return recommendation;
	}
	List<String> recommendation(DiseaseRiskScreen disease, GeneRiskScreen gene, Snv v, String genotype);
	default List<String> recommendationNegative(DiseaseRiskScreen disease) {
		return List.of();
	}
	String geneRiskScreenInfo(GeneRiskScreen gene);
	String lblLifestyleTitle();
	String lblLifestyleInfo();
	String lblLifestyleInfo2();
	String lblLifestyle();
	String lblLifestyleDisease();
	String lblLifestyleResultTitle();
	String lblLifestyleResultHeader(GenomeScreenWithRiskScreenDto dto);
	default boolean isLifestyleResultPositive(GenomeScreenWithRiskScreenDto dto) {
		if(Arrays.stream(riskscreens()).anyMatch(disease->disease.isPositive(dto))) return true;
		else return false;
	}
	default String lblLifestyleResultValue(GenomeScreenWithRiskScreenDto dto) {
		if(isLifestyleResultPositive(dto)) return diseaseSubRiskPositive();
		else return diseaseSubRiskNegative();
	}
	RiskFactor[] riskfactors();
	String toString(RiskFactor factor);
	String lblGuidePreventTitle();
	String lblGuideDietTitle();
	String lblGuideDietInfo();
	String lblGuideDietGood();
	String lblGuideDietBad();
	String lblGuideAthleticTitle();
	String lblGuideReferenceTitle();
	String[] lblAthletics();
	TextBlock[] referencesRiskScreen();
	String lblClinicalMeanings();
	String lblClinicalMeanings(Tier tier);
	String lblClinicalMeaningInfo();
	String clinicalReference(GeneRiskScreen gene);
	String clinicalMeaning(GeneRiskScreen gene);
	interface RiskFactor {
		double risk();
	}
}
