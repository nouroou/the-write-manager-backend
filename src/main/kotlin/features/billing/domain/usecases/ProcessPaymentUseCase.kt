package com.thewritebrothers.features.billing.domain.usecases

import com.stripe.Stripe
import com.stripe.model.PaymentIntent
import com.stripe.param.PaymentIntentCreateParams
import com.thewritebrothers.features.auth.domain.repositories.UserRepository
import com.thewritebrothers.features.billing.domain.entities.BillingRecord
import com.thewritebrothers.features.billing.domain.entities.BillingStatus
import com.thewritebrothers.features.billing.domain.repositories.BillingRepository

class ProcessPaymentUseCase(
    private val billingRepository: BillingRepository,
    private val userRepository: UserRepository,
    private val stripeApiKey: String
) {
    init {
        Stripe.apiKey = stripeApiKey
    }

    suspend fun execute(
        userId: String,
        planId: String,
        amount: Double,
        paymentMethodId: String
    ): Result<BillingRecord> {
        return try {
            val paymentIntentParams = PaymentIntentCreateParams.builder()
                .setAmount((amount * 100).toLong())
                .setCurrency("usd")
                .setPaymentMethod(paymentMethodId)
                .setConfirmationMethod(PaymentIntentCreateParams.ConfirmationMethod.AUTOMATIC)
                .setConfirm(true)
                .build()
            val payementIntent = PaymentIntent.create(paymentIntentParams)

            val billingRecord = BillingRecord(
                userId = userId,
                planId = planId,
                amount = amount,
                status = if (payementIntent.status == "succeeded") BillingStatus.SUCCESSFUL else BillingStatus.PENDING,
                paymentMethod = paymentMethodId,
                stripePaymentId = payementIntent.id,
            )

            val savedBillingRecord = billingRepository.createBillingRecord(billingRecord)

            billingRepository.updateUserBillingStatus(userId, true)

            Result.success(savedBillingRecord)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}