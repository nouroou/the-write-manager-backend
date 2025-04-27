package com.thewritebrothers.features.auth.domain.usecases

import com.thewritebrothers.features.auth.domain.repositories.AuthRepository
import com.thewritebrothers.features.auth.presentation.models.dto.LogoutResponse

class LogoutUseCase(
    private val authRepository: AuthRepository,
    ) {
    suspend fun execute(token: String): LogoutResponse {
      authRepository.logOut()

        return LogoutResponse("$token: Successfully logged out")
    }
}