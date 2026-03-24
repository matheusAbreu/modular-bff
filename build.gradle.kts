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

plugins {
    kotlin("plugin.allopen") version "2.2.10"
    kotlin("jvm") version "2.2.0"
    id("io.quarkus") version "3.32.4"
}


dependencies {
    implementation("$quarkusPluginId:quarkus-smallrye-health")
    implementation("$quarkusPluginId:quarkus-smallrye-openapi")
    implementation(enforcedPlatform("$quarkusPlatformGroupId:$quarkusPlatformArtifactId:$quarkusPlatformVersion"))
    implementation("$quarkusPluginId:quarkus-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("$quarkusPluginId:quarkus-rest")
    implementation("$quarkusPluginId:quarkus-kotlin")
    implementation("$quarkusPluginId:quarkus-arc")
    testImplementation("$quarkusPluginId:quarkus-junit")
    testImplementation("io.rest-assured:rest-assured")
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
