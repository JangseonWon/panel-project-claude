package com.greencross.lims.report.mrd;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gcgenome.lims.dto.interpretation.Mrd;
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
import com.greencross.lims.report.mrd.kokr.MrdResourceKoKr;
import com.greencross.lims.report.mrd.kokr.MrdTemplateKoKr;
import org.apache.commons.lang.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.greencross.lims.report.ReportUtil.*;

@Component
public class MrdReportBuilder implements ReportFactory<Mrd> {
	private final ObjectMapper om;
	private final MrdScreenReportBuilder screenBuilder;
	private final NumberFormat fmtPct = NumberFormat.getInstance();
	private TestInfo test;
	private MrdTemplate template;
	private MrdDto dto;
	private boolean hasScreen = false;

	public MrdReportBuilder(ObjectMapper om) {
		this.om = om;
		this.screenBuilder = new MrdScreenReportBuilder() {
			TestInfo info(String service) {
				return MrdReportBuilder.this.info(service);
			}
		};
        fmtPct.setMaximumFractionDigits(2);
        fmtPct.setMinimumFractionDigits(2);
	}

	@Override
	public Class<Mrd> clazz() {
		return Mrd.class;
	}
	@Override
	public boolean match(Request request) {
		String service = request.pk().service();
		return Arrays.stream(TestInfo.TESTS).anyMatch(m->m.code().equalsIgnoreCase(service));
	}
	@Override
	public byte[] build(Request request, Mrd interpretation, Class clazz) throws IOException {
		if(interpretation.histories() == null && interpretation.results()!=null) {
			MrdScreen cast = om.readValue(om.writeValueAsString(interpretation), MrdScreen.class);
			hasScreen = true;
			return screenBuilder.build(request, cast, clazz);
		}

		PDDocument doc = new PDDocument();
		test = info(request.pk().service());
		dto = dto(test, request, interpretation);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		Painter<MrdTemplate, MrdDto> footer = null;
		Painter<MrdTemplate, MrdDto> sign = null;
		Painter<MrdTemplate, MrdDto> page = null;
		LogoType logoType;

		if (isLabsRequest(request.sample())) {
			logoType = LogoType.DEPENDENT;
			sign = new SectionSign<>(81);
			footer = new   com.greencross.lims.report.kokr.SectionFooterLabs<>();
			template = new MrdTemplateKoKr(new MrdResourceKoKr(doc), test, logoType);
			page = new SectionPage<>(547, 81, template.resource().fontDefault());
		} else {
			logoType = LogoType.INDEPENDENT;
			sign = new SectionSign<>(65);
			footer = new   com.greencross.lims.report.kokr.SectionFooterGenome<>();
			template = new MrdTemplateKoKr(new MrdResourceKoKr(doc), test, logoType);
			page = new SectionPage<>(547, 65, template.resource().fontDefault());
		}

		new PageBuilder<>(template, dto).add(new MrdPage(template, footer, sign, page).page()).build().save(baos);
		return baos.toByteArray();
	}
	@Override
	public String buildLongFormText(Request request) {
		if(hasScreen) {
			return screenBuilder.buildLongFormText(request);
		}
		test = info(request.service().id());
		String headerSummary = String.format(SUMMARY_TABLE_FORMAT,
				StringUtils.center("Gene", 8),
				StringUtils.center("MRD 검출 여부", 14),
				StringUtils.center("% of Clonal/Total {:CELL}s", 38),
				StringUtils.center("% of Clonal/Total Nucleated Cells", 38));
		StringBuilder summary = new StringBuilder();
		MrdDto.MrdHistory last = dto.last();
		for(String gene: test.genes()) {
			MrdDto.MrdDtoGeneResult t = last.results().get(gene);
			String line = String.format(SUMMARY_TABLE_FORMAT,
					StringUtils.center(gene, 8),
					StringUtils.center(toString(t.result()), 14),
					StringUtils.center(df.format(t.pctClonalBCells()*100), 38),
					StringUtils.center(df.format(last.pctClonalNucelatedCells(gene)*100), 38));
			summary.append(line).append("\r\n");
		}
		StringBuilder mutationRate = new StringBuilder();
		if(dto.mutationRate()!=null && !dto.mutationRate().isEmpty()) mutationRate.append("Mutation Rate: ").append(fmtPct.format(Double.parseDouble(dto.mutationRate()))).append("%\r\n");
		var histories = Arrays.stream(dto.histories()).sorted(Comparator.comparing(MrdDto.MrdHistory::date)).collect(Collectors.toList());
		StringBuilder result = new StringBuilder();
		for(String gene: test.genes()) {
			MrdDto.MrdDtoGeneResult t = last.results().get(gene);
			result.append("---------------------------------------------------------------------------------------------------------------------------------------------------------\r\n")
					.append(" ").append(gene).append(" MRD History: ");
			String resulText = tmplInterpretation(t.result()).replace("%g", gene);
			result.append(resulText).append("\r\n")
					.append("---------------------------------------------------------------------------------------------------------------------------------------------------------\r\n");
			String headerResult = String.format(RESULT_TABLE_FORMAT,
					StringUtils.center("f/u No", 6),
					StringUtils.center("Date", 13),
					StringUtils.center("Total %g Depth".replace("%g", gene), 20),
					StringUtils.center("Clonal %g Depth".replace("%g", gene), 20),
					StringUtils.center("Clonal Cell Equivalent*", 30),
					StringUtils.center("Clonal/Total %cs**".replace("%c", test.cell()), 30),
					StringUtils.center("Clonal/Total Nucleated Cells***", 30));
			result.append("---------------------------------------------------------------------------------------------------------------------------------------------------------\r\n")
					.append(headerResult).append("\r\n")
					.append("---------------------------------------------------------------------------------------------------------------------------------------------------------\r\n");
			for(int i = 0; i < dto.histories().length; ++i) {
				MrdDto.MrdHistory history = histories.get(dto.histories().length-1-i);
				MrdDto.MrdDtoGeneResult target = history.results().get(gene);
				String line = String.format(RESULT_TABLE_FORMAT,
						StringUtils.center(fu(dto.histories().length-1-i), 4),
						StringUtils.center(history.date().toString(), 13),
						StringUtils.center(nf.format(target.target().readDepth()), 20),
						StringUtils.center(nf.format(target.target().clonalDepth()), 20),
						StringUtils.center(nf.format(target.equivalent()), 30),
						StringUtils.center(nf.format(target.pctClonalBCells()*100), 30),
						StringUtils.center(df.format(history.pctClonalNucelatedCells(gene)*100), 30));
				result.append(line).append("\r\n");
			}
			result.append("---------------------------------------------------------------------------------------------------------------------------------------------------------\r\n")
					.append(String.format(RESULT_INFO_FORMAT,
							StringUtils.center("Total {:CELL} Count*: ", 20),
							StringUtils.center(nf.format(t.bCells()), 10),
							StringUtils.center("Total Nucleated cell Count***: ", 30),
							StringUtils.center(nf.format(dto.last().nucleatedCells()), 10)))
					.append("\r\n")
					.append("---------------------------------------------------------------------------------------------------------------------------------------------------------\r\n");
			String cmt1 = "* 검체당 " + test.cell() + " 100개의 DNA (LymphoQuant Internal Control, LQIC)를 혼합하여 측정된 근사치입니다.";
			String cmt2 = "** 전체 " + test.cell() + " 중 클론성 세포가 차지하는 비율을 환산한 근사치입니다.";
			String cmt3 = "*** 전체 유핵 세포 수는 Input DNA로부터 도출된 값입니다. (Estimated DNA mass per 1 cell = 6.5pg)";
			result.append(cmt1).append("\r\n")
					.append(cmt2).append("\r\n")
					.append(cmt3).append("\r\n\r\n");
			MrdDto.MrdDtoGeneResult target = last.results().get(gene);
			if(target.interpretation()!=null && !target.interpretation().isEmpty()) {
				result.append("----------------------------------------------------------------------------------------------------\r\n")
						.append("◇ Interpretation\r\n")
						.append(target.interpretation())
						.append("\r\n\r\n");
			}
		}
		StringBuilder qc = new StringBuilder();
		for(String gene: test.genes()) {
			MrdDto.MrdDtoGeneResult t = last.results().get(gene);
			String readDepth = " Total " + gene + " Read Depth(X): ";
			String equivalent = " Estimated " + gene + " Total " + test.cell() + " Count(cell equivalents)**: ";
			qc.append(readDepth).append(nf.format(t.target().readDepth())).append("\r\n")
					.append(equivalent).append(nf.format(t.bCells())).append("\r\n");
		}
		String[][] labelNamePairs = HasSign.getSanitizedContributorsLabelsAndNames(template, test);
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
				.replace("{:INPUT-DNA}", nf.format(dto.last().inputDna()))
				.replace("{:TOTAL-NC}", nf.format(dto.last().nucleatedCells()))
				.replace("{:QC-GENES}", qc.toString())
				.replace("{:CELL}", test.cell())
				.replace("{:INSPECTOR_LABEL}", labelNamePairs[0][0])
				.replace("{:INSPECTOR}", labelNamePairs[0][1])
				.replace("{:REPORTER_AND_REVIEWER_LABEL}", labelNamePairs[1][0])
				.replace("{:REPORTER}", labelNamePairs[1][1])
				.replace("{:REVIEWER}", labelNamePairs[2][1]);
	}

	@Override
	public String buildShortFormText(Request request) {
		test = info(request.service().id());
		if(hasScreen) {
			return screenBuilder.buildShortFormText(request);
		} else return buildLongFormText(request);
	}
	private TestInfo info(String service) {
		return Arrays.stream(TestInfo.TESTS).filter(m->m.code().equalsIgnoreCase(service)).findFirst().get();
	}
	private static final Pattern BIRTHDAT_PATTERN = Pattern.compile("^\\d{6}$");
	private static final Pattern BIRTHDAT_PATTERN2 = Pattern.compile("^(\\d{6})(-)*(\\d)$");
	private MrdDto dto(TestInfo test, Request request, Mrd interpretation) {
		Objects.requireNonNull(request.sample(), "Sample cannot be null");
		Objects.requireNonNull(request.sample().patient(), "Patient cannot be null");

		var dto = new MrdDto();
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
		   .mutationRate(interpretation.mutationRate())
		   .cancerType(interpretation.cancerType())
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
		MrdDto.MrdHistory[] histories = Arrays.stream(interpretation.histories()).map(this::map).toArray(MrdDto.MrdHistory[]::new);
		MrdDto.MrdHistory[] histories2 = new MrdDto.MrdHistory[histories.length];
		for(int i = 0; i < histories.length; ++i) {
			MrdDto.MrdHistory h = histories[histories.length-1-i];
			if(i == 0) h.no("Initial");
			else if(i == 1) h.no("1st f/u");
			else if(i == 2) h.no("2nd f/u");
			else if(i == 3) h.no("3rd f/u");
			else h.no(i + "th f/u");
			histories2[i] = h;
		}
		return dto.histories(histories2);
	}
	private MrdDto.MrdHistory map(Mrd.MrdHistory h) {
		DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		Map<String, MrdDto.MrdDtoGeneResult> results = new HashMap<>();
		for(Mrd.MrdGeneResult r: h.results()) results.put(r.gene(), map(r));
		return new MrdDto.MrdHistory().date(LocalDate.parse(h.date(), DTF))
									  .inputDna(h.inputDna())
									  .results(results);
	}
	private MrdDto.MrdDtoGeneResult map(Mrd.MrdGeneResult r) {
		MrdDto.Result result = MrdDto.Result.NOT_DETECTED;
		if(r.result() == Mrd.Result.DETECTED) result = MrdDto.Result.DETECTED;
		else if(r.result() == Mrd.Result.NA) result = MrdDto.Result.NA;
		else if(r.result() == Mrd.Result.CUSTOM) result = MrdDto.Result.CUSTOM;
		return new MrdDto.MrdDtoGeneResult().gene(r.gene())
											.target(map(r.target()))
											.lqic(map(r.lqic()))
											.result(result)
											.interpretation(r.interpretation());
	}
	private MrdDto.MrdDtoCloneResult map(Mrd.MrdCloneResult r) {
		return new MrdDto.MrdDtoCloneResult().readDepth(r.readDepth()!=null?r.readDepth():0)
											 .clonalDepth(r.clonalDepth()!=null?r.clonalDepth():0);
	}
	private static final String SUMMARY_TABLE_FORMAT = "%1$-8s%2$-14s%3$-38s%4$-38s";
	private static final String RESULT_TABLE_FORMAT = "%1$-6s%2$-13s%3$-20s%4$-20s%5$-30s%6$-30s%7$-30s";
	private static final String RESULT_INFO_FORMAT = "%1$-20s%2$-10s     %3$-30s%4$-10s";
	protected final NumberFormat nf = NumberFormat.getInstance();
	protected final DecimalFormat df = new DecimalFormat("#.###");
	private static final String TEXT_TEMPLATE = "◆ {:TEST-NAME}\r\n" +
			"\r\n" +
			"◇ Cancer Type: {:CANCER-TYPE}\r\n" +
			"\r\n" +
			"◇ 결과 요약\r\n" +
			"---------------------------------------------------------------------------------------------------------------------------------------------------------\r\n" +
			"{:HEADER-SUMMARY}\r\n" +
			"---------------------------------------------------------------------------------------------------------------------------------------------------------\r\n" +
			"{:SUMMARY-GENES}" +
			"---------------------------------------------------------------------------------------------------------------------------------------------------------\r\n" +
			"{:MUTATION-RATE}\r\n" +
			"◇ 상세 결과\r\n" +
			"{:RESULT-GENES}\r\n" +
			"\r\n" +
			"◇ 검사 정보\r\n" +
			" 1. 검사 방법\r\n" +
			" Target enrichment method: {:METHOD}\r\n" +
			" Bioinformatic pipeline: {:PIPELINE}\r\n" +
			" Tested Panel: {:PANEL}\r\n" +
			" Massively Parallel Sequencing: {:SEQUENCING}\r\n" +
			" Reference Genome: {:REFERENCE}\r\n" +
			"\r\n" +
			" 2. QC 정보 \r\n" +
			" Input DNA(ng): {:INPUT-DNA}\r\n" +
			" Estimated Total Nucleated Cells (cell equivalents)*: {:TOTAL-NC}\r\n" +
			"{:QC-GENES}" +
			" * Estimated DNA mass per 1 cell = 6.5pg\r\n" +
			" ** 검체당 {:CELL} 100개의 DNA(LymphoQuant Internal Control, LQIC)를 혼합하여 측정된 근사치입니다.\r\n" +
			"\r\n" +
			" 3. 검사의 한계 \r\n" +
			" ● 검체 내 전체 read depth가 190,000 이상인 경우 클론성 염기서열은 10⁻⁴ 의 분석 민감도를 가지며, read depth가 190,000 이상이 아닌 경우에도 95% 신뢰구간 내에 발견된 클론성 염기서열은 보고될 수 있습니다.\r\n" +
			" ● PCR 증폭은 {:CELL}의 DNA양에 영향을 받으며 primer결합 부위 변이가 있는 경우 위음성을 보일 가능성이 있습니다.\r\n" +
			" ● 본 검사 결과는 임상, 조직학 및 면역 표현형 데이터의 맥락에서 해석되어야 합니다.\r\n" +
			" ● 본 검사는 clonal cell populations 의 100 %를 식별하지 않습니다.\r\n" +
			"\r\n" +
			"{:INSPECTOR_LABEL} {:INSPECTOR} / {:REPORTER_AND_REVIEWER_LABEL} {:REPORTER}, {:REVIEWER}";
	private static String fu(int i) {
		if(i == 0) return "Initial";
		else if(i == 1) return "1st f/u";
		else if(i == 2) return "2nd f/u";
		else if(i == 3) return "3rd f/u";
		return i + "th f/u";
	}
	private String tmplInterpretation(MrdDto.Result result) {
		return switch (result) {
			case DETECTED       -> tmplInterpretationPositive;
			case NOT_DETECTED   -> tmplInterpretationNegative;
			case NA             -> tmplInterpretationNA;
			case CUSTOM         -> "-";
		};
	}
	private final String tmplInterpretationPositive		= "%g 클론 추적 관찰 결과 미세잔존질환이 확인되었습니다.";
	private final String tmplInterpretationNegative		= "%g 클론 추적 관찰 결과 미세잔존질환이 확인되지 않았습니다.";
	private final String tmplInterpretationNA		    = "See Interpretation.";
	private String toString(MrdDto.Result result) {
		if(result == null) return "-";
		return switch (result) {
			case DETECTED       -> "Detected";
			case NOT_DETECTED   -> "Not Detected";
			case NA             -> "N/A";
			default             -> "-";
		};
	}
}
