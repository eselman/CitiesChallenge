package com.eselman.cities.challenge.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    title: String, navigationIcon: ImageVector? = null, navigationIconClick: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = title, color = Color.White, maxLines = 1, overflow = TextOverflow.Ellipsis
            )
        }, colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary
        ), navigationIcon = {
            navigationIcon?.let {
                Icon(
                    it,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .clickable(onClick = { navigationIconClick() })
                )
            }
        })
}
