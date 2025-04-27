package com.thewritebrothers.features.billing.domain.usecases

import com.thewritebrothers.features.auth.domain.repositories.UserRepository
import com.thewritebrothers.features.billing.domain.repositories.BillingRepository
import com.thewritebrothers.features.billing.presentation.models.dto.CheckoutResult

class InitiateCheckoutUseCase(
    private val userRepository: UserRepository,
    private val billingRepository: BillingRepository,
) {
    suspend fun execute(uid: String, priceId: String): CheckoutResult {
        val user = userRepository.getUserById(uid)
        val session = billingRepository.createSubscriptionCheckoutSession(
            priceId = priceId,
            uid = uid,
            email = user.email,
        )
        return CheckoutResult(sessionId = session.id, checkoutUrl = session.url)
    }
}
