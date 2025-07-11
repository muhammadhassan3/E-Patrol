package com.muhammhassan.epatrol.presentation.patrol.event.add

import android.Manifest
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.content.ContextCompat
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
import com.muhammhassan.epatrol.utils.camera.CameraActivity
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

    /**
     * Handle Camera Activity Page
     */
    private val customCameraCallback =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                val data = it.data?.getStringExtra(CameraActivity.URI)
                val uri = data?.toUri()
                uri?.let {
                    viewModel.setImage(uri)
                }
            }
        }
    private val cameraPermissionCallback =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                openCameraPage()
            } else {
                Toast.makeText(
                    this,
                    "Silahkan izinkan penggunaan kamera untuk mengambil gambar",
                    Toast.LENGTH_SHORT
                ).show()
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
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(Color.Transparent.toArgb(), Color.Transparent.toArgb())
        )
        val patrolId = intent.getLongExtra(patrolId, 0L)
        viewModel.patrolId = patrolId
        setContent {
            val image by viewModel.image.collectAsStateWithLifecycle()
            val event by viewModel.event.collectAsStateWithLifecycle()
            val summary by viewModel.summary.collectAsStateWithLifecycle()
            val action by viewModel.action.collectAsStateWithLifecycle()
            val state by viewModel.state.collectAsStateWithLifecycle()
            AddEventView(onNavUp = { finish() },
                onCaptureImage = { initCamera() },
                image = image,
                event = event,
                eventChanged = viewModel::setEvent,
                summary = summary,
                summaryChanged = viewModel::setSummary,
                action = action,
                actionChanged = viewModel::setAction,
                onSubmit = viewModel::save,
                addState = state,
                onResponseSuccess = { navigateBack() })
        }

        initLocation()
        startLocationUpdate()
    }

    private fun navigateBack() {
        setResult(RESULT_OK)
        Toast.makeText(this, "Kejadian berhasil ditambahkan", Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun initCamera() {
        if (ContextCompat.checkSelfPermission(
                this, REQUIRED_CAMERA_PERMISISON
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            openCameraPage()
        } else {
            cameraPermissionCallback.launch(REQUIRED_CAMERA_PERMISISON)
        }
    }

    private fun openCameraPage() {
        customCameraCallback.launch(Intent(this, CameraActivity::class.java))
    }

    private fun initLocation() {
        fusedLocation = LocationServices.getFusedLocationProviderClient(this)
        locationRequest =
            LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, TimeUnit.SECONDS.toMillis(2))
                .build()

        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        val client = LocationServices.getSettingsClient(this)

        client.checkLocationSettings(builder.build()).addOnSuccessListener {

        }.addOnFailureListener {
            if (it is ResolvableApiException) {
                try {
                    locationIntentCallback.launch(
                        IntentSenderRequest.Builder(it.resolution).build()
                    )
                } catch (e: IntentSender.SendIntentException) {
                    Timber.e(e)
                    Toast.makeText(this, "Gagal menyelesaikan masalah", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }

    private fun startLocationUpdate() {
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION
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
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
        )

        private val REQUIRED_CAMERA_PERMISISON = Manifest.permission.CAMERA
    }
}
