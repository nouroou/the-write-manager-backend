package com.thewritebrothers.features.billing.domain.repositories

import com.stripe.model.Event
import com.stripe.model.checkout.Session
import com.thewritebrothers.features.billing.domain.entities.BillingRecord
import com.thewritebrothers.features.billing.domain.entities.SubscriptionPlan

interface BillingRepository {
    suspend fun createSubscriptionCheckoutSession(priceId: String, email: String?, uid: String): Session
    suspend fun constructWebhookEvent(payload: String, signature: String):Event
    suspend fun createBillingRecord(billingRecord: BillingRecord):BillingRecord
    suspend fun getBillingHistory(userId: String): List<BillingRecord>
    suspend fun getSubscriptionPlans(): List<SubscriptionPlan>
    suspend fun updateUserBillingStatus(userId: String, isActive: Boolean): Boolean
}