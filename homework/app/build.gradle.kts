plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.kotlin.android)
  alias(libs.plugins.kotlin.compose)
  alias(libs.plugins.apollo)
  id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
  kotlin("plugin.serialization") version "2.0.21"
  id("com.google.dagger.hilt.android")
  id("com.google.devtools.ksp")
}

android {
  namespace = "com.homework"
  compileSdk = 35

  defaultConfig {
    applicationId = "com.homework"
    minSdk = 34
    targetSdk = 35
    versionCode = 1
    versionName = "1.0"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

    javaCompileOptions {
      annotationProcessorOptions {
        arguments["room.incremental"] = "true"
      }
    }
  }

  buildTypes {
    release {
      isMinifyEnabled = false
      proguardFiles(
        getDefaultProguardFile("proguard-android-optimize.txt"),
        "proguard-rules.pro"
      )
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }
  kotlinOptions {
    jvmTarget = "11"
  }
  buildFeatures {
    compose = true
  }
}


dependencies {

  // Implementations
  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.lifecycle.runtime.ktx)
  implementation(libs.androidx.lifecycle.service)
  implementation(libs.androidx.activity)
  implementation(libs.androidx.activity.compose)
  implementation(platform(libs.androidx.compose.bom))
  implementation(libs.androidx.ui)
  implementation(libs.androidx.ui.graphics)
  implementation(libs.androidx.ui.tooling.preview)
  implementation(libs.androidx.material)
  implementation(libs.androidx.material3)
  implementation(libs.androidx.navigation.compose)
  implementation(libs.androidx.navigation.fragment)
  implementation(libs.androidx.navigation.ui)
  implementation(libs.androidx.navigation.dynamic.features.fragment)
  implementation(libs.androidx.room.runtime)
  implementation(libs.androidx.room.ktx)
  implementation(libs.coil.compose)
  implementation(libs.javax.inject)
  implementation(libs.androidx.work.runtime)

  //CameraX
  implementation(libs.androidx.camera.core)
  implementation(libs.androidx.camera.camera2)
  implementation(libs.androidx.camera.lifecycle)
  implementation(libs.androidx.camera.video)
  implementation(libs.androidx.camera.view)
  implementation(libs.androidx.camera.extensions)

  //Apollo
  implementation(libs.apollo.runtime)

  // DataStore used to save simple data.
  implementation(libs.androidx.datastore.preferences)

  // Hilt
  implementation(libs.hilt.android)
  implementation(libs.androidx.hilt.work)
  implementation(libs.androidx.hilt.navigation.compose)
  ksp(libs.hilt.compiler)

  ksp(libs.androidx.room.compiler)

  // Maps SDK for Android
  implementation(libs.play.services.maps)

  // Testing Implementation
  testImplementation(libs.junit)
  testImplementation(libs.androidx.room.testing)
  androidTestImplementation(libs.androidx.navigation.testing)
  androidTestImplementation(libs.androidx.junit)
  androidTestImplementation(libs.androidx.espresso.core)
  androidTestImplementation(platform(libs.androidx.compose.bom))
  androidTestImplementation(libs.androidx.ui.test.junit4)
  androidTestImplementation(libs.androidx.core.testing)

  //Debug Implementation
  debugImplementation(libs.androidx.ui.tooling)
  debugImplementation(libs.androidx.ui.test.manifest)

  // JSON serialization library
  implementation(libs.kotlinx.serialization.json)

  // For local unit tests
  testImplementation(libs.hilt.android.testing)
  kspTest(libs.hilt.compiler)
  // AndroidX Test - Hilt testing
  androidTestImplementation(libs.hilt.android.testing)
  kspAndroidTest(libs.hilt.compiler)
}

apollo {

  service("service") {

    packageName.set("com.homework")
    introspection {
      endpointUrl.set("https://api.oulunliikenne.fi/proxy/graphql")
      schemaFile.set(file("src/main/graphql/schema.graphqls"))

    }
  }
}

secrets {
  // To add your Maps API key to this project:
  // 1. If the secrets.properties file does not exist, create it in the same folder as the local.properties file.
  // 2. Add this line, where YOUR_API_KEY is your API key:
  //        MAPS_API_KEY=YOUR_API_KEY
  propertiesFileName = "secrets.properties"

  // A properties file containing default secret values. This file can be
  // checked in version control.
  defaultPropertiesFileName = "local.defaults.properties"
}