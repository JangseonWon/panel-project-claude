package com.greencross.lims.publish.mrd;

import com.gcgenome.lims.test.mrd.TestInfo;
import com.greencross.alis.api.AlisResult;
import com.greencross.alis.api.AlisVariantResult;
import com.greencross.lims.dto.interpretation.MrdScreen;
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
public class MrdScreenMapper implements AlisMapper<MrdScreen> {
    private final ReportDAO reportDAO;
    private final NumberFormat fmtPct = NumberFormat.getInstance();
    {
        fmtPct.setMaximumFractionDigits(2);
        fmtPct.setMinimumFractionDigits(2);
    }
    @Override
    public Class<MrdScreen> clazz() {
        return MrdScreen.class;
    }

    @Override
    public boolean match(Request request) {
        String service = request.pk().service();
        return Arrays.stream(TestInfo.SCREEN_TESTS).anyMatch(m->m.code().equalsIgnoreCase(service));
    }

    @Override
    public AlisResult[] map(Request request, MrdScreen interpretation, Class clazz) {
        String code = request.pk().service();
        if("N144".equals(code)) return n144(interpretation);
        if("N145".equals(code)) return n145(interpretation);
        if("N146".equals(code)) return n146(interpretation);
        return null;
    }
    @Override
    public AlisVariantResult[] variants(Request request, MrdScreen interpretation, Class<MrdScreen> clazz) {
        List<AlisVariantResult> values = new LinkedList<>();
        int n = 0;
        if(interpretation.results()!=null) {
            for(int i = 0; i < interpretation.results().length; ++i) {
                if(interpretation.results()[i]==null) continue;
                String gene = interpretation.results()[i].gene();
                long depth = interpretation.results()[i].depthLqic();
                long bCells = interpretation.results()[i].bCells();
                if(interpretation.results()[i].clones()!=null)
                    for(int j = 0; j < interpretation.results()[i].clones().length; ++j) {
                        MrdScreen.MrdScreenCloneResult v = interpretation.results()[i].clones()[j];
                        values.addAll(map(gene, v, depth, bCells, n++));
                    }
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

    private List<AlisVariantResult> map(String gene, MrdScreen.MrdScreenCloneResult v, long depthLqic, long bCells, int i) {
        List<AlisVariantResult> values = new LinkedList<>();
        values.add(AlisVariantResult.builder().row(i).key("Gene").value(gene).build());
        values.add(AlisVariantResult.builder().row(i).key("V Region").value(v.regionV()).build());
        values.add(AlisVariantResult.builder().row(i).key("J Region").value(v.regionJ()).build());
        values.add(AlisVariantResult.builder().row(i).key("Length(bp)").value(NumberFormat.getInstance().format(v.length())).build());
        values.add(AlisVariantResult.builder().row(i).key("Clonal Read Depth").value(NumberFormat.getInstance().format(v.depth())).build());
        values.add(AlisVariantResult.builder().row(i).key("Clonal Cell Equivalent").value(NumberFormat.getInstance().format(v.equivalent(depthLqic))).build());
        values.add(AlisVariantResult.builder().row(i).key("Clonal/Total B-cells").value(NumberFormat.getInstance().format(v.coverage(depthLqic, bCells))).build());
        values.add(AlisVariantResult.builder().row(i).key("Sequence").value(v.sequence()).build());
        return values;
    }

    public AlisResult[] n144(MrdScreen interpretation) {
        MrdScreen.MrdScreenGeneResult igh = Arrays.stream(interpretation.results()).filter(r->"IGH".equalsIgnoreCase(r.gene())).findAny().get();
        String comment = "미세잔존질환(MRD) 추적에 적합한 IGH 클론이 %d개 검출되었습니다.".replace("%d", String.valueOf(igh.clones()!=null?igh.clones().length:0));
        return new AlisResult[]{
                AlisResult.builder().subCode("N144010").text(interpretation.cancerType()).build(),
                AlisResult.builder().subCode("N144020").result1(String.valueOf(igh.clones()!=null?igh.clones().length:0)).build(),
                AlisResult.builder().subCode("N144022").result1(fmtPct.format(igh.totalClonalCells())).build(),
                AlisResult.builder().subCode("N144030").text(String.valueOf(igh.clones()!=null?igh.clones().length:0)).build(),
                AlisResult.builder().subCode("N144040").text(comment).build(),
                AlisResult.builder().subCode("N144050").result1(NumberFormat.getInstance().format(interpretation.inputDna())).build(),
                AlisResult.builder().subCode("N144060").result1(NumberFormat.getInstance().format(igh.depthTotal())).build(),
                AlisResult.builder().subCode("N144070").result1(NumberFormat.getInstance().format(interpretation.nucleatedCells())).build(),
                AlisResult.builder().subCode("N144080").result1(NumberFormat.getInstance().format(igh.bCells())).build()
        };
    }
    public AlisResult[] n145(MrdScreen interpretation) {
        MrdScreen.MrdScreenGeneResult igh = Arrays.stream(interpretation.results()).filter(r->"IGH".equalsIgnoreCase(r.gene())).findAny().get();
        MrdScreen.MrdScreenGeneResult igk = Arrays.stream(interpretation.results()).filter(r->"IGK".equalsIgnoreCase(r.gene())).findAny().get();
        String comment1 = "미세잔존질환(MRD) 추적에 적합한 IGH 클론이 %d개 검출되었습니다.".replace("%d", String.valueOf(igh.clones()!=null?igh.clones().length:0));
        String comment2 = "미세잔존질환(MRD) 추적에 적합한 IGK 클론이 %d개 검출되었습니다.".replace("%d", String.valueOf(igk.clones()!=null?igk.clones().length:0));
        return new AlisResult[]{
                AlisResult.builder().subCode("N145010").text(interpretation.cancerType()).build(),
                AlisResult.builder().subCode("N145020").result1(String.valueOf(igh.clones()!=null?igh.clones().length:0)).build(),
                AlisResult.builder().subCode("N145022").result1(fmtPct.format(igh.totalClonalCells())).build(),
                AlisResult.builder().subCode("N145030").result1(String.valueOf(igk.clones()!=null?igk.clones().length:0)).build(),
                AlisResult.builder().subCode("N145031").result1(fmtPct.format(igk.totalClonalCells())).build(),
                AlisResult.builder().subCode("N145040").text(String.valueOf(igh.clones()!=null?igh.clones().length:0)).build(),
                AlisResult.builder().subCode("N145045").text(comment1).build(),
                AlisResult.builder().subCode("N145050").text(String.valueOf(igk.clones()!=null?igk.clones().length:0)).build(),
                AlisResult.builder().subCode("N145055").text(comment2).build(),
                AlisResult.builder().subCode("N145060").result1(NumberFormat.getInstance().format(interpretation.inputDna())).build(),
                AlisResult.builder().subCode("N145070").result1(NumberFormat.getInstance().format(igh.depthTotal())).build(),
                AlisResult.builder().subCode("N145075").result1(NumberFormat.getInstance().format(igk.depthTotal())).build(),
                AlisResult.builder().subCode("N145080").result1(NumberFormat.getInstance().format(interpretation.nucleatedCells())).build(),
                AlisResult.builder().subCode("N145090").result1(NumberFormat.getInstance().format(igh.bCells())).build(),
                AlisResult.builder().subCode("N145095").result1(NumberFormat.getInstance().format(igk.bCells())).build()
        };
    }
    public AlisResult[] n146(MrdScreen interpretation) {
        MrdScreen.MrdScreenGeneResult trb = Arrays.stream(interpretation.results()).filter(r->"TRB".equalsIgnoreCase(r.gene())).findAny().get();
        MrdScreen.MrdScreenGeneResult trg = Arrays.stream(interpretation.results()).filter(r->"TRG".equalsIgnoreCase(r.gene())).findAny().get();
        String comment1 = "미세잔존질환(MRD) 추적에 적합한 TRB 클론이 %d개 검출되었습니다.".replace("%d", String.valueOf(trb.clones()!=null?trb.clones().length:0));
        String comment2 = "미세잔존질환(MRD) 추적에 적합한 TRG 클론이 %d개 검출되었습니다.".replace("%d", String.valueOf(trg.clones()!=null?trg.clones().length:0));
        return new AlisResult[]{
                AlisResult.builder().subCode("N146010").text(interpretation.cancerType()).build(),
                AlisResult.builder().subCode("N146020").result1(String.valueOf(trb.clones()!=null?trb.clones().length:0)).build(),
                AlisResult.builder().subCode("N146022").result1(fmtPct.format(trb.totalClonalCells())).build(),
                AlisResult.builder().subCode("N146030").result1(String.valueOf(trg.clones()!=null?trg.clones().length:0)).build(),
                AlisResult.builder().subCode("N146031").result1(fmtPct.format(trg.totalClonalCells())).build(),
                AlisResult.builder().subCode("N146040").text(String.valueOf(trb.clones()!=null?trb.clones().length:0)).build(),
                AlisResult.builder().subCode("N146045").text(comment1).build(),
                AlisResult.builder().subCode("N146050").text(String.valueOf(trg.clones()!=null?trg.clones().length:0)).build(),
                AlisResult.builder().subCode("N146055").text(comment2).build(),
                AlisResult.builder().subCode("N146060").result1(NumberFormat.getInstance().format(interpretation.inputDna())).build(),
                AlisResult.builder().subCode("N146070").result1(NumberFormat.getInstance().format(trb.depthTotal())).build(),
                AlisResult.builder().subCode("N146075").result1(NumberFormat.getInstance().format(trg.depthTotal())).build(),
                AlisResult.builder().subCode("N146080").result1(NumberFormat.getInstance().format(interpretation.nucleatedCells())).build(),
                AlisResult.builder().subCode("N146090").result1(NumberFormat.getInstance().format(trb.bCells())).build(),
                AlisResult.builder().subCode("N146095").result1(NumberFormat.getInstance().format(trg.bCells())).build()
        };
    }

}
