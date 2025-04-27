package com.thewritebrothers.features.tasks.domain.entities

import com.thewritebrothers.core.common.domain.entities.Comment
import java.time.LocalDateTime

data class Task(
    val id: String,
    val title: String,
    val description: String,
    val dueDate: LocalDateTime,
    val membersIdList: List<String>,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now(),
    val completed: Boolean,
    val comments: List<Comment>,
)

