package com.greencross.lims.vcf;

import com.greencross.lims.dto.Sheet;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class VcfController {
    private final VcfService svc;
    public VcfController(VcfService svc) {
        this.svc = svc;
    }
    @RequestMapping(value="/vcf/template", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Sheet template() {
        return svc.template();
    }
    @RequestMapping(value="/vcf/analysis", method= RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<Vcf> analysis() {
        return null;
    }
}
