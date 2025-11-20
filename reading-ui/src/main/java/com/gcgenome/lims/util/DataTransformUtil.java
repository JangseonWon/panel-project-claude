package com.gcgenome.lims.util;

import com.google.gwt.i18n.client.DateTimeFormat;
import lombok.experimental.UtilityClass;

import java.util.Date;

@UtilityClass
public class DataTransformUtil {
    public String formatSampleId(Long id) {
        if(id == null) return null;
        return formatSampleId(String.valueOf(id));
    }
    public String formatSampleId(String id) {
        if(id == null) return null;
        if(id.length() ==15) return id.substring(0, 8) + "-" + id.substring(8, 11) + "-" + id.substring(11);
        return id;
    }
    private final DateTimeFormat DEFAULT_DATE_FORMAT = DateTimeFormat.getFormat("yyyy-MM-dd");
    public String formatDate(Long epoch) {
        if(epoch == null) return null;
        return DEFAULT_DATE_FORMAT.format(new Date(epoch));
    }

    private final DateTimeFormat DEFAULT_DATETIME_FORMAT = DateTimeFormat.getFormat("yyyy-MM-dd HH:mm:ss");
    public String formatDateTime(Long epoch) {
        return epoch == null ? null : DEFAULT_DATETIME_FORMAT.format(new Date(epoch));
    }
}
