package com.thewritebrothers

import com.thewritebrothers.features.auth.presentation.routes.authRoutes
import com.thewritebrothers.features.billing.presentation.routes.billingRouting
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        authRoutes()
        billingRouting()
    }
}
