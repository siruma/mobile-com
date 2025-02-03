package com.homework.testUtils

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

/**
 * Extension function to get the value from LiveData or wait for it to be available.
 *
 * This function is useful in unit tests where you need to get the value from LiveData
 * synchronously. It uses a CountDownLatch to wait for the LiveData to emit a value.
 *
 * This function was generated using AI tools to improve efficiency.
 *
 * @receiver LiveData<T> The LiveData instance from which to get the value.
 * @return T The value emitted by the LiveData.
 * @throws TimeoutException If the current thread is interrupted while waiting.
 */
fun <T> LiveData<T>.getOrAwaitValue(
  time: Long = 2,
  timeUnit: TimeUnit = TimeUnit.SECONDS,
  afterObserve: () -> Unit = {}
): T {
  var data: T? = null
  val countDownLatch = CountDownLatch(1)
  val observer = object : Observer<T> {
    override fun onChanged(value: T) {
      data = value
      countDownLatch.countDown()
      this@getOrAwaitValue.removeObserver(this)
    }
  }
  this.observeForever(observer)

  try {
    afterObserve.invoke()
    if (!countDownLatch.await(time, timeUnit)) {
      throw TimeoutException("LiveData value was never set.")
    }
  } finally {
    this.removeObserver(observer)
  }
  return data ?: throw KotlinNullPointerException("LiveData value was null")
}
