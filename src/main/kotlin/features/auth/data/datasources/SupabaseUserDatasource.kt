package com.thewritebrothers.features.auth.data.datasources

import com.thewritebrothers.features.auth.domain.entities.User
import com.thewritebrothers.features.auth.domain.entities.UserProfileUpdate
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.postgrest.from
import io.ktor.server.application.*

class SupabaseUserDatasource(private val supabase: SupabaseClient, private val application: Application) {

    suspend fun getUserById(uid: String): User {
        try {
            val result = supabase.from("users")
                .select {
                    filter { eq("id", uid) }
                }
                .decodeSingle<User>()
            return result
        } catch (e: Exception) {
            throw Exception("User fetch failed: ${e.message}")
        }
    }

    suspend fun updateUserBillingStatus(
        uid: String,
        isBilled: Boolean,
        role: String,
        stripeCustomerId: String,
        stripeSubscriptionId: String,
        ): Boolean {
        try {
            val updatedData = UserProfileUpdate(
                billed = isBilled,
                role = role,
                stripeCustomerId = stripeCustomerId,
                stripeSubscriptionId = stripeSubscriptionId,
            )
            val result = supabase.from("users").update(updatedData) { filter { eq("id", uid) } }
            return true
        } catch (e: Exception) {
            application.log.error("Error updating user billing status: ${e.message}")
            return false
        }
    }

    suspend fun deleteUserById(uid: String):String {
        try {
            val session = supabase.auth
            if (session.currentUserOrNull() == null && session.currentUserOrNull()?.id != uid) {
                throw Exception("User not found")
            }
            supabase.auth.admin.deleteUser(uid).let {
                supabase.from("users").delete { filter { eq("id", uid) } }
            }

            return "User deleted successfully"
        } catch (e: Exception) {
            throw Exception("Could Not Delete User: ${e.message}")
        }
    }
}