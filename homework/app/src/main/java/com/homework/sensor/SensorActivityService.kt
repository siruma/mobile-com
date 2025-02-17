package com.homework.sensor

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import kotlin.math.roundToInt

/**
 * Sensor Activity Service
 *
 * Handles crating, measurement and stopping of sensor
 */
class SensorActivityService : SensorEventListener {

  private lateinit var measurementName: String

  // Sensor manager
  private lateinit var measurementSensorManager: SensorManager
  private var measurementSensor: Sensor? = null

  private var measurementValues: IntArray = IntArray(MEASUREMENT_SIZE)

  /**
   * Crate the sensor if available.
   *
   * @param sensorManager Sensor service
   * @param type Type of sensor
   * @param name Name of sensor
   */
  fun onCreate(sensorManager: SensorManager, type: Int, name: String): SensorActivityService? {
    measurementName = name
    measurementSensorManager = sensorManager
    try {
      measurementSensor = measurementSensorManager.getDefaultSensor(type)
    } catch (e: NullPointerException) {
      Log.d(TAG, "$name sensor not found.")
      return null
    }
    Log.d(TAG, "$name sensor found.")
    return this
  }

  /**
   * Start the measurement from sensor.
   *
   * @param delay
   */
  fun startMeasurement(delay: Int) {
    val sensorRegister = measurementSensorManager.registerListener(
      this, measurementSensor, delay
    )
    Log.d(TAG, "$measurementName sensor register: " + (if (sensorRegister) "YES" else "NO"))
  }

  /**
   * Stop the measurement.
   */
  fun stopMeasurement() {
    measurementSensorManager.unregisterListener(this)
  }

  /**
   * Gets the value from sensor if it change.
   */
  override fun onSensorChanged(event: SensorEvent) {
    if (measurementName == "LIGHT") {
      measurementValues[0] = event.values[0].roundToInt()
    } else {
      measurementValues = IntArray(MEASUREMENT_SIZE) { event.values[it].roundToInt() }
    }
  }

  /**
   * Handles accuracy changes.
   */
  override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    when (accuracy) {
      SensorManager.SENSOR_STATUS_ACCURACY_HIGH -> {
        // Handle high accuracy
      }

      SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM -> {
        // Handle medium accuracy
      }

      SensorManager.SENSOR_STATUS_ACCURACY_LOW -> {
        // Handle low accuracy
      }

      SensorManager.SENSOR_STATUS_UNRELIABLE -> {
        // Handle unreliable accuracy
      }
    }
  }

  fun onPause() {
    measurementSensorManager.unregisterListener(this)
  }

  /**
   * Getter for measurement.
   */
  fun getMeasurementRate(): IntArray {
    return measurementValues
  }

  companion object {
    private const val TAG = "SENSOR SERVICE:"
    private const val MEASUREMENT_SIZE = 3
  }
}
