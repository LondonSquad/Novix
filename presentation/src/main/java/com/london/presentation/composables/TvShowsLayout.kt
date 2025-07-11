package com.london.presentation.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.dp
import com.london.designsystem.component.HomeCard
import com.london.domain.entity.TvShow

@Composable
fun TvShowLayOut(
    tvShowUis: List<TvShow>,
    onSaveClick: (TvShow) -> Unit,
    isTvShowSaved: (TvShow) -> Boolean,
    onTvShowClick: (TvShow) -> Unit
) {
    val screenWidth = LocalWindowInfo.current.containerSize.width
    val itemWidthPx = with(LocalDensity.current) { 158.dp.toPx() }
    val screenPaddingPx = with(LocalDensity.current) { 32.dp.toPx() }
    val columns = ((screenWidth - screenPaddingPx) / itemWidthPx).toInt().coerceAtLeast(2)

    LazyVerticalGrid(
        columns = GridCells.Fixed(columns),
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),

        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(tvShowUis) { tvShow ->
            HomeCard(
                imageUrl = tvShow.posterPicture,
                onSaveClick = { onSaveClick(tvShow) },
                isSaved = isTvShowSaved(tvShow),
                imageDescription = tvShow.name,
                modifier = Modifier.clickable { onTvShowClick(tvShow) }
            )
        }
    }
}