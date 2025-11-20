package com.gcgenome.lims.util;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import lombok.experimental.UtilityClass;

import java.util.Date;

@UtilityClass
public class DataTransformUtil {
    private final DateTimeFormat DEFAULT_DATETIME_FORMAT = DateTimeFormat.getFormat("yyyy-MM-dd HH:mm:ss");
    public String formatDateTime(Long epoch) {
        return epoch == null ? null : DEFAULT_DATETIME_FORMAT.format(new Date(epoch));
    }
    public static String formatPrecision(Double value, int precision) {
        if(value == null) return null;
        StringBuilder sb = new StringBuilder("0.");
        for(int i = 0; i < precision; ++i) sb.append("0");
        NumberFormat nf = NumberFormat.getFormat(sb.toString());
        return nf.format(value);
    }
}
