import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.9.25"
    id("org.jetbrains.kotlin.plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.4.5"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.openapi.generator") version "7.5.0"
    id("nu.studer.jooq") version "9.0"
    id("org.flywaydb.flyway") version "10.14.0"
}

group = "com.ecommerce"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
    sourceCompatibility = JavaVersion.VERSION_21
}

repositories {
    mavenCentral()
}

val MAPSTRUCT_VERSION = "1.5.5.Final"
val TESTCONTAINERS_VERSION = "1.19.3"
val KOTLIN_LOGGING_VERSION = "3.0.5"

// Database configuration
val dbUrl: String by project
val dbUser: String by project
val dbPassword: String by project

dependencies {
    // Spring Boot Starters
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-jooq")
    
    // Database
    implementation("org.postgresql:postgresql")
    implementation("org.flywaydb:flyway-core")
    
    // Redis
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("io.lettuce:lettuce-core:6.2.6.RELEASE")
    
    // Kotlin
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("io.github.microutils:kotlin-logging-jvm:$KOTLIN_LOGGING_VERSION")
    
    // OpenAPI
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.3.0")
    
    // JOOQ
    jooqGenerator("org.postgresql:postgresql")
    
    // Test Dependencies
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
    testImplementation("org.testcontainers:postgresql:$TESTCONTAINERS_VERSION")
    testImplementation("org.testcontainers:junit-jupiter:$TESTCONTAINERS_VERSION")
    testImplementation("org.mockito.kotlin:mockito-kotlin:4.1.0")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

// Flyway Configuration
flyway {
    url = dbUrl
    user = dbUser
    password = dbPassword
    schemas = arrayOf("public")
    locations = arrayOf("classpath:db/migration")
    baselineOnMigrate = true
    cleanDisabled = false
}

// JOOQ Configuration
jooq {
    version.set("3.19.3")
    edition = nu.studer.gradle.jooq.JooqEdition.OSS
    
    configurations {
        create("main") {
            jooqConfiguration.apply {
                logging = org.jooq.meta.jaxb.Logging.WARN
                jdbc.apply {
                    driver = "org.postgresql.Driver"
                    url = dbUrl
                    user = dbUser
                    password = dbPassword
                }
                generator.apply {
                    name = "org.jooq.codegen.DefaultGenerator"
                    database.apply {
                        name = "org.jooq.meta.postgres.PostgresDatabase"
                        inputSchema = "public"
                        excludes = "flyway_schema_history"
                        includes = ".*"
                        recordVersionFields = "version"
                        recordTimestampFields = "created_at|updated_at"
                    }
                    generate.apply {
                        isDeprecated = false
                        isRecords = true
                        isImmutablePojos = true
                        isFluentSetters = true
                    }
                    target.apply {
                        packageName = "com.ecommerce.infrastructure.jooq"
                        directory = "build/generated/jooq"
                    }
                    strategy.name = "org.jooq.codegen.DefaultGeneratorStrategy"
                }
            }
        }
    }
}

// OpenAPI Generator Configuration
openApiGenerate {
    generatorName.set("kotlin-spring")
    inputSpec.set("$rootDir/src/main/resources/openapi/api.yaml")
    outputDir.set("$buildDir/generated/openapi")
    apiPackage.set("${project.group}.generated.api")
    modelPackage.set("${project.group}.generated.model")
    configOptions.set(
        mapOf(
            "interfaceOnly" to "true",
            "useSpringBoot3" to "true",
            "useBeanValidation" to "true",
            "documentationProvider" to "none",
            "serializationLibrary" to "jackson",
            "useTags" to "true",
            "enumPropertyNaming" to "UPPERCASE"
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

// Add generated sources to the source sets
sourceSets.main {
    kotlin {
        srcDirs(
            "$buildDir/generated/jooq",
            "$buildDir/generated/openapi/src/main/kotlin"
        )
    }
}

// Ensure code generation tasks run before compilation
tasks.named("compileKotlin") {
    dependsOn("openApiGenerate")
    dependsOn("generateJooq")
}

// Kotlin compiler options
tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "21"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}

// Clean up generated code
tasks.clean {
    delete("$buildDir/generated")
}
