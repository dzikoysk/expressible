plugins {
    kotlin("jvm")
}

group = "com.dzikoysk"
version = "2.0.0"

dependencies {
    implementation(project(":expressible"))
    implementation(project(":expressible-specification"))
    implementation(kotlin("stdlib"))

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}