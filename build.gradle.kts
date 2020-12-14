import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.10"
}

group = "org.dbshell"
version = "0.0.1"

repositories {
    mavenCentral()
}

val log4jVersion = "2.14.0"

dependencies {

    api("org.bradfordmiller", "simplejndiutils", "0.0.12") {
        isTransitive = true
    }
    implementation("us.fatehi:schemacrawler:16.11.6")
    implementation("org.springframework.shell", "spring-shell-core", "2.0.1.RELEASE")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.apache.logging.log4j",  "log4j-core",  log4jVersion)
    implementation("org.apache.logging.log4j",  "log4j-api",  log4jVersion)
    implementation("org.apache.logging.log4j", "log4j-slf4j-impl", log4jVersion)

    testImplementation(kotlin("test-junit"))
}

tasks.test {
    useJUnit()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}