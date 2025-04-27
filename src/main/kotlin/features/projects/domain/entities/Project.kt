package com.thewritebrothers.features.projects.domain.entities

import java.time.LocalDateTime

data class Project(
    val id: String,
    val name: String,
    val description: String,
    val memberIds: List<String>,
    val taskIds: List<String>,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now(),
)
