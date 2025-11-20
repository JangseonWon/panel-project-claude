/*
  panel-project의 모든 모듈은 `data` 모듈을 의존하고 있으며,
  이 파이프라인은 주어진 파라미터에 따라 프로젝트의 다양한 모듈을 빌드하고 배포합니다.

  파라미터:
  - DEPLOY_ENV: 배포 환경을 지정 ('test' 또는 'prod')

  제외된 모듈:
  - 미사용: File, Trello, CNV, SNV
  - 특정 조건에서만 빌드: Legacy-결과전송, Legacy-보고서 생성
 */
pipeline {
    agent any

    parameters {
        choice(name: 'DEPLOY_ENV', choices: ['test', 'prod'])
    }

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

        stage('Reading Build') {
            tools {
                jdk 'java-17'
                gradle 'gradle-8.2'
            }
            steps {
                sh 'gradle clean :reading:bootJar'
                buildAndDeploy('reading', 'panel-service-reading')
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

        stage('Analysis Build') {
            tools {
                jdk 'java-17'
                gradle 'gradle-8.9'
            }
            steps {
                sh 'gradle clean :analysis:bootJar'
                buildAndDeploy('analysis', 'panel-service-analysis')
            }
        }

        stage('Publish Build') {
            tools {
                jdk 'java-21'
                gradle 'gradle-8.9'
            }
            steps {
                sh 'gradle clean :publish:bootJar'
                buildAndDeploy('publish', 'panel-service-publish')
            }
        }

        stage('Reading-UI Build') {
            tools {
                jdk 'java-21'
                gradle 'gradle-8.9'
            }
            steps {
                sh 'gradle :reading-ui:copyWebResources'
                buildAndDeployFE('reading-ui')
            }
        }

        stage('Interpretation Build') {
            tools {
                jdk 'java-17'
                gradle 'gradle-8.9'
            }
            steps {
                sh 'gradle clean :interpretation:bootJar -x test'
                buildAndDeploy('interpretation', 'panel-service-interpretation')
            }
        }

        stage('Report-UI Build') {
            tools {
                jdk 'java-21'
                gradle 'gradle-8.9'
            }
            steps {
                sh 'gradle :report-ui:copyWebResources'
                buildAndDeployFE('report-ui')
            }
        }

        stage('Report Build') {
            tools {
                jdk 'java-17'
                gradle 'gradle-8.2'
            }
            steps {
                sh 'gradle clean :report:bootJar -x test'
                buildAndDeploy('report', 'panel-service-report')
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

        stage('Request Build') {
            tools {
                jdk 'java-17'
                gradle 'gradle-8.2'
            }
            steps {
                sh 'gradle :request:clean :request:bootJar'
                buildAndDeploy('request', 'panel-service-request')
            }
        }

        stage('Request-UI Build') {
            tools {
                jdk 'java-21'
                gradle 'gradle-8.9'
            }
            steps {
                sh 'gradle :request-ui:copyWebResources'
                buildAndDeployFE('request-ui')
            }
        }

        stage('Worklist Build') {
            tools {
                jdk 'java-17'
                gradle 'gradle-8.9'
            }
            steps {
                sh 'gradle :worklist:clean :worklist:bootJar'
                buildAndDeploy('worklist', 'panel-service-worklist')
            }
        }

        stage('Worklist-UI Build') {
            tools {
                jdk 'java-21'
                gradle 'gradle-8.9'
            }
            steps {
                sh 'gradle :worklist-ui:copyWebResources'
                buildAndDeployFE('worklist-ui')
            }
        }

        stage('Variant-SNV Build') {
            tools {
                jdk 'java-17'
                gradle 'gradle-8.9'
            }
            steps {
                sh 'gradle :variant-snv:clean :variant-snv:bootJar'
                buildAndDeploy('variant-snv', 'panel-service-variant-snv')
            }
        }

        stage('Variant-SNV-UI Build') {
            tools {
                jdk 'java-21'
                gradle 'gradle-8.9'
            }
            steps {
                sh 'gradle :variant-snv-ui:copyWebResources'
                buildAndDeployFE('variant-snv-ui')
            }
        }

        stage('Legacy Build') {
            tools {
                jdk 'java-17'
                gradle 'gradle-8.2'
            }
            steps {
                sh 'gradle :legacy:clean :legacy:bootJar'
                buildAndDeploy('legacy', 'panel-service-legacy')
            }
        }

        stage('Analysis-Handler Build') {
            tools {
                jdk 'java-17'
                gradle 'gradle-8.2'
            }
            steps {
                sh 'gradle clean :analysis-crawler:clean :analysis-crawler:bootJar'
                sh 'mv analysis-crawler/build/libs/*.jar /data/lims/'
                sh 'sudo systemctl restart panel-service-analysis-handler'
            }
        }
    }
}

def deployToServer(jarPath, remotePath, service = null) {
    def transfers = [
        sshTransfer(
            sourceFiles: jarPath,
            removePrefix: jarPath.substring(0, jarPath.lastIndexOf('/') + 1),
            remoteDirectory: remotePath,
            execCommand: service ? "sudo systemctl restart ${service}" : '',
        )
    ]

    sshPublisher(
        failOnError: true,
        publishers: [
            sshPublisherDesc(
                configName: 'Aries',
                transfers: transfers
            ),
            sshPublisherDesc(
                configName: 'Taurus',
                transfers: transfers
            )
        ]
    )
}

def buildAndDeploy(module, serviceName) {
    def jarPath = "${module}/build/libs/*.jar"
    def remotePath = 'lims'
    def tempBuildPath = "/data/${remotePath}"

    if (params.DEPLOY_ENV == 'test') {
        sh "mv ${jarPath} ${tempBuildPath}"
        sh "sudo systemctl restart ${serviceName}"
    } else if (params.DEPLOY_ENV == 'prod') {
        deployToServer(jarPath, remotePath, serviceName)
    }
}

def buildAndDeployFE(module) {
    def sourcePath = "${module}/build/static/"
    def jarPath = "${sourcePath}**"
    def remotePath = 'lims/static/panel-service'
    def tempBuildPath = "/data/${remotePath}/"

    if (params.DEPLOY_ENV == 'test') {
        sh "sudo rsync -a --no-perms ${sourcePath} ${tempBuildPath}"
    } else if (params.DEPLOY_ENV == 'prod') {
        deployToServer(jarPath, remotePath)
    }
}