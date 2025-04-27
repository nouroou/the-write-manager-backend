package com.thewritebrothers.features.auth.domain.usecases

import com.thewritebrothers.features.auth.domain.entities.User
import com.thewritebrothers.features.auth.domain.repositories.AuthRepository

class SignupUseCase(
    private val authRepository: AuthRepository,
) {
    suspend fun execute(
        email: String,
        password: String,
        name: String,
        username: String,
        role: String,
        token: String,
        company: String
    ): User {
        return authRepository.signUpWithEmailAndPassword(email, password, name, username, role, token, company)
    }
}