package com.homework.ui.map

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo.api.Optional
import com.homework.AppDestinationsArgs
import com.homework.GetMaintenanceVehicleObservationsQuery
import com.homework.apolloClient
import com.homework.data.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

data class MapUiState(
  val vehicle:Int = 0,
  val vehicleLon: Double = 25.457969,
  val vehicleLat: Double = 65.019359,
  val timeStamp: String = "2025-01-01T00:00:00"
)

@HiltViewModel
class MapViewModel @Inject constructor(
  private val appRepository: AppRepository,
  savedStateHandle: SavedStateHandle
) : ViewModel() {

  private val userId: Int? = savedStateHandle[AppDestinationsArgs.ACCOUNT_ID_ARG]

  private var updateTime = 0L
  private val _mapState = MutableStateFlow(MapUiState())
  val mapState: StateFlow<MapUiState> = _mapState.asStateFlow()


  fun updateStatus(): Boolean {
    val time = Date().time
    return time - updateTime > TEN_MINUTES_IN_MILLS
  }

  /**
   * Get the info from server.
   */
  fun getInfo() {
    viewModelScope.launch {
      val now = Date()
      val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.UK)
      val formattedEndTime = sdf.format(now)
      val endTime = Optional.presentIfNotNull(formattedEndTime)

      val calendar = Calendar.getInstance()
      calendar.time = now
      calendar.add(Calendar.HOUR_OF_DAY, -TWENTY_FOUR_HOURS)
      val formattedStartTime = sdf.format(calendar.time)
      val startTime = Optional.presentIfNotNull(formattedStartTime)
      updateTime = now.time

      val response = apolloClient.query(
        GetMaintenanceVehicleObservationsQuery(begin = startTime, end = endTime)).execute()
      val data = response.data?.maintenanceVehicleObservations

      if (data.isNullOrEmpty() || data.contains(null)) {
        Log.d(TAG, "NO DATA" +
            " FROM QUERY: start: $formattedStartTime end: $formattedEndTime " +
            "Errors: ${response.errors}")
      } else {
        try {
          val observation = data[0]
          observation?.let {
            _mapState.update { state ->
              state.copy(
                vehicleLat = it.lat ?: state.vehicleLat,
                vehicleLon = it.lon ?: state.vehicleLon,
                vehicle = it.vehicleNumber,
                timeStamp = it.timestamp.toString()
              )
            }
          }
        } catch (e: Exception) {
          Log.d(TAG, "Get info error: $e")
        }
      }
    }
  }


  companion object {
    private const val TEN_MINUTES_IN_MILLS: Long = 10 * 60 * 1000
    private const val TWENTY_FOUR_HOURS = 24
    private const val TAG = "MapViewModel()"
  }
  
}