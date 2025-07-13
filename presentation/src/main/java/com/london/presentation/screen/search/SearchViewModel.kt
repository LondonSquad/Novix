package com.london.presentation.screen.search

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.london.domain.entity.Actor
import com.london.domain.entity.Movie
import com.london.domain.entity.TvShow
import com.london.domain.usecase.GetActorsUseCase
import com.london.domain.usecase.GetMoviesUseCase
import com.london.domain.usecase.GetTvShowsUseCase
import com.london.presentation.composables.filterbottomsheet.FilterBottomSheetUiState
import com.london.presentation.composables.filterbottomsheet.availableMovieGenres
import com.london.presentation.composables.filterbottomsheet.availableTvGenres
import com.london.presentation.screen.search.model.MovieUi
import com.london.presentation.utils.convertGenreCodeToString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class SearchViewModel(
    private val getActorsUseCase: GetActorsUseCase,
    private val getTvShowsUseCase: GetTvShowsUseCase,
    private val getMoviesUseCase: GetMoviesUseCase
) : ViewModel(), SearchInteractions {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private val _filterUiState = MutableStateFlow(FilterBottomSheetUiState())
    var filterUiState: StateFlow<FilterBottomSheetUiState> = _filterUiState.asStateFlow()

    private var allMovies: List<Movie> = emptyList()
    private var allTvShows: List<TvShow> = emptyList()
    private var allActors: List<Actor> = emptyList()

    private var filteredMovieResults: List<Movie> = emptyList()
    private var filteredTvShowResults: List<TvShow> = emptyList()
    private var filteredActorResults: List<Actor> = emptyList()

    private var searchJob: Job? = null

    override fun onSearchQueryChange(newValue: TextFieldValue) {
        _uiState.update { it.copy(searchQuery = newValue) }

        searchJob?.cancel()

        searchJob = viewModelScope.launch {
            delay(300)
            performSearch(newValue.text, _uiState.value.selectedCategory)
        }
    }

    override fun onCategorySelected(category: SearchCategory) {
        _uiState.update { it.copy(selectedCategory = category) }

        updateAvailableGenres(category)

        searchJob?.cancel()

        searchJob = viewModelScope.launch {
            performSearch(_uiState.value.searchQuery.text, category)
        }
    }

    override fun onSearchFilterClick(query: String, category: SearchCategory) {
        searchJob?.cancel()

        searchJob = viewModelScope.launch {
            performSearch(query, category)
        }
    }

    private fun performSearch(query: String, category: SearchCategory) {
        val trimmedQuery = query.trim()

        updateAvailableGenres(category)

        if (trimmedQuery.isEmpty()) {
            filteredMovieResults = allMovies
            filteredTvShowResults = allTvShows
            filteredActorResults = allActors

            _uiState.update { currentState ->
                when (category) {
                    SearchCategory.Movies -> currentState.copy(
                        movieResults = allMovies,
                        tvShowUiResults = emptyList(),
                        actorUiResults = emptyList()
                    )
                    SearchCategory.Actors -> currentState.copy(
                        actorUiResults = allActors,
                        movieResults = emptyList(),
                        tvShowUiResults = emptyList()
                    )
                    SearchCategory.TvShows -> currentState.copy(
                        tvShowUiResults = allTvShows,
                        actorUiResults = emptyList(),
                        movieResults = emptyList()
                    )
                }
            }
            return
        }

        searchWithApi(trimmedQuery, category)
    }

    private fun searchWithApi(query: String, category: SearchCategory) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                when (category) {
                    SearchCategory.Movies -> {
                        val searchResults = getMoviesUseCase(query, "en-US")
                        filteredMovieResults = searchResults

                        val filteredResults = applyMovieFilters(searchResults)

                        _uiState.update { currentState ->
                            currentState.copy(
                                movieResults = filteredResults,
                                actorUiResults = emptyList(),
                                tvShowUiResults = emptyList()
                            )
                        }
                    }

                    SearchCategory.Actors -> {
                        val searchResults = getActorsUseCase(query, "en-US")
                        filteredActorResults = searchResults

                        _uiState.update { currentState ->
                            currentState.copy(
                                actorUiResults = searchResults,
                                movieResults = emptyList(),
                                tvShowUiResults = emptyList()
                            )
                        }
                    }

                    SearchCategory.TvShows -> {
                        val searchResults = getTvShowsUseCase(query, "en-US")
                        filteredTvShowResults = searchResults

                        val filteredResults = applyTvShowFilters(searchResults)

                        _uiState.update { currentState ->
                            currentState.copy(
                                tvShowUiResults = filteredResults,
                                actorUiResults = emptyList(),
                                movieResults = emptyList()
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                _uiState.update { currentState ->
                    currentState.copy(
                        movieResults = emptyList(),
                        tvShowUiResults = emptyList(),
                        actorUiResults = emptyList()
                    )
                }
            }
        }
    }

    private fun applyMovieFilters(movies: List<Movie>): List<Movie> {
        val filterState = _filterUiState.value
        return movies.filter { movie ->
            val matchesGenres = filterState.selectedGenres.isEmpty() ||
                    movie.genreIds.any { genre -> filterState.selectedGenres.contains(genre) }

            val matchesRating = movie.rating >= filterState.imdbRating

            val matchesYear = movie.releaseYear in filterState.releaseYearRange.start.toInt()..filterState.releaseYearRange.endInclusive.toInt()

            matchesGenres && matchesRating && matchesYear
        }
    }

    private fun applyTvShowFilters(tvShows: List<TvShow>): List<TvShow> {
        val filterState = _filterUiState.value
        return tvShows.filter { tvShow ->
            val matchesGenres = filterState.selectedGenres.isEmpty() ||
                    tvShow.genres.any { genre -> filterState.selectedGenres.contains(genre) }

            val matchesRating = tvShow.rating >= filterState.imdbRating

            val matchesYear = tvShow.releaseYear in filterState.releaseYearRange.start.toInt()..filterState.releaseYearRange.endInclusive.toInt()

            matchesGenres && matchesRating && matchesYear
        }
    }

    override fun onSavedMovieClick(movie: MovieUi) {
        _uiState.update { currentState ->
            val isNowSaved = !currentState.savedMovies.contains(movie.id)
            val updatedSavedMovies = if (isNowSaved) {
                currentState.savedMovies + movie.id
            } else {
                currentState.savedMovies - movie.id
            }
            currentState.copy(savedMovies = updatedSavedMovies)
        }
    }

    fun addToRecentSearches(query: String) {
        if (query.isBlank()) return

        val currentSearches = _uiState.value.recentSearches.toMutableList()
        currentSearches.remove(query)
        currentSearches.add(0, query)

        if (currentSearches.size > 10) {
            currentSearches.removeAt(currentSearches.size - 1)
        }

        _uiState.update { it.copy(recentSearches = currentSearches) }
    }

    fun addToRecentViewed(imageUrl: String) {
        if (imageUrl.isBlank()) return

        val currentViewed = _uiState.value.recentViewed.toMutableList()
        currentViewed.remove(imageUrl)
        currentViewed.add(0, imageUrl)

        if (currentViewed.size > 10) {
            currentViewed.removeAt(currentViewed.size - 1)
        }

        _uiState.update { it.copy(recentViewed = currentViewed) }
    }

    override fun clearRecentViewed() {
        _uiState.update { it.copy(recentViewed = emptyList()) }
    }

    override fun clearRecentSearches() {
        _uiState.update { it.copy(recentSearches = emptyList()) }
    }

    override fun removeRecentSearch(search: String) {
        val updatedSearches = _uiState.value.recentSearches.filter { it != search }
        _uiState.update { it.copy(recentSearches = updatedSearches) }
    }

    override fun onRecentSearchClick(search: String) {
        _uiState.update { it.copy(searchQuery = TextFieldValue(search)) }

        searchJob?.cancel()

        searchJob = viewModelScope.launch {
            performSearch(search, _uiState.value.selectedCategory)
        }
    }

    fun clearSearch() {
        searchJob?.cancel()

        _uiState.update {
            it.copy(
                searchQuery = TextFieldValue(""),
                movieResults = allMovies,
                tvShowUiResults = allTvShows,
                actorUiResults = allActors
            )
        }
    }

    private fun updateAvailableGenres(searchCategory: SearchCategory) {
        val availableGenres = when (searchCategory) {
            SearchCategory.Movies -> availableMovieGenres
            SearchCategory.TvShows -> availableTvGenres
            SearchCategory.Actors -> emptyList()
        }

        val availableGenresWithNames = availableGenres.map { genreId ->
            genreId to convertGenreCodeToString(genreId, searchCategory)
        }

        _filterUiState.update {
            it.copy(
                availableGenres = availableGenres,
                availableGenresWithNames = availableGenresWithNames
            )
        }
    }

    fun onApplyFilter(
        selectedGenres: List<Int>,
        minimumRating: Int,
        releaseYearRange: ClosedFloatingPointRange<Float>
    ) {
        _filterUiState.update {
            it.copy(
                selectedGenres = selectedGenres,
                imdbRating = minimumRating,
                releaseYearRange = releaseYearRange
            )
        }

        _uiState.update { currentState ->
            when (currentState.selectedCategory) {
                SearchCategory.Movies -> {
                    val filteredMovies = applyMovieFilters(filteredMovieResults)
                    currentState.copy(
                        movieResults = filteredMovies,
                        tvShowUiResults = emptyList(),
                        actorUiResults = emptyList()
                    )
                }

                SearchCategory.TvShows -> {
                    val filteredTvShows = applyTvShowFilters(filteredTvShowResults)
                    currentState.copy(
                        tvShowUiResults = filteredTvShows,
                        movieResults = emptyList(),
                        actorUiResults = emptyList()
                    )
                }

                SearchCategory.Actors -> {
                    currentState.copy(
                        actorUiResults = filteredActorResults,
                        movieResults = emptyList(),
                        tvShowUiResults = emptyList()
                    )
                }
            }
        }
    }

    fun onClearFilter() {
        _filterUiState.update {
            it.copy(
                selectedGenres = emptyList(),
                imdbRating = 0,
                releaseYearRange = 1950f..2030f
            )
        }

        _uiState.update { currentState ->
            when (currentState.selectedCategory) {
                SearchCategory.Movies -> {
                    currentState.copy(
                        movieResults = filteredMovieResults,
                        tvShowUiResults = emptyList(),
                        actorUiResults = emptyList()
                    )
                }

                SearchCategory.TvShows -> {
                    currentState.copy(
                        tvShowUiResults = filteredTvShowResults,
                        movieResults = emptyList(),
                        actorUiResults = emptyList()
                    )
                }

                SearchCategory.Actors -> {
                    currentState.copy(
                        actorUiResults = filteredActorResults,
                        movieResults = emptyList(),
                        tvShowUiResults = emptyList()
                    )
                }
            }
        }
    }

    fun onReleaseYearRangeChange(range: ClosedFloatingPointRange<Float>) {
        _filterUiState.update { it.copy(releaseYearRange = range) }
    }

    fun onGenreSelectedChange(selectedGenres: List<Int>) {
        _filterUiState.update { it.copy(selectedGenres = selectedGenres) }
    }

    fun onRatingChanged(selectedRating: Int) {
        _filterUiState.update { it.copy(imdbRating = selectedRating) }
    }

    override fun onCleared() {
        super.onCleared()
        searchJob?.cancel()
    }
}