package com.thewritebrothers.features.billing.presentation.models.dto

import com.thewritebrothers.features.billing.domain.entities.BillingRecord
import kotlinx.serialization.Serializable

@Serializable
data class SubscriptionPlansRequest(
    val userId: String,
    val billingRecord: BillingRecord? = null,
)
