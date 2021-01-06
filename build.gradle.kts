import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.util.Properties
import org.apache.commons.io.FileUtils

plugins {
    kotlin("jvm") version "1.4.10"
    id("org.jetbrains.kotlin.plugin.spring") version "1.4.21"
    // Apply the application plugin to add support for building a CLI application.
    id("java-library")
    id ("com.github.johnrengelman.shadow").version( "5.1.0")
    id ("distribution")
    id("net.researchgate.release").version("2.6.0")
    application
    java
}

repositories {
    mavenCentral()
}

fun getSoftwareVersion(): String {
    val props = Properties()
    return File("$projectDir/version.properties").inputStream().use { inputStream ->
        props.load(inputStream)
        props.get("version")!!.toString()
    }
}

tasks.create("set-defaults") {
    doFirst {
        val softwareVersion = getSoftwareVersion()

        println("Software Version: " + softwareVersion)

        group = "org.dbshell"
        version = softwareVersion

        val source = File("version.properties")
        val dest = File("src/deploy/lib/conf/version.properties")

        org.apache.commons.io.FileUtils.copyFile(source, dest)
    }
    doLast {
        println("Current software version is $version")
    }
}

tasks.build {
    dependsOn("set-defaults")
}

tasks.named<CreateStartScripts>("startScripts") {
    classpath = files(classpath) + files("conf")
    //This is a HACK....as seen here:  https://discuss.gradle.org/t/classpath-in-application-plugin-is-building-always-relative-to-app-home-lib-directory/2012
    doLast {
        var windowsScriptFile = FileUtils.readFileToString(windowsScript, "UTF-8")
        var unixScriptFile = FileUtils.readFileToString(unixScript, "UTF-8")
        val windowsScriptContent = windowsScriptFile.replace("%APP_HOME%\\lib\\conf", "%APP_HOME%\\conf")
        val unixScriptContent = unixScriptFile.replace("\$APP_HOME/lib/conf", "\$APP_HOME/conf")
        FileUtils.writeStringToFile(windowsScript, windowsScriptContent, "UTF-8", false)
        FileUtils.writeStringToFile(unixScript, unixScriptContent, "UTF-8", false)
    }

}

distributions {
    getByName("main") {
        contents {
            from("src/deploy/bin/conf/jndi") {
                into("conf/jndi")
            }
            from("src/deploy/lib/conf") {
                into("conf")
            }
        }
    }
}

//Sample gradle CLI: gradle release -Prelease.useAutomaticVersion=true
release {
    failOnCommitNeeded = true
    failOnPublishNeeded = true
    failOnSnapshotDependencies = true
    failOnUnversionedFiles = true
    failOnUpdateNeeded = true
    revertOnFail = true
    preCommitText = ""
    preTagCommitMessage = "[Gradle Release Plugin] - pre tag commit: "
    tagCommitMessage = "[Gradle Release Plugin] - creating tag: "
    newVersionCommitMessage = "[Gradle Release Plugin] - new version commit: "
    version = "$version"
    versionPropertyFile = "version.properties"
}

val log4jVersion = "2.14.0"
val springVersion = "2.0.1.RELEASE"

dependencies {

    api("org.bradfordmiller", "simplejndiutils", "0.0.13") {
        isTransitive = true
    }
    implementation("us.fatehi:schemacrawler:16.11.6")
    implementation("org.springframework.shell", "spring-shell-starter", springVersion)
    implementation("org.springframework.boot", "spring-boot-starter", "2.4.1")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.apache.logging.log4j",  "log4j-core",  log4jVersion)
    implementation("org.apache.logging.log4j",  "log4j-api",  log4jVersion)
    implementation("org.apache.logging.log4j", "log4j-slf4j-impl", log4jVersion)
    api("org.bradfordmiller:sqlutils:0.0.4")

    testImplementation(kotlin("test-junit"))
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

configurations {
    implementation {
        exclude(group = "org.springframework.boot", module = "spring-boot-starter-logging")
        exclude(group = "ch.qos.logback'", module = "logback-classic")
        exclude(group = "org.apache.logging.log4j", module = "log4j-to-slf4j")
        exclude(group = "org.springframework.boot", module = "spring-boot-devtools")
    }
}

tasks.test {
    useJUnit()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.withType<Jar> {
    manifest {
        attributes["Implementation-Title"] = "db-shell"
        attributes["Implementation-Version"] = getSoftwareVersion()
    }
}

application {
    // Define the main class for the application.
    mainClassName = "org.dbshell.DriverKt"

    group = "org.dbshell"
    version = getSoftwareVersion()
}