import com.android.build.api.dsl.Packaging

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt.android)
}

android {
    namespace = "com.eselman.cities.challenge"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.eselman.cities.challenge"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "com.eselman.cities.challenge.app.HiltTestRunner"

        val citiesApiUrl: String = findProperty("CITIES_URL") as String? ?: ""
        buildConfigField("String", "CITIES_API_URL", "\"$citiesApiUrl\"")

        packaging {
            resources {
                excludes.add("META-INF/LICENSE.md")
                excludes.add("META-INF/LICENSE-notice.md")
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
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    // Hilt
    implementation(libs.hilt)
    implementation(libs.hilt.android.testing)
    implementation(libs.androidx.runner)
    ksp(libs.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    // Room
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.gson.converter)

    // DataStore
    implementation(libs.androidx.datastore.preferences)

    // WindowSizeClass
    implementation(libs.androidx.material3.window.size)

    // Maps
    implementation(libs.maps.compose)

    // Coil
    implementation(libs.coil.compose)

    testImplementation("androidx.test:core") {
        version { strictly("1.6.1") }
    }
    testImplementation(libs.junit)

    testImplementation(libs.mockk)

    testImplementation(libs.kotlinx.coroutines.test)

    testImplementation(libs.turbine)

    androidTestImplementation("androidx.test:core") {
        version { strictly("1.6.1") }
    }
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom)){
        exclude(group = "androix.test", module = "core")
    }
    androidTestImplementation(libs.androidx.ui.test.junit4)

    // Hilt Android Test
    androidTestImplementation(libs.hilt.android.testing) {
        exclude(group = "androix.test", module = "core")
    }
    testImplementation(libs.hilt.android.testing) {
        exclude(group = "androix.test", module = "core")
    }
    kspAndroidTest(libs.hilt.compiler)

    androidTestImplementation(libs.mockk.android)

    debugImplementation("androidx.test:core") {
        version { strictly("1.6.1") }
    }
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}