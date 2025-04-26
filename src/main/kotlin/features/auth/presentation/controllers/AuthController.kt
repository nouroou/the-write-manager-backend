package com.thewritebrothers.features.auth.presentation.controllers

import com.thewritebrothers.features.auth.domain.usecases.LogoutUseCase
import com.thewritebrothers.features.auth.domain.usecases.LoginUseCase
import com.thewritebrothers.features.auth.domain.usecases.SignupUseCase
import com.thewritebrothers.features.auth.presentation.models.dto.*

class AuthController(
    private val loginUseCase: LoginUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val signupUseCase: SignupUseCase
) {
    suspend fun logIn(request: LoginRequest): LoginResponse {
        val loginResult = loginUseCase.execute(request.email, request.password)
        return LoginResponse(loginResult.token)
    }

    suspend fun signup(request: SignupRequest
    ): SignupResponse {

        val signupResult = signupUseCase.execute(
            request.email,
            request.password,
            request.role,
            request.name,
            request.username,
            request.token,
            request.company
        )

        return SignupResponse(
            signupResult,
            success = true,
            message = "Account created successfully! Please complete billing",
            nextStep = "billing",
            token = request.token,
            billingUrl = "/billing?user_id=${signupResult.id}"
        )
    }

    suspend fun logOut(request: LogoutRequest): LogoutResponse {
        logoutUseCase.execute(request.token)
        return LogoutResponse("User logged out")
    }


}