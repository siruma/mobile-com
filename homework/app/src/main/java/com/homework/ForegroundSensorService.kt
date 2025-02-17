package com.homework

import android.content.Intent
import android.content.res.Configuration
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Binder
import android.os.IBinder
import android.util.Log
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.homework.data.AppRepository
import com.homework.sensor.SensorActivityService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@AndroidEntryPoint
class ForegroundSensorService: LifecycleService() {

  @Inject lateinit var appRepository: AppRepository

  private lateinit var gyroRateSensor: SensorActivityService
  private lateinit var lightSensor: SensorActivityService

  private var sensorDataActive = false
  private val localBinder = LocalBinder()
  private var serviceRunForeground = false
  private var confChange = false
  private var dataFromSensorsJob: Job? = null
  private var lightSensorValue = 0

  override fun onCreate() {
    super.onCreate()
    Log.d(TAG, "ON CRATE")
    appRepository.activeSensorDataFlow.asLiveData().observe(this) { active ->
      if (sensorDataActive != active) {
        sensorDataActive = active
      }
    }

    lightSensor = SensorActivityService().onCreate(
      getSystemService(SENSOR_SERVICE) as SensorManager,
      Sensor.TYPE_LIGHT,
      "LIGHT"
    )!!

    gyroRateSensor = SensorActivityService().onCreate(
      getSystemService(SENSOR_SERVICE) as SensorManager,
      Sensor.TYPE_GYROSCOPE,
      "GYROSCOPE"
    )!!
  }

  override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
    super.onStartCommand(intent, flags, startId)
    Log.d(TAG, "ON START COMMAND")
    val cancelSensorData =
      intent?.getBooleanExtra(EXTRA_CANCEL_SENSOR_FROM_NOTIFICATION, false) ?: false
    if (cancelSensorData) {
      stopSensorDataService(true)
    }
    //Stop the system start a new service after it's been killed
    return START_NOT_STICKY
  }

  override fun onBind(intent: Intent): IBinder {
    super.onBind(intent)
    Log.d(TAG,"ON BIND")
    notForegroundSevice()
    return localBinder
  }

  override fun onRebind(intent: Intent?) {
    super.onRebind(intent)
    Log.d(TAG,"ON REBIND")
    notForegroundSevice()
  }

  override fun onUnbind(intent: Intent?): Boolean {
     super.onUnbind(intent)
    Log.d(TAG,"ON UNBIND")
    if (!confChange && sensorDataActive) {

      serviceRunForeground = true
    }
    return true
  }

  override fun onConfigurationChanged(newConfig: Configuration) {
    super.onConfigurationChanged(newConfig)
    Log.d(TAG,"ON CONFIGURATION CHANGED")
    confChange = true
  }

  fun startSensorDataService() {
    Log.d(TAG, "START SENSOR DATA SERVICE")
    setActiveSensorDataService(true)
    gyroRateSensor.startMeasurement(SENSOR_DELAY)
    lightSensor.startMeasurement(SENSOR_DELAY)
    startService(Intent(applicationContext, ForegroundSensorService::class.java))
    dataFromSensorsJob = lifecycleScope.launch {
      readSensorData()
    }
  }

  private suspend fun readSensorData() {
    while (true) {
      var intArray: IntArray
      gyroRateSensor.let {
        intArray = it.getMeasurementRate()
        appRepository.setGyroRate(intArray)
      }
      lightSensor.let {
        intArray = it.getMeasurementRate()
        appRepository.setSteps(intArray[0])
      }
      val timeStamp = Date().time
      appRepository.setSensorTimeStamp(timeStamp)
      delay(THREE_SECONDS_MILLISECONDS)
      if (lightSensorValue != intArray[0]) {
        lightSensorValue = intArray[0]
        val body = "Light Measurement: ${lightSensorValue}"
        appRepository.updateNotification(1, body, timeStamp)
      }
    }
  }

  private fun notForegroundSevice() {
    serviceRunForeground = false
    confChange = false
  }

  private fun stopSensorDataService() {
    stopSensorDataService(false)
  }

  private fun stopSensorDataService(stop: Boolean){
    gyroRateSensor.stopMeasurement()
    lightSensor.stopMeasurement()
    lifecycleScope.launch {
      val job: Job = setActiveSensorDataService(false)
      if (stop) {
        //wait until DataStore data is saved
        job.join()
        stopSelf()
      }
    }
  }

  private fun setActiveSensorDataService(b: Boolean): Job {
    return lifecycleScope.launch { appRepository.setActiveSensorDataService(b) }
  }

  inner class LocalBinder : Binder() {
    internal val sensorDataService: ForegroundSensorService
      get() = this@ForegroundSensorService
  }

  companion object {
    private const val SENSOR_DELAY = 1000000
    private const val THREE_SECONDS_MILLISECONDS = 3000L
    private const val PACKAGE_NAME = "com.homework"
    private const val EXTRA_CANCEL_SENSOR_FROM_NOTIFICATION =
      "$PACKAGE_NAME.extra.CANCEL_SUBSCRIPTION_FROM_NOTIFICATION"

    private const val TAG = "ForegroundSensorService()"
  }
}
