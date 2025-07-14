package com.london.presentation.screen.details.actor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.london.designsystem.component.HomeCard
import com.london.designsystem.component.TopBar
import com.london.designsystem.theme.NovixTheme
import com.london.designsystem.theme.ThemePreviews
import com.london.presentation.R

@Composable
fun TopPicksDetails(
    title: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(NovixTheme.colors.surface)
    ) {
        TopBar(
            title = title,
            onBackClick = {},
            modifier = Modifier
                .statusBarsPadding()
                .padding(vertical = 12.dp, horizontal = 16.dp)
        )
        TopPicksDetailsContent()
    }
}


@Composable
fun TopPicksDetailsContent() {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.padding(horizontal = 4.dp, vertical = 5.dp)
    ) {
        items(10) {
            HomeCard(
                imageUrl = "https://image.tmdb.org/t/p/w500//8Gxv8gSFCU0XGDykEGv7zR1n2ua.jpg",
                isSaved = true,
                onSaveClick = {},
                modifier = Modifier.padding(12.dp)
            )
        }
    }
}

@ThemePreviews
@Composable
fun TopPicksDetailsPreview() {
    TopPicksDetails(
        stringResource(R.string.top_movies_picks)
    )
}