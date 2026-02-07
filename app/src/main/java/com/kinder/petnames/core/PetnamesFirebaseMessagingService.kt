package com.kinder.petnames.core

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.kinder.petnames.MainActivity
import com.kinder.petnames.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class PetnamesFirebaseMessagingService : FirebaseMessagingService() {
    
    @Inject
    lateinit var notificationManager: NotificationManager
    
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    
    companion object {
        private const val CHANNEL_ID = "petnames_matches"
        private const val CHANNEL_NAME = "Matches"
    }
    
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        println("📱 New FCM token received: $token")
        
        // Save token to server via NotificationManager
        serviceScope.launch {
            try {
                notificationManager.handleNewToken(token)
            } catch (e: Exception) {
                println("❌ Failed to handle new token: ${e.message}")
            }
        }
    }
    
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        
        val title = message.notification?.title ?: message.data["title"] ?: "Petnames"
        val body = message.notification?.body ?: message.data["body"] ?: ""
        val type = message.data["type"] ?: "match"
        
        showNotification(title, body, type)
    }
    
    private fun showNotification(title: String, body: String, type: String) {
        val systemNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as android.app.NotificationManager
        
        // Create channel for Android O+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                android.app.NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Notifications for matches and household activity"
            }
            systemNotificationManager.createNotificationChannel(channel)
        }
        
        // Create intent
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("notification_type", type)
        }
        
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        
        // Build notification
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()
        
        systemNotificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }
}
