package com.greencross.lims.report.mrd;

import com.gcgenome.lims.report.func.PageBuilder;
import com.gcgenome.lims.report.func.Painter;
import com.gcgenome.lims.test.mrd.TestInfo;
import com.greencross.lims.dto.interpretation.MrdScreen;
import com.greencross.lims.entity.Patient;
import com.greencross.lims.entity.Request;
import com.greencross.lims.entity.Sample;
import com.greencross.lims.report.HasSign;
import com.greencross.lims.report.ReportFactory;
import com.greencross.lims.report.SectionPage;
import com.greencross.lims.report.builder.LogoType;
import com.greencross.lims.report.builder.Sex;
import com.greencross.lims.report.kokr.SectionSign;
import com.greencross.lims.report.mrd.kokr.MrdScreenResourceKoKr;
import com.greencross.lims.report.mrd.kokr.MrdScreenTemplateKoKr;
import org.apache.commons.lang.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.greencross.lims.report.ReportUtil.*;

@Component
public class MrdScreenReportBuilder implements ReportFactory<MrdScreen> {
	private TestInfo test;
	private MrdScreenDto dto;
	private MrdScreenTemplate template;

	@Override
	public Class<MrdScreen> clazz() {
		return MrdScreen.class;
	}
	@Override
	public boolean match(Request request) {
		String service = request.pk().service();
		return Arrays.stream(TestInfo.SCREEN_TESTS).anyMatch(m->m.code().equalsIgnoreCase(service));
	}
	@Override
	public byte[] build(Request request, MrdScreen interpretation, Class clazz) throws IOException {
		PDDocument doc = new PDDocument();
		test = info(request.pk().service());
		dto = dto(test, request, interpretation);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		Painter<MrdScreenTemplate, MrdScreenDto> footer;
		Painter<MrdScreenTemplate, MrdScreenDto> sign;
		Painter<MrdScreenTemplate, MrdScreenDto> page;
		LogoType logoType;

		if(isLabsRequest(request.sample())) {
			logoType = LogoType.DEPENDENT;
			sign = new SectionSign<>(81);
			footer = new com.greencross.lims.report.kokr.SectionFooterLabs<>();
			template = new MrdScreenTemplateKoKr(new MrdScreenResourceKoKr(doc), test, logoType);
			page = new SectionPage<>(547, 81, template.resource().fontDefault());
		} else {
			logoType = LogoType.INDEPENDENT;
			sign = new SectionSign<>(65);
			footer = new com.greencross.lims.report.kokr.SectionFooterGenome<>();
			template = new MrdScreenTemplateKoKr(new MrdScreenResourceKoKr(doc), test, logoType);
			page = new SectionPage<>(547, 65, template.resource().fontDefault());
		}

		new PageBuilder<>(template, dto).add(new MrdScreenPage(template, footer, sign, page).page()).build().save(baos);
		return baos.toByteArray();
	}
	@Override
	public String buildLongFormText(Request request) {
		test = info(request.service().id());
		String headerSummary = String.format(SUMMARY_TABLE_FORMAT,
				StringUtils.center("Gene", 8),
				StringUtils.center("검출된 클론", 10),
				StringUtils.center("Estimated % of Clonal/Total {:CELL}s", 40));
		StringBuilder summary = new StringBuilder();
		for(MrdScreenDto.MrdScreenDtoGeneResult gene: dto.results().values()) {
			String line = String.format(SUMMARY_TABLE_FORMAT,
					StringUtils.center(gene.gene(), 8),
					StringUtils.center(nf.format(gene.clones().length), 10),
					StringUtils.center(df.format(gene.totalClonalCells()), 40));
			summary.append(line).append("\r\n");
		}
		StringBuilder mutationRate = new StringBuilder();
		if(dto.somaticMutations()!=null && dto.somaticMutations().size() > 0) {
			mutationRate.append("[Somatic hypermutation status]\r\n");
			for(var mutation: dto.somaticMutations()) {
				String clone = mutation.clone();
				String status = mutation.hyperMutation();
				String rate = mutation.mutationRate();
				if(rate!=null && status.contains("TEXT")) status = status.replace("TEXT", rate);
				mutationRate.append(clone).append(": ").append(status).append("\r\n");
			}
		}

		StringBuilder result = new StringBuilder();
		for(MrdScreenDto.MrdScreenDtoGeneResult gene: dto.results().values()) {
			result.append("----------------------------------------------------------------------------------------------------\r\n")
					.append(" 검출된 ").append(gene.gene()).append(" 클론: ");
			String resulText = gene.clones()!=null&&gene.clones().length>0?tmplInterpretationPositive:tmplInterpretationNegative;
			result.append(resulText.replace("%g", gene.gene()).replace("%d", nf.format(gene.clones().length)))
					.append("\r\n");
			String headerResult = String.format(RESULT_TABLE_FORMAT,
					StringUtils.center("No", 4),
					StringUtils.center("V region", 13),
					StringUtils.center("J region", 13),
					StringUtils.center("Length", 10),
					StringUtils.center("Clonal %g Depth".replace("%g", gene.gene()), 22),
					StringUtils.center("Clonal Cell Equiv.*", 22),
					StringUtils.center("Clonal/Total %cs**".replace("%c", test.cell()), 20));
			result.append("----------------------------------------------------------------------------------------------------\r\n")
					.append(headerResult).append("\r\n")
					.append("----------------------------------------------------------------------------------------------------\r\n");
			for(int i = 0; i < gene.clones().length; ++i) {
				var clone = gene.clones()[i];
				String line = String.format(RESULT_TABLE_FORMAT,
						StringUtils.center(nf.format(i+1), 4),
						StringUtils.center(clone.regionV(), 13),
						StringUtils.center(clone.regionJ(), 13),
						StringUtils.center(nf.format(clone.length()), 10),
						StringUtils.center(nf.format(clone.depth()), 22),
						StringUtils.center(nf.format(clone.equivalent(gene.lqic().depth())), 22),
						StringUtils.center(df.format(clone.coverage(gene.lqic().depth(), gene.bCells())), 20));
				result.append(line).append("\r\n");
			}
			result.append("----------------------------------------------------------------------------------------------------\r\n")
					.append(String.format(RESULT_INFO_FORMAT,
							StringUtils.center("Total %g Read Depth(X)".replace("%g", gene.gene()), 30),
							StringUtils.center(nf.format(gene.depth()), 20),
							StringUtils.center("Total " + test.cell() + " Count*", 30),
							StringUtils.center(nf.format(gene.bCells()), 20)))
					.append("\r\n")
					.append("----------------------------------------------------------------------------------------------------\r\n");
			String cmt1 = "* 검체당 " + test.cell() + " 100개의 DNA (LymphoQuant Internal Control, LQIC)를 혼합하여 측정된 근사치입니다.";
			String cmt2 = "** 전체 " + test.cell() + " 중 클론성 세포가 차지하는 비율을 환산한 근사치입니다.";
			result.append(cmt1).append("\r\n")
					.append(cmt2).append("\r\n\r\n");
			if(gene.interpretation()!=null && !gene.interpretation().isEmpty()) {
				result.append("----------------------------------------------------------------------------------------------------\r\n")
						.append("◇ Interpretation\r\n")
						.append(gene.interpretation())
						.append("\r\n\r\n");
			}
		}
		StringBuilder qc = new StringBuilder();
		for(MrdScreenDto.MrdScreenDtoGeneResult gene: dto.results().values()) {
			String readDepth = " Total " + gene.gene() + " Read Depth(X): ";
			String equivalent = " Estimated " + gene.gene() + " Total " + test.cell() + " Count(cell equivalents)**: ";
			qc.append(readDepth).append(nf.format(gene.depth())).append("\r\n")
					.append(equivalent).append(nf.format(gene.bCells())).append("\r\n");
		}
		String[][] labelNamePairs = HasSign.getSanitizedContributorsLabelsAndNames(template, test);

		StringBuilder limitations = new StringBuilder();
		for(String limitation: limitations(test)) limitations.append(" ● ").append(limitation).append("\r\n");
		return TEXT_TEMPLATE.replace("{:TEST-NAME}", test.name())
				.replace("{:CANCER-TYPE}", dto.cancerType())
				.replace("{:HEADER-SUMMARY}", headerSummary)
				.replace("{:SUMMARY-GENES}", summary.toString())
				.replace("{:MUTATION-RATE}", mutationRate.toString())
				.replace("{:RESULT-GENES}", result.toString())
				.replace("{:METHOD}", test.method())
				.replace("{:PIPELINE}", test.pipeline())
				.replace("{:PANEL}", test.panel())
				.replace("{:SEQUENCING}", test.sequencing())
				.replace("{:REFERENCE}", test.reference())
				.replace("{:INPUT-DNA}", nf.format(dto.inputDna()))
				.replace("{:TOTAL-NC}", nf.format(dto.nucleatedCells()))
				.replace("{:QC-GENES}", qc.toString())
				.replace("{:CELL}", test.cell())
				.replace("{:LIMITATIONS}", limitations.toString())
				.replace("{:INSPECTOR_LABEL}", labelNamePairs[0][0])
				.replace("{:INSPECTOR}", labelNamePairs[0][1])
				.replace("{:REPORTER_AND_REVIEWER_LABEL}", labelNamePairs[1][0])
				.replace("{:REPORTER}", labelNamePairs[1][1])
				.replace("{:REVIEWER}", labelNamePairs[2][1]);
	}

	@Override
	public String buildShortFormText(Request request) {
		test = info(request.service().id());
		String headerSummary = "Gene\t검출된 클론\tEstimated % of Clonal/Total {:CELL}s";
		StringBuilder summary = new StringBuilder();
		for(MrdScreenDto.MrdScreenDtoGeneResult gene: dto.results().values()) {
			String line = " " + gene.gene() + "\t" + nf.format(gene.clones().length) + "\t" + df.format(gene.totalClonalCells());
			summary.append(line).append("\r\n");
		}

		StringBuilder mutationRate = new StringBuilder();
		if(dto.somaticMutations()!=null && dto.somaticMutations().size() > 0) {
			mutationRate.append("[Somatic hypermutation status]\r\n");
			for(var mutation: dto.somaticMutations()) {
				String clone = mutation.clone();
				String status = mutation.hyperMutation();
				String rate = mutation.mutationRate();
				if(rate!=null && status.contains("TEXT")) status = status.replace("TEXT", rate);
				mutationRate.append(clone).append(": ").append(status).append("\r\n");
			}
		}

		StringBuilder result = new StringBuilder();
		for(MrdScreenDto.MrdScreenDtoGeneResult gene: dto.results().values()) {
			result.append(" 검출된 ").append(gene.gene()).append(" 클론: ");
			String resulText = gene.clones()!=null&&gene.clones().length>0?tmplInterpretationPositive:tmplInterpretationNegative;
			result.append(resulText.replace("%g", gene.gene()).replace("%d", nf.format(gene.clones().length)))
					.append("\r\n");
			String headerResult = "No\tV region\tJ region\tLength\tClonal %g Depth\tClonal Cell Equiv.*\tClonal/Total %cs**"
					.replace("%g", gene.gene())
					.replace("%c", test.cell());
			result.append(headerResult).append("\r\n");
			for(int i = 0; i < gene.clones().length; ++i) {
				var clone = gene.clones()[i];
				result.append(nf.format(i+1)).append("\t")
						.append(clone.regionV()).append("\t")
						.append(clone.regionJ()).append("\t")
						.append(nf.format(clone.length())).append("\t")
						.append(nf.format(clone.depth())).append("\t")
						.append(nf.format(clone.equivalent(gene.lqic().depth()))).append("\t")
						.append(df.format(clone.coverage(gene.lqic().depth(), gene.bCells())))
						.append("\r\n");
			}
			result.append("Total %g Read Depth(X): ".replace("%g", gene.gene())).append("\t")
					.append(nf.format(gene.depth())).append("\t")
					.append("Total " + test.cell() + " Count*: ").append("\t")
					.append(nf.format(gene.bCells()))
					.append("\r\n");
			String cmt1 = "* 검체당 " + test.cell() + " 100개의 DNA (LymphoQuant Internal Control, LQIC)를 혼합하여 측정된 근사치입니다.";
			String cmt2 = "** 전체 " + test.cell() + " 중 클론성 세포가 차지하는 비율을 환산한 근사치입니다.";
			result.append(cmt1).append("\r\n")
					.append(cmt2).append("\r\n\r\n");
			if(gene.interpretation()!=null && !gene.interpretation().isEmpty()) {
				result.append("◇ Interpretation\r\n")
						.append(gene.interpretation())
						.append("\r\n\r\n");
			}
		}
		StringBuilder qc = new StringBuilder();
		for(MrdScreenDto.MrdScreenDtoGeneResult gene: dto.results().values()) {
			String readDepth = " Total " + gene.gene() + " Read Depth(X): ";
			String equivalent = " Estimated " + gene.gene() + " Total " + test.cell() + " Count(cell equivalents)**: ";
			qc.append(readDepth).append(nf.format(gene.depth())).append("\r\n")
					.append(equivalent).append(nf.format(gene.bCells())).append("\r\n");
		}
		String[][] labelNamePairs = HasSign.getSanitizedContributorsLabelsAndNames(template, test);

		StringBuilder limitations = new StringBuilder();
		for(String limitation: limitations(test)) limitations.append(" ● ").append(limitation).append("\r\n");
		return TEXT_SHORT_TEMPLATE.replace("{:TEST-NAME}", test.name())
				.replace("{:CANCER-TYPE}", dto.cancerType())
				.replace("{:HEADER-SUMMARY}", headerSummary)
				.replace("{:SUMMARY-GENES}", summary.toString())
				.replace("{:MUTATION-RATE}", mutationRate.toString())
				.replace("{:RESULT-GENES}", result.toString())
				.replace("{:METHOD}", test.method())
				.replace("{:PIPELINE}", test.pipeline())
				.replace("{:PANEL}", test.panel())
				.replace("{:SEQUENCING}", test.sequencing())
				.replace("{:REFERENCE}", test.reference())
				.replace("{:INPUT-DNA}", nf.format(dto.inputDna()))
				.replace("{:TOTAL-NC}", nf.format(dto.nucleatedCells()))
				.replace("{:QC-GENES}", qc.toString())
				.replace("{:CELL}", test.cell())
				.replace("{:LIMITATIONS}", limitations.toString())
				.replace("{:INSPECTOR_LABEL}", labelNamePairs[0][0])
				.replace("{:INSPECTOR}", labelNamePairs[0][1])
				.replace("{:REPORTER_AND_REVIEWER_LABEL}", labelNamePairs[1][0])
				.replace("{:REPORTER}", labelNamePairs[1][1])
				.replace("{:REVIEWER}", labelNamePairs[2][1]);
	}
	TestInfo info(String service) {
		return Arrays.stream(TestInfo.SCREEN_TESTS).filter(m->m.code().equalsIgnoreCase(service)).findFirst().get();
	}
	private static final Pattern BIRTHDAT_PATTERN = Pattern.compile("^\\d{6}$");
	private static final Pattern BIRTHDAT_PATTERN2 = Pattern.compile("^(\\d{6})(-)*(\\d)$");
	private MrdScreenDto dto(TestInfo test, Request request, MrdScreen interpretation) {
		Objects.requireNonNull(request.sample(), "Sample cannot be null");
		Objects.requireNonNull(request.sample().patient(), "Patient cannot be null");

		var dto = new MrdScreenDto();
		Sample sample = request.sample();
		Patient patient = sample.patient();

		String patientCode = patient.code();
		if(patient.code()!=null && BIRTHDAT_PATTERN.matcher(patient.code()).find()) patientCode = patient.code() + "-*******";
		else if(patient.code()!=null && BIRTHDAT_PATTERN2.matcher(patient.code()).find()) {
			Matcher m = BIRTHDAT_PATTERN2.matcher(patient.code());
			m.find();
			patientCode = m.group(1) + "-" + m.group(3) + "******";
		}
		Map<String, MrdScreenDto.MrdScreenDtoGeneResult> results = new HashMap<>();
		for(MrdScreen.MrdScreenGeneResult r: interpretation.results()) {
			String gene = r.gene();
			var lqic = new MrdScreenDto.MrdScreenDtoCloneResult().depth(r.depthLqic()).length(r.lengthLqic().intValue());
			var clones = Arrays.stream(r.clones())
							   .map(c->new MrdScreenDto.MrdScreenDtoCloneResult().regionV(c.regionV())
																				 .regionJ(c.regionJ())
																				 .depth(c.depth())
																				 .length(c.length())
																				 .sequence(c.sequence()))
							   .toArray(MrdScreenDto.MrdScreenDtoCloneResult[]::new);
			for(int i = 0; i < clones.length; ++i) clones[i].no(String.valueOf(i+1));
			var m = new MrdScreenDto.MrdScreenDtoGeneResult().gene(r.gene())
					.depth(r.depthTotal())
					.lqic(lqic)
					.clones(clones)
					.interpretation(r.interpretation());
			results.put(gene, m);
		}
		List<MrdScreenDto.SomaticMutation> sm = new LinkedList<>();
		if(interpretation.somaticMutations()!=null) sm = Arrays.stream(interpretation.somaticMutations())
				.map(i->new MrdScreenDto.SomaticMutation().clone(i.clone()).hyperMutation(i.hyperMutation()).mutationRate(i.mutationRate()))
				.collect(Collectors.toList());
		dto.code(request.pk().service())
		   .somaticMutations(sm)
		   .cancerType(interpretation.cancerType())
		   .inputDna(interpretation.inputDna())
		   .results(results)
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
		request.customInfos().stream().filter(i->"TA0023".equals(i.code())).findFirst().ifPresent(barcode->dto.barcode(barcode.value()));
		return dto;
	}
	private final String tmplInterpretationPositive		= "미세잔존질환(MRD) 추적에 적합한 %g 클론이 %d개 검출되었습니다.";
	private final String tmplInterpretationNegative		= "미세잔존질환(MRD) 추적에 적합한 %g 클론이 검출되지 않았습니다.";

	private static final String SUMMARY_TABLE_FORMAT = "%1$-8s%2$-10s%3$-40s";
	private static final String RESULT_TABLE_FORMAT = "%1$-4s%2$-13s%3$-13s%4$-10s%5$-22s%6$-22s%7$-20s";
	private static final String RESULT_INFO_FORMAT = "%1$-30s%2$-20s%3$-30s%4$-20s";
	protected final NumberFormat nf = NumberFormat.getInstance();
	protected final DecimalFormat df = new DecimalFormat("#.##");
	private static final String TEXT_TEMPLATE = "◆ {:TEST-NAME}\r\n\r\n" +
			"◇ Cancer Type : {:CANCER-TYPE}\r\n\r\n" +
			"◇ 결과 요약\r\n" +
			"----------------------------------------------------------------------------------------------------\r\n" +
			"{:HEADER-SUMMARY}\r\n" +
			"----------------------------------------------------------------------------------------------------\r\n" +
			"{:SUMMARY-GENES}" +
			"----------------------------------------------------------------------------------------------------\r\n" +
			"{:MUTATION-RATE}\r\n" +
			"◇ 상세 결과\r\n" +
			"{:RESULT-GENES}" +
			"\r\n" +
			"◇ 검사 정보\r\n" +
			" 1. 검사 방법\r\n" +
			" Target enrichment method: {:METHOD}\r\n" +
			" Bioinformatic pipeline: {:PIPELINE}\r\n" +
			" Tested Panel: {:PANEL}\r\n" +
			" Massively Parallel Sequencing: {:SEQUENCING}\r\n" +
			" Reference Genome: {:REFERENCE}\r\n" +
			"\r\n" +
			" 2. QC 정보\r\n" +
			" Input DNA(ng): {:INPUT-DNA}\r\n" +
			" Estimated Total Nucleated Cells (cell equivalents)*: {:TOTAL-NC}\r\n" +
			"{:QC-GENES}" +
			" * Estimated DNA mass per 1 cell = 6.5pg\r\n" +
			" ** 검체당 {:CELL} 100개의 DNA(LymphoQuant Internal Control, LQIC)를 혼합하여 측정된 근사치입니다.\r\n" +
			"\r\n" +
			"3. 검사의 한계\r\n" +
			"{:LIMITATIONS}\r\n" +
			"\r\n" +
			"{:INSPECTOR_LABEL} {:INSPECTOR} / {:REPORTER_AND_REVIEWER_LABEL} {:REPORTER}, {:REVIEWER}";
	private static final String TEXT_SHORT_TEMPLATE = "◆ {:TEST-NAME}\r\n\r\n" +
			"◇ Cancer Type : {:CANCER-TYPE}\r\n\r\n" +
			"◇ 결과 요약\r\n" +
			"{:HEADER-SUMMARY}\r\n" +
			"{:SUMMARY-GENES}\r\n" +
			"{:MUTATION-RATE}\r\n" +
			"◇ 상세 결과\r\n" +
			"{:RESULT-GENES}" +
			"\r\n" +
			"◇ 검사 정보\r\n" +
			" 1. 검사 방법\r\n" +
			" Target enrichment method: {:METHOD}\r\n" +
			" Bioinformatic pipeline: {:PIPELINE}\r\n" +
			" Tested Panel: {:PANEL}\r\n" +
			" Massively Parallel Sequencing: {:SEQUENCING}\r\n" +
			" Reference Genome: {:REFERENCE}\r\n" +
			"\r\n" +
			" 2. QC 정보\r\n" +
			" Input DNA(ng): {:INPUT-DNA}\r\n" +
			" Estimated Total Nucleated Cells (cell equivalents)*: {:TOTAL-NC}\r\n" +
			"{:QC-GENES}" +
			" * Estimated DNA mass per 1 cell = 6.5pg\r\n" +
			" ** 검체당 {:CELL} 100개의 DNA(LymphoQuant Internal Control, LQIC)를 혼합하여 측정된 근사치입니다.\r\n" +
			"\r\n" +
			"3. 검사의 한계\r\n" +
			"{:LIMITATIONS}\r\n" +
			"\r\n" +
			"{:INSPECTOR_LABEL} {:INSPECTOR} / {:REPORTER_AND_REVIEWER_LABEL} {:REPORTER}, {:REVIEWER}";

	private static final String lblLimitationIgh = "IGH 클론성 염기서열은 검체 내 전체 read depth가 20,000 이상이면서 전체 read depth 중 최소 ≥ 2.5% 이상, 3순위 read depth의 2배 이상인 dominant sequence로 판정합니다.";
	private static final String lblLimitationIgk = "IGK 클론성 염기서열은 검체 내 전체 read depth가 20,000 이상이면서 전체 read depth 중 최소 ≥ 5% 이상, 3순위 read depth의 2배 이상인 dominant sequence로 판정합니다.";
	private static final String lblLimitationTrb = "TRB 클론성 염기서열은 검체 내 전체 read depth가 20,000 이상이면서 전체 read depth 중 최소 ≥ 2.5% 이상, 3순위 read depth의 2배 이상인 dominant sequence로 판정합니다.";
	private static final String lblLimitationTrg = "TRG 클론성 염기서열은 검체 내 전체 read depth가 20,000 이상이면서 전체 read depth 중 최소 ≥ 2.5% 이상, 3순위 read depth의 2배 이상인 dominant sequence로 판정합니다.";
	private static final String lblLimitationEtc1 = "Clonal process를 통해서 두 개 또는 그 이상의 clone 이 존재할 수 있습니다. 예를 들어 다발성 림프증식성 장애 (multiple lymphoproliferative disorders)나 a dominant population with a small sub-clonal population 인 경우 가능합니다. 이러한 사례는 임상적 배경과 같이 해석되어야 합니다.";
	private static final String lblLimitationEtc2 = "본 검사 결과는 임상, 조직학 및 면역 표현형 데이터의 맥락에서 해석되어야 합니다.";
	private static final String lblLimitationEtc3 = "본 검사는 clonal cell populations 의 100%를 식별하지 않습니다.";
	private static final String[] limitations(TestInfo testInfo) {
		String lblLimitationPcr = "PCR 증폭은 " + testInfo.cell() + "의 DNA양에 영향을 받으며 primer결합 부위 변이가 있는 경우 위음성을 보일 가능성이 있습니다.";
		List<String> limitations = new LinkedList<>();
		if(Arrays.stream(testInfo.genes()).anyMatch("IGH"::equalsIgnoreCase)) limitations.add(lblLimitationIgh);
		if(Arrays.stream(testInfo.genes()).anyMatch("IGK"::equalsIgnoreCase)) limitations.add(lblLimitationIgk);
		if(Arrays.stream(testInfo.genes()).anyMatch("TRB"::equalsIgnoreCase)) limitations.add(lblLimitationTrb);
		if(Arrays.stream(testInfo.genes()).anyMatch("TRG"::equalsIgnoreCase)) limitations.add(lblLimitationTrg);
		limitations.add(lblLimitationPcr);
		limitations.add(lblLimitationEtc1);
		limitations.add(lblLimitationEtc2);
		limitations.add(lblLimitationEtc3);
		return limitations.toArray(new String[0]);
	}
}
