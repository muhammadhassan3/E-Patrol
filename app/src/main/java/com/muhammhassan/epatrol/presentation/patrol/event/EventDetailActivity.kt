package com.muhammhassan.epatrol.presentation.patrol.event

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.muhammhassan.epatrol.ui.theme.EPatrolTheme
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Locale
import androidx.core.net.toUri

class EventDetailActivity : ComponentActivity() {
    private val viewModel by viewModel<EventDetailViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(Color.Transparent.toArgb(), Color.Transparent.toArgb())
        )
        val removable = intent.getBooleanExtra(removable, false)
        val eventId = intent.getLongExtra(eventId, 0)
        viewModel.eventId = eventId
        viewModel.getData()
        setContent {
            val deleteState by viewModel.deleteState.collectAsStateWithLifecycle()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            EPatrolTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    EventDetailView(
                        onNavUp = { finish() },
                        uiState = uiState,
                        onDeleteAction = viewModel::deleteEvent,
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
        intent.data = url.toUri()
        startActivity(intent)

    }

    companion object {
        const val eventId = "event_id"
        const val removable = "removable"
    }
}