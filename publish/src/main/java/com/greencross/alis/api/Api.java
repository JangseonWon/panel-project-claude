package com.greencross.alis.api;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Base64;
import java.util.Locale;
import java.util.Map;

@RequiredArgsConstructor
@ToString
@Configuration
@Service("ALIS-API")
public class Api {
	private final Logger Log = LoggerFactory.getLogger(getClass());
	private static final String API_SET_UPLOAD_LAB_REGFILE_TEMPLATE = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
			"<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">" +
			"<soap12:Header>" +
			"<AuthenticationHeader xmlns=\"http://tempuri.org/\">" +
			"<UserName>{:username}</UserName>" +
			"<Password>{:password}</Password>" +
			"</AuthenticationHeader>" +
			"</soap12:Header>" +
			"<soap12:Body>" +
			"<SetUploadLabRegFile xmlns=\"http://tempuri.org/\">" +
			"<req>" +
			"<LabRegDate>{:date}</LabRegDate>" +
			"<LabRegNo>{:reqno}</LabRegNo>" +
			"<ReportCode>{:code}</ReportCode>" +
			"<TestCode>{:code}</TestCode>" +
			"<FileDisplayName>{:file-name}</FileDisplayName>" +
			"<FileExt>{:file-ext}</FileExt>" +
			"<FileSize>{:file-size}</FileSize>" +
			"<FileKind>{:file-type}</FileKind>" +
			"<FileBuffer>{:file-data}</FileBuffer>" +
			"<FileCreateTime>{:file-date}</FileCreateTime>" +
			"<FileDescription>{:file-desc}</FileDescription>" +
			"<TextReport><![CDATA[{:text-report}]]></TextReport>" +
			"<MemberID>{:member-id}</MemberID>" +
			"</req>" +
			"</SetUploadLabRegFile>" +
			"</soap12:Body>" +
			"</soap12:Envelope>";
	private static final String API_SET_LAB_REG_URL = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
			"<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">\n" +
			"  <soap12:Header>\n" +
			"<AuthenticationHeader xmlns=\"http://tempuri.org/\">" +
			"<UserName>{:username}</UserName>" +
			"<Password>{:password}</Password>" +
			"</AuthenticationHeader>" +
			"  </soap12:Header>\n" +
			"  <soap12:Body>\n" +
			"    <SetLabRegUrl xmlns=\"http://tempuri.org/\">\n" +
			"      <req>\n" +
			"<LabRegDate>{:date}</LabRegDate>" +
			"<LabRegNo>{:reqno}</LabRegNo>" +
			"<ReportCode>{:code}</ReportCode>" +
			"<TestCode>{:code}</TestCode>" +
			"<UrlDisplayName>{:file-name}</UrlDisplayName>" +
			"        <UrlPath><![CDATA[{:url}]]></UrlPath>\n" +
			"        <LimsReqNo>{:reqno2}</LimsReqNo>\n" +
			"        <LimsItemCd>{:code2}</LimsItemCd>\n" +
			"        <LimsSeqno>{:seq}</LimsSeqno>\n" +
			"        <LimsGubun>{:ext}</LimsGubun>\n" +
			"        <LimsFilename>{:file-name}</LimsFilename>\n" +
			"        <LimsFilepath><![CDATA[{:url}]]></LimsFilepath>\n" +
			"        <LimsEmpno>{:member-id}</LimsEmpno>\n" +
			"      </req>\n" +
			"      <errorMsg>string</errorMsg>\n" +
			"    </SetLabRegUrl>\n" +
			"  </soap12:Body>\n" +
			"</soap12:Envelope>";
	private static final String API_UPDATE_LAB_WORK_LIST = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
			"<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">\n" +
			"  <soap12:Header>\n" +
			"    <AuthenticationHeader xmlns=\"http://tempuri.org/\">\n" +
			"      <UserName>{:username}</UserName>\n" +
			"      <Password>{:password}</Password>\n" +
			"    </AuthenticationHeader>\n" +
			"  </soap12:Header>\n" +
			"  <soap12:Body>\n" +
			"    <UpdateLabWorkList xmlns=\"http://tempuri.org/\">\n" +
			"      <labRegDate>{:date}</labRegDate>\n" +
			"      <labRegNo>{:reqno}</labRegNo>\n" +
			"      <errorMsg></errorMsg>\n" +
			"    </UpdateLabWorkList>\n" +
			"  </soap12:Body>\n" +
			"</soap12:Envelope>";
	private static final String API_SET_REPORT_PUBLISH_CANCEL = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
			"<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">" +
			"<soap12:Header>" +
			"<AuthenticationHeader xmlns=\"http://tempuri.org/\">" +
			"<UserName>{:username}</UserName>" +
			"<Password>{:password}</Password>" +
			"</AuthenticationHeader>" +
			"</soap12:Header>" +
			"<soap12:Body>" +
			"<ReportPublishCancel xmlns=\"http://tempuri.org/\">" +
			"<labRegDate>{:date}</labRegDate>" +
			"<labRegNo>{:reqno}</labRegNo>" +
			"<reportCode>{:code}</reportCode>" +
			"<errorMsg></errorMsg>" +
			"</ReportPublishCancel>" +
			"</soap12:Body>" +
			"</soap12:Envelope>";
	private static final String API_GET_GENE_DATA_TEMPLATES = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
			"<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">\n" +
			"  <soap12:Header>\n" +
			"<AuthenticationHeader xmlns=\"http://tempuri.org/\">" +
			"<UserName>{:username}</UserName>" +
			"<Password>{:password}</Password>" +
			"</AuthenticationHeader>" +
			"  </soap12:Header>\n" +
			"  <soap12:Body>\n" +
			"    <GetListGeneDataTemplate xmlns=\"http://tempuri.org/\">\n" +
			"      <errorMsg></errorMsg>\n" +
			"    </GetListGeneDataTemplate>\n" +
			"  </soap12:Body>\n" +
			"</soap12:Envelope>";
	private static final String API_GET_LIST_GENE_DATA_RESULT = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
			"<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">\n" +
			"  <soap12:Header>\n" +
			"<AuthenticationHeader xmlns=\"http://tempuri.org/\">" +
			"<UserName>{:username}</UserName>" +
			"<Password>{:password}</Password>" +
			"</AuthenticationHeader>" +
			"  </soap12:Header>\n" +
			"  <soap12:Body>\n" +
			"    <GetListGeneDataResult xmlns=\"http://tempuri.org/\">\n" +
			"      <regDate>{:date}</regDate>\n" +
			"      <regNo>{:reqno}</regNo>\n" +
			"      <testCode>{:code}</testCode>\n" +
			"      <errorMsg></errorMsg>\n" +
			"    </GetListGeneDataResult>\n" +
			"  </soap12:Body>\n" +
			"</soap12:Envelope>";
	private static final String API_DELETE_GENE_DATA_RESULT = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
			"<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">\n" +
			"  <soap12:Header>\n" +
			"<AuthenticationHeader xmlns=\"http://tempuri.org/\">" +
			"<UserName>{:username}</UserName>" +
			"<Password>{:password}</Password>" +
			"</AuthenticationHeader>" +
			"  </soap12:Header>\n" +
			"  <soap12:Body>\n" +
			"    <DeleteGeneDataResult xmlns=\"http://tempuri.org/\">\n" +
			"      <LabRegDate>{:date}</LabRegDate>\n" +
			"      <LabRegNo>{:reqno}</LabRegNo>\n" +
			"      <OrderCode>{:code}</OrderCode>\n" +
			"      <GeneDataRowSeq>{:row}</GeneDataRowSeq>\n" +
			"      <errorMsg></errorMsg>\n" +
			"    </DeleteGeneDataResult>\n" +
			"  </soap12:Body>\n" +
			"</soap12:Envelope>";
	private static final String API_SAVE_GENE_DATA_RESULT = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
			"<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">\n" +
			"  <soap12:Header>\n" +
			"<AuthenticationHeader xmlns=\"http://tempuri.org/\">" +
			"<UserName>{:username}</UserName>" +
			"<Password>{:password}</Password>" +
			"</AuthenticationHeader>" +
			"  </soap12:Header>\n" +
			"  <soap12:Body>\n" +
			"    <SaveGeneDataResult xmlns=\"http://tempuri.org/\">\n" +
			"      <LabRegDate>{:date}</LabRegDate>\n" +
			"      <LabRegNo>{:reqno}</LabRegNo>\n" +
			"      <OrderCode>{:code}</OrderCode>\n" +
			"      <GeneDataRowSeq>{:row}</GeneDataRowSeq>\n" +
			"      <GeneDataID><![CDATA[{:key}]]></GeneDataID>\n" +
			"      <GeneDataResult01><![CDATA[{:value}]]></GeneDataResult01>\n" +
			"      <errorMsg></errorMsg>\n" +
			"    </SaveGeneDataResult>\n" +
			"  </soap12:Body>\n" +
			"</soap12:Envelope>";

	private static final DateTimeFormatter DTF = new DateTimeFormatterBuilder().parseCaseInsensitive().appendPattern("yyyy-MM-dd").toFormatter();
	@Value("${alis.api.url}")
	private String url;
	@Value("${alis.api.content-type}")
	private String contentType;
	@Value("${alis.api.username}")
	private String username;
	@Value("${alis.api.password}")
	private String password;
	@Value("${alis.api.timeout-millis:300000}")
	private int timeoutMillis;
	private static final ObjectMapper OM = new ObjectMapper().configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
			.setLocale(Locale.KOREA)
			.registerModule(new JavaTimeModule());
	@PersistenceContext(unitName = "ALIS")
	private EntityManager em;

	public enum FileType {
		GENERAL, PDF, JPG, JPG_PER_PAGE, ETC, TEXT_SHORTER, TEXT, JSON
	}

	/*report 파일 전송*/
	public boolean fileUpload(String user, Request request, byte[] file, String fileName, FileType type, LocalDate create, String desc) throws IOException {
		// String fileName = file.getName();
		//String ext = fileName.contains(".")?fileName.substring(fileName.lastIndexOf(".")+1):"";
		String ext = type.ordinal() == 1 ? "pdf" : "jpg";
		long size = /*file.length();*/file.length;
		String data = Base64.getEncoder().encodeToString(/*Files.toByteArray(file)*/file);
		String xml = API_SET_UPLOAD_LAB_REGFILE_TEMPLATE.replace("{:username}", username)
				.replace("{:password}", password)
				.replace("{:date}", DTF.format(request.requestDate()))
				.replace("{:reqno}", String.valueOf(request.requestNo()))
				.replace("{:code}", request.itemCode())
				.replace("{:code}", request.itemCode())
				.replace("{:file-name}", fileName)
				.replace("{:file-ext}", ext)
				.replace("{:file-size}", String.valueOf(size))
				.replace("{:file-type}", String.valueOf(type.ordinal()))
				.replace("{:file-date}", DTF.format(create))
				.replace("{:file-desc}", desc)
				.replace("{:file-data}", data)
				.replace("{:member-id}", user);
		Document response = Jsoup.connect(url).maxBodySize(0)
				.method(Connection.Method.POST)
				.header("content-type", contentType)
				.requestBody(xml)
				.parser(Parser.xmlParser())
				.timeout(timeoutMillis)
				.execute().parse();
		String values = response.select("SetUploadLabRegFileResult").text().replace("\"\"", "null");
		boolean result = OM.readValue(values, Boolean.class);
		Log.info(request.requestDate().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + request.requestNo() + ", " + request.itemCode() + ": FileUpload(" + type + ")=>" + result);
		return result;
	}

	public boolean textUpload(String user, Request request, String text, FileType type, LocalDate create, String desc) throws IOException {
		String xml = API_SET_UPLOAD_LAB_REGFILE_TEMPLATE.replace("{:username}", username)
				.replace("{:password}", password)
				.replace("{:date}", DTF.format(request.requestDate()))
				.replace("{:reqno}", String.valueOf(request.requestNo()))
				.replace("{:code}", request.itemCode())
				.replace("{:code}", request.itemCode())
				.replace("{:file-name}", "")
				.replace("{:file-ext}", "")
				.replace("{:file-size}", "50")
				.replace("{:file-type}", String.valueOf(type.ordinal()))
				.replace("{:file-date}", DTF.format(create))
				.replace("{:file-desc}", desc)
				.replace("{:file-data}", "")
				.replace("{:text-report}", text)
				.replace("{:member-id}", user);
		Document response = Jsoup.connect(url).maxBodySize(0)
				.method(Connection.Method.POST)
				.header("content-type", contentType)
				.requestBody(xml)
				.parser(Parser.xmlParser())
				.timeout(timeoutMillis)
				.execute().parse();
		String values = response.select("SetUploadLabRegFileResult").text().replace("\"\"", "null");
		boolean result = OM.readValue(values, Boolean.class);
		Log.info(request.requestDate().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + request.requestNo() + ", " + request.itemCode() + ": TextUpload =>" + result);
		return result;
	}

	/*report 파일 전송*/
	public boolean urlUpload(String user, Request request, Request request2, String url0, String seq, String ext, String fileName) throws IOException {
		String xml = API_SET_LAB_REG_URL.replace("{:username}", username)
				.replace("{:password}", password)
				.replace("{:date}", DTF.format(request.requestDate()))
				.replace("{:reqno}", String.valueOf(request.requestNo()))
				.replace("{:code}", request.itemCode())
				.replace("{:code}", request.itemCode())
				.replace("{:file-name}", fileName)
				.replace("{:file-name}", fileName)
				.replace("{:url}", url0)
				.replace("{:url}", url0)
				.replace("{:seq}", seq)
				.replace("{:ext}", ext)
				.replace("{:member-id}", user);
		if (request2 != null) xml = xml.replace("{:reqno2}", String.valueOf(request2.requestNo2()))
				.replace("{:code2}", request2.itemCode());
		else xml = xml.replace("{:reqno2}", "").replace("{:code2}", "");
		Document response = Jsoup.connect(url).maxBodySize(0)
				.method(Connection.Method.POST)
				.header("content-type", contentType)
				.requestBody(xml)
				.parser(Parser.xmlParser())
				.timeout(timeoutMillis)
				.execute().parse();
		String values = response.select("SetLabRegUrlResult").text().replace("\"\"", "null");
		boolean result = OM.readValue(values, Boolean.class);
		Log.info(request.requestDate().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + request.requestNo() + ", " + request.itemCode() + ": UrlUpload=>" + result);
		System.out.println(xml);
		if (!result) try {
			String msg = response.select("errorMsg").text().replace("\"\"", "null");
			throw new RuntimeException(msg);
		} catch (Exception e) {
			throw new RuntimeException(response.data());
		}
		return true;
	}

	/*체크리스트에서 워크리스트로 전송(워크리스트 전송 누락건 처리. 에러를 리턴하는 것이 정상이므로 결과를 반전하여 리턴함)*/
	public boolean chkWorklist(Request request) throws IOException {
		String xml = API_UPDATE_LAB_WORK_LIST.replace("{:username}", username)
				.replace("{:password}", password)
				.replace("{:date}", DTF.format(request.requestDate()))
				.replace("{:reqno}", String.valueOf(request.requestNo()));
		Document response = Jsoup.connect(url).maxBodySize(0)
				.method(Connection.Method.POST)
				.header("content-type", contentType)
				.requestBody(xml)
				.parser(Parser.xmlParser())
				.timeout(timeoutMillis)
				.execute().parse();
		String values = response.select("UpdateLabWorkListResult").text().replace("\"\"", "null");
		return OM.readValue(values, Boolean.class);
	}

	/*report 배포 취소(파일 삭제)*/
	public boolean cancelPublish(Request request) throws IOException {
		String xml = API_SET_REPORT_PUBLISH_CANCEL.replace("{:username}", username)
				.replace("{:password}", password)
				.replace("{:date}", DTF.format(request.requestDate()))
				.replace("{:reqno}", String.valueOf(request.requestNo()))
				.replace("{:code}", request.itemCode());
		Document response = Jsoup.connect(url).maxBodySize(0)
				.method(Connection.Method.POST)
				.header("content-type", contentType)
				.requestBody(xml)
				.parser(Parser.xmlParser())
				.timeout(timeoutMillis)
				.execute().parse();
		String values = response.select("ReportPublishCancelResult").text().replace("\"\"", "null");
		boolean result = !OM.readValue(values, Boolean.class);
		if (result) try {
			String msg = response.select("errorMsg").text().replace("\"\"", "null");
			throw new RuntimeException(msg);
		} catch (Exception e) {
			throw new RuntimeException(response.data());
		}
		return true;
	}

	/*저장된 변이 정보 출력*/
	public Map<String, String>[] getVariant(Request request) throws IOException {
		String xml = API_GET_LIST_GENE_DATA_RESULT.replace("{:username}", username)
				.replace("{:password}", password)
				.replace("{:date}", DTF.format(request.requestDate()))
				.replace("{:reqno}", String.valueOf(request.requestNo()))
				.replace("{:code}", request.itemCode());
		Document response = Jsoup.connect(url).maxBodySize(0)
				.method(Connection.Method.POST)
				.header("content-type", contentType)
				.requestBody(xml)
				.parser(Parser.xmlParser())
				.timeout(timeoutMillis)
				.execute().parse();
		String values = response.select("GetListGeneDataResultResult").text().replace("\"\"", "null");
		if ("".equals(values.trim())) return new Map[0];
		else return OM.readValue(values, Map[].class);
	}

	public int deleteVariant(Request request, int row) throws IOException {
		String xml = API_DELETE_GENE_DATA_RESULT.replace("{:username}", username)
				.replace("{:password}", password)
				.replace("{:date}", DTF.format(request.requestDate()))
				.replace("{:reqno}", String.valueOf(request.requestNo()))
				.replace("{:code}", request.itemCode())
				.replace("{:row}", String.format("%d", row));
		Document response = Jsoup.connect(url).maxBodySize(0)
				.method(Connection.Method.POST)
				.header("content-type", contentType)
				.requestBody(xml)
				.parser(Parser.xmlParser())
				.timeout(timeoutMillis)
				.execute().parse();
		String values = response.select("DeleteGeneDataResultResult").text().replace("\"\"", "null");
		return OM.readValue(values, Integer.class);
	}

	/*변이 정보 입력*/
	public int saveVariantValue(Request request, int row, String key, String value) throws IOException {
		String xml = API_SAVE_GENE_DATA_RESULT.replace("{:username}", username)
				.replace("{:password}", password)
				.replace("{:date}", DTF.format(request.requestDate()))
				.replace("{:reqno}", String.valueOf(request.requestNo()))
				.replace("{:code}", request.itemCode())
				.replace("{:row}", String.format("%d", row))
				.replace("{:key}", key)
				.replace("{:value}", value);
		Document response = Jsoup.connect(url).maxBodySize(0)
				.method(Connection.Method.POST)
				.header("content-type", contentType)
				.requestBody(xml)
				.parser(Parser.xmlParser())
				.timeout(timeoutMillis)
				.execute().parse();
		String values = response.select("SaveGeneDataResultResult").text().replace("\"\"", "null");
		return OM.readValue(values, Integer.class);
	}

	@Transactional("transactionManagerAlis")
	public boolean result(Request request, AlisResult data, String member, String machine) {
		StoredProcedureQuery query = em.createStoredProcedureQuery("Interface_SetPatientResult");
		query.registerStoredProcedureParameter(1, String.class, ParameterMode.IN)        // LabRegDate
				.registerStoredProcedureParameter(2, Integer.class, ParameterMode.IN)       // LabRegNo
				.registerStoredProcedureParameter(3, String.class, ParameterMode.IN)        // OrderCode
				.registerStoredProcedureParameter(4, String.class, ParameterMode.IN)        // TestCode
				.registerStoredProcedureParameter(5, String.class, ParameterMode.IN)        // TestSubCode
				.registerStoredProcedureParameter(6, String.class, ParameterMode.IN)        // Result01
				.registerStoredProcedureParameter(7, String.class, ParameterMode.IN)        // Result02
				.registerStoredProcedureParameter(8, String.class, ParameterMode.IN)        // TestResultText
				.registerStoredProcedureParameter(9, String.class, ParameterMode.IN)        // EditorMemberId
				.registerStoredProcedureParameter(10, String.class, ParameterMode.IN)       // MachineCode
				.setParameter(1, request.requestDate().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
				.setParameter(2, request.requestNo())
				.setParameter(3, request.itemCode())
				.setParameter(4, request.itemCode())
				.setParameter(5, data.subCode())
				.setParameter(6, data.result1())
				.setParameter(7, data.result2())
				.setParameter(8, data.text())
				.setParameter(9, member)
				.setParameter(10, machine);
		int result = query.executeUpdate();
		Log.info(request.requestDate().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + request.requestNo() + ", " + request.itemCode() + ": " + data + "=>" + result);
		return result == 1;
	}

	@Transactional("transactionManagerAlis")
	public boolean state(Request request, String state, String member, String machine) {
		;
		StoredProcedureQuery query = em.createStoredProcedureQuery("Interface_SetPatientTestState");
		query.registerStoredProcedureParameter(1, String.class, ParameterMode.IN)        // LabRegDate
				.registerStoredProcedureParameter(2, Integer.class, ParameterMode.IN)       // LabRegNo
				.registerStoredProcedureParameter(3, String.class, ParameterMode.IN)        // OrderCode
				.registerStoredProcedureParameter(4, String.class, ParameterMode.IN)        // TestCode
				.registerStoredProcedureParameter(5, String.class, ParameterMode.IN)        // TestStateCode
				.registerStoredProcedureParameter(6, String.class, ParameterMode.IN)        // EditorMemberId
				.registerStoredProcedureParameter(7, String.class, ParameterMode.IN)       // MachineCode
				.setParameter(1, request.requestDate().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
				.setParameter(2, request.requestNo())
				.setParameter(3, request.itemCode())
				.setParameter(4, request.itemCode())
				.setParameter(5, state)
				.setParameter(6, member)
				.setParameter(7, machine);
		int result = query.executeUpdate();
		Log.info(request.requestDate().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + request.requestNo() + ", " + request.itemCode() + ": State(" + state + ") =>" + result);
		return result == 1;
	}

	@Transactional(value = "transactionManagerAlis", readOnly = true)
	public String state(Request request) {
		StoredProcedureQuery query = em.createStoredProcedureQuery("Interface_GetPatientTest");
		query.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN)            // StatusIndex
				.registerStoredProcedureParameter(2, String.class, ParameterMode.IN)        // LabRegDate
				.registerStoredProcedureParameter(3, Integer.class, ParameterMode.IN)       // LabRegNo
				.registerStoredProcedureParameter(4, String.class, ParameterMode.IN)        // OrderCode
				.registerStoredProcedureParameter(5, String.class, ParameterMode.IN)        // TestDate
				.setParameter(1, 0)
				.setParameter(2, request.requestDate().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
				.setParameter(3, request.requestNo())
				.setParameter(4, request.itemCode())
				.setParameter(5, request.itemCode());
		Object[] row = (Object[]) query.getResultList().stream().findFirst().orElse(new Object[10]);
		return row[8].toString();
	}
}