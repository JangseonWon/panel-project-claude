package com.greencross.lims.report.enus;

import com.greencross.lims.report.Revisionable;

public interface RevisionableEnUs extends Revisionable {
    @Override
    default String revisionComment() {
        return "* This is the result of the revised report.";
    }
}
