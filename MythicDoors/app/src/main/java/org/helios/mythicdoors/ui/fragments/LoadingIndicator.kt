package org.helios.mythicdoors.ui.fragments

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.helios.mythicdoors.R
import org.helios.mythicdoors.utils.AppConstants

@Composable
fun LoadingIndicator(
    isLoading: Boolean = false,
) {
    if (isLoading) {
        CircularProgressIndicator(
            modifier = Modifier
                .wrapContentSize()
                .padding(AppConstants.ScreenConstants.AVERAGE_PADDING.dp),
            color = MaterialTheme.colorScheme.secondary,
        )
        Text(text = stringResource(id = R.string.loading_indicator_msg),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .wrapContentSize()
        )
    }
}