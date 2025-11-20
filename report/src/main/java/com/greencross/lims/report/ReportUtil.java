package com.greencross.lims.report;

import com.gcgenome.lims.test.I18N;
import com.greencross.lims.entity.Patient;
import com.greencross.lims.entity.Request;
import com.greencross.lims.entity.Sample;
import com.greencross.lims.trans.Formatter;

import java.util.Objects;

public class ReportUtil {
    public static String determineMedicalInstitution(I18N i18nSource, Request request, Patient patient) {
        String primaryCustomerName = Objects.isNull(patient.customerName2()) ? patient.customerName() : patient.customerName2();

        return isEnglishNameRequiredForDomesticCodes(i18nSource.i18n(), request) ? request.ward() : primaryCustomerName ;
    }
    public static String determineRequestNumber(Sample sample) {
        if (isLabsRequest(sample) && sample.remark() != null) {
            String sanitized = sample.remark().replace("-", "");
            return Formatter.formatSampleId(Long.parseLong(sanitized));
        }
        return Formatter.formatSampleId(sample.id());
    }
    private static boolean isEnglishNameRequiredForDomesticCodes(String i18n, Request request) {
        return "ENUS".equalsIgnoreCase(i18n) && Objects.nonNull(request.ward());
    }
    public static boolean isLabsRequest(Sample sample) {
        return  Objects.nonNull(sample.patient().customerName2()) && sample.patient().customerName().equalsIgnoreCase("Gclabs");
    }
}
