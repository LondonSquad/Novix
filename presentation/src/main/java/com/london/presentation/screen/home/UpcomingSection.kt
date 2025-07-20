package com.london.presentation.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.london.designsystem.component.HomeCard
import com.london.designsystem.component.NovixChip
import com.london.designsystem.theme.NovixTheme
import com.london.designsystem.theme.ThemePreviews

@Composable
fun UpcomingSection(
    onMovieClick: (movieId:Int) -> Unit,
    onGenreClick: (genreId:Int) -> Unit,
    modifier: Modifier = Modifier
) {

    val density = LocalDensity.current
    val screenWidth = with(density) {
        LocalConfiguration.current.screenWidthDp.dp
    }
        LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(
            top = 12.dp,
            bottom = 16.dp,
            start = 16.dp,
            end = 16.dp
        ),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
            .background(color = NovixTheme.colors.surface)
    ) {
        item(span = { GridItemSpan(maxLineSpan) }) {
//            Text(
//                text = "Upcoming",
//                style = NovixTheme.typography.headline.small,
//                color = NovixTheme.colors.title
//            )
        }
        item(span = { GridItemSpan(maxLineSpan) }) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 16.dp),
                modifier = Modifier.requiredWidth(screenWidth)
            ) {
                items(10) {
                    NovixChip(
                        text = "Adventure",
                        isSelected = true,
                        onClick = {onGenreClick(it)}
                    )
                }
            }
        }
        items(10) {
            HomeCard(
                imageUrl = "",
                isSaved = false,
                onSaveClick = { },
                modifier = Modifier
                    .clickable { onMovieClick(it) })
        }
    }
}

@Composable
@Preview
@ThemePreviews
private fun Preview() {
    UpcomingSection(onMovieClick = {}, onGenreClick = {})
}