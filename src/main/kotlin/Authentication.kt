package com.thewritebrothers

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.config.*
import io.ktor.server.response.*

fun Application.configureAuthentication() {
    install(Authentication) {
        jwt("auth-jwt") {
            verifier (
                JWT
                    .require(Algorithm.HMAC256(ApplicationConfig("application.conf").property("jwt.secret").getString()))
                    .withIssuer("client-manager-backend")
                    .build()
            )
            validate { credential ->
                if (credential.payload.getClaim("sub") != null && credential.payload.getClaim("role") != null) {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }
            challenge { _, _ -> call.respond(HttpStatusCode.Unauthorized) }
        }
    }
}