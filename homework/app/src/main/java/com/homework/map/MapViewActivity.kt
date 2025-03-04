package com.homework.map

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.homework.R

/**
 * Map view activity.
 */
class MapViewActivity: AppCompatActivity(), OnMapReadyCallback {

  private lateinit var mMapView: MapView

  private var latitude = 0.0
  private var longitude = 0.0
  private var vehicle = 0

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.raw_mapview)

    var mapViewBundle: Bundle? = null
    if (savedInstanceState != null) {
      mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY)
    }
    mMapView = findViewById(R.id.map)
    mMapView.onCreate(mapViewBundle)
    mMapView.getMapAsync(this)

    vehicle = intent.getIntExtra("vehicle", 0)
    latitude = intent.getDoubleExtra("latitude", 0.0)
    longitude = intent.getDoubleExtra("longitude", 0.0)

  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)

    val mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY) ?: Bundle().apply {
     putBundle(MAPVIEW_BUNDLE_KEY, this)
    }

    mMapView.onSaveInstanceState(mapViewBundle)
  }

  override fun onResume() {
    super.onResume()
    mMapView.onResume()
  }

  override fun onStart() {
    super.onStart()
    mMapView.onStart()
  }

  override fun onStop() {
    super.onStop()
    mMapView.onStop()
  }

  override fun onMapReady(map: GoogleMap) {
    Log.d(TAG, "Map is ready")
    map.moveCamera(CameraUpdateFactory.newLatLngZoom(
      LatLng(OULU_LATITUDE, OULU_LONGITUDE), MAP_ZOOM))

    val markerOptions = MarkerOptions().position(
      LatLng(latitude, longitude)
    ).title("Vehicle: $vehicle")

    val marker = map.addMarker(markerOptions)
    if (marker != null) {
      Log.d(TAG, "Marker added at latitude: $latitude, longitude: $longitude")
    } else {
      Log.e(TAG, "Failed to add marker")
    }
  }

  override fun onPause() {
    mMapView.onPause()
    super.onPause()
  }

  override fun onDestroy() {
    super.onDestroy()
    mMapView.onDestroy()
  }

  companion object {
    private const val TAG = "MapViewActivity()"
    private const val MAPVIEW_BUNDLE_KEY = "MapViewBundleKey"
    private const val OULU_LATITUDE = 65.019359
    private const val OULU_LONGITUDE = 25.457969
    private const val MAP_ZOOM = 10f
  }
}