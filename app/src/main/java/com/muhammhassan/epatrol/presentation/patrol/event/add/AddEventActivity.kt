package com.muhammhassan.epatrol.presentation.patrol.event.add

import android.Manifest
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.getValue
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.Priority
import com.muhammhassan.epatrol.utils.createCustomTempFile
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.io.File
import java.util.concurrent.TimeUnit

class AddEventActivity : ComponentActivity() {

    private val viewModel by viewModel<AddEventViewModel>()
    private lateinit var currentPath: String
    private var fusedLocation: FusedLocationProviderClient? = null
    private lateinit var locationRequest: LocationRequest
    private val locationCallback: LocationCallback by lazy {
        object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                super.onLocationResult(p0)
                for (location in p0.locations) {
                    Timber.i("Location Updated : ${location.latitude}/${location.longitude}")
                    viewModel.latitude = location.latitude
                    viewModel.longitude = location.longitude
                }
            }
        }
    }

    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                Timber.i("Image loaded")
                val file = File(currentPath)
                viewModel.setImage(file.toUri())
            }
        }

    private val locationIntentCallback =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            when (result.resultCode) {
                RESULT_OK -> {
                    Timber.i("Location Activated")
                }

                RESULT_CANCELED -> {
                    Timber.e("Location request canceled")
                }
            }
        }

    private val permissionCallback =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            val result = it.all { permission ->
                permission.value
            }

            if (result) {
                startLocationUpdate()
            } else {
                Toast.makeText(
                    this,
                    "Perizinan ditolak, silahkan aktifkan perizinan lokasi pada pengaturan",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
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

        initLocation()
        startLocationUpdate()
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

    private fun initLocation() {
        fusedLocation = LocationServices.getFusedLocationProviderClient(this)
        locationRequest =
            LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, TimeUnit.SECONDS.toMillis(2))
                .build()

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
        val client = LocationServices.getSettingsClient(this)

        client.checkLocationSettings(builder.build())
            .addOnSuccessListener {

            }.addOnFailureListener {
                if (it is ResolvableApiException) {
                    try {
                        locationIntentCallback.launch(
                            IntentSenderRequest.Builder(it.resolution).build()
                        )
                    } catch (e: IntentSender.SendIntentException) {
                        Timber.e(e)
                        Toast.makeText(this, "Gagal menyelesaikan masalah", Toast.LENGTH_SHORT)
                            .show()
                        finish()
                    }
                }
            }
    }

    private fun startLocationUpdate() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocation?.requestLocationUpdates(locationRequest, locationCallback, mainLooper)
        } else {
            permissionCallback.launch(REQUIRED_LOCATION_PERMISSION)
        }
    }

    private fun stopLocationUpdate() {
        fusedLocation?.removeLocationUpdates(locationCallback)
    }

    override fun onDestroy() {
        super.onDestroy()
        stopLocationUpdate()
    }


    companion object {
        const val patrolId = "patrol_id"
        private val REQUIRED_LOCATION_PERMISSION = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }
}
