package com.thewritebrothers.features.billing.presentation.models.dto

import kotlinx.serialization.Serializable

@Serializable
data class CreateCheckoutRequest(
    val email : String,
    val password: String,
    val name: String,
    val priceId: String,
)
