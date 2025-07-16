package com.london.presentation.screen.search

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.london.domain.entity.Movie
import com.london.domain.entity.TvShow
import com.london.domain.usecase.AddToRecentSearchUseCase
import com.london.domain.usecase.ClearRecentSearchUseCase
import com.london.domain.usecase.GetActorsUseCase
import com.london.domain.usecase.GetGenreInterestCountsUseCase
import com.london.domain.usecase.GetMoviesUseCase
import com.london.domain.usecase.GetRecentSearchUseCase
import com.london.domain.usecase.GetTvShowsUseCase
import com.london.domain.usecase.IncrementGenreInterestUseCase
import com.london.presentation.composables.filterbottomsheet.FilterBottomSheetUiState
import com.london.presentation.composables.filterbottomsheet.availableMovieGenres
import com.london.presentation.composables.filterbottomsheet.availableTvGenres
import com.london.presentation.screen.base.createPagingSourceFlow
import com.london.presentation.screen.search.model.MovieUi
import com.london.presentation.utils.convertGenreCodeToString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@OptIn(FlowPreview::class)
@KoinViewModel
class SearchViewModel(
    private val getActorsUseCase: GetActorsUseCase,
    private val getTvShowsUseCase: GetTvShowsUseCase,
    private val getMoviesUseCase: GetMoviesUseCase,
    private val addToRecentSearchUseCase: AddToRecentSearchUseCase,
    private val getRecentSearchUseCase: GetRecentSearchUseCase,
    private val clearRecentSearchUseCase: ClearRecentSearchUseCase,
    private val getGenreInterestCountsUseCase: GetGenreInterestCountsUseCase,
    private val incrementGenreInterestUseCase: IncrementGenreInterestUseCase,
) : ViewModel(), SearchInteractions {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private val _filterUiState = MutableStateFlow(FilterBottomSheetUiState())
    var filterUiState: StateFlow<FilterBottomSheetUiState> = _filterUiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")

    init {
        viewModelScope.launch {
            _searchQuery
                .debounce(500)
                .collectLatest { query ->
                    performSearch(
                        query = query,
                        category = _uiState.value.selectedCategory
                    )
                }
        }
    }

    override fun onSearchQueryChange(newValue: TextFieldValue) {
        _uiState.update { it.copy(searchQuery = newValue) }
        _searchQuery.value = newValue.text
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

    override fun onClickMovie(id :Int) {
        incrementGenreInterest(id, "tv")
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
                actorsFlow = flow {},
                moviesFlow = flow {},
                tvShowsFlow = flow {}
            )
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

    fun incrementGenreInterest(genreId: Int, genreType: String) {
        viewModelScope.launch {
            incrementGenreInterestUseCase(genreId, genreType)
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
                actorsFlow = flow {},
                moviesFlow = flow {},
                tvShowsFlow = flow {}
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

    private fun searchMovies(query: String) {
        val moviesFlow = createPagingSourceFlow { pageNumber ->
            val movies = getMoviesUseCase(
                name = query,
                language = "en-US",
                pageNumber = pageNumber
            )
            movies.copy(items = applyMovieFilters(movies.items))
        }

        _uiState.update { currentState ->
            currentState.copy(
                moviesFlow = moviesFlow,
                actorsFlow = flow {},
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
                moviesFlow = flow {},
                tvShowsFlow = flow {}
            )
        }
    }

    private fun searchTvShows(query: String) {
        val tvShowsFlow = createPagingSourceFlow { pageNumber ->
            val tvShows = getTvShowsUseCase(
                name = query,
                language = "en-US",
                pageNumber = pageNumber
            )
            tvShows.copy(items = applyTvShowFilters(tvShows.items))
        }

        _uiState.update { currentState ->
            currentState.copy(
                tvShowsFlow = tvShowsFlow,
                actorsFlow = flow {},
                moviesFlow = flow {}
            )
        }
    }


    private fun clearAllSearchResults() {
        _uiState.update { currentState ->
            currentState.copy(
                moviesFlow = flow {},
                tvShowsFlow = flow {},
                actorsFlow = flow {}
            )
        }
    }

    private suspend fun applyMovieFilters(movies: List<Movie>): List<Movie> {
        val filterState = _filterUiState.value
        val interests = getGenreInterestCountsUseCase("movie")
        val interestMap = interests.associate { it.first to it.second }

        val movieFiltered = movies.filter { movie ->
            val matchesGenres =
                filterState.selectedGenres.isEmpty() || movie.genreIds.any { genre ->
                    filterState.selectedGenres.contains(genre)
                }

            val matchesRating = movie.rating >= filterState.imdbRating

            val matchesYear =
                movie.releaseYear in filterState.releaseYearRange.start.toInt()..filterState.releaseYearRange.endInclusive.toInt()

            matchesGenres && matchesRating && matchesYear
        }

        return movieFiltered.sortedByDescending { movie ->
            movie.genreIds.maxOfOrNull { genreId ->
                interestMap[genreId] ?: 0
            } ?: 0
        }
    }

    private suspend fun applyTvShowFilters(tvShows: List<TvShow>): List<TvShow> {
        val filterState = _filterUiState.value

        val interests = getGenreInterestCountsUseCase("tv")
        val interestMap = interests.associate { it.first to it.second }

        val tvShowsFiltered = tvShows.filter { tvShow ->
            val matchesGenres = filterState.selectedGenres.isEmpty() ||
                    tvShow.genres.any { genre ->
                        filterState.selectedGenres.contains(genre)
                    }

            val matchesRating = tvShow.rating >= filterState.imdbRating

            val matchesYear = tvShow.releaseYear in
                    filterState.releaseYearRange.start.toInt()..filterState.releaseYearRange.endInclusive.toInt()

            matchesGenres && matchesRating && matchesYear
        }

        return tvShowsFiltered.sortedByDescending { tv ->
            tv.genres.maxOfOrNull { genreId ->
                interestMap[genreId] ?: 0
            } ?: 0
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
}