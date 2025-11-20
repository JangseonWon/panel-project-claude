package com.greencross.lims.vcf;

import com.greencross.lims.dto.Vcf;
import com.greencross.lims.entity.Request;
import com.greencross.lims.trans.AnalysisToDTO;
import lombok.experimental.UtilityClass;

import java.util.Set;

@UtilityClass
public class VcfToDTO {
    public Vcf map(com.greencross.lims.vcf.Vcf entity) {
        Set<Request> reqs = entity.analysis().requests();
        return new Vcf().batch(entity.pk().batch()+0.0)
                .row(entity.pk().row()+0.0)
                .tier(entity.tier())
                .genotype(entity.genotype())
                .vaf(entity.vaf())
                .depth(entity.depth())
                .request(AnalysisToDTO.map(reqs.size()>0?reqs.stream().findAny().get():null));
    }
}
