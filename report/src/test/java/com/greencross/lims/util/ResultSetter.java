package com.greencross.lims.util;

@FunctionalInterface
public interface ResultSetter<T> {
    void setResult(T testInfo);
}