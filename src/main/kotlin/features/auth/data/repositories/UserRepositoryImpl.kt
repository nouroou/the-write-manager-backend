package com.thewritebrothers.features.auth.data.repositories

import com.thewritebrothers.features.auth.data.datasources.SupabaseUserDatasource
import com.thewritebrothers.features.auth.domain.entities.User
import com.thewritebrothers.features.auth.domain.repositories.UserRepository

class UserRepositoryImpl(private val userDatasource: SupabaseUserDatasource):UserRepository {
    override suspend fun deleteUserById(uid: String): String {
        return userDatasource.deleteUserById(uid)
    }

    override suspend fun getUserById(uid: String): User {
        return userDatasource.getUserById(uid)
    }

}