import org.wisepersist.gradle.plugins.gwt.Style

plugins {
    kotlin("jvm")
    id("org.wisepersist.gwt") version "1.1.19"
    id("java")
    id("war")
}
dependencies {
    implementation(project(":data"))
    implementation("com.gcgenome:sample-data:2025.08.25-1")
    implementation("com.gcgenome:sample-panel:1.0")
    implementation("com.gcgenome:gateway-api:1.0")
    implementation("com.gcgenome:gateway-service:1.0")
    implementation("com.gcgenome:lims-icon:2.1")

    implementation("org.jboss.elemento:elemento-core:1.4.2")
    implementation("com.google.elemental2:elemental2-svg:1.2.0")
    implementation("org.gwtproject:gwt-user:2.11.0")
    compileOnly("org.gwtproject:gwt-dev:2.11.0")
    implementation("net.sayaya:ui:4.1")
    implementation("net.sayaya:chart:2.1")
    implementation("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")
}
val lombok = project.configurations.annotationProcessor.get().filter { it.name.startsWith("lombok") }.single()
tasks {
    gwt {
        gwt.modules = listOf("com.gcgenome.lims.Interpretation")
        minHeapSize = "1024M"
        maxHeapSize = "2048M"
        sourceLevel = "auto"
        extraJvmArgs = listOf("-javaagent:${lombok}=ECJ")
        gwt.jsInteropExports.setGenerate(true)
        compiler.apply {
            strict = true
            disableClassMetadata = true
            disableCastChecking = true
            style = Style.PRETTY
        }
    }
    gwtDev {
        extraJvmArgs = listOf("-XX:ReservedCodeCacheSize=512M","-javaagent:${lombok}=ECJ")
        port = 9631
        codeServerPort = 9632
        war = file("src/main/webapp")
    }
}
tasks.war {
    archiveFileName.set("panel-service-interpretation.war")
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}
tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}
tasks.register<Copy>("copyWebResources") {
    dependsOn(tasks.build)
    from(zipTree("build/libs/panel-service-interpretation.war")) {
        include("**/*.js")
        include("**/*.css")
        include("**/*.png")
        include("**/*.gif")
        include("**/*.svg")
        include("**/*.ttf")
        include("**/*.woff")
        include("**/*.woff2")
        include("**/*.eot")
        include("*.ico")
        include("*.html")
        includeEmptyDirs = false
    }
    into("build/static")
}
tasks.withType<Delete> {
    doFirst {
        delete("build/")
    }
}
