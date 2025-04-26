package com.thewritebrothers.features.auth.presentation.models.dto

data class LoginRequest (
    val email: String,
    val password: String,
)