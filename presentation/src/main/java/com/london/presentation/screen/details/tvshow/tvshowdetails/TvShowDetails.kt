package com.london.presentation.screen.details.tvshow.tvshowdetails

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun TvShowsDetailsScreen(
    tvShowId: Int,
    onBackClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ){
        Text(
            text = "Tv Shows Details Screen and the id is $tvShowId",
            modifier = Modifier.align(Alignment.Center)
        )
    }
}