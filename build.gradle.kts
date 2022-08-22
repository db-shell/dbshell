import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.util.Properties
import org.apache.commons.io.FileUtils

plugins {
    kotlin("jvm") version "1.7.10"
    id("org.jetbrains.kotlin.plugin.spring") version "1.7.10"
    // Apply the application plugin to add support for building a CLI application.
    id("java-library")
    id ("com.github.johnrengelman.shadow").version( "7.1.2")
    id ("distribution")
    id("net.researchgate.release").version("3.0.0")
    application
    java
}

repositories {
    mavenCentral()
    maven {
        url = uri("https://raw.github.com/bulldog2011/bulldog-repo/master/repo/releases/")
    }
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

        FileUtils.copyFile(source, dest)
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
            from("src/deploy/bin/data") {
                into("data")
            }
            from("src/deploy/queue/data") {
                into("queue/data")
            }
        }
    }
}

//Sample gradle CLI: gradle release -Prelease.useAutomaticVersion=true
release {
    var failOnCommitNeeded = true
    var failOnPublishNeeded = true
    var failOnSnapshotDependencies = true
    var failOnUnversionedFiles = true
    var failOnUpdateNeeded = true
    var revertOnFail = true
    var preCommitText = ""
    var preTagCommitMessage = "[Gradle Release Plugin] - pre tag commit: "
    var tagCommitMessage = "[Gradle Release Plugin] - creating tag: "
    var newVersionCommitMessage = "[Gradle Release Plugin] - new version commit: "
    var version = "$version"
    var versionPropertyFile = "version.properties"
}

val log4jVersion = "2.18.0"
val springVersion = "2.1.1"
val jacksonVersion = "2.13.3"
val springBootVersion = "2.7.3"

dependencies {

    api("org.bradfordmiller", "simplejndiutils", "0.0.14") {
        isTransitive = true
    }
    implementation("us.fatehi:schemacrawler:16.16.18")
    implementation("org.springframework.shell", "spring-shell-starter", springVersion, classifier="sources")
    implementation("org.springframework.boot", "spring-boot-starter", springBootVersion)
    implementation("org.springframework.boot", "spring-boot-starter-test", springBootVersion)
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.apache.logging.log4j",  "log4j-core",  log4jVersion)
    implementation("org.apache.logging.log4j",  "log4j-api",  log4jVersion)
    implementation("org.apache.logging.log4j", "log4j-slf4j-impl", log4jVersion)
    implementation("org.postgresql", "postgresql", "42.4.0")
    implementation("org.xerial:sqlite-jdbc:3.36.0.3")
    implementation("org.jooq", "jooq", "3.17.2")
    implementation("com.github.mnadeem", "sql-table-name-parser", "0.0.5")
    implementation("org.mybatis", "mybatis", "3.5.10")
    implementation("net.sourceforge.csvjdbc:csvjdbc:1.0.40")
    implementation("com.fasterxml.jackson.core", "jackson-core", jacksonVersion)
    implementation("com.fasterxml.jackson.core", "jackson-databind", jacksonVersion)
    implementation("com.fasterxml.jackson.module", "jackson-module-kotlin", jacksonVersion)
    implementation("com.leansoft", "bigqueue", "0.7.0")
    implementation("io.vavr", "vavr-jackson", "0.10.3")
    implementation("io.vavr", "vavr", "0.10.4")
    implementation("com.opencsv:opencsv:5.6")
    implementation("com.amazon.deequ:deequ:2.0.1-spark-3.2")
    api("org.bradfordmiller:sqlutils:0.0.4")

    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:1.7.10")
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

tasks.withType<ShadowJar> {
    isZip64 = true
}

application {
    // Define the main class for the application.
    mainClassName = "org.dbshell.DriverKt"

    group = "org.dbshell"
    version = getSoftwareVersion()
}