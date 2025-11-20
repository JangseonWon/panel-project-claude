package com.greencross.lims.report;

import com.gcgenome.lims.dto.Report;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.Map;

@RestController
public class ReportController {
	private final Logger Log = LoggerFactory.getLogger(getClass());
	private final ReportService svc;
	public ReportController(ReportService svc) {
		this.svc = svc;
	}

	@RequestMapping(value="/samples/{sample}/services/{service}/reports/log",  method= RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public Flux<com.gcgenome.report.versions.log.Log> reportLogs(@PathVariable long sample, @PathVariable String service) {
		return svc.reportLogs(sample, service);
	}
	@RequestMapping(value="/samples/{sample}/services/{service}/reports/pdf",  method= RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public Mono<byte[]> reportLogPdf(@PathVariable long sample, @PathVariable String service) {
		return svc.reportLogsPdf(sample, service);
	}

	@RequestMapping(value="/samples/{sample}/services/{service}/reports",  method= RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public Flux<Report> reports(@PathVariable long sample, @PathVariable String service) {
		return svc.reports(sample, service);
	}
	@RequestMapping(value="/samples/{sample}/services/{service}/preview", method= RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public Mono<byte[]> preview(@PathVariable long sample, @PathVariable String service, @RequestBody Map<String, Object> json) throws IOException {
		return svc.preview(sample, service, json);
	}
	@RequestMapping(value="/samples/{sample}/services/{service}/print", method= RequestMethod.PUT, consumes = MediaType.TEXT_PLAIN_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public Mono<Report> print(@PathVariable long sample, @PathVariable String service, @RequestBody(required = false) String description) throws IOException {
		return svc.print(sample, service, description);
	}
	@RequestMapping(value="/samples/{sample}/services/{service}/reports/{createAt:[0-9]+}", method= RequestMethod.GET, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public Mono<byte[]> report(@PathVariable long sample, @PathVariable String service, @PathVariable long createAt) {
		return svc.report(sample, service, createAt);
	}
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public String exception(Exception e) {
		Log.error(e.getMessage(), e);
		return e.getMessage();
	}
}
