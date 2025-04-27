package com.thewritebrothers.features.auth.data.datasources

import com.thewritebrothers.features.auth.domain.entities.User
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.gotrue.OtpType
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.github.jan.supabase.postgrest.from
import java.time.LocalDateTime

class SupabaseAuthDatasource(private val supabase: SupabaseClient) {
    suspend fun signInWithEmailAndPassword(email:String, password:String): String {
        try {
            val session = supabase.auth
            session.signInWith(Email) {
                this.email = email
                this.password = password
            }
            val userId = session.currentUserOrNull()?.id
                ?: throw Exception("User ID not found")
            return userId
        } catch (e: Exception) {
            throw Exception("Authentication failed: ${e.message}")
        }
    }

    suspend fun signUpWithEmailAndPassword(
        email:String,
        password:String,
        name: String,
        username:String,
        role: String,
        company: String,
        token: String): User {
        try {
            val session = supabase.auth
                session.signUpWith(Email) {
                    this.email = email
                    this.password = password
                }
            val uid = session.currentUserOrNull()?.id ?: throw Exception("User ID not found")
            val user = User(
                email = email,
                emailVerified = false,
                id = uid,
                name = name,
                username = username,
                role = role,
                billed = false,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now(),
                token = session.currentSessionOrNull()?.accessToken ?: token,
                company = company
            )
            supabase.from("users").insert(user)
            sendEmailVerification(email)
            return user
        } catch (e: Exception) {
            throw Exception("Authentication failed: ${e.message}")
        }
    }

    suspend fun logout() {
        try {
            supabase.auth.signOut()
        } catch (e: Exception) {
            throw Exception("Logout failed: ${e.message}")
        }
    }

    suspend fun sendEmailVerification(email : String): String {
        try {
            val session = supabase.auth
            val currentUser = session.currentSessionOrNull()

                if (isEmailVerified(currentUser?.user?.email.toString())) {
                    return "Email is verified"
                } else {
                    session.verifyEmailOtp(
                        type = OtpType.Email.EMAIL, email = email, token = currentUser!!.accessToken
                    )
                    return "Verification code sent"
                }

        } catch (e: Exception) {
            throw Exception("Authentication failed: ${e.message}")
        }
    }

    private fun isEmailVerified(email: String): Boolean {
         try {
            val session = supabase.auth
            val currentUser = session.currentUserOrNull()
             return if (currentUser?.emailConfirmedAt != null && currentUser.email == email) {
                 true
             } else {
                 false
             }
        } catch (e: Exception) {
            throw Exception("Authentication failed: ${e.message}")
        }
    }



}