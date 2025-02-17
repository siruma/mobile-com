package com.homework.sample

import com.homework.model.AppMessage

object SampleData {
  val conversationSample = listOf(
    AppMessage(
      0L,
      "2025-02-02T15:07:07+02:00",
      0,
      "Maki",
      "Welcome to Mobile Computing"
    ),
    AppMessage(
      0L,
      "2025-02-02T15:07:37+02:00",
      1,
      "Sora",
      "Hi Maki, great to be here!"
    ),
    AppMessage(
      0L,
      "2025-02-02T15:08:07+02:00",
      0,
      "Maki",
      "Today we’ll discuss mobile app development."
    ),
    AppMessage(
      0L,
      "2025-02-02T15:08:37+02:00",
      1,
      "Sora",
      "Sounds exciting! What tools will we use?"
    ),
    AppMessage(
      0L,
      "2025-02-02T15:09:07+02:00",
      0,
      "Maki",
      "We’ll focus on Android Studio and Kotlin."
    ),
    AppMessage(
      0L,
      "2025-02-02T15:10:07+02:00",
      0,
      "Maki",
      "Mobile app development involves several stages:\n- Designing the user interface\n" +
          "- Writing the code\n- Testing the app\n- Publishing it to app stores"
    ),
    AppMessage(
      0L,
      "2025-02-02T15:11:07+02:00",
      1,
      "Sora",
      "That’s a lot of steps! Can we explore them one by one?\n" +
          "I’d like to understand the design phase better first."
    ),
    AppMessage(
      0L,
      "2025-02-02T15:12:07+02:00",
      0,
      "Maki",
      "Of course! Design is crucial as it sets the tone for the user experience.\n" +
          "We’ll start with wireframes and prototypes before diving into actual development."
    )
  )
}
