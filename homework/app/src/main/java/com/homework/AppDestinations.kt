package com.homework

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.homework.ui.account.AccountScreen
import com.homework.ui.home.HomeScreen
import com.homework.ui.settings.SettingsScreen

interface AppDestination {
  val icon: ImageVector
  val route: String
  val screen: @Composable () -> Unit
}

object Home : AppDestination {
  override val icon = Icons.Filled.Home
  override val route = "Home"
  override val screen: @Composable () -> Unit = { HomeScreen() }
}

object Account : AppDestination {
  override val icon = Icons.Filled.AccountCircle
  override val route = "Account"
  override val screen: @Composable () -> Unit = { AccountScreen() }
}

object Settings : AppDestination {
  override val icon = Icons.Filled.Settings
  override val route = "Settings"
  override val screen: @Composable () -> Unit = { SettingsScreen() }
}

// Selections of screens to be displayed
val appTabRowScreens = listOf(Home, Account, Settings)
