package com.muhammhassan.epatrol.presentation.patrol.event

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.muhammhassan.epatrol.domain.model.PatrolEventModel
import com.muhammhassan.epatrol.ui.theme.EPatrolTheme
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Locale

class EventDetailActivity : ComponentActivity() {
    private val viewModel by viewModel<EventDetailViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val patrolId = intent.getLongExtra(patrolId, 0L)
        val removable = intent.getBooleanExtra(removable, false)
        val event = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(eventData, PatrolEventModel::class.java)
        } else {
            intent.getParcelableExtra(eventData)
        } ?: throw IllegalArgumentException("Data bundle cannot null")
        viewModel.patrolId = patrolId
        setContent {
            val email by viewModel.email.collectAsStateWithLifecycle()
            val deleteState by viewModel.deleteState.collectAsStateWithLifecycle()
            EPatrolTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    EventDetailView(
                        onNavUp = { finish() },
                        data = event,
                        onDeleteAction = viewModel::deleteEvent,
                        email = email,
                        deleteState = deleteState,
                        onResponseSuccess = ::responseSuccess,
                        removable = removable,
                        showLocationOnMap = ::showLocationOnMap
                    )
                }
            }
        }
    }

    private fun responseSuccess() {
        Toast.makeText(this, "Kejadian berhasil dihapus", Toast.LENGTH_SHORT).show()
        setResult(RESULT_OK)
        finish()
    }

    private fun showLocationOnMap(lat: Double, long: Double) {
        val title = "Lokasi kejadian"
        val url = String.format(
            Locale.ENGLISH,
            "geo:%f,%f(%s)?q=%f,%f(%s)",
            lat,
            long,
            title,
            lat,
            long,
            title
        )
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)

    }

    companion object {
        const val patrolId = "patrol_id"
        const val eventData = "event_data"
        const val removable = "removable"
    }
}