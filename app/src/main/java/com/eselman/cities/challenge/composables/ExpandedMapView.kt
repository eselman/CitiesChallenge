package com.eselman.cities.challenge.composables

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.eselman.cities.challenge.model.City
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch

@Composable
fun ExpandedMapView(
    selectedCity: City?,
    modifier: Modifier = Modifier
) {
    val cityLatLng = LatLng(
        selectedCity?.lat ?: 0.0,
        selectedCity?.lon ?: 0.0
    )

    val cameraPositionState = rememberCameraPositionState(
        key = selectedCity?.id.toString()
    ) {
        position = CameraPosition.fromLatLngZoom(
            cityLatLng,
            15f
        )
    }

    val scope = rememberCoroutineScope()
    LaunchedEffect(cityLatLng) {
        scope.launch {
            cameraPositionState.animate(
                update = CameraUpdateFactory.newLatLng(cityLatLng),
                durationMs = 1000
            )
        }
    }

    GoogleMap(
        modifier = modifier,
        cameraPositionState = cameraPositionState
    ) {
        SimpleMarker(
            cityLatLng,
            "${selectedCity?.name}, ${selectedCity?.country}"
        )
    }
}

@Composable
fun SimpleMarker(
    position: LatLng,
    title: String
) {
    val state = rememberUpdatedMarkerState(position)
    Marker(
        state = state,
        title = title
    )
}

@Composable
private fun rememberUpdatedMarkerState(newPosition: LatLng): MarkerState =
    remember { MarkerState(position = newPosition) }
        .apply { position = newPosition }
