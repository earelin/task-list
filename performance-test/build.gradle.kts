plugins {
	id("task-list-java-conventions")
	id("io.gatling.gradle") version "3.13.5.4"
}

val commonsLangVersion: String by extra

dependencies {
	gatlingImplementation("net.datafaker:datafaker:2.4.2")
	gatlingImplementation("org.apache.commons:commons-lang3:${commonsLangVersion}")
}
