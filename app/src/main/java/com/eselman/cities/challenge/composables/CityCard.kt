package com.eselman.cities.challenge.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eselman.cities.challenge.R
import com.eselman.cities.challenge.model.City
import com.eselman.cities.challenge.ui.theme.Pink80
import com.eselman.cities.challenge.ui.theme.PurpleGrey80
import com.eselman.cities.challenge.utils.AppConstants.CITY_IS_FAVORITE_STAR_TAG
import com.eselman.cities.challenge.utils.AppConstants.CITY_IS_NOT_FAVORITE_STAR_TAG
import com.eselman.cities.challenge.utils.AppConstants.VIEW_DETAILS_BUTTON_TAG
import com.eselman.cities.challenge.utils.dummyCities

@Composable
fun CityCard(
    city: City,
    selectedCity: City?,
    shouldNavigateToMap: Boolean,
    goToMap: () -> Unit,
    goToDetails: (cityId: Long) -> Unit,
    updateSelectedCity: (city: City) -> Unit,
    updateCityIsFavorite: (city: City) -> Unit,
) {
    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        colors = CardColors(
            containerColor = if (selectedCity?.id == city.id && !shouldNavigateToMap) PurpleGrey80 else Pink80,
            contentColor = Color.Black,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = Color.Transparent
        ),
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .clickable {
                updateSelectedCity(city)

                if (shouldNavigateToMap) {
                    goToMap()
                }
            }
    ) {
        Row(
            modifier = Modifier.padding(16.dp)
        ) {
            Box(
                modifier = Modifier.weight(2f)
            ) {
                Column {
                    Text(
                        text = "${city.name}, ${city.country}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = stringResource(R.string.located_at, city.lat, city.lon)
                    )
                    Button(
                        onClick = { goToDetails(city.id) },
                        modifier = Modifier.testTag("$VIEW_DETAILS_BUTTON_TAG - ${city.id}")
                    ) {
                        Text(
                            text = stringResource(R.string.view_details_btn)
                        )
                    }
                }
            }

            IconButton(
                onClick = {
                    val updatedCity = city.copy(isFavorite = !city.isFavorite)
                    updateCityIsFavorite(updatedCity)
                },
                modifier = Modifier.align(Alignment.CenterVertically)
            ) {
                if (city.isFavorite) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.testTag("$CITY_IS_FAVORITE_STAR_TAG - ${city.id}")
                    )
                } else {
                    Icon(
                        painter = painterResource(R.drawable.ic_star_outline),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.testTag("$CITY_IS_NOT_FAVORITE_STAR_TAG - ${city.id}")
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun CityCardPreview() {
    CityCard(
        city = dummyCities[0],
        selectedCity = dummyCities[0],
        shouldNavigateToMap = true,
        goToMap = {},
        goToDetails = {},
        updateSelectedCity = {},
        updateCityIsFavorite = {},
    )
}
