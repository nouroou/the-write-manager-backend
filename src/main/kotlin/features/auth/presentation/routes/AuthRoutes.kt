package com.thewritebrothers.features.auth.presentation.routes

import com.thewritebrothers.features.auth.presentation.controllers.AuthController
import com.thewritebrothers.features.auth.presentation.models.dto.LoginRequest
import com.thewritebrothers.features.auth.presentation.models.dto.LogoutRequest
import com.thewritebrothers.features.auth.presentation.models.dto.SignupRequest
import com.thewritebrothers.features.auth.presentation.models.dto.SignupResponse
import io.github.jan.supabase.exceptions.SupabaseEncodingException
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Routing.authRoutes() {
    val authController: AuthController by inject()
    route("/auth") {
        post("/login") {
            try {
                val request = call.receive<LoginRequest>()
                val response = authController.logIn(request)
                call.respond(HttpStatusCode.OK, response)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.Unauthorized, e.toString())
            }
        }
        post("/signup") {
            try {
                val request = call.receive<SignupRequest>()
                val result = authController.signup(request)
                if (result.success) {

                    val responseWithBillingUrl = SignupResponse(
                        user = result.user,
                        success = true,
                        message = "Signup successful, please complete billing",
                        nextStep = "billing",
                        token = result.token,
                        billingUrl = "/billing?user_id=${result.user.id}"
                    )
                    call.respond(HttpStatusCode.Created, responseWithBillingUrl)
                } else {
                    call.respond(HttpStatusCode.BadRequest, result)
                }
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, e.toString())
            }
        }
        authenticate("auth-jwt") {
            post("/logout") {
                try {
                    val request = call.receive<LogoutRequest>()
                    val response = authController.logOut(request)
                    call.respond(HttpStatusCode.OK, response)
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.Unauthorized, e.toString())
                }
            }
        }

    }
}