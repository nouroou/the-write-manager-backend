package com.thewritebrothers.features.billing.domain.usecases

import com.stripe.model.checkout.Session
import com.thewritebrothers.features.auth.domain.repositories.UserRepository
import com.thewritebrothers.features.billing.domain.repositories.BillingRepository

class HandleWebhookUseCase(
    private val billingRepository: BillingRepository,
    private val userRepository: UserRepository,
) {
    suspend fun execute(payload: String, signature: String) {
        val event = billingRepository.constructWebhookEvent(payload, signature)

        if (event.type == "checkout.session.completed") {
            val session = event.dataObjectDeserializer.`object`.orElse(null) as? Session
                ?: throw IllegalArgumentException("Could not deserialize session from event data.")
            if (session.paymentStatus == "paid") {
                val supabaseUid = session.metadata["supabase_uid"]
                    ?: throw IllegalArgumentException("Missing supabase_uid in session metadata.")
                val stripeCustomerId = session.customer?.toString()
                    ?: throw  IllegalArgumentException("Missing customer ID in session.")
                val stripeSubscriptionId = session.subscription?.toString()
                    ?: throw  IllegalArgumentException("Missing subscription id in session.")
                 billingRepository.updateUserBillingStatus(
                     userId = supabaseUid,
                     isActive = true
                 )
                println("Successfully processed checkout and updated user: $supabaseUid")
            } else {
                println("Checkout session completed but payment status was not 'paid': ${session.paymentStatus}")
            }
        } else {
            println("Received unhandled checkout event with type ${event.type}")
        }
    }
}