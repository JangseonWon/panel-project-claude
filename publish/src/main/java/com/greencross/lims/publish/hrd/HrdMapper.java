package com.greencross.lims.publish.hrd;

import com.gcgenome.lims.test.hrd.TestInfo;
import com.greencross.alis.api.AlisResult;
import com.greencross.alis.api.AlisVariantResult;
import com.greencross.lims.dto.interpretation.Hrd;
import com.greencross.lims.entity.Report;
import com.greencross.lims.entity.Request;
import com.greencross.lims.publish.AlisMapper;
import com.greencross.lims.publish.ReportDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class HrdMapper implements AlisMapper<Hrd> {
    private final ReportDAO reportDAO;
    @Override
    public Class<Hrd> clazz() {
        return Hrd.class;
    }

    @Override
    public boolean match(Request request) {
        String service = request.pk().service();
        return Arrays.stream(TestInfo.TESTS).anyMatch(m->m.code().equalsIgnoreCase(service));
    }
    @Override
    public AlisResult[] map(Request request, Hrd interpretation, Class clazz) {
        String code = request.pk().service();
        Hrd.GeneResult brcaResult = interpretation.results()!=null? Arrays.stream(interpretation.results()).filter(g->g.tier().equalsIgnoreCase("BRCA")).findAny().orElse(null) :null;
        Hrd.GeneResult tier1Result = interpretation.results()!=null? Arrays.stream(interpretation.results()).filter(g->g.tier().equalsIgnoreCase("TIER1")).findAny().orElse(null) :null;
        Hrd.GeneResult tier2Result = interpretation.results()!=null? Arrays.stream(interpretation.results()).filter(g->g.tier().equalsIgnoreCase("TIER2")).findAny().orElse(null) :null;
        return new AlisResult[]{
                AlisResult.builder().subCode(code + "010").result1(interpretation.cancerType()).build(),
                AlisResult.builder().subCode(code + "020").result1(interpretation.gi()).build(),
                AlisResult.builder().subCode(code + "030").result1(interpretation.brca()!=null? interpretation.brca():null).build(),
                AlisResult.builder().subCode(code + "040").result1(interpretation.snv()).build(),
                AlisResult.builder().subCode(code + "050").result1(interpretation.cnv()).build(),
                AlisResult.builder().subCode(code + "060").result1(NumberFormat.getInstance().format(interpretation.giScore())).build(),
                AlisResult.builder().subCode(code + "070").text(brcaResult!=null?brcaResult.interpretation():null).build(),

                AlisResult.builder().subCode(code + "080").result1(tier1Result!=null&&tier1Result.variants()!=null? String.valueOf(tier1Result.variants().length) : "0").build(),
                AlisResult.builder().subCode(code + "085").text(tier1Result!=null? tier1Result.interpretation():null).build(),
                AlisResult.builder().subCode(code + "090").result1(tier2Result!=null&&tier2Result.variants()!=null? String.valueOf(tier2Result.variants().length) : "0").build(),
                AlisResult.builder().subCode(code + "095").text(tier2Result!=null? tier2Result.interpretation():null).build(),
        };
    }

    @Override
    public AlisVariantResult[] variants(Request request, Hrd interpretation, Class<Hrd> clazz) {
        List<AlisVariantResult> values = new LinkedList<>();
        if(interpretation.results()!=null && interpretation.results()[0].variants()!=null) {
            for(int i = 0; i < interpretation.results()[0].variants().length; ++i) {
                Hrd.Variant v = interpretation.results()[0].variants()[i];
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

    private List<AlisVariantResult> map(Hrd.Variant v, int i) {
        List<AlisVariantResult> values = new LinkedList<>();
        values.add(AlisVariantResult.builder().row(i).key("Gene").value(v.gene()).build());
        values.add(AlisVariantResult.builder().row(i).key("DNA").value(v.hgvsc()!=null?v.hgvsc():v.dna()).build());
        values.add(AlisVariantResult.builder().row(i).key("Protein").value(v.hgvsp()!=null?v.hgvsp():v.protein()).build());
        values.add(AlisVariantResult.builder().row(i).key("VAF").value(v.vaf()!=null?NumberFormat.getInstance().format(v.vaf()):null).build());
        values.add(AlisVariantResult.builder().row(i).key("Depth").value(v.depth()!=null?NumberFormat.getInstance().format(v.depth()):null).build());
        values.add(AlisVariantResult.builder().row(i).key("COSMIC ID").value(v.cosmicId()).build());
        return values;
    }
}
