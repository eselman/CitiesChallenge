package com.eselman.cities.challenge.composables

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.eselman.cities.challenge.model.City
import com.eselman.cities.challenge.viewmodels.CitiesViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun CompactMapScreen(
    citiesViewModel: CitiesViewModel,
    navigateBackFromMap: () -> Unit
) {
    val selectedCity by citiesViewModel.selectedCity.collectAsState()
    CompactMapView(
        selectedCity = selectedCity,
        modifier = Modifier.fillMaxSize(),
        navigateBackFromMap = navigateBackFromMap,
    )
}

@Composable
fun CompactMapView(
    selectedCity: City?,
    modifier: Modifier = Modifier,
    navigateBackFromMap: () -> Unit
) {
    val cityLatLng = LatLng(
        selectedCity?.lat ?: 0.0,
        selectedCity?.lon ?: 0.0
    )
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            cityLatLng,
            15f
        )
    }

    Scaffold(
        topBar = {
            AppBar(
                title = "${selectedCity?.name}, ${selectedCity?.country}",
                navigationIcon = Icons.AutoMirrored.Default.ArrowBack,
                navigationIconClick = navigateBackFromMap
            )
        }
    ) { innerPadding ->
        GoogleMap(
            modifier = modifier.padding(innerPadding),
            cameraPositionState = cameraPositionState
        ) {
            Marker(
                state = MarkerState(
                    cityLatLng
                ),
                title = "${selectedCity?.name}, ${selectedCity?.country}"
            )
        }
    }
}
