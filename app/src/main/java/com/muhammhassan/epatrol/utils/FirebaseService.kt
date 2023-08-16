package com.muhammhassan.epatrol.utils

import android.app.NotificationManager
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.muhammhassan.epatrol.R
import timber.log.Timber

class FirebaseService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Timber.e(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        val channelId = resources.getString(R.string.channel_id)
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notifManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        message.notification?.let { data ->
            val notification = NotificationCompat.Builder(this, channelId).also {
                it.setAutoCancel(true)
                it.setContentTitle(data.title)
                it.setContentText(data.body)
                it.setSmallIcon(R.drawable.bell)
                it.priority = NotificationCompat.PRIORITY_MAX
                it.setSound(alarmSound)
            }

            notifManager.notify(0, notification.build())
        }
    }
}