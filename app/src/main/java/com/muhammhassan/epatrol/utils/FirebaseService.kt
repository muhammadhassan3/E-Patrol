package com.muhammhassan.epatrol.utils

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.media.Ringtone
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import androidx.core.app.PendingIntentCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.muhammhassan.epatrol.R
import com.muhammhassan.epatrol.domain.usecase.NotificationServiceUseCase
import com.muhammhassan.epatrol.presentation.main.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import timber.log.Timber
import kotlin.math.roundToInt

class FirebaseService : FirebaseMessagingService(), KoinComponent {
    private val notif: NotificationServiceUseCase by inject()
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Timber.e(token)
        CoroutineScope(Dispatchers.IO).launch {
            notif.setToken(token)
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        val channelId = resources.getString(R.string.channel_id)
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notifManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val vibrate = longArrayOf(500,500,500)
        val id = Math.random().toInt()

        message.notification?.let { data ->
            val notification = NotificationCompat.Builder(this, channelId).also {
                it.setAutoCancel(true)
                it.setContentTitle(data.title)
                it.setContentText(data.body)
                it.setSmallIcon(R.drawable.bell)
                it.setVibrate(vibrate)
                it.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                it.priority = NotificationCompat.PRIORITY_MAX
                it.setContentIntent(pendingIntent)
                it.setSound(alarmSound)
            }

            notifManager.notify(id, notification.build())
        }
    }
}