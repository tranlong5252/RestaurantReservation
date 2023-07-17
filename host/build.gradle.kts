group = "tranlong5252"
version = "1.0-SNAPSHOT"

dependencies {
    implementation(project(mapOf("path" to ":common")))
	testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("mysql:mysql-connector-java:8.0.28")
}

tasks.test {
    useJUnitPlatform()
}