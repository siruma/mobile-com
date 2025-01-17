package com.homework.data

import androidx.compose.runtime.Immutable
import com.homework.R

@Immutable
data class Account(
  val name: String,
  val email: String,
  val profilePicture: Int
)

object UserData {
  val accounts : List<Account> = listOf(
    Account(
      "Maki",
      "maki@japan.game",
      R.drawable.dalle_2025_01_15__japan_game
    )
  )

  fun getAccount(accountName: String?): Account {
    return accounts.first { it.name == accountName}
  }
}

/**
 * Conversion message data.
 */
@Immutable
data class Message(val author: String, val body: String)
