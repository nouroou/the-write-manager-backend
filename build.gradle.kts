import io.ktor.plugin.*

val exposed_version = "0.45.0"
val logback_version = "1.5.13"
val postgresql_version = "42.7.2"
val hikari_version = "5.0.1"
val supabase_version = "2.4.1"

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktor)
}

group = "com.thewritebrothers"
version = "0.0.1"

application {
    mainClass = "io.ktor.server.netty.EngineMain"

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.serialization.gson)
    implementation(libs.ktor.server.netty)
    implementation(libs.logback.classic)
    testImplementation(libs.ktor.server.test.host)
    testImplementation(libs.kotlin.test.junit)




    implementation ("com.stripe:stripe-java:20.126.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.2")
    // Ktor server
    implementation("io.ktor:ktor-server-core-jvm:2.3.7")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:2.3.7")
    implementation("io.ktor:ktor-server-auth:$KTOR_VERSION")
    implementation("io.ktor:ktor-server-auth-jwt:$KTOR_VERSION")
    implementation("io.ktor:ktor-server-cors:$KTOR_VERSION")
    // Supabase
    implementation("io.github.jan-tennert.supabase:gotrue-kt:$supabase_version")
    implementation(platform("io.github.jan-tennert.supabase:bom:$KTOR_VERSION"))
    implementation("io.github.jan-tennert.supabase:postgrest-kt:$supabase_version")
    implementation("io.github.jan-tennert.supabase:auth-kt:$supabase_version")
    implementation("io.github.jan-tennert.supabase:realtime-kt:$supabase_version")
    // Ktor engine
    implementation("io.ktor:ktor-client-java:$KTOR_VERSION")
    implementation("io.ktor:ktor-client-core:$KTOR_VERSION")
    implementation("io.ktor:ktor-utils:$KTOR_VERSION")

    //Database
    implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-dao:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-java-time:$exposed_version")
    implementation("org.postgresql:postgresql:$postgresql_version")
    implementation("com.zaxxer:HikariCP:$hikari_version")
    //Stripe
    implementation("com.stripe:stripe-java:29.0.0")
    //Env
    implementation("io.github.cdimascio:dotenv-kotlin:6.5.1")
    //Injection
    implementation("io.insert-koin:koin-ktor:4.0.4")
    implementation("io.insert-koin:koin-logger-slf4j:4.0.4")
    //Logging
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("io.ktor:ktor-server-call-logging:$KTOR_VERSION")


}
