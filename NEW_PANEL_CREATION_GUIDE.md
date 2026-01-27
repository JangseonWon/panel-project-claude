# 새로운 패널(검사 타입) 추가 가이드

## 📋 목차
1. [개요](#개요)
2. [사전 준비](#사전-준비)
3. [단계별 구현 가이드](#단계별-구현-가이드)
4. [테스트 및 검증](#테스트-및-검증)
5. [배포](#배포)
6. [체크리스트](#체크리스트)

---

## 개요

### 패널이란?

**패널(Panel)**은 특정 질환이나 검사 목적에 따라 정의된 유전자 검사 타입을 의미합니다.

**현재 운영 중인 패널**:
- `GenomeScreen` - 전장 유전체 스크리닝
- `BloodCancer` - 혈액암
- `SolidTumor2` - 고형암
- `PanelTest` - 패널 검사
- `MrdScreen` - MRD 스크리닝
- `SingleGene` - 단일 유전자 검사
- `Hrd` - 상동 재조합 결핍 검사
- `Dgs` - DiGeorge 증후군
- `Mrd` - 미세잔존암
- `Sanger` - 생어 시퀀싱

### 새로운 패널 추가 시 영향 범위

```
┌─────────────────────────────────────────────────────────────┐
│                    새로운 패널 추가                           │
└─────────────────┬───────────────────────────────────────────┘
                  │
      ┌───────────┴───────────┐
      │                       │
   데이터 모델              비즈니스 로직
   (data 모듈)           (백엔드 서비스)
      │                       │
      ├─ DTO 정의             ├─ Controller (API)
      └─ GWT 호환             ├─ Service (비즈니스 로직)
                              ├─ Repository (데이터 접근)
                              └─ Template (보고서 생성)
      │                       │
      └───────────┬───────────┘
                  │
      ┌───────────┴───────────┐
      │                       │
   UI 컴포넌트            인프라 설정
(interpretation-ui)      (데이터베이스 등)
      │                       │
      ├─ 입력 폼              ├─ PostgreSQL 스키마
      ├─ 데이터 테이블        ├─ Cassandra 문서
      ├─ 해석 화면            └─ Elasticsearch 매핑 (선택)
      └─ 상태 관리
```

---

## 사전 준비

### 1. 요구사항 정의

새로운 패널을 추가하기 전에 다음 정보를 명확히 정의해야 합니다:

#### 필수 정의 항목

| 항목 | 설명 | 예시 |
|------|------|------|
| **패널 코드명** | 내부에서 사용할 고유 식별자 (CamelCase) | `LungCancer` |
| **패널 표시명** | 사용자에게 보여질 이름 (한글/영문) | 폐암 패널 검사 |
| **검사 대상 유전자** | 이 패널에서 분석할 유전자 목록 | EGFR, ALK, ROS1, KRAS 등 |
| **보고서 형식** | 보고서에 포함될 정보 및 레이아웃 | 변이 목록, 해석, 치료 권고사항 |
| **해석 기준** | 변이 분류 기준 (ACMG 가이드라인 등) | 5-tier 분류 또는 커스텀 |
| **필수 입력 필드** | 해석 시 필수로 입력해야 할 정보 | summary, interpretation, variants |
| **선택 입력 필드** | 추가로 입력 가능한 정보 | clinical_history, family_history |

#### 업무 프로세스 확인

```
1. 샘플 접수
   ↓
2. 시퀀싱 완료
   ↓
3. 변이 분석 (Elasticsearch에서 조회)
   ↓
4. [새 패널] 해석 화면에서 변이 검토 및 분류
   ↓
5. [새 패널] 해석 결과 저장 (Cassandra)
   ↓
6. [새 패널] 보고서 생성 (PDF)
   ↓
7. 결과 발행
```

### 2. 개발 환경 준비

```bash
# JDK 21 설정 (UI 모듈용)
export JAVA_HOME=/usr/lib/jvm/java-21-openjdk

# 프로젝트 클론 및 의존성 설치
cd /home/user/panel-project-claude
gradle clean build

# 로컬 인프라 실행
# PostgreSQL, Cassandra, Elasticsearch 필요
```

### 3. 참고 자료 준비

- **기존 패널 소스 코드**: `GenomeScreen` 또는 `BloodCancer`를 참고
- **ACMG 가이드라인**: 변이 분류 기준
- **의학 지식**: 해당 질환에 대한 도메인 지식

---

## 단계별 구현 가이드

### 📌 Step 1: 데이터 모델 정의 (data 모듈)

#### 1.1 DTO 클래스 생성

**위치**: `/data/src/main/java/com/gcgenome/lims/dto/interpretation/`

**예시**: `LungCancer.java`

```java
package com.gcgenome.lims.dto.interpretation;

import jsinterop.annotations.*;
import jsinterop.base.Js;
import jsinterop.base.JsPropertyMap;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 폐암 패널 검사 해석 데이터 모델
 */
@JsType(isNative=true, namespace= JsPackage.GLOBAL, name="Object")
@Setter(onMethod_={@JsOverlay, @JsIgnore})
@Getter(onMethod_={@JsOverlay, @JsIgnore})
@Accessors(fluent=true)
public final class LungCancer {
    // === 필수 필드 ===

    /**
     * 해석 요약
     */
    private String summary;

    /**
     * 상세 해석
     */
    private String interpretation;

    /**
     * 검출된 변이 목록
     */
    private Variant[] variants;

    /**
     * 평균 깊이 (Mean Depth)
     */
    @JsProperty(name="mean_depth")
    private String meanDepth;

    /**
     * 커버리지
     */
    private String coverage;

    // === 선택 필드 (폐암 특화) ===

    /**
     * 임상 이력
     */
    @JsProperty(name="clinical_history")
    private String clinicalHistory;

    /**
     * 흡연 이력
     */
    @JsProperty(name="smoking_history")
    private String smokingHistory;

    /**
     * 조직 타입 (선암, 편평상피암 등)
     */
    @JsProperty(name="histology_type")
    private String histologyType;

    /**
     * 병기 (Stage)
     */
    private String stage;

    /**
     * 치료 권고사항
     */
    @JsProperty(name="treatment_recommendation")
    private String treatmentRecommendation;

    // === 동적 프로퍼티 접근 메서드 ===

    @JsOverlay
    @JsIgnore
    public LungCancer put(String key, String value) {
        JsPropertyMap<Object> map = Js.asPropertyMap(this);
        map.set(key, value);
        return this;
    }

    @JsOverlay
    @JsIgnore
    public String get(String key) {
        JsPropertyMap<Object> map = Js.asPropertyMap(this);
        return (String) map.get(key);
    }

    // === 중첩 클래스: Variant ===

    @JsType(isNative=true, namespace= JsPackage.GLOBAL, name="Object")
    @Setter(onMethod_={@JsOverlay, @JsIgnore})
    @Getter(onMethod_={@JsOverlay, @JsIgnore})
    @Accessors(fluent=true)
    public static final class Variant {
        /**
         * 유전자명
         */
        private String gene;

        /**
         * HGVSc (cDNA 표기)
         */
        private String hgvsc;

        /**
         * HGVSp (단백질 표기)
         */
        private String hgvsp;

        /**
         * VAF (Variant Allele Frequency)
         */
        private Double vaf;

        /**
         * Depth (읽기 깊이)
         */
        private Integer depth;

        /**
         * ACMG 분류
         */
        @JsProperty(name="acmg_class")
        private String acmgClass;

        /**
         * Tier (1-5)
         */
        private String tier;

        /**
         * 임상적 의미
         */
        @JsProperty(name="clinical_significance")
        private String clinicalSignificance;

        /**
         * 약물 반응성 (Drug Response)
         */
        @JsProperty(name="drug_response")
        private String drugResponse;

        /**
         * 참고 문헌 (PMID)
         */
        private String[] pmids;

        /**
         * 해석 코멘트
         */
        private String comment;
    }

    // === 중첩 클래스: 치료 옵션 ===

    @JsType(isNative=true, namespace= JsPackage.GLOBAL, name="Object")
    @Setter(onMethod_={@JsOverlay, @JsIgnore})
    @Getter(onMethod_={@JsOverlay, @JsIgnore})
    @Accessors(fluent=true)
    public static final class Treatment {
        /**
         * 약물명
         */
        private String drug;

        /**
         * 적응증 레벨
         */
        private String level;

        /**
         * 근거
         */
        private String evidence;
    }
}
```

#### 1.2 GWT 모듈에 추가

**위치**: `/data/src/main/java/com/gcgenome/lims/Data.gwt.xml`

```xml
<!-- 새로운 패널 DTO 추가 -->
<source path="dto/interpretation"/>
```

> **참고**: 이미 `interpretation` 패키지가 포함되어 있다면 추가 작업 불필요

#### 1.3 빌드 및 검증

```bash
# data 모듈 빌드
gradle :data:clean :data:build

# GWT 컴파일 오류 확인
gradle :data:compileGwt

# 성공하면 다음 단계로
```

**⚠️ 주의사항**:
- GWT는 Java 8+ API를 일부 지원하지 않음
- `LocalDate`, `Optional`, Stream API 등 사용 금지
- 대신 `Date`, 기본 컬렉션 사용
- 모든 필드는 `Serializable` 호환

---

### 📌 Step 2: 백엔드 서비스 구현 (interpretation 모듈)

#### 2.1 서비스 클래스 생성

**위치**: `/interpretation/src/main/java/com/greencross/lims/service/interpretation/`

**예시**: `LungCancer.java`

```java
package com.greencross.lims.service.interpretation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.greencross.lims.dto.interpretation.InterpretationParam;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.*;

/**
 * 폐암 패널 해석 서비스
 */
@Service("lungcancer")  // ⭐ Bean 이름을 소문자로 지정 (service 식별자와 일치)
public class LungCancer extends Interpretation {

    private final ObjectMapper om;

    public LungCancer(ObjectMapper om) {
        this.om = om;
    }

    /**
     * 자동 해석 (변이 데이터를 기반으로 초안 생성)
     */
    @Override
    public Mono<Object> auto(long sampleId, InterpretationParam param) {
        return Mono.fromCallable(() -> {
            Map<String, Object> result = new HashMap<>();

            // 1. Elasticsearch에서 변이 데이터 조회
            List<Map<String, Object>> variants = fetchVariantsFromES(sampleId, param);

            // 2. 변이 필터링 (폐암 특화 유전자만)
            List<Map<String, Object>> filteredVariants = filterLungCancerVariants(variants);

            // 3. ACMG 분류 및 Tier 할당
            List<Map<String, Object>> classifiedVariants = classifyVariants(filteredVariants);

            // 4. 요약 및 해석 초안 생성
            String summary = generateSummary(classifiedVariants);
            String interpretation = generateInterpretation(classifiedVariants);

            // 5. 결과 구성
            result.put("summary", summary);
            result.put("interpretation", interpretation);
            result.put("variants", classifiedVariants);
            result.put("mean_depth", calculateMeanDepth(variants));
            result.put("coverage", calculateCoverage(variants));

            return result;
        });
    }

    /**
     * Negative 해석 (변이 없음)
     */
    @Override
    public Object negative(long sampleId) {
        Map<String, Object> result = new HashMap<>();
        result.put("summary", "폐암 관련 병원성 변이가 검출되지 않았습니다.");
        result.put("interpretation",
            "본 검사에서는 폐암과 연관된 임상적으로 유의미한 변이가 발견되지 않았습니다. " +
            "다만, 본 검사에 포함되지 않은 변이나 현재 알려지지 않은 변이가 존재할 가능성은 배제할 수 없습니다."
        );
        result.put("variants", new Object[0]);
        return result;
    }

    // === Private Helper 메서드 ===

    /**
     * Elasticsearch에서 변이 조회
     */
    private List<Map<String, Object>> fetchVariantsFromES(long sampleId, InterpretationParam param) {
        // Elasticsearch 쿼리 구현
        // POST gemini:9200/analysis-snv/_search
        // {
        //   "query": {
        //     "bool": {
        //       "must": [
        //         {"term": {"sample": sampleId}},
        //         {"terms": {"gene.refgene": ["EGFR", "ALK", "ROS1", "KRAS", ...]}}
        //       ]
        //     }
        //   }
        // }

        // 실제 구현은 ElasticsearchClient 사용
        return Collections.emptyList();
    }

    /**
     * 폐암 관련 유전자 필터링
     */
    private List<Map<String, Object>> filterLungCancerVariants(List<Map<String, Object>> variants) {
        // 폐암 특화 유전자 목록
        Set<String> lungCancerGenes = Set.of(
            "EGFR", "ALK", "ROS1", "KRAS", "BRAF", "MET", "RET",
            "ERBB2", "PIK3CA", "NRAS", "TP53", "STK11", "KEAP1"
        );

        return variants.stream()
            .filter(v -> lungCancerGenes.contains(v.get("gene")))
            .toList();
    }

    /**
     * 변이 분류 (ACMG + Tier)
     */
    private List<Map<String, Object>> classifyVariants(List<Map<String, Object>> variants) {
        // ACMG 가이드라인에 따라 분류
        // - Pathogenic (P)
        // - Likely Pathogenic (LP)
        // - Uncertain Significance (VUS)
        // - Likely Benign (LB)
        // - Benign (B)

        // Tier 분류 (1-5)
        // Tier 1: Actionable variants (치료 가능)
        // Tier 2: Diagnostic variants
        // Tier 3: VUS with clinical significance
        // Tier 4: VUS
        // Tier 5: Likely benign/benign

        for (Map<String, Object> variant : variants) {
            // ClinVar, OncoKB, COSMIC 등 데이터베이스 참조
            String acmgClass = determineACMGClass(variant);
            String tier = determineTier(variant);

            variant.put("acmg_class", acmgClass);
            variant.put("tier", tier);
        }

        return variants;
    }

    /**
     * 요약 생성
     */
    private String generateSummary(List<Map<String, Object>> variants) {
        long pathogenicCount = variants.stream()
            .filter(v -> "Pathogenic".equals(v.get("acmg_class")) ||
                         "Likely Pathogenic".equals(v.get("acmg_class")))
            .count();

        if (pathogenicCount == 0) {
            return "임상적으로 유의미한 병원성 변이가 검출되지 않았습니다.";
        }

        return String.format(
            "총 %d개의 병원성 또는 병원성 가능 변이가 검출되었습니다.",
            pathogenicCount
        );
    }

    /**
     * 상세 해석 생성
     */
    private String generateInterpretation(List<Map<String, Object>> variants) {
        StringBuilder sb = new StringBuilder();

        // Tier 1 변이 (actionable) 우선 기술
        for (Map<String, Object> variant : variants) {
            if ("1".equals(variant.get("tier"))) {
                sb.append(generateVariantInterpretation(variant));
                sb.append("\n\n");
            }
        }

        // 나머지 변이
        for (Map<String, Object> variant : variants) {
            if (!"1".equals(variant.get("tier"))) {
                sb.append(generateVariantInterpretation(variant));
                sb.append("\n\n");
            }
        }

        return sb.toString();
    }

    /**
     * 개별 변이 해석 생성
     */
    private String generateVariantInterpretation(Map<String, Object> variant) {
        String gene = (String) variant.get("gene");
        String hgvsp = (String) variant.get("hgvsp");
        String acmgClass = (String) variant.get("acmg_class");

        // OncoKB, CIViC 등에서 약물 반응성 조회
        String drugResponse = queryDrugResponse(gene, hgvsp);

        return String.format(
            "%s 유전자의 %s 변이는 %s로 분류됩니다. %s",
            gene, hgvsp, acmgClass, drugResponse
        );
    }

    private String determineACMGClass(Map<String, Object> variant) {
        // ACMG 가이드라인 기반 분류 로직
        return "VUS";
    }

    private String determineTier(Map<String, Object> variant) {
        // Tier 분류 로직
        return "4";
    }

    private String queryDrugResponse(String gene, String hgvsp) {
        // OncoKB, CIViC API 호출 또는 DB 조회
        return "";
    }

    private String calculateMeanDepth(List<Map<String, Object>> variants) {
        // depth 인덱스에서 평균 계산
        return "100x";
    }

    private String calculateCoverage(List<Map<String, Object>> variants) {
        // coverage 계산
        return "99.5%";
    }
}
```

#### 2.2 InterpretationService에 등록

**위치**: `/interpretation/src/main/java/com/greencross/lims/service/InterpretationService.java`

```java
@Service
public class InterpretationService {
    private final Map<String, Interpretation> interpretations;

    public InterpretationService(Map<String, Interpretation> interpretations) {
        this.interpretations = interpretations;
    }

    public Mono<Object> auto(long sample, String service, InterpretationParam param) {
        // service = "lungcancer" 같은 식별자
        Interpretation interpretation = interpretations.get(service.toLowerCase());
        if (interpretation == null) {
            throw new IllegalArgumentException("Unknown service: " + service);
        }
        return interpretation.auto(sample, param);
    }

    // ... 기타 메서드
}
```

> **참고**: Spring이 자동으로 `@Service("lungcancer")` Bean을 `Map<String, Interpretation>`에 주입합니다.

#### 2.3 보고서 템플릿 구현 (선택)

**위치**: `/interpretation/src/main/java/com/greencross/lims/service/report/lungcancer/`

```java
package com.greencross.lims.service.report.lungcancer;

import com.greencross.lims.dto.interpretation.LungCancer;
import org.springframework.stereotype.Component;

/**
 * 폐암 패널 보고서 템플릿
 */
@Component
public class LungCancerTemplate {

    /**
     * PDF 보고서 생성
     */
    public byte[] generatePDF(LungCancer data, Map<String, Object> sampleInfo) {
        // iText, Apache PDFBox 등 사용
        // 또는 Jasper Reports

        // 1. 헤더 (환자 정보, 샘플 정보)
        // 2. 요약
        // 3. 변이 테이블
        // 4. 상세 해석
        // 5. 참고 문헌
        // 6. 서명

        return new byte[0]; // PDF 바이트 배열 반환
    }
}
```

---

### 📌 Step 3: UI 컴포넌트 구현 (interpretation-ui 모듈)

#### 3.1 해석 화면 컴포넌트

**위치**: `/interpretation-ui/src/main/java/com/gcgenome/lims/client/interpretation/`

**예시**: `LungCancerInterpretationElement.java`

```java
package com.gcgenome.lims.client.interpretation;

import com.gcgenome.lims.api.InterpretationApi;
import com.gcgenome.lims.dto.interpretation.LungCancer;
import elemental2.dom.HTMLElement;
import org.jboss.elemento.HtmlContentBuilder;

import static org.jboss.elemento.Elements.*;

/**
 * 폐암 패널 해석 UI
 */
public class LungCancerInterpretationElement {

    private final long sampleId;
    private final String service = "lungcancer";
    private LungCancer data;

    public LungCancerInterpretationElement(long sampleId) {
        this.sampleId = sampleId;
        loadData();
    }

    /**
     * UI 렌더링
     */
    public HTMLElement render() {
        return div().css("lung-cancer-interpretation")
            .add(renderHeader())
            .add(renderSummarySection())
            .add(renderVariantsTable())
            .add(renderInterpretationSection())
            .add(renderClinicalInfoSection())
            .add(renderActionButtons())
            .element();
    }

    /**
     * 헤더
     */
    private HTMLElement renderHeader() {
        return div().css("header")
            .add(h2("폐암 패널 검사 해석"))
            .add(p("Sample ID: " + sampleId))
            .element();
    }

    /**
     * 요약 섹션
     */
    private HTMLElement renderSummarySection() {
        return section().css("summary")
            .add(h3("요약"))
            .add(textarea()
                .attr("rows", "3")
                .attr("placeholder", "요약을 입력하세요")
                .textContent(data != null ? data.summary() : "")
                .on("input", e -> {
                    if (data != null) {
                        data.summary(((HTMLTextAreaElement) e.target).value);
                    }
                })
            )
            .element();
    }

    /**
     * 변이 테이블
     */
    private HTMLElement renderVariantsTable() {
        HtmlContentBuilder<HTMLElement> table = table().css("variants-table");

        // 테이블 헤더
        table.add(thead()
            .add(tr()
                .add(th("Gene"))
                .add(th("HGVSc"))
                .add(th("HGVSp"))
                .add(th("VAF"))
                .add(th("Depth"))
                .add(th("ACMG Class"))
                .add(th("Tier"))
                .add(th("Drug Response"))
                .add(th("Actions"))
            )
        );

        // 테이블 바디
        HtmlContentBuilder<HTMLElement> tbody = tbody();
        if (data != null && data.variants() != null) {
            for (LungCancer.Variant variant : data.variants()) {
                tbody.add(renderVariantRow(variant));
            }
        }
        table.add(tbody);

        return section().css("variants")
            .add(h3("검출된 변이"))
            .add(button("자동 해석")
                .css("btn-auto")
                .on("click", e -> autoInterpret())
            )
            .add(table)
            .element();
    }

    /**
     * 변이 행 렌더링
     */
    private HTMLElement renderVariantRow(LungCancer.Variant variant) {
        return tr()
            .add(td(variant.gene()))
            .add(td(variant.hgvsc()))
            .add(td(variant.hgvsp()))
            .add(td(String.format("%.2f%%", variant.vaf() * 100)))
            .add(td(String.valueOf(variant.depth())))
            .add(td()
                .add(select()
                    .add(option("Pathogenic"))
                    .add(option("Likely Pathogenic"))
                    .add(option("VUS"))
                    .add(option("Likely Benign"))
                    .add(option("Benign"))
                    .attr("value", variant.acmgClass())
                    .on("change", e -> {
                        variant.acmgClass(((HTMLSelectElement) e.target).value);
                    })
                )
            )
            .add(td()
                .add(select()
                    .add(option("1"))
                    .add(option("2"))
                    .add(option("3"))
                    .add(option("4"))
                    .add(option("5"))
                    .attr("value", variant.tier())
                    .on("change", e -> {
                        variant.tier(((HTMLSelectElement) e.target).value);
                    })
                )
            )
            .add(td(variant.drugResponse() != null ? variant.drugResponse() : "-"))
            .add(td()
                .add(button("삭제")
                    .css("btn-danger")
                    .on("click", e -> removeVariant(variant))
                )
            )
            .element();
    }

    /**
     * 상세 해석 섹션
     */
    private HTMLElement renderInterpretationSection() {
        return section().css("interpretation")
            .add(h3("상세 해석"))
            .add(textarea()
                .attr("rows", "10")
                .attr("placeholder", "상세 해석을 입력하세요")
                .textContent(data != null ? data.interpretation() : "")
                .on("input", e -> {
                    if (data != null) {
                        data.interpretation(((HTMLTextAreaElement) e.target).value);
                    }
                })
            )
            .element();
    }

    /**
     * 임상 정보 섹션 (폐암 특화)
     */
    private HTMLElement renderClinicalInfoSection() {
        return section().css("clinical-info")
            .add(h3("임상 정보"))
            .add(div().css("form-group")
                .add(label("흡연 이력"))
                .add(select()
                    .add(option("비흡연자"))
                    .add(option("과거 흡연자"))
                    .add(option("현재 흡연자"))
                    .attr("value", data != null ? data.smokingHistory() : "")
                    .on("change", e -> {
                        if (data != null) {
                            data.smokingHistory(((HTMLSelectElement) e.target).value);
                        }
                    })
                )
            )
            .add(div().css("form-group")
                .add(label("조직 타입"))
                .add(select()
                    .add(option("선암 (Adenocarcinoma)"))
                    .add(option("편평상피암 (Squamous Cell Carcinoma)"))
                    .add(option("소세포폐암 (Small Cell Lung Cancer)"))
                    .add(option("기타"))
                    .attr("value", data != null ? data.histologyType() : "")
                    .on("change", e -> {
                        if (data != null) {
                            data.histologyType(((HTMLSelectElement) e.target).value);
                        }
                    })
                )
            )
            .add(div().css("form-group")
                .add(label("병기 (Stage)"))
                .add(input("text")
                    .attr("placeholder", "예: Stage IIB")
                    .attr("value", data != null ? data.stage() : "")
                    .on("input", e -> {
                        if (data != null) {
                            data.stage(((HTMLInputElement) e.target).value);
                        }
                    })
                )
            )
            .element();
    }

    /**
     * 액션 버튼
     */
    private HTMLElement renderActionButtons() {
        return div().css("actions")
            .add(button("저장")
                .css("btn-primary")
                .on("click", e -> save())
            )
            .add(button("Negative 처리")
                .css("btn-warning")
                .on("click", e -> setNegative())
            )
            .add(button("보고서 미리보기")
                .css("btn-info")
                .on("click", e -> previewReport())
            )
            .element();
    }

    // === 이벤트 핸들러 ===

    /**
     * 데이터 로드
     */
    private void loadData() {
        InterpretationApi.get(sampleId, service, result -> {
            if (result != null) {
                this.data = (LungCancer) result;
                // UI 업데이트
            } else {
                this.data = new LungCancer();
            }
        });
    }

    /**
     * 자동 해석
     */
    private void autoInterpret() {
        InterpretationApi.autoInterpret(sampleId, service, null, result -> {
            this.data = (LungCancer) result;
            // UI 리렌더링
        });
    }

    /**
     * 저장
     */
    private void save() {
        InterpretationApi.save(sampleId, service, data, () -> {
            // 성공 메시지
            alert("저장되었습니다.");
        });
    }

    /**
     * Negative 처리
     */
    private void setNegative() {
        InterpretationApi.negative(sampleId, service, result -> {
            this.data = (LungCancer) result;
            // UI 업데이트
        });
    }

    /**
     * 보고서 미리보기
     */
    private void previewReport() {
        // 새 창에서 PDF 열기
        window.open("/api/samples/" + sampleId + "/services/" + service + "/report/preview", "_blank");
    }

    /**
     * 변이 제거
     */
    private void removeVariant(LungCancer.Variant variant) {
        if (data != null && data.variants() != null) {
            LungCancer.Variant[] filtered = Arrays.stream(data.variants())
                .filter(v -> v != variant)
                .toArray(LungCancer.Variant[]::new);
            data.variants(filtered);
            // UI 리렌더링
        }
    }
}
```

#### 3.2 API 클라이언트 (선택)

**위치**: `/interpretation-ui/src/main/java/com/gcgenome/lims/api/InterpretationApi.java`

> 이미 공통 API 클라이언트가 있다면 재사용 가능

```java
package com.gcgenome.lims.api;

import elemental2.core.Function;

/**
 * Interpretation API 클라이언트
 */
public class InterpretationApi {

    public static void get(long sampleId, String service, Function callback) {
        String url = "/api/samples/" + sampleId + "/services/" + service + "/interpretation";
        JsonUtils.get(url, callback);
    }

    public static void save(long sampleId, String service, Object data, Runnable callback) {
        String url = "/api/samples/" + sampleId + "/services/" + service + "/interpretation";
        JsonUtils.put(url, data, callback);
    }

    public static void autoInterpret(long sampleId, String service, Object param, Function callback) {
        String url = "/api/samples/" + sampleId + "/services/" + service + "/auto-interpret";
        JsonUtils.post(url, param, callback);
    }

    public static void negative(long sampleId, String service, Function callback) {
        String url = "/api/samples/" + sampleId + "/services/" + service + "/negative-interpret";
        JsonUtils.get(url, callback);
    }
}
```

#### 3.3 메인 UI에 통합

**위치**: `/interpretation-ui/src/main/java/com/gcgenome/lims/client/Main.java`

```java
// 라우팅 추가
if (service.equals("lungcancer")) {
    body.add(new LungCancerInterpretationElement(sampleId).render());
}
```

---

### 📌 Step 4: 데이터베이스 스키마 (선택)

#### 4.1 PostgreSQL 테이블 (메타데이터)

```sql
-- 해석 정보 테이블 (이미 존재하면 생략)
CREATE TABLE IF NOT EXISTS interpretation (
    id BIGSERIAL PRIMARY KEY,
    sample_id BIGINT NOT NULL,
    service VARCHAR(50) NOT NULL,
    data JSONB NOT NULL,  -- 실제 해석 데이터 (JSON)
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW(),
    created_by VARCHAR(100),
    UNIQUE(sample_id, service)
);

-- 인덱스
CREATE INDEX idx_interpretation_sample ON interpretation(sample_id);
CREATE INDEX idx_interpretation_service ON interpretation(service);
```

#### 4.2 Cassandra 문서 저장 (선택)

```cql
-- Cassandra 키스페이스 및 테이블
CREATE KEYSPACE IF NOT EXISTS lims
WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 3};

USE lims;

CREATE TABLE IF NOT EXISTS interpretation_document (
    sample_id BIGINT,
    service TEXT,
    version INT,
    data TEXT,  -- JSON 문자열
    created_at TIMESTAMP,
    PRIMARY KEY ((sample_id, service), version)
) WITH CLUSTERING ORDER BY (version DESC);
```

#### 4.3 Elasticsearch 매핑 (필요시)

새로운 패널에 특화된 검색이 필요한 경우:

```json
{
  "mappings": {
    "properties": {
      "sample_id": {"type": "long"},
      "service": {"type": "keyword"},
      "gene": {"type": "keyword"},
      "smoking_history": {"type": "keyword"},
      "histology_type": {"type": "keyword"},
      "stage": {"type": "keyword"},
      "drug_response": {
        "type": "text",
        "fields": {
          "keyword": {"type": "keyword"}
        }
      }
    }
  }
}
```

---

### 📌 Step 5: 서비스 등록 및 라우팅

#### 5.1 서비스 정보 제공 엔드포인트

**위치**: `/interpretation/src/main/java/com/greencross/lims/service/InterpretationController.java`

```java
@GetMapping("/services")
public Page[] services() {
    return new Page[]{
        Page.builder()
            .title("폐암 패널 해석")
            .path("/interpretation/lungcancer")
            .icon("lungs")
            .order(10)
            .build()
    };
}
```

> Gateway가 `/services` 엔드포인트를 호출하여 메뉴에 자동 추가

#### 5.2 Zookeeper 서비스 디스커버리

**위치**: `/interpretation/src/main/resources/application.yml`

```yaml
spring:
  application:
    name: interpretation-service  # ⭐ Zookeeper에 등록될 이름
  cloud:
    zookeeper:
      connect-string: test:12181
      discovery:
        enabled: true
        root: /panel-services
```

> 서비스 시작 시 자동으로 Zookeeper에 등록되어 Gateway가 발견

---

## 테스트 및 검증

### ✅ 1. 단위 테스트

#### 백엔드 서비스 테스트

**위치**: `/interpretation/src/test/java/com/greencross/lims/service/interpretation/`

```java
package com.greencross.lims.service.interpretation;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class LungCancerTest {

    @Autowired
    private LungCancer lungCancerService;

    @Test
    void testAutoInterpretation() {
        // Given
        long sampleId = 12345L;
        InterpretationParam param = new InterpretationParam();

        // When
        Object result = lungCancerService.auto(sampleId, param).block();

        // Then
        assertThat(result).isNotNull();
        assertThat(result).isInstanceOf(Map.class);

        Map<String, Object> data = (Map<String, Object>) result;
        assertThat(data).containsKeys("summary", "interpretation", "variants");
    }

    @Test
    void testNegativeInterpretation() {
        // Given
        long sampleId = 12345L;

        // When
        Object result = lungCancerService.negative(sampleId);

        // Then
        assertThat(result).isNotNull();
        Map<String, Object> data = (Map<String, Object>) result;
        assertThat(data.get("summary")).asString().contains("검출되지 않았습니다");
    }
}
```

### ✅ 2. 통합 테스트

```bash
# 전체 빌드
gradle clean build

# 특정 모듈 테스트
gradle :data:test
gradle :interpretation:test
gradle :interpretation-ui:test
```

### ✅ 3. 로컬 환경 테스트

#### 백엔드 실행

```bash
# Interpretation 서비스 실행
cd interpretation
gradle bootRun

# 로그 확인
tail -f logs/interpretation.log
```

#### UI 개발 모드

```bash
# GWT SuperDev Mode
cd interpretation-ui
gradle gwtDev

# 브라우저에서 접속
# http://localhost:9570
```

#### API 테스트 (curl)

```bash
# 자동 해석
curl -X POST http://localhost:8080/api/samples/12345/services/lungcancer/auto-interpret \
  -H "Content-Type: application/json" \
  -d '{"genes": ["EGFR", "ALK"]}'

# 저장
curl -X PUT http://localhost:8080/api/samples/12345/services/lungcancer/interpretation \
  -H "Content-Type: application/json" \
  -d '{"summary": "테스트", "interpretation": "테스트"}'

# 조회
curl http://localhost:8080/api/samples/12345/services/lungcancer/interpretation
```

### ✅ 4. E2E 테스트

**시나리오**:
1. 샘플 선택
2. 폐암 패널 해석 화면 진입
3. "자동 해석" 버튼 클릭
4. 변이 목록 확인 및 분류 수정
5. 상세 해석 작성
6. 임상 정보 입력
7. 저장
8. 보고서 미리보기

---

## 배포

### 🚀 1. 빌드

```bash
# 전체 빌드
gradle clean build

# 또는 개별 모듈
gradle :data:build
gradle :interpretation:build
gradle :interpretation-ui:build
```

### 🚀 2. Jenkins 파이프라인

**위치**: `/Jenkinsfile`

Jenkins 파이프라인에 새 스테이지 추가 (필요시):

```groovy
stage('Interpretation Build') {
    tools {
        jdk 'java-17'
        gradle 'gradle-8.9'
    }
    steps {
        sh 'gradle clean :interpretation:bootJar'
        buildAndDeploy('interpretation', 'panel-service-interpretation')
    }
}

stage('Interpretation-UI Build') {
    tools {
        jdk 'java-21'
        gradle 'gradle-8.9'
    }
    steps {
        sh 'gradle :interpretation-ui:copyWebResources'
        buildAndDeployFE('interpretation-ui')
    }
}
```

> **참고**: interpretation 모듈은 이미 파이프라인에 있으므로 별도 추가 불필요

### 🚀 3. Docker 이미지 빌드

```bash
# Dockerfile 예시 (interpretation 모듈)
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY build/libs/panel-service-interpretation.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

```bash
# 이미지 빌드
docker build -t panel-service-interpretation:latest ./interpretation

# ECR 푸시
aws ecr get-login-password --region ap-northeast-2 | \
    docker login --username AWS --password-stdin ${ECR_REGISTRY}
docker tag panel-service-interpretation:latest ${ECR_REGISTRY}/panel-service-interpretation:latest
docker push ${ECR_REGISTRY}/panel-service-interpretation:latest
```

### 🚀 4. 배포 순서

```
1. data 모듈 빌드 및 배포
   ↓
2. interpretation 서비스 빌드 및 배포
   ↓
3. interpretation-ui 빌드
   ↓
4. gateway에 UI 리소스 복사 (copyWebResources)
   ↓
5. gateway 재배포
   ↓
6. Zookeeper에서 서비스 확인
   ↓
7. 동작 검증
```

### 🚀 5. 배포 후 검증

```bash
# 1. Zookeeper에서 서비스 등록 확인
echo stat | nc test 12181
echo ls /panel-services | nc test 12181

# 2. Gateway /services 엔드포인트 확인
curl http://gateway:8080/services

# 3. 새 패널 API 확인
curl http://gateway:8080/api/samples/12345/services/lungcancer/interpretation

# 4. UI 접속
# http://gateway:8080/interpretation/lungcancer
```

---

## 체크리스트

### 📋 구현 체크리스트

#### 데이터 모델
- [ ] `data/src/main/java/.../dto/interpretation/LungCancer.java` 생성
- [ ] GWT 호환성 확인 (`@JsType`, `Serializable`)
- [ ] 필수 필드 정의 (summary, interpretation, variants)
- [ ] 패널 특화 필드 정의
- [ ] `Data.gwt.xml`에 패키지 포함 확인
- [ ] `gradle :data:build` 성공

#### 백엔드 서비스
- [ ] `interpretation/.../service/interpretation/LungCancer.java` 생성
- [ ] `@Service("lungcancer")` 어노테이션 추가
- [ ] `auto()` 메서드 구현 (자동 해석)
- [ ] `negative()` 메서드 구현 (음성 처리)
- [ ] Elasticsearch 쿼리 구현
- [ ] ACMG 분류 로직 구현
- [ ] Tier 분류 로직 구현
- [ ] 약물 반응성 조회 구현
- [ ] 보고서 템플릿 구현 (선택)
- [ ] 단위 테스트 작성
- [ ] `gradle :interpretation:build` 성공

#### UI 컴포넌트
- [ ] `interpretation-ui/.../client/interpretation/LungCancerInterpretationElement.java` 생성
- [ ] 해석 화면 레이아웃 구현
- [ ] 변이 테이블 구현
- [ ] 자동 해석 버튼 연동
- [ ] 저장 기능 구현
- [ ] Negative 처리 기능 구현
- [ ] 패널 특화 입력 필드 추가
- [ ] API 클라이언트 연동
- [ ] CSS 스타일링
- [ ] `gradle :interpretation-ui:build` 성공

#### 인프라
- [ ] PostgreSQL 테이블 생성 (필요시)
- [ ] Cassandra 테이블 생성 (필요시)
- [ ] Elasticsearch 매핑 정의 (필요시)
- [ ] Zookeeper 서비스 등록 확인
- [ ] Gateway 라우팅 확인

#### 테스트
- [ ] 로컬 환경 백엔드 실행 확인
- [ ] 로컬 환경 UI 개발 모드 확인
- [ ] API 엔드포인트 테스트 (curl)
- [ ] E2E 시나리오 테스트
- [ ] 보고서 생성 테스트
- [ ] 성능 테스트 (대량 변이 처리)

#### 배포
- [ ] Jenkins 파이프라인 수정 (필요시)
- [ ] Docker 이미지 빌드
- [ ] ECR 푸시
- [ ] Test 환경 배포
- [ ] Test 환경 검증
- [ ] Production 배포 계획 수립
- [ ] Production 배포
- [ ] Production 검증

#### 문서화
- [ ] API 문서 작성
- [ ] 사용자 가이드 작성
- [ ] 코드 주석 작성
- [ ] README 업데이트
- [ ] 변경 이력 기록

---

## 운영 고려사항

### 🔧 1. 성능 최적화

#### Elasticsearch 쿼리 최적화
```json
{
  "query": {
    "bool": {
      "must": [
        {"term": {"sample": 12345}},
        {"terms": {"gene.refgene": ["EGFR", "ALK", "ROS1"]}}
      ]
    }
  },
  "_source": ["gene", "hgvsc", "hgvsp", "vaf", "depth", "clinvar"],
  "size": 1000
}
```

- 필요한 필드만 `_source`에 명시
- 적절한 `size` 설정 (기본 10)
- 인덱스 샤드 수 최적화 (현재 64)

#### 캐싱 전략
```java
@Cacheable(value = "variants", key = "#sampleId")
public List<Variant> fetchVariants(long sampleId) {
    // Elasticsearch 쿼리
}
```

- Redis 또는 Caffeine 캐시 사용
- TTL 설정 (예: 1시간)

### 🔧 2. 확장성

#### 비동기 처리
```java
@Async
public CompletableFuture<Object> autoAsync(long sampleId) {
    return CompletableFuture.supplyAsync(() -> auto(sampleId));
}
```

#### 배치 처리
```java
// 다수 샘플 일괄 해석
public void batchInterpret(List<Long> sampleIds) {
    sampleIds.parallelStream()
        .forEach(this::auto);
}
```

### 🔧 3. 모니터링

#### 로깅
```java
@Slf4j
public class LungCancer extends Interpretation {
    @Override
    public Mono<Object> auto(long sampleId, InterpretationParam param) {
        log.info("Starting auto-interpretation for sample: {}", sampleId);

        return Mono.fromCallable(() -> {
            try {
                Object result = performInterpretation(sampleId, param);
                log.info("Auto-interpretation completed for sample: {}", sampleId);
                return result;
            } catch (Exception e) {
                log.error("Auto-interpretation failed for sample: {}", sampleId, e);
                throw e;
            }
        });
    }
}
```

#### 메트릭
```java
@Timed(value = "interpretation.auto", description = "Auto-interpretation time")
public Mono<Object> auto(long sampleId, InterpretationParam param) {
    // ...
}
```

Spring Boot Actuator로 메트릭 수집:
```yaml
management:
  endpoints:
    web:
      exposure:
        include: health,metrics,prometheus
  metrics:
    export:
      prometheus:
        enabled: true
```

### 🔧 4. 보안

#### 권한 확인
```java
@PreAuthorize("hasRole('INTERPRETER') or hasRole('ADMIN')")
public void create(long sample, String service, Map<String, Object> json) {
    // ...
}
```

#### 감사 로그
```java
@AfterReturning("execution(* create(..))")
public void logCreate(JoinPoint joinPoint) {
    log.info("Interpretation created by user: {}, sample: {}",
        SecurityContextHolder.getContext().getAuthentication().getName(),
        joinPoint.getArgs()[0]
    );
}
```

### 🔧 5. 에러 처리

#### 전역 예외 핸들러
```java
@RestControllerAdvice
public class InterpretationExceptionHandler {

    @ExceptionHandler(VariantNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleVariantNotFound(VariantNotFoundException e) {
        return new ErrorResponse("VARIANT_NOT_FOUND", e.getMessage());
    }

    @ExceptionHandler(InvalidInterpretationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleInvalidInterpretation(InvalidInterpretationException e) {
        return new ErrorResponse("INVALID_INTERPRETATION", e.getMessage());
    }
}
```

#### Retry 로직
```java
@Retryable(
    value = {ElasticsearchException.class},
    maxAttempts = 3,
    backoff = @Backoff(delay = 2000)
)
public List<Variant> fetchVariants(long sampleId) {
    // Elasticsearch 쿼리
}
```

---

## 참고 자료

### 기존 패널 참고 코드

| 패널 | DTO | Service | UI |
|------|-----|---------|-----|
| GenomeScreen | `data/.../dto/interpretation/GenomeScreen.java` | `interpretation/.../service/interpretation/GenomeScreen.java` | `interpretation-ui/.../client/interpretation/SnvGenomeScreenTableElement.java` |
| BloodCancer | `data/.../dto/interpretation/BloodCancer.java` | `interpretation/.../service/interpretation/BloodCancer.java` | - |
| SolidTumor2 | `data/.../dto/interpretation/SolidTumor2.java` | - | - |

### 외부 데이터베이스

- **ClinVar**: https://www.ncbi.nlm.nih.gov/clinvar/
- **HGMD**: http://www.hgmd.cf.ac.uk/
- **OncoKB**: https://www.oncokb.org/
- **CIViC**: https://civicdb.org/
- **COSMIC**: https://cancer.sanger.ac.uk/cosmic

### ACMG 가이드라인

- Richards et al. (2015) - Standards and guidelines for the interpretation of sequence variants
- https://www.acmg.net/

---

**문서 버전**: 1.0
**최종 수정일**: 2025-11-20
**작성자**: Claude AI

이 가이드는 실제 프로젝트 구조를 분석하여 작성되었으며, 신규 패널 추가 시 참고 문서로 활용할 수 있습니다.
