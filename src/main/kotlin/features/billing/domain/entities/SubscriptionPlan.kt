package com.thewritebrothers.features.billing.domain.entities

data class SubscriptionPlan(
    val id: String,
    val name: String,
    val price: Double,
    val currency: String = "USD",
    val interval: String = "month",
    val description: String? = null,
)
