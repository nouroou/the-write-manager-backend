package com.thewritebrothers.features.invoices.domain.entities

import java.time.LocalDateTime

data class Invoice(
    val id: String,
    val title: String,
    val description: String,
    val services: List<String>,
    val price: Double,
    val dueDate: LocalDateTime,
    val sentAt: LocalDateTime,
    val sent: Boolean,
    val senderName: String,
    val recipientName: String,
    val senderEmail: String,
    val recipientEmail: String,
    val senderPhone: String?,
    val recipientPhone: String?,
    val senderCompany: String,
    val recipientCompany: String,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now(),
    val status: String,
    )
