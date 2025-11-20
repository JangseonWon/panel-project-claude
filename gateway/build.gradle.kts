plugins {
    kotlin("jvm")
    kotlin("kapt")
    id("org.springframework.boot") version "2.6.1"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
}
dependencies {
    implementation(kotlin("reflect"))
    implementation(kotlin("stdlib-jdk8"))
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    implementation("com.greencross:lims-api-gateway-data:1.0")
    implementation("org.springframework.cloud:spring-cloud-starter-gateway")
    implementation("org.springframework.cloud:spring-cloud-starter-zookeeper-discovery")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
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
    duplicatesStrategy = DuplicatesStrategy.WARN
    dependsOn(":worklist-ui:build")
    from(zipTree("../worklist-ui/build/libs/panel-service-worklist-ui.war")) {
        include("**/*.js")
        include("**/*.css")
        include("**/*.png")
        include("**/*.gif")
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
    dependsOn(":dna-ui:build")
    from(zipTree("../dna-ui/build/libs/panel-service-dna-ui.war")) {
        include("**/*.js")
        include("**/*.css")
        include("**/*.png")
        include("**/*.gif")
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
    dependsOn(":library-ui:build")
    from(zipTree("../library-ui/build/libs/panel-service-library-ui.war")) {
        include("**/*.js")
        include("**/*.css")
        include("**/*.png")
        include("**/*.gif")
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
    dependsOn(":sequencing-ui:build")
    from(zipTree("../sequencing-ui/build/libs/panel-service-sequencing.war")) {
        include("**/*.js")
        include("**/*.css")
        include("**/*.png")
        include("**/*.gif")
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
        archiveFileName.set("panel-service-gateway.jar")
    }
}
tasks.getByName<Jar>("jar") {
    enabled = false
}