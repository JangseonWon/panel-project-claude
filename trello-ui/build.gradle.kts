plugins {
    kotlin("jvm")
    id("org.wisepersist.gwt") version "1.1.15"
    id("java")
    id("war")
}
java.sourceCompatibility = JavaVersion.VERSION_17
java.targetCompatibility = JavaVersion.VERSION_17
dependencies {
    implementation(project(":data"))
    implementation("com.greencross:lims-api-gateway-data:1.0")
    implementation("com.greencross:lims-service:1.1")
    implementation("com.greencross:lims-sheet:1.1")
    implementation("com.greencross:lims-service-util:1.0")
    implementation("com.greencross:lims-icon:1.1")
    implementation("org.jboss.elemento:elemento-core:1.0.3")
    implementation("com.google.elemental2:elemental2-svg:1.1.0")
    implementation("com.google.gwt:gwt-user:2.9.0")
    implementation("com.google.gwt:gwt-dev:2.9.0")
    implementation("net.sayaya:ui:3.1")
    implementation("net.sayaya:calculator:1.0")
    implementation("org.projectlombok:lombok:1.18.22")
    annotationProcessor("org.projectlombok:lombok:1.18.22")
}
val lombok = project.configurations.annotationProcessor.get().filter { it.name.startsWith("lombok") }.single()
tasks {
    gwt {
        gwt.modules = listOf("com.greencross.lims.Trello")
        minHeapSize = "1024M"
        maxHeapSize = "2048M"
    }
    compileGwt {
        extraJvmArgs = listOf("-XX:ReservedCodeCacheSize=512M","-javaagent:${lombok}=ECJ")
    }
    gwtDev {
        extraJvmArgs = listOf("-XX:ReservedCodeCacheSize=512M","-javaagent:${lombok}=ECJ")
        port = 9620
        codeServerPort = 9621
        war = file("src/main/webapp")
    }
}
tasks.war {
    archiveFileName.set("panel-service-trello.war")
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}
tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}
tasks.withType<Delete> {
    doFirst {
        delete("build/")
    }
}