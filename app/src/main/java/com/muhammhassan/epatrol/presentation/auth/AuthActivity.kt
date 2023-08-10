package com.muhammhassan.epatrol.presentation.auth

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.muhammhassan.epatrol.presentation.auth.login.LoginView
import com.muhammhassan.epatrol.presentation.home.HomeActivity
import com.muhammhassan.epatrol.ui.theme.EPatrolTheme

class AuthActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EPatrolTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .systemBarsPadding(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LoginView(
                        onResponseSuccess = {
                            navigateToMainMenu()
                        }
                    )
                }
            }
        }
    }

    private fun navigateToMainMenu() {
        startActivity(Intent(this@AuthActivity, HomeActivity::class.java))
        finish()
    }
}