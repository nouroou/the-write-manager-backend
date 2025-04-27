package com.thewritebrothers.features.billing.data.datasource

import com.stripe.model.Event
import com.stripe.model.checkout.Session
import com.stripe.net.Webhook
import com.stripe.param.checkout.SessionCreateParams
import com.thewritebrothers.features.billing.domain.entities.BillingRecord
import com.thewritebrothers.features.billing.domain.entities.SubscriptionPlan
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import kotlinx.serialization.json.Json

class BillingDatasource(
    stripeSecretKey: String,
    private val webhookSecret: String,
    private val successUrl: String,
    private val cancelUrl: String,
    private val supabase: SupabaseClient
) {
    init {
        com.stripe.Stripe.apiKey = stripeSecretKey
    }
    //First Method
    fun createCheckoutSession(priceId: String, email:String?, metadata: Map<String,String>): Session {
        val paramsBuilder = SessionCreateParams.builder()
            .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
            .addLineItem(
                SessionCreateParams.LineItem.builder()
                    .setPrice(priceId)
                    .setQuantity(1L)
                    .build()
            )
            .setSuccessUrl(successUrl)
            .setCancelUrl(cancelUrl)
            .putAllMetadata(metadata)
        email?.let { paramsBuilder.setCustomerEmail(it) }
        return Session.create(paramsBuilder.build())
    }

    fun constructEvent(payload: String, signature: String): Event {
        return Webhook.constructEvent(payload, signature, webhookSecret)
    }
    //Second Method
    suspend fun createBillingRecord(billingRecord: BillingRecord): BillingRecord {
        val response = supabase.from("billing_history").insert(billingRecord)
        return Json.decodeFromString(response.data)
    }

    suspend fun getBillingHistory(userId: String): List<BillingRecord> {
        val response = supabase.from("billing_history").select {
            filter {
                BillingRecord::userId eq userId
            }
        }
        return Json.decodeFromString(response.data)
    }

    suspend fun getSubscriptionPlans(): List<SubscriptionPlan> {
        val response = supabase.from("subscription_plans").select()
        return Json.decodeFromString(response.data)
    }

    suspend fun updateUserBillingStatus(userId: String, isActive: Boolean): Boolean {
        val response = supabase.from("users").update({"is_billing_active" to isActive}) {
            filter {
                BillingRecord::userId eq userId
            }
        }

        return Json.decodeFromString(response.data)
    }
}