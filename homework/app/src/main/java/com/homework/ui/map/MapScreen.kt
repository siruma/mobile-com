package com.homework.ui.map

import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.homework.map.MapViewActivity

/**
 * Map screen
 */
@Composable
fun MapScreen(
  accountId: Int,
  viewModel: MapViewModel = hiltViewModel()
) {
  val mapState by viewModel.mapState.collectAsStateWithLifecycle()
  val canUpdate = viewModel.updateStatus()
  val padding = Modifier.padding(all = 10.dp)

  Surface(modifier = Modifier.fillMaxSize()) {
    Column(modifier = Modifier.fillMaxSize()) {
      Row(modifier = padding) {
        Column {
          Button(onClick = { viewModel.getInfo() }, enabled = canUpdate) {
            Text(
              text = "Update vehicle data",
              color = MaterialTheme.colorScheme.inversePrimary,
              style = MaterialTheme.typography.titleMedium
            )
          }
        }
      }
      Row(modifier = padding) {
        Text(
          text = "Vehicle: ${mapState.vehicle}, longitude: ${mapState.vehicleLon}, latitude: ${mapState.vehicleLat}",
          color = MaterialTheme.colorScheme.primary,
          style = MaterialTheme.typography.titleMedium
        )
      }
      if (mapState.vehicle == 0) {
        Row(modifier = padding) {
          Text(
            text = "No new data",
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.titleMedium
          )
        }
      }

      Row(modifier = padding) {
        MapViewScreen(mapState)
      }
    }
  }
}

/**
 * Map View screen
 *
 * @param mapState UI state of map
 */
@Composable
fun MapViewScreen(mapState: MapUiState) {
  val context = LocalContext.current
  Button(onClick = {
    val intent = Intent(context, MapViewActivity::class.java).apply {
      putExtra("vehicle", mapState.vehicle)
      putExtra("latitude", mapState.vehicleLat)
      putExtra("longitude", mapState.vehicleLon)
      putExtra("timeStamp", mapState.timeStamp)
    }
    context.startActivity(intent)
  }) {
    Text("Open Map View")
  }
}
