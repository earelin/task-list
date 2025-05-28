import com.github.spotbugs.snom.Confidence
import com.github.spotbugs.snom.Effort
import com.github.spotbugs.snom.SpotBugsTask

plugins {
    java
    checkstyle
    id("com.github.spotbugs")
}

val bugPatternVersion: String by extra
val checkstyleVersion: String by extra
val sbContribVersion: String by extra
val findSecBugsPluginVersion: String by extra
val lombokVersion: String by extra
val spotbugsVersion: String by extra

repositories {
    mavenCentral()
}

spotbugs {
    ignoreFailures = true
    effort = Effort.MAX
    reportLevel = Confidence.LOW
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)

    }
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

dependencies {
    checkstyle("com.puppycrawl.tools:checkstyle:${checkstyleVersion}") {
        exclude("com.google.guava:guava")
    }

    annotationProcessor("org.projectlombok:lombok:${lombokVersion}")

    compileOnly("org.projectlombok:lombok:${lombokVersion}")

    spotbugs("com.github.spotbugs:spotbugs:${spotbugsVersion}")
    spotbugsPlugins("com.h3xstream.findsecbugs:findsecbugs-plugin:${findSecBugsPluginVersion}")
    spotbugsPlugins("com.mebigfatguy.sb-contrib:sb-contrib:${sbContribVersion}")
    spotbugsPlugins("jp.skypencil.findbugs.slf4j:bug-pattern:${bugPatternVersion}@jar")

    testAnnotationProcessor("org.projectlombok:lombok:${lombokVersion}")
    testCompileOnly("org.projectlombok:lombok:${lombokVersion}")
}

tasks.withType<SpotBugsTask>().configureEach {
    reports {
        create("html")
        create("xml")
    }
}

tasks.withType<Checkstyle>().configureEach {
    reports {
        xml.required.set(false)
        html.required.set(true)
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.compilerArgs.add("-Xlint")
}

