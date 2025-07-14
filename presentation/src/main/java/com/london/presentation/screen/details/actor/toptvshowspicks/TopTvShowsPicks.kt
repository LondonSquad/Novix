package com.london.presentation.screen.details.actor.toptvshowspicks

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.london.designsystem.theme.ThemePreviews
import com.london.presentation.R
import com.london.presentation.screen.details.actor.TopPicksDetails

@Composable
fun TopTvShowsPicks(
    modifier: Modifier = Modifier
) {
    TopPicksDetails(
        title = stringResource(R.string.top_tv_shows_picks),
    )
}

@ThemePreviews
@Composable
fun TopTvShowsPicksPreview() {
    TopTvShowsPicks()
}