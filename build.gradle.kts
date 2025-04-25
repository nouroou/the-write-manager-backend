import io.ktor.plugin.*

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
    implementation("io.github.jan-tennert.supabase:gotrue-kt:2.4.1")
    implementation("io.github.jan-tennert.supabase:postgrest-kt:2.4.1")
    //Stripe
    implementation("com.stripe:stripe-java:29.0.0")
    //Env
    implementation("io.github.cdimascio:dotenv-kotlin:6.5.1")
    //Injection
    implementation("io.insert-koin:koin-ktor:4.0.4")
    implementation("io.insert-koin:koin-logger-slf4j:4.0.4")


}
