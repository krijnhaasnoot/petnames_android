package com.kinder.petnames.core

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.builtin.Email
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor(
    private val supabase: SupabaseClient
) {
    private val _isAuthenticated = MutableStateFlow(false)
    val isAuthenticated: StateFlow<Boolean> = _isAuthenticated.asStateFlow()
    
    private val _currentUserId = MutableStateFlow<String?>(null)
    val currentUserId: StateFlow<String?> = _currentUserId.asStateFlow()
    
    /**
     * Sign in anonymously if there's no existing session
     */
    suspend fun signInAnonymouslyIfNeeded() {
        try {
            // Check if we already have a session
            val currentSession = supabase.auth.currentSessionOrNull()
            if (currentSession != null) {
                _currentUserId.value = currentSession.user?.id
                _isAuthenticated.value = true
                println("✅ Already authenticated: ${currentSession.user?.id}")
                return
            }
            
            // Sign in anonymously
            supabase.auth.signInAnonymously()
            
            val session = supabase.auth.currentSessionOrNull()
            _currentUserId.value = session?.user?.id
            _isAuthenticated.value = true
            println("✅ Signed in anonymously: ${session?.user?.id}")
            
        } catch (e: Exception) {
            println("❌ Auth error: ${e.message}")
            throw e
        }
    }
    
    /**
     * Get the current user ID
     */
    fun requireUserId(): String {
        return currentUserId.value 
            ?: throw IllegalStateException("User is not authenticated")
    }
}
