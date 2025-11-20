package com.greencross.lims.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class AnalysisController {
    private final AnalysisService svc;
    public AnalysisController(AnalysisService svc) {
        this.svc = svc;
    }

    @PutMapping(value="/samples/{sample}/services/{service}/batches/{batch}/{row}/{key}")
    @ResponseStatus(HttpStatus.OK)
    public String put(@PathVariable long sample, @PathVariable String service, @PathVariable String batch, @PathVariable int row, @PathVariable String key, @RequestBody Map<String, Object> value) throws Throwable {
        return svc.put(sample, service, batch, row, key, value);
    }
}
