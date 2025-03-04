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
import java.util.Date
import java.util.Locale
import javax.inject.Inject

data class MapUiState(
  val vehicle:Int = 0,
  val vehicleLon: Double = 25.457969,
  val vehicleLat: Double = 65.019359
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
      val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.UK)
      val formattedDate = "\"${sdf.format(now)}\""
      val optionalGraphQLDateTime = Optional.presentIfNotNull(formattedDate)
      updateTime = now.time

      val response = apolloClient.query(
        GetMaintenanceVehicleObservationsQuery(optionalGraphQLDateTime)).execute()
      val data: List<GetMaintenanceVehicleObservationsQuery.MaintenanceVehicleObservation?>? =
        response.data?.maintenanceVehicleObservations

      if (data.isNullOrEmpty() || data.contains(null)) {
        Log.d(TAG, "NO DATA FROM QUERY")
      } else {
        try {
          val observation = data[0]
          observation?.let {
            _mapState.update { state ->
              state.copy(
                vehicleLat = it.lat ?: state.vehicleLat,
                vehicleLon = it.lon ?: state.vehicleLon,
                vehicle = it.vehicleNumber
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
    private const val TAG = "MapViewModel()"
  }
  
}