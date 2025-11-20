package com.gcgenome.lims.client.interpretation;

import com.gcgenome.lims.dto.interpretation.Des;
import com.gcgenome.lims.dto.interpretation.GenomeScreen;
import com.gcgenome.lims.dto.interpretation.PanelTest;

public class PreviousObjectUtils {
    public static native boolean hasField(Object obj, String field) /*-{
        return field in obj;
    }-*/;
    public enum Type {
        PANEL_TEST,
        DES, // WesWithSingle은 WES의 IF를 incidentalFindings의 incidentalFindings 필드에 저장하고 있음.
        GENOME_SCREEN;
        public static Type detect(Object previous) {
            if (hasField(previous, "addendum")) return PANEL_TEST;
            if (hasField(previous, "incidental_findings")) return DES;
            return GENOME_SCREEN;
        }
    }
    public interface Handler<T> {
        T handle(PanelTest panelTest);
        T handle(Des des);
        T handle(GenomeScreen genomeScreen);
    }
    public static <T> T process(Object previous, Handler<T> handler) {
        Type type = Type.detect(previous);
        switch (type) {
            case PANEL_TEST:
                return handler.handle((PanelTest) previous);
            case DES:
                return handler.handle((Des) previous);
            case GENOME_SCREEN:
                return handler.handle((GenomeScreen) previous);
            default:
                throw new IllegalArgumentException("Unsupported DTO type. Expected one of: PanelTest, Des, or GenomeScreen.");
        }
    }
}
