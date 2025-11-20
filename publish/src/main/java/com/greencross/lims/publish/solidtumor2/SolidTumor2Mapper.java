package com.greencross.lims.publish.solidtumor2;

import com.greencross.alis.api.AlisResult;
import com.greencross.alis.api.AlisVariantResult;
import com.greencross.lims.dto.interpretation.SolidTumor2;
import com.greencross.lims.entity.Report;
import com.greencross.lims.entity.Request;
import com.greencross.lims.publish.AlisMapper;
import com.greencross.lims.publish.ReportDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@RequiredArgsConstructor
public class SolidTumor2Mapper implements AlisMapper<SolidTumor2> {
    private final ReportDAO reportDAO;
    @Override
    public Class<SolidTumor2> clazz() {
        return SolidTumor2.class;
    }

    @Override
    public boolean match(Request request) {
        return List.of("N198","N199","N200","ON198","ON199","ON200","G0022402").contains(request.pk().service());
    }

    @Override
    public AlisResult[] map(Request request, SolidTumor2 interpretation, Class<SolidTumor2> clazz) {
        return new AlisResult[]{};
    }
    @Override
    public AlisVariantResult[] variants(Request request, SolidTumor2 interpretation, Class<SolidTumor2> clazz) {
        List<AlisVariantResult> values = new LinkedList<>();
        AtomicInteger row = new AtomicInteger(0);
        for(int i = 0; i < interpretation.variants().length; ++i) values.addAll(map(interpretation.variants()[i], row.getAndIncrement()));
        return values.toArray(new AlisVariantResult[0]);
    }

    @Override
    public String text(Request request, long createAt) throws IOException {
        Report.ReportPK pk = com.greencross.lims.entity.Report.ReportPK.builder().sample(request.pk().sample()).service(request.pk().service()).createAt(createAt).build();
        return reportDAO.find(pk).map(Report::longFormText).orElse(null);
    }
    @Override
    public String textShort(Request request, long createAt) throws IOException {
        return null;
    }

    private List<AlisVariantResult> map(SolidTumor2.Variant v, int i) {
        List<AlisVariantResult> values = new LinkedList<>();
        values.add(AlisVariantResult.builder().row(i).key("Gene").value(v.gene()).build());
        values.add(AlisVariantResult.builder().row(i).key("DNA").value(v.hgvsc()).build());
        values.add(AlisVariantResult.builder().row(i).key("Protein").value(v.hgvsp()).build());
        values.add(AlisVariantResult.builder().row(i).key("VAF").value(v.vaf()).build());
        if(v.depth()!=null) values.add(AlisVariantResult.builder().row(i).key("Depth").value(NumberFormat.getInstance().format(v.depth())).build());
        values.add(AlisVariantResult.builder().row(i).key("Significance").value(v.significance()).build());
        values.add(AlisVariantResult.builder().row(i).key("Tier 구분").value(String.valueOf(v.tier().ordinal()+1)).build());
        return values;
    }
}
