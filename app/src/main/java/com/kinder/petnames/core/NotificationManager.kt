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
     * Note: This requires a Supabase Edge Function "send-push" to be set up
     * For now, this is a no-op since Edge Functions require additional setup
     */
    suspend fun sendMatchPushToHousehold(householdId: String, name: String) {
        // Edge Functions require additional Supabase setup
        // For now, just log the intent - the local notification will still show
        println("📤 Would send match push to household $householdId for name: $name")
        // TODO: Implement when Supabase Edge Function "send-push" is deployed
    }
    
    /**
     * Send push notification when new member joins
     * Note: This requires a Supabase Edge Function "send-push" to be set up
     */
    suspend fun sendNewMemberPushToHousehold(householdId: String, memberName: String) {
        // Edge Functions require additional Supabase setup
        // For now, just log the intent
        println("📤 Would send new member push to household for: $memberName")
        // TODO: Implement when Supabase Edge Function "send-push" is deployed
    }
}
