package com.thewritebrothers.features.auth.presentation.models.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserProfileUpdate (
    val billed: Boolean,
    val role: String,
    val stripeCustomerId: String,
    val stripeSubscriptionId: String,
)