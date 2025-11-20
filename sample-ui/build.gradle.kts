plugins {
    kotlin("jvm")
    id("org.wisepersist.gwt") version "1.1.19"
    id("java")
    id("war")
}

dependencies {
    implementation("com.gcgenome:sample-data:2025.08.25-1")
    implementation("com.gcgenome:sample-panel:1.0")
    implementation("com.gcgenome:gateway-api:1.0")
    implementation("com.gcgenome:gateway-service:1.0")
    implementation("com.gcgenome:lims-icon:2.1")
    implementation("net.sayaya:ui:4.1")
    implementation("net.sayaya:chart:2.1")
    implementation("org.jboss.elemento:elemento-core:1.4.2")
    implementation("com.google.elemental2:elemental2-svg:1.2.0")
    implementation("org.gwtproject:gwt-user:2.11.0")
    compileOnly("org.gwtproject:gwt-dev:2.11.0")
    implementation("org.projectlombok:lombok:1.18.32")
    annotationProcessor("org.projectlombok:lombok:1.18.32")
}
val lombok = project.configurations.annotationProcessor.get().filter { it.name.startsWith("lombok") }.single()
tasks {
    gwt {
        gwt.modules = listOf("com.greencross.lims.Sample")
        minHeapSize = "1024M"
        maxHeapSize = "2048M"
        sourceLevel = "auto"
    }
    compileGwt {
        extraJvmArgs = listOf("-XX:ReservedCodeCacheSize=512M","-javaagent:${lombok}=ECJ")
    }
    gwtDev {
        extraJvmArgs = listOf("-XX:ReservedCodeCacheSize=512M","-javaagent:${lombok}=ECJ")
        port = 9580
        codeServerPort = 9581
        war = file("src/main/webapp")
    }
}
tasks.war {
    archiveFileName.set("panel-service-sample-ui.war")
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
}
tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}
tasks.withType<Delete> {
    doFirst {
        delete("build/")
    }
}
