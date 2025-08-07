package com.london.presentation.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.london.designsystem.component.TabLayout
import com.london.designsystem.component.Tabbable
import com.london.designsystem.component.TopBar
import com.london.designsystem.theme.NovixTheme
import com.london.designsystem.theme.ThemePreviews
import com.london.domain.entity.Movie
import com.london.domain.entity.TvShow
import com.london.presentation.R
import com.london.presentation.utils.MovieGenre
import com.london.presentation.utils.TvShowGenre

@Composable
fun <T : Any> MediaLazyGridWithTabs(
    title: String,
    onBack: () -> Unit,
    getImageUrl: (T) -> String,
    onItemClick: (T) -> Unit,
    modifier: Modifier = Modifier,
    items: List<T>? = null,
    pagingItems: LazyPagingItems<T>? = null,
    onSaveClick: (T) -> Unit = {},
    isItemSaved: (T) -> Boolean = { false },
    tabSelected: Int = 0,
    selectedMovieGenre: MovieGenre = MovieGenre.All,
    selectedTvShowGenre: TvShowGenre = TvShowGenre.All,
    onTabSelected: (Int) -> Unit = {},
    onMovieGenreClick: (MovieGenre) -> Unit = {},
    onTvShowGenreClick: (TvShowGenre) -> Unit = {}
) {

    val tabs = listOf(
        TabbableItem(R.string.Movies),
        TabbableItem(R.string.TV_Shows)
    )

    val selectedTab = tabs.getOrNull(tabSelected)

    val isMovieSelected = tabSelected == 0
    val isTvShowSelected = tabSelected == 1

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = NovixTheme.colors.surface)
    ) {
        TopBar(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(start = 16.dp, end = 16.dp, top = 12.dp),
            title = title,
            onBackClick = onBack
        )

        TabLayout(
            tabs = tabs,
            selectedTab = selectedTab,
            onTabSelected = { tab ->
                val index = tabs.indexOf(tab)
                if (index != -1) onTabSelected(index)
            },
            modifier = Modifier.padding(top = 4.dp)
        )

        MediaLazyGridWithFilter(
            items = items,
            pagingItems = pagingItems,
            getImageUrl = getImageUrl,
            onItemClick = onItemClick,
            modifier = Modifier.fillMaxSize(),
            onSaveClick = onSaveClick,
            isItemSaved = isItemSaved,
            isMovieSelected = isMovieSelected,
            isTvShowSelected = isTvShowSelected,
            selectedMovieGenre = selectedMovieGenre,
            selectedTvShowGenre = selectedTvShowGenre,
            onMovieGenreClick = onMovieGenreClick,
            onTvShowGenreClick = onTvShowGenreClick
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
        title = stringResource(R.string.continue_watch),
        onBack = {},
        items = combinedItems,
        getImageUrl = {
            when (it) {
                is Movie -> it.posterUrl
                is TvShow -> it.posterPicture
                else -> ""
            }
        },
        onItemClick = {},
        onSaveClick = {},
        isItemSaved = { false },
        tabSelected = 0,
        selectedMovieGenre = MovieGenre.All,
        selectedTvShowGenre = TvShowGenre.All,
        onTabSelected = {},
        onMovieGenreClick = {},
        onTvShowGenreClick = {}
    )
}
