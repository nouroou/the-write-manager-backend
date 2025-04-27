package com.thewritebrothers.features.billing.presentation.models.dto

import kotlinx.serialization.Serializable

data class CheckoutResult(
    val sessionId: String,
    val checkoutUrl: String?,
)
