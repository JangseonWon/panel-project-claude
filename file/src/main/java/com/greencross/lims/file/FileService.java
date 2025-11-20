package com.greencross.lims.file;

import com.gcgenome.lims.dto.File;
import com.greencross.lims.entity.File.FilePK;
import com.greencross.lims.entity.readOnly.Request;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.codec.multipart.Part;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.file.Path;
import java.util.stream.Collectors;

@Service
public class FileService {
	private final FileDao dao;
	private final TransactionTemplate tx;
	public FileService(FileDao dao, PlatformTransactionManager tx) {
		this.dao = dao;
		this.tx = new TransactionTemplate(tx);
	}
	@Transactional(readOnly = true)
	public Flux<File> list(long sample, String service) {
		Request request = dao.em().find(Request.class, Request.RequestPK.builder().sample(sample).service(service).build());
		if(request!=null && request.files()!=null) return Flux.fromIterable(request.files().stream().map(FileToDto::map).collect(Collectors.toList()));
		else return Flux.empty();
	}

	private Path tmp = Path.of("/data/lis");
	public Mono<Void> upload(long sample, String service, String fileName, long contentLength, Mono<Part> part) {
		String prefix = String.valueOf(sample).substring(0, 8);
		Path dest = tmp.resolve(prefix).resolve(sample + service);
		return part.cast(FilePart.class)
				   .flatMap(file->{
					   dest.toFile().mkdirs();
					   return file.transferTo(dest.resolve(fileName).toFile())
								  .doOnSuccess(c->{
									  int sequence = dest.toFile().listFiles().length + 1;
									  String ext = fileName.substring(fileName.lastIndexOf(".")+1);
									  tx.execute(ts->dao.merge(new com.greencross.lims.entity.File().pk(FilePK.builder().sample(sample).service(service).sequence(sequence).build())
																									.name(fileName)
																									.extension(ext)
																									.size(contentLength)
																									.path(dest.resolve(fileName).toAbsolutePath().toString()))
									  );
								  });
				   });
	}
	@Transactional
	public void delete(long sample, String service, String fileName) {
		String prefix = String.valueOf(sample).substring(0, 8);
		Path dest = tmp.resolve(prefix).resolve(sample + service);
		dest.resolve(fileName).toFile().delete();
		dao.em().createNativeQuery("DELETE FROM panel.file WHERE sample=" + sample + " AND service='" + service + "' AND name='" + fileName + "'").executeUpdate();
	}
}
