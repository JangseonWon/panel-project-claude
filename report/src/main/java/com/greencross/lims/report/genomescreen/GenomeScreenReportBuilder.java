package com.greencross.lims.report.genomescreen;

import com.gcgenome.lims.report.func.Painter;
import com.gcgenome.lims.test.genomescreen.TestInfo;
import com.gcgenome.lims.test.genomescreen.TestWithRiskScreen;
import com.greencross.lims.dto.interpretation.GenomeScreen;
import com.greencross.lims.entity.Patient;
import com.greencross.lims.entity.Request;
import com.greencross.lims.entity.Sample;
import com.greencross.lims.report.InterpretationDAO;
import com.greencross.lims.report.ReportFactory;
import com.greencross.lims.report.SectionPage;
import com.greencross.lims.report.builder.LogoType;
import com.greencross.lims.report.builder.Sex;
import com.greencross.lims.report.genomescreen.enus.*;
import com.greencross.lims.report.genomescreen.kokr.*;
import com.greencross.lims.report.kokr.SectionSign;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.greencross.lims.report.ReportUtil.*;

@Component
public class GenomeScreenReportBuilder implements ReportFactory<GenomeScreen> {
	private final InterpretationDAO interpretationDAO;
	public GenomeScreenReportBuilder(InterpretationDAO interpretationDAO) {
		this.interpretationDAO = interpretationDAO;
	}

	@Override
	public Class<GenomeScreen> clazz() {
		return GenomeScreen.class;
	}
	private TestInfo info(String service) {
		for(TestInfo test: TestInfo.TESTS) if(test.code().equalsIgnoreCase(service)) return test;
		return null;
	}
	@Override
	public boolean match(Request request) {
		for (TestInfo test : TestInfo.TESTS) if (test.code().equalsIgnoreCase(request.pk().service())) return true;
		return false;
	}
	private GenomeScreenTemplate<?> template(TestInfo test) throws IOException {
		PDDocument doc = new PDDocument();
		if(TestWithRiskScreen.N087 == test || TestWithRiskScreen.J020 == test || TestWithRiskScreen.ON087 == test)			return new GenomeScreenTemplateN087KoKr(new GenomeScreenResourceN087KoKr(doc), test);
		else if(TestWithRiskScreen.N088 == test || TestWithRiskScreen.J021 == test || TestWithRiskScreen.ON088 == test)	return new GenomeScreenTemplateN088KoKr(new GenomeScreenResourceN088KoKr(doc), test);
		else if(TestInfo.N089 == test || TestInfo.J019 == test || TestInfo.N112 == test|| TestInfo.N075 == test)	return new GenomeScreenTemplateN089KoKr(new GenomeScreenResourceN089KoKr(test, doc), test);
		else if(TestInfo.ON089 == test)	return new GenomeScreenTemplateN089EnUs(new GenomeScreenResourceN089EnUs(doc), test);
		else if(TestInfo.N185 == test)	return new GenomeScreenTemplateN185KoKr(new GenomeScreenResourceN089KoKr(test, doc), test);
		else if(TestInfo.N090 == test || TestInfo.J018 == test)	return new GenomeScreenTemplateN090KoKr(new GenomeScreenResourceN090KoKr(doc), test);
		else if(TestInfo.N074 == test)	return new GenomeScreenTemplateN074KoKr(new GenomeScreenResourceN090KoKr(doc), test);
		else if(TestInfo.ON090 == test)return new GenomeScreenTemplateN090EnUs(new GenomeScreenResourceN090EnUs(doc), test);
		else if(TestInfo.N101 == test || TestInfo.N111 == test)	return new GenomeScreenTemplateN101KoKr(new GenomeScreenResourceN101KoKr(doc), test);
		else return null;
	}
	private GenomeScreenPageBuilder<?> builder(Request request, TestInfo test, GenomeScreenTemplate<?> template, GenomeScreenDto dto, LogoType logoType,
			Painter<GenomeScreenTemplate<GenomeScreenResource>, GenomeScreenDto> sign,
			Painter<GenomeScreenTemplate<GenomeScreenResource>, GenomeScreenDto> footer,
			Painter<GenomeScreenTemplate<GenomeScreenResource>, GenomeScreenDto> page) {
		if(TestWithRiskScreen.N087 == test || TestWithRiskScreen.J020 == test || TestWithRiskScreen.ON087 == test) 		return new GenomeScreenN087((GenomeScreenTemplateN087<?>) template, dto, logoType, sign, footer, page);
		else if(TestWithRiskScreen.N088 == test || TestWithRiskScreen.J021 == test || TestWithRiskScreen.ON088 == test)	return new GenomeScreenN088((GenomeScreenTemplateN088<?>) template, dto, logoType, sign, footer, page);
		else if(TestInfo.N089 == test || TestInfo.J019 == test || TestInfo.N112 == test || TestInfo.N075 == test) return new GenomeScreenN089((GenomeScreenTemplateN089<?>) template, dto, new SectionHeaderKoKr(), logoType, sign, footer, page, SectionGeneListKoKr::new);
		else if(TestInfo.ON089 == test) return new GenomeScreenN089((GenomeScreenTemplateN089<?>) template, dto, new SectionHeaderEnUs(), logoType, sign, footer, page, SectionGeneList::new);
		else if(TestInfo.N185 == test) return new GenomeScreenN185((GenomeScreenTemplateN089<?>) template, dto, new SectionHeaderKoKr(), logoType, sign, footer, page, SectionGeneListKoKr::new);
		else if(TestInfo.N090 == test || TestInfo.J018 == test) return new GenomeScreenN090((GenomeScreenTemplateN090<?>) template, dto, new SectionHeaderKoKr(), logoType, sign, footer, page, SectionGeneListKoKr::new);
		else if(TestInfo.N074 == test) return new GenomeScreenN090((GenomeScreenTemplateN074<?>) template, dto, new SectionHeaderKoKr(), logoType, sign, footer, page, SectionGeneListKoKr::new);
		else if(TestInfo.ON090 == test) return new GenomeScreenN090((GenomeScreenTemplateN090<?>) template, dto, new SectionHeaderEnUs(), logoType, sign, footer, page, SectionGeneList::new);
		else if(TestInfo.N101 == test || TestInfo.N111 == test) return new GenomeScreenN101((GenomeScreenTemplateN101<?>) template, dto, logoType, sign, footer, page);
		else return null;
	}
	@Override
	public byte[] build(Request request, GenomeScreen value, Class<GenomeScreen> clazz) throws IOException {
		TestInfo test = info(request.pk().service());
		GenomeScreenTemplate<?> template = template(test);
		GenomeScreenDto dto = dto(test, request, value, template);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		Painter<GenomeScreenTemplate<GenomeScreenResource>, GenomeScreenDto> sign;
		Painter<GenomeScreenTemplate<GenomeScreenResource>, GenomeScreenDto> footer;
		Painter<GenomeScreenTemplate<GenomeScreenResource>, GenomeScreenDto> page;
		GenomeScreenResource resource = Objects.requireNonNull(template).resource();
		LogoType logoType;

		if("KOKR".equalsIgnoreCase(test.i18n())) {
			if(isLabsRequest(request.sample())) {
				logoType = LogoType.ASSOCIATED;
				sign = new SectionSign<>(65);
				footer = new com.greencross.lims.report.kokr.SectionFooterGenomeLabs<>();
				page = new SectionPage<>(547, 65, resource.fontDefault());
			} else {
				logoType = LogoType.INDEPENDENT;
				sign = new SectionSign<>(65);
				footer = new com.greencross.lims.report.kokr.SectionFooterGenome<>();
				page = new SectionPage<>(547, 65, resource.fontDefault());
			}
		} else {
			logoType = LogoType.INDEPENDENT;
			sign = new com.greencross.lims.report.enus.SectionSign<>(65);
			footer = new com.greencross.lims.report.enus.SectionFooterGenome<>();
			page = new SectionPage<>(565, 65, resource.fontDefault());
		}
		builder(request, test, template, dto, logoType, sign, footer, page).build().save(baos);
		return baos.toByteArray();
	}

	@Override
	public String buildLongFormText(Request request) throws IOException {
		return null;
	}

	@Override
	public String buildShortFormText(Request request) throws IOException {
		return null;
	}

	private static final Pattern BIRTHDAT_PATTERN = Pattern.compile("^\\d{6}$");
	private static final Pattern BIRTHDAT_PATTERN2 = Pattern.compile("^(\\d{6})(-)*(\\d)$");
	private GenomeScreenWithRiskScreenDto dto(GenomeScreen value, RiskScreenTemplate<?> template) {
		var dto = new GenomeScreenWithRiskScreenDto().variantsRiskScreen(new HashMap<>());
		if(value.genotypes()!=null) for(var disease: template.riskscreens()) for(var sub: disease.subs()) for(var gene: sub.genes()) for(var snv: gene.snvs()) {
			String genotype = Arrays.stream(value.genotypes())
									.filter(g->g.gene().equals(gene.name()))
									.filter(g->g.pos().equals(snv.pos()))
									.map(g->g.genotype())
									.findFirst().orElse(null);
			dto.variantsRiskScreen().put(snv, genotype);
		}
		return dto;
	}
	private GenomeScreenDto dto(TestInfo test, Request request, GenomeScreen value, GenomeScreenTemplate<?> template) {
		Objects.requireNonNull(request.sample(), "Sample cannot be null");
		Objects.requireNonNull(request.sample().patient(), "Patient cannot be null");

		GenomeScreenDto dto = (template instanceof RiskScreenTemplate)?dto(value, (RiskScreenTemplate<?>)template):new GenomeScreenDto();
		Sample sample = request.sample();
		Patient patient = sample.patient();

		String patientCode = patient.code();
		if(patient.code()!=null && BIRTHDAT_PATTERN.matcher(patient.code()).find()) patientCode = patient.code() + "-*******";
		else if(patient.code()!=null && BIRTHDAT_PATTERN2.matcher(patient.code()).find()) {
			Matcher m = BIRTHDAT_PATTERN2.matcher(patient.code());
			m.find();
			patientCode = m.group(1) + "-" + m.group(3) + "******";
		}
		dto.code(request.pk().service())
		   .summary(value.summary())
		   .interpretation(value.interpretation())
		   .patientName(patient.name())
		   .medicalRecordNumber(patient.mrn())
		   .age(patient.age())
		   .birthDate(patient.birth())
		   .patientInfo(request.info())
		   .physician(request.physician())
		   .department(request.customerDeptName())
		   .ward(request.ward())
		   .collectionDate(request.dateSampling())
		   .sex(Sex.from(patient.sex()))
		   .specimenType(sample.sampleType())
		   .receiptDate(request.dateRequest())
		   .reportDate(LocalDate.now())
		   .patientCode(patientCode)
			.medicalInstitution(determineMedicalInstitution(test::i18n, request, patient))
			.requestNumber(determineRequestNumber(sample));

		if(dto.medicalInstitution().startsWith("건강관리협회")) dto.patientName(dto.medicalRecordNumber());

		Map<GenomeScreenTemplate.DiseaseSub, Map<GenomeScreenTemplate.Gene, Boolean>> diseases = new HashMap<>();
		for(var category: template.diseases()) {
			for (var disease : category.subs()) {
				Enum ed = (Enum)disease;
				diseases.put(disease, new HashMap<>());
				for (var gene : disease.genes()) {
					Optional<GenomeScreen.Gene> found = Arrays.stream(value.diseases()).filter(d-> d.name()
									.replace(" ", "_")
									.replace("-", "_").equals(ed.name()))
							.map(GenomeScreen.Disease::values).flatMap(Arrays::stream).filter(g -> g.name().equals(gene.name())).findAny();
					if (found.isPresent() && found.get().value() != null) diseases.get(disease).put(gene, found.get().value());
					else throw new RuntimeException("Can't find Gene value:" + disease + ", " + gene);
				}
			}
		}
		dto.diseases(diseases);
		Map<GenomeScreenTemplate.Gene, List<GenomeScreenDto.Variant>> values = new HashMap<>();
		Arrays.stream(value.variants()).map(this::map).forEach(v->{
			for(var disease: template.diseases()) for(var sub: disease.subs()) for(var gene: sub.genes())
				if(gene.name().equalsIgnoreCase(v.gene()) /*&& ("PV".equals(v.clazz()) || "LPV".equals(v.clazz()) || "VUS".equals(v.clazz()))*/) {
					if(!values.containsKey(gene)) values.put(gene, new LinkedList<>());
					values.get(gene).add(v);
				}
		});
		dto.variants(values);
		request.customInfos().stream().filter(i->"TA0023".equals(i.code())).findFirst().ifPresent(barcode->dto.barcode(barcode.value()));
		return dto;
	}

	private GenomeScreenDto.Variant map(GenomeScreen.Variant dto) {
		return new GenomeScreenDto.Variant().gene(dto.gene()).dnaChange(dto.hgvsc()).predictedAa(dto.hgvsp()).zygosity(dto.zygosity()).clazz(dto.clazz());
	}
}
