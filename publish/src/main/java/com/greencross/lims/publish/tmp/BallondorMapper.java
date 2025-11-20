package com.greencross.lims.publish.tmp;

import com.greencross.alis.api.AlisResult;
import com.greencross.alis.api.AlisVariantResult;
import com.gcgenome.lims.dto.interpretation.tmp.BallondorDto;
import com.greencross.lims.entity.Request;
import com.greencross.lims.publish.AlisMapper;
import com.gcgenome.lims.test.tmp.Ballondor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Component
public class BallondorMapper implements AlisMapper<BallondorDto> {
    @Override
    public Class<BallondorDto> clazz() {
        return BallondorDto.class;
    }

    @Override
    public boolean match(Request request) {
        String service = request.pk().service();
        return Arrays.stream(Ballondor.TESTS).anyMatch(test->test.code().equalsIgnoreCase(service));
    }
    @Override
    public AlisResult[] map(Request request, BallondorDto interpretation, Class clazz) {
        String code = request.pk().service();
        AlisResult[] result = new AlisResult[]{
                AlisResult.builder().subCode(code + "010").text(interpretation.summary()).build(),
                AlisResult.builder().subCode(code + "020").text(interpretation.sample()).build()
        };
        return Arrays.stream(result).toArray(AlisResult[]::new);
    }
    @Override
    public AlisVariantResult[] variants(Request request, BallondorDto interpretation, Class<BallondorDto> clazz) {
        List<AlisVariantResult> values = new LinkedList<>();
        Ballondor test = Arrays.stream(Ballondor.TESTS).filter(t->t.code().equalsIgnoreCase(request.pk().service())).findFirst().get();
        values.add(AlisVariantResult.builder().row(0).key("Gene").value(test.gene()).build());
        values.add(AlisVariantResult.builder().row(0).key("DNA change").value(interpretation.hgvsc()).build());
        values.add(AlisVariantResult.builder().row(0).key("AA change").value(test.hgvsp()).build());
        values.add(AlisVariantResult.builder().row(0).key("Results").value(interpretation.result()).build());
        values.add(AlisVariantResult.builder().row(0).key("Vaf (%)").value(interpretation.vaf()).build());
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
}
