package com.london.presentation.screen.search

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.input.TextFieldValue
import com.london.presentation.screen.search.model.ActorUi
import com.london.presentation.screen.search.model.MovieUi
import com.london.presentation.screen.search.model.TvShowUi

data class SearchUiState(
    var searchQuery: TextFieldValue = TextFieldValue(""),
    val showNoSearchBefore: Boolean = false,
    val showNoSearchResults: Boolean = false,
    var showFilterBottomSheet: Boolean = false,
    val isSearchHistoryExpanded: Boolean = false,
    val isMovieSaved: (MovieUi) -> Boolean = { false },
    val searchHistory: List<String> = emptyList(),
    val actorUiResults: List<ActorUi> = DummyData.dummyActorsList,
    val movieResults: List<MovieUi> = DummyData.dummyMoviesList,
    val tvShowUiResults: List<TvShowUi> = DummyData.dummyTvShowsList,
    val savedMovies: Set<Int> = emptySet(),
    val selectedCategory: SearchCategory = SearchCategory.Movies,
    val recentViewed: List<String> = emptyList(),
    val recentSearches: List<String> = emptyList(),
)

data class CategoryContent<T>(
    val items: List<T>,
    val content: @Composable (List<T>) -> Unit
)

object DummyData {
    val dummyMoviesList = listOf(
        MovieUi(
            id = 1,
            title = "Interstellar",
            posterUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT9oW0XQlu1lo1G_49M-YwGzKR6rUg-CtflZj07HfbT8d2GwKWg",
            isSaved = false
        ),
        MovieUi(
            id = 2,
            title = "The Dark Knight",
            posterUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT9oW0XQlu1lo1G_49M-YwGzKR6rUg-CtflZj07HfbT8d2GwKWg",
            isSaved = false
        ),
        MovieUi(
            id = 3,
            title = "Inception",
            posterUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT9oW0XQlu1lo1G_49M-YwGzKR6rUg-CtflZj07HfbT8d2GwKWg",
            isSaved = false
        ),
        MovieUi(
            id = 4,
            title = "Dune",
            posterUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT9oW0XQlu1lo1G_49M-YwGzKR6rUg-CtflZj07HfbT8d2GwKWg",
            isSaved = false
        ),
        MovieUi(
            id = 5,
            title = "Oppenheimer",
            posterUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT9oW0XQlu1lo1G_49M-YwGzKR6rUg-CtflZj07HfbT8d2GwKWg",
            isSaved = false
        ),
    )

    val dummyTvShowsList = listOf(
        TvShowUi(
            id = 1,
            title = "Breaking Bad",
            posterUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT9oW0XQlu1lo1G_49M-YwGzKR6rUg-CtflZj07HfbT8d2GwKWg",
        ),
        TvShowUi(
            id = 2,
            title = "Game of Thrones",
            posterUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT9oW0XQlu1lo1G_49M-YwGzKR6rUg-CtflZj07HfbT8d2GwKWg",
        ),
        TvShowUi(
            id = 3,
            title = "The Office",
            posterUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT9oW0XQlu1lo1G_49M-YwGzKR6rUg-CtflZj07HfbT8d2GwKWg",
        )
    )

    val dummyActorsList = listOf(
        ActorUi(
            id = 1,
            name = "Leonardo DiCaprio",
            profileUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT9oW0XQlu1lo1G_49M-YwGzKR6rUg-CtflZj07HfbT8d2GwKWg"
        ),
        ActorUi(
            id = 2,
            name = "Margot Robbie",
            profileUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT9oW0XQlu1lo1G_49M-YwGzKR6rUg-CtflZj07HfbT8d2GwKWg"
        ),
        ActorUi(
            id = 3,
            name = "Ryan Gosling",
            profileUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT9oW0XQlu1lo1G_49M-YwGzKR6rUg-CtflZj07HfbT8d2GwKWg"
        )
    )
}