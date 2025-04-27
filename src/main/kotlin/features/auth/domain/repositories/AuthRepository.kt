package com.thewritebrothers.features.auth.domain.repositories

import com.thewritebrothers.features.auth.domain.entities.User

interface AuthRepository {
    suspend fun signInWithEmailAndPassword(email: String, password:String): String
    suspend fun signUpWithEmailAndPassword(
        email:String,
        password:String,
        name: String,
        username:String,
        role: String,
        company: String,
        token: String): User
    suspend fun logOut()
}