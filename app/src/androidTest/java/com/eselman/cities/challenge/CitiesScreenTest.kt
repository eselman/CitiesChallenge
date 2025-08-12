package com.eselman.cities.challenge

import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performScrollToIndex
import com.eselman.cities.challenge.screens.CitiesScreen
import com.eselman.cities.challenge.utils.AppConstants.CITIES_LIST_TAG
import com.eselman.cities.challenge.utils.AppConstants.CITIES_LOADING_TAG
import com.eselman.cities.challenge.utils.AppConstants.CITY_IS_FAVORITE_STAR_TAG
import com.eselman.cities.challenge.utils.AppConstants.CITY_IS_NOT_FAVORITE_STAR_TAG
import com.eselman.cities.challenge.utils.AppConstants.ERROR_MESSAGE_TAG
import com.eselman.cities.challenge.utils.AppConstants.ERROR_RETRY_BUTTON_TAG
import com.eselman.cities.challenge.utils.AppConstants.EXPANDED_MAP_VIEW_TAG
import com.eselman.cities.challenge.utils.AppConstants.FILTERING_PROGRESS_INDICATOR_TAG
import com.eselman.cities.challenge.utils.AppConstants.SEARCH_ONLY_FAVORITE_CHECKBOX_TAG
import com.eselman.cities.challenge.utils.AppConstants.SEARCH_TEXT_FIELD_TAG
import com.eselman.cities.challenge.utils.AppConstants.SEARCH_TEXT_ROW_TAG
import com.eselman.cities.challenge.utils.AppConstants.VIEW_DETAILS_BUTTON_TAG
import com.eselman.cities.challenge.utils.dummyCities
import com.eselman.cities.challenge.viewmodels.CitiesUIState
import com.eselman.cities.challenge.viewmodels.CitiesViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class CitiesScreenTest {

    @get: Rule(order = 1)
    var hitTestRule = HiltAndroidRule(this)

    @get: Rule(order = 2)
    var composeTestRule = createAndroidComposeRule<MainActivity>()

    private lateinit var citiesViewModel: CitiesViewModel

    @Before
    fun setup() {
        hitTestRule.inject()
        citiesViewModel = composeTestRule.activity.viewModels<CitiesViewModel>().value
    }

    @Test
    fun testCitiesScreenLoading() {
        citiesViewModel.citiesUIState.value = CitiesUIState.Loading

        with(composeTestRule) {
            activity.setContent {
                CitiesScreen(
                    citiesViewModel = citiesViewModel,
                    windowSizeClass = WindowWidthSizeClass.Compact,
                    goToMap = {},
                    goToDetails = {}
                )
            }
            onNodeWithTag(CITIES_LOADING_TAG).assertExists()
            onNodeWithTag(CITIES_LOADING_TAG).assertIsDisplayed()
            onNodeWithTag(CITIES_LIST_TAG).assertDoesNotExist()
        }
    }

    @Test
    fun testCitiesScreenError() {
        citiesViewModel.citiesUIState.value = CitiesUIState.Error("")

        with(composeTestRule) {
            activity.setContent {
                CitiesScreen(
                    citiesViewModel = citiesViewModel,
                    windowSizeClass = WindowWidthSizeClass.Compact,
                    goToMap = {},
                    goToDetails = {}
                )
            }
            onNodeWithTag(CITIES_LOADING_TAG).assertDoesNotExist()
            onNodeWithTag(CITIES_LIST_TAG).assertDoesNotExist()
            onNodeWithTag(ERROR_MESSAGE_TAG).assertExists()
            onNodeWithTag(ERROR_MESSAGE_TAG).assertIsDisplayed()
            onNodeWithTag(ERROR_RETRY_BUTTON_TAG).assertExists()
            onNodeWithTag(ERROR_RETRY_BUTTON_TAG).assertIsDisplayed()
        }
    }

    @Test
    fun testCitiesScreenExpandedDevice() {
        with(composeTestRule) {
            activity.setContent {
                CitiesScreen(
                    citiesViewModel = citiesViewModel,
                    windowSizeClass = WindowWidthSizeClass.Expanded,
                    goToMap = {},
                    goToDetails = {}
                )
            }

            val sortedDummyCities = dummyCities.sortedWith(compareBy({ it.name }, { it.country }))
            sortedDummyCities.sortedBy { it.name }.forEachIndexed { index, city ->
                onNodeWithTag(CITIES_LIST_TAG).performScrollToIndex(index)
                onNodeWithText("${city.name}, ${city.country}").assertIsDisplayed()
                onNodeWithText(
                    activity.getString(R.string.located_at, city.lat, city.lon)
                ).assertIsDisplayed()
                onNodeWithTag("$VIEW_DETAILS_BUTTON_TAG - ${city.id}")
                    .assertExists()
                if (city.isFavorite) {
                    onNodeWithTag("$CITY_IS_FAVORITE_STAR_TAG - ${city.id}", useUnmergedTree = true).assertExists()
                    onNodeWithTag("$CITY_IS_NOT_FAVORITE_STAR_TAG - ${city.id}", useUnmergedTree = true).assertDoesNotExist()
                } else {
                    onNodeWithTag("$CITY_IS_NOT_FAVORITE_STAR_TAG - ${city.id}", useUnmergedTree = true).assertExists()
                    onNodeWithTag("$CITY_IS_FAVORITE_STAR_TAG - ${city.id}", useUnmergedTree = true).assertDoesNotExist()
                }
                onNodeWithTag(EXPANDED_MAP_VIEW_TAG).assertExists()
            }
        }
    }

    @Test
    fun testCitiesScreenCompactDevice() {
        with(composeTestRule) {
            activity.setContent {
                CitiesScreen(
                    citiesViewModel = citiesViewModel,
                    windowSizeClass = WindowWidthSizeClass.Compact,
                    goToMap = {},
                    goToDetails = {}
                )
            }

            val sortedDummyCities = dummyCities.sortedWith(compareBy({ it.name }, { it.country }))
            sortedDummyCities.sortedBy { it.name }.forEachIndexed { index, city ->
                onNodeWithTag(CITIES_LIST_TAG).performScrollToIndex(index)
                onNodeWithText("${city.name}, ${city.country}").assertIsDisplayed()
                onNodeWithText(
                    activity.getString(R.string.located_at, city.lat, city.lon)
                ).assertIsDisplayed()
                onNodeWithTag("$VIEW_DETAILS_BUTTON_TAG - ${city.id}")
                    .assertExists()
                if (city.isFavorite) {
                    onNodeWithTag("$CITY_IS_FAVORITE_STAR_TAG - ${city.id}", useUnmergedTree = true).assertExists()
                    onNodeWithTag("$CITY_IS_NOT_FAVORITE_STAR_TAG - ${city.id}", useUnmergedTree = true).assertDoesNotExist()
                } else {
                    onNodeWithTag("$CITY_IS_NOT_FAVORITE_STAR_TAG - ${city.id}", useUnmergedTree = true).assertExists()
                    onNodeWithTag("$CITY_IS_FAVORITE_STAR_TAG - ${city.id}", useUnmergedTree = true).assertDoesNotExist()
                }
                onNodeWithTag(EXPANDED_MAP_VIEW_TAG).assertDoesNotExist()
            }
        }
    }

    @Test
    fun testCitiesScreenWhenIsFiltering() {
        citiesViewModel.isFiltering.value = true
        with(composeTestRule) {
            activity.setContent {
                CitiesScreen(
                    citiesViewModel = citiesViewModel,
                    windowSizeClass = WindowWidthSizeClass.Compact,
                    goToMap = {},
                    goToDetails = {}
                )
            }
            onNodeWithTag(CITIES_LOADING_TAG).assertDoesNotExist()
            onNodeWithTag(CITIES_LIST_TAG).assertDoesNotExist()
            onNodeWithTag(SEARCH_TEXT_ROW_TAG).assertExists()
            onNodeWithTag(SEARCH_TEXT_ROW_TAG).assertIsDisplayed()
            onNodeWithTag(SEARCH_TEXT_FIELD_TAG).assertExists()
            onNodeWithTag(SEARCH_TEXT_FIELD_TAG).assertIsDisplayed()
            onNodeWithTag(SEARCH_ONLY_FAVORITE_CHECKBOX_TAG).assertExists()
            onNodeWithTag(SEARCH_ONLY_FAVORITE_CHECKBOX_TAG).assertIsDisplayed()
            onNodeWithTag(FILTERING_PROGRESS_INDICATOR_TAG).assertExists()
            onNodeWithTag(FILTERING_PROGRESS_INDICATOR_TAG).assertIsDisplayed()
        }
    }

    @Test
    fun testCitiesScreenWhenIdNotFiltering() {
        with(composeTestRule) {
            activity.setContent {
                CitiesScreen(
                    citiesViewModel = citiesViewModel,
                    windowSizeClass = WindowWidthSizeClass.Compact,
                    goToMap = {},
                    goToDetails = {}
                )
            }
            onNodeWithTag(CITIES_LOADING_TAG).assertDoesNotExist()
            onNodeWithTag(CITIES_LIST_TAG).assertExists()
            onNodeWithTag(CITIES_LIST_TAG).assertIsDisplayed()
            onNodeWithTag(SEARCH_TEXT_ROW_TAG).assertExists()
            onNodeWithTag(SEARCH_TEXT_ROW_TAG).assertIsDisplayed()
            onNodeWithTag(SEARCH_TEXT_FIELD_TAG).assertExists()
            onNodeWithTag(SEARCH_TEXT_FIELD_TAG).assertIsDisplayed()
            onNodeWithTag(SEARCH_ONLY_FAVORITE_CHECKBOX_TAG).assertExists()
            onNodeWithTag(SEARCH_ONLY_FAVORITE_CHECKBOX_TAG).assertIsDisplayed()
            onNodeWithTag(FILTERING_PROGRESS_INDICATOR_TAG).assertDoesNotExist()
        }
    }
}
