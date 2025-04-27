package com.thewritebrothers.features.crm.domain.entities

import java.time.LocalDateTime

data class CrmCard(
    val id:String,
    val title:String,
    val description:String,
    val listId:String,
    val boardId:String,
    val ownerId:String,
    val memberIds:List<String>,
    val taskIds:List<String>,
    val commentIds:List<String>,
    val labels:List<String>,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now(),
    )
