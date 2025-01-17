package com.homework.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.ripple
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.homework.AppDestination
import java.util.Locale

@Composable
fun AppTabRow(
  allScreens: List<AppDestination>,
  onTabSelected: (AppDestination) -> Unit,
  currentScreen: AppDestination
) {
  Surface(
    modifier = Modifier
      .height(TabHeight)
      .fillMaxWidth()
  ) {
    Row(Modifier
      .selectableGroup()) {
      allScreens.forEach { screen ->
        AppTab(
          text = screen.route,
          icon = screen.icon,
          onSelected = { onTabSelected(screen) },
          selected = currentScreen == screen
        )
      }
    }
  }
}

@Composable
private fun AppTab(
  text: String,
  icon: ImageVector,
  onSelected: () -> Unit,
  selected: Boolean
) {
  val color = MaterialTheme.colorScheme.primary
  val durationMillis = if (selected) TAB_FADE_IN_DURATION else TAB_FADE_OUT_DURATION
  val animSpec = remember {
    tween<Color>(
      durationMillis = durationMillis,
      easing = LinearEasing,
      delayMillis = TAB_FADE_IN_DELAY
    )
  }
  val tabTintColor by animateColorAsState(
    targetValue = if (selected) color else color.copy(alpha = INACTIVE_TAP_ICON),
    animationSpec = animSpec
  )

  Row(
    modifier = Modifier
      .padding(16.dp)
      .animateContentSize()
      .height(TabHeight)
      .selectable(
        selected = selected,
        onClick = onSelected,
        role = Role.Tab,
        interactionSource = remember { MutableInteractionSource() },
        indication = ripple(
          bounded = false,
          radius = Dp.Unspecified,
          color = Color.Unspecified
        )
      )
      .clearAndSetSemantics { contentDescription = text }
  ) {
    Icon(
      imageVector = icon,
      contentDescription = text,
      tint = tabTintColor
    )
    if (selected) {
      Spacer(Modifier.width(12.dp))
      Text(text.uppercase(Locale.getDefault()), color = tabTintColor)
    }
  }
}

private val TabHeight = 56.dp

private const val INACTIVE_TAP_ICON = 0.06f
private const val TAB_FADE_IN_DURATION = 150
private const val TAB_FADE_OUT_DURATION = 100
private const val TAB_FADE_IN_DELAY = 100
