package com.greencross.lims.report.single;

import com.gcgenome.lims.test.single.TestInfo;
import com.greencross.lims.report.builder.AbstractReportTemplate;
import com.greencross.lims.report.builder.LogoType;

import java.awt.*;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface SingleGeneTemplate extends AbstractReportTemplate<SingleGeneResource> {
	LogoType logoType();
	TestInfo testInfo();
	default String resultToString(SingleGeneDto.Result result) {
		switch(result) {
			case P: return "POSITIVE";
			case N: return "NEGATIVE";
			case I: return "INCONCLUSIVE";
			default: return null;
		}
	}
	default Color resultToColor(SingleGeneDto.Result result) {
		switch(result) {
			case P: return new Color(135,51,61);
			case N: return Color.decode("#0077BF");
			case I: return Color.decode("#548235");
			default: return null;
		}
	}
	default Color individualResultToColor(String result) {
		if(result.equals("Not Detected")) return Color.decode("0x484848");
		else return new Color(135,51,61);
	}
	default String lblTestPerformed() {
		return "TEST PERFORMED";
	}
	default String lblReasonForRR() {
		return "REASON FOR REFERRAL";
	}
	default String lblResult() {
		return "RESULT";
	}
	default String[] lblIndividualResults() {
		return new String[]{
				String.format("%s Gene Test [Sequencing]", testInfo().gene()),
				String.format("%s Gene Test [MLPA]", testInfo().gene())
		};
	}
	default String[] lblGene() {
		return new String[]{"Gene", "DNA change", "Predicted\nAA change", "Zygosity", "OMIM Disease", "Inherit", "Class"};
	}
	default String lblNoDisease() {
		return "No disease-related variant";
	}
	default String lblReference() {
		return "Reference sequence";
	}
	default String lblOMIM() {
		return "Disease";
	}
	default String lblAbbreviation(){
		return "Abbreviation";
	}
	default String lblInterpretation() {
		return "INTERPRETATION";
	}
	default String lblAddendumResult() {
		return "ADDENDUM RESULT";
	}
	default String lblMethods() {
		return "TEST INFORMATION";
	}
	default String[] lblMethodList() {
		return new String[]{"Specimen", "Analysed Gene", "Methods", "Penetrance"};
	}
	default String lblLimitation() {
		return "LIMITATION";
	}
	default String lblReferences() {
		return "REFERENCES";
	}
	default String formatSequencingResult(SingleGeneDto dto) {
		return Optional.ofNullable(dto.report().variants())
				.filter(variants -> variants.length > 0)
				.map(variants -> Arrays.stream(variants)
						.map(SingleGeneDto.Variant::clazz)
						.collect(Collectors.toCollection(TreeSet::new))
				)
				.map(sortedSet -> String.join(",", sortedSet))
				.orElse("Not Detected");
	}

	default String formatMlpaResult(SingleGeneDto dto) {
		return Optional.of(dto.mlpaResult())
				.map(SingleGeneDto.Mlpa::result)
				.map(result -> {
					String content = Stream.of(dto.mlpaResult().exons(), dto.mlpaResult().zygosity(), dto.mlpaResult().delDup())
							.filter(Objects::nonNull)
							.collect(Collectors.joining(" "));
					return content.isEmpty() ? result : String.format("%s(%s)", result, content);
				})
				.orElse("Not Detected");
	}
}
