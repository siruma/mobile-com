package com.homework.sample

import com.homework.data.Message

object SampleData {
  val conversationSample = listOf(
    Message(
      "Maki", "Welcome to Mobile Computing"
    ),
    Message(
      "Sora", "Hi Maki, great to be here!"
    ),
    Message(
      "Maki", "Today we’ll discuss mobile app development."
    ),
    Message(
      "Sora", "Sounds exciting! What tools will we use?"
    ),
    Message(
      "Maki", "We’ll focus on Android Studio and Kotlin."
    ),
    Message(
      "Maki",
      "Mobile app development involves several stages:\n- Designing the user interface\n" +
          "- Writing the code\n- Testing the app\n- Publishing it to app stores"
    ),
    Message(
      "Sora",
      "That’s a lot of steps! Can we explore them one by one?\n" +
          "I’d like to understand the design phase better first."
    ),
    Message(
      "Maki",
      "Of course! Design is crucial as it sets the tone for the user experience.\n" +
          "We’ll start with wireframes and prototypes before diving into actual development."
    )
  )
}
