package com.greencross.lims.publish.genomescreen;

import com.gcgenome.lims.dto.interpretation.GenomeScreen;
import com.gcgenome.lims.dto.interpretation.PanelTest.Variant;
import com.gcgenome.lims.test.genomescreen.TestInfo;
import com.gcgenome.lims.test.genomescreen.TestWithRiskScreen;
import com.greencross.alis.api.AlisResult;
import com.greencross.alis.api.AlisVariantResult;
import com.greencross.lims.entity.Request;
import com.greencross.lims.entity.User;
import com.greencross.lims.publish.AlisMapper;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Component
public class GenomeScreenMapper implements AlisMapper<GenomeScreen> {

	private final MultiValueMap<String, HttpEntity<?>> multipartBody;
	private final RestTemplate restTemplate;

	private final Long contentLength;

	{
		File pdfFile = Path.of("/data/lims/resources/img/genomescreen/N090/CancerGenomeScreen_Holder.pdf").toFile();
		MultipartBodyBuilder builder = new MultipartBodyBuilder();
		contentLength = pdfFile.length();
		builder.part("file",new FileSystemResource(pdfFile));
		multipartBody = builder.build();
	}

	public GenomeScreenMapper(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	@Override
	public Class<GenomeScreen> clazz() {
		return GenomeScreen.class;
	}

	@Override
	public boolean match(Request request) {
		String service = request.pk().service();
		return Arrays.stream(TestInfo.TESTS).anyMatch(m -> m.code().equalsIgnoreCase(service));
	}

	@Override
	public void preprocessing(Request request, User<?> user) {
		// 암지놈스크린 홀더 첨부파일 생성
		if(!request.pk().service().equals("N090") && !request.pk().service().equals("J018")) return;
		long sample = request.sample().id();
		String service = request.service().id();
		Mono.fromRunnable(() -> {
			restTemplate.postForEntity("http://file-service/samples/" + sample + "/services/" + service + "/files/CancerGenomeScreen_Holder.pdf", requestEntity(user.id()), String.class);
		}).subscribeOn(Schedulers.boundedElastic()).subscribe();
	}

	private HttpEntity<MultiValueMap<String, HttpEntity<?>>> requestEntity(String user){
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		headers.add("X-USER-ID", user);
		headers.add("Content-Length", contentLength.toString());
		return new HttpEntity<>(multipartBody, headers);
	}

	@Override
	public AlisResult[] map(Request request, GenomeScreen dto, Class<GenomeScreen> clazz) {
		String code = request.pk().service();
		TestInfo test = Arrays.stream(TestInfo.TESTS).filter(m -> m.code().equalsIgnoreCase(code)).findFirst().get();
		List<AlisResult> result = new LinkedList<>();
		if(test.summaryCode()!=null) result.add(AlisResult.builder().subCode(test.summaryCode()).text(dto.summary()).build());
		if(test.interpretationCode()!=null) result.add(AlisResult.builder().subCode(test.interpretationCode()).text(dto.interpretation()).build());
		for(var disease: test.diseases()) for(TestInfo.Gene gene: disease.genes()) {
			Optional<GenomeScreen.Gene> found = Arrays.stream(dto.diseases())
					.map(GenomeScreen.Disease::values).flatMap(Arrays::stream)
					.filter(g -> g.name().equals(gene.gene())).findAny();
			if (found.isPresent() && found.get().value() != null) result.add(AlisResult.builder().subCode(gene.code()).result1(found.get().value()?"발견":"미발견").build());
			else result.add(AlisResult.builder().subCode(gene.code()).result1("미발견").build());
		}
		if(test instanceof TestWithRiskScreen) {
			TestWithRiskScreen cast = (TestWithRiskScreen) test;
			for(TestInfo.Genotype gt: cast.genotypes()) {
				GenomeScreen.Genotype value = Arrays.stream(dto.genotypes())
						.filter(g->g.gene().equals(gt.gene())).filter(g->g.pos().equals(gt.pos()))
						.findFirst().orElse(null);
				result.add(AlisResult.builder().subCode(gt.code()).result1(value.genotype()).build());
			}
		}
		return result.stream().toArray(AlisResult[]::new);
	}

	@Override
	public AlisVariantResult[] variants(Request request, GenomeScreen interpretation, Class<GenomeScreen> clazz) {
		List<AlisVariantResult> values = new LinkedList<>();
		try {
			Variant[] variants = interpretation.variants();
			if(variants!=null) {
				for(int i = 0; i < variants.length; ++i) {
					Variant v = variants[i];
					values.addAll(map(v, i));
				}
			}
		} catch(Exception ignore){}
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

	private List<AlisVariantResult> map(Variant v, int i) {
		List<AlisVariantResult> values = new LinkedList<>();
		values.add(AlisVariantResult.builder().row(i).key("유전자").value(v.gene()).build());
		values.add(AlisVariantResult.builder().row(i).key("DNA 변이").value(v.hgvsc()).build());
		values.add(AlisVariantResult.builder().row(i).key("아미노산 변이").value(v.hgvsp()).build());
		values.add(AlisVariantResult.builder().row(i).key("접합자").value(v.zygosity()).build());
		values.add(AlisVariantResult.builder().row(i).key("변이의 분류").value(v.clazz()).build());
		return values;
	}
}
