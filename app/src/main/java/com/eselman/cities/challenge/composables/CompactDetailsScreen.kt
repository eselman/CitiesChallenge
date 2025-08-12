package com.eselman.cities.challenge.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.eselman.cities.challenge.R
import com.eselman.cities.challenge.model.City
import com.eselman.cities.challenge.model.getDisplayCountry
import com.eselman.cities.challenge.utils.AppConstants.CITY_COORDINATES_TAG
import com.eselman.cities.challenge.utils.AppConstants.CITY_COUNTRY_TAG
import com.eselman.cities.challenge.utils.AppConstants.CITY_MAP_IMAGE_TAG
import com.eselman.cities.challenge.utils.AppConstants.CITY_NAME_TAG
import com.eselman.cities.challenge.utils.dummyCities
import com.eselman.cities.challenge.utils.getMapsApiKey

@Composable
fun CompactDetailsScreen(
    city: City?,
    onMapImageClicked: () -> Unit = {},
) {
    val mapsApiKey = LocalContext.current.getMapsApiKey()
    val imagePreviewUrl =
        "https://maps.googleapis.com/maps/api/staticmap?center=${city?.lat ?: 0.0},${city?.lon ?: 0.0}" +
                "&zoom=15&size=400x200&maptype=roadmap&markers=color:red%7Clabel:C%7C${city?.lat ?: 0.0},${city?.lon ?: 0.0}&key=${mapsApiKey}"

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 32.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = city?.name ?: "",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(start = 8.dp)
                .align(Alignment.CenterHorizontally)
                .testTag(CITY_NAME_TAG)
        )

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Row {
                Text(
                    text = stringResource(R.string.country),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                )
                Text(
                    text = city?.getDisplayCountry() ?: "",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .testTag(CITY_COUNTRY_TAG),
                )
            }
        }

        Text(
            text = stringResource(
                R.string.located_at,
                city?.lat ?: 0.0,
                city?.lon ?: 0.0
            ),
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .padding(start = 16.dp)
                .testTag(CITY_COORDINATES_TAG),
        )

        Image(
            painter = rememberAsyncImagePainter(
                model = imagePreviewUrl,
                placeholder = painterResource(
                    R.drawable.ic_launcher_background
                ),
            ),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(16.dp)
                .clip(shape = RoundedCornerShape(16.dp))
                .clickable {
                    onMapImageClicked()
                }
                .testTag(CITY_MAP_IMAGE_TAG)
        )
    }
}

@Preview
@Composable
fun CompactDetailsScreenPreview() {
    CompactDetailsScreen(
        city = dummyCities[0],
        onMapImageClicked = {},
    )
}
