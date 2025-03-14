package com.homework

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.homework.ui.account.AccountScreen
import com.homework.ui.camera.CameraScreen
import com.homework.ui.home.HomeScreen
import com.homework.ui.map.MapScreen
import com.homework.ui.settings.SettingsScreen

interface AppDestination {

  val icon: ImageVector
  val route: String
  val screen: @Composable (accountId: Int) -> Unit
}

object Home : AppDestination {
  override val icon = Icons.Filled.Home
  override val route = "Home"
  override val screen: @Composable (Int) -> Unit = { accountId ->
    HomeScreen(
      accountId = accountId) }
}

object Account : AppDestination {
  override val icon = Icons.Filled.AccountCircle
  override val route = "Account"
  override val screen: @Composable (Int) -> Unit = { accountId ->
    AccountScreen(
      accountId = accountId
    ) }
}

object Settings : AppDestination {
  override val icon = Icons.Filled.Settings
  override val route = "Settings"
  override val screen: @Composable (Int) -> Unit = { accountId ->
    SettingsScreen(
      accountId = accountId
    ) }
}

object Map : AppDestination {
  override val icon = Icons.Filled.Place
  override val route = "Map"
  override val screen: @Composable (Int) -> Unit = {accountId ->
    MapScreen(
      accountId = accountId
    )
  }
}

object Camera : AppDestination {
  override val icon = Icons.Filled.Face
  override val route = "Camera"
  override val screen: @Composable (accountId: Int) -> Unit = { accountId ->
    CameraScreen(
      accountId = accountId
    )
  }
}

// Selections of screens to be displayed
val appTabRowScreens = listOf(Home, Account, Map, Camera, Settings)

object AppDestinationsArgs {
  const val HOME_ARG = "home"
  const val ACCOUNT_ID_ARG = "accountID"
  const val SETTINGS_ARG = "setting"
}
