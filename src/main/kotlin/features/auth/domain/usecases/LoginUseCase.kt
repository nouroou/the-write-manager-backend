package com.thewritebrothers.features.auth.domain.usecases

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.thewritebrothers.features.auth.domain.repositories.AuthRepository
import com.thewritebrothers.features.auth.domain.repositories.UserRepository
import com.thewritebrothers.features.auth.presentation.models.dto.LoginResponse
import io.ktor.server.config.*
import java.util.*

class LoginUseCase(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
) {
    suspend fun execute(email: String, password: String): LoginResponse {
        val uid = authRepository.signInWithEmailAndPassword(email, password)
        val user = userRepository.getUserById(uid)
            return LoginResponse(generateJwt(user.id, user.role))
    }

    private fun generateJwt(uid: String, role: String): String {
        val config = ApplicationConfig("application.conf")
        val secret = config.property("jwt.secret").getString()
        val issuer = "client-manager-backend"
        val expiresAt = Date(System.currentTimeMillis() + 60 * 60 * 1000)
        return JWT.create()
            .withSubject(uid)
            .withClaim("role", role)
            .withIssuer(issuer)
            .withExpiresAt(expiresAt)
            .sign(Algorithm.HMAC256(secret))
    }
}
