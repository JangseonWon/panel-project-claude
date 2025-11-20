package com.greencross.lims.file;

import com.gcgenome.lims.dto.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.codec.multipart.Part;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class FileController {
	private final Logger Log = LoggerFactory.getLogger(getClass());
	private final FileService svc;
	public FileController(FileService svc) {
		this.svc = svc;
	}
	@GetMapping(value="/samples/{sample}/services/{service}/files")
	@ResponseStatus(HttpStatus.OK)
	public Flux<File> files(@PathVariable long sample, @PathVariable String service) {
		return svc.list(sample, service);
	}
	@RequestMapping(value="/samples/{sample}/services/{service}/files/{file:.+}",  method={RequestMethod.POST, RequestMethod.PUT})
	@ResponseStatus(HttpStatus.OK)
	public Mono<Void> upload(@PathVariable long sample, @PathVariable String service, @PathVariable String file, @RequestHeader("Content-Length") long contentLength, @RequestBody Flux<Part> parts) {
		return svc.upload(sample, service, file, contentLength, parts.filter(c->"file".equalsIgnoreCase(c.name())).next());
	}
	@DeleteMapping(value="/samples/{sample}/services/{service}/files/{file:.+}")
	@ResponseStatus(HttpStatus.OK)
	public Mono<Void> upload(@PathVariable long sample, @PathVariable String service, @PathVariable String file) {
		svc.delete(sample, service, file);
		return Mono.empty();
	}
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public String exception(Exception e) {
		Log.error(e.getMessage(), e);
		return e.getMessage();
	}
}
