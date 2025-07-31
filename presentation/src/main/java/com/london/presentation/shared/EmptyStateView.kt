package com.london.presentation.shared

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.london.designsystem.component.Text
import com.london.designsystem.theme.NovixTheme
import com.london.presentation.R

@Composable
fun EmptyStateView() {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = stringResource(R.string.no_data_until_now),
            style = NovixTheme.typography.label.large,
            color = NovixTheme.colors.body
        )
    }
}
