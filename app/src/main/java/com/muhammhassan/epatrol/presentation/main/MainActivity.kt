package com.muhammhassan.epatrol.presentation.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.muhammhassan.epatrol.presentation.auth.AuthActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition {
            true
        }
        super.onCreate(savedInstanceState)

        runBlocking {
            delay(1200)
            startActivity(Intent(this@MainActivity, AuthActivity::class.java))
            finish()
        }
    }
}
