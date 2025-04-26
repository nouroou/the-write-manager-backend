package com.thewritebrothers.features.auth.domain.repositories

import com.thewritebrothers.features.auth.domain.entities.User

interface UserRepository {
    suspend fun deleteUserById(uid: String):String
    suspend fun getUserById(uid: String): User

}