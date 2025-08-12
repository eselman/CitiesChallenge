package com.eselman.cities.challenge.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.eselman.cities.challenge.composables.AppBar
import com.eselman.cities.challenge.composables.CompactDetailsScreen
import com.eselman.cities.challenge.composables.ExpandedDetailsScreen
import com.eselman.cities.challenge.model.City
import com.eselman.cities.challenge.ui.theme.Pink80
import com.eselman.cities.challenge.utils.dummyCities
import com.eselman.cities.challenge.viewmodels.CityDetailsViewModel


@Composable
fun CityDetailsScreen(
    cityDetailsViewModel: CityDetailsViewModel,
    windowSizeClass: WindowWidthSizeClass,
    navigateBackFromDetails: () -> Unit,
    onMapImageClicked: () -> Unit,
) {
    val selectedCity by cityDetailsViewModel.city.collectAsState()

    CityDetailsView(
        selectedCity = selectedCity,
        windowSizeClass = windowSizeClass,
        navigateBackFromDetails = navigateBackFromDetails,
        onMapImageClicked = onMapImageClicked,
    )
}

@Composable
fun CityDetailsView(
    selectedCity: City?,
    windowSizeClass: WindowWidthSizeClass,
    navigateBackFromDetails: () -> Unit,
    onMapImageClicked: () -> Unit = {},
) {
    Scaffold(
        topBar = {
            AppBar(
                title = "${selectedCity?.name}, ${selectedCity?.country}",
                navigationIcon = Icons.AutoMirrored.Default.ArrowBack,
                navigationIconClick = navigateBackFromDetails
            )
        }
    ) { innerPadding ->
        Surface(
            color = Pink80,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (windowSizeClass) {
                WindowWidthSizeClass.Compact,
                WindowWidthSizeClass.Medium -> {
                    CompactDetailsScreen(
                        city = selectedCity,
                        onMapImageClicked = onMapImageClicked,
                    )
                }

                WindowWidthSizeClass.Expanded -> {
                    ExpandedDetailsScreen(
                        city = selectedCity,
                        onMapImageClicked = onMapImageClicked,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun CityDetailsViewCompactPreview() {
    CityDetailsView(
        selectedCity = dummyCities[0],
        windowSizeClass = WindowWidthSizeClass.Compact,
        navigateBackFromDetails = {},
        onMapImageClicked = {},
    )
}

@Preview(device = "spec:width=640dp,height=360dp,dpi=420")
@Composable
fun CityDetailsViewExpandedPreview() {
    CityDetailsView(
        selectedCity = dummyCities[0],
        windowSizeClass = WindowWidthSizeClass.Expanded,
        navigateBackFromDetails = {},
        onMapImageClicked = {},
    )
}
