plugins {
    kotlin("jvm")
    kotlin("kapt")
    id("org.springframework.boot") version "2.6.1"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
}

repositories {
    maven {
        url = uri("http://gemini/api/packages/LIMS/maven")
        isAllowInsecureProtocol = true
    }
}
dependencies {
    implementation(project(":data"))
    implementation(kotlin("reflect"))
    implementation(kotlin("stdlib-jdk8"))
    implementation("com.gcgenome:lims-report-versions-jpa:1.0.0")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    implementation("com.gcgenome:lims-report:1.0")
    implementation("org.apache.pdfbox:pdfbox:2.0.24")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.retry:spring-retry")
    implementation("org.springframework.cloud:spring-cloud-starter-zookeeper-discovery")
    implementation("org.springframework.boot:spring-boot-starter-log4j2") {
        exclude("org.apache.logging.log4j", "log4j-core")
        exclude("org.apache.logging.log4j", "log4j-slf4j-impl")
        exclude("org.apache.logging.log4j", "log4j-jul")
        exclude("org.apache.logging.log4j", "log4j-api")
    }
    implementation("org.apache.logging.log4j:log4j-core:2.17.0")
    implementation("org.apache.logging.log4j:log4j-slf4j-impl:2.17.0")
    implementation("org.apache.logging.log4j:log4j-jul:2.17.0")
    implementation("org.apache.logging.log4j:log4j-api:2.17.0")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    runtimeOnly("org.postgresql:postgresql")
    implementation("org.springframework.boot:spring-boot-starter-data-cassandra")
    implementation("org.springframework.data:spring-data-cassandra:3.2.2")
    implementation("com.datastax.oss:java-driver-core:4.12.0")
    implementation("com.datastax.oss:native-protocol:1.5.0")
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("org.springframework.boot:spring-boot-starter-data-jpa")
    testImplementation("org.testcontainers:testcontainers:1.18.1")
    testImplementation("org.testcontainers:junit-jupiter:1.18.1")
    testImplementation("org.testcontainers:postgresql:1.18.1")
    implementation("com.google.zxing:core:3+")
    implementation("com.google.zxing:javase:3+")
}
kapt {
    keepJavacAnnotationProcessors = true
}
dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:2021.0.0")
    }
}
configurations {
    all {
        exclude(group = "org.springframework.boot", module = "spring-boot-starter-logging")
    }
}
val isLegacy: Boolean = System.getProperty("isLegacy")?.toBoolean() ?: false
tasks {
    jar {
        enabled = false
    }
    test {
        useJUnitPlatform()
    }
    processResources {
        if (project.gradle.startParameter.taskNames.contains("build")) exclude("application.yml")
    }
    bootJar {
        if (isLegacy) {
            archiveFileName.set("panel-service-report-legacy.jar")
        } else {
            archiveFileName.set("panel-service-report.jar")
        }
    }
}

sourceSets {
    main {
        java {
            if (isLegacy) {
                setSrcDirs(listOf("src/main/java"))
            } else {
                setSrcDirs(listOf("src/main/java", "src/main/filter"))
            }
        }
    }
}
