package com.greencross.lims;

import com.gcgenome.report.versions.log.ReportInfo;
import com.gcgenome.report.versions.pdf.Sex;
import com.gcgenome.report.versions.report.ReportInfoService;
import com.greencross.lims.entity.Patient;
import com.greencross.lims.entity.Request;
import com.greencross.lims.entity.Sample;
import com.greencross.lims.report.InterpretationDAO;
import com.greencross.lims.trans.Formatter;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Configuration
public class ReportVersionConfig {
    private static final Pattern BIRTHDAT_PATTERN = Pattern.compile("^\\d{6}$");
    private static final Pattern BIRTHDAT_PATTERN2 = Pattern.compile("^(\\d{6})(-)*(\\d)$");
    @Bean public ReportInfoService reportInfoService(InterpretationDAO interpretationDao) {
        return new ReportInfoService() {
            @NotNull @Override @Transactional
            public ReportInfo getReportInfo(long s, @NotNull String service) {
                Request request = request(s, service);
                Sample sample = request.sample();
                Patient patient = sample.patient();
                String medicalInstitutionName = patient.customerName();
                String requestNumber = Formatter.formatSampleId(sample.id());
                if(sample.remark()!=null && request.sample().patient().customerCode2()!=null) {
                    medicalInstitutionName = patient.customerName2();
                    requestNumber = Formatter.formatSampleId(Long.parseLong(sample.remark()));
                } // else if("ENUS".equalsIgnoreCase(test.i18n())) dto.medicalInstitution(request.ward()).requestNumber(Formatter.formatSampleId(sample.id()));
                String patientCode = patient.code();
                if(patient.code()!=null && BIRTHDAT_PATTERN.matcher(patient.code()).find()) patientCode = patient.code() + "-*******";
                else if(patient.code()!=null && BIRTHDAT_PATTERN2.matcher(patient.code()).find()) {
                    Matcher m = BIRTHDAT_PATTERN2.matcher(patient.code());
                    m.find();
                    patientCode = m.group(1) + "-" + m.group(3) + "******";
                }
                return new ReportInfo(
                        request.service().name(),
                        medicalInstitutionName, patient.mrn(),
                        requestNumber, patient.name(), patientCode,
                        patient.age(), patient.birth(),
                        patient.sex()== Patient.Sex.M? Sex.M : (
                                patient.sex()== Patient.Sex.F? Sex.F:null
                        ), sample.sampleType(), request.ward(),
                        request.customerDeptName(), request.dateSampling(),
                        request.info(), request.physician(), request.dateRequest(), LocalDate.now()
                );
            }
            private Request request(long sample, @NotNull String service) {
                return interpretationDao.em().find(Request.class, Request.RequestPK.builder().sample(sample).service(service).build());
            }
        };
    }
}
