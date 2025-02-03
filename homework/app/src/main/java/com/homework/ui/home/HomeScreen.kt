package com.homework.ui.home

import android.net.Uri
import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.homework.R
import com.homework.model.AppMessage
import com.homework.model.AppUser

/**
 * Home Screen function.
 *
 * Add greeting message and conversation.
 */
@Composable
fun HomeScreen(
  accountId: Int,
  viewModel: HomeViewModel = hiltViewModel()
) {
  val lifecycleOwner = LocalLifecycleOwner.current
  val conversations = remember { mutableListOf<AppMessage>() }
  val allUser = remember { mutableListOf<AppUser>() }
  val isLoadingConversation = remember { mutableStateOf(true) }
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  LaunchedEffect(accountId) {
    //Load main user
    viewModel.loadAccount(accountId)
  }
  //Load messages
  viewModel.allMessages.observe(lifecycleOwner) { messages ->
    messages.let {
      conversations.clear()
      conversations.addAll(it)
      isLoadingConversation.value = false
    }
  }
  //Load users
  viewModel.allAppUser.observe(lifecycleOwner) { users ->
    users.let {
      allUser.clear()
      allUser.addAll(it)
    }
  }

  if (uiState.isLoading) {
    CircularProgressIndicator()
  } else if (uiState.userId == null) {
    Box(
      contentAlignment = Alignment.Center
    ) {
      Text(text = stringResource(R.string.no_user_found))
    }
  } else {
    Log.d(
      TAG,
      "Used account ID is ${uiState.userId}"
    )
    Surface(modifier = Modifier.fillMaxSize()) {
      Column(
        modifier = Modifier.fillMaxSize(),
      ) {
        Greeting(uiState.userName)
        if (isLoadingConversation.value) {
          CircularProgressIndicator()
        } else {
          Conversation(conversations, allUser)
        }
      }
    }
  }

}

/**
 * Add Greeting text.
 */
@Composable
private fun Greeting(name: String = "User") {
  Row(
    modifier = Modifier
      .padding(all = 10.dp)
  ) {
    Column {
      Text(
        text = "Welcome to Japan Game, $name",
        color = MaterialTheme.colorScheme.secondary,
        style = MaterialTheme.typography.titleMedium
      )
    }
  }
}

/**
 * Conversation iterator.
 *
 * @param messages List of app messages
 * @param users List of app users
 */
@Composable
private fun Conversation(messages: List<AppMessage>, users: List<AppUser>) {
  LazyColumn {
    items(messages) { message ->
      val user = users[message.authorId]
      MessageCard(message, user)
    }
  }
}

/**
 * Message card.
 *
 * @param msg message
 * @param user App user
 */
@Composable
private fun MessageCard(msg: AppMessage, user: AppUser) {
  Row(
    modifier = Modifier
      .padding(all = 10.dp)
  ) {
    ProfilePicture(user.imgSrc)
    Spacer(modifier = Modifier.width(8.dp))

    // Variable tracking if message expanded
    var isExpanded by remember { mutableStateOf(false) }
    val surfaceColor by animateColorAsState(
      if (isExpanded) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
    )

    // Toggle the isExpanded when clicked
    Column(modifier = Modifier.clickable { isExpanded = !isExpanded }) {
      Text(
        text = user.username,
        color = MaterialTheme.colorScheme.secondary,
        style = MaterialTheme.typography.titleSmall
      )
      Spacer(modifier = Modifier.height(4.dp))

      Surface(
        shape = MaterialTheme.shapes.medium,
        shadowElevation = 1.dp,
        color = surfaceColor,
        modifier = Modifier
          .animateContentSize()
          .padding(1.dp)
      ) {
        Text(
          text = msg.body,
          modifier = Modifier.padding(all = 4.dp),
          // Shows only first line if not expanded
          maxLines = if (isExpanded) Int.MAX_VALUE else 1,
          style = MaterialTheme.typography.bodyMedium
        )
      }
    }
  }
}

/**
 * Draw the profile picture.
 */
@Composable
private fun ProfilePicture(imgUrl: String) {
  val context = LocalContext.current
  val modifier = Modifier
    .size(80.dp)
    .clip(CircleShape)
    .border(1.5.dp, MaterialTheme.colorScheme.primary, CircleShape)

  if (imgUrl.isNotEmpty()) {
    AsyncImage(
      model = ImageRequest.Builder(context)
        .data(Uri.parse(imgUrl))
        .error(R.drawable.dalle_2025_01_15__japan_game)
        .build(),
      contentDescription = "Profile picture",
      modifier = modifier
    )
  } else {
    Image(
      painter = painterResource(R.drawable.dalle_2025_01_15__japan_game),
      contentDescription = "Game picture",
      modifier = modifier
    )
  }
}

const val TAG = "HomeScreen"
