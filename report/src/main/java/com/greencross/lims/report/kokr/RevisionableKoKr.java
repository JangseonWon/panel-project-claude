package com.greencross.lims.report.kokr;

import com.greencross.lims.report.Revisionable;

public interface RevisionableKoKr extends Revisionable {
    @Override
    default String revisionComment() {
        return "* 수정보고 결과입니다.";
    }
}
