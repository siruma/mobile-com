package com.homework.ui.account

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.homework.data.UserData
import com.homework.ui.components.AccountBox

/**
 * Account screen function.
 *
 * Show profile picture and account information.
 */
@Composable
fun AccountScreen(
  accountType: String? = UserData.accounts.first().name
) {
  val account = remember(accountType) { UserData.getAccount(accountType) }
  AccountBox(
    name = account.name,
    email = account.email,
    profilePicture = account.profilePicture
  )
}
