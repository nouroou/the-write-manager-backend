package com.thewritebrothers.features.billing.presentation.models.dto

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class PaymentResponse(
    val success: Boolean,
    val paymentId: String? = null,
    val billingRecordId: UUID? = null,
    val message: String? = null
)
