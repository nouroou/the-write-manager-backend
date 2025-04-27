package com.thewritebrothers.features.billing.presentation.routes

import com.stripe.exception.SignatureVerificationException
import com.thewritebrothers.features.billing.presentation.controllers.BillingController
import com.thewritebrothers.features.billing.presentation.models.dto.BillingHistoryRequest
import com.thewritebrothers.features.billing.presentation.models.dto.CreateCheckoutRequest
import com.thewritebrothers.features.billing.presentation.models.dto.PaymentRequest
import com.thewritebrothers.features.billing.presentation.models.dto.SubscriptionPlansRequest
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.billingRouting() {
    val billingController : BillingController by inject()
    authenticate("auth-jwt") {
        route("/billing") {
            post("/create-checkout-session") {
                try {
                    val principal = call.principal<JWTPrincipal>()
                    val uid = principal?.payload?.getClaim("uid")?.asString()
                        ?: return@post call.respond(HttpStatusCode.Unauthorized, "User ID is not found")

                    val request = call.receive<CreateCheckoutRequest>()

                    val response = billingController.createCheckoutSession(uid, request)

                    call.respond(HttpStatusCode.OK, response)
                } catch (e: ContentTransformationException) {
                    call.respond(HttpStatusCode.BadRequest, "Invalid body request ${e.message}")
                } catch (e: Exception) {
                    val (status, message) = billingController.handleCheckoutException(e)
                    call.respond(status, message)
                }

            }
        }

    }

    route("/billing") {
        authenticate("auth-jwt") {
            post("/process-payment") {
                try {
                    val paymentRequest = call.receive<PaymentRequest>()
                    val result = billingController.processPayment(paymentRequest)

                    if (result.success) {
                        call.respond(HttpStatusCode.OK, result)
                    } else {
                        call.respond(HttpStatusCode.BadRequest, result)
                    }
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.InternalServerError, mapOf(e.message to "Unknown error"))
                }
            }
            get("/subscription-plans") {
                try {
                    val subscriptionPlansRequest = call.receive<SubscriptionPlansRequest>()
                    val result = billingController.getSubscriptionPlans(subscriptionPlansRequest)

                    if (result.success) {
                        call.respond(HttpStatusCode.OK, result)
                    } else {
                        call.respond(HttpStatusCode.BadRequest, result)
                    }
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.InternalServerError, mapOf(e.message to "Unknown error"))
                }

            }
            get ("/billing-history") {

                try {
                    val billingHistoryRequest = call.receive<BillingHistoryRequest>()
                    val result = billingController.getBillingHistory(billingHistoryRequest)

                    if (result.success) {
                        call.respond(HttpStatusCode.OK, result)
                    } else {
                        call.respond(HttpStatusCode.BadRequest, result)
                    }
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.InternalServerError, mapOf(e.message to "Unknown error"))
                }

            }
        }
    }
    route("/webhooks") {
        post("/stripe") {
            val payload = call.receiveText()
            val sigHeader = call.request.headers["Stripe-Signature"]

            if(sigHeader == null || sigHeader == "") {
                call.respond(HttpStatusCode.BadRequest, "Missing Stripe-Signature header")
                return@post
            }
            try {
                billingController.handleWebhook(payload, sigHeader)
                call.respond(HttpStatusCode.OK)
            } catch (e: SignatureVerificationException) {
                application.log.warn("Error occurred while creating checkout ${e.message}")
                call.respond(HttpStatusCode.BadRequest, "Signature verification failed")

            } catch (e: Exception) {
                val (status, message) = billingController.handleCheckoutException(e)
                call.respond(status, message)
            }

        }
    }



}