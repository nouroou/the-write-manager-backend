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
    val jwtSecret = ApplicationConfig("jwt.secret").toString()
    val jwtIssuer = ApplicationConfig("jwt.issuer").toString()
    val jwtAudience = ApplicationConfig("jwt.audience").toString()
    val jwtRealm = ApplicationConfig("jwt.realm").toString()
    install(Authentication) {
        jwt("auth-jwt") {
            realm = jwtRealm
            verifier (
                JWT
                    .require(Algorithm.HMAC256(jwtSecret))
                    .withAudience(jwtAudience)
                    .withIssuer(jwtIssuer)
                    .build()
            )
            validate { credential ->
                if (credential.payload.audience.contains(jwtAudience)) {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }
            challenge { _, _ -> call.respond(HttpStatusCode.Unauthorized) }
        }
    }
}