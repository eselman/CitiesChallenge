package com.eselman.cities.challenge

import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.eselman.cities.challenge.screens.CityDetailsScreen
import com.eselman.cities.challenge.utils.AppConstants.CITY_COORDINATES_TAG
import com.eselman.cities.challenge.utils.AppConstants.CITY_COUNTRY_TAG
import com.eselman.cities.challenge.utils.AppConstants.CITY_MAP_IMAGE_TAG
import com.eselman.cities.challenge.utils.AppConstants.CITY_NAME_TAG
import com.eselman.cities.challenge.utils.dummyCities
import com.eselman.cities.challenge.viewmodels.CityDetailsViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class CityDetailsScreenTest {

    @get: Rule(order = 1)
    var hitTestRule = HiltAndroidRule(this)

    @get: Rule(order = 2)
    var composeTestRule = createAndroidComposeRule<MainActivity>()

    private lateinit var cityDetailsViewModel: CityDetailsViewModel

    @Before
    fun setup() {
        hitTestRule.inject()
        cityDetailsViewModel = composeTestRule.activity.viewModels<CityDetailsViewModel>().value
        cityDetailsViewModel.getCityById(dummyCities[0].id)
    }

    @Test
    fun testCityDetailsScreenCompact() {
        with(composeTestRule) {
            activity.setContent {
                CityDetailsScreen(
                    cityDetailsViewModel = cityDetailsViewModel,
                    windowSizeClass = WindowWidthSizeClass.Compact,
                    navigateBackFromDetails = { },
                    onMapImageClicked = { }
                )
            }

            onNodeWithTag(CITY_NAME_TAG).assertExists()
            onNodeWithTag(CITY_NAME_TAG).assertIsDisplayed()
            onNodeWithTag(CITY_COUNTRY_TAG).assertExists()
            onNodeWithTag(CITY_COUNTRY_TAG).assertIsDisplayed()
            onNodeWithTag(CITY_COORDINATES_TAG).assertExists()
            onNodeWithTag(CITY_COORDINATES_TAG).assertIsDisplayed()
            onNodeWithTag(CITY_MAP_IMAGE_TAG).assertExists()
            onNodeWithTag(CITY_MAP_IMAGE_TAG).assertIsDisplayed()
        }
    }

    @Test
    fun testCityDetailsScreenExpanded() {
        with(composeTestRule) {
            activity.setContent {
                CityDetailsScreen(
                    cityDetailsViewModel = cityDetailsViewModel,
                    windowSizeClass = WindowWidthSizeClass.Expanded,
                    navigateBackFromDetails = { },
                    onMapImageClicked = { }
                )
            }

            onNodeWithTag(CITY_NAME_TAG).assertExists()
            onNodeWithTag(CITY_NAME_TAG).assertIsDisplayed()
            onNodeWithTag(CITY_COUNTRY_TAG).assertExists()
            onNodeWithTag(CITY_COUNTRY_TAG).assertIsDisplayed()
            onNodeWithTag(CITY_COORDINATES_TAG).assertExists()
            onNodeWithTag(CITY_COORDINATES_TAG).assertIsDisplayed()
            onNodeWithTag(CITY_MAP_IMAGE_TAG).assertExists()
            onNodeWithTag(CITY_MAP_IMAGE_TAG).assertIsDisplayed()
        }
    }
}
