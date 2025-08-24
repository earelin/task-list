plugins {
    jacoco
    id("task-list-java-conventions")
    id("org.springframework.boot") version "3.5.1"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "net.earelin"
version = "0.0.1-SNAPSHOT"

val caffeineVersion: String by extra
val logbackClassicVersion: String by extra
val logstashLogbackEncoderVersion: String by extra
val mapstructSpringExtensionsVersion: String by extra
val mapstructVersion: String by extra
val restAssuredVersion: String by extra
val springCloudGcpVersion: String by extra

sourceSets {
    create("integrationTest") {
        compileClasspath += sourceSets.main.get().output
        runtimeClasspath += sourceSets.main.get().output
    }
}

dependencyManagement {
    imports {
        mavenBom("com.google.cloud:spring-cloud-gcp-dependencies:${springCloudGcpVersion}")
    }
}

configurations["integrationTestRuntimeOnly"].extendsFrom(configurations.testRuntimeOnly.get())
configurations["integrationTestImplementation"].extendsFrom(configurations.testImplementation.get())

dependencies {
    annotationProcessor("org.mapstruct.extensions.spring:mapstruct-spring-extensions:${mapstructSpringExtensionsVersion}")
    annotationProcessor("org.mapstruct:mapstruct-processor:${mapstructVersion}")
    implementation("org.mapstruct:mapstruct:${mapstructVersion}")
    implementation("org.mapstruct.extensions.spring:mapstruct-spring-annotations:${mapstructSpringExtensionsVersion}")


    implementation("org.springframework.boot:spring-boot-starter-cache")
    implementation("com.github.ben-manes.caffeine:caffeine:${caffeineVersion}")

    implementation("io.micrometer:micrometer-registry-prometheus")
    implementation("io.micrometer:micrometer-tracing-bridge-otel")
    implementation("io.opentelemetry:opentelemetry-exporter-otlp")


    implementation("com.google.cloud:spring-cloud-gcp-starter-data-firestore")

    developmentOnly("org.springframework.boot:spring-boot-devtools")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-jetty")
    modules {
        module("org.springframework.boot:spring-boot-starter-tomcat") {
            replacedBy("org.springframework.boot:spring-boot-starter-jetty")
        }
    }

    runtimeOnly("ch.qos.logback:logback-classic:${logbackClassicVersion}")
    runtimeOnly("net.logstash.logback:logstash-logback-encoder:${logstashLogbackEncoderVersion}")

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    "integrationTestImplementation"("io.rest-assured:rest-assured:${restAssuredVersion}")
    "integrationTestImplementation"("io.rest-assured:spring-mock-mvc:${restAssuredVersion}")

    "integrationTestImplementation"("org.springframework.security:spring-security-test")
}

tasks.register("integrationTest", Test::class) {
    description = "Runs integration tests."
    group = "verification"

    testClassesDirs = sourceSets["integrationTest"].output.classesDirs
    classpath = sourceSets["integrationTest"].runtimeClasspath
}

tasks.jacocoTestReport {
    executionData(tasks.test.get(), tasks.named("integrationTest").get())
    reports {
        xml.required.set(true)
        html.required.set(true)
    }
}

tasks.getByName<Jar>("jar") {
    enabled = false
}

tasks.withType<Test> {
    finalizedBy(tasks.jacocoTestReport)
    useJUnitPlatform()
}
