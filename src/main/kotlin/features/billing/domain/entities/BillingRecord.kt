package com.thewritebrothers.features.billing.domain.entities

import java.time.LocalDateTime
import java.util.UUID


data class BillingRecord (
    val id: UUID = UUID.randomUUID(),
    val userId: String,
    val planId: String,
    val amount: Double,
    val currency: String = "USD",
    val status :BillingStatus,
    val paymentMethod: String,
    val stripePaymentId: String,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now(),
    val periodEndsAt: LocalDateTime = LocalDateTime.now().plusMonths(1)
)