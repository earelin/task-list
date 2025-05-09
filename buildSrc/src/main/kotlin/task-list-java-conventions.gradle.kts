import gradle.kotlin.dsl.accessors._b5d2fb2a34bca56a40c462d03b0fc4a3.checkstyle

plugins {
    java
    checkstyle
}

val checkstyleVersion: String by extra
val lombokVersion: String by extra

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    checkstyle("com.puppycrawl.tools:checkstyle:${checkstyleVersion}") {
        exclude("com.google.guava:guava")
    }

    annotationProcessor("org.projectlombok:lombok:${lombokVersion}")
    compileOnly("org.projectlombok:lombok:${lombokVersion}")
    testAnnotationProcessor("org.projectlombok:lombok:${lombokVersion}")
    testCompileOnly("org.projectlombok:lombok:${lombokVersion}")
}

checkstyle {
    toolVersion = checkstyleVersion
}

tasks.withType<JavaCompile>().configureEach {
    options.compilerArgs.add("-Xlint")
}
