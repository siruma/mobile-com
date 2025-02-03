package com.homework.ui.components

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.homework.R

/**
 * Common account information box.
 *
 * @param modifier The modifier to be applied to the layout.
 * @param name User name
 * @param email User email
 * @param profilePictureUrl User profile picture URL
 */
@Composable
fun AccountBox(
  modifier: Modifier = Modifier,
  name: String,
  email: String,
  profilePictureUrl: String
) {
  val context = LocalContext.current
  val imageModifier = Modifier
    .size(180.dp)
    .clip(CircleShape)
    .border(1.5.dp, MaterialTheme.colorScheme.primary, CircleShape)

  Box(
    modifier = Modifier.fillMaxSize(),
    contentAlignment = Alignment.TopCenter
  ) {
    Column(
      modifier = modifier
    ) {
      if (profilePictureUrl.isNotEmpty()) {
        Image(
          painter = rememberAsyncImagePainter(
            ImageRequest.Builder(context)
              .data(Uri.parse(profilePictureUrl))
              .error(R.drawable.dalle_2025_01_15__japan_game)
              .build()
          ),
          contentDescription = "Profile picture",
          modifier = imageModifier
        )
      } else {
        Image(
          painter = painterResource(R.drawable.dalle_2025_01_15__japan_game),
          contentDescription = "Profile picture",
          modifier = imageModifier
        )
      }
      Spacer(modifier = Modifier.height(8.dp))
      Column {
        Row {
          Icon(
            imageVector = Icons.Filled.Person,
            contentDescription = "name"
          )
          Spacer(Modifier.width(12.dp))
          Text(
            text = name,
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.titleLarge
          )
        }
        Row {
          Icon(
            imageVector = Icons.Filled.Email,
            contentDescription = "email"
          )
          Spacer(Modifier.width(12.dp))
          Text(
            text = email,
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.titleLarge
          )
        }
      }
    }
  }
}
