package com.greencross.lims.report.hrd;

import com.gcgenome.lims.report.func.PageBuilder;
import com.gcgenome.lims.report.func.Painter;
import com.gcgenome.lims.test.hrd.TestInfo;
import com.greencross.lims.dto.interpretation.Hrd;
import com.greencross.lims.entity.Patient;
import com.greencross.lims.entity.Request;
import com.greencross.lims.entity.Sample;
import com.greencross.lims.report.HasSign;
import com.greencross.lims.report.ReportFactory;
import com.greencross.lims.report.SectionPage;
import com.greencross.lims.report.builder.LogoType;
import com.greencross.lims.report.builder.Sex;
import com.greencross.lims.report.hrd.enus.HrdHeaderEnUs;
import com.greencross.lims.report.hrd.enus.HrdResourceEnUs;
import com.greencross.lims.report.hrd.enus.HrdTemplateEnUs;
import com.greencross.lims.report.hrd.kokr.HrdHeaderKoKr;
import com.greencross.lims.report.hrd.kokr.HrdResourceKoKr;
import com.greencross.lims.report.hrd.kokr.HrdTemplateKoKr;
import com.greencross.lims.report.kokr.SectionSign;
import org.apache.commons.lang.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.greencross.lims.report.ReportUtil.*;

@Component
public class HrdReportBuilder implements ReportFactory<Hrd> {
	private HrdDto dto;
	private TestInfo test;
	private HrdTemplate template;
	@Override
	public Class<Hrd> clazz() {
		return Hrd.class;
	}
	@Override
	public boolean match(Request request) {
		String service = request.pk().service();
		return Arrays.stream(TestInfo.TESTS).anyMatch(m->m.code().equalsIgnoreCase(service));
	}
	@Override
	public byte[] build(Request request, Hrd interpretation, Class clazz) throws IOException {
		PDDocument doc = new PDDocument();
		test = info(request.pk().service());
		dto = dto(test, request, interpretation);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		Painter<HrdTemplate, HrdDto> header;
		Painter<HrdTemplate, HrdDto> footer;
		Painter<HrdTemplate, HrdDto> sign;
		Painter<HrdTemplate, HrdDto> page;
		HrdResource resource;
		LogoType logoType;

		if("KOKR".equalsIgnoreCase(test.i18n())) {
			if(isLabsRequest(request.sample())) {
				logoType = LogoType.DEPENDENT;
				footer = new com.greencross.lims.report.kokr.SectionFooterLabs<>();
				sign = new SectionSign<>(81);
				template = new HrdTemplateKoKr(new HrdResourceKoKr(doc), test, logoType);
				page = new SectionPage<>(539, 79, template.resource().fontDefault());
			} else {
				logoType = LogoType.INDEPENDENT;
				footer = new com.greencross.lims.report.kokr.SectionFooterGenome<>();
				sign = new SectionSign<>(65);
				resource = new HrdResourceKoKr(doc);
				template = new HrdTemplateKoKr(resource, test, logoType);
				page = new SectionPage<>(560, 63, template.resource().fontDefault());
			}
			header = new HrdHeaderKoKr(template).header();
		} else {
			if(isLabsRequest(request.sample())) {
				logoType = LogoType.DEPENDENT;
				footer = new com.greencross.lims.report.enus.SectionFooterLabs<>();
				sign = new com.greencross.lims.report.enus.SectionSign<>(80);
				template = new HrdTemplateEnUs(new HrdResourceEnUs(doc), test, logoType);
				page = new SectionPage<>(542, 80, template.resource().fontDefault());
			} else {
				logoType = LogoType.INDEPENDENT;
				footer = new com.greencross.lims.report.enus.SectionFooterGenomeLabs<>();
				sign = new com.greencross.lims.report.enus.SectionSign<>(65);
				template = new HrdTemplateEnUs(new HrdResourceEnUs(doc), test, logoType);
				page = new SectionPage<>(560, 65, template.resource().fontDefault());
			}
			header = new HrdHeaderEnUs(template).header();
		}
		new PageBuilder<>(template, dto).add(new HrdPage(template, header, footer, sign, page).page()).build().save(baos);
		return baos.toByteArray();
	}
	@Override
	public String buildLongFormText(Request request) {
		test = info(request.service().id());
		String resultSummary = "-";
		HrdDto.HrdDtoGeneResult brca = dto.details().getOrDefault("BRCA", null);
		HrdDto.HrdDtoGeneResult tier1 = dto.details().getOrDefault("Tier1", dto.details().get("TIER1"));
		HrdDto.HrdDtoGeneResult tier2 = dto.details().getOrDefault("Tier2", dto.details().get("TIER2"));
		String resultGi = "-";
		if(HrdDto.Result.P.equals(dto.gi())) resultGi = "양성";
		else if(HrdDto.Result.N.equals(dto.gi())) resultGi = "음성";
		String resultBrcaSummary = "-";
		if(HrdDto.Result.P.equals(dto.brcaResult())) resultBrcaSummary ="양성";
		else if(!"-".equalsIgnoreCase(resultGi)) resultBrcaSummary = "음성";
		if("양성".equalsIgnoreCase(resultGi) || "양성".equalsIgnoreCase(resultBrcaSummary)) resultSummary = "양성";
		else if("음성".equalsIgnoreCase(resultGi)) resultSummary ="음성";

		String header = String.format(VARIANT_TABLE_FORMAT, StringUtils.center(VARIANT_TABLE_HEADER[0], 4), StringUtils.center(VARIANT_TABLE_HEADER[1], 8),
				StringUtils.center(VARIANT_TABLE_HEADER[2], 15), StringUtils.center(VARIANT_TABLE_HEADER[3],15),
				StringUtils.center(VARIANT_TABLE_HEADER[4], 12), StringUtils.center(VARIANT_TABLE_HEADER[5], 12),
				StringUtils.center(VARIANT_TABLE_HEADER[6], 12));
		String[][] labelNamePairs = HasSign.getSanitizedContributorsLabelsAndNames(template, test);
		return TEXT_TEMPLATE.replace("{:CANCER-TYPE}", dto.cancerType())
				.replace("{:HRD}", resultSummary)
				.replace("{:GI}", resultGi)
				.replace("{:GI-SCORE}", String.valueOf(dto.giScore()))
				.replace("{:INTERPRETATION}", dto.interpretation())
				.replace("{:HEADER}", header)
				.replace("{:BRCA-RESULT}", resultBrcaSummary)
				.replace("{:BRCA-VARIANTS}", variantRows(brca))
				.replace("{:BRCA-INTERPRETATION}", brca.interpretation())
				.replace("{:TIER1-RESULT}", (tier1!=null && tier1.variants()!=null)?String.valueOf(tier1.variants().length):"0")
				.replace("{:TIER1-VARIANTS}", variantRows(tier1))
				.replace("{:TIER1-INTERPRETATION}", tier1.interpretation())
				.replace("{:TIER2-RESULT}", (tier2!=null && tier2.variants()!=null)?String.valueOf(tier2.variants().length):"0")
				.replace("{:TIER2-VARIANTS}", variantRows(tier2))
				.replace("{:TIER2-INTERPRETATION}", tier2.interpretation())
				.replace("{:SNV}", dto.snv())
				.replace("{:CNV}", dto.cnv())
				.replace("{:ESSENTIALS}", Arrays.stream(test.genesEssential()).map(TestInfo.Gene::symbol).collect(Collectors.joining(", ")))
				.replace("{:ADDITIONALS}", Arrays.stream(test.genesAdditional()).map(TestInfo.Gene::symbol).collect(Collectors.joining(", ")))
				.replace("{:INSPECTOR_LABEL}", labelNamePairs[0][0])
				.replace("{:INSPECTOR}", labelNamePairs[0][1])
				.replace("{:REPORTER_AND_REVIEWER_LABEL}", labelNamePairs[1][0])
				.replace("{:REPORTER}", labelNamePairs[1][1])
				.replace("{:REVIEWER}", labelNamePairs[2][1]);
	}
	private String variantRows(HrdDto.HrdDtoGeneResult result) {
		StringBuilder variants = new StringBuilder();
		if(result!=null && result.variants()!=null && result.variants().length > 0) {
			for(int i = 0; i < result.variants().length; ++i) {
				HrdDto.HrdVariantResult variant = result.variants()[i];
				variants.append(String.format(VARIANT_TABLE_FORMAT, StringUtils.center(String.valueOf(i+1), 4), StringUtils.center(variant.gene(), 8),
						StringUtils.center(variant.hgvsc()!=null?variant.hgvsc():variant.dna(), 15),
						StringUtils.center(variant.hgvsp()!=null?variant.hgvsp():variant.protein(),15),
						StringUtils.center(variant.vaf()!=null? NumberFormat.getInstance().format(variant.vaf()):"-", 12),
						StringUtils.center(variant.depth()!=null?NumberFormat.getInstance().format(variant.depth()):"-", 12),
						StringUtils.center(variant.cosmic(), 12)));
			}
		} else variants.append(StringUtils.center("No identified variant", 74)).append("\r\n");
		return variants.toString();
	}
	@Override
	public String buildShortFormText(Request request) {
		return null;
	}
	private TestInfo info(String service) {
		return Arrays.stream(TestInfo.TESTS).filter(m->m.code().equalsIgnoreCase(service)).findFirst().get();
	}
	private static final Pattern BIRTHDAT_PATTERN = Pattern.compile("^\\d{6}$");
	private static final Pattern BIRTHDAT_PATTERN2 = Pattern.compile("^(\\d{6})(-)*(\\d)$");
	private HrdDto dto(TestInfo test, Request request, Hrd interpretation) {
		Objects.requireNonNull(request.sample(), "Sample cannot be null");
		Objects.requireNonNull(request.sample().patient(), "Patient cannot be null");

		var dto = new HrdDto();
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
				.cancerType(interpretation.cancerType()).gi(HrdDto.Result.fromInterpretation(interpretation.gi()))
				.snv(interpretation.snv())
				.cnv(interpretation.cnv())
				.brcaResult(HrdDto.Result.fromInterpretation(interpretation.brca()))
				.giScore(interpretation.giScore())
				.interpretation(interpretation.interpretation())
				.patientName(patient.name())
				.medicalRecordNumber(patient.mrn())
				.age(patient.age())
				.birthDate(patient.birth())
				.collectionDate(request.dateSampling())
				.patientInfo(request.info())
				.physician(request.physician())
				.department(request.customerDeptName())
				.ward(request.ward())
				.sex(Sex.from(patient.sex()))
				.specimenType(sample.sampleType())
				.receiptDate(request.dateRequest())
				.reportDate(LocalDate.now())
				.patientCode(patientCode)
				.medicalInstitution(determineMedicalInstitution(test::i18n, request, patient))
				.requestNumber(determineRequestNumber(sample));

		if(interpretation.results()!=null) {
			Arrays.stream(interpretation.results()).forEach(gene->{
				var result = new HrdDto.HrdDtoGeneResult();
				result.variants(Arrays.stream(gene.variants()).map(HrdReportBuilder::map).toArray(HrdDto.HrdVariantResult[]::new))
						.interpretation(gene.interpretation());
				dto.details().put(gene.tier(), result);
			});
		}
		request.customInfos().stream().filter(i->"TA0023".equals(i.code())).findFirst().ifPresent(barcode->dto.barcode(barcode.value()));
		request.customInfos().stream().filter(i->"TA0028".equals(i.code())).findFirst().ifPresent(relation->dto.relation(relation.value()));
		return dto;
	}

	private static HrdDto.HrdVariantResult map(Hrd.Variant dto) {
		return new HrdDto.HrdVariantResult()
				.gene(dto.gene())
				.hgvsc(dto.hgvsc())
				.hgvsp(dto.hgvsp())
				.dna(dto.dna())
				.protein(dto.protein())
				.vaf(dto.vaf())
				.depth(dto.depth())
				.cosmic(dto.cosmicId());
	}
	private static final String TEXT_TEMPLATE =
			"◆ 상동 재조합 결핍 검사(그린플랜 HRD)\r\n" +
					"◇ 검사목적질환: {:CANCER-TYPE}\r\n" +
					"◇ 상동 재조합 결핍(HRD) 검사결과: {:HRD}\r\n" +
					"◇ 유전체 불안정성 결과: {:GI}\r\n" +
					"◇ 유전체 불안정성 점수: {:GI-SCORE}\r\n" +
					"◇ 유전체 불안정성 결과해석\r\n" +
					"{:INTERPRETATION}\r\n\r\n" +
					"◇ BRCA 검사결과: {:BRCA-RESULT}\r\n" +
					"◇ BRCA 변이\r\n" +
					"--------------------------------------------------------------------------------\r\n" +
					"{:HEADER}"+
					"--------------------------------------------------------------------------------\r\n" +
					"{:BRCA-VARIANTS}"+
					"--------------------------------------------------------------------------------\r\n" +
					"◇ BRCA 결과해석\r\n" +
					"{:BRCA-INTERPRETATION}\r\n\r\n" +
					"◇ Tier 1 검사결과: {:TIER1-RESULT}\r\n" +
					"◇ Tier 1 변이\r\n" +
					"--------------------------------------------------------------------------------\r\n" +
					"{:HEADER}"+
					"--------------------------------------------------------------------------------\r\n" +
					"{:TIER1-VARIANTS}"+
					"--------------------------------------------------------------------------------\r\n" +
					"◇ Tier 1 결과해석\r\n" +
					"{:TIER1-INTERPRETATION}\r\n\r\n" +
					"◇ Tier 2 검사결과: {:TIER2-RESULT}\r\n" +
					"◇ Tier 2 변이\r\n" +
					"--------------------------------------------------------------------------------\r\n" +
					"{:HEADER}"+
					"--------------------------------------------------------------------------------\r\n" +
					"{:TIER2-VARIANTS}"+
					"--------------------------------------------------------------------------------\r\n" +
					"◇ Tier 2 결과해석\r\n" +
					"{:TIER2-INTERPRETATION}\r\n\r\n" +
					"◇ QC 정보\r\n" +
					"DNA(SNV): {:SNV}\r\n" +
					"DNA(CNV): {:CNV}\r\n\r\n" +
					"◇ 검사 정보\r\n" +
					"본 검사는 최신 유전자 분석 기법인 차세대 염기서열 분석을 기반으로 유전체 불안정성과 BRCA 유전자 변이를 확인하여 상동재조합결핍 여부를 검사합니다.\r\n" +
					"1. 상동재조합결핍(Homologous recombination deficiency; HRD)이란?\r\n" +
					" 일반적으로 세포에서 DNA 손상이 일어나게 되면, DNA 복구 과정을 통해 DNA 손상을 복구합니다. 암세포에서는 이러한 DNA 손상이 제대로 복구되지 않고 계속 분열하게 됩니다. DNA 복구 과정 중 BRCA 유전자 이상 등과 같이 상동 재조합 기능에 문제가 생겨 DNA 복구가 일어나지 않는 경우를 상동재조합결핍(HRD)이라고 하며, 상동재조합결핍에 의한 종양은 PARP 억제제와 같은 특정 약물을 이용한 치료 효과가 좋다는 특징이 있습니다.\r\n" +
					"2. PARP 억제제(PARP inhibitor)\r\n" +
					" PARP 억제제는 난소암에 이용되는 대표적인 항암제로 DNA 복구 과정에 관여하는 PARP 단백질을 억제시키는 항암제로 DNA 복구 능력이 있는 정상 세포에서는 독성이 나타나지 않지만 DNA 복구 능력이 정상적이지 않은 암세포에서 특이적으로 작용하는 항암제입니다. 이러한 PARP 억제제는 특히 BRCA 유전자 이상과 같은 상동재조합결핍이 나타난 종양에서 특히 효과적으로 나타납니다.\r\n\r\n" +
					"◇ 검사의 한계\r\n" +
					"- 본 검사는 염기서열분석법으로 시행되었으며, BRCA1, BRCA2 유전자의 변이와 유전체 불안정성 지표를 검사하며, 검사에 포함되지 않은 영역에 존재하는 변이는 검출할 수 없습니다.\r\n" +
					"- BRCA1,BRCA2 유전자의 SNV 및 small indel 검출 한계는 약 5%입니다.\r\n" +
					"- 일부 target region은 coverage가 떨어질 가능성이 있습니다.\r\n" +
					"- 본 검사로 germline 변이와 somatic 변이를 감별할 수 없으며 variant allele frequency가 50% 혹은 100%에 가까운 경우 germline variant의 가능성을 배제할 수 없습니다.\r\n\r\n" +
					"◇ 참고문헌\r\n" +
					"1. Br J Cancer. 2018 Nov;119(11):1401-1409.\r\n" +
					"2. Mol Cancer Res. 2018 Jul;16(7):1103-1111.\r\n" +
					"3. N Engl J Med. 2019 Dec 19;381(25):2391-2402.\r\n" +
					"4. N Engl J Med. 2019 Dec 19;381(25):2416-2428.\r\n\r\n" +
					"◇ 유전자 정보\r\n" +
					"- 필수 유전자 목록: {:ESSENTIALS}\r\n" +
					"- 추가 유전자 목록: {:ADDITIONALS}\r\n" +
					"\r\n" +
					"{:INSPECTOR_LABEL} {:INSPECTOR} / {:REPORTER_AND_REVIEWER_LABEL} {:REPORTER}, {:REVIEWER}";
	private static final String VARIANT_TABLE_FORMAT = "%1$-4s%2$-8s%3$-15s%4$-15s%5$-12s%6$-12s%7$-12s\r\n";
	private static final String[] VARIANT_TABLE_HEADER = new String[] {"No.", "Gene", "DNA", "Protein", "VAF(%)", "Depth(X)", "COSMIC ID"};

}
