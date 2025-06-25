package com.muhammhassan.epatrol.presentation.patrol

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.muhammhassan.epatrol.domain.model.PatrolEventModel
import com.muhammhassan.epatrol.presentation.patrol.event.EventDetailActivity
import com.muhammhassan.epatrol.presentation.patrol.event.add.AddEventActivity
import com.muhammhassan.epatrol.ui.theme.EPatrolTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class PatrolDetailActivity : ComponentActivity() {
    private val viewModel by viewModel<PatrolDetailViewModel>()
    private val addEventLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if(it.resultCode == RESULT_OK){
            viewModel.getDetail()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(Color.Transparent.toArgb(), Color.Transparent.toArgb())
        )
        val id = intent.getLongExtra(id, 0)
        viewModel.orderId = id
        viewModel.getDetail()
        setContent {
            EPatrolTheme {
                val state by viewModel.state.collectAsState()
                val email by viewModel.email.collectAsState()
                val markAsDoneState by viewModel.confirmState.collectAsStateWithLifecycle()
                val eventState by viewModel.eventState.collectAsStateWithLifecycle()
                Surface{
                    PatrolDetailView(
                        onNavUp = { finish() },
                        userEmail = email,
                        state = state,
                        navigateToAddEvent = ::navigateToAddEvent,
                        navigateToDetailEvent = ::navigateToDetailEvent,
                        confirmState = markAsDoneState,
                        onCompleteSuccess = ::completedPatrolAction,
                        markAsDonePatrol = viewModel::verify,
                        onRefresh = viewModel::getDetail,
                        eventState = eventState
                    )
                }
            }
        }
    }

    private fun navigateToAddEvent(patrolId: Long) {
        val intent = Intent(this, AddEventActivity::class.java)
        intent.putExtra(AddEventActivity.patrolId, patrolId)
        addEventLauncher.launch(intent)
    }

    private fun navigateToDetailEvent(data: PatrolEventModel, removable: Boolean) {
        val intent = Intent(this, EventDetailActivity::class.java)
        intent.putExtra(EventDetailActivity.removable, removable)
        intent.putExtra(EventDetailActivity.eventId, data.id)
        addEventLauncher.launch(intent)
    }

    private fun completedPatrolAction() {
        setResult(RESULT_OK)
        finish()
    }

    companion object {
        const val id = "id"
    }
}