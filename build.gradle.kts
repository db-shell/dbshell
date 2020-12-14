import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.10"
    id("org.jetbrains.kotlin.plugin.spring") version "1.4.21"
    // Apply the application plugin to add support for building a CLI application.
    application
    java
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
    /*implementation("org.springframework.shell", "spring-shell-core", "2.0.1.RELEASE")
    implementation("org.springframework.shell", "spring-shell-standard", "2.0.1.RELEASE")
    implementation("org.springframework.shell", "spring-shell-table", "2.0.1.RELEASE")
    implementation("org.springframework.shell", "spring-shell-standard-commands", "2.0.1.RELEASE")*/
    implementation("org.springframework.shell", "spring-shell-starter", "2.0.1.RELEASE")
    implementation("org.springframework.boot", "spring-boot-starter", "2.4.1")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.apache.logging.log4j",  "log4j-core",  log4jVersion)
    implementation("org.apache.logging.log4j",  "log4j-api",  log4jVersion)
    implementation("org.apache.logging.log4j", "log4j-slf4j-impl", log4jVersion)

    testImplementation(kotlin("test-junit"))
}

configurations {
    implementation {
        exclude(group = "org.springframework.boot", module = "spring-boot-starter-logging")
        exclude(group = "ch.qos.logback'", module = "logback-classic")
        exclude(group = "org.apache.logging.log4j", module = "log4j-to-slf4j")
    }
}

tasks.test {
    useJUnit()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    // Define the main class for the application.
    mainClassName = "org.dbshell.DriverKt"
}