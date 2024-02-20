import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  id("org.springframework.boot") version "3.2.2"
  id("io.spring.dependency-management") version "1.1.4"
  kotlin("jvm") version "1.9.22"
  kotlin("plugin.spring") version "1.9.22"
}

group = "de.lausi"
version = "0.0.1-SNAPSHOT"

java {
  sourceCompatibility = JavaVersion.VERSION_21
}

repositories {
  mavenCentral()
}

dependencies {
  // SpringBoot dependencies
  implementation("org.springframework.boot:spring-boot-starter-web")
  implementation("org.springframework.boot:spring-boot-starter-validation")
  implementation("org.springframework.boot:spring-boot-starter-actuator")
  implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
  implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

  annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

  // Discord Bot Dependencies
  implementation("dev.kord:kord-core:0.13.1")
  implementation("com.kotlindiscord.kord.extensions:kord-extensions:1.6.0")

  // Kotlin dependencies
  implementation("org.jetbrains.kotlin:kotlin-reflect")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0-RC")

  testImplementation("org.springframework.boot:spring-boot-starter-test")
  testImplementation("com.tngtech.archunit:archunit:1.2.1")
}

tasks.withType<KotlinCompile> {
  kotlinOptions {
    freeCompilerArgs += "-Xjsr305=strict"
    jvmTarget = "21"
  }
}

tasks.withType<Test> {
  useJUnitPlatform()
}

tasks.bootJar {
  archiveFileName = "application.jar"
}
