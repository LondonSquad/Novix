package com.london.presentation.screen.search

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.london.domain.entity.Movie
import com.london.domain.entity.TvShow
import com.london.domain.usecase.AddToRecentSearchUseCase
import com.london.domain.usecase.ClearRecentSearchUseCase
import com.london.domain.usecase.GetActorsUseCase
import com.london.domain.usecase.GetMoviesUseCase
import com.london.domain.usecase.GetRecentSearchUseCase
import com.london.domain.usecase.GetTvShowsUseCase
import com.london.presentation.composables.filterbottomsheet.FilterBottomSheetUiState
import com.london.presentation.composables.filterbottomsheet.availableMovieGenres
import com.london.presentation.composables.filterbottomsheet.availableTvGenres
import com.london.presentation.screen.base.createPagingSourceFlow
import com.london.presentation.screen.search.model.MovieUi
import com.london.presentation.utils.convertGenreCodeToString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class SearchViewModel(
    private val getActorsUseCase: GetActorsUseCase,
    private val getTvShowsUseCase: GetTvShowsUseCase,
    private val getMoviesUseCase: GetMoviesUseCase,
    private val addToRecentSearchUseCase: AddToRecentSearchUseCase,
    private val getRecentSearchUseCase: GetRecentSearchUseCase,
    private val clearRecentSearchUseCase: ClearRecentSearchUseCase
) : ViewModel(), SearchInteractions {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private val _filterUiState = MutableStateFlow(FilterBottomSheetUiState())
    var filterUiState: StateFlow<FilterBottomSheetUiState> = _filterUiState.asStateFlow()

    override fun onSearchQueryChange(newValue: TextFieldValue) {
        _uiState.update { it.copy(searchQuery = newValue) }

        viewModelScope.launch {
            delay(300)
            performSearch(newValue.text, _uiState.value.selectedCategory)
        }
    }

    override fun onCategorySelected(category: SearchCategory) {
        _uiState.update { it.copy(selectedCategory = category) }

        updateAvailableGenres(category)

        viewModelScope.launch {
            performSearch(_uiState.value.searchQuery.text, category)
        }
    }

    override fun onSearchFilterClick(query: String, category: SearchCategory) {
        viewModelScope.launch {
            performSearch(query, category)
        }
    }

    private fun performSearch(query: String, category: SearchCategory) {
        val trimmedQuery = query.trim()

        updateAvailableGenres(category)

        if (trimmedQuery.isEmpty()) {
            clearSearchResults()
            return
        }

        searchWithApi(trimmedQuery, category)
    }

    private fun clearSearchResults() {
        _uiState.update { currentState ->
            currentState.copy(
                movieResults = emptyList(),
                tvShowUiResults = emptyList(),
                actorsFlow = flow {}
            )
        }
    }

    private fun searchWithApi(query: String, category: SearchCategory) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                when (category) {
                    SearchCategory.Movies -> searchMovies(query)
                    SearchCategory.Actors -> searchActors(query)
                    SearchCategory.TvShows -> searchTvShows(query)
                }
            } catch (e: Exception) {
                clearAllSearchResults()
            }
        }
    }

    private suspend fun searchMovies(query: String) {
        val searchResults = getMoviesUseCase(query, "en-US", 0)
        val filteredResults = applyMovieFilters(searchResults.items)

        _uiState.update { currentState ->
            currentState.copy(
                movieResults = filteredResults,
                actorsFlow = flow {},
                tvShowUiResults = emptyList()
            )
        }
    }

    private fun searchActors(query: String) {
        val actorsFlow = createPagingSourceFlow { pageNumber ->
            getActorsUseCase(
                name = query,
                language = "en-US",
                pageNumber = pageNumber
            )
        }

        _uiState.update { currentState ->
            currentState.copy(
                actorsFlow = actorsFlow,
                movieResults = emptyList(),
                tvShowUiResults = emptyList()
            )
        }
    }

    private suspend fun searchTvShows(query: String) {
        val searchResults = getTvShowsUseCase(query, "en-US", 0)
        val filteredResults = applyTvShowFilters(searchResults.items)

        _uiState.update { currentState ->
            currentState.copy(
                tvShowUiResults = filteredResults,
                actorsFlow = flow {},
                movieResults = emptyList()
            )
        }
    }

    private fun clearAllSearchResults() {
        _uiState.update { currentState ->
            currentState.copy(
                movieResults = emptyList(),
                tvShowUiResults = emptyList(),
                actorsFlow = flow {}
            )
        }
    }

    private fun applyMovieFilters(movies: List<Movie>): List<Movie> {
        val filterState = _filterUiState.value
        return movies.filter { movie ->
            val matchesGenres =
                filterState.selectedGenres.isEmpty() || movie.genreIds.any { genre ->
                    filterState.selectedGenres.contains(genre)
                }

            val matchesRating = movie.rating >= filterState.imdbRating

            val matchesYear =
                movie.releaseYear in filterState.releaseYearRange.start.toInt()..filterState.releaseYearRange.endInclusive.toInt()

            matchesGenres && matchesRating && matchesYear
        }
    }

    private fun applyTvShowFilters(tvShows: List<TvShow>): List<TvShow> {
        val filterState = _filterUiState.value
        return tvShows.filter { tvShow ->
            val matchesGenres = filterState.selectedGenres.isEmpty() || tvShow.genres.any { genre ->
                filterState.selectedGenres.contains(genre)
            }

            val matchesRating = tvShow.rating >= filterState.imdbRating

            val matchesYear =
                tvShow.releaseYear in filterState.releaseYearRange.start.toInt()..filterState.releaseYearRange.endInclusive.toInt()

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

    override fun addToRecentSearches(query: String) {
        if (query.isBlank()) return

        viewModelScope.launch {
            addToRecentSearchUseCase.invoke(query)
            _uiState.update { it.copy(recentSearches = getRecentSearchUseCase.invoke()) }
        }
    }

    override fun addToRecentViewed(imageUrl: String) {
        // (important) make the recent data in the database
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
        viewModelScope.launch {
            clearRecentSearchUseCase.invoke()
        }
    }

    override fun removeRecentSearch(search: String) {
        val updatedSearches = _uiState.value.recentSearches.filter { it != search }
        _uiState.update { it.copy(recentSearches = updatedSearches) }
    }

    override fun onRecentSearchClick(search: String) {
        _uiState.update { it.copy(searchQuery = TextFieldValue(search)) }

        viewModelScope.launch {
            performSearch(search, _uiState.value.selectedCategory)
        }
    }

    override fun clearSearch() {
        _uiState.update {
            it.copy(
                searchQuery = TextFieldValue(""),
                movieResults = emptyList(),
                tvShowUiResults = emptyList(),
                actorsFlow = flow {}
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

    override fun onApplyFilter(
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

        viewModelScope.launch {
            performSearch(_uiState.value.searchQuery.text, _uiState.value.selectedCategory)
        }
    }

    override fun onClearFilter() {
        _filterUiState.update {
            it.copy(
                selectedGenres = emptyList(), imdbRating = 0, releaseYearRange = 1950f..2030f
            )
        }

        viewModelScope.launch {
            performSearch(_uiState.value.searchQuery.text, _uiState.value.selectedCategory)
        }
    }

    override fun onReleaseYearRangeChange(range: ClosedFloatingPointRange<Float>) {
        _filterUiState.update { it.copy(releaseYearRange = range) }
    }

    override fun onGenreSelectedChange(selectedGenres: List<Int>) {
        _filterUiState.update { it.copy(selectedGenres = selectedGenres) }
    }

    override fun onRatingChanged(selectedRating: Int) {
        _filterUiState.update { it.copy(imdbRating = selectedRating) }
    }
}