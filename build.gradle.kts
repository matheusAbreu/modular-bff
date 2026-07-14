import io.gitlab.arturbosch.detekt.Detekt
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jlleitschuh.gradle.ktlint.KtlintExtension

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
val mockkVersion = "1.14.11"
val quarkusJunitMockVersion = "3.1.0"
val quarkusRestVersion = "3.15.6.1"
val jacksonModuleVersion = "3.1.0"
val quarkusRestReactiveVersion = "3.15.6.1"

plugins {
    kotlin("plugin.allopen") version "2.4.10"
    kotlin("jvm") version "2.4.10"
    id("io.quarkus") version "3.37.2"
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
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonModuleVersion")
    implementation("$quarkusPluginId:quarkus-resteasy-reactive:$quarkusRestReactiveVersion")
    implementation("$quarkusPluginId:quarkus-resteasy-reactive-jackson:$quarkusRestReactiveVersion")
    implementation("$quarkusPluginId:quarkus-hibernate-orm")
    implementation("$quarkusPluginId:quarkus-arc")
    implementation("$quarkusPluginId:quarkus-rest-client-reactive:$quarkusRestVersion")
    implementation("$quarkusPluginId:quarkus-rest-client-reactive-jackson:$quarkusRestVersion")

    testImplementation("io.quarkus:quarkus-junit5")
    testImplementation("io.quarkus:quarkus-junit-mockito")
    testImplementation("io.quarkus:quarkus-jacoco")
    testImplementation("io.rest-assured:rest-assured")
    testImplementation("io.rest-assured:kotlin-extensions")
    testImplementation("io.mockk:mockk:$mockkVersion")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("io.quarkiverse.mockk:quarkus-junit5-mockk:$quarkusJunitMockVersion")
}

allOpen {
    annotation("jakarta.ws.rs.Path")
    annotation("jakarta.enterprise.context.ApplicationScoped")
    annotation("jakarta.persistence.Entity")
    annotation("io.quarkus.test.junit.QuarkusTest")
}

group = "edu.uerj"
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

    configure<KtlintExtension> {
        enableExperimentalRules.set(true)
    }

    detekt {
        config.setFrom("config/detekt/detekt.yaml")
        buildUponDefaultConfig = true
    }

    withType<Detekt>().configureEach {
        reports {
            html.required.set(true)
            xml.required.set(true)
            txt.required.set(true)
            sarif.required.set(false)
            md.required.set(false)
        }
    }
}
