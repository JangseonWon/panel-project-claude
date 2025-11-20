package com.greencross.lims.publish.bloodcancer;

import com.gcgenome.lims.test.bloodcancer.TestInfo;
import com.greencross.alis.api.AlisResult;
import com.greencross.alis.api.AlisVariantResult;
import com.greencross.lims.dto.interpretation.BloodCancer;
import com.greencross.lims.entity.Report;
import com.greencross.lims.entity.Request;
import com.greencross.lims.publish.AlisMapper;
import com.greencross.lims.publish.ReportDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BloodCancerMapper implements AlisMapper<BloodCancer> {
    private final ReportDAO reportDAO;
    @Override
    public Class<BloodCancer> clazz() {
        return BloodCancer.class;
    }

    @Override
    public boolean match(Request request) {
        String service = request.pk().service();
        return Arrays.stream(TestInfo.TESTS).anyMatch(m->m.code().equalsIgnoreCase(service));
    }

    @Override
    public AlisResult[] map(Request request, BloodCancer interpretation, Class<BloodCancer> clazz) {
        String code = request.pk().service();
        BloodCancer.Result tier1 = interpretation.results()!=null?Arrays.stream(interpretation.results()).filter(r->r.tier()== BloodCancer.Tier.Tier1).findFirst().orElse(null):null;
        BloodCancer.Result tier2 = interpretation.results()!=null?Arrays.stream(interpretation.results()).filter(r->r.tier()== BloodCancer.Tier.Tier2).findFirst().orElse(null):null;
        BloodCancer.Result tier3 = interpretation.results()!=null?Arrays.stream(interpretation.results()).filter(r->r.tier()== BloodCancer.Tier.Tier3).findFirst().orElse(null):null;
        if(tier1 == null) tier1 = new BloodCancer.Result();
        if(tier2 == null) tier2 = new BloodCancer.Result();
        if(tier3 == null) tier3 = new BloodCancer.Result();
        if(tier1.variants()==null) tier1.variants(new BloodCancer.Variant[0]);
        if(tier2.variants()==null) tier2.variants(new BloodCancer.Variant[0]);
        if(tier3.variants()==null) tier3.variants(new BloodCancer.Variant[0]);
        String genesTier1 = Arrays.stream(tier1.variants()).map(BloodCancer.Variant::gene).distinct().collect(Collectors.joining(", "));
        if(genesTier1.isEmpty()) genesTier1 = "-";
        String genesTier2 = Arrays.stream(tier2.variants()).map(BloodCancer.Variant::gene).distinct().collect(Collectors.joining(", "));
        if(genesTier2.isEmpty()) genesTier2 = "-";
        String genesTier3 = Arrays.stream(tier3.variants()).map(BloodCancer.Variant::gene).distinct().collect(Collectors.joining(", "));
        if(genesTier3.isEmpty()) genesTier3 = "-";
        return new AlisResult[]{
                AlisResult.builder().subCode(code + "008").text(interpretation.cancerType()).build(),
                AlisResult.builder().subCode(code + "010").result1(String.valueOf(tier1.variants().length)).build(),
                AlisResult.builder().subCode(code + "011").result1(String.valueOf(tier2.variants().length)).build(),
                AlisResult.builder().subCode(code + "012").result1(String.valueOf(tier3.variants().length)).build(),
                AlisResult.builder().subCode(code + "013").text(genesTier1).build(),
                AlisResult.builder().subCode(code + "014").text(genesTier2).build(),
                AlisResult.builder().subCode(code + "015").text(genesTier3).build(),
                AlisResult.builder().subCode(code + "020").text(tier1.interpretation()).build(),
                AlisResult.builder().subCode(code + "030").text(tier2.interpretation()).build(),
                AlisResult.builder().subCode(code + "040").text(tier3.interpretation()).build(),
                AlisResult.builder().subCode(code + "100").result1(interpretation.qcDna()).build(),
                AlisResult.builder().subCode(code + "101").result1(interpretation.qcLibrary()).build(),
                AlisResult.builder().subCode(code + "102").result1(interpretation.qcSequencing()).build(),
                AlisResult.builder().subCode(code + "103").result1(interpretation.meanDepth()).build(),
                AlisResult.builder().subCode(code + "104").result1(interpretation.coverage()).build()
        };
    }

    @Override
    public AlisVariantResult[] variants(Request request, BloodCancer interpretation, Class<BloodCancer> clazz) {
        List<AlisVariantResult> values = new LinkedList<>();
        BloodCancer.Result tier1 = interpretation.results()!=null?Arrays.stream(interpretation.results()).filter(r->r.tier()== BloodCancer.Tier.Tier1).findFirst().orElse(null):null;
        BloodCancer.Result tier2 = interpretation.results()!=null?Arrays.stream(interpretation.results()).filter(r->r.tier()== BloodCancer.Tier.Tier2).findFirst().orElse(null):null;
        BloodCancer.Result tier3 = interpretation.results()!=null?Arrays.stream(interpretation.results()).filter(r->r.tier()== BloodCancer.Tier.Tier3).findFirst().orElse(null):null;
        if(tier1 == null) tier1 = new BloodCancer.Result();
        if(tier2 == null) tier2 = new BloodCancer.Result();
        if(tier3 == null) tier3 = new BloodCancer.Result();
        if(tier1.variants()==null) tier1.variants(new BloodCancer.Variant[0]);
        if(tier2.variants()==null) tier2.variants(new BloodCancer.Variant[0]);
        if(tier3.variants()==null) tier3.variants(new BloodCancer.Variant[0]);
        AtomicInteger row = new AtomicInteger(0);
        for(int i = 0; i < tier1.variants().length; ++i) values.addAll(map(tier1.variants()[i], row.getAndIncrement(), BloodCancer.Tier.Tier1));
        for(int i = 0; i < tier2.variants().length; ++i) values.addAll(map(tier2.variants()[i], row.getAndIncrement(), BloodCancer.Tier.Tier2));
        for(int i = 0; i < tier3.variants().length; ++i) values.addAll(map(tier3.variants()[i], row.getAndIncrement(), BloodCancer.Tier.Tier3));
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
    private final DecimalFormat df = new DecimalFormat("#.##");
    private List<AlisVariantResult> map(BloodCancer.Variant v, int i, BloodCancer.Tier tier) {
        List<AlisVariantResult> values = new LinkedList<>();
        values.add(AlisVariantResult.builder().row(i).key("Gene").value(v.gene()).build());
        values.add(AlisVariantResult.builder().row(i).key("DNA").value(v.hgvsc()).build());
        values.add(AlisVariantResult.builder().row(i).key("Protein").value(v.hgvsp()).build());
        values.add(AlisVariantResult.builder().row(i).key("VAF").value(df.format(v.vaf())).build());
        values.add(AlisVariantResult.builder().row(i).key("Depth").value(NumberFormat.getInstance().format(v.depth())).build());
        values.add(AlisVariantResult.builder().row(i).key("COSMIC ID").value(v.cosmic()).build());
        values.add(AlisVariantResult.builder().row(i).key("Tier 구분").value(String.valueOf(tier.ordinal()+1)).build());
        return values;
    }
}

