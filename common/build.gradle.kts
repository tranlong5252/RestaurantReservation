plugins {
    id("java")
}

group = "tranlong5252"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation(project(mapOf("path" to ":common")))
}

tasks.test {
    useJUnitPlatform()
}