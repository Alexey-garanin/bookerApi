plugins {
    kotlin("jvm") version "2.3.0"
    id("io.qameta.allure") version "3.0.1"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

sourceSets {
    main {
        kotlin.srcDir("src/main/kotlin")
    }
}

dependencies {
    // REST Assured
    implementation("io.rest-assured:rest-assured:5.5.0")
    implementation("io.rest-assured:kotlin-extensions:5.5.0")

    // JUnit 5
    implementation("org.junit.jupiter:junit-jupiter:5.10.2")
    implementation(kotlin("test"))

    // Allure
    implementation("io.qameta.allure:allure-junit5:2.25.0")
    implementation("io.qameta.allure:allure-rest-assured:2.25.0")

    // Logs
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.17.2")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.17.2")
    implementation("org.assertj:assertj-core:3.27.7")
    implementation("org.slf4j:slf4j-api:2.0.9")
    implementation("ch.qos.logback:logback-classic:1.4.14")
}

kotlin {
    jvmToolchain(25)
}

tasks.test {
    useJUnitPlatform()
}
