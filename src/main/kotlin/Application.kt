package com.thewritebrothers

import com.thewritebrothers.core.di.authModule
import com.thewritebrothers.core.di.billingModule
import io.github.cdimascio.dotenv.dotenv
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.cors.routing.*
import org.koin.core.context.startKoin
import org.koin.dsl.module


fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}



fun Application.module() {

    configureSerialization()
    configureRouting()
    configureAuthentication()

    val dotenv = dotenv {
        ignoreIfMissing = true // Don't fail if .env is not present (e.g., in production using system env vars)
        directory = "resources"
        filename = ".env"
    }

    startKoin {
        modules(
            module {
                single(qualifier = org.koin.core.qualifier.named("stripe_api_key")) {
                    dotenv["STRIPE_SECRET_KEY"]
                }
                single(qualifier = org.koin.core.qualifier.named("jwt_secret")) {
                    dotenv["JWT_SECRET"]
                }
                single {
                    createSupabaseClient(
                        supabaseUrl = dotenv["SUPABASE_URL"],
                        supabaseKey = dotenv["SUPABASE_SERVICE_KEY"]
                    ) {
                        install(Postgrest)
                    }
                }
            },
            authModule(),
            billingModule(dotenv),
        )
    }

    install(CORS) {
        anyHost()
        methods.addAll(HttpMethod.DefaultMethods)
        allowHeader(HttpHeaders.Authorization)
        allowHeader(HttpHeaders.ContentType)
        allowCredentials = true
        maxAgeInSeconds = 3600
    }



}
