package com.thewritebrothers.core.common.domain.entities

import java.time.LocalDateTime

data class Comment(
    val id: String,
    val comment: String,
    val commenterId: String,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now(),
    val mentionIds: List<String>,
)
