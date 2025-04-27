package com.thewritebrothers.features.billing.domain.usecases

import com.thewritebrothers.features.billing.domain.entities.BillingRecord
import com.thewritebrothers.features.billing.domain.repositories.BillingRepository

class CreateBillingRecordUseCase(private val billingRepository: BillingRepository) {
    suspend fun execute(billingRecord: BillingRecord): Result<BillingRecord> {
        return try {
            val record = billingRepository.createBillingRecord(billingRecord)
            Result.success(record)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}