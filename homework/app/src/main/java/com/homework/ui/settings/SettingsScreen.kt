package com.homework.ui.settings

import android.Manifest
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.hilt.navigation.compose.hiltViewModel

/**
 * Settings screen function.
 * Add app settings
 *
 * @param accountId User ID
 * @param viewModel SettingsViewModel
 */
@Composable
fun SettingsScreen(
  accountId: Int,
  viewModel: SettingsViewModel = hiltViewModel()
) {
  Surface(modifier = Modifier.fillMaxSize()) {
    Column(
      modifier = Modifier.fillMaxSize(),
    ) {
      Greeting()
    }
  }
}

/**
 * Add setting greeting.
 */
@Composable
private fun Greeting() {
  val activity = LocalActivity.current
  val permission: Array<String> = arrayOf(Manifest.permission.POST_NOTIFICATIONS)
  Column(
    modifier = Modifier.fillMaxSize(),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
  ) {
    Button(onClick = {
      if (activity != null) {
        requestPermissions(activity, permission, 0)
      }
    }) {
      Text(
        text = "Enable notifications",
        color = MaterialTheme.colorScheme.inversePrimary,
        style = MaterialTheme.typography.titleMedium
      )
    }

  }
}

private const val TAG = "SettingScreen()"
