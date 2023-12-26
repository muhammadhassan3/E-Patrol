package com.muhammhassan.epatrol.presentation.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
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

    private val toMainMenu by lazy{
        intent.getBooleanExtra(TO_MAIN_MENU, true)
    }

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
                            if(toMainMenu){
                                navigateToMainMenu()
                            }else{
                                Toast.makeText(
                                    this@AuthActivity,
                                    "Silahkan ulangi permintaanmu",
                                    Toast.LENGTH_SHORT
                                ).show()
                                finish()
                            }
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

    companion object{
        const val TO_MAIN_MENU = "to_main_menu"
    }
}