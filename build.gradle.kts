import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.9.25"
    id("org.jetbrains.kotlin.plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.4.5"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.openapi.generator") version "7.5.0"
}

group = "learn.ai"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // Spring Boot Starters
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-redis-reactive")
    
    // Database
    implementation("org.postgresql:r2dbc-postgresql")
    implementation("org.postgresql:postgresql")
    implementation("org.flywaydb:flyway-core")
    implementation("io.r2dbc:r2dbc-pool")
    
    // Redis
    implementation("io.lettuce:lettuce-core:6.2.6.RELEASE")
    
    // Kotlin
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    
    // OpenAPI
    implementation("org.springdoc:springdoc-openapi-starter-webflux-ui:2.3.0")
    
    // Test Dependencies
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test")
    testImplementation("io.r2dbc:r2dbc-h2")
    testImplementation("com.h2database:h2")
    testImplementation("org.testcontainers:testcontainers:1.19.7")
    testImplementation("org.testcontainers:junit-jupiter:1.19.7")
    testImplementation("org.testcontainers:postgresql:1.19.7")
    testImplementation("org.testcontainers:r2dbc:1.19.7")
    testImplementation("io.mockk:mockk:1.13.8")
    testImplementation("com.ninja-squad:springmockk:4.0.2")
    testImplementation("org.awaitility:awaitility:4.2.0")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

// OpenAPI Generator Configuration
openApiGenerate {
    generatorName.set("kotlin-spring")
    inputSpec.set("$rootDir/src/main/resources/openapi/api.yaml")
    outputDir.set("$buildDir/generated")
    apiPackage.set("${project.group}.ecommerce.generated.api")
    modelPackage.set("${project.group}.ecommerce.generated.model")
    configOptions.set(
        mapOf(
            "interfaceOnly" to "true",
            "useSpringBoot3" to "true",
            "useBeanValidation" to "true",
            "documentationProvider" to "none",
            "serializationLibrary" to "jackson",
            "useTags" to "true"
        )
    )
    globalProperties.set(
        mapOf(
            "apis" to "",
            "models" to "",
            "modelDocs" to "false"
        )
    )
}

// Add generated sources to the main source set
sourceSets.main {
    java.srcDirs("$buildDir/generated/src/main/kotlin")
}

// Ensure the openApiGenerate task runs before compilation
tasks.compileKotlin {
    dependsOn("openApiGenerate")
}

// Kotlin compiler options
tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "21"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

// Clean up generated code
tasks.clean {
    delete("$buildDir/generated")
}
