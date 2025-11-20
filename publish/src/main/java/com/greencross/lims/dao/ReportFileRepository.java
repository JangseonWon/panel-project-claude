package com.greencross.lims.dao;

import com.greencross.lims.report.ReportFile;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ReportFileRepository extends CassandraRepositoryExtended<ReportFile, UUID> {

}
