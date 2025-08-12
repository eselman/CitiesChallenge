package com.eselman.cities.challenge.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eselman.cities.challenge.R
import com.eselman.cities.challenge.ui.theme.Pink80
import com.eselman.cities.challenge.utils.AppConstants.SEARCH_ONLY_FAVORITE_CHECKBOX_TAG
import com.eselman.cities.challenge.utils.AppConstants.SEARCH_TEXT_FIELD_TAG
import com.eselman.cities.challenge.utils.AppConstants.SEARCH_TEXT_ROW_TAG

@Composable
fun SearchTextRow(
    storeSearchText: String,
    storeOnlyFavoriteCities: Boolean,
    updateSearchText: (searchText: String) -> Unit,
    updateOnlyFavoriteCities: (onlyFavoriteCities: Boolean) -> Unit,
    filterCities: (searchText: String, onlyFavoriteCities: Boolean) -> Unit,
) {
    var searchText by rememberSaveable { mutableStateOf("") }
    var onlyFavoriteCities by rememberSaveable { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(
                color = Pink80, shape = RoundedCornerShape(16.dp)
            )
            .testTag(SEARCH_TEXT_ROW_TAG)
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(8.dp)
        )

        TextField(
            value = storeSearchText,
            onValueChange = {
                searchText = it
                updateSearchText(searchText)
                filterCities(searchText, storeOnlyFavoriteCities)
            },
            placeholder = { Text(text = stringResource(R.string.filter_cities)) },
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Pink80,
                unfocusedContainerColor = Pink80,
                disabledContainerColor = Pink80,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                cursorColor = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(8.dp)
                .testTag(SEARCH_TEXT_FIELD_TAG)
        )

        Checkbox(
            checked = storeOnlyFavoriteCities,
            onCheckedChange = {
                onlyFavoriteCities = it
                updateOnlyFavoriteCities(onlyFavoriteCities)
                filterCities(storeSearchText, onlyFavoriteCities)
            },
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .testTag(SEARCH_ONLY_FAVORITE_CHECKBOX_TAG)
        )
        Text(
            text = stringResource(R.string.only_favorite_cities),
            modifier = Modifier
                .padding(end = 8.dp)
                .align(Alignment.CenterVertically)
        )
    }
}

@Preview
@Composable
fun SearchTextRowPreview() {
    SearchTextRow(
        storeSearchText = "",
        storeOnlyFavoriteCities = false,
        updateSearchText = {},
        updateOnlyFavoriteCities = {},
        filterCities = { _, _ -> },
    )
}
