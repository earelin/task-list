plugins {
	id("task-list-java-conventions")
	id("io.gatling.gradle") version "3.14.3"
}

val commonsLangVersion: String by extra
val dataFakerVersion: String by extra

dependencies {
	gatlingImplementation("net.datafaker:datafaker:${dataFakerVersion}")
	gatlingImplementation("org.apache.commons:commons-lang3:${commonsLangVersion}")
}
