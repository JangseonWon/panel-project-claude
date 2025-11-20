package com.greencross.lims.publish.sanger;

import com.greencross.alis.api.AlisResult;
import com.greencross.alis.api.AlisVariantResult;
import com.greencross.lims.dto.interpretation.Sanger;
import com.greencross.lims.entity.Report;
import com.greencross.lims.entity.Request;
import com.greencross.lims.publish.AlisMapper;
import com.gcgenome.lims.test.sanger.TestInfo;
import com.greencross.lims.publish.ReportDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SangerMapper implements AlisMapper<Sanger> {
    private final ReportDAO reportDAO;
    @Override
    public Class<Sanger> clazz() {
        return Sanger.class;
    }
    @Override
    public boolean match(Request request) {
        String service = request.pk().service();
        return Arrays.stream(TestInfo.TESTS).anyMatch(m->m.code().equalsIgnoreCase(service));
    }
    @Override
    public AlisResult[] map(Request request, Sanger interpretation, Class clazz) {
        String code = request.pk().service();
        AlisResult[] result = new AlisResult[]{
               AlisResult.builder().subCode(code + ("T012".equalsIgnoreCase(request.pk().service())?"050":"010")).text(interpretation.interpretation()).build()
        };
        return Arrays.stream(result).toArray(AlisResult[]::new);
    }

    @Override
    public AlisVariantResult[] variants(Request request, Sanger interpretation, Class<Sanger> clazz) {
        List<AlisVariantResult> values = new LinkedList<>();
        if(interpretation.variants()!=null) {
            for(int i = 0; i < interpretation.variants().length; ++i) values.addAll(map(interpretation.variants()[i], i));
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

    private List<AlisVariantResult> map(Sanger.Variant v, int i) {
        List<AlisVariantResult> values = new LinkedList<>();
        values.add(AlisVariantResult.builder().row(i).key("Gene").value(v.gene()).build());
        values.add(AlisVariantResult.builder().row(i).key("DNA change").value(v.hgvsc()).build());
        values.add(AlisVariantResult.builder().row(i).key("Predicted AA change").value(v.hgvsp()).build());
        values.add(AlisVariantResult.builder().row(i).key("Class").value(v.clazz()).build());
        values.add(AlisVariantResult.builder().row(i).key("Zygosity").value(v.zygosity()).build());
        values.add(AlisVariantResult.builder().row(i).key("Result").value(v.result()).build());
        values.add(AlisVariantResult.builder().row(i).key("Figure").value(String.format("[Attached file %d]", (i+1))).build());
        return values;
    }

}
