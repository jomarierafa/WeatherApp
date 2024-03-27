package com.jvrcoding.weatherapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.jvrcoding.weatherapp.common.Screen
import com.jvrcoding.weatherapp.presentation.MainActivity
import kotlin.math.abs


class PushNotificationService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        Log.d("awit sir", token)
        // update server
    }

    override fun onMessageReceived(message: RemoteMessage) {

        val notification = message.notification
        val messageData = message.data

        Log.d("NotificationMessage", notification?.body.toString())
        Log.d("MessageData", messageData.toString())

        if (messageData.isEmpty() && notification == null) {
            return
        }

        val title = notification?.title ?: messageData["title"]
        val body = notification?.body ?: messageData["body"]

        createNotification(title, body)

    }

    private fun createNotification(title: String?, body: String?) {
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // sample intent
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            action = Intent.ACTION_VIEW
            data =
                Uri.parse("weather://${getString(R.string.app_scheme_host)}/${Screen.SignUpScreen.deeplinkId}")
        }

        val pendingIntent: PendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        } else {
            PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }

        val builder = NotificationCompat.Builder(this, getString(R.string.channel_id))
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setStyle(NotificationCompat.BigTextStyle().bigText(body))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAllowSystemGeneratedContextualActions(false)
            .build()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = getString(R.string.channel_id)
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system.
            notificationManager.createNotificationChannel(channel)
        }

        val id = System.currentTimeMillis().hashCode()
        notificationManager.notify(abs(id), builder)

    }


}