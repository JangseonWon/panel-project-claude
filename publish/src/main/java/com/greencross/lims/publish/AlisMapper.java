package com.greencross.lims.publish;

import com.greencross.alis.api.AlisResult;
import com.greencross.alis.api.AlisVariantResult;
import com.greencross.lims.entity.Request;

import java.io.IOException;

public interface AlisMapper<T> extends Publisher<T> {
    default AlisResult[] map(Request request, Object interpretation) throws IOException {
        return map(request, (T)interpretation, clazz());
    }
    AlisResult[] map(Request request, T interpretation, Class<T> clazz) throws IOException;
    default AlisVariantResult[] variants(Request request, Object interpretation) throws IOException {
        return variants(request, (T)interpretation, clazz());
    }
    default AlisVariantResult[] variants(Request request, T interpretation, Class<T> clazz) throws IOException {
        return new AlisVariantResult[0];
    }
    String text(Request request, long createAt) throws IOException;
    String textShort(Request request, long createAt) throws IOException;
}