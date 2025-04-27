package com.thewritebrothers.features.clients.domain.entities

import java.time.LocalDateTime


data class Client(
    val id: String,
    val name: String,
    val email: String,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now(),
)
