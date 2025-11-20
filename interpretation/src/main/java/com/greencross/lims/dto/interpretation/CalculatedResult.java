package com.greencross.lims.dto.interpretation;


import java.util.List;
import java.util.stream.Collectors;

abstract public class CalculatedResult<V extends BaseVariant<V>> {

    private boolean inconclusivePredicate(V variant){
        String clazz = variant.clazz().trim();
        if(clazz.equalsIgnoreCase("VUS")) return true;
        return checkClazzPathogenic(clazz) && Inheritance.recessive(variant.inheritance());
    }

    private boolean positivePredicate(V variant){
        String clazz = variant.clazz().trim();
        return switch (variant.zygosity().toUpperCase()) {
            case "HOM", "HEM" -> checkClazzPathogenic(clazz);
            default -> checkClazzPathogenic(clazz) && Inheritance.dominant(variant.inheritance());
        };
    }

    private boolean checkClazzPathogenic(String clazz){
        return clazz.equalsIgnoreCase("PV") || clazz.equalsIgnoreCase("LPV");
    }
    private boolean findIfMultiplePathogensExist(List<V> variantList){
        return variantList.stream().filter(var -> checkClazzPathogenic(var.clazz())).collect(Collectors.groupingBy(BaseVariant::gene))
                .entrySet().stream().anyMatch(entry -> entry.getValue().size() > 2);
    }

    protected String calculateResultString(List<V> variantList){
        if(variantList.stream().anyMatch(this::inconclusivePredicate)) return "INCONCLUSIVE";
        if(variantList.stream().anyMatch(this::positivePredicate)) return "POSITIVE";
        if(findIfMultiplePathogensExist(variantList)) return "POSITIVE";
        return null;
    }

    abstract public void calculateResult(List<V> variantList);

}
