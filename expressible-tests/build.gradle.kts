description = "Expressible | Extensions for Kotlin"

plugins {
    kotlin("kapt")
}

dependencies {
    implementation(project(":expressible"))
    implementation(project(":expressible-specification"))
    kapt(project(":expressible-annotation-processor"))

    testImplementation(kotlin("stdlib-jdk8"))
    testImplementation(project(":expressible-junit"))
    testImplementation(project(":expressible-kt"))
}