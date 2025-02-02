package com.homework

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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

