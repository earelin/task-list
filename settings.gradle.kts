rootProject.name = "task-list"

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

include("app")
include("acceptance-test")
include("performance-test")
