package com.greencross.lims.publish;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gcgenome.lims.EventConfig;
import com.greencross.alis.api.AlisVariantResult;
import com.greencross.alis.api.Api;
import com.greencross.app.api.GenomeHealthApi;
import com.greencross.lims.dao.ReportFileRepository;
import com.greencross.lims.dto.PublishMessage;
import com.greencross.lims.entity.*;

import com.greencross.lims.jandiwebhook.Webhook;
import com.greencross.lims.jandiwebhook.dto.ConnectInfo;
import com.greencross.lims.report.ReportFile;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
public class PublishService {
    private final ReportDAO dao;
    private final ReportFileRepository fileRepo;
    private final SnvConsensualClassDao consensualClassDao;
    private final HandbookTestDao handbookTestDao;
    private final Api alisApi;
    private final AlisMapper[] mappers;
    private final ObjectMapper om;
    private final GenomeHealthApi medichkApi;
    private final Webhook jandi;
    private final Sinks.Many<PublishMessage> publisher = Sinks.many().unicast().onBackpressureBuffer();
    private final EventConfig eventConfig;
    private final Logger Log = LoggerFactory.getLogger(getClass());
    public PublishService(ReportDAO dao, ReportFileRepository fileRepo, SnvConsensualClassDao consensualClassDao, HandbookTestDao handbookTestDao, Api alisApi, AlisMapper[] mappers, ObjectMapper om, GenomeHealthApi medichkApi, Webhook jandi, EventConfig eventConfig) {
        this.dao = dao;
        this.fileRepo = fileRepo;
        this.consensualClassDao = consensualClassDao;
        this.handbookTestDao = handbookTestDao;
        this.alisApi = alisApi;
        this.mappers = mappers;
        this.om = om;
        this.medichkApi = medichkApi;
        this.jandi = jandi;
        this.eventConfig = eventConfig;
    }

    @Transactional
    public void publish(UserActivated user, long sample, String service, long createAt) throws IOException {
        com.greencross.lims.entity.Report.ReportPK pk = com.greencross.lims.entity.Report.ReportPK.builder().sample(sample).service(service).createAt(createAt).build();
        Optional<com.greencross.lims.entity.Report> report = dao.find(pk);
        Interpretation interpretation = report.map(com.greencross.lims.entity.Report::interpretation).orElseThrow(() -> new RuntimeException("Can't find Report:" + sample + ", " + service + ", " + createAt));

        // Publish 직전 추가사항(Mapper에 위임)
        Request entity = interpretation.request();
        var mapper = Arrays.stream(mappers).filter(m->m.match(entity)).findFirst().orElse(null);
        if(mapper == null) throw new RuntimeException("Can't find Mapper:" + entity.pk());
        mapper.preprocessing(entity, user);

        long sampleId = entity.pk().sample();
        int requestNo = (int) (sampleId % 10000000);
        LocalDate requestDate = LocalDate.parse(String.format("%d", sampleId / 10000000), DateTimeFormatter.ofPattern("yyyyMMdd"));
        com.greencross.alis.api.Request request = com.greencross.alis.api.Request.builder()
                                                                                           .requestDate(requestDate)
                                                                                           .requestNo(requestNo)
                                                                                           .itemCode(entity.pk().service()).build();
        if("F".equalsIgnoreCase(alisApi.state(request))) {
            String sid = String.valueOf(sample);
            String sampleFmt = sid.substring(0, 8) + "-" + sid.substring(8, 11) + "-" + sid.substring(11);
            jandi.sendWithConnectInfos("검사 결과 전송이 실패하였습니다: " + sampleFmt + " / " + service,
                    List.of(new ConnectInfo().title("최종 상태의 검사 변경 시도")));
            return;
        }
        byte[] data = report.map(com.greencross.lims.entity.Report::file)
                            .map(id -> fileRepo.findById(id).orElseThrow(() -> new RuntimeException("Can't find Report File:" + id)))
                            .map(ReportFile::data)
                            .map(ByteBuffer::array)
                            .orElse(null);
        boolean worklist = false, cancel=false, values=false, urllink=false, state=false, text=false;
        Boolean img1=null, img2=null, pdf=null;
        List<com.gcgenome.lims.workflow.Event> events = null;
        try {
            worklist = alisApi.chkWorklist(request);                                    // 결과 전송 전 워크리스트 전송 누락 건 처리
            cancel = alisApi.cancelPublish(request);                                    // 파일전송 전 기존파일 삭제처리
            if(cancel) values = sendAlisResultValues(user.id(), interpretation, request);// 검사 결과 전송
            if(values) urllink = sendUrl(user.id(), interpretation, request);           // 파일 다운로드 URL 전송
            if(urllink) text = sendToAlisText(user.id(), interpretation, request, createAt);      // 텍스트 포맷 전송
            if (data != null) {
                if(text)    state = alisApi.state(request, "F", user.id(),"LIMS");
                if(state)   img1 = createImgTotal(user.id(), data, request);                    // 통이미지 전송
                if(img1)    img2 = createImgDiv(user.id(), data, request);                      // 낱장이미지 전송
                if(img2)    pdf = sendToResult(user.id(), data, request, "pdf", "");  // PDF를 A-LIS에 전송, 저장
                if(pdf)     events = report.stream().map(r->eventConfig.publish(user.id(), r)).flatMap(List::stream).collect(Collectors.toList());
            } else {
                if(text) state = alisApi.state(request, "M", user.id(), "LIMS");
            }
        } catch(Exception e) {
            e.printStackTrace();
            StringBuilder sb = new StringBuilder();
            sb.append("[Worklist 생성].............").append(worklist?"Success":"Fail").append("\n")
              .append("[기존 파일 삭제]..........").append(cancel?"Success":"Fail").append("\n")
              .append("[검사 결과 전송]..........").append(values?"Success":"Fail").append("\n")
              .append("[다운로드 링크 전송]...").append(urllink?"Success":"Fail").append("\n")
              .append("[서술형 결과 전송].......").append(text?"Success":"Fail").append("\n")
              .append("[최종 상태로 변경].......").append(state?"Success":"Fail").append("\n")
              .append("[통이미지 전송]............").append(img1!=null?(img1?"Success":"Fail"):"N/A").append("\n")
              .append("[낱장이미지 전송]........").append(img2!=null?(img2?"Success":"Fail"):"N/A").append("\n")
              .append("[PDF 전송]...................").append(pdf!=null?(pdf?"Success":"Fail"):"N/A").append("\n")
              .append("[EVENT 전송]..............").append(events!=null?(!events.isEmpty()?"Success":"Fail"):"N/A");
            String sid = String.valueOf(sample);
            String sampleFmt = sid.substring(0, 8) + "-" + sid.substring(8, 11) + "-" + sid.substring(11);
            jandi.sendWithConnectInfos("검사 결과 전송이 실패하였습니다: " + sampleFmt + " / " + service,
                                       List.of(new ConnectInfo().title("전송 상태").description(sb.toString()),
                                               new ConnectInfo().title("에러 메시지").description(e.getMessage())));
            return;
        }
        if(interpretation.value().containsKey("private_key")) {
            String privateKey = (String) interpretation.value().get("private_key");
            String publicKey = (String) interpretation.value().get("public_key");
            String appData = medichkApi.post(publicKey, service, interpretation.value());
            String appPdf = medichkApi.postFile(publicKey, service, data);
            Log.info("Send to GenomeHealth.. -> " + interpretation.pk().sample() + ", " + interpretation.pk().service() + " (" + privateKey + ", " + publicKey + ")");
            Log.info(appData + ", " + appPdf);
            report.get().privateKey(privateKey).publicKey(publicKey);
        }
        dao.em().merge(interpretation.publishAt(LocalDateTime.now()));
        dao.merge(report.get().publishAt(LocalDateTime.now()));
        updateNovelVariantConsensualClass(interpretation, request);
        publisher.tryEmitNext(publish(entity, interpretation.value(), data));
    }

    private boolean sendUrl(String user, Interpretation interpretation, final com.greencross.alis.api.Request request) throws IOException {
        Request entity = interpretation.request();
        if(entity.files()!=null) {
            com.greencross.alis.api.Request request2 = req2(entity);
            for (var file : entity.files()) {
                var url = "https://download.gcgenome.com/samples/{id}/services/{code}/files/{file}"
                        .replace("{id}", String.valueOf(file.pk().sample()))
                        .replace("{code}", file.pk().service())
                        .replace("{file}", file.name());
                boolean sent = alisApi.urlUpload(user, request, request2, url, String.valueOf(file.pk().sequence()), file.extension(), file.name());
                if (!sent) return false;
            }
        }
        return true;
    }
    private boolean sendAlisResultValues(String user, Interpretation interpretation, final com.greencross.alis.api.Request request) throws IOException {
        Request entity = interpretation.request();
        var mapper = Arrays.stream(mappers).filter(m->m.match(entity)).findFirst().orElse(null);
        if(mapper == null) throw new RuntimeException("Can't find Mapper:" + entity.pk());
        clearVariants(request);
        var var = om.readValue(om.writeValueAsString(interpretation.value()), mapper.clazz());
        AlisVariantResult[] variants = mapper.variants(entity, var);
        if(variants!=null) for(AlisVariantResult variant : variants) alisApi.saveVariantValue(request, variant.row(), variant.key(), variant.value());
        // return Arrays.stream(mapper.map(entity, var)).allMatch(result->alisApi.result(request, result, user, "LIMS"));
        Arrays.stream(mapper.map(entity, var)).forEach(result->alisApi.result(request, result, user, "LIMS"));
        return true;
    }
    private com.greencross.alis.api.Request req2(Request entity) {
        if(entity.sample().patient().customerCode2()==null) return null;
        try {
            Sample smp = entity.sample();
            long sampleId = Long.parseLong(smp.remark());
            String code = code(entity.dateRequest(), entity.pk().service());
            System.out.println("GenomeCode: " + entity.pk().service() + ", Labs Code: " + code);
            return com.greencross.alis.api.Request.builder().requestNo2(sampleId).itemCode(code).build();
        } catch(NumberFormatException e) {
            e.printStackTrace();
            return null;
        }
    }
    private String code(LocalDate time, String code) {
        return handbookTestDao.findByCode(code)
                .filter(it->time.isAfter(it.dateEffective().toLocalDate()) || time.isEqual(it.dateEffective().toLocalDate()))
                .filter(it->it.dateExpiry()==null || it.dateExpiry().toLocalDate().isAfter(time))
                .findAny().map(Test::labs)
                .orElse(null);
    }

    private void clearVariants(com.greencross.alis.api.Request request) {
        try {
            Map<String, String>[] values = alisApi.getVariant(request);
            for(int i = 0; i < values.length; ++i) alisApi.deleteVariant(request, Integer.parseInt(values[i].get("GeneDataRowSeq")));
        } catch(Exception ignore){
            ignore.printStackTrace();
        }
    }
    private boolean sendToAlisText(String user, Interpretation interpretation, com.greencross.alis.api.Request request, long createAt) throws IOException {
        Request entity = interpretation.request();
        AlisMapper<?> mapper = Arrays.stream(mappers).filter(m->m.match(entity)).findFirst().orElse(null);
        if(mapper!=null) {
            String textResult = mapper.text(entity, createAt);
            if(textResult!=null) textResult = escape(textResult);
            if(textResult!=null && !textResult.trim().isEmpty()) alisApi.textUpload(user, request, textResult, Api.FileType.TEXT, LocalDate.now(), "");

            String textShort = mapper.textShort(entity, createAt);
            if(textShort==null) alisApi.textUpload(user, request, "결과지 또는 PDF, 웹페이지 등 다른 포맷의 결과를 참고해 주십시오.", Api.FileType.TEXT_SHORTER, LocalDate.now(), "");
            if(textShort!=null && textShort.getBytes().length > 4000) alisApi.textUpload(user, request, "결과 내용이 본 포맷에 허용된 용량을 초과하였습니다. 결과지 또는 PDF, 웹페이지 등 다른 포맷의 결과를 참고해 주십시오.", Api.FileType.TEXT_SHORTER, LocalDate.now(), "");
            if(textShort!=null) textShort = escape(textShort);
            if(textShort!=null && !textShort.trim().isEmpty()) alisApi.textUpload(user, request, textShort, Api.FileType.TEXT_SHORTER, LocalDate.now(), "");
        }
        return true;
    }
    private String escape(String text) {        // 텍스트 내의 대체 가능한 특수문자 처리
        if(text.contains("–")) text = text.replace('–', '-');
        return text;
    }
    private boolean createImgDiv(String user, byte[] bytes, com.greencross.alis.api.Request request) throws IOException {    //낱장
        PDDocument doc = PDDocument.load(bytes);
        PDFRenderer pdfRenderer = new PDFRenderer(doc);
        System.out.println(bytes);
        for (int page = 0; page < doc.getNumberOfPages(); ++page) {
            BufferedImage img = pdfRenderer.renderImageWithDPI(page, 120, ImageType.RGB);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(img, "jpg", baos);
            baos.flush();
            baos.close();
            byte[] imageByte = baos.toByteArray();
            if(!sendToResult(user, imageByte, request, "imgDiv", String.valueOf(page))) return false;
        }
        return true;
    }
    private boolean createImgTotal(String user, byte[] bytes, com.greencross.alis.api.Request request) throws IOException {    //total
        PDDocument doc = PDDocument.load(bytes);
        PDFRenderer pdfRenderer = new PDFRenderer(doc);
        int height = 0;
        int width = 0;
        LinkedList<BufferedImage> cache = new LinkedList<>();
        for (int page = 0; page < doc.getNumberOfPages(); ++page) {
            BufferedImage img = pdfRenderer.renderImageWithDPI(page, 120, ImageType.RGB);
            cache.add(img);
            width = Math.max(img.getWidth(), width);
            height += img.getHeight();
        }

        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = img.getGraphics();
        int cursor = 0;
        for (BufferedImage img2 : cache) {
            g.drawImage(img2, 0, cursor, null);
            cursor += img2.getHeight();
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(img, "jpg", baos);
        baos.flush();
        baos.close();
        byte[] imageByte = baos.toByteArray();
        return sendToResult(user, imageByte, request, "imgTotal", "");
    }
    private boolean sendToResult(String user, byte[] data, com.greencross.alis.api.Request request, String flg, String page) throws IOException {
        String prefix = request.requestDate().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String infix = String.format("%d", request.requestNo()/10000);
        String suffix = String.format("%d", request.requestNo()%10000);
        String title = prefix + "_" + infix + suffix;
        /*pdf img 구분 조건 추가*/
        if("pdf".equals(flg))          return alisApi.fileUpload(user, request, data, title, Api.FileType.PDF, LocalDate.now(), "");
        else if("imgDiv".equals(flg))  return alisApi.fileUpload(user, request, data, title + "_" + page, Api.FileType.JPG_PER_PAGE, LocalDate.now(), "");
        else if("imgTotal".equals(flg))return alisApi.fileUpload(user, request, data, title, Api.FileType.JPG, LocalDate.now(), "");
        return false;
    }

    private void updateNovelVariantConsensualClass(Interpretation interpretation, final com.greencross.alis.api.Request request) throws JsonProcessingException {
        Request entity = interpretation.request();
        var mapper = Arrays.stream(mappers).filter(m->m.match(entity)).findFirst().orElse(null);
        if(mapper == null) throw new RuntimeException("Can't find Mapper:" + entity.pk());

        var var = om.readValue(om.writeValueAsString(interpretation.value()), mapper.clazz());
        VariantReference[] variants = mapper.variantReferences(entity, var);
        if(variants!=null) for(VariantReference variant : variants) {
            if(consensualClassDao.findBySnv(variant.snv()).findAny().isPresent()) continue;
            var pk = new SnvConsensualClass.SnvConsensualClassPK().snv(variant.snv());
            var clazz = new SnvConsensualClass().pk(pk).classification(variant.clazz());
            consensualClassDao.merge(clazz);
        }
    }
    private PublishMessage publish(Request request, Object interpretation, byte[] report) {
        Sample smp = request.sample();
        Patient pat = smp.patient();
        String institution = pat.customerCode();
        String institutionName = pat.customerName();
        if(pat.customerCode2()!=null && !pat.customerCode2().trim().isEmpty()) {
            institution += "-" + pat.customerCode2();
            institutionName = pat.customerName2();
        }
        Object customInfos = null;
        if(request.customInfos()!=null) customInfos = request.customInfos().stream().collect(Collectors.toMap(r->r.code(), r->r.value()));
        return PublishMessage.builder()
                             .institution(institution).institutionName(institutionName).departmentName(request.customerDeptName()).wardName(request.ward())
                             .patientName(pat.name())
                             .sex(pat.sex()!=null?pat.sex().name():null)
                             .mrn(pat.mrn())
                             .info(request.info())
                             .physician(request.physician())
                             .birth(pat.birth())
                             .sample(request.pk().sample()).service(request.pk().service()).serviceName(request.service().name())
                             .interpretation(interpretation)
                             .report(report)
                             .customInfos(customInfos)
                             .build();
    }
    @Bean("publish")
    Supplier<Flux<String>> publish() {
        return ()->publisher.asFlux().map(v-> {
            try {
                return om.writeValueAsString(v);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return null;
        }).filter(Objects::nonNull);
    }
    public String state(long sample, String service) {
        int requestNo = (int) (sample % 10000000);
        LocalDate requestDate = LocalDate.parse(String.format("%d", sample / 10000000), DateTimeFormatter.ofPattern("yyyyMMdd"));
        com.greencross.alis.api.Request request = com.greencross.alis.api.Request.builder()
                .requestDate(requestDate)
                .requestNo(requestNo)
                .itemCode(service).build();
        return alisApi.state(request);
    }
}
