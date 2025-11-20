package com.greencross.lims.report;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gcgenome.report.versions.log.Log;
import com.gcgenome.report.versions.log.LogService;
import com.gcgenome.report.versions.report.ReportVersionService;
import com.greencross.lims.dao.ReportFileRepository;
import com.gcgenome.lims.dto.Report;
import com.greencross.lims.entity.Interpretation;
import com.greencross.lims.entity.Request;
import com.greencross.lims.trans.LocalDateTimeToEpoch;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class ReportService {
	private final InterpretationDAO interpretationDao;
	private final ReportDAO reportDao;
	private final ReportFileRepository fileRepo;
	private final List<ReportFactory<?>> factories;
	private final ObjectMapper om;
	private final ReportVersionService reportVersionService;

	public ReportService(InterpretationDAO interpretationDao, ReportDAO reportDao, ReportFileRepository fileRepo, List<ReportFactory<?>> factories, ObjectMapper om, LogService logService, ReportVersionService reportVersionService) {
		this.interpretationDao = interpretationDao;
		this.reportDao = reportDao;
		this.fileRepo = fileRepo;
		this.factories = factories;
		this.om = om;
		this.reportVersionService = reportVersionService;
	}

	@Transactional
	public Flux<Report> reports(long sample, String service) {
		Interpretation interpretation = interpretationDao.find(Interpretation.InterpretationPK.builder().sample(sample).service(service).build()).orElse(null);
		if (interpretation == null) return null;
		return Flux.fromArray(interpretation.reports().stream().map(ReportToDTO::map).toArray(Report[]::new));
	}

	public Flux<Log> reportLogs(long sample, String service) {
		return Flux.fromIterable(reportVersionService.getReportLogs(sample,service));
	}

	public Mono<byte[]> reportLogsPdf(long sample, String service) {
		return Mono.just(reportVersionService.getReportLogPdf(sample,service));
	}
	@Transactional(readOnly = true)
	public Mono<byte[]> preview(long sample, String service, Map<String, Object> json) throws IOException {
		Request request = interpretationDao.em().find(Request.class, Request.RequestPK.builder().sample(sample).service(service).build());
		if(request == null) throw new RuntimeException("Can't find Request:" + sample + ", " + service);
		ReportFactory<?> builder = factories.stream().filter(m->m.match(request)).findFirst().orElse(null);
		if(builder!=null) {
			byte[] data = builder.build(request, om.readValue(om.writeValueAsString(json), builder.clazz()));
			return Mono.just(data);
		} return Mono.empty();
	}

	@Transactional
	public Mono<Report> print(long sample, String service, String description) throws IOException {
		Interpretation interpretation = interpretationDao.find(Interpretation.InterpretationPK.builder().sample(sample).service(service).build()).orElse(null);
		if (interpretation == null) throw new RuntimeException("Can't find Interpretation:" + sample + ", " + service);
		com.greencross.lims.entity.Report entity = new com.greencross.lims.entity.Report().pk(com.greencross.lims.entity.Report.ReportPK.builder().sample(sample).service(service).createAt(LocalDateTimeToEpoch.map(LocalDateTime.now())).build()).description((description==null)?"최초 보고":description);
		ReportFactory<?> builder = factories.stream().filter(m->m.match(interpretation.request())).findFirst().orElse(null);
		if(builder!=null) {
			Object typedInterpretation = om.readValue(om.writeValueAsString(interpretation.value()), builder.clazz());
			byte[] data = builder.build(interpretation.request(), typedInterpretation);
			String fileName = sample + "_" + service + "_" + entity.createAt().format(DateTimeFormatter.ofPattern("yyMMdd")) + ".pdf";
			ReportFile file = new ReportFile().id(UUID.randomUUID()).createTime(entity.createAt())
					.data(ByteBuffer.wrap(data)).extension("pdf").name(fileName).size((long) data.length);
			fileRepo.save(file);
			entity.file(file.id()).name(file.name()).size(file.size().intValue());
			entity.shortFormText(builder.buildShortFormText(interpretation.request()));
			entity.longFormText(builder.buildLongFormText(interpretation.request()));
		} else entity.etc("Publish 후 A-LIS에서 결과지를 생성하세요.");
		return Mono.just(ReportToDTO.map(reportDao.merge(entity)));
	}

	@Transactional(readOnly = true)
	public Mono<byte[]> report(long sample, String service, long createAt) {
		com.greencross.lims.entity.Report.ReportPK pk = com.greencross.lims.entity.Report.ReportPK.builder().sample(sample).service(service).createAt(createAt).build();
		return reportDao.find(pk).map(com.greencross.lims.entity.Report::file)
				.map(id -> fileRepo.findById(id).orElseThrow(() -> new RuntimeException("Can't find Report File:" + id)))
				.map(ReportFile::data)
				.map(ByteBuffer::array).map(Mono::just)
				.orElseThrow(() -> new RuntimeException("Can't find Report:" + sample + ", " + service + ", " + createAt));
	}
}
