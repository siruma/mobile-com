package com.homework.ui.settings

import android.Manifest
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
  Row(
    modifier = Modifier
      .padding(all = 10.dp)
  ) {
    Column {
      Button(onClick = {
        if (activity != null) {
          requestPermissions(activity, permission,0)
        }
      }) {
        Text(
          text = "Enable notifications",
          color = MaterialTheme.colorScheme.secondary,
          style = MaterialTheme.typography.titleMedium
        )
      }

    }
  }
}

private const val TAG = "SettingScreen()"
