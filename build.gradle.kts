import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.2.5.RELEASE"
    id("io.spring.dependency-management") version "1.0.9.RELEASE"
    kotlin("jvm") version "1.3.71"
    kotlin("kapt") version "1.3.71"
    kotlin("plugin.spring") version "1.3.71"
    groovy
}

group = "es.e1sordo.thesis.wtiat"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8
java.targetCompatibility = JavaVersion.VERSION_1_8

val developmentOnly by configurations.creating
configurations {
    runtimeClasspath {
        extendsFrom(developmentOnly)
    }
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}
repositories {
    mavenCentral()
    maven(url = "https://maven.vaadin.com/vaadin-addons")
}

extra["springCloudVersion"] = "Hoxton.SR3"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.vaadin:vaadin-spring-boot-starter")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.springframework.cloud:spring-cloud-config-server")
    implementation("org.springframework.kafka:spring-kafka")
    implementation("org.influxdb:influxdb-java:2.8")
    implementation("com.appnexus.grafana-client:grafana-api-java-client:1.0.5")
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    implementation("org.mapstruct:mapstruct:1.3.1.Final")
    kapt("org.mapstruct:mapstruct-processor:1.3.1.Final")
    kapt("org.springframework.boot:spring-boot-configuration-processor")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
    testImplementation("de.flapdoodle.embed:de.flapdoodle.embed.mongo")
    testImplementation("org.springframework.kafka:spring-kafka-test")
    testImplementation("junit:junit:4.12")
    testImplementation("org.codehaus.groovy:groovy-all:2.4.4")
    testImplementation("org.spockframework:spock-core:1.1-groovy-2.4")
    testImplementation("org.spockframework:spock-spring:1.1-groovy-2.4")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
        mavenBom("com.vaadin:vaadin-bom:14.0.9")
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }
}
