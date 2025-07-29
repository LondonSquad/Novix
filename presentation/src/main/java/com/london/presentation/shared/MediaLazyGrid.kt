package com.london.presentation.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.london.designsystem.component.CircularLoading
import com.london.designsystem.component.EmptyLayout
import com.london.designsystem.component.HomeCard
import com.london.designsystem.component.TopBar
import com.london.designsystem.theme.NovixTheme
import com.london.domain.entity.actordetails.actormovie.ActorMovieCastMemberEntity

@Composable
fun MediaLazyGrid(
    title: String,
    movies: List<ActorMovieCastMemberEntity>,
    onBackClick: () -> Unit,
    onMovieClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    emptyTitle: String = "",
    emptyImage: Int? = null
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = NovixTheme.colors.surface)
    ) {
        TopBar(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            title = title,
            onBackClick = onBackClick
        )

        when {
            isLoading -> CircularLoading(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center)
            )

            !isLoading && movies.isEmpty() && emptyImage != null -> EmptyLayout(
                text = emptyTitle,
                image = emptyImage,
                modifier = Modifier
                    .fillMaxSize()
                    .navigationBarsPadding()
            )

            else -> {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 158.dp),
                    contentPadding = PaddingValues(
                        top = 12.dp,
                        bottom = 16.dp
                    ),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .navigationBarsPadding()
                        .padding(horizontal = 16.dp)
                ) {
                    items(movies) { item ->
                        HomeCard(
                            imageUrl = item.posterUrl,
                            isSaved = false,
                            onSaveClick = { onMovieClick(item.id) },
                            modifier = Modifier.clickable { onMovieClick(item.id) }
                        )
                    }
                }
            }
        }
    }
}
