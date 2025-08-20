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
import com.london.domain.entity.genre.MovieGenre
import com.london.domain.entity.genre.TvShowGenre
import com.london.domain.entity.movie.Movie
import com.london.domain.entity.tvshow.TvShow
import com.london.presentation.R
import com.london.presentation.shared.MediaCategory
import com.london.presentation.shared.genre.MovieGenreUi
import com.london.presentation.shared.genre.TvShowGenreUi

@Composable
fun <T : Any> MediaLazyGridWithTabs(
    items: List<T>,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    name: (T) -> String = { it.getName() },
    imageUrl: (T) -> String? = { it.getImageUrl() },
    tabSelected: Int = MediaCategory.Movies.ordinal,
    onTabSelected: (MediaCategory) -> Unit = {},
    onMovieGenreClick: (MovieGenreUi) -> Unit = {},
    onTvShowGenreClick: (TvShowGenreUi) -> Unit = {},
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
                handleTabSelection(tab, tabs, onTabSelected)
            },
            modifier = Modifier.padding(top = 4.dp)
        )

        MediaLazyGridWithFilter(
            items = items,
            imageUrl = imageUrl,
            name = name,
            isLoading = isLoading,
            modifier = Modifier.fillMaxSize(),
            onMovieGenreClick = onMovieGenreClick,
            onTvShowGenreClick = onTvShowGenreClick,
            config = config
        )
    }
}

@Composable
fun <T : Any> MediaLazyGridWithTabs(
    pagingItems: LazyPagingItems<T>,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    name: (T) -> String = { it.getName() },
    imageUrl: (T) -> String? = { it.getImageUrl() },
    tabSelected: Int = MediaCategory.Movies.ordinal,
    onTabSelected: (MediaCategory) -> Unit = {},
    onMovieGenreClick: (MovieGenreUi) -> Unit = {},
    onTvShowGenreClick: (TvShowGenreUi) -> Unit = {},
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
                handleTabSelection(tab, tabs, onTabSelected)
            },
            modifier = Modifier.padding(top = 4.dp)
        )

        MediaLazyGridWithFilter(
            pagingItems = pagingItems,
            imageUrl = imageUrl,
            name = name,
            isLoading = isLoading,
            modifier = Modifier.fillMaxSize(),
            onMovieGenreClick = onMovieGenreClick,
            onTvShowGenreClick = onTvShowGenreClick,
            config = config
        )
    }
}

private fun handleTabSelection(
    tab: Tabbable,
    tabs: List<Tabbable>,
    onTabSelected: (MediaCategory) -> Unit
) {
    val index = tabs.indexOf(tab)
    if (index != -1) {
        val category =
            if (index == MediaCategory.Movies.ordinal) MediaCategory.Movies else MediaCategory.TvShows
        onTabSelected(category)
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
            genres = listOf(MovieGenre.TV_MOVIE)
        )
    )

    val sampleTvShows = listOf(
        TvShow(
            id = 1,
            name = "TVShow 1",
            posterPicture = "https://example.com/tvshow1.jpg",
            releaseYear = 2023,
            rating = 8,
            genres = listOf(TvShowGenre.ACTION_ADVENTURE)
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
            rate = "3",
            isMovieSelected = true,
            isTvShowSelected = false,
            selectedMovieGenre = MovieGenreUi.All,
            selectedTvShowGenre = TvShowGenreUi.All,
            onNavigateToMovie = {},
            onNavigateToTvShow = {},
            onSaveClick = {},
            isItemSaved = { false },
            onDeleteClick = {}
        )
    )
}
