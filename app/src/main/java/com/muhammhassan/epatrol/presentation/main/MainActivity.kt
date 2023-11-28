package com.muhammhassan.epatrol.presentation.main

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.muhammhassan.epatrol.R
import com.muhammhassan.epatrol.presentation.auth.AuthActivity
import kotlinx.coroutines.runBlocking

class MainActivity : ComponentActivity() {
    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                navigate()
            } else {
                navigate()
                Toast.makeText(
                    this,
                    "Silahkan aktifkan perizinan notifikasi pada halaman pengaturan",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition {
            true
        }
        super.onCreate(savedInstanceState)
        initiateNotificationChannel()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                navigate()
            } else {
                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        } else {
            navigate()
        }
    }

    private fun initiateNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = resources.getString(R.string.channel_id)
            val channelName = resources.getString(R.string.channel_name)
            val channelDesc = resources.getString(R.string.channel_desc)
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            ).also {
                it.description = channelDesc
            }

            val nm = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            nm.createNotificationChannel(channel)
        }
    }


    private fun navigate() {
        runBlocking {
            startActivity(Intent(this@MainActivity, AuthActivity::class.java))
            finish()
        }
    }
}
