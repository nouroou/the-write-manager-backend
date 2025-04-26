package com.thewritebrothers.features.auth.presentation.models.dto

data class SignupRequest(
    val email:String,
    val password:String,
    val name: String,
    val username:String,
    val role: String,
    val token: String,
    val company: String
)
