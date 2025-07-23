package com.london.presentation.screen.search

import androidx.compose.ui.text.input.TextFieldValue
import androidx.paging.PagingData
import com.london.domain.entity.Actor
import com.london.domain.entity.Movie
import com.london.domain.entity.TvShow
import com.london.domain.entity.recent.RecentSearch
import com.london.domain.entity.recent.RecentViewed
import com.london.presentation.screen.base.ErrorState
import com.london.presentation.screen.search.model.MovieUi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

data class SearchUiState(
    var searchQuery: TextFieldValue = TextFieldValue(""),
    val error: ErrorState? = null,
    val showNoSearchBefore: Boolean = false,
    val showNoSearchResults: Boolean = false,
    val showFilterBottomSheet: Boolean = false,
    val showFilterButton: Boolean = true,
    val isSearchHistoryExpanded: Boolean = false,
    val isMovieSaved: (MovieUi) -> Boolean = { false },
    val searchHistory: List<String> = emptyList(),
    val actorsFlow: Flow<PagingData<Actor>> = flow {},
    val moviesFlow: Flow<PagingData<Movie>> = flow {},
    val tvShowsFlow: Flow<PagingData<TvShow>> = flow {},
    val savedMovies: Set<Int> = emptySet(),
    val savedTvShows: Set<Int> = emptySet(),
    val selectedCategory: SearchCategory = SearchCategory.Movies,
    val recentViewed: List<RecentViewed> = emptyList(),
    val recentSearches: List<RecentSearch> = emptyList(),
    val lastSearch: String="",
    val availableGenres: List<Int> = availableMovieGenres,
    val availableGenresWithNames: List<Pair<Int, Int>> = emptyList(),
    val selectedGenres: List<Int> = listOf(),
    val minimumRating: Double = 0.0,
    val maximumRating: Double = 0.0,
    val imdbRating: Int = 0,
    val releaseYearRange: ClosedFloatingPointRange<Float> = 1950f..2030f
)

enum class MoviesGenres(val id: Int) {
    Action(28),
    Adventure(12),
    Animation(16),
    Comedy(35),
    Crime(80),
    Documentary(99),
    Drama(18),
    Family(10751),
    Fantasy(14),
    History(36),
    Horror(27),
    Music(10402),
    Mystery(9648),
    Romance(10749),
    ScienceFiction(878),
    TVMovie(10770),
    Thriller(53),
    War(10752),
    Western(37)
}


val availableMovieGenres = listOf(
    MoviesGenres.Action.id,
    MoviesGenres.Adventure.id,
    MoviesGenres.Animation.id,
    MoviesGenres.Comedy.id,
    MoviesGenres.Crime.id,
    MoviesGenres.Documentary.id,
    MoviesGenres.Drama.id,
    MoviesGenres.Family.id,
    MoviesGenres.Fantasy.id,
    MoviesGenres.History.id,
    MoviesGenres.Horror.id,
    MoviesGenres.Music.id,
    MoviesGenres.Mystery.id,
    MoviesGenres.Romance.id,
    MoviesGenres.ScienceFiction.id,
    MoviesGenres.TVMovie.id,
    MoviesGenres.Thriller.id,
    MoviesGenres.War.id,
    MoviesGenres.Western.id,
)


enum class TvGenres(val id: Int) {
    ActionAdventure(10759),
    Animation(16),
    Comedy(35),
    Crime(80),
    Documentary(99),
    Drama(18),
    Family(10751),
    Kids(10762),
    Mystery(9648),
    News(10763),
    Reality(10764),
    SciFiFantasy(10765),
    Soap(10766),
    Talk(10767),
    WarPolitics(10768),
    Western(37)
}


val availableTvGenres = listOf(
    TvGenres.ActionAdventure.id,
    TvGenres.Animation.id,
    TvGenres.Comedy.id,
    TvGenres.Crime.id,
    TvGenres.Documentary.id,
    TvGenres.Drama.id,
    TvGenres.Family.id,
    TvGenres.Kids.id,
    TvGenres.Mystery.id,
    TvGenres.News.id,
    TvGenres.Reality.id,
    TvGenres.SciFiFantasy.id,
    TvGenres.Soap.id,
    TvGenres.Talk.id,
    TvGenres.WarPolitics.id,
    TvGenres.Western.id
)