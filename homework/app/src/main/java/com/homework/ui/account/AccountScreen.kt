package com.homework.ui.account


import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.homework.ui.components.AccountBox
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

/**
 * Account screen function.
 *
 * Show profile picture and account information.
 *
 * @param viewModel AccountViewModel
 * @param accountId ID of the main user
 */
@Composable
fun AccountScreen(
  viewModel: AccountViewModel = hiltViewModel(),
  accountId: Int
) {

  val edintingName = remember { mutableStateOf(false) }
  val context = LocalContext.current
  val pickPic = rememberLauncherForActivityResult(PickVisualMedia()) { image ->
    if (image != null) {
      Log.d("PhotoPicker", "Selected URI: ${image.encodedPath}")
      viewModel.updatePicUrl(saveProfilePicture(context, image))
    } else {
      Log.d("PhotoPicker", "No media selected")
    }
  }

  val accountState by viewModel.accountState.collectAsStateWithLifecycle()
  LaunchedEffect(accountId) {
    viewModel.loadAccount(accountId)
  }

  if (!accountState.isLoading) {
    Log.d(TAG, "Used account ID is ${accountState.userId}")
    AccountBox(
      name = accountState.username,
      email = accountState.userEmail,
      profilePictureUrl = accountState.userPicUrl
    )
    Box(
      modifier = Modifier.fillMaxSize(),
      contentAlignment = Alignment.Center
    ) {
      Column(horizontalAlignment = Alignment.CenterHorizontally) {
        if (edintingName.value) {
          Box(
            modifier = Modifier.size(200.dp, 100.dp)
          ) {
            EditUserName(
              name = accountState.username,
              onNameChange = viewModel::updateName
            )
          }
        }
        Button(onClick = { edintingName.value = !edintingName.value }) {
          if (edintingName.value) {
            Text("OK")
          } else {
            Text("Change User name")
          }
        }
        if (!edintingName.value) {
          Button(onClick = { pickPic.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly)) }) {
            Text("Change picture")
          }
          Button(onClick = {
            viewModel.saveAccount()// Update ViewModel & Database
            Log.d(
              "$TAG:SaveButton", "Account saved: name:${accountState.username} " +
                  "Email: ${accountState.userEmail}  UserPic: ${accountState.userPicUrl}"
            )
          }) {
            Text("Save")
          }
        }
      }
    }
  } else {
    CircularProgressIndicator()
  }
}

/**
 * Edit user name.
 *
 * @param name Username
 * @param onNameChange Function when name is change
 */
@Composable
fun EditUserName(
  name: String,
  onNameChange: (String) -> Unit
) {
  Column {
    val textFieldColors = OutlinedTextFieldDefaults.colors(
      focusedTextColor = Color.White,
      unfocusedBorderColor = Color.Transparent,
      cursorColor = MaterialTheme.colorScheme.onSurface
    )
    OutlinedTextField(
      value = name,
      modifier = Modifier.fillMaxWidth(),
      onValueChange = onNameChange,
      placeholder = {
        Text(
          text = name,
          style = MaterialTheme.typography.headlineSmall
        )
      },
      textStyle = MaterialTheme.typography.headlineSmall
        .copy(fontWeight = FontWeight.Bold),
      maxLines = 1,
      colors = textFieldColors
    )
  }
}

/**
 * Save profile picture to app picture director.
 *
 * @param context LocalContext
 * @param uri PhotoPicker URL
 */
private fun saveProfilePicture(context: Context, uri: Uri): String {
  val destinationFile = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
    "profile_picture.png")
  try {
    val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
    val outputStream: OutputStream = FileOutputStream(destinationFile)
    inputStream?.use { input ->
      outputStream.use { output ->
        val buffer = ByteArray(BUFFER_SIZE)
        var bytesRead: Int
        while (input.read(buffer).also { bytesRead = it } != -1) {
          output.write(buffer, 0, bytesRead)
        }
      }
    }
  } catch (e: IOException) {
    Log.d(TAG, "savePhotoPickerFile: $e")
    return ""
  }
  return destinationFile.path
}

private const val TAG = "AccountScreen"
const val BUFFER_SIZE = 1024
