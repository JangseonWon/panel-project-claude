package com.greencross.lims.publish.tmp;

import com.greencross.alis.api.AlisResult;
import com.greencross.alis.api.AlisVariantResult;
import com.greencross.lims.dto.interpretation.tmp.N159Dto;
import com.greencross.lims.entity.Request;
import com.greencross.lims.publish.AlisMapper;
import com.gcgenome.lims.test.tmp.N159;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Component
public class N159Mapper implements AlisMapper<N159Dto> {
    private final N159 test = N159.builder().build();

    @Override
    public Class<N159Dto> clazz() {
        return N159Dto.class;
    }

    @Override
    public boolean match(Request request) {
        String service = request.pk().service();
        return test.code().equalsIgnoreCase(service);
    }

    @Override
    public AlisResult[] map(Request request, N159Dto interpretation, Class clazz) {
        String code = request.pk().service();
        AlisResult[] result = new AlisResult[]{
                AlisResult.builder().subCode(code + "020").text(interpretation.result()).build(),
                AlisResult.builder().subCode(code + "021").text(interpretation.resultText()).build(),
                AlisResult.builder().subCode(code + "028").text(interpretation.abbreviationReference()).build(),
                AlisResult.builder().subCode(code + "040").text(interpretation.interpretation()).build()
        };
        return Arrays.stream(result).toArray(AlisResult[]::new);
    }

    @Override
    public AlisVariantResult[] variants(Request request, N159Dto interpretation, Class<N159Dto> clazz) {
        List<AlisVariantResult> values = new LinkedList<>();
        if(interpretation.variants()!=null) {
            for(int i = 0; i < interpretation.variants().length; ++i) values.addAll(map(interpretation.variants()[i], i));
        }
        return values.toArray(new AlisVariantResult[0]);
    }

    @Override
    public String text(Request request, long createAt) throws IOException {
        return null;
    }

    @Override
    public String textShort(Request request, long createAt) throws IOException {
        return null;
    }

    private final DecimalFormat df = new DecimalFormat("#.##");
    private List<AlisVariantResult> map(N159Dto.Variant v, int i) {
        List<AlisVariantResult> values = new LinkedList<>();
        N159Dto.Tier tier = v.tier();
        values.add(AlisVariantResult.builder().row(i).key("Gene").value(v.gene()).build());
        values.add(AlisVariantResult.builder().row(i).key("DNA").value(v.hgvsc()).build());
        values.add(AlisVariantResult.builder().row(i).key("Protein").value(v.hgvsp()).build());
        values.add(AlisVariantResult.builder().row(i).key("VAF").value(df.format(v.vaf())).build());
        values.add(AlisVariantResult.builder().row(i).key("Depth").value(NumberFormat.getInstance().format(v.depth())).build());
        values.add(AlisVariantResult.builder().row(i).key("COSMIC ID").value(v.cosmic()).build());
        values.add(AlisVariantResult.builder().row(i).key("Tier 구분").value(tier.toString()).build());
        return values;
    }
}
