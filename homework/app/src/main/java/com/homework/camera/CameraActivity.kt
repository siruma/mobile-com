package com.homework.camera

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.Recorder
import androidx.camera.video.Recording
import androidx.camera.video.VideoCapture
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * Camera activity class.
 */
class CameraActivity : ComponentActivity() {

  private var imageCapture: ImageCapture? = null
  private var videoCapture: VideoCapture<Recorder>? = null
  private var recording: Recording? = null

  private lateinit var cameraExecutor: ExecutorService

  private val activityResultLauncher = registerForActivityResult(
    ActivityResultContracts.RequestMultiplePermissions()
  ) { permissions ->
    // Handle Permission granted/rejected
    var permissionGranted = true
    permissions.entries.forEach {
      if (it.key in REQUIRED_PERMISSIONS && !it.value) {
        permissionGranted = false
      }
    }
    if (!permissionGranted) {
      Toast.makeText(
        baseContext,
        "Permission request denied",
        Toast.LENGTH_SHORT
      ).show()
    } else {
      startCameraView()
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    if (allPermissionGranted()) {
      startCameraView()
    } else {
      requestPermission()
    }

    cameraExecutor = Executors.newSingleThreadExecutor()
  }

  private fun startCameraView() {
    setContent {
      CameraPreview(startCamera = { previewView -> startCamera(previewView) },
        onImageCapture = { takePhoto() },
        onVideoCapture = { captureVideo() })

    }
  }

  private fun takePhoto() {
    // Get a stable reference of the modifiable image capture use case
    val imageCapture = imageCapture ?: return

    // Create time stamped name and MediaStore entry
    val name = SimpleDateFormat(FILENAME_FORMAT, Locale.UK)
      .format(System.currentTimeMillis())
    val contentValues = ContentValues().apply {
      put(MediaStore.MediaColumns.DISPLAY_NAME, name)
      put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
      put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Images")
    }

    // Create output options object which contains file + metadata
    val outputOption = ImageCapture.OutputFileOptions.Builder(
      contentResolver, MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
      contentValues
    ).build()

    // Set up image capture listener, which is triggered after photo has been taken
    imageCapture.takePicture(
      outputOption,
      ContextCompat.getMainExecutor(this),
      object : ImageCapture.OnImageSavedCallback {
        override fun onError(exception: ImageCaptureException) {
          Log.d(TAG, "Photo capture failed: ${exception.message}", exception)
        }

        override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
          val msg = "Photo capture succeeded: ${outputFileResults.savedUri}"
          Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
          Log.d(TAG, msg)
        }
      }
    )
  }

  private fun captureVideo() {
    TODO("Not yet implement")
  }

  private fun startCamera(previewView: PreviewView) {
    val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

    cameraProviderFuture.addListener({
      val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

      // Preview
      val preview = Preview.Builder()
        .build()
        .also {
          it.surfaceProvider = previewView.surfaceProvider
        }

      imageCapture = ImageCapture.Builder().build()

      val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

      try {
        // Unbind use cases before rebinding
        cameraProvider.unbindAll()

        // Bind use cases to camera
        cameraProvider.bindToLifecycle(
          this, cameraSelector, preview, imageCapture
        )
      } catch (exc: Exception) {
        Log.d(TAG, "Use case binding failed", exc)
      }
    }, ContextCompat.getMainExecutor(this))
  }

  private fun requestPermission() {
    activityResultLauncher.launch(REQUIRED_PERMISSIONS)
  }

  private fun allPermissionGranted() = REQUIRED_PERMISSIONS.all {
    ContextCompat.checkSelfPermission(
      baseContext, it
    ) == PackageManager.PERMISSION_GRANTED
  }

  override fun onDestroy() {
    super.onDestroy()
    cameraExecutor.shutdown()
  }


  companion object {
    private const val TAG = "CameraActivity"
    private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"

    private val REQUIRED_PERMISSIONS =
      mutableListOf(
        Manifest.permission.CAMERA,
        Manifest.permission.RECORD_AUDIO
      ).toTypedArray()
  }
}

@Composable
fun CameraPreview(
  startCamera: (PreviewView) -> Unit,
  onImageCapture: () -> Unit,
  onVideoCapture: () -> Unit,

  ) {
  val context = LocalContext.current
  val previewView = remember { PreviewView(context) }

  AndroidView(
    factory = { previewView },
    modifier = Modifier.fillMaxSize()
  ) {
    startCamera(previewView)
  }
  Column(
    modifier = Modifier.fillMaxSize(),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Bottom
  ) {
    Button(onClick = onImageCapture) {
      Text(text = "Capture Photo")
    }
    //Spacer(modifier = Modifier.height(16.dp))
    /*Button(onClick = onVideoCapture) {
      Text(text = "Capture Video")
    }*/
  }
}
