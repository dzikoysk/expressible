import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.10"
}

description = "Expressible | Extensions for Kotlin"

dependencies {
    api(project(":expressible"))
    implementation(kotlin("stdlib-jdk8"))
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = "1.8"
        languageVersion = "1.7"
        freeCompilerArgs = listOf(
            "-Xjvm-default=all", // For generating default methods in interfaces
            // "-Xcontext-receivers"
        )
    }
}