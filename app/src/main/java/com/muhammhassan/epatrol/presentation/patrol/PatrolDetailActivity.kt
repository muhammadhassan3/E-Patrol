package com.muhammhassan.epatrol.presentation.patrol

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.muhammhassan.epatrol.domain.model.PatrolEventModel
import com.muhammhassan.epatrol.presentation.patrol.event.EventDetailActivity
import com.muhammhassan.epatrol.ui.theme.EPatrolTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class PatrolDetailActivity : ComponentActivity() {
    private val viewModel by viewModel<PatrolDetailViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val id = intent.getLongExtra(id, 0)
        viewModel.getDetail(id)
        setContent {
            EPatrolTheme {
                val state by viewModel.state.collectAsState()
                val email by viewModel.email.collectAsState()
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    PatrolDetailView(
                        onNavUp = { finish() },
                        userEmail = email,
                        state = state,
                        navigateToAddEvent = ::navigateToAddEvent,
                        navigateToDetailEvent = ::navigateToDetailEvent
                    )
                }
            }
        }
    }

    private fun navigateToAddEvent(patrolId: Long) {

    }

    private fun navigateToDetailEvent(data: PatrolEventModel) {
        val intent = Intent(this, EventDetailActivity::class.java)
        intent.putExtra(EventDetailActivity.patrolId, viewModel.patrolId)
        intent.putExtra(EventDetailActivity.eventData, data)
        startActivity(intent)
    }

    companion object {
        const val id = "id"
    }
}