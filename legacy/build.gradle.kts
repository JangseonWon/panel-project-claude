plugins {
    kotlin("jvm")
    id("org.springframework.boot") version "2.6.2"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
}

group = "com.greencross"
version = "1.0"

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    implementation(project(":data"))
    implementation("com.gcgenome:sample-data:2025.08.25-1")
    implementation("com.gcgenome:sample-panel:1.0")
    implementation("com.gcgenome:gateway-api:1.0")
    implementation("com.gcgenome:gateway-service:1.0")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.retry:spring-retry")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.postgresql:postgresql")
    implementation("org.springframework.retry:spring-retry")
    implementation("org.springframework.cloud:spring-cloud-starter-zookeeper-discovery")
    implementation("org.springframework.boot:spring-boot-starter-log4j2")
    implementation("org.springframework.boot:spring-boot-starter-actuator")

    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    implementation("org.springframework.retry:spring-retry")
    implementation("com.google.jsinterop:jsinterop-annotations:2.0.0")
    implementation("org.jsoup:jsoup:1.13.1")
    implementation("org.apache.poi:poi-ooxml:4.1.2")
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
tasks.processResources {
    if(project.gradle.startParameter.taskNames.contains("build")) {
        exclude("application.properties")
        exclude("bootstrap.properties")
    }
}
tasks.bootJar {
    archiveFileName.set("panel-service-legacy.jar")
}
tasks. withType<Delete> { doFirst { delete("build/") } }
