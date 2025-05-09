plugins {
	jacoco
	id("task-list-java-conventions")
	id("org.springframework.boot") version "3.4.5"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "net.earelin"
version = "0.0.1-SNAPSHOT"

val caffeineVersion: String by extra
val logstashLogbackEncoderVersion: String by extra
val lombokMapstructVersion: String by extra
val mapstructSpringExtensionsVersion: String by extra
val mapstructVersion: String by extra
val restAssuredVersion: String by extra

sourceSets {
	create("integrationTest") {
		compileClasspath += sourceSets.main.get().output
		runtimeClasspath += sourceSets.main.get().output
	}
}

configurations["integrationTestRuntimeOnly"].extendsFrom(configurations.testRuntimeOnly.get())
configurations["integrationTestImplementation"].extendsFrom(configurations.testImplementation.get())

dependencies {
	annotationProcessor("org.mapstruct.extensions.spring:mapstruct-spring-extensions:${mapstructSpringExtensionsVersion}")
	annotationProcessor("org.mapstruct:mapstruct-processor:${mapstructVersion}")
	annotationProcessor("org.projectlombok:lombok-mapstruct-binding:${lombokMapstructVersion}")

	developmentOnly("org.springframework.boot:spring-boot-devtools")

	implementation("com.github.ben-manes.caffeine:caffeine:${caffeineVersion}")
	implementation("io.micrometer:micrometer-registry-prometheus")
	implementation("io.micrometer:micrometer-tracing-bridge-otel")
	implementation("io.opentelemetry:opentelemetry-exporter-otlp")
	implementation("org.mapstruct:mapstruct:${mapstructVersion}")
	implementation("org.mapstruct.extensions.spring:mapstruct-spring-annotations:${mapstructSpringExtensionsVersion}")
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-cache")
	implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-web")

	runtimeOnly("ch.qos.logback:logback-classic:1.5.18")
	runtimeOnly("net.logstash.logback:logstash-logback-encoder:${logstashLogbackEncoderVersion}")

	testImplementation("org.springframework.boot:spring-boot-starter-test")

	"integrationTestImplementation"("io.rest-assured:rest-assured:${restAssuredVersion}")
	"integrationTestImplementation"("org.springframework.security:spring-security-test")

	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.register("integrationTest", Test::class) {
	description = "Runs integration tests."
	group = "verification"

	testClassesDirs = sourceSets["integrationTest"].output.classesDirs
	classpath = sourceSets["integrationTest"].runtimeClasspath
}

tasks.jacocoTestReport {
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
