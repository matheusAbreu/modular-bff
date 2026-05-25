import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

repositories {
    mavenCentral()
    gradlePluginPortal()
    mavenLocal()
}
val quarkusPluginId: String by project
val quarkusPluginVersion: String by project
val quarkusPlatformGroupId: String by project
val quarkusPlatformArtifactId: String by project
val quarkusPlatformVersion: String by project
val mockkVersion = "1.14.9"
val quarkusJunitMockVersion = "3.1.0"

plugins {
    kotlin("plugin.allopen") version "2.3.21"
    kotlin("jvm") version "2.3.21"
    id("io.quarkus") version "3.36.0"
    id("org.jlleitschuh.gradle.ktlint") version "14.2.0"
    id("io.gitlab.arturbosch.detekt") version "1.23.8"
}


dependencies {
    implementation(enforcedPlatform("$quarkusPlatformGroupId:$quarkusPlatformArtifactId:$quarkusPlatformVersion"))
    implementation("$quarkusPluginId:quarkus-smallrye-health")
    implementation("$quarkusPluginId:quarkus-smallrye-openapi")
    implementation("$quarkusPluginId:quarkus-kotlin")
    implementation("$quarkusPluginId:quarkus-jdbc-postgresql")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("$quarkusPluginId:quarkus-rest")
    implementation("$quarkusPluginId:quarkus-kotlin")
    implementation("$quarkusPluginId:quarkus-hibernate-orm")
    implementation("$quarkusPluginId:quarkus-arc")

    testImplementation("io.quarkus:quarkus-junit5")
    testImplementation("io.quarkus:quarkus-junit-mockito")
    testImplementation("io.quarkus:quarkus-jacoco")
    testImplementation("io.rest-assured:rest-assured")
    testImplementation("io.rest-assured:kotlin-extensions")
    testImplementation("io.mockk:mockk:${mockkVersion}")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("io.quarkiverse.mockk:quarkus-junit5-mockk:${quarkusJunitMockVersion}")
}

allOpen {
    annotation("jakarta.ws.rs.Path")
    annotation("jakarta.enterprise.context.ApplicationScoped")
    annotation("jakarta.persistence.Entity")
    annotation("io.quarkus.test.junit.QuarkusTest")
}

group = "org.acme"
version = "1.0.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

tasks {
     withType<KotlinCompile> {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
            javaParameters = true
            freeCompilerArgs.add("-Xjsr305=strict")
        }
    }
}
