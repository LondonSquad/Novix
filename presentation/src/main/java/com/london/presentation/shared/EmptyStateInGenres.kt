package com.london.presentation.shared

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.london.designsystem.component.Text
import com.london.designsystem.theme.NovixTheme
import com.london.presentation.R

@Composable
fun EmptyStateInGenres() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 12.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Image(
            painter = painterResource(id = R.drawable.empty),
            contentDescription = stringResource(R.string.there_is_no_items_for_this_genre),
            modifier = Modifier.size(128.dp)
        )

        Text(
            text = stringResource(R.string.there_is_no_items_for_this_genre),
            style = NovixTheme.typography.body.small,
            color = NovixTheme.colors.body
        )
    }
}

@Preview
@Composable
fun EmptyStateViewPreview() {
    EmptyStateInGenres()
}