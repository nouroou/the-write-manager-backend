package com.thewritebrothers.features.billing.di

import com.thewritebrothers.features.billing.data.datasource.BillingDatasource
import com.thewritebrothers.features.billing.data.repositories.BillingRepositoryImpl
import com.thewritebrothers.features.billing.domain.repositories.BillingRepository
import com.thewritebrothers.features.billing.domain.usecases.HandleWebhookUseCase
import com.thewritebrothers.features.billing.domain.usecases.InitiateCheckoutUseCase
import com.thewritebrothers.features.billing.presentation.controllers.BillingController
import io.github.cdimascio.dotenv.Dotenv
import io.github.jan.supabase.SupabaseClient
import io.ktor.server.application.*
import org.koin.dsl.module

fun billingModule(dotenv: Dotenv) = module {
    //Data Layer
    single {
        val successUrl = dotenv["STRIPE_SUCCESS_URL"] ?: "http://localhost:3000/payment-success?session_id={CHECKOUT_SESSION_ID}"
        val cancelUrl = dotenv["STRIPE_CANCEL_URL"] ?: "http://localhost:3000/payment-cancel"

        BillingDatasource(
            stripeSecretKey = dotenv["STRIPE_SECRET_KEY"] ?: throw RuntimeException("STRIPE_SECRET_KEY not set"),
            webhookSecret = dotenv["WEBHOOK_SECRET_KEY"] ?: throw RuntimeException("WEBHOOK_SECRET_KEY not set"),
            successUrl = successUrl,
            cancelUrl = cancelUrl,
            supabase = get<SupabaseClient>()
        )
    }
    single<BillingRepository> { BillingRepositoryImpl(get(), get())}

    //Domain Layer
    factory { InitiateCheckoutUseCase(get(), get()) }
    factory { HandleWebhookUseCase(get(), get()) }

    //Presentation
    single { BillingController(
        get(), get(), get(),
        get(), get(), get<Application>()
    ) }
}