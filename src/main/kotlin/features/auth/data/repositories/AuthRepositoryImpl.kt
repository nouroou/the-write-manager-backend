package com.thewritebrothers.features.auth.data.repositories

import com.thewritebrothers.features.auth.data.datasources.SupabaseAuthDatasource
import com.thewritebrothers.features.auth.domain.entities.User
import com.thewritebrothers.features.auth.domain.repositories.AuthRepository

class AuthRepositoryImpl(private val authDatasource: SupabaseAuthDatasource): AuthRepository {
    override suspend fun signInWithEmailAndPassword(email: String, password: String): String {
        return authDatasource.signInWithEmailAndPassword(email, password)
    }

    override suspend fun signUpWithEmailAndPassword(
        email: String,
        password: String,
        name: String,
        username: String,
        role: String,
        company: String,
        token: String
    ): User {
        return authDatasource.signUpWithEmailAndPassword(email, password, name, username, role, company, token)
    }

    override suspend fun logOut() {
        return authDatasource.logout()
    }
}