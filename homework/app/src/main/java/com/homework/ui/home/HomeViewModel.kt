package com.homework.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.homework.AppDestinationsArgs
import com.homework.data.AppRepository
import com.homework.model.AppMessage
import com.homework.model.AppUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
  val userId: Int? = null,
  val userName: String = "",
  val userPicUrl: String = "",
  val isLoading: Boolean = true
)

@HiltViewModel
class HomeViewModel @Inject constructor(
  private val appRepository: AppRepository,
  savedStateHandle: SavedStateHandle
) : ViewModel() {

  private val userId: Int? = savedStateHandle[AppDestinationsArgs.ACCOUNT_ID_ARG]

  val allMessages: LiveData<List<AppMessage>> = appRepository.allMessages.asLiveData()
  val allAppUser: LiveData<List<AppUser>> = appRepository.allUser.asLiveData()

  private val _uiState = MutableStateFlow(HomeUiState())
  val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

  init {
    if (userId != null) {
      loadAccount(userId)
    }
  }

  /**
   * Load the main user.
   *
   * @param accountId User ID
   */
  fun loadAccount(accountId: Int) {
    _uiState.update {
      it.copy(isLoading = true)
    }
    viewModelScope.launch {
      appRepository.getAppUser(accountId).let { account ->
        if (account != null) {
          _uiState.update {
            it.copy(
              userId = account.userId,
              userName = account.username,
              userPicUrl = account.imgSrc,
              isLoading = false
            )
          }
        } else {
          _uiState.update {
            Log.d(TAG, "NO USER FOUND")
            it.copy(isLoading = false)
          }
        }
      }
    }
  }

  companion object {
    private const val TAG = "HomeViewModel()"
  }
}
