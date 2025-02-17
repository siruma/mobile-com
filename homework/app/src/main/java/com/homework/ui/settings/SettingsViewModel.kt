package com.homework.ui.settings

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.homework.AppDestinationsArgs
import com.homework.data.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class SettingsUiState(
  val userName: String = ""
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
  private val appRepository: AppRepository,
  savedStateHandle: SavedStateHandle
): ViewModel() {

  private val userId: Int? = savedStateHandle[AppDestinationsArgs.ACCOUNT_ID_ARG]

  private val _uiState = MutableStateFlow(SettingsUiState())
  val uiState : StateFlow<SettingsUiState> = _uiState.asStateFlow()

  companion object {
    private const val TAG = "SettingsViewModel()"
  }
}
