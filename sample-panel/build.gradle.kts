plugins {
    kotlin("jvm")
    id("org.wisepersist.gwt") version "1.1.15"
    id("java")
}
dependencies {
    implementation("com.greencross:lims-api-gateway-data:1.0")
    implementation("net.sayaya:ui:3.1")
    implementation("org.jboss.elemento:elemento-core:1.0.3")
    implementation("com.google.gwt:gwt-user:2.9.0")
    implementation("com.google.gwt:gwt-dev:2.9.0")
    compileOnly("org.projectlombok:lombok:1.18.22")
    annotationProcessor("org.projectlombok:lombok:1.18.22")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
}
val lombok = project.configurations.annotationProcessor.get().filter { it.name.startsWith("lombok") }.single()
tasks {
    gwt {
        minHeapSize = "1024M"
        maxHeapSize = "2048M"
        jsInteropExports.setGenerate(true)
        compiler.apply {
            localWorkers = 12
            disableClassMetadata = true
            disableCastChecking = true
        }
    }
    compileGwt {
        extraJvmArgs = listOf("-XX:ReservedCodeCacheSize=512M","-javaagent:${lombok}=ECJ", "-generateJsInteropExport")
    }
}
tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }
}
tasks.withType<Jar> {
    from(sourceSets.main.get().allSource)
    archiveFileName.set("panel-service-sample-panel")
}
tasks.withType<Delete> {
    doFirst {
        delete("build/")
    }
}