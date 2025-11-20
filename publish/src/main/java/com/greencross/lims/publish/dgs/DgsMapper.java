package com.greencross.lims.publish.dgs;

import com.gcgenome.lims.test.dgs.TestInfo;
import com.greencross.alis.api.AlisResult;
import com.greencross.alis.api.AlisVariantResult;
import com.greencross.lims.dto.interpretation.Dgs;
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
public class DgsMapper implements AlisMapper<Dgs> {
	private final ReportDAO reportDAO;
	@Override
	public Class<Dgs> clazz() {
		return Dgs.class;
	}

	@Override
	public boolean match(Request request) {
		String service = request.pk().service();
		return Arrays.stream(TestInfo.TESTS).anyMatch(m->m.code().equalsIgnoreCase(service));
	}

	@Override
	public AlisResult[] map(Request request, Dgs interpretation, Class clazz) {
		String code = request.pk().service();
		AlisResult[] result = new AlisResult[]{
				AlisResult.builder().subCode(code + "010").text(interpretation.clinicalInformation()).build(),
				AlisResult.builder().subCode(code + "020").result1(interpretation.result()).build(),
				AlisResult.builder().subCode(code + "021").text(interpretation.resultText()).build(),
				AlisResult.builder().subCode(code + "030").text(interpretation.abbreviationReference()).build(),
				AlisResult.builder().subCode(code + "031").text(interpretation.abbreviationDisease()).build(),
				AlisResult.builder().subCode(code + "032").text(interpretation.abbreviation()).build(),
				AlisResult.builder().subCode(code + "040").text(interpretation.interpretation()).build(),
				AlisResult.builder().subCode(code + "050").text(interpretation.recommendation()).build(),
				AlisResult.builder().subCode(code + "070").result1(interpretation.meanDepth()).build(),
				AlisResult.builder().subCode(code + "080").result1(interpretation.coverage()).build()
		};
		AlisResult[] addendum = interpretation.incidentalFindings() != null ? new AlisResult[]{
				AlisResult.builder().subCode(code + "060").text(interpretation.incidentalFindings().interpretation()).build(),
		} : new AlisResult[]{
				AlisResult.builder().subCode(code + "060").text(null).build(),
		};
		return Stream.concat(Arrays.stream(result), Arrays.stream(addendum)).toArray(AlisResult[]::new);
	}

	@Override
	public AlisVariantResult[] variants(Request request, Dgs interpretation, Class<Dgs> clazz) {
		List<AlisVariantResult> values = new LinkedList<>();
		if(interpretation.variants()!=null) {
			for(int i = 0; i < interpretation.variants().length; ++i) {
				Dgs.Variant v = interpretation.variants()[i];
				values.addAll(map(v, i));
			}
		}
		return values.toArray(new AlisVariantResult[0]);
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
	@Override
	public VariantReference[] variantReferences(Request request, Dgs interpretation, Class<Dgs> clazz) {
		List<VariantReference> values = new LinkedList<>();
		if(interpretation.variants()!=null) {
			for(int i = 0; i < interpretation.variants().length; ++i) {
				Dgs.Variant v = interpretation.variants()[i];
				values.add(VariantReference.builder().snv(v.snv()).clazz(mapCls(v.clazz())).build());
			}
		}
		if(interpretation.incidentalFindings()!=null && interpretation.incidentalFindings().variants()!=null) {
			for(int i = 0; i < interpretation.incidentalFindings().variants().length; ++i) {
				Dgs.Variant v = interpretation.incidentalFindings().variants()[i];
				values.add(VariantReference.builder().snv(v.snv()).clazz(mapCls(v.clazz())).build());
			}
		}
		return values.toArray(new VariantReference[0]);
	}
	private List<AlisVariantResult> map(Dgs.Variant v, int i) {
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