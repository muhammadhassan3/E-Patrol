package com.muhammhassan.epatrol.presentation.patrol.event.add

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.getValue
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.muhammhassan.epatrol.utils.createCustomTempFile
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.io.File

class AddEventActivity : ComponentActivity() {

    private val viewModel by viewModel<AddEventViewModel>()
    private lateinit var currentPath: String

    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                Timber.i("Image loaded")
                val file = File(currentPath)
                viewModel.setImage(file.toUri())
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val patrolId = intent.getLongExtra(patrolId, 0L)
        viewModel.patrolId = patrolId
        setContent {
            val image by viewModel.image.collectAsStateWithLifecycle()
            val event by viewModel.event.collectAsStateWithLifecycle()
            val summary by viewModel.summary.collectAsStateWithLifecycle()
            val action by viewModel.action.collectAsStateWithLifecycle()
            AddEventView(
                onNavUp = { finish() },
                onCaptureImage = { openIntentCamera() },
                image = image,
                event = event,
                eventChanged = viewModel::setEvent,
                summary = summary,
                summaryChanged = viewModel::setSummary,
                action = action,
                actionChanged = viewModel::setAction,
                onSubmit = {
                    viewModel.save(this@AddEventActivity)
                })
        }
    }

    private fun openIntentCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)

        createCustomTempFile(applicationContext).also {
            val photoUri = FileProvider.getUriForFile(
                this,
                "com.muhammhassan.epatrol",
                it
            )

            currentPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
            cameraLauncher.launch(intent)
        }
    }

    companion object {
        const val patrolId = "patrol_id"
    }
}
