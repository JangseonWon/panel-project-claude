package com.greencross.lims.publish.wes;

import com.gcgenome.lims.test.wes.TestInfo;
import com.greencross.alis.api.AlisResult;
import com.greencross.alis.api.AlisVariantResult;
import com.greencross.lims.dto.interpretation.Des;
import com.greencross.lims.entity.Report;
import com.greencross.lims.entity.Request;
import com.greencross.lims.publish.AlisMapper;
import com.greencross.lims.publish.ReportDAO;
import com.greencross.lims.publish.VariantReference;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class WesMapper implements AlisMapper<Des> {
	private final ReportDAO reportDAO;

	@Override
	public Class<Des> clazz() {
		return Des.class;
	}

	@Override
	public boolean match(Request request) {
		String service = request.pk().service();
		return Arrays.stream(TestInfo.TESTS).anyMatch(m->m.code().equalsIgnoreCase(service));
	}

	@Override
	public AlisResult[] map(Request request, Des interpretation, Class clazz) throws IOException {
		String code = request.pk().service();
		AlisResult[] result = new AlisResult[]{
				AlisResult.builder().subCode(code + "010").text(interpretation.reasonForReferral()).build(),
				AlisResult.builder().subCode(code + "020").result1(interpretation.result()).build(),
				AlisResult.builder().subCode(code + "021").text(interpretation.resultText()).build(),
				AlisResult.builder().subCode(code + "030").text(interpretation.abbreviationReference()).build(),
				AlisResult.builder().subCode(code + "031").text(interpretation.abbreviationDisease()).build(),
				AlisResult.builder().subCode(code + "032").text(interpretation.abbreviation()).build(),
				AlisResult.builder().subCode(code + "040").text(interpretation.interpretation()).build(),
				AlisResult.builder().subCode(code + "120").result1(interpretation.meanDepth()).build(),
				AlisResult.builder().subCode(code + "130").result1(interpretation.coverage()).build()
		};
		AlisResult[] addendum = interpretation.incidentalFindings() != null ? new AlisResult[]{
				AlisResult.builder().subCode(code + "100").text(interpretation.incidentalFindings().resultText()).build(),
				AlisResult.builder().subCode(code + "101").text(interpretation.incidentalFindings().abbreviationReference()).build(),
				AlisResult.builder().subCode(code + "102").text(interpretation.incidentalFindings().abbreviationDisease()).build(),
				AlisResult.builder().subCode(code + "103").text(interpretation.incidentalFindings().abbreviation()).build(),
				AlisResult.builder().subCode(code + "105").text(interpretation.incidentalFindings().interpretation()).build(),
		} : new AlisResult[]{
				AlisResult.builder().subCode(code + "100").text(null).build(),
				AlisResult.builder().subCode(code + "101").text(null).build(),
				AlisResult.builder().subCode(code + "102").text(null).build(),
				AlisResult.builder().subCode(code + "103").text(null).build(),
				AlisResult.builder().subCode(code + "105").text(null).build(),
		};
		return Stream.concat(Arrays.stream(result), Arrays.stream(addendum)).toArray(AlisResult[]::new);
	}

	@Override
	public AlisVariantResult[] variants(Request request, Des interpretation, Class<Des> clazz) {
		List<AlisVariantResult> values = new LinkedList<>();
		if(interpretation.variants()!=null) {
			for(int i = 0; i < interpretation.variants().length; ++i) {
				Des.Variant v = interpretation.variants()[i];
				values.addAll(map(v, i));
				values.add(AlisVariantResult.builder().row(i).key("Incidental findings").value(null).build());
			}
		}
		if(interpretation.incidentalFindings()!=null && interpretation.incidentalFindings().variants()!=null) {
			for(int i = 0; i < interpretation.incidentalFindings().variants().length; ++i) {
				Des.Variant v = interpretation.incidentalFindings().variants()[i];
				values.addAll(map(v, i));
				values.add(AlisVariantResult.builder().row(i).key("Incidental findings").value("IF").build());
			}
		}
		return values.toArray(new AlisVariantResult[0]);
	}

	@Override
	public VariantReference[] variantReferences(Request request, Des interpretation, Class<Des> clazz) {
		List<VariantReference> values = new LinkedList<>();
		if(interpretation.variants()!=null) {
			for(int i = 0; i < interpretation.variants().length; ++i) {
				Des.Variant v = interpretation.variants()[i];
				values.add(VariantReference.builder().snv(v.snv()).clazz(mapCls(v.clazz())).build());
			}
		}
		if(interpretation.incidentalFindings()!=null && interpretation.incidentalFindings().variants()!=null) {
			for(int i = 0; i < interpretation.incidentalFindings().variants().length; ++i) {
				Des.Variant v = interpretation.incidentalFindings().variants()[i];
				values.add(VariantReference.builder().snv(v.snv()).clazz(mapCls(v.clazz())).build());
			}
		}
		return values.toArray(new VariantReference[0]);
	}

	@Override
	public String text(Request request, long createAt) throws IOException {
		Report.ReportPK pk = com.greencross.lims.entity.Report.ReportPK.builder().sample(request.pk().sample()).service(request.pk().service()).createAt(createAt).build();
		return reportDAO.find(pk).map(Report::longFormText).orElse(null);
	}

	@Override
	public String textShort(Request request, long createAt) throws IOException {
		Report.ReportPK pk = com.greencross.lims.entity.Report.ReportPK.builder().sample(request.pk().sample()).service(request.pk().service()).createAt(createAt).build();
		return reportDAO.find(pk).map(Report::shortFormText).orElse(null);
	}

	private List<AlisVariantResult> map(Des.Variant v, int i) {
		List<AlisVariantResult> values = new LinkedList<>();
		values.add(AlisVariantResult.builder().row(i).key("Gene").value(v.gene()).build());
		values.add(AlisVariantResult.builder().row(i).key("DNA change").value(v.hgvsc()).build());
		values.add(AlisVariantResult.builder().row(i).key("Predicted AA change").value(v.hgvsp()).build());
		values.add(AlisVariantResult.builder().row(i).key("Zygosity").value(v.zygosity()).build());
		values.add(AlisVariantResult.builder().row(i).key("OMIM Disease").value(v.disease()).build());
		values.add(AlisVariantResult.builder().row(i).key("Inherit").value(v.inheritance()).build());
		values.add(AlisVariantResult.builder().row(i).key("Class").value(v.clazz()).build());
		return values;
	}
}