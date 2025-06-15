package com.muhammhassan.epatrol.presentation.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.muhammhassan.epatrol.presentation.auth.login.LoginView
import com.muhammhassan.epatrol.presentation.home.HomeActivity
import com.muhammhassan.epatrol.ui.theme.EPatrolTheme

class AuthActivity : ComponentActivity() {

    private val toMainMenu by lazy{
        intent.getBooleanExtra(TO_MAIN_MENU, true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(Color.Transparent.toArgb(), Color.Transparent.toArgb())
        )
        setContent {
            EPatrolTheme {
                Surface {
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