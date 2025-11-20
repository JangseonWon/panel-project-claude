plugins {
    kotlin("jvm")
    kotlin("kapt")
    id("org.springframework.boot") version "2.6.2"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
}

dependencies {
    implementation(project(":data"))
    implementation(kotlin("stdlib-jdk8"))
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("com.greencross:lims-api-gateway-data:1.0")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-aop")
    implementation("org.springframework.retry:spring-retry")
    implementation("org.springframework.cloud:spring-cloud-starter-zookeeper-discovery")
    implementation("org.springframework.boot:spring-boot-starter-log4j2")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-data-elasticsearch")
    runtimeOnly("org.postgresql:postgresql")
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
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
tasks.register<Copy>("copyWebResources") {
    delete(files("src/main/resources/static"))
    dependsOn(":trello-ui:build")
    from(zipTree("../trello-ui/build/libs/panel-service-trello.war")) {
        include("**/*.js")
        include("**/*.css")
        include("**/*.png")
        include("**/*.wav")
        include("**/*.ogg")
        include("**/*.svg")
        include("**/*.ttf")
        include("**/*.woff")
        include("**/*.woff2")
        include("**/*.eot")
        include("*.html")
        includeEmptyDirs = false
    }
    into("src/main/resources/static")
}
tasks.processResources {
    dependsOn("copyWebResources")
    if(project.gradle.startParameter.taskNames.contains("build")) exclude("application.yml")
}
tasks {
    bootJar {
        archiveFileName.set("panel-service-trello.jar")
    }
}
tasks.getByName<Jar>("jar") {
    enabled = false
}