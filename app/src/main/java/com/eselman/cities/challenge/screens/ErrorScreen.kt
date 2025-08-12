package com.eselman.cities.challenge.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eselman.cities.challenge.R
import com.eselman.cities.challenge.utils.AppConstants.ERROR_MESSAGE_TAG
import com.eselman.cities.challenge.utils.AppConstants.ERROR_RETRY_BUTTON_TAG

@Composable
fun ErrorScreen(
    onRetry: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center)
        ) {
            Text(
                text = stringResource(R.string.error_message),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .testTag(ERROR_MESSAGE_TAG)
            )
            Button(
                onClick = {
                    onRetry()
                },
                modifier = Modifier
                    .padding(top = 24.dp)
                    .align(Alignment.CenterHorizontally)
                    .testTag(ERROR_RETRY_BUTTON_TAG)
            ) {
                Text(
                    text = stringResource(R.string.error_retry_btn)
                )
            }
        }
    }
}

@Preview
@Composable
fun ErrorScreenPreview() {
    ErrorScreen {}
}
