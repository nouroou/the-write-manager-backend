package com.thewritebrothers.features.billing.presentation.models.dto

import com.thewritebrothers.features.billing.domain.entities.SubscriptionPlan

data class SubscriptionPlansResponse(
    val plans: List<SubscriptionPlan>? = emptyList(),
    val currentPlanId: String ? = null,
    val success: Boolean,
    val message: String? = null,
)
