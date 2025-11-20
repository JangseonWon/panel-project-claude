# LIMS Panel 검사 서비스 프로젝트 구조 및 유지보수 가이드

## 📋 목차
1. [프로젝트 개요](#프로젝트-개요)
2. [기술 스택](#기술-스택)
3. [아키텍처 개요](#아키텍처-개요)
4. [폴더 구조 및 모듈 설명](#폴더-구조-및-모듈-설명)
5. [워크플로우 및 데이터 흐름](#워크플로우-및-데이터-흐름)
6. [빌드 및 배포](#빌드-및-배포)
7. [유지보수 가이드](#유지보수-가이드)
8. [트러블슈팅](#트러블슈팅)

---

## 프로젝트 개요

**LIMS Panel 검사 서비스**는 유전체 검사 관리를 위한 Laboratory Information Management System입니다.

### 핵심 특징
- **아키텍처**: 마이크로서비스 아키텍처 (MSA)
- **모노레포**: 35개의 Gradle 서브모듈로 구성
- **프론트엔드**: GWT (Google Web Toolkit) 기반 SPA
- **백엔드**: Spring Boot + Spring Cloud
- **빌드 시스템**: Gradle (Kotlin DSL)
- **배포**: Jenkins CI/CD + Docker + AWS ECR

### 비즈니스 도메인
유전체 검사 프로세스의 전 과정을 관리:
- 샘플 접수 → DNA 추출 → 라이브러리 제작 → 시퀀싱 → 데이터 분석 → 결과 해석 → 보고서 발행

---

## 기술 스택

### 백엔드 기술

#### 언어 & 프레임워크
| 기술 | 버전 | 용도 |
|------|------|------|
| Kotlin | 2.0.0 | 주 개발 언어 |
| Java | 17, 21 | 레거시 및 GWT 호환 |
| Spring Boot | 2.6.1 | 애플리케이션 프레임워크 |
| Spring Cloud | 2021.0.0 | MSA 인프라 |

#### 인프라 & 미들웨어
| 기술 | 용도 |
|------|------|
| **Spring Cloud Gateway** | API 게이트웨이, 라우팅, 로드밸런싱 |
| **Apache Zookeeper** | 서비스 디스커버리 |
| **Apache Kafka** | 메시징, 이벤트 스트리밍 |
| **PostgreSQL** | 관계형 데이터베이스 (JPA 사용) |
| **Apache Cassandra** | NoSQL (문서 저장, 3중화) |
| **R2DBC** | 리액티브 데이터베이스 드라이버 |

#### 주요 라이브러리
```kotlin
// 리액티브 프로그래밍
implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

// JSON 처리
implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

// 로깅
implementation("org.apache.logging.log4j:log4j-core:2.17.0")

// 코드 간소화
implementation("org.projectlombok:lombok:1.18.32")
```

### 프론트엔드 기술

#### GWT 스택
| 기술 | 버전 | 용도 |
|------|------|------|
| **GWT** | 2.12.1 | Java → JavaScript 트랜스파일러 |
| **Elemento** | 1.4.2 | HTML 템플릿 엔진 |
| **Elemental2** | 1.2.0 | DOM API 바인딩 |

#### UI 라이브러리
```kotlin
implementation("net.sayaya:ui:4.1")           // 커스텀 UI 컴포넌트
implementation("net.sayaya:chart:2.1")        // 차트 라이브러리
implementation("com.gcgenome:lims-icon:2.1")  // 아이콘 라이브러리
```

### 빌드 & 배포

| 도구 | 버전/설정 | 용도 |
|------|-----------|------|
| **Gradle** | 8.2, 8.9 | 빌드 자동화 |
| **GWT Plugin** | 1.1.19 | GWT 컴파일 |
| **Jenkins** | - | CI/CD 파이프라인 |
| **Docker** | - | 컨테이너화 |
| **AWS ECR** | - | Docker 이미지 저장소 |
| **GitHub Actions** | - | 코드 리뷰 자동화 (Gemini AI) |

---

## 아키텍처 개요

### 마이크로서비스 구조

```
┌─────────────────────────────────────────────────────────────┐
│                         클라이언트                           │
│              (브라우저에서 GWT 컴파일된 JS 실행)              │
└────────────────────────┬────────────────────────────────────┘
                         │ HTTP/REST
                         ▼
┌─────────────────────────────────────────────────────────────┐
│                    Spring Cloud Gateway                      │
│                  (라우팅 + 로드밸런싱)                        │
└────────────┬────────────────────────────────────────────────┘
             │
    ┌────────┴────────┐
    │   Zookeeper     │ (서비스 디스커버리)
    └────────┬────────┘
             │
    ┌────────┴────────────────────────────────────────────────┐
    │                 마이크로서비스들                          │
    ├─────────────┬──────────────┬──────────────┬─────────────┤
    │  worklist   │   sample     │   reading    │   analysis  │
    │  request    │   report     │   publish    │   file      │
    │  interpretation │ preprocessing │ variant-snv │ legacy   │
    └─────────────┴──────────────┴──────────────┴─────────────┘
             │                          │
    ┌────────┴────────┐        ┌────────┴────────┐
    │   PostgreSQL    │        │    Cassandra    │
    │   (JPA/R2DBC)   │        │   (3중화 구성)   │
    └─────────────────┘        └─────────────────┘
             │
    ┌────────┴────────┐
    │      Kafka      │ (메시징)
    └─────────────────┘
```

### 프론트엔드 - 백엔드 통신

```
┌─────────────────┐
│   UI 모듈       │ (GWT 컴파일 → JavaScript)
│  - worklist-ui  │
│  - sample-ui    │
│  - analysis-ui  │
└────────┬────────┘
         │ REST API 호출
         ▼
┌─────────────────┐
│    Gateway      │ (정적 리소스 호스팅 + API 프록시)
│  /static/       │
└────────┬────────┘
         │ 라우팅
         ▼
┌─────────────────┐
│ 백엔드 서비스    │
│ @RestController │
└─────────────────┘
```

---

## 폴더 구조 및 모듈 설명

### 전체 디렉토리 구조

```
panel-project-claude/
├── .git/                          # Git 저장소
├── .github/                       # GitHub 설정
│   └── workflows/
│       └── code-review.yaml       # Gemini AI 코드 리뷰 자동화
├── build.gradle.kts               # 루트 빌드 설정
├── settings.gradle.kts            # 멀티모듈 설정 (35개 모듈)
├── Jenkinsfile                    # 전체 파이프라인
├── Jenkinsfile.sequencing-dna-library-ui  # UI 전용 파이프라인
├── readme.md                      # 프로젝트 기본 문서
├── resources/                     # 공유 리소스 (이미지 등)
│
├── [공통 라이브러리 모듈]
│   ├── data/                      # 공통 DTO/API (모든 모듈이 의존)
│   ├── service/                   # 공통 서비스 유틸리티
│   └── sample-panel/              # 샘플 패널 공통 기능
│
├── [인프라 모듈]
│   └── gateway/                   # API Gateway (진입점)
│
├── [비즈니스 도메인 모듈 - 백엔드]
│   ├── worklist/                  # 작업 목록 관리
│   ├── sample/                    # 샘플 관리
│   ├── request/                   # 검사 요청 관리
│   ├── dna/                       # DNA 추출 (UI만 존재)
│   ├── library/                   # 라이브러리 제작 (UI만 존재)
│   ├── sequencing/                # 시퀀싱 (UI만 존재)
│   ├── reading/                   # 데이터 읽기
│   ├── analysis/                  # 데이터 분석
│   ├── interpretation/            # 결과 해석
│   ├── report/                    # 보고서 생성
│   ├── publish/                   # 결과 발행
│   ├── preprocessing/             # 데이터 전처리
│   ├── file/                      # 파일 관리
│   ├── variant-snv/               # SNV 변이 분석
│   ├── snv/                       # SNV 서비스
│   ├── miscellaneous/             # 기타 기능
│   ├── trello/                    # Trello 연동
│   ├── legacy/                    # 레거시 시스템 연동
│   ├── analysis-crawler/          # 분석 크롤러
│   └── worklist-r2dbc/            # R2DBC 기반 워크리스트
│
└── [비즈니스 도메인 모듈 - 프론트엔드]
    ├── worklist-ui/
    ├── sample-ui/
    ├── request-ui/
    ├── dna-ui/
    ├── library-ui/
    ├── sequencing-ui/
    ├── reading-ui/
    ├── analysis-ui/
    ├── interpretation-ui/
    ├── report-ui/
    ├── preprocessing-ui/
    ├── file-ui/
    ├── variant-snv-ui/
    ├── snv-ui/
    ├── miscellaneous-ui/
    └── trello-ui/
```

### 모듈별 상세 설명

#### 🔧 인프라 모듈

##### `gateway/` - API Gateway
**역할**: 모든 클라이언트 요청의 진입점
**위치**: `/gateway/src/main/java/com/greencross/lims/service/`

**주요 기능**:
- Spring Cloud Gateway를 통한 라우팅
- 로드밸런싱
- 정적 리소스 호스팅 (UI 모듈들의 빌드 결과물)
- Zookeeper 서비스 디스커버리 클라이언트

**주요 파일**:
- `Application.java` - 메인 진입점
- `Router.java` - 라우팅 규칙 정의
- `WebConfig.java` - CORS, 정적 리소스 설정
- `src/main/resources/static/` - UI 빌드 결과물 배치

**빌드 특징**:
```kotlin
// UI 모듈들의 WAR 파일에서 정적 리소스 추출
tasks.register<Copy>("copyWebResources") {
    dependsOn(":worklist-ui:build")
    dependsOn(":dna-ui:build")
    dependsOn(":library-ui:build")
    dependsOn(":sequencing-ui:build")
    // JS, CSS, HTML, 이미지 등을 gateway/src/main/resources/static/에 복사
}
```

---

#### 📦 공통 라이브러리 모듈

##### `data/` - 공통 데이터 모델
**역할**: 모든 모듈이 의존하는 공통 DTO 및 API 인터페이스
**위치**: `/data/src/main/java/com/gcgenome/lims/`

**주요 구조**:
```
data/
├── dto/                           # 데이터 전송 객체
│   ├── Comment.java               # 코멘트
│   ├── Gene.java                  # 유전자 정보
│   ├── Report.java                # 보고서
│   ├── SnvConsensualClass.java    # SNV 합의 분류
│   └── interpretation/            # 검사 타입별 DTO
│       ├── GenomeScreen.java      # 전장 유전체 스크리닝
│       ├── BloodCancer.java       # 혈액암
│       ├── SolidTumor2.java       # 고형암
│       ├── PanelTest.java         # 패널 검사
│       ├── MrdScreen.java         # MRD 스크리닝
│       ├── SingleGene.java        # 단일 유전자 검사
│       ├── Hrd.java               # 상동 재조합 결핍 검사
│       ├── Dgs.java               # DiGeorge 증후군
│       ├── Mrd.java               # 미세잔존암
│       └── Sanger.java            # 생어 시퀀싱
└── api/                           # API 인터페이스 정의
```

**GWT 호환성**:
- `Data.gwt.xml` 파일로 GWT 모듈 정의
- 모든 DTO는 `Serializable` 구현
- GWT에서 사용 불가능한 Java API 사용 금지

**빌드 설정**:
```kotlin
tasks.compileGwt {
    extraJvmArgs = listOf(
        "-XX:ReservedCodeCacheSize=512M",
        "-javaagent:${lombok}=ECJ",
        "-generateJsInteropExport"
    )
}
```

##### `service/` - 공통 서비스
**역할**: 공통 유틸리티 및 헬퍼 클래스

##### `sample-panel/` - 샘플 패널 공통 기능
**역할**: 샘플 패널 관련 공통 기능 및 위젯
**GWT 모듈**: `SamplePanel.gwt.xml`

---

#### 🔬 비즈니스 도메인 모듈 (백엔드)

##### `worklist/` - 작업 목록 관리
**역할**: 검사 작업 목록 및 큐 관리
**기술**: Spring Boot + JPA + PostgreSQL
**진입점**: `com.gcgenome.lims.Application`

**주요 기능**:
- 검사 작업 생성, 조회, 수정, 삭제
- 작업 상태 관리 (대기/진행중/완료)
- 작업 우선순위 관리
- 검사자 할당

##### `sample/` - 샘플 관리
**역할**: 검사 샘플 정보 관리
**진입점**: `com.greencross.lims.Application`

**주요 기능**:
- 샘플 접수
- 샘플 정보 조회/수정
- 바코드 관리
- 샘플 이력 추적

##### `request/` - 검사 요청 관리
**역할**: 검사 요청 접수 및 관리
**진입점**: `com.gcgenome.lims.Application`

**주요 기능**:
- 검사 요청서 접수
- 요청 정보 관리
- 의뢰 기관 관리

##### `reading/` - 데이터 읽기
**역할**: 시퀀싱 데이터 읽기 및 품질 관리
**진입점**: `com.greencross.lims.Application`

**주요 기능**:
- FastQ 파일 품질 분석
- QC 메트릭 계산
- 데이터 통계 생성

##### `analysis/` - 데이터 분석
**역할**: 유전체 데이터 분석 파이프라인 관리
**진입점**: `com.greencross.lims.Application`

**주요 기능**:
- 분석 파이프라인 실행 관리
- VCF 파일 처리
- 변이 필터링
- 어노테이션 정보 통합

##### `interpretation/` - 결과 해석
**역할**: 분석 결과 임상적 해석
**진입점**: `com.greencross.lims.Application`

**주요 기능**:
- 변이 임상적 의미 평가 (ACMG 가이드라인)
- 질병 연관성 분석
- 약물 반응성 예측
- 해석 템플릿 관리

**지원하는 검사 타입**:
- Genome Screen (전장 유전체)
- Blood Cancer (혈액암)
- Solid Tumor (고형암)
- Panel Test (패널 검사)
- MRD Screen (미세잔존암 스크리닝)
- Single Gene (단일 유전자)
- HRD (상동 재조합 결핍)
- DGS (DiGeorge 증후군)
- Sanger (생어 시퀀싱)

##### `report/` - 보고서 생성
**역할**: 검사 보고서 생성 및 관리
**진입점**: `com.greencross.lims.Application`

**주요 기능**:
- PDF 보고서 생성
- 보고서 템플릿 관리
- 서명 및 승인 관리
- 보고서 이력 관리

##### `publish/` - 결과 발행
**역할**: 최종 결과 발행 및 전송
**진입점**: `com.greencross.Application`

**주요 기능**:
- 검사 결과 최종 승인
- 외부 시스템 연동 (EMR 등)
- 발행 이력 관리

##### `preprocessing/` - 데이터 전처리
**역할**: 분석 전 데이터 전처리
**진입점**: `com.greencross.lims.Application`

##### `file/` - 파일 관리
**역할**: 파일 업로드/다운로드 관리
**진입점**: `com.greencross.lims.Application`

**주요 기능**:
- 파일 업로드/다운로드
- 파일 메타데이터 관리
- S3 연동 (추정)

##### `variant-snv/` - SNV 변이 분석
**역할**: Single Nucleotide Variant 분석
**진입점**: `com.gcgenome.lims.Application`

##### `snv/` - SNV 서비스
**역할**: SNV 관련 추가 서비스

##### `miscellaneous/` - 기타 기능
**역할**: 기타 보조 기능
**진입점**: `com.greencross.lims.Application`

##### `trello/` - Trello 연동
**역할**: Trello와의 통합
**사용 여부**: 현재 미사용 (Jenkinsfile에서 제외)

##### `legacy/` - 레거시 시스템 연동
**역할**: 기존 시스템과의 인터페이스
**진입점**: `com.greencross.lims.Application`

**주요 기능**:
- 결과 전송 (특정 조건에서만 빌드)
- 보고서 생성 레거시 인터페이스

##### `analysis-crawler/` - 분석 크롤러
**역할**: 분석 결과 자동 수집
**진입점**: `com.greencross.lims.Application`

##### `worklist-r2dbc/` - R2DBC 워크리스트
**역할**: 리액티브 방식의 워크리스트 서비스
**기술**: R2DBC (Reactive Database Connectivity)
**진입점**: `com.greencross.lims.Application`

---

#### 🎨 비즈니스 도메인 모듈 (프론트엔드)

모든 UI 모듈은 동일한 구조를 가집니다:

```
[모듈명]-ui/
├── build.gradle.kts               # GWT 플러그인 포함
└── src/main/
    ├── java/com/[패키지]/lims/
    │   ├── [모듈명].gwt.xml       # GWT 모듈 정의
    │   ├── client/                # 클라이언트 코드
    │   │   ├── Main.java          # UI 진입점
    │   │   └── [컴포넌트들]/
    │   ├── api/                   # 백엔드 API 호출
    │   │   └── [모듈명]Api.java
    │   ├── dto/                   # 프론트엔드 전용 DTO
    │   └── util/                  # 유틸리티
    └── webapp/                    # 정적 리소스
        └── index.html
```

##### `worklist-ui/` - 작업 목록 UI
**GWT 모듈**: `com.gcgenome.lims.Worklist`
**진입점**: `com.gcgenome.lims.client.Main`
**빌드 산출물**: `panel-service-worklist.war`

**주요 컴포넌트**:
- `WorkElement` - 개별 작업 위젯
- `WorklistElement` - 작업 목록 컨테이너
- `WorkGridElement` - 그리드 뷰

**API 연동**:
- `WorklistApi` - 워크리스트 API
- `DnaApi` - DNA 관련 API
- `QueueApi` - 큐 관리 API

##### `sample-ui/` - 샘플 관리 UI
**GWT 모듈**: `com.greencross.lims.Sample`

##### `request-ui/` - 검사 요청 UI
**GWT 모듈**: `com.gcgenome.lims.Request`

##### `dna-ui/` - DNA 추출 UI
**GWT 모듈**: `com.greencross.lims.DNA`
**빌드 산출물**: `panel-service-dna-ui.war`

##### `library-ui/` - 라이브러리 제작 UI
**GWT 모듈**: `com.greencross.lims.Library`
**빌드 산출물**: `panel-service-library-ui.war`

##### `sequencing-ui/` - 시퀀싱 UI
**GWT 모듈**: `com.greencross.lims.Sequencing`
**빌드 산출물**: `panel-service-sequencing.war`

##### `reading-ui/` - 데이터 읽기 UI
**GWT 모듈**: `com.gcgenome.lims.Reading`

##### `analysis-ui/` - 데이터 분석 UI
**GWT 모듈**: `com.gcgenome.lims.Analysis`

**주요 기능**:
- 분석 파이프라인 모니터링
- VCF 뷰어
- 변이 필터 UI

##### `interpretation-ui/` - 결과 해석 UI
**GWT 모듈**: `com.gcgenome.lims.Interpretation`

**주요 기능**:
- 변이 분류 인터페이스 (ACMG)
- 문헌 검색 및 링크
- 해석 템플릿 에디터

##### `report-ui/` - 보고서 생성 UI
**GWT 모듈**: `com.gcgenome.lims.Report`

##### `preprocessing-ui/` - 전처리 UI
**GWT 모듈**: `com.greencross.lims.Preprocessing`

##### `file-ui/` - 파일 관리 UI
**GWT 모듈**: `com.greencross.lims.File`
**사용 여부**: 현재 미사용

##### `variant-snv-ui/` - SNV 변이 UI
**GWT 모듈**: `com.gcgenome.lims.VarSnv`

##### `snv-ui/` - SNV UI
**GWT 모듈**: `com.greencross.lims.Snv`

##### `miscellaneous-ui/` - 기타 기능 UI
**GWT 모듈**: `com.greencross.lims.Misc`

##### `trello-ui/` - Trello 연동 UI
**GWT 모듈**: `com.greencross.lims.Trello`
**사용 여부**: 현재 미사용

---

## 워크플로우 및 데이터 흐름

### 전체 비즈니스 프로세스

```
1. 검사 요청 접수 (request)
   ↓
2. 샘플 등록 (sample)
   ↓
3. 작업 목록 생성 (worklist)
   ↓
4. DNA 추출 (dna)
   ↓
5. 라이브러리 제작 (library)
   ↓
6. 시퀀싱 (sequencing)
   ↓
7. 데이터 읽기 및 QC (reading)
   ↓
8. 전처리 (preprocessing)
   ↓
9. 데이터 분석 (analysis)
   ↓
10. 변이 분석 (variant-snv, snv)
   ↓
11. 결과 해석 (interpretation)
   ↓
12. 보고서 생성 (report)
   ↓
13. 결과 발행 (publish)
   ↓
14. 레거시 시스템 전송 (legacy)
```

### 데이터 흐름

#### 1. UI → Backend 흐름
```
GWT UI (JavaScript)
  → REST API 호출
  → Gateway (Spring Cloud Gateway)
  → Zookeeper 서비스 디스커버리
  → 백엔드 마이크로서비스
  → 데이터베이스 (PostgreSQL/Cassandra)
```

#### 2. 서비스 간 통신
```
서비스 A
  → Kafka 메시지 발행
  → Kafka Broker
  → 서비스 B가 메시지 구독
  → 비즈니스 로직 처리
```

#### 3. 파일 처리 흐름
```
파일 업로드 (file-ui)
  → file 서비스
  → S3 또는 로컬 스토리지
  → Cassandra 메타데이터 저장

분석 파이프라인
  → 파일 다운로드
  → 분석 실행
  → 결과 파일 저장
  → 결과 메타데이터 DB 저장
```

#### 4. 실시간 업데이트
```
백엔드 이벤트 발생
  → Kafka 이벤트 발행
  → analysis-crawler가 수집
  → 데이터베이스 업데이트
  → (선택적) WebSocket/SSE로 UI에 푸시
```

### API 라우팅 예시

Gateway의 라우팅 규칙 (추정):
```
/api/worklist/**    → worklist 서비스
/api/sample/**      → sample 서비스
/api/analysis/**    → analysis 서비스
/api/interpretation/** → interpretation 서비스
/api/report/**      → report 서비스
...
```

정적 리소스:
```
/                   → index.html
/worklist/**        → worklist-ui 정적 리소스
/dna/**             → dna-ui 정적 리소스
/library/**         → library-ui 정적 리소스
/sequencing/**      → sequencing-ui 정적 리소스
```

---

## 빌드 및 배포

### 로컬 빌드

#### 전체 프로젝트 빌드
```bash
# 전체 빌드
gradle build

# 특정 모듈만 빌드
gradle :worklist:build
gradle :worklist-ui:build

# 백엔드 JAR 생성
gradle :worklist:bootJar

# 프론트엔드 WAR 생성
gradle :worklist-ui:war
```

#### GWT 개발 모드 (Hot Reload)
```bash
# 특정 UI 모듈 개발 모드 실행
gradle :worklist-ui:gwtDev

# 접속: http://localhost:9570
# Code Server: http://localhost:9571
```

#### Gateway 빌드 (UI 통합)
```bash
# Gateway는 UI 모듈들의 정적 리소스를 자동으로 복사
gradle :gateway:build

# 순서:
# 1. worklist-ui, dna-ui, library-ui, sequencing-ui 빌드
# 2. 각 WAR 파일에서 정적 리소스 추출
# 3. gateway/src/main/resources/static/에 복사
# 4. gateway JAR에 패키징
```

### Jenkins CI/CD 파이프라인

#### 파이프라인 파라미터
- `DEPLOY_ENV`: 배포 환경 (`test` 또는 `prod`)

#### 빌드 단계

**Jenkinsfile 구조**:
```groovy
pipeline {
    environment {
        GITHUB_CREDS = credentials('github-creds')
        GITHUB_USERNAME = "${GITHUB_CREDS_USR}"
        GITHUB_TOKEN = "${GITHUB_CREDS_PSW}"
    }

    stages {
        stage('Sample Build') {
            tools {
                jdk 'java-17'
                gradle 'gradle-8.2'
            }
            steps {
                sh 'gradle clean :sample:bootJar'
                buildAndDeploy('sample', 'panel-service-sample')
            }
        }

        stage('Analysis-UI Build') {
            tools {
                jdk 'java-21'
                gradle 'gradle-8.9'
            }
            steps {
                sh 'gradle :analysis-ui:copyWebResources'
                buildAndDeployFE('analysis-ui')
            }
        }

        // ... 기타 모듈들
    }
}
```

#### JDK 버전별 모듈 분류

**JDK 17 사용 모듈** (Gradle 8.2):
- sample, reading, analysis, interpretation, report, request
- preprocessing, variant-snv, miscellaneous, legacy
- worklist-r2dbc

**JDK 21 사용 모듈** (Gradle 8.9):
- publish
- 모든 UI 모듈 (analysis-ui, reading-ui, interpretation-ui 등)

#### 배포 함수 (추정)

**백엔드 배포**:
```groovy
def buildAndDeploy(moduleName, serviceName) {
    // 1. Docker 이미지 빌드
    sh "docker build -t ${serviceName}:${BUILD_NUMBER} ./${moduleName}"

    // 2. ECR 로그인
    sh "aws ecr get-login-password --region ap-northeast-2 | docker login --username AWS --password-stdin ${ECR_REGISTRY}"

    // 3. 이미지 태깅
    sh "docker tag ${serviceName}:${BUILD_NUMBER} ${ECR_REGISTRY}/${serviceName}:${BUILD_NUMBER}"

    // 4. ECR 푸시
    sh "docker push ${ECR_REGISTRY}/${serviceName}:${BUILD_NUMBER}"

    // 5. 배포 (ECS/EKS/EC2 등)
    // ...
}
```

**프론트엔드 배포**:
```groovy
def buildAndDeployFE(moduleName) {
    // 1. copyWebResources 태스크 실행 (이미 완료)

    // 2. 정적 리소스를 S3 또는 CDN에 업로드
    sh "aws s3 sync ./build/static/ s3://${S3_BUCKET}/${moduleName}/"

    // 3. CloudFront 캐시 무효화
    sh "aws cloudfront create-invalidation --distribution-id ${CF_DIST_ID} --paths '/${moduleName}/*'"
}
```

#### 제외된 모듈
현재 Jenkins 파이프라인에서 빌드하지 않는 모듈:
- file (미사용)
- trello (미사용)
- cnv (미사용)
- snv (미사용)

조건부 빌드:
- legacy (결과 전송, 보고서 생성 기능은 특정 조건에서만 빌드)

### GitHub Actions 워크플로우

**`.github/workflows/code-review.yaml`**:
```yaml
name: Code Review with Gemini AI

on:
  pull_request:
    types: [opened, synchronize]

jobs:
  code-review:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3

      - name: Get PR diff
        run: git diff origin/${{ github.base_ref }}...HEAD > diff.txt

      - name: Gemini AI Review
        uses: google/generative-ai-action@v1
        with:
          model: gemini-1.5-pro
          prompt: |
            다음 코드 변경사항을 리뷰해주세요:
            $(cat diff.txt)

      - name: Post comment
        uses: actions/github-script@v6
        with:
          script: |
            github.rest.issues.createComment({
              issue_number: context.issue.number,
              owner: context.repo.owner,
              repo: context.repo.repo,
              body: process.env.GEMINI_RESPONSE
            })
```

---

## 유지보수 가이드

### 신규 유지보수 담당자를 위한 가이드

#### 1단계: 환경 구성

**필수 도구 설치**:
```bash
# JDK 17, 21 설치
sudo apt install openjdk-17-jdk openjdk-21-jdk

# Gradle 설치
sdk install gradle 8.9

# Docker 설치
sudo apt install docker.io docker-compose

# AWS CLI 설치 (배포용)
curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "awscliv2.zip"
unzip awscliv2.zip
sudo ./aws/install
```

**인프라 실행** (로컬 개발):
```bash
# docker-compose.yml이 있다면
docker-compose up -d

# Cassandra, Zookeeper, Kafka 컨테이너 실행 확인
docker ps
```

**GitHub 패키지 인증 설정**:
```bash
# ~/.gradle/gradle.properties 파일 생성
cat > ~/.gradle/gradle.properties << EOF
github_username=YOUR_GITHUB_USERNAME
github_password=YOUR_GITHUB_TOKEN
EOF
```

#### 2단계: 프로젝트 구조 파악

**우선순위 순으로 읽어야 할 파일**:
1. `readme.md` - 프로젝트 개요
2. `settings.gradle.kts` - 모듈 목록
3. `build.gradle.kts` - 공통 빌드 설정
4. `Jenkinsfile` - 배포 프로세스
5. `gateway/src/main/java/.../Router.java` - API 라우팅
6. `data/src/main/java/.../dto/` - 데이터 모델

**코드 읽기 순서**:
```
1. data 모듈 (공통 DTO)
   ↓
2. gateway 모듈 (라우팅 이해)
   ↓
3. 관심 있는 도메인의 백엔드 모듈
   ↓
4. 해당 도메인의 UI 모듈
```

#### 3단계: 로컬 실행

**백엔드 서비스 실행**:
```bash
# application.yml 설정 (gitignored)
# 각 모듈의 src/main/resources/application.yml 생성 필요

# 예시: worklist 서비스
cd worklist
# application.yml 작성 (DB 연결 정보, Zookeeper 주소 등)
gradle bootRun
```

**프론트엔드 개발 모드**:
```bash
# GWT SuperDev Mode
cd worklist-ui
gradle gwtDev

# 브라우저에서 http://localhost:9570 접속
# 코드 변경 시 자동 리컴파일
```

**Gateway 실행** (통합 환경):
```bash
# UI 모듈들 먼저 빌드
gradle :worklist-ui:build
gradle :dna-ui:build
gradle :library-ui:build
gradle :sequencing-ui:build

# Gateway 빌드 및 실행
gradle :gateway:bootRun

# 접속: http://localhost:8080
```

#### 4단계: 주요 작업별 가이드

##### 새로운 DTO 추가
```java
// 1. data 모듈에 DTO 추가
// data/src/main/java/com/gcgenome/lims/dto/NewDto.java
package com.gcgenome.lims.dto;

import lombok.Data;
import java.io.Serializable;

@Data
public class NewDto implements Serializable {
    private Long id;
    private String name;
    // GWT 호환: Date 대신 Long, Java 8 Time API 사용 금지
}

// 2. Data.gwt.xml에 추가 (필요시)
// <source path="dto"/>

// 3. data 모듈 빌드
gradle :data:build

// 4. 의존하는 모듈들 리빌드
gradle :worklist:build
```

##### 새로운 API 엔드포인트 추가
```java
// 1. 백엔드 컨트롤러에 추가
// worklist/src/main/java/.../controller/WorklistController.java
@RestController
@RequestMapping("/api/worklist")
public class WorklistController {

    @GetMapping("/new-endpoint")
    public ResponseEntity<NewDto> getNewData() {
        // 구현
    }
}

// 2. UI에서 API 호출 추가
// worklist-ui/src/main/java/.../api/WorklistApi.java
public class WorklistApi {
    public static void getNewData(Callback<NewDto> callback) {
        String url = "/api/worklist/new-endpoint";
        JsonUtils.get(url, callback);
    }
}

// 3. UI 컴포넌트에서 사용
WorklistApi.getNewData(new Callback<NewDto>() {
    @Override
    public void onSuccess(NewDto result) {
        // UI 업데이트
    }
});
```

##### 새로운 UI 화면 추가
```java
// 1. GWT 컴포넌트 생성
// worklist-ui/src/main/java/.../client/new/NewPageElement.java
public class NewPageElement extends HTMLElement {

    @Override
    public HTMLElement element() {
        return div()
            .add(h1("새로운 페이지"))
            .add(button("저장").onClick(this::onSave))
            .element();
    }

    private void onSave(Event e) {
        // 저장 로직
    }
}

// 2. 라우팅 추가 (Main.java)
// worklist-ui/src/main/java/.../client/Main.java
public class Main implements EntryPoint {
    @Override
    public void onModuleLoad() {
        Router router = new Router();
        router.addRoute("/new-page", () -> new NewPageElement());
    }
}
```

##### 새로운 마이크로서비스 추가
```bash
# 1. settings.gradle.kts에 모듈 추가
echo 'include("new-service")' >> settings.gradle.kts
echo 'include("new-service-ui")' >> settings.gradle.kts

# 2. 디렉토리 생성
mkdir -p new-service/src/main/java/com/greencross/lims
mkdir -p new-service/src/main/resources

# 3. build.gradle.kts 작성 (기존 모듈 참고)
cp worklist/build.gradle.kts new-service/build.gradle.kts

# 4. Application.java 작성
cat > new-service/src/main/java/com/greencross/lims/Application.java << 'EOF'
package com.greencross.lims;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
EOF

# 5. application.yml 작성
cat > new-service/src/main/resources/application.yml << 'EOF'
spring:
  application:
    name: new-service
  cloud:
    zookeeper:
      connect-string: localhost:2181

server:
  port: 8090
EOF

# 6. Jenkinsfile에 빌드 스테이지 추가
# (수동으로 편집)
```

#### 5단계: 디버깅

**백엔드 디버깅**:
```bash
# IntelliJ IDEA에서 Remote Debug 설정
# Run → Edit Configurations → Remote JVM Debug

# Gradle로 디버그 모드 실행
gradle :worklist:bootRun --debug-jvm

# 또는 JAR 직접 실행
java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005 \
     -jar worklist/build/libs/panel-service-worklist.jar

# IntelliJ에서 localhost:5005로 연결
```

**프론트엔드 디버깅**:
```bash
# GWT SuperDev Mode 실행
gradle :worklist-ui:gwtDev

# Chrome DevTools 사용
# 1. http://localhost:9570 접속
# 2. F12 → Sources 탭
# 3. Java 소스 코드가 매핑되어 있어 브레이크포인트 설정 가능
```

**로그 확인**:
```bash
# 백엔드 로그
tail -f logs/application.log

# Docker 컨테이너 로그
docker logs -f cassandra
docker logs -f zookeeper
docker logs -f kafka
```

#### 6단계: 테스트

**단위 테스트**:
```bash
# 전체 테스트
gradle test

# 특정 모듈 테스트
gradle :worklist:test

# 테스트 리포트: build/reports/tests/test/index.html
```

**통합 테스트**:
```bash
# Spring Boot 통합 테스트
gradle :worklist:integrationTest
```

**E2E 테스트** (UI):
```bash
# GWT 테스트 모드
gradle :worklist-ui:gwtTest
```

#### 7단계: 배포

**Test 환경 배포**:
```bash
# Jenkins에서 파이프라인 실행
# Parameters: DEPLOY_ENV = test

# 또는 수동 배포
gradle :worklist:bootJar
docker build -t panel-service-worklist:latest ./worklist
docker push ${ECR_REGISTRY}/panel-service-worklist:latest
```

**Production 배포**:
```bash
# Jenkins에서 파이프라인 실행
# Parameters: DEPLOY_ENV = prod

# 주의사항:
# - 반드시 test 환경에서 먼저 검증
# - 배포 전 데이터베이스 백업
# - 롤백 계획 수립
```

---

### 일반적인 유지보수 시나리오

#### 버그 수정

**프로세스**:
```
1. 이슈 확인
   - 사용자 리포트 또는 모니터링 알람
   - 재현 단계 파악

2. 로컬에서 재현
   - 해당 모듈 실행
   - 디버거 연결
   - 로그 분석

3. 원인 파악
   - 스택 트레이스 분석
   - 데이터베이스 상태 확인
   - API 호출 흐름 추적

4. 수정
   - 코드 수정
   - 단위 테스트 추가
   - 로컬 검증

5. PR 생성
   - 변경사항 커밋
   - PR 생성 → GitHub Actions가 자동 리뷰
   - 코드 리뷰 받기

6. 배포
   - PR 머지
   - Jenkins 파이프라인 실행 (test → prod)
```

**예시: worklist에서 작업 조회 오류**
```bash
# 1. 로그 확인
tail -f worklist/logs/application.log
# ERROR: NullPointerException at WorklistService.getWork()

# 2. 로컬 재현
gradle :worklist:bootRun --debug-jvm

# 3. IntelliJ에서 디버거 연결하여 원인 파악
# → work.getStatus()에서 work가 null

# 4. 코드 수정
# WorklistService.java
public Work getWork(Long id) {
    Work work = workRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Work not found: " + id));
    return work;
}

# 5. 테스트 추가
@Test
public void testGetWorkNotFound() {
    assertThrows(NotFoundException.class, () -> {
        worklistService.getWork(999L);
    });
}

# 6. 커밋 및 PR
git add .
git commit -m "fix: handle null work in getWork()"
git push origin feature/fix-worklist-null
# GitHub에서 PR 생성
```

#### 기능 추가

**프로세스**:
```
1. 요구사항 분석
   - 비즈니스 요구사항 문서화
   - UI 목업 작성
   - API 스펙 정의

2. 설계
   - 데이터 모델 설계 (DTO 추가)
   - API 엔드포인트 설계
   - UI 컴포넌트 설계

3. 구현
   a. data 모듈에 DTO 추가
   b. 백엔드 구현 (Entity, Repository, Service, Controller)
   c. UI 구현 (API, Component)

4. 테스트
   - 단위 테스트
   - 통합 테스트
   - UI 테스트

5. 문서화
   - API 문서 업데이트
   - 사용자 가이드 작성

6. 배포
```

#### 성능 최적화

**분석 도구**:
```bash
# JVM 프로파일링
# VisualVM, JProfiler 등 사용

# 데이터베이스 쿼리 분석
# PostgreSQL: EXPLAIN ANALYZE
# Cassandra: TRACING ON

# API 응답 시간 모니터링
# Spring Boot Actuator 사용
curl http://localhost:8080/actuator/metrics/http.server.requests
```

**일반적인 최적화**:
- N+1 쿼리 문제 → Join Fetch 사용
- 불필요한 데이터 로딩 → DTO Projection
- 느린 API → 캐싱 추가 (Redis)
- GWT 번들 크기 큰 경우 → Code Splitting

#### 데이터베이스 마이그레이션

**Flyway 또는 Liquibase 사용 (권장)**:
```sql
-- V1__initial_schema.sql
CREATE TABLE work (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255),
    status VARCHAR(50),
    created_at TIMESTAMP
);

-- V2__add_priority.sql
ALTER TABLE work ADD COLUMN priority INTEGER DEFAULT 0;
```

```bash
# 마이그레이션 실행
gradle :worklist:flywayMigrate
```

#### 보안 패치

**의존성 취약점 확인**:
```bash
# Gradle 의존성 검사
gradle dependencyCheckAnalyze

# 취약점 있는 라이브러리 업데이트
# build.gradle.kts에서 버전 수정
implementation("org.apache.logging.log4j:log4j-core:2.17.0")  # 2.17.0 이상
```

**Log4Shell 예방 (이미 적용됨)**:
```kotlin
// gateway/build.gradle.kts
implementation("org.apache.logging.log4j:log4j-core:2.17.0")  // 패치된 버전
```

---

### 코드 컨벤션

#### Java/Kotlin 코드 스타일

**네이밍**:
- 클래스: PascalCase (`WorklistService`)
- 메서드/변수: camelCase (`getWorkById`)
- 상수: UPPER_SNAKE_CASE (`MAX_RETRY_COUNT`)
- 패키지: lowercase (`com.greencross.lims`)

**구조**:
```java
// Controller
@RestController
@RequestMapping("/api/worklist")
public class WorklistController {
    private final WorklistService service;

    @GetMapping("/{id}")
    public ResponseEntity<Work> getWork(@PathVariable Long id) {
        return ResponseEntity.ok(service.getWork(id));
    }
}

// Service
@Service
public class WorklistService {
    private final WorkRepository repository;

    public Work getWork(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new NotFoundException("Work not found"));
    }
}

// Repository
public interface WorkRepository extends JpaRepository<Work, Long> {
    List<Work> findByStatus(String status);
}
```

#### GWT 코드 스타일

**컴포넌트 구조**:
```java
public class WorkElementUI extends HTMLElement {
    private final Work work;

    public WorkElementUI(Work work) {
        this.work = work;
    }

    @Override
    public HTMLElement element() {
        return div().css("work-element")
            .add(renderHeader())
            .add(renderBody())
            .add(renderActions())
            .element();
    }

    private HTMLElement renderHeader() {
        return h3(work.getName()).element();
    }

    private HTMLElement renderActions() {
        return div()
            .add(button("편집").onClick(this::onEdit))
            .add(button("삭제").onClick(this::onDelete))
            .element();
    }

    private void onEdit(Event e) {
        WorklistApi.updateWork(work, new Callback<Work>() {
            @Override
            public void onSuccess(Work result) {
                // 성공 처리
            }
        });
    }
}
```

#### Git 커밋 메시지

**형식**:
```
<type>(<scope>): <subject>

<body>

<footer>
```

**타입**:
- `feat`: 새로운 기능
- `fix`: 버그 수정
- `docs`: 문서 변경
- `style`: 코드 포맷팅
- `refactor`: 리팩토링
- `test`: 테스트 추가
- `chore`: 빌드 설정 등

**예시**:
```
feat(worklist): add priority filtering

사용자가 우선순위별로 작업 목록을 필터링할 수 있도록 기능 추가

Closes #123
```

---

## 트러블슈팅

### 자주 발생하는 문제

#### 1. Gradle 빌드 실패

**증상**:
```
Could not resolve com.greencross:lims-api-gateway-data:1.0
```

**원인**: GitHub Packages 인증 실패

**해결**:
```bash
# ~/.gradle/gradle.properties 확인
cat ~/.gradle/gradle.properties
github_username=YOUR_USERNAME
github_password=YOUR_PERSONAL_ACCESS_TOKEN

# 또는 환경 변수 설정
export GITHUB_USERNAME=your_username
export GITHUB_TOKEN=your_token
```

#### 2. GWT 컴파일 실패

**증상**:
```
[ERROR] Errors in 'com/gcgenome/lims/client/Main.java'
[ERROR] Line XX: No source code is available for type java.time.LocalDate
```

**원인**: GWT는 Java 8+ API 일부를 지원하지 않음

**해결**:
```java
// ❌ 사용 불가
import java.time.LocalDate;
LocalDate date = LocalDate.now();

// ✅ 사용 가능
import java.util.Date;
Date date = new Date();

// 또는 Long으로 타임스탬프 저장
Long timestamp = System.currentTimeMillis();
```

#### 3. Zookeeper 연결 실패

**증상**:
```
Unable to connect to zookeeper server: localhost:2181
```

**원인**: Zookeeper 서비스 미실행

**해결**:
```bash
# Docker Compose로 Zookeeper 실행
docker-compose up -d zookeeper

# 연결 확인
echo stat | nc localhost 2181
```

#### 4. Cassandra 연결 실패

**증상**:
```
All host(s) tried for query failed
```

**원인**: Cassandra 미실행 또는 키스페이스 미생성

**해결**:
```bash
# Cassandra 실행
docker-compose up -d cassandra

# 키스페이스 생성
docker exec -it cassandra cqlsh
CREATE KEYSPACE lims WITH replication = {'class':'SimpleStrategy', 'replication_factor':3};
```

#### 5. Gateway 정적 리소스 404

**증상**: 브라우저에서 `/worklist/index.html` 접속 시 404

**원인**: UI 모듈 빌드가 Gateway보다 먼저 실행되지 않음

**해결**:
```bash
# UI 모듈 먼저 빌드
gradle :worklist-ui:build

# Gateway 빌드 (copyWebResources 자동 실행)
gradle :gateway:build

# 또는 명시적으로 실행
gradle :gateway:copyWebResources
gradle :gateway:bootRun
```

#### 6. CORS 오류

**증상**: 브라우저 콘솔에 CORS 에러

**원인**: Gateway의 CORS 설정 누락

**해결**:
```java
// gateway/src/main/java/.../WebConfig.java
@Configuration
public class WebConfig {
    @Bean
    public CorsWebFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("http://localhost:9570");  // GWT Dev 모드
        config.addAllowedMethod("*");
        config.addAllowedHeader("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsWebFilter(source);
    }
}
```

#### 7. Out of Memory (GWT 컴파일)

**증상**:
```
Java heap space
```

**원인**: GWT 컴파일 시 메모리 부족

**해결**:
```kotlin
// build.gradle.kts
tasks.gwt {
    minHeapSize = "2048M"  // 증가
    maxHeapSize = "4096M"  // 증가
}
```

```bash
# 또는 Gradle JVM 옵션 설정
export GRADLE_OPTS="-Xmx4g"
gradle :worklist-ui:build
```

#### 8. Jenkins 빌드 실패 (Credential 오류)

**증상**:
```
ERROR: Credentials 'github-creds' not found
```

**원인**: Jenkins Credentials 미등록

**해결**:
1. Jenkins 관리 → Credentials → Add Credentials
2. Kind: Username with password
3. ID: `github-creds`
4. Username: GitHub 사용자명
5. Password: GitHub Personal Access Token

#### 9. Docker 이미지 푸시 실패

**증상**:
```
denied: Your authorization token has expired
```

**원인**: ECR 로그인 만료

**해결**:
```bash
# ECR 재로그인
aws ecr get-login-password --region ap-northeast-2 | \
    docker login --username AWS --password-stdin ${ECR_REGISTRY}

# 이미지 푸시
docker push ${ECR_REGISTRY}/panel-service-worklist:latest
```

---

### 성능 이슈

#### 느린 API 응답

**진단**:
```java
// Controller에 로깅 추가
@GetMapping("/{id}")
public ResponseEntity<Work> getWork(@PathVariable Long id) {
    long start = System.currentTimeMillis();
    Work work = service.getWork(id);
    long duration = System.currentTimeMillis() - start;
    log.info("getWork took {}ms", duration);
    return ResponseEntity.ok(work);
}
```

**해결책**:
1. **데이터베이스 쿼리 최적화**
   ```java
   // N+1 문제 해결
   @Query("SELECT w FROM Work w JOIN FETCH w.samples WHERE w.id = :id")
   Optional<Work> findByIdWithSamples(@Param("id") Long id);
   ```

2. **캐싱 추가**
   ```java
   @Cacheable("works")
   public Work getWork(Long id) {
       return repository.findById(id).orElseThrow();
   }
   ```

3. **비동기 처리**
   ```java
   @Async
   public CompletableFuture<Work> getWorkAsync(Long id) {
       return CompletableFuture.completedFuture(
           repository.findById(id).orElseThrow()
       );
   }
   ```

#### GWT 번들 크기 큰 경우

**해결**:
```java
// Code Splitting 적용
GWT.runAsync(new RunAsyncCallback() {
    public void onFailure(Throwable err) {
        Window.alert("Failed to load module");
    }

    public void onSuccess() {
        // 모듈 로드 성공
        new HeavyComponent().render();
    }
});
```

---

### 데이터베이스 이슈

#### Cassandra 노드 다운

**진단**:
```bash
docker exec -it cassandra nodetool status
# UN (Up/Normal) 상태 확인
```

**해결**:
```bash
# 노드 재시작
docker restart cassandra

# 복구 확인
docker exec -it cassandra nodetool repair
```

#### PostgreSQL 연결 풀 고갈

**증상**:
```
HikariPool - Connection is not available
```

**해결**:
```yaml
# application.yml
spring:
  datasource:
    hikari:
      maximum-pool-size: 20  # 증가
      minimum-idle: 5
      connection-timeout: 30000
```

---

## 부록

### 주요 설정 파일 예시

#### application.yml (worklist 모듈)

```yaml
spring:
  application:
    name: worklist

  datasource:
    url: jdbc:postgresql://localhost:5432/lims
    username: lims_user
    password: ${DB_PASSWORD}
    driver-class-name: org.postgresql.Driver
    hikari:
      maximum-pool-size: 10
      minimum-idle: 5

  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: false
    properties:
      hibernate:
        format_sql: true
        dialect: org.hibernate.dialect.PostgreSQLDialect

  cloud:
    zookeeper:
      connect-string: localhost:2181
      discovery:
        enabled: true

  kafka:
    bootstrap-servers: localhost:9092
    consumer:
      group-id: worklist-group
      auto-offset-reset: earliest
    producer:
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: org.springframework.kafka.support.serializer.JsonSerializer

server:
  port: 8081

logging:
  level:
    root: INFO
    com.gcgenome.lims: DEBUG
  file:
    name: logs/worklist.log
```

#### docker-compose.yml

```yaml
version: '3.8'

services:
  zookeeper:
    image: confluentinc/cp-zookeeper:latest
    environment:
      ZOOKEEPER_CLIENT_PORT: 2181
      ZOOKEEPER_TICK_TIME: 2000
    ports:
      - "2181:2181"

  kafka:
    image: confluentinc/cp-kafka:latest
    depends_on:
      - zookeeper
    environment:
      KAFKA_BROKER_ID: 1
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://localhost:9092
      KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1
    ports:
      - "9092:9092"

  cassandra:
    image: cassandra:4.0
    environment:
      CASSANDRA_CLUSTER_NAME: lims-cluster
      CASSANDRA_DC: dc1
      CASSANDRA_RACK: rack1
    ports:
      - "9042:9042"
    volumes:
      - cassandra-data:/var/lib/cassandra

  postgres:
    image: postgres:14
    environment:
      POSTGRES_DB: lims
      POSTGRES_USER: lims_user
      POSTGRES_PASSWORD: lims_password
    ports:
      - "5432:5432"
    volumes:
      - postgres-data:/var/lib/postgresql/data

volumes:
  cassandra-data:
  postgres-data:
```

### 참고 자료

- [Spring Boot 공식 문서](https://spring.io/projects/spring-boot)
- [Spring Cloud Gateway 문서](https://spring.io/projects/spring-cloud-gateway)
- [GWT 공식 문서](http://www.gwtproject.org/doc/latest/DevGuide.html)
- [Gradle Kotlin DSL 가이드](https://docs.gradle.org/current/userguide/kotlin_dsl.html)
- [Jenkins Pipeline 문서](https://www.jenkins.io/doc/book/pipeline/)
- [Cassandra 문서](https://cassandra.apache.org/doc/latest/)
- [Kafka 문서](https://kafka.apache.org/documentation/)

### 연락처

- 프로젝트 관리자: [담당자 이메일]
- 기술 지원: [기술 지원 채널]
- Issue 트래커: [GitHub Issues 링크]

---

**문서 버전**: 1.0
**최종 수정일**: 2025-11-20
**작성자**: Claude AI (유지보수 가이드)
