pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins { id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0" }

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "imageproc-kotlin"

val imageKotlinLocal = file("../image-kotlin")
if (imageKotlinLocal.exists()) {
    includeBuild(imageKotlinLocal) {
        dependencySubstitution {
            substitute(module("io.github.kotlinmania:image-kotlin"))
                .using(project(":"))
        }
    }
}
