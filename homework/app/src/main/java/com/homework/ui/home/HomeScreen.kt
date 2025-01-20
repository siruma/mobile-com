package com.homework.ui.home

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.homework.R
import com.homework.data.Message
import com.homework.data.UserData
import com.homework.sample.SampleData

/**
 * Home Screen function.
 *
 * Add greeting message and conversation.
 */
@Composable
fun HomeScreen(name: String? = UserData.accounts.first().name) {
  Surface(modifier = Modifier.fillMaxSize()) {
    Column(
      modifier = Modifier.fillMaxSize(),
    ) {
      Greeting(name)
      Conversation(SampleData.conversationSample)
    }
  }
}

@Composable
private fun Greeting(name: String?) {
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

@Composable
private fun MessageCard(msg: Message) {
  Row(
    modifier = Modifier
      .padding(all = 10.dp)
  ) {
    Image(
      painter = painterResource(R.drawable.dalle_2025_01_15__japan_game),
      contentDescription = "Game picture",
      modifier = Modifier
        .size(80.dp)
        .clip(CircleShape)
        .border(1.5.dp, MaterialTheme.colorScheme.primary, CircleShape)
    )
    Spacer(modifier = Modifier.width(8.dp))

    // Variable tracking if message expanded
    var isExpanded by remember { mutableStateOf(false) }
    val surfaceColor by animateColorAsState(
      if (isExpanded) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
    )

    // Toggle the isExpanded when clicked
    Column(modifier = Modifier.clickable { isExpanded = !isExpanded }) {
      Text(
        text = msg.author,
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

@Composable
private fun Conversation(messages: List<Message>) {
  LazyColumn {
    items(messages) { message ->
      MessageCard(message)
    }
  }
}
