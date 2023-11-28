package com.muhammhassan.epatrol.utils.camera

import android.content.Intent
import android.os.Bundle
import android.view.OrientationEventListener
import android.view.Surface
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.camera.core.ImageCapture
import androidx.compose.runtime.remember
import com.muhammhassan.epatrol.R
import com.muhammhassan.epatrol.ui.theme.EPatrolTheme
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraActivity : ComponentActivity() {
    private lateinit var outputDirectory: File
    private val cameraExecutor: ExecutorService = Executors.newSingleThreadExecutor()
    private val imageCapture = ImageCapture.Builder().build()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val imageCapture: ImageCapture = remember { this.imageCapture }
            EPatrolTheme {
                CameraView(outputDirectory = outputDirectory,
                    executor = cameraExecutor,
                    onImageCaptured = {
                        val intent = Intent()
                        intent.putExtra(URI, it.path)
                        setResult(RESULT_OK, intent)

                        finish()
                    },
                    imageCapture = imageCapture,
                    onError = {
                        Toast.makeText(this, "Gagal mengambil kamera", Toast.LENGTH_SHORT).show()
                        finish()
                    })
            }
        }
        outputDirectory = getOutputDirectory()
    }

    private val cameraOrientationListener by lazy{
        object: OrientationEventListener(this){
            override fun onOrientationChanged(orientation: Int) {
                if(orientation == ORIENTATION_UNKNOWN) return
                val rotation = when(orientation){
                    in 45 until 135 -> Surface.ROTATION_270
                    in 135 until 225 -> Surface.ROTATION_180
                    in 225 until 315 -> Surface.ROTATION_90
                    else -> Surface.ROTATION_0
                }

                imageCapture.targetRotation = rotation
            }

        }
    }

    private fun getOutputDirectory(): File {
        val mediaDir = externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
        }

        return if (mediaDir != null && mediaDir.exists()) mediaDir else filesDir
    }

    override fun onStart() {
        super.onStart()
        cameraOrientationListener.enable()
    }
    override fun onStop() {
        super.onStop()
        cameraOrientationListener.disable()
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    companion object {
        const val URI = "uri"
    }
}