package com.greencross.lims.report;

import com.gcgenome.lims.report.Template;
import com.gcgenome.lims.report.TextBlock;
import com.gcgenome.lims.report.TextStyle;
import com.gcgenome.lims.report.func.PDPageContentStreamPageAccessible;
import com.gcgenome.lims.report.func.Painter;
import com.gcgenome.lims.test.genomescreen.TestInfo;
import com.gcgenome.lims.test.genomescreen.TestWithRiskScreen;
import com.gcgenome.lims.test.wes.TestWithSingleInfo;
import com.greencross.lims.report.builder.AbstractReportDto;
import com.greencross.lims.report.builder.LogoType;

import java.awt.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

import static com.gcgenome.lims.report.func.AlignHorizontal.CENTER;
import static com.gcgenome.lims.report.func.AlignVertical.MIDDLE;

public class SectionLDT<T extends Template<?>, D extends AbstractReportDto&HasServiceCode> implements Painter<T, D> {
    private final static String LDT_TYPE1 = "※ This test was developed and its performance characteristics determined by GC Labs.";
    private final static String LDT_TYPE2 = "※ 이 검사는 GC 지놈에서 자체 개발한 검사(Laboratory-developed Test, LDT)로 적절한 평가를 통해 임상 검사에 적합함을 확인하였습니다.";
    private final static String LDT_TYPE3 = "※ 이 검사는 GC 지놈에서 자체 개발한 검사(Laboratory-developed Test, LDT)로 적절한 평가를 통해 성능을 확인하였습니다.";
    private final static String LDT_TYPE4 = "※ The test has been developed by GC Genome (Laboratory - developted Test, LDT) and it has been deemed suitable for clinical testing through appropriate evaluation. This report contains the results of the genetic analysis.";
    private final static String LDT_ENUS = "※ This test was developed and its performance characteristics determined by GC Genome. It has not been cleared or approved by the Korean Ministry of Food and Drug Safety (MFDS).";
    private final static String LDT_GCLP = "※ This test was developed and its performance characteristics determined by GC Genome";
    private final LogoType type;
    private final float y;
    public SectionLDT(LogoType type, float y) {
        this.type = type; this.y = y;
    }
    @Override
    public PDPageContentStreamPageAccessible paint(PDPageContentStreamPageAccessible stream, T template, D dto) throws IOException {
        String text = type(dto.code(), dto);
        if(text == null) return stream;
        stream.saveGraphicsState();
        TextStyle ts = new TextStyle().color(Color.decode("#808080")).fontSize(8);
        stream.paragraph(297.5f, y, 500, CENTER, MIDDLE, new TextBlock(ts, text));
        stream.restoreGraphicsState();
        return stream;
    }
    private String type(String code, D dto) {
        if(Arrays.stream(com.gcgenome.lims.test.bloodcancer.TestInfo.TESTS).filter(test->test.i18n().equalsIgnoreCase("KOKR")).anyMatch(test->test.code().equalsIgnoreCase(code))) {
            if(type == LogoType.DEPENDENT) return LDT_TYPE1;
            else return LDT_TYPE2;
        } else if(Arrays.stream(com.gcgenome.lims.test.bloodcancer.TestInfo.TESTS).filter(test->test.i18n().equalsIgnoreCase("ENUS")).anyMatch(test->test.code().equalsIgnoreCase(code))) return LDT_TYPE4;

        else if(Arrays.stream(com.gcgenome.lims.test.dgs.TestInfo.TESTS).filter(test->test.i18n().equalsIgnoreCase("KOKR")).anyMatch(test->test.code().equalsIgnoreCase(code))) {
            if(type == LogoType.DEPENDENT) return LDT_TYPE1;
            else return LDT_TYPE2;
        }
        else if(Arrays.stream(com.gcgenome.lims.test.dgs.TestInfo.TESTS).filter(test->test.i18n().equalsIgnoreCase("ENUS")).anyMatch(test->test.code().equalsIgnoreCase(code))) return LDT_ENUS;

        else if(Arrays.stream(com.gcgenome.lims.test.geneplus.TestInfo.TESTS_BRCA).anyMatch(test->test.code().equalsIgnoreCase(code))) {
            if(type == LogoType.DEPENDENT) return LDT_TYPE1;
            else return LDT_TYPE2;
        }
        else if(Arrays.stream(com.gcgenome.lims.test.geneplus.TestInfo.TESTS_ETC).anyMatch(test->test.code().equalsIgnoreCase(code))) {
            if(type == LogoType.DEPENDENT) return LDT_TYPE1;
            else return LDT_TYPE2;
        }

        else if(Arrays.stream(TestInfo.TESTS).filter(Objects::nonNull).filter(test->test.i18n().equalsIgnoreCase("KOKR")).anyMatch(test->test.code().equalsIgnoreCase(code))) {
            if(type == LogoType.DEPENDENT) return LDT_TYPE1;
            else return LDT_TYPE3;
        }
        else if(Arrays.stream(TestInfo.TESTS).filter(Objects::nonNull).filter(test->test.i18n().equalsIgnoreCase("ENUS")).anyMatch(test->test.code().equalsIgnoreCase(code))) return LDT_ENUS;
        else if(Arrays.stream(TestWithRiskScreen.TESTS).filter(test->test.i18n().equalsIgnoreCase("KOKR")).anyMatch(test->test.code().equalsIgnoreCase(code))) {
            if(type == LogoType.DEPENDENT) return LDT_TYPE1;
            else return LDT_TYPE3;
        }
        else if(Arrays.stream(com.gcgenome.lims.test.hrd.TestInfo.TESTS).filter(test->test.i18n().equalsIgnoreCase("KOKR")).anyMatch(test->test.code().equalsIgnoreCase(code))) {
            if(type == LogoType.DEPENDENT) return LDT_TYPE1;
            else return LDT_TYPE2;
        } else if(Arrays.stream(com.gcgenome.lims.test.hrd.TestInfo.TESTS).filter(test->test.i18n().equalsIgnoreCase("ENUS")).anyMatch(test->test.code().equalsIgnoreCase(code))) return LDT_ENUS;

        // else if(Arrays.stream(com.greencross.lims.test.mrd.TestInfo.SCREEN_TESTS).anyMatch(test->test.code().equalsIgnoreCase(code))) return LDT_TYPE2;
        // else if(Arrays.stream(com.greencross.lims.test.mrd.TestInfo.TESTS).anyMatch(test->test.code().equalsIgnoreCase(code))) return LDT_TYPE2;

        else if(Arrays.stream(com.gcgenome.lims.test.panel.TestInfo.TESTS).filter(test->test.i18n().equalsIgnoreCase("KOKR")).anyMatch(test->test.code().equalsIgnoreCase(code))) {
            if(type == LogoType.DEPENDENT) return LDT_TYPE1;
            else if(code.length() > 5) return LDT_GCLP;        // 연구검사
            else if(dto.medicalInstitution().contains("정도관리")) return LDT_TYPE3;
            else return LDT_TYPE2;
        }
        else if(Arrays.stream(com.gcgenome.lims.test.panel.TestInfo.TESTS).filter(test->test.i18n().equalsIgnoreCase("ENUS")).anyMatch(test->test.code().equalsIgnoreCase(code))) return LDT_ENUS;

        else if(Arrays.stream(com.gcgenome.lims.test.sanger.TestInfo.TESTS).filter(com.gcgenome.lims.test.sanger.TestInfo::isNationalInsuranceTest).anyMatch(test->test.code().equalsIgnoreCase(code))) {
            if(type == LogoType.DEPENDENT) return LDT_TYPE1;
            else return LDT_TYPE2;
        }
        else if(Arrays.stream(com.gcgenome.lims.test.sanger.TestInfo.TESTS).filter(test->test.i18n().equalsIgnoreCase("KOKR")).anyMatch(test->test.code().equalsIgnoreCase(code))) {
            if(type == LogoType.DEPENDENT) return LDT_TYPE1;
            else return LDT_TYPE2;
        }
        else if(Arrays.stream(com.gcgenome.lims.test.sanger.TestInfo.TESTS).filter(test->test.i18n().equalsIgnoreCase("ENUS")).anyMatch(test->test.code().equalsIgnoreCase(code))) return LDT_ENUS;

        else if(Arrays.stream(com.gcgenome.lims.test.single.TestInfo.TESTS).anyMatch(test->test.code().equalsIgnoreCase(code))) {
            if(type == LogoType.DEPENDENT) return LDT_TYPE1;
            var test = Arrays.stream(com.gcgenome.lims.test.single.TestInfo.TESTS).filter(i->i.code().equalsIgnoreCase(code)).findFirst().get();
            if(test.isNationalInsuranceTest()) return LDT_TYPE2;
            else return LDT_TYPE3;
        }

        else if(Arrays.stream(com.gcgenome.lims.test.solidtumor2.TestInfo.TESTS).anyMatch(test->test.code().equalsIgnoreCase(code))) return null;

        else if(Arrays.stream(com.gcgenome.lims.test.wes.TestInfo.TESTS).filter(test->test.i18n().equalsIgnoreCase("KOKR")).anyMatch(test->test.code().equalsIgnoreCase(code))) {
            if(type == LogoType.DEPENDENT) return LDT_TYPE1;
            else return LDT_TYPE2;
        }
        else if(Arrays.stream(com.gcgenome.lims.test.wes.TestInfo.TESTS).filter(test->test.i18n().equalsIgnoreCase("ENUS")).anyMatch(test->test.code().equalsIgnoreCase(code))) return LDT_ENUS;
        else if(Arrays.stream(TestWithSingleInfo.TESTS).filter(test->test.i18n().equalsIgnoreCase("KOKR")).anyMatch(test->test.code().equalsIgnoreCase(code))) {
            if(type == LogoType.DEPENDENT) return LDT_TYPE1;
            else return LDT_TYPE2;
        } else if(Arrays.stream(TestWithSingleInfo.TESTS).filter(test->test.i18n().equalsIgnoreCase("ENUS")).anyMatch(test->test.code().equalsIgnoreCase(code))) return LDT_ENUS;
        else if("S051".equalsIgnoreCase(code)) {
            if(type == LogoType.DEPENDENT) return LDT_TYPE1;
            else if(type == LogoType.INDEPENDENT) return null;
            else throw new RuntimeException();
        }
        else if("N159".equalsIgnoreCase(code)) {
            if(type == LogoType.DEPENDENT) return LDT_TYPE1;
            else return LDT_TYPE2;
        }
        else return null;
    }
}
