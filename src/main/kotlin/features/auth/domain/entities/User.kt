package com.thewritebrothers.features.auth.domain.entities

import java.time.LocalDateTime

data class User(
    val id: String,
    val name: String,
    val username: String,
    val email: String,
    val company: String,
    val token: String,
    val emailVerified: Boolean,
    val role:String,
    val billed:Boolean,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now(),
)
