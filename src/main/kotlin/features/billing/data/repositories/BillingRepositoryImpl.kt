package com.thewritebrothers.features.billing.data.repositories

import com.stripe.exception.SignatureVerificationException
import com.stripe.model.Event
import com.stripe.model.checkout.Session
import com.thewritebrothers.features.billing.data.datasource.BillingDatasource
import com.thewritebrothers.features.billing.domain.entities.BillingRecord
import com.thewritebrothers.features.billing.domain.entities.SubscriptionPlan
import com.thewritebrothers.features.billing.domain.repositories.BillingRepository
import io.ktor.server.application.*

class BillingRepositoryImpl(
    private val billingDataSource: BillingDatasource,
    private val application: Application,
    ): BillingRepository {
    override suspend fun createSubscriptionCheckoutSession(priceId: String, email: String?, uid: String): Session {
        val metadata = mapOf("supabase_uid" to uid)
        return billingDataSource.createCheckoutSession(priceId, email, metadata)
    }

    override suspend fun constructWebhookEvent(payload: String, signature: String): Event {
        try {
            return billingDataSource.constructEvent(payload, signature)
        } catch (e: SignatureVerificationException) {
            application.log.error("Webhook signature verification failed: ${e.message}")
            throw e
        } catch (e: Exception) {
            application.log.error("Error constructing webhook event: ${e.message}")
            throw e // Re-throw
        }
    }

    override suspend fun createBillingRecord(billingRecord: BillingRecord): BillingRecord {
        return billingDataSource.createBillingRecord(billingRecord)
    }

    override suspend fun getBillingHistory(userId: String): List<BillingRecord> {
        return billingDataSource.getBillingHistory(userId)
    }

    override suspend fun getSubscriptionPlans(): List<SubscriptionPlan> {
        return billingDataSource.getSubscriptionPlans()
    }

    override suspend fun updateUserBillingStatus(userId: String, isActive: Boolean): Boolean {
        return billingDataSource.updateUserBillingStatus(userId, isActive)
    }
}