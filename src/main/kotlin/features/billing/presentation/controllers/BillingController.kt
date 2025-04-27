package com.thewritebrothers.features.billing.presentation.controllers

import com.stripe.exception.SignatureVerificationException
import com.stripe.exception.StripeException
import com.thewritebrothers.features.billing.domain.entities.BillingStatus
import com.thewritebrothers.features.billing.domain.usecases.*
import com.thewritebrothers.features.billing.presentation.models.dto.*
import io.ktor.http.*
import io.ktor.server.application.*

class BillingController(
    private val initiateCheckoutUseCase: InitiateCheckoutUseCase,
    private val handleWebhookUseCase: HandleWebhookUseCase,
    private val processPaymentUseCase: ProcessPaymentUseCase,
    private val getSubscriptionPlansUseCase: GetSubscriptionPlansUseCase,
    private val getBillingHistoryUseCase: GetBillingHistoryUseCase,
    private val application: Application,
) {
    //First Method
    suspend fun processPayment(paymentRequest: PaymentRequest): PaymentResponse {
        val planAmount = when (paymentRequest.planId) {
            "solo entrepreneur " -> 19.99
            "agency" -> 49.99
            "firm" -> 99.99
            else -> throw IllegalArgumentException("Invalid Plan ID")
        }

        val result = processPaymentUseCase.execute(
            userId = paymentRequest.userId,
            planId = paymentRequest.planId,
            amount = planAmount,
            paymentMethodId = paymentRequest.paymentMethodId
        )

        return if(result.isSuccess) {
            val billingRecord = result.getOrNull()!!
            PaymentResponse(
                success = billingRecord.status == BillingStatus.SUCCESSFUL,
                paymentId = billingRecord.stripePaymentId,
                billingRecordId = billingRecord.id,
                message = "Payment Processed Successfully"
            )
        } else {
            PaymentResponse(
                success = false,
                message = "Payment failed: ${result.exceptionOrNull()?.message}"
            )
        }
    }


    suspend fun getSubscriptionPlans(subscriptionPlansRequest: SubscriptionPlansRequest): SubscriptionPlansResponse {
        val result = getSubscriptionPlansUseCase.execute()


        return if (result.isSuccess) {
            val subscriptionPlansResponse = result.getOrNull()!!
            var currentPlan = ""
            for (plan in subscriptionPlansResponse) {
                if (plan.id == subscriptionPlansRequest.billingRecord?.planId) {
                    currentPlan = plan.id
                }
            }
            SubscriptionPlansResponse(
                success = true,
                message = "Available subscription plans",
                plans = subscriptionPlansResponse,
                currentPlanId = currentPlan
            )
        } else {
            SubscriptionPlansResponse(
                success = false,
                message = "Available subscription plans not available",
            )

        }
    }

    suspend fun getBillingHistory(billingHistoryRequest: BillingHistoryRequest): BillingHistoryResponse {
        val result = getBillingHistoryUseCase.execute(billingHistoryRequest.userId)

        return if (result.isSuccess) {
            val billingHistoryResponse = result.getOrNull()!!
            BillingHistoryResponse(
                message = "Billing history retrieved successfully",
                success = true,
                billingRecords = billingHistoryResponse
            )
        } else {
            BillingHistoryResponse(
                message = "Unavailable billing history",
                success = false,
            )
        }
    }

    //Second Method
    suspend fun createCheckoutSession(uid: String,request: CreateCheckoutRequest): CreateCheckoutResponse {
        val checkoutResult = initiateCheckoutUseCase.execute(
            uid = uid,
            priceId = request.priceId
        )
        return CreateCheckoutResponse(
            sessionId = checkoutResult.sessionId,
            checkoutUrl = checkoutResult.checkoutUrl,
        )
    }

    suspend fun handleWebhook(payload: String, signature: String) {
        handleWebhookUseCase.execute(payload, signature)
    }

    private fun logError(message: String, cause: Throwable? = null) {
        application.log.error(message,cause)
    }

    fun handleCheckoutException(e: Exception): Pair<HttpStatusCode,String> {
        return when(e) {
            is StripeException -> {
                logError("Stripe error during checkout creation", e)
                HttpStatusCode.InternalServerError to "Payment provider error ${e.message}"
            }
            is IllegalArgumentException -> {
                logError("Illegal argument during checkout", e)
                HttpStatusCode.BadRequest to (e.message ?: "Illegal argument")
            }
            else -> {
                logError("Failed to create checkout session", e)
                HttpStatusCode.InternalServerError to "Session creation error"
            }
        }
    }

    fun handleWebhookException(e: Exception): Pair<HttpStatusCode,String> {
        return when(e) {
            is SignatureVerificationException -> {
                application.log.warn("Invalid Stripe webhook signature ${e.message}")
                HttpStatusCode.BadRequest to "Invalid signature"
            }
            is IllegalArgumentException -> {
                application.log.error("Webhook processing error: Invalid data:", e)
                HttpStatusCode.BadRequest to (e.message ?: "Invalid data in webhook")
            }
            else -> {
                application.log.error("Error processing Stripe webhook", e)
                HttpStatusCode.InternalServerError to "Webhook processing failed"
            }
        }
    }
}