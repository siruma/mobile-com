package com.homework.ui.account

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.homework.AppDestinationsArgs
import com.homework.R
import com.homework.data.AppRepository
import com.homework.model.AppUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class EditAccountUiState(
  val userId: Int? = null,
  val username: String = "",
  val userEmail: String = "",
  val userPicUrl: String = "",
  val isLoading: Boolean = true,
  val isAccountSaved: Boolean = false,
  val userMessage: Int? = null
)

@HiltViewModel
class AccountViewModel @Inject constructor(
  private val appRepository: AppRepository,
  savedStateHandle: SavedStateHandle
) : ViewModel() {

  private val accountId: Int? = savedStateHandle[AppDestinationsArgs.ACCOUNT_ID_ARG]

  private val _accountState = MutableStateFlow(EditAccountUiState())
  val accountState: StateFlow<EditAccountUiState> = _accountState.asStateFlow()


  init {
    if (accountId != null) {
      loadAccount(accountId)
    }
  }

  fun updateName(newName: String) {
    _accountState.update {
      it.copy(username = newName)
    }
  }

  fun updateEmail(newEmail: String) {
    _accountState.update {
      it.copy(userEmail = newEmail)
    }
  }

  fun updatePicUrl(newUrl: String) {
    _accountState.update {
      it.copy(userPicUrl = newUrl)
    }
  }

  fun loadAccount(accountId: Int) {
    _accountState.update {
      it.copy(isLoading = true)
    }
    viewModelScope.launch {
      appRepository.getAppUser(accountId).let { account ->
        if (account != null) {
          _accountState.update {
            it.copy(
              userId = account.userId,
              username = account.username,
              userEmail = account.userEmail,
              userPicUrl = account.imgSrc,
              isLoading = false
            )
          }
        } else {
          _accountState.update {
            Log.d(TAG,"NO USER FOUND")
            it.copy(isLoading = false)
          }
        }
      }
    }
  }

  fun saveAccount() {
    if (accountState.value.userId == null) {
      throw RuntimeException("SaveAccount() was called but no user ID was given.")
    }
    if (accountState.value.username.isEmpty()) {
      _accountState.update {
        it.copy(userMessage = R.string.empty_account_name)
      }
    } else if (accountState.value.userPicUrl.isEmpty()) {
      _accountState.update {
        it.copy(userMessage = R.string.empty_account_picture)
      }
    } else {
      viewModelScope.launch {
        appRepository.updateAppUser(
          AppUser(
            userId = accountState.value.userId!!,
            username = accountState.value.username,
            userEmail = accountState.value.userEmail,
            imgSrc = accountState.value.userPicUrl
          )
        )
        _accountState.update {
          it.copy(isAccountSaved = true)
        }
      }
    }
  }
}