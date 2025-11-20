package com.greencross.lims.dto.interpretation;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Inheritance {
    private static Set<String> dominants = new HashSet<>(List.of("AD","XLD","XD","DD","XL"));
    private static Set<String> recessives = new HashSet<>(List.of("AR","XLR","XR","DR"));
    public static boolean dominant(String check){
        return dominants.contains(check.trim().toUpperCase());
    }

    public static boolean recessive(String check){
        return recessives.contains(check.trim().toUpperCase());
    }
}
