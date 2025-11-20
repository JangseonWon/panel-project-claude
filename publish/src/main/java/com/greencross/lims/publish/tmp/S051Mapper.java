package com.greencross.lims.publish.tmp;

import com.greencross.alis.api.AlisResult;
import com.gcgenome.lims.dto.interpretation.tmp.S051Dto;
import com.greencross.lims.entity.Report;
import com.greencross.lims.entity.Request;
import com.greencross.lims.publish.AlisMapper;
import com.gcgenome.lims.test.tmp.S051;
import com.greencross.lims.publish.ReportDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class S051Mapper implements AlisMapper<S051Dto> {
    private final ReportDAO reportDAO;
    private final S051 test = S051.instance;
    @Override
    public Class<S051Dto> clazz() {
        return S051Dto.class;
    }

    @Override
    public boolean match(Request request) {
        String service = request.pk().service();
        return test.code().equalsIgnoreCase(service);
    }

    @Override
    public AlisResult[] map(Request request, S051Dto interpretation, Class clazz) {
        String code = request.pk().service();
        AlisResult[] result = new AlisResult[]{
                AlisResult.builder().subCode(code + "010").text(interpretation.result()).build(),
                AlisResult.builder().subCode(code + "020").text(interpretation.interpretation()).build()
        };
        return Arrays.stream(result).toArray(AlisResult[]::new);
    }

    @Override
    public String text(Request request, long createAt) throws IOException {
        Report.ReportPK pk = com.greencross.lims.entity.Report.ReportPK.builder().sample(request.pk().sample()).service(request.pk().service()).createAt(createAt).build();
        return reportDAO.find(pk).map(Report::longFormText).orElse(null);
    }

    @Override
    public String textShort(Request request, long createAt) throws IOException {
        return text(request, createAt);
    }
}
