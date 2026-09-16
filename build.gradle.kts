plugins {
    kotlin("jvm") version "2.3.0"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))

    // REST Assured — нужен в main И в test
    implementation("io.rest-assured:rest-assured:5.5.0")
    implementation("io.rest-assured:kotlin-extensions:5.5.0")
    testImplementation("io.rest-assured:rest-assured:5.5.0")

    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")

    // Логирование
    implementation("org.slf4j:slf4j-api:2.0.9")
    implementation("ch.qos.logback:logback-classic:1.4.14")

    // Jackson для маппинга JSON
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.16.1")

    // Основные библиотеки JUnit 5
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.2")

    // Опционально: для поддержки параметризованных тестов
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.10.2")
}

kotlin {
    jvmToolchain(24)
}

tasks.test {
    useJUnitPlatform()
}