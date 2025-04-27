package com.thewritebrothers

import com.thewritebrothers.features.auth.presentation.routes.authRoutes
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        authRoutes()
//        commented out for being developed in feature/billing branch
//        billingRouting()
    }
}
