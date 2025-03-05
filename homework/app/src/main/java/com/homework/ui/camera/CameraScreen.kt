package com.homework.ui.camera

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.homework.camera.CameraActivity

/**
 * Camera screen.
 */
@Composable
fun CameraScreen(
  accountId: Int
) {
  val context = LocalContext.current
  Surface(modifier = Modifier.fillMaxSize()) {
    Column(
      modifier = Modifier.fillMaxSize(),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center
    ) {
      Button(onClick = {
        val intent = Intent(context, CameraActivity::class.java)
        context.startActivity(intent)
      }) {
        Text("Open camera")
      }
    }
  }
}