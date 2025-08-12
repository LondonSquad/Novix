package com.london.presentation.shared.container

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
    imageUrl: (T) -> String,
    name: (T) -> String,
    onItemClick: (T) -> Unit,
    modifier: Modifier = Modifier,
    items: List<T>? = null,
    pagingItems: LazyPagingItems<T>? = null,
    hasSaveIcon: Boolean = true,
    onSaveClick: (T) -> Unit = {},
    isItemSaved: (T) -> Boolean = { false },
    onDeleteClick: (T) -> Unit = {},
    isDarkMode: Boolean = true,
    myRatingList: Boolean = false,
    rate: String = "3",
    tabSelected: Int = 0,
    onTabSelected: (Int) -> Unit = {},
    selectedMovieGenre: MovieGenre = MovieGenre.All,
    selectedTvShowGenre: TvShowGenre = TvShowGenre.All,
    onMovieGenreClick: (MovieGenre) -> Unit = {},
    onTvShowGenreClick: (TvShowGenre) -> Unit = {},
    topBar: @Composable (() -> Unit)? = null
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
        topBar?.invoke()

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
            imageUrl = imageUrl,
            name = name,
            onItemClick = onItemClick,
            modifier = Modifier.fillMaxSize(),
            hasSaveIcon = hasSaveIcon,
            onSaveClick = onSaveClick,
            isItemSaved = isItemSaved,
            onDeleteClick = onDeleteClick,
            isDarkMode = isDarkMode,
            myRatingList = myRatingList,
            rate = rate,
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
        items = combinedItems,
        imageUrl = {
            when (it) {
                is Movie -> it.posterUrl
                is TvShow -> it.posterPicture
                else -> ""
            }
        },
        name = { 
            when (it) {
                is Movie -> it.name
                is TvShow -> it.name
                else -> it.toString()
            }
        },
        onItemClick = {},
        hasSaveIcon = true,
        onSaveClick = {},
        isItemSaved = { false },
        onDeleteClick = {},
        isDarkMode = true,
        myRatingList = false,
        rate = "3",
        tabSelected = 0,
        selectedMovieGenre = MovieGenre.All,
        selectedTvShowGenre = TvShowGenre.All,
        onTabSelected = {},
        onMovieGenreClick = {},
        onTvShowGenreClick = {}
    )
}
