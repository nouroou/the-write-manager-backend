package com.thewritebrothers.features.billing.domain.usecases

import com.thewritebrothers.features.billing.domain.entities.SubscriptionPlan
import com.thewritebrothers.features.billing.domain.repositories.BillingRepository

class GetSubscriptionPlansUseCase(
    private val billingRepository: BillingRepository,
) {
    suspend fun execute(): Result<List<SubscriptionPlan>> {
        return try {
            val result = billingRepository.getSubscriptionPlans()
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
