package com.greencross.lims.publish.panels;

import com.greencross.alis.api.AlisResult;
import com.greencross.alis.api.AlisVariantResult;
import com.greencross.lims.dto.interpretation.PanelTest;
import com.greencross.lims.entity.Request;
import com.greencross.lims.publish.AlisMapper;
import com.greencross.lims.publish.VariantReference;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

@Component
public class CancerEnUsMapper implements AlisMapper<PanelTest> {
    @Override
    public Class<PanelTest> clazz() {
        return PanelTest.class;
    }
    @Override
    public boolean match(Request request) {
        String service = request.pk().service();
        return "ON001".equalsIgnoreCase(service) || "ON040".equalsIgnoreCase(service);
    }
    @Override
    public AlisResult[] map(Request request, PanelTest interpretation, Class clazz) {
        String code = request.pk().service();
        return new AlisResult[]{
                AlisResult.builder().subCode(code + "010").text(interpretation.reasonForReferral()).build(),
                AlisResult.builder().subCode(code + "020").text(interpretation.result()).build(),
                AlisResult.builder().subCode(code + ("ON040".equalsIgnoreCase(code)?"021":"030")).text(interpretation.resultText()).build(),
                AlisResult.builder().subCode(code + ("ON040".equalsIgnoreCase(code)?"030":"040")).text(interpretation.abbreviationReference()).build(),
                AlisResult.builder().subCode(code + ("ON040".equalsIgnoreCase(code)?"031":"050")).text(interpretation.abbreviationDisease()).build(),
                AlisResult.builder().subCode(code + ("ON040".equalsIgnoreCase(code)?"032":"060")).text(interpretation.abbreviation()).build(),
                AlisResult.builder().subCode(code + ("ON040".equalsIgnoreCase(code)?"040":"070")).text(interpretation.interpretation()).build(),
                AlisResult.builder().subCode(code + ("ON040".equalsIgnoreCase(code)?"120":"080")).text(interpretation.meanDepth()).build(),
                AlisResult.builder().subCode(code + ("ON040".equalsIgnoreCase(code)?"130":"090")).text(interpretation.coverage()).build()
        };
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
        return values.toArray(new VariantReference[0]);
    }
    @Override
    public String text(Request request, long createAt) throws IOException {
        return null;
    }
    @Override
    public String textShort(Request request, long createAt) throws IOException {
        return null;
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
