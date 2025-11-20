package com.greencross.lims.report;

import com.gcgenome.report.versions.TableInfo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ReportConfig {
    @Bean
    public TableInfo tableInfo(){
        return new TableInfo("panel.report","sample","service","publish_at","description","file");
    }
}
