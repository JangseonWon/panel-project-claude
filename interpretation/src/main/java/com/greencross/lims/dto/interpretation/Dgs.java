package com.greencross.lims.dto.interpretation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Setter
@Getter
@Accessors(fluent=true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Dgs extends CalculatedResult<com.greencross.lims.dto.interpretation.Dgs.Variant> {
	private String result;
	@JsonProperty("result_text")
	private String resultText;
	@JsonProperty("clinical_information")
	private String clinicalInformation;
	@JsonProperty("abbreviation_reference")
	private String abbreviationReference;
	@JsonProperty("abbreviation_disease")
	private String abbreviationDisease;
	private String abbreviation;
	private String interpretation;
	@JsonProperty("mean_depth")
	private String meanDepth;
	private String coverage;
	private String recommendation;
	private Variant[] variants;
	@JsonProperty("consent_incidental_findings")
	private Boolean consentIncidentalFindings;
	@JsonProperty("incidental_findings")
	private com.greencross.lims.dto.interpretation.Dgs incidentalFindings;
	private String inspector;
	private String reporter;
	private String reviewer;
	private String comment1;
	private String comment2;


	@Override
	public void calculateResult(List<Variant> variantList) {
		String res = calculateResultString(variantList);
		if(res != null) result(res);
	}

	public static final class Variant extends BaseVariant<Variant> {}
}

