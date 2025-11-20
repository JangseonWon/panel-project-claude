package com.greencross.lims.variant.data;

import com.greencross.lims.entity.Analysis;
import lombok.experimental.UtilityClass;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
public class IdGenerator {
    public String snvId(Map<String, String> map) {
        return map.get("reference") + ":" + chrom(map.get("chrom")) + ":"
                + String.format("%09d", Long.parseLong(map.get("pos"))) + ":"
                + map.get("ref") + ":" + map.get("alt");
    }
    public String depthId(Map<String, String> map) {
        return map.get("reference") + ":" +  chrom(map.get("chrom")) + ":"
                       + String.format("%09d", Long.parseLong(map.get("start"))) + ":"
                       + String.format("%09d", Long.parseLong(map.get("end")));
    }
    public String analysisId(Analysis analysis) {
        return analysis.pk().sheet() + ":" + analysis.pk().batch() + ":" + String.format("%03d", analysis.pk().row());
    }
    private final Pattern PATTERN_CHR = Pattern.compile("^(?i)(chr)*((\\d+)||(\\w+))$");
    public String chrom(String chr) {
        if(chr == null || chr.trim().isEmpty()) return null;
        Matcher m = PATTERN_CHR.matcher(chr.trim());
        if(m.find()) {
            if(m.group(3)!=null) return String.format("%02d", Integer.parseInt(m.group(3)));
            else return m.group(2);
        } else return null;
    }
}
