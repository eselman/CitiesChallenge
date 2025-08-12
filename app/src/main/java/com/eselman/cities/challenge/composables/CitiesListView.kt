package com.eselman.cities.challenge.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eselman.cities.challenge.R
import com.eselman.cities.challenge.model.City
import com.eselman.cities.challenge.ui.theme.Pink80
import com.eselman.cities.challenge.utils.AppConstants.CITIES_LIST_TAG
import com.eselman.cities.challenge.utils.AppConstants.EXPANDED_MAP_VIEW_TAG
import com.eselman.cities.challenge.utils.AppConstants.FILTERING_PROGRESS_INDICATOR_TAG
import com.eselman.cities.challenge.utils.dummyCities

@Composable
fun CitiesListView(
    cities: List<City>?,
    isFiltering: Boolean,
    storeSearchText: String,
    storeOnlyFavoriteCities: Boolean,
    selectedCity: City?,
    windowSizeClass: WindowWidthSizeClass,
    goToMap: () -> Unit,
    goToDetails: (cityId: Long) -> Unit,
    updateSelectedCity: (city: City) -> Unit,
    updateCityIsFavorite: (city: City) -> Unit,
    updateSearchText: (searchText: String) -> Unit,
    updateOnlyFavoriteCities: (onlyFavoriteCities: Boolean) -> Unit,
    filterCities: (searchText: String, onlyFavoriteCities: Boolean) -> Unit,
) {
    if (cities?.isNotEmpty() == true) {
        when (windowSizeClass) {
            WindowWidthSizeClass.Compact,
            WindowWidthSizeClass.Medium -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    SearchTextRow(
                        storeSearchText = storeSearchText,
                        storeOnlyFavoriteCities = storeOnlyFavoriteCities,
                        updateSearchText = updateSearchText,
                        updateOnlyFavoriteCities = updateOnlyFavoriteCities,
                        filterCities = filterCities,
                    )
                    if (isFiltering) {
                        FilteringProgressIndicator()
                    } else {
                        CitiesList(
                            cities = cities,
                            selectedCity = selectedCity,
                            shouldNavigateToMap = true,
                            goToMap = goToMap,
                            goToDetails = goToDetails,
                            updateSelectedCity = updateSelectedCity,
                            updateCityIsFavorite = updateCityIsFavorite,
                        )
                    }
                }
            }

            WindowWidthSizeClass.Expanded -> {
                Row(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Column(
                        modifier = Modifier
                            .weight(0.5f)
                    ) {
                        SearchTextRow(
                            storeSearchText = storeSearchText,
                            storeOnlyFavoriteCities = storeOnlyFavoriteCities,
                            updateSearchText = updateSearchText,
                            updateOnlyFavoriteCities = updateOnlyFavoriteCities,
                            filterCities = filterCities,
                        )
                        if (isFiltering) {
                            FilteringProgressIndicator()
                        } else {
                            CitiesList(
                                cities = cities,
                                selectedCity = selectedCity,
                                shouldNavigateToMap = false,
                                goToMap = goToMap,
                                goToDetails = goToDetails,
                                updateSelectedCity = updateSelectedCity,
                                updateCityIsFavorite = updateCityIsFavorite,
                            )
                        }
                    }
                    if (!isFiltering) {
                        ExpandedMapView(
                            selectedCity,
                            Modifier
                                .weight(0.5f)
                                .testTag(EXPANDED_MAP_VIEW_TAG)
                        )
                    }
                }
            }
        }
    } else {
        Column(modifier = Modifier.fillMaxSize()) {
            SearchTextRow(
                storeSearchText = storeSearchText,
                storeOnlyFavoriteCities = storeOnlyFavoriteCities,
                updateSearchText = updateSearchText,
                updateOnlyFavoriteCities = updateOnlyFavoriteCities,
                filterCities = filterCities,
            )

            if (isFiltering) {
                FilteringProgressIndicator()
            } else {
                Text(
                    text = stringResource(R.string.cities_empty_list),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}

@Composable
fun CitiesList(
    cities: List<City>,
    selectedCity: City?,
    shouldNavigateToMap: Boolean,
    goToMap: () -> Unit,
    goToDetails: (cityId: Long) -> Unit,
    updateSelectedCity: (city: City) -> Unit,
    updateCityIsFavorite: (city: City) -> Unit,
) {
    LazyColumn(
        contentPadding = PaddingValues(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.testTag(CITIES_LIST_TAG)
    ) {
        items(
            items = cities,
            key = { city -> city.id }
        ) { city ->
            CityCard(
                city,
                selectedCity,
                shouldNavigateToMap,
                goToMap,
                goToDetails,
                updateSelectedCity,
                updateCityIsFavorite
            )
        }
    }
}

@Composable
fun FilteringProgressIndicator() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .testTag(FILTERING_PROGRESS_INDICATOR_TAG)
    ) {
        LinearProgressIndicator(
            color = Pink80,
            trackColor = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.CenterHorizontally)
        )
        Text(
            text = stringResource(R.string.filtering),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}

@Preview
@Composable
fun CitiesListViewCompactPreview() {
    CitiesListView(
        cities = dummyCities,
        isFiltering = false,
        storeSearchText = "",
        storeOnlyFavoriteCities = false,
        selectedCity = dummyCities[0],
        windowSizeClass = WindowWidthSizeClass.Compact,
        goToMap = {},
        goToDetails = {},
        updateSelectedCity = {},
        updateCityIsFavorite = {},
        updateSearchText = {},
        updateOnlyFavoriteCities = {},
        filterCities = { _, _ -> },
    )
}

@Preview
@Composable
fun CitiesListViewCompactIsFilteringPreview() {
    CitiesListView(
        cities = dummyCities,
        isFiltering = true,
        storeSearchText = "abc",
        storeOnlyFavoriteCities = true,
        selectedCity = dummyCities[0],
        windowSizeClass = WindowWidthSizeClass.Compact,
        goToMap = {},
        goToDetails = {},
        updateSelectedCity = {},
        updateCityIsFavorite = {},
        updateSearchText = {},
        updateOnlyFavoriteCities = {},
        filterCities = { _, _ -> },
    )
}

@Preview(device = "spec:width=640dp,height=360dp,dpi=420")
@Composable
fun CitiesListViewExpandedPreview() {
    CitiesListView(
        cities = dummyCities,
        isFiltering = false,
        storeSearchText = "",
        storeOnlyFavoriteCities = false,
        selectedCity = dummyCities[0],
        windowSizeClass = WindowWidthSizeClass.Expanded,
        goToMap = {},
        goToDetails = {},
        updateSelectedCity = {},
        updateCityIsFavorite = {},
        updateSearchText = {},
        updateOnlyFavoriteCities = {},
        filterCities = { _, _ -> },
    )
}

@Preview(device = "spec:width=640dp,height=360dp,dpi=420")
@Composable
fun CitiesListViewExpandedIsFilteringPreview() {
    CitiesListView(
        cities = dummyCities,
        isFiltering = true,
        storeSearchText = "abc",
        storeOnlyFavoriteCities = true,
        selectedCity = dummyCities[0],
        windowSizeClass = WindowWidthSizeClass.Compact,
        goToMap = {},
        goToDetails = {},
        updateSelectedCity = {},
        updateCityIsFavorite = {},
        updateSearchText = {},
        updateOnlyFavoriteCities = {},
        filterCities = { _, _ -> },
    )
}
