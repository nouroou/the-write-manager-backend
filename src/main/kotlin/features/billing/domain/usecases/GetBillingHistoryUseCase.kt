package com.thewritebrothers.features.billing.domain.usecases

import com.thewritebrothers.features.billing.domain.entities.BillingRecord
import com.thewritebrothers.features.billing.domain.entities.SubscriptionPlan
import com.thewritebrothers.features.billing.domain.repositories.BillingRepository

class GetBillingHistoryUseCase(private val billingRepository: BillingRepository) {
    suspend fun execute(userId: String): Result<List<BillingRecord>> {
        return try {
            val result = billingRepository.getBillingHistory(userId)
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}