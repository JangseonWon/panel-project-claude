package com.greencross.lims.report;

import com.gcgenome.lims.dto.Report;
import com.greencross.lims.trans.LocalDateTimeToEpoch;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ReportToDTO {
    public Report map(com.greencross.lims.entity.Report entity) {
        return new Report().sample(entity.pk().sample())
                .service(entity.pk().service())
                .createAt(LocalDateTimeToEpoch.map(entity.createAt()))
                .publishAt(entity.publishAt()!=null?LocalDateTimeToEpoch.map(entity.publishAt()):null)
                .fileName(entity.name())
                .fileSize(entity.size())
                .creator(entity.user()!=null?entity.user().name():null)
                .publisher(entity.lastModifiedBy()!=null?entity.lastModifiedBy().name():null)
                .description(entity.description())
                .longFormText(entity.longFormText())
                .shortFormText(entity.shortFormText())
                .fileUrl("/samples/" + entity.pk().sample() + "/services/" + entity.pk().service() + "/reports/" + LocalDateTimeToEpoch.map(entity.createAt()));
    }
}
