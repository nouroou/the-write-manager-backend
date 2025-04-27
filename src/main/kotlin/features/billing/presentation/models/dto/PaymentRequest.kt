package com.thewritebrothers.features.billing.presentation.models.dto

import kotlinx.serialization.Serializable

@Serializable
data class PaymentRequest(
    val userId: String,
    val planId: String,
    val paymentMethodId: String,
)
