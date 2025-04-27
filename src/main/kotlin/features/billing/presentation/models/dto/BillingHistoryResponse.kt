package com.thewritebrothers.features.billing.presentation.models.dto

import com.thewritebrothers.features.billing.domain.entities.BillingRecord

data class BillingHistoryResponse(
    val message: String? = null,
    val success: Boolean,
    val billingRecords: List<BillingRecord>? = null,
)
