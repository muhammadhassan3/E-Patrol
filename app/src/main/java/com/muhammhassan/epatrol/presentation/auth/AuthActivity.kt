package com.muhammhassan.epatrol.presentation.auth

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.muhammhassan.epatrol.presentation.auth.login.LoginView
import com.muhammhassan.epatrol.presentation.auth.login.LoginViewModel
import com.muhammhassan.epatrol.presentation.home.HomeActivity
import com.muhammhassan.epatrol.ui.theme.EPatrolTheme
import org.koin.androidx.compose.koinViewModel

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
                    val viewModel = koinViewModel<LoginViewModel>()
                    val email by viewModel.email.collectAsState()
                    val password by viewModel.password.collectAsState()
                    val state by viewModel.state.collectAsState()
                    LoginView(
                        email = email,
                        onEmailChanged = viewModel::setEmail,
                        onSaveButton = viewModel::login,
                        password = password,
                        onPasswordChanged = viewModel::setPassword,
                        state = state,
                        onResponseSuccess = {
                            navigateToMainMenu()
                        }
                    )
                }
            }
        }
    }

    private fun navigateToMainMenu(){
        startActivity(Intent(this@AuthActivity, HomeActivity::class.java))
        finish()
    }
}