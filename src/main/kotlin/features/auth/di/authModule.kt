package com.thewritebrothers.features.auth.di

import com.thewritebrothers.features.auth.data.datasources.SupabaseAuthDatasource
import com.thewritebrothers.features.auth.data.datasources.SupabaseUserDatasource
import com.thewritebrothers.features.auth.data.repositories.AuthRepositoryImpl
import com.thewritebrothers.features.auth.data.repositories.UserRepositoryImpl
import com.thewritebrothers.features.auth.domain.repositories.AuthRepository
import com.thewritebrothers.features.auth.domain.repositories.UserRepository
import com.thewritebrothers.features.auth.domain.usecases.LoginUseCase
import com.thewritebrothers.features.auth.domain.usecases.LogoutUseCase
import com.thewritebrothers.features.auth.domain.usecases.SignupUseCase
import com.thewritebrothers.features.auth.presentation.controllers.AuthController
import io.ktor.server.application.*
import org.koin.dsl.module

fun authModule() = module {
    //Data Layer
    single { SupabaseAuthDatasource(get()) }
    single { SupabaseUserDatasource(get(), get<Application>())}

    single<AuthRepository> { AuthRepositoryImpl(get()) }
    single<UserRepository> { UserRepositoryImpl(get()) }

    //Domain Layer
    factory { SignupUseCase(get()) }
    factory { LoginUseCase(get(), get()) }
    factory { LogoutUseCase(get()) }

    //Presentation Layer
    single { AuthController(get(), get(), get()) }
}