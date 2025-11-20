package com.greencross.lims.publish.wes;

import com.greencross.alis.api.AlisResult;
import com.greencross.alis.api.AlisVariantResult;
import com.greencross.lims.dto.interpretation.Des;
import com.greencross.lims.entity.Report;
import com.greencross.lims.entity.Request;
import com.greencross.lims.publish.AlisMapper;
import com.greencross.lims.publish.ReportDAO;
import com.greencross.lims.publish.VariantReference;
import com.gcgenome.lims.test.wes.TestWithSingleInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class WesWithSingleGeneMapper implements AlisMapper<Des> {
	private final ReportDAO reportDAO;

	@Override
	public Class<Des> clazz() {
		return Des.class;
	}

	@Override
	public boolean match(Request request) {
		String service = request.pk().service();
		return Arrays.stream(TestWithSingleInfo.TESTS).anyMatch(m->m.code().equalsIgnoreCase(service));
	}

	@Override
	public AlisResult[] map(Request request, Des interpretation, Class clazz) {
		String code = request.pk().service();
		AlisResult[] result = new AlisResult[]{
				// Single
				AlisResult.builder().subCode(code + "020").text(interpretation.result()).build(),
				AlisResult.builder().subCode(code + "021").text(interpretation.resultText()).build(),
				AlisResult.builder().subCode(code + "028").text(interpretation.abbreviationReference()).build(),
				AlisResult.builder().subCode(code + "029").text(interpretation.abbreviationDisease()).build(),
				AlisResult.builder().subCode(code + "030").text(interpretation.abbreviation()).build(),
				AlisResult.builder().subCode(code + "040").text(interpretation.interpretation()).build(),

				// DES
				AlisResult.builder().subCode(code + "050").text(interpretation.incidentalFindings().reasonForReferral()).build(),
				AlisResult.builder().subCode(code + "060").result1(interpretation.incidentalFindings().result()).build(),
				AlisResult.builder().subCode(code + "061").text(interpretation.incidentalFindings().resultText()).build(),
				AlisResult.builder().subCode(code + "070").text(interpretation.incidentalFindings().abbreviationReference()).build(),
				AlisResult.builder().subCode(code + "071").text(interpretation.incidentalFindings().abbreviationDisease()).build(),
				AlisResult.builder().subCode(code + "072").text(interpretation.incidentalFindings().abbreviation()).build(),
				AlisResult.builder().subCode(code + "080").text(interpretation.incidentalFindings().interpretation()).build(),
				AlisResult.builder().subCode(code + "120").result1(interpretation.meanDepth()).build(),
				AlisResult.builder().subCode(code + "130").result1(interpretation.coverage()).build()
		};
		AlisResult[] addendum = interpretation.incidentalFindings().incidentalFindings() != null ? new AlisResult[]{
				AlisResult.builder().subCode(code + "090").text(interpretation.incidentalFindings().incidentalFindings().resultText()).build(),
				AlisResult.builder().subCode(code + "100").text(interpretation.incidentalFindings().incidentalFindings().abbreviationReference()).build(),
				AlisResult.builder().subCode(code + "102").text(interpretation.incidentalFindings().incidentalFindings().abbreviationDisease()).build(),
				AlisResult.builder().subCode(code + "104").text(interpretation.incidentalFindings().incidentalFindings().abbreviation()).build(),
				AlisResult.builder().subCode(code + "110").text(interpretation.incidentalFindings().incidentalFindings().interpretation()).build(),
		} : new AlisResult[]{
				AlisResult.builder().subCode(code + "090").text(null).build(),
				AlisResult.builder().subCode(code + "100").text(null).build(),
				AlisResult.builder().subCode(code + "102").text(null).build(),
				AlisResult.builder().subCode(code + "104").text(null).build(),
				AlisResult.builder().subCode(code + "110").text(null).build(),
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
				values.add(AlisVariantResult.builder().row(i).key("Incidental findings").value("DES").build());
			}
			if(interpretation.incidentalFindings().incidentalFindings()!=null && interpretation.incidentalFindings().incidentalFindings().variants()!=null) {
				for(int i = 0; i < interpretation.incidentalFindings().incidentalFindings().variants().length; ++i) {
					Des.Variant v = interpretation.incidentalFindings().incidentalFindings().variants()[i];
					values.addAll(map(v, i));
					values.add(AlisVariantResult.builder().row(i).key("Incidental findings").value("DES").build());
				}
			}
		}
		return values.toArray(new AlisVariantResult[0]);
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
			if(interpretation.incidentalFindings().incidentalFindings()!=null && interpretation.incidentalFindings().incidentalFindings().variants()!=null) {
				for(int i = 0; i < interpretation.incidentalFindings().incidentalFindings().variants().length; ++i) {
					Des.Variant v = interpretation.incidentalFindings().incidentalFindings().variants()[i];
					values.add(VariantReference.builder().snv(v.snv()).clazz(mapCls(v.clazz())).build());
				}
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
}