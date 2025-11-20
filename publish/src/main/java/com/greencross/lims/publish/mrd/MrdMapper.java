package com.greencross.lims.publish.mrd;

import com.gcgenome.lims.test.mrd.TestInfo;
import com.greencross.alis.api.AlisResult;
import com.greencross.lims.dto.interpretation.Mrd;
import com.greencross.lims.entity.Report;
import com.greencross.lims.entity.Request;
import com.greencross.lims.publish.AlisMapper;
import com.greencross.lims.publish.ReportDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class MrdMapper implements AlisMapper<Mrd> {
    private final ReportDAO reportDAO;
    private final NumberFormat fmtPct = NumberFormat.getInstance();

    @Override
    public Class<Mrd> clazz() {
        return Mrd.class;
    }

    @Override
    public boolean match(Request request) {
        String service = request.pk().service();
        return Arrays.stream(TestInfo.TESTS).anyMatch(m->m.code().equalsIgnoreCase(service));
    }

    @Override
    public AlisResult[] map(Request request, Mrd interpretation, Class clazz) {
        String code = request.pk().service();
        if(interpretation.histories()!=null) {
            if ("N150".equals(code)) return n150(interpretation);
            if ("N151".equals(code)) return n151(interpretation);
            if ("N152".equals(code)) return n152(interpretation);
        }
        return new AlisResult[0];
    }

    public AlisResult[] n150(Mrd interpretation) {
        Mrd.MrdHistory top = interpretation.last();
        Mrd.MrdGeneResult igh = Arrays.stream(top.results()).filter(r->"IGH".equalsIgnoreCase(r.gene())).findAny().get();
        String result = toString(igh.result());
        String comment = tmplInterpretation(igh.result()).replace("%g", "IGH");
        return new AlisResult[]{
                AlisResult.builder().subCode("N150010").result1(result).build(),
                AlisResult.builder().subCode("N150020").result1(fmtPct.format(igh.pctClonalBCells()*100)).build(),
                AlisResult.builder().subCode("N150030").result1(fmtPct.format(top.pctClonalNucelatedCells("IGH")*100)).build(),
                AlisResult.builder().subCode("N150040").text(comment).build(),
                AlisResult.builder().subCode("N150050").result1(String.valueOf(top.inputDna())).build(),
                AlisResult.builder().subCode("N150060").result1(String.valueOf(igh.target().readDepth())).build(),
                AlisResult.builder().subCode("N150070").result1(String.valueOf(top.nucleatedCells())).build(),
                AlisResult.builder().subCode("N150080").result1(String.valueOf(igh.bCells())).build()
        };
    }
    public AlisResult[] n151(Mrd interpretation) {
        Mrd.MrdHistory top = interpretation.last();
        Mrd.MrdGeneResult igh = Arrays.stream(top.results()).filter(r->"IGH".equalsIgnoreCase(r.gene())).findAny().get();
        Mrd.MrdGeneResult igk = Arrays.stream(top.results()).filter(r->"IGK".equalsIgnoreCase(r.gene())).findAny().get();
        String result1 = toString(igh.result());
        String comment1 = tmplInterpretation(igh.result()).replace("%g", "IGH");
        String result2 = toString(igk.result());
        String comment2 = tmplInterpretation(igk.result()).replace("%g", "IGK");
        return new AlisResult[]{
                AlisResult.builder().subCode("N151010").result1(result1).build(),
                AlisResult.builder().subCode("N151020").result1(fmtPct.format(igh.pctClonalBCells()*100)).build(),
                AlisResult.builder().subCode("N151030").result1(fmtPct.format(top.pctClonalNucelatedCells("IGH")*100)).build(),
                AlisResult.builder().subCode("N151040").result1(result2).build(),
                AlisResult.builder().subCode("N151050").result1(fmtPct.format(igk.pctClonalBCells()*100)).build(),
                AlisResult.builder().subCode("N151060").result1(fmtPct.format(top.pctClonalNucelatedCells("IGK")*100)).build(),
                AlisResult.builder().subCode("N151070").text(comment1).build(),
                AlisResult.builder().subCode("N151080").text(comment2).build(),
                AlisResult.builder().subCode("N151090").result1(String.valueOf(top.inputDna())).build(),
                AlisResult.builder().subCode("N151100").result1(String.valueOf(igh.target().readDepth())).build(),
                AlisResult.builder().subCode("N151110").result1(String.valueOf(igk.target().readDepth())).build(),
                AlisResult.builder().subCode("N151120").result1(String.valueOf(top.nucleatedCells())).build(),
                AlisResult.builder().subCode("N151130").result1(String.valueOf(igh.bCells())).build(),
                AlisResult.builder().subCode("N151140").result1(String.valueOf(igk.bCells())).build()
        };
    }
    public AlisResult[] n152(Mrd interpretation) {
        Mrd.MrdHistory top = interpretation.last();
        Mrd.MrdGeneResult trb = Arrays.stream(top.results()).filter(r->"TRB".equalsIgnoreCase(r.gene())).findAny().get();
        Mrd.MrdGeneResult trg = Arrays.stream(top.results()).filter(r->"TRG".equalsIgnoreCase(r.gene())).findAny().get();
        String result1 = toString(trb.result());
        String comment1 = tmplInterpretation(trb.result()).replace("%g", "TRB");
        String result2 = toString(trg.result());
        String comment2 =  tmplInterpretation(trg.result()).replace("%g", "TRG");
        return new AlisResult[]{
                AlisResult.builder().subCode("N152010").result1(result1).build(),
                AlisResult.builder().subCode("N152020").result1(fmtPct.format(trb.pctClonalBCells()*100)).build(),
                AlisResult.builder().subCode("N152030").result1(fmtPct.format(top.pctClonalNucelatedCells("TRB")*100)).build(),
                AlisResult.builder().subCode("N152040").result1(result2).build(),
                AlisResult.builder().subCode("N152050").result1(fmtPct.format(trg.pctClonalBCells()*100)).build(),
                AlisResult.builder().subCode("N152060").result1(fmtPct.format(top.pctClonalNucelatedCells("TRG")*100)).build(),
                AlisResult.builder().subCode("N152070").text(comment1).build(),
                AlisResult.builder().subCode("N152080").text(comment2).build(),
                AlisResult.builder().subCode("N152090").result1(String.valueOf(top.inputDna())).build(),
                AlisResult.builder().subCode("N152100").result1(String.valueOf(trb.target().readDepth())).build(),
                AlisResult.builder().subCode("N152110").result1(String.valueOf(trg.target().readDepth())).build(),
                AlisResult.builder().subCode("N152120").result1(String.valueOf(top.nucleatedCells())).build(),
                AlisResult.builder().subCode("N152130").result1(String.valueOf(trb.bCells())).build(),
                AlisResult.builder().subCode("N152140").result1(String.valueOf(trg.bCells())).build()
        };
    }
    private final String tmplInterpretationPositive		= "%g 클론 추적 관찰 결과 미세잔존질환이 확인되었습니다.";
    private final String tmplInterpretationNegative		= "%g 클론 추적 관찰 결과 미세잔존질환이 확인되지 않았습니다.";
    private final String tmplInterpretationNA		    = "See Interpretation.";
    private String toString(Mrd.Result result) {
        if(result == null) return "-";
        return switch (result) {
            case DETECTED       -> "Detected";
            case NOT_DETECTED   -> "Not Detected";
            case NA             -> "N/A";
            default             -> "-";
        };
    }
    private String tmplInterpretation(Mrd.Result result) {
        return switch (result) {
            case DETECTED       -> tmplInterpretationPositive;
            case NOT_DETECTED   -> tmplInterpretationNegative;
            case NA             -> tmplInterpretationNA;
            case CUSTOM         -> "-";
        };
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
