plugins {
    kotlin("jvm")
    kotlin("kapt")
    id("org.springframework.boot") version "3.1.4"
    id("io.spring.dependency-management") version "1.0.12.RELEASE"
    id("org.jetbrains.kotlin.plugin.spring") version "2.0.0"
}
dependencies {
    implementation(project(":data"))
    implementation(kotlin("reflect"))
    implementation(kotlin("stdlib"))
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.retry:spring-retry")
    implementation("org.springframework.cloud:spring-cloud-starter-zookeeper-discovery")
    implementation("org.springframework.boot:spring-boot-starter-log4j2")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.cloud:spring-cloud-starter-stream-kafka")
    runtimeOnly("org.postgresql:postgresql")
    runtimeOnly("com.microsoft.sqlserver:mssql-jdbc:12.2.0.jre11")
    implementation("org.springframework.boot:spring-boot-starter-data-cassandra")
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    implementation("org.apache.pdfbox:pdfbox:2.+")
    implementation("org.jsoup:jsoup:1.+")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
    implementation("com.gcgenome:jandi-webhook:1.0.2")
    implementation("com.gcgenome:workflow-publisher:2025.3.19")
}
kapt {
    keepJavacAnnotationProcessors = true
}
dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:2022.0.2")
    }
}
configurations {
    all {
        exclude(group = "org.springframework.boot", module = "spring-boot-starter-logging")
    }
}
tasks.processResources {
    if (project.gradle.startParameter.taskNames.contains("build")) exclude("application.yml")
}
val isLegacy: Boolean = System.getProperty("isLegacy")?.toBoolean() ?: false
tasks {
    bootJar {
        if (isLegacy) {
            archiveFileName.set("panel-service-publish-legacy.jar")
        } else {
            archiveFileName.set("panel-service-publish.jar")
        }
    }
}
tasks.getByName<Jar>("jar") {
    enabled = false
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