package com.london.presentation.screen.details.actor.topmoviespicks

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.london.designsystem.theme.NovixTheme
import com.london.designsystem.theme.ThemePreviews
import com.london.presentation.R
import com.london.presentation.screen.details.actor.TopPicksDetails

@Composable
fun TopMoviesPicks(
    modifier: Modifier = Modifier
) {
    TopPicksDetails(
        title = stringResource(R.string.top_movies_picks),
    )
}

@ThemePreviews
@Composable
fun TopMoviesPicksPreview() {
    NovixTheme {
        TopMoviesPicks()
    }
}