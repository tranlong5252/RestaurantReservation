plugins {
    id("java")
}

group = "tranlong5252"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    //jdbc-mysql
    implementation("mysql:mysql-connector-java:8.0.26")

}
