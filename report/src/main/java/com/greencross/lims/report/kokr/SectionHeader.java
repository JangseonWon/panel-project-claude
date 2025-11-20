package com.greencross.lims.report.kokr;

import com.gcgenome.lims.report.Template;
import com.gcgenome.lims.report.TextBlock;
import com.gcgenome.lims.report.TextStyle;
import com.gcgenome.lims.report.func.PDPageContentStreamPageAccessible;
import com.gcgenome.lims.report.func.Painter;
import com.greencross.lims.report.builder.AbstractReportDto;
import com.greencross.lims.report.builder.Sex;
import com.greencross.lims.report.builder.Util;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;

import static com.gcgenome.lims.report.func.AlignVertical.MIDDLE;

public class SectionHeader<T extends Template<? extends HasHeaderKoKr>, D extends AbstractReportDto> implements Painter<T, D> {
    private static final DateTimeFormatter DTF 			= DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final String lblMedicalInstitution			= "의뢰기관";
    private final String lblMedicalRecordNumber			= "등록번호";
    private final String lblRequestNumber				= "접수번호";
    private final String lblPatientName					= "성명";
    private final String lblPatientCode					= "주민번호";
    private final String lblAgeSex						= "나이/성별";
    private final String lblSpecimenType				= "검체종류";
    private final String lblWardDepartment				= "병동/진료과";
    private final String lblCollectionDate				= "검체채취일";
    private final String lblPatientInfo					= "임상정보/기타";
    private final String lblPhysician					= "주치의";
    private final String lblReceiptReportDate			= "접수일/보고일";

    @Override
    public PDPageContentStreamPageAccessible paint(PDPageContentStreamPageAccessible stream, T template, D dto) throws IOException {
        stream.saveGraphicsState();
        var resource = template.resource();
        var colorGray = resource.colorGray();
        var styleHeaderTitle = new TextStyle().color(resource.colorText()).fonts(resource.fontHeader(), resource.fontDefault()).fontSize(9).justify(true).paragraph(false);
        var styleValueTitle = new TextStyle().color(resource.colorText()).fonts(resource.fontHeaderValue(), resource.fontDefault()).fontSize(7).paragraph(false);
        float y = stream.cursorY();
        stream.setNonStrokingColor(colorGray)
                .setLineWidth(0.25f).setStrokingColor(colorGray)
                .addRect(60, y,70, -63)
                .addRect(216, y,70, -63)
                .addRect(374, y,70, -63)
                .fill();
        y -= 10;

        stream.line(60, y-5, 533, y-5).stroke();
        stream.paragraph(62, y, 65, new TextBlock(styleHeaderTitle, lblMedicalInstitution));
        stream.paragraph(219, y, 65, new TextBlock(styleHeaderTitle, lblMedicalRecordNumber));
        stream.paragraph(377, y, 65, new TextBlock(styleHeaderTitle, lblRequestNumber));
        stream.paragraph(135, y+2.5f, 83, MIDDLE, new TextBlock(styleValueTitle, Util.dashIfEmpty(dto.medicalInstitution())));
        stream.paragraph(290, y, 88, new TextBlock(styleValueTitle, Util.dashIfEmpty(dto.medicalRecordNumber())));
        y = stream.paragraph(447, y, 100, new TextBlock(styleValueTitle, Util.dashIfEmpty(dto.requestNumber())));
        y -= Math.max(styleHeaderTitle.fontSize(), styleValueTitle.fontSize()) * 1.8f;

        stream.paragraph(62, y, 65, new TextBlock(styleHeaderTitle, lblPatientName));
        stream.paragraph(219, y, 65, new TextBlock(styleHeaderTitle, lblPatientCode));
        stream.paragraph(377, y, 65, new TextBlock(styleHeaderTitle, lblAgeSex));
        stream.paragraph(135, y, 86, new TextBlock(styleValueTitle, Util.dashIfEmpty(dto.patientName())));
        stream.paragraph(290, y, 88, new TextBlock(styleValueTitle, Util.dashIfEmpty(dto.patientCode())));
        y = stream.paragraph(447, y, 100,
                new TextBlock(styleValueTitle, Util.dashIfEmpty(age(dto.age(), dto.birthDate(), dto.collectionDate()))),
                new TextBlock(styleValueTitle, " / "),
                new TextBlock(styleValueTitle, Util.dashIfEmpty(sex(dto.sex()))));
        stream.line(60, y-5, 533, y-5);
        y -= Math.max(styleHeaderTitle.fontSize(), styleValueTitle.fontSize()) * 1.8f;

        stream.paragraph(62, y, 65, new TextBlock(styleHeaderTitle, lblSpecimenType));
        stream.paragraph(219, y, 65, new TextBlock(styleHeaderTitle, lblWardDepartment));
        stream.paragraph(377, y, 65, new TextBlock(styleHeaderTitle, lblCollectionDate));
        stream.paragraph(135, y, 86, new TextBlock(styleValueTitle, Util.dashIfEmpty(dto.specimenType())));
        stream.paragraph(290, y, 88,
                new TextBlock(styleValueTitle, Util.dashIfEmpty(dto.ward())),
                new TextBlock(styleValueTitle, " / "),
                new TextBlock(styleValueTitle, Util.dashIfEmpty(dto.department())));
        y = stream.paragraph(447, y, 100, new TextBlock(styleValueTitle, Util.dashIfEmpty(date(dto.collectionDate()))));
        stream.line(60, y-5, 533, y-5);
        y -= Math.max(styleHeaderTitle.fontSize(), styleValueTitle.fontSize()) * 1.8f;

        stream.paragraph(62, y, 65, new TextBlock(styleHeaderTitle, lblPatientInfo));
        stream.paragraph(219, y, 65, new TextBlock(styleHeaderTitle, lblPhysician));
        stream.paragraph(377, y, 65, new TextBlock(styleHeaderTitle, lblReceiptReportDate));
        stream.paragraph(135, y, 86, new TextBlock(styleValueTitle, Util.dashIfEmpty(dto.patientInfo())));
        stream.paragraph(290, y, 88, new TextBlock(styleValueTitle, Util.dashIfEmpty(dto.physician())));
        y = stream.paragraph(447, y, 100,
                new TextBlock(styleValueTitle, Util.dashIfEmpty(date(dto.receiptDate()))),
                new TextBlock(styleValueTitle, " / "),
                new TextBlock(styleValueTitle, Util.dashIfEmpty(date(dto.reportDate()))));
        stream.line(60, y-5, 533, y-5).stroke();
        stream.restoreGraphicsState();
        stream.cursorY(y);
        return stream;
    }

    public String date(LocalDate date) {
        if(date == null) return null;
        return DTF.format(date);
    }
    public String date(LocalDateTime date) {
        if(date == null) return null;
        return DTF.format(date);
    }
    public final String age(Integer age, LocalDate birth, LocalDate sampling) {
        if(age!=null) return String.valueOf(age);
        if(birth == null)	 return "-";
        LocalDate target = sampling;
        if(sampling == null) target = LocalDate.now();
        return String.valueOf(Period.between(birth, target).getYears());
    }
    public final String sex(Sex sex) {
        if(sex == null) return "-";
        switch(sex) {
            case M: return "남";
            case F: return "여";
            default: return "-";
        }
    }
}
