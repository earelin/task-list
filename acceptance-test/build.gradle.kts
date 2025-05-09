plugins {
    id("task-list-java-conventions")
    id("se.thinkcode.cucumber-runner") version "0.0.12"
}

group = "net.earelin"
version = "0.0.1-SNAPSHOT"

val assertjVersion: String by extra
val cucumberVersion: String by extra
val restAssuredVersion: String by extra

dependencies {
    testImplementation("io.cucumber:cucumber-java:${cucumberVersion}")
    testImplementation("io.cucumber:cucumber-picocontainer:${cucumberVersion}")

    testImplementation("io.rest-assured:rest-assured:${restAssuredVersion}")

    testImplementation("org.assertj:assertj-core:${assertjVersion}")
}

cucumber {
    glue = "classpath:net.earelin.tasklist"
    plugin = arrayOf("pretty", "html:build/reports/cucumber.html")
    featurePath = "src/test/resources/features"
}
