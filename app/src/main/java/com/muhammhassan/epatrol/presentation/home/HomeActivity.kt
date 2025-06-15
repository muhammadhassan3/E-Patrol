package com.muhammhassan.epatrol.presentation.home

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.muhammhassan.epatrol.presentation.auth.AuthActivity
import com.muhammhassan.epatrol.presentation.patrol.PatrolDetailActivity
import com.muhammhassan.epatrol.ui.theme.EPatrolTheme

class HomeActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(Color.Transparent.toArgb(), Color.Transparent.toArgb())
        )
        setContent {
            EPatrolTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HomeView(navigateToDetailPage = {
                        navigateToDetailPatrol(it)
                    }, onReset = ::navigateToLogin)
                }
            }
        }
    }

    private fun navigateToDetailPatrol(id: Long) {
        val intent = Intent(this, PatrolDetailActivity::class.java)
        intent.putExtra(PatrolDetailActivity.id, id)
        startActivity(intent)
    }

    private fun navigateToLogin(){
        val intent = Intent(this, AuthActivity::class.java)
        finishAffinity()
        startActivity(intent)
    }
}