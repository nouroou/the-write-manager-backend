package com.thewritebrothers.features.billing.presentation.models.dto

import kotlinx.serialization.Serializable

@Serializable
data class CreateCheckoutResponse(
    val sessionId: String,
    val checkoutUrl: String?,
)
