package com.thewritebrothers.features.auth.presentation.models.dto

import com.thewritebrothers.features.auth.domain.entities.User

data class SignupResponse(
    val user: User,
    val success: Boolean,
    val message: String? = null,
    val nextStep: String? = null,
    val token: String? = null,
    val billingUrl: String? = null,
)
