package com.thewritebrothers.features.crm.domain.entities

import java.time.LocalDateTime

data class CrmList(
    val id: String,
    val name:String,
    val ownerId: String,
    val crmBoardId: String,
    val cardIds: List<String>,
    val order: Int,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now(),
)
