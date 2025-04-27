package com.thewritebrothers.features.crm.domain.entities

import java.time.LocalDateTime

data class CrmBoard(
    val id: String,
    val title: String,
    val description: String,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now(),
    val memberIds: List<String>,
    val memberRoles: List<String>,
)
