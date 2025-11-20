#### LIMS Panel 검사 서비스:
* 모듈
  * gateway
    * MSA 포워딩, 로드밸런싱을 담당하는 게이트웨이 서비스
  * worklist
  * worklist-ui
  * docker-compose.yml
    * Cassandra
      * 생성된 모델, 업로드한 샘플 문서를 저장하는 데이터베이스
      * 3중화 구성
    * Zookeeper
      * MSA 디스커버리 서비스
    * Kafka
  * Jenkinsfile
      * `data` 모듈에 의존하는 멀티 모듈을 일괄적으로 배포하기 위한 Jenkins 파이프라인 스크립트
      * [Jenkins] - [Jenkins 관리] - [Credentials]에 등록한 정보는 각 Credential ID를 활용하여 credentials() 메서드 인자로 넘기면 환경 변수로 할당
        ![image](https://github.com/user-attachments/assets/de17478a-636e-4d2e-94f3-98f7981b2cb1)
      * 선언된 환경변수는 실제로 다음 세 가지 환경 변수를 설정
        ``` groovy
        // Jenkinsfile (Declarative Pipeline)
        pipeline {
         environment {
               ENV  = credentials('각 Credential ID') // ENV는 임의의 환경변수명
               ENV_USR  = "${ENV_USR}"
               ENV_PSW  = "${ENV_PSW}"
         }
        }
        ```
        * ENV: 사용자 이름과 비밀번호로 구성 (구분자: 콜론, 예시: `username:password`)
        * ENV_USR: 사용자 이름 구성 요소만 포함하는 추가 변수
        * ENV_PSW: 비밀번호 구성요소만 포함하는 추가 변수
      * [Jenkinsfile 공식 문서](https://www.jenkins.io/doc/book/pipeline/jenkinsfile/)
    
