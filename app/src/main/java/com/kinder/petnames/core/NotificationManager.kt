package com.kinder.petnames.core

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessaging
import com.kinder.petnames.R
import dagger.hilt.android.qualifiers.ApplicationContext
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await
import kotlinx.serialization.Serializable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val supabase: SupabaseClient,
    private val sessionManager: SessionManager
) {
    
    companion object {
        private const val CHANNEL_ID = "petnames_matches"
        private const val CHANNEL_NAME = "Matches"
        private const val CHANNEL_DESCRIPTION = "Notifications for matches and household activity"
    }
    
    private val _fcmToken = MutableStateFlow<String?>(null)
    val fcmToken: StateFlow<String?> = _fcmToken.asStateFlow()
    
    private val _isAuthorized = MutableStateFlow(false)
    val isAuthorized: StateFlow<Boolean> = _isAuthorized.asStateFlow()
    
    init {
        createNotificationChannel()
    }
    
    /**
     * Create notification channel for Android O+
     */
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = CHANNEL_DESCRIPTION
            }
            
            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }
    
    /**
     * Check if notification permission is granted (Android 13+)
     */
    fun checkPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true // Permission not needed pre-Android 13
        }
    }
    
    /**
     * Get FCM token and save to server
     */
    suspend fun registerForPushNotifications() {
        try {
            val token = FirebaseMessaging.getInstance().token.await()
            _fcmToken.value = token
            println("📱 FCM token: $token")
            
            // Save token to server
            saveTokenToServer(token)
            
            _isAuthorized.value = true
        } catch (e: Exception) {
            println("❌ Failed to get FCM token: ${e.message}")
            _isAuthorized.value = false
        }
    }
    
    /**
     * Handle new token from FirebaseMessagingService
     */
    suspend fun handleNewToken(token: String) {
        _fcmToken.value = token
        println("📱 New FCM token: $token")
        saveTokenToServer(token)
    }
    
    /**
     * Save FCM token to Supabase profiles table
     */
    private suspend fun saveTokenToServer(token: String) {
        val userId = sessionManager.currentUserId.value
        if (userId == null) {
            println("⚠️ No user ID, cannot save push token")
            return
        }
        
        try {
            @Serializable
            data class TokenUpdate(val push_token: String)
            
            supabase.postgrest
                .from("profiles")
                .update(TokenUpdate(token)) {
                    filter { eq("id", userId) }
                }
            
            println("✅ Push token saved to server")
        } catch (e: Exception) {
            println("❌ Failed to save push token: ${e.message}")
        }
    }
    
    /**
     * Show local notification (for testing or when server push isn't available)
     */
    fun showLocalNotification(title: String, body: String) {
        if (!checkPermission()) {
            println("⚠️ Notification permission not granted")
            return
        }
        
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()
        
        NotificationManagerCompat.from(context).notify(
            System.currentTimeMillis().toInt(),
            notification
        )
    }
    
    /**
     * Show match notification locally
     */
    fun showMatchNotification(name: String) {
        showLocalNotification(
            title = "🎉 It's a Match!",
            body = "Jullie vinden '$name' allebei een geweldige naam!"
        )
    }
    
    /**
     * Show new member notification locally
     */
    fun showNewMemberNotification(memberName: String) {
        showLocalNotification(
            title = "👋 Nieuw lid!",
            body = "$memberName is toegetreden tot je household!"
        )
    }
    
    /**
     * Send push notification to other household members via Edge Function
     */
    suspend fun sendMatchPushToHousehold(householdId: String, name: String) {
        val userId = sessionManager.currentUserId.value ?: return
        
        try {
            @Serializable
            data class Payload(val name: String)
            
            @Serializable
            data class PushRequest(
                val type: String,
                val household_id: String,
                val exclude_user_id: String,
                val payload: Payload
            )
            
            val request = PushRequest(
                type = "match",
                household_id = householdId,
                exclude_user_id = userId,
                payload = Payload(name)
            )
            
            supabase.functions.invoke(
                function = "send-push",
                body = request
            )
            
            println("✅ Push notification sent to household members")
        } catch (e: Exception) {
            println("⚠️ Failed to send push to household: ${e.message}")
            // Non-critical - local notification already shown
        }
    }
    
    /**
     * Send push notification when new member joins
     */
    suspend fun sendNewMemberPushToHousehold(householdId: String, memberName: String) {
        val userId = sessionManager.currentUserId.value ?: return
        
        try {
            @Serializable
            data class Payload(val member_name: String)
            
            @Serializable
            data class PushRequest(
                val type: String,
                val household_id: String,
                val exclude_user_id: String,
                val payload: Payload
            )
            
            val request = PushRequest(
                type = "new_member",
                household_id = householdId,
                exclude_user_id = userId,
                payload = Payload(memberName)
            )
            
            supabase.functions.invoke(
                function = "send-push",
                body = request
            )
            
            println("✅ New member push sent to household")
        } catch (e: Exception) {
            println("⚠️ Failed to send new member push: ${e.message}")
        }
    }
}
