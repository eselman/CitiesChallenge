package com.eselman.cities.challenge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.eselman.cities.challenge.composables.CompactMapScreen
import com.eselman.cities.challenge.screens.CitiesScreen
import com.eselman.cities.challenge.screens.CityDetailsScreen
import com.eselman.cities.challenge.ui.theme.CitiesChallengeTheme
import com.eselman.cities.challenge.viewmodels.CitiesViewModel
import dagger.hilt.android.AndroidEntryPoint

sealed class Destination(val route: String) {
    data object CityDetails : Destination("city_details/{cityId}") {
        fun createRoute(cityId: Long?) = "city_details/$cityId"
    }

    data object CitiesList : Destination("cities_list")
    data object MapView : Destination("map")
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CitiesChallengeTheme {
                CitiesChallengeApp(
                    calculateWindowSizeClass(this),
                )
            }
        }
    }
}

@Composable
fun CitiesChallengeApp(
    windowsSizeClass: WindowSizeClass,
) {
    val navController = rememberNavController()
    val citiesViewModel = hiltViewModel<CitiesViewModel>()

    NavHost(
        navController = navController,
        startDestination = Destination.CitiesList.route,
    ) {
        composable(Destination.CitiesList.route) {
            CitiesScreen(
                citiesViewModel = citiesViewModel,
                windowSizeClass = windowsSizeClass.widthSizeClass,
                goToMap = {
                    navController.navigate(Destination.MapView.route)
                },
                goToDetails = { cityId ->
                    navController.navigate(Destination.CityDetails.createRoute(cityId))
                }
            )
        }

        composable(Destination.MapView.route) {
            CompactMapScreen(
                citiesViewModel = citiesViewModel,
                navigateBackFromMap = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Destination.CityDetails.route,
            arguments = listOf(navArgument("cityId") {
                type = NavType.LongType
            })
        ) {
            CityDetailsScreen(
                cityDetailsViewModel = hiltViewModel(),
                windowSizeClass = windowsSizeClass.widthSizeClass,
                navigateBackFromDetails = {
                    navController.popBackStack()
                },
                onMapImageClicked = {
                    navController.navigate(Destination.MapView.route)
                }
            )
        }
    }
}
