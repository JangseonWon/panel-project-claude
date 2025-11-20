package com.greencross.lims.report.builder;

import com.gcgenome.lims.report.Dto;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@Accessors(fluent = true)
public abstract class AbstractReportDto implements Dto {
	private String medicalInstitution;		// 의뢰기관
	private String medicalRecordNumber;		// 등록번호
	private String requestNumber;       	// 접수번호
	private String patientName;				// 성명
	private String patientCode;				// 주민번호
	private Integer age;
	private LocalDate birthDate;			// 생년월일->나이
	private Sex sex;						// 성별
	private String specimenType;			// 검체종류
	private String ward;					// 병동
	private String department;				// 진료과
	private LocalDate collectionDate;		// 검체채취일
	private String patientInfo;				// 임상정보/기타
	private String physician;				// 주치의
	private LocalDate receiptDate;			// 접수일
	private LocalDate reportDate;			// 보고일
	private String barcode;					// 의뢰기관 바코드(Optional)
	private Boolean revision;				// 개정여부
}
