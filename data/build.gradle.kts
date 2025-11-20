plugins {
    kotlin("jvm")
    id("org.wisepersist.gwt") version "1.1.19"
    id("java")
}
dependencies {
    implementation("org.jboss.elemento:elemento-core:1.4.2")
    implementation("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    testImplementation("org.junit.platform:junit-platform-engine:2.0.0")
    testImplementation("org.junit.platform:junit-platform-launcher:2.0.0")
    testImplementation("org.junit.platform:junit-platform-runner:2.0.0")
    testImplementation("org.assertj:assertj-core:3.24.2")
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
tasks.withType<Jar> {
    from(sourceSets.main.get().allSource)
    archiveFileName.set("panel-service-data.jar")
}
tasks.withType<Delete> {
    doFirst {
        delete("build/")
    }
}
tasks.test{
    useJUnitPlatform()
}