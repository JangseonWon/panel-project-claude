plugins {
    kotlin("jvm")
    id("org.wisepersist.gwt") version "1.1.19"
    id("java")
    id("war")
}
group = "com.gcgenome"
version = "1.0"
dependencies {
    implementation(project(":data"))
    implementation("com.gcgenome:gateway-api:1.0")
    implementation("com.gcgenome:gateway-service:1.0")
    implementation("com.gcgenome:sample-data:2025.08.25-1")
    implementation("com.gcgenome:sample-panel:1.0")
    implementation("com.gcgenome:lims-icon:2.1")
    implementation("org.jboss.elemento:elemento-core:1.4.2")
    implementation("com.google.elemental2:elemental2-svg:1.2.0")
    implementation("org.gwtproject:gwt-user:2.12.1")
    compileOnly("org.gwtproject:gwt-dev:2.12.1")
    implementation("net.sayaya:ui:4.1")
    implementation("net.sayaya:chart:2.1")
    implementation("org.projectlombok:lombok:1.18.32")
    annotationProcessor("org.projectlombok:lombok:1.18.32")
}
val lombok = project.configurations.annotationProcessor.get().filter { it.name.startsWith("lombok") }.single()
tasks {
    withType<Delete> {
        doFirst {
            delete("build/")
        }
    }
    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }
    withType<War> {
        archiveFileName.set("panel-service-worklist.war")
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }
    gwt {
        gwt.modules = listOf("com.gcgenome.lims.Worklist")
        minHeapSize = "1024M"
        maxHeapSize = "2048M"
        sourceLevel = "auto"
        extraJvmArgs = listOf("-javaagent:${lombok}=ECJ")
        gwt.jsInteropExports.setGenerate(true)
        compiler.apply {
            strict = true
            disableClassMetadata = true
            disableCastChecking = true
        }
    }
    gwtDev {
        extraJvmArgs = listOf("-XX:ReservedCodeCacheSize=512M","-javaagent:${lombok}=ECJ")
        port = 9570
        codeServerPort = 9571
        war = file("src/main/webapp")
    }
    register<Copy>("copyWebResources") {
        dependsOn(build)
        from(zipTree("build/libs/panel-service-worklist.war")) {
            include("**/*.js")
            include("**/*.css")
            include("*.html")
            includeEmptyDirs = false
        }
        into("build/static")
    }
    getByName<Test>("test") {
        useJUnitPlatform()
    }
}
