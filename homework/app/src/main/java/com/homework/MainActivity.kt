package com.homework

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.homework.ui.components.AppTabRow
import com.homework.ui.theme.HomeworkTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * Main activity for the homework app
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

  private val accountId = 0

  private var foregroundSensorService: ForegroundSensorService? = null
  private var foregroundOnlyServiceBound = false

  private var foregroundOnlyServiceConnection = object : ServiceConnection {
    override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
      val binder = p1 as ForegroundSensorService.LocalBinder
      foregroundSensorService = binder.sensorDataService
      foregroundOnlyServiceBound = true
      foregroundSensorService?.startSensorDataService()
    }

    override fun onServiceDisconnected(p0: ComponentName?) {
      foregroundSensorService = null
      foregroundOnlyServiceBound = false
    }
  }

  override fun onStart() {
    super.onStart()

    val serviceIntent = Intent(this, ForegroundSensorService::class.java)
    bindService(serviceIntent, foregroundOnlyServiceConnection, BIND_AUTO_CREATE)
  }

  override fun onStop() {
    if (foregroundOnlyServiceBound) {
      unbindService(foregroundOnlyServiceConnection)
      foregroundOnlyServiceBound = false
    }
    super.onStop()
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContent {
      MainApp()
    }
  }

  @Composable
  fun MainApp() {
    HomeworkTheme {
      val navController = rememberNavController()
      val currentBackStack by navController.currentBackStackEntryAsState()
      // Fetch app current destination
      val currentDestination = currentBackStack?.destination
      // Change the variable to current screen. If returns null, uses Account screen
      val currentScreen = appTabRowScreens.find { it.route == currentDestination?.route } ?: Account

      Scaffold(
        topBar = {
          AppTabRow(
            allScreens = appTabRowScreens,
            onTabSelected = { screen ->
              navController
                .navigateSingleTopTo(screen.route)
            },
            currentScreen = currentScreen
          )
        }
      ) { innerPadding ->
        NavHost(
          navController = navController,
          startDestination = Home.route,
          modifier = Modifier.padding(innerPadding)
        ) {
          composable(route = Home.route) {
            Home.screen(accountId)
          }
          composable(route = Account.route) {
            Account.screen(accountId)
          }
          composable(route = Settings.route) {
            Settings.screen(accountId)
          }
        }
      }
    }
  }
  companion object {
    private const val TAG = "MainActivity()"
  }
}

fun NavHostController.navigateSingleTopTo(route: String) =
  this.navigate(route) {
    popUpTo(
      this@navigateSingleTopTo.graph.findStartDestination().id
    ) {
      saveState = true
    }
    launchSingleTop = true
    restoreState = true
  }

