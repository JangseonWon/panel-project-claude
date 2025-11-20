package com.greencross.lims.publish.single;

import com.greencross.alis.api.AlisResult;
import com.greencross.alis.api.AlisVariantResult;
import com.greencross.lims.dto.interpretation.PanelTest;
import com.greencross.lims.entity.Report;
import com.greencross.lims.entity.Request;
import com.greencross.lims.publish.AlisMapper;
import com.greencross.lims.publish.ReportDAO;
import com.greencross.lims.publish.VariantReference;
import com.gcgenome.lims.test.single.TestInfo;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SingleGeneMapper implements AlisMapper<PanelTest> {
    private final ReportDAO reportDAO;

    @Override
    public Class<PanelTest> clazz() {
        return PanelTest.class;
    }

    @Override
    public boolean match(Request request) {
        String service = request.pk().service();
        return Arrays.stream(TestInfo.TESTS).anyMatch(m->m.code().equalsIgnoreCase(service));
    }

    @Override
    public AlisResult[] map(Request request, PanelTest interpretation, Class clazz) throws IOException {
        String code = request.pk().service();
        AlisResult[] result = new AlisResult[]{
                AlisResult.builder().subCode(code + "010").text(interpretation.reasonForReferral()).build(),
                AlisResult.builder().subCode(code + "020").text(interpretation.result()).build(),
                AlisResult.builder().subCode(code + "021").text(interpretation.resultText()).build(),
                AlisResult.builder().subCode(code + "028").text(interpretation.abbreviationReference()).build(),
                AlisResult.builder().subCode(code + "029").text(interpretation.abbreviationDisease()).build(),
                AlisResult.builder().subCode(code + "030").text(interpretation.abbreviation()).build(),
                AlisResult.builder().subCode(code + "040").text(interpretation.interpretation()).build()
        };
        return Arrays.stream(result).toArray(AlisResult[]::new);
    }

    @Override
    public AlisVariantResult[] variants(Request request, PanelTest interpretation, Class<PanelTest> clazz) {
        List<AlisVariantResult> values = new LinkedList<>();
        if(interpretation.variants()!=null) {
            for(int i = 0; i < interpretation.variants().length; ++i) {
                PanelTest.Variant v = interpretation.variants()[i];
                values.addAll(map(v, i));
                values.add(AlisVariantResult.builder().row(i).key("Incidental findings").value(null).build());
            }
        }
        if(interpretation.addendum()!=null && interpretation.addendum().variants()!=null) {
            for(int i = 0; i < interpretation.addendum().variants().length; ++i) {
                PanelTest.Variant v = interpretation.addendum().variants()[i];
                values.addAll(map(v, i));
                values.add(AlisVariantResult.builder().row(i).key("Incidental findings").value("IF").build());
            }
        }
        return values.toArray(new AlisVariantResult[0]);
    }
    @Override
    public VariantReference[] variantReferences(Request request, PanelTest interpretation, Class<PanelTest> clazz) {
        List<VariantReference> values = new LinkedList<>();
        if(interpretation.variants()!=null) {
            for(int i = 0; i < interpretation.variants().length; ++i) {
                PanelTest.Variant v = interpretation.variants()[i];
                values.add(VariantReference.builder().snv(v.snv()).clazz(mapCls(v.clazz())).build());
            }
        }
        if(interpretation.addendum()!=null && interpretation.addendum().variants()!=null) {
            for(int i = 0; i < interpretation.addendum().variants().length; ++i) {
                PanelTest.Variant v = interpretation.addendum().variants()[i];
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

    private List<AlisVariantResult> map(PanelTest.Variant v, int i) {
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
