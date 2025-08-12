package com.eselman.cities.challenge.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.eselman.cities.challenge.R
import com.eselman.cities.challenge.composables.AppBar
import com.eselman.cities.challenge.composables.CitiesListView
import com.eselman.cities.challenge.model.City
import com.eselman.cities.challenge.ui.theme.Pink80
import com.eselman.cities.challenge.utils.AppConstants.CITIES_LOADING_TAG
import com.eselman.cities.challenge.utils.dummyCities
import com.eselman.cities.challenge.viewmodels.CitiesUIState
import com.eselman.cities.challenge.viewmodels.CitiesViewModel

@Composable
fun CitiesScreen(
    citiesViewModel: CitiesViewModel,
    windowSizeClass: WindowWidthSizeClass,
    goToMap: () -> Unit,
    goToDetails: (cityId: Long) -> Unit
) {
    val citiesUIState by citiesViewModel.citiesUIState.collectAsState()

    val isFiltering by citiesViewModel.isFiltering.collectAsState()

    val storeSearchText by citiesViewModel.searchText.collectAsState()

    val storeOnlyFavoriteCities by citiesViewModel.onlyFavoriteCities.collectAsState()

    val selectedCity by citiesViewModel.selectedCity.collectAsState()

    CitiesView(
        citiesUIState = citiesUIState,
        windowSizeClass = windowSizeClass,
        isFiltering = isFiltering,
        storeSearchText = storeSearchText,
        storeOnlyFavoriteCities = storeOnlyFavoriteCities,
        selectedCity = selectedCity,
        onErrorRetry = { citiesViewModel.getCities() },
        goToMap = goToMap,
        goToDetails = goToDetails,
        updateSelectedCity = { city -> citiesViewModel.updateSelectedCity(city) },
        updateCityIsFavorite = { city -> citiesViewModel.updateCityIsFavorite(city) },
        updateSearchText = { searchText -> citiesViewModel.updateSearchText(searchText) },
        updateOnlyFavoriteCities = { onlyFavoriteCities ->
            citiesViewModel.updateOnlyFavoriteCities(
                onlyFavoriteCities
            )
        },
        filterCities = { searchText, onlyFavoriteCities ->
            citiesViewModel.filterCities(
                searchText,
                onlyFavoriteCities
            )
        }
    )
}

@Composable
fun CitiesView(
    citiesUIState: CitiesUIState,
    windowSizeClass: WindowWidthSizeClass,
    isFiltering: Boolean,
    storeSearchText: String,
    storeOnlyFavoriteCities: Boolean,
    selectedCity: City?,
    onErrorRetry: () -> Unit,
    goToMap: () -> Unit,
    goToDetails: (cityId: Long) -> Unit,
    updateSelectedCity: (city: City) -> Unit,
    updateCityIsFavorite: (city: City) -> Unit,
    updateSearchText: (searchText: String) -> Unit,
    updateOnlyFavoriteCities: (onlyFavoriteCities: Boolean) -> Unit,
    filterCities: (searchText: String, onlyFavoriteCities: Boolean) -> Unit,
) {
    Scaffold(
        topBar = {
            AppBar(
                title = stringResource(R.string.cities_screen_title)
            ) {}
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            when (citiesUIState) {
                is CitiesUIState.Loading -> {
                    CitiesLoadingView(
                        Modifier
                            .fillMaxSize()
                            .align(Alignment.CenterHorizontally)
                            .testTag(CITIES_LOADING_TAG)
                    )
                }

                is CitiesUIState.Error -> {
                    ErrorScreen(onErrorRetry)
                }

                is CitiesUIState.Success -> {
                    CitiesListView(
                        cities = citiesUIState.cities,
                        isFiltering = isFiltering,
                        storeSearchText = storeSearchText,
                        storeOnlyFavoriteCities = storeOnlyFavoriteCities,
                        selectedCity = selectedCity,
                        windowSizeClass = windowSizeClass,
                        goToMap = goToMap,
                        goToDetails = goToDetails,
                        updateSelectedCity = updateSelectedCity,
                        updateCityIsFavorite = updateCityIsFavorite,
                        updateSearchText = updateSearchText,
                        updateOnlyFavoriteCities = updateOnlyFavoriteCities,
                        filterCities = filterCities,
                    )
                }
            }
        }
    }
}

@Composable
fun CitiesLoadingView(modifier: Modifier) {
    Box(
        modifier = modifier
    ) {
        CircularProgressIndicator(
            color = Pink80,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Preview
@Composable
fun CitiesViewLoadingPreview() {
    CitiesView(
        citiesUIState = CitiesUIState.Loading,
        windowSizeClass = WindowWidthSizeClass.Compact,
        isFiltering = false,
        storeSearchText = "",
        storeOnlyFavoriteCities = false,
        selectedCity = null,
        onErrorRetry = {},
        goToMap = {},
        goToDetails = {},
        updateSelectedCity = {},
        updateCityIsFavorite = {},
        updateSearchText = {},
        updateOnlyFavoriteCities = {},
        filterCities = { _, _ -> }
    )
}

@Preview
@Composable
fun CitiesViewErrorPreview() {
    CitiesView(
        citiesUIState = CitiesUIState.Error(""),
        windowSizeClass = WindowWidthSizeClass.Compact,
        isFiltering = false,
        storeSearchText = "",
        storeOnlyFavoriteCities = false,
        selectedCity = null,
        onErrorRetry = {},
        goToMap = {},
        goToDetails = {},
        updateSelectedCity = {},
        updateCityIsFavorite = {},
        updateSearchText = {},
        updateOnlyFavoriteCities = {},
        filterCities = { _, _ -> }
    )
}

@Preview
@Composable
fun CitiesViewSuccessPreview() {
    CitiesView(
        citiesUIState = CitiesUIState.Success(dummyCities),
        windowSizeClass = WindowWidthSizeClass.Compact,
        isFiltering = false,
        storeSearchText = "",
        storeOnlyFavoriteCities = false,
        selectedCity = dummyCities[0],
        onErrorRetry = {},
        goToMap = {},
        goToDetails = {},
        updateSelectedCity = {},
        updateCityIsFavorite = {},
        updateSearchText = {},
        updateOnlyFavoriteCities = {},
        filterCities = { _, _ -> }
    )
}
