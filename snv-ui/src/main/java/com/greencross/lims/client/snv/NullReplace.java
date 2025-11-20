package com.greencross.lims.client.snv;

public class NullReplace {
    public static Object replaceWithBlankIfNull(Object value){
        if(value == null) return "";
        return value;
    }
}
