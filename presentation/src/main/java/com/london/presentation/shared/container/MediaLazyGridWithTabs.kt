package com.london.presentation.shared.container

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.london.designsystem.component.TabLayout
import com.london.designsystem.component.Tabbable
import com.london.designsystem.theme.NovixTheme
import com.london.designsystem.theme.ThemePreviews
import com.london.domain.entity.Movie
import com.london.domain.entity.TvShow
import com.london.presentation.R
import com.london.presentation.shared.MediaCategory
import com.london.presentation.utils.MovieGenre
import com.london.presentation.utils.TvShowGenre

@Composable
fun <T : Any> MediaLazyGridWithTabs(
    modifier: Modifier = Modifier,
    imageUrl: (T) -> String = { it.getImageUrl() },
    name: (T) -> String = { it.getName() },
    items: List<T>? = null,
    isLoading: Boolean = false,
    pagingItems: LazyPagingItems<T>? = null,
    tabSelected: Int = MediaCategory.Movies.ordinal,
    onTabSelected: (MediaCategory) -> Unit = {},
    onMovieGenreClick: (MovieGenre) -> Unit = {},
    onTvShowGenreClick: (TvShowGenre) -> Unit = {},
    config: MediaGridConfig = MediaGridConfig(),
    topBar: @Composable (() -> Unit)? = null,
) {

    val tabs = listOf(
        TabbableItem(R.string.Movies),
        TabbableItem(R.string.TV_Shows)
    )

    val selectedTab = tabs.getOrNull(tabSelected)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = NovixTheme.colors.surface)
    ) {
        topBar?.invoke()

        TabLayout(
            tabs = tabs,
            selectedTab = selectedTab,
            onTabSelected = { tab ->
                val index = tabs.indexOf(tab)
                if (index != -1) {
                    val category = if (index == MediaCategory.Movies.ordinal) MediaCategory.Movies else MediaCategory.TvShows
                    onTabSelected(category)
                }
            },
            modifier = Modifier.padding(top = 4.dp)
        )

        MediaLazyGridWithFilter(
            items = items,
            pagingItems = pagingItems,
            imageUrl = imageUrl,
            name = name,
            isLoading = isLoading,
            tabSelected = tabSelected,
            modifier = Modifier.fillMaxSize(),
            onMovieGenreClick = onMovieGenreClick,
            onTvShowGenreClick = onTvShowGenreClick,
            config = config
        )
    }
}

data class TabbableItem(override val tabTextResId: Int) : Tabbable

@ThemePreviews
@Composable
private fun Preview() {
    val sampleMovies = listOf(
        Movie(
            id = 1,
            name = "Movie One",
            posterUrl = "https://example.com/movie1.jpg",
            releaseYear = 2023,
            rating = 8,
            genreIds = listOf(28, 12)
        )
    )

    val sampleTvShows = listOf(
        TvShow(
            id = 1,
            name = "TVShow 1",
            posterPicture = "https://example.com/tvshow1.jpg",
            releaseYear = 2023,
            rating = 8,
            genres = listOf(18, 80)
        )
    )

    val combinedItems = sampleMovies + sampleTvShows

    MediaLazyGridWithTabs(
        items = combinedItems,
        tabSelected = 0,
        onTabSelected = {},
        onMovieGenreClick = {},
        onTvShowGenreClick = {},
        config = MediaGridConfig(
            showSaveIcon = true,
            isDarkMode = true,
            myRatingList = false,
            rate = "3",
            isMovieSelected = true,
            isTvShowSelected = false,
            selectedMovieGenre = MovieGenre.All,
            selectedTvShowGenre = TvShowGenre.All,
            onNavigateToMovie = {},
            onNavigateToTvShow = {},
            onSaveClick = {},
            isItemSaved = { false },
            onDeleteClick = {}
        )
    )
}