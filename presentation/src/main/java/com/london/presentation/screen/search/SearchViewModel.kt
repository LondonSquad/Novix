package com.london.presentation.screen.search

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.london.domain.entity.Movie
import com.london.domain.entity.TvShow
import com.london.domain.entity.recent.RecentViewed
import com.london.domain.usecase.AddToRecentSearchUseCase
import com.london.domain.usecase.AddToRecentViewedUseCase
import com.london.domain.usecase.ClearRecentSearchUseCase
import com.london.domain.usecase.ClearRecentViewedUseCase
import com.london.domain.usecase.GetActorsUseCase
import com.london.domain.usecase.GetGenreInterestCountsUseCase
import com.london.domain.usecase.GetMoviesUseCase
import com.london.domain.usecase.GetRecentSearchUseCase
import com.london.domain.usecase.GetRecentViewedUseCase
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
import org.koin.core.annotation.Provided

@OptIn(FlowPreview::class)
@KoinViewModel
class SearchViewModel(
    @Provided
    private val getActorsUseCase: GetActorsUseCase,
    @Provided
    private val getTvShowsUseCase: GetTvShowsUseCase,
    @Provided
    private val getMoviesUseCase: GetMoviesUseCase,
    @Provided
    private val addToRecentSearchUseCase: AddToRecentSearchUseCase,
    @Provided
    private val getRecentSearchUseCase: GetRecentSearchUseCase,
    @Provided
    private val clearRecentSearchUseCase: ClearRecentSearchUseCase,
    @Provided
    private val getGenreInterestCountsUseCase: GetGenreInterestCountsUseCase,
    @Provided
    private val incrementGenreInterestUseCase: IncrementGenreInterestUseCase,
    @Provided
    private val getRecentViewedUseCase: GetRecentViewedUseCase,
    @Provided
    private val addToRecentViewedUseCase: AddToRecentViewedUseCase,
    @Provided
    private val clearRecentViewedUseCase: ClearRecentViewedUseCase,
) : ViewModel(), SearchInteractions {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private val _filterUiState = MutableStateFlow(FilterBottomSheetUiState())
    var filterUiState: StateFlow<FilterBottomSheetUiState> = _filterUiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")

    init {
        viewModelScope.launch {
            _uiState.update { it.copy(recentViewed = getRecentViewedUseCase.invoke()) }
            _searchQuery.debounce(500).collectLatest { query ->
                performSearch(
                    query = query, category = _uiState.value.selectedCategory
                )
            }
        }
    }

    override fun onSearchQueryChange(newValue: TextFieldValue) {
        _uiState.update { it.copy(searchQuery = newValue) }
        _searchQuery.value = newValue.text
    }


    override fun onCategorySelected(category: SearchCategory) {
        _uiState.update {
            it.copy(
                selectedCategory = category,
                showFilterButton = category != SearchCategory.Actors
            )
        }

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

    private var lastQuery = ""
    override fun addToRecentSearches(query: String) {
        if (query.isBlank()|| query == lastQuery) return
        lastQuery = query
        viewModelScope.launch {
            addToRecentSearchUseCase.invoke(query)
            _uiState.update { it.copy(recentSearches = getRecentSearchUseCase.invoke()) }
        }
    }

    override fun addToRecentViewed(item: RecentViewed) {
        viewModelScope.launch {
            addToRecentViewedUseCase.invoke(item)
            _uiState.update { state ->
                state.copy(
                    recentViewed = getRecentViewedUseCase.invoke()
                        .sortedByDescending { it.viewDate })
            }
        }
    }

    override fun onClickMovie(genresListId: List<Int>) {
        genresListId.forEach { genreId ->
            incrementGenreInterest(genreId, "tv")
        }

    }

    override fun clearRecentViewed() {
        _uiState.update { it.copy(recentViewed = emptyList()) }
        viewModelScope.launch {
            clearRecentViewedUseCase.invoke()
        }
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
                tvShowsFlow = flow {})
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

    fun incrementGenreInterest(genreId: Int, mediaType: String) {
        viewModelScope.launch {
            incrementGenreInterestUseCase.invoke(genreId, mediaType)
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
            currentState.copy(actorsFlow = flow {}, moviesFlow = flow {}, tvShowsFlow = flow {})
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
        val moviesFlow = createPagingSourceFlow(query) { currentQuery, pageNumber ->
            val movies = getMoviesUseCase(
                name = currentQuery, language = "en-US", pageNumber = pageNumber
            )
            movies.copy(items = applyMovieFilters(movies.items))
        }

        _uiState.update { currentState ->
            currentState.copy(moviesFlow = moviesFlow, actorsFlow = flow {}, tvShowsFlow = flow {})
        }
    }


    private fun searchActors(query: String) {
        val actorsFlow = createPagingSourceFlow(query) { currentQuery, pageNumber ->
            val actors = getActorsUseCase(
                name = currentQuery, language = "en-US", pageNumber = pageNumber
            )
            actors.copy(items = actors.items)
        }

        _uiState.update { currentState ->
            currentState.copy(actorsFlow = actorsFlow, moviesFlow = flow {}, tvShowsFlow = flow {})
        }
    }

    private fun searchTvShows(query: String) {

        val tvShowsFlow = createPagingSourceFlow(query) { currentQuery, pageNumber ->
            val tvShows = getTvShowsUseCase(
                name = currentQuery, language = "en-US", pageNumber = pageNumber
            )
            tvShows.copy(items = applyTvShowFilters(tvShows.items))
        }

        _uiState.update { currentState ->
            currentState.copy(tvShowsFlow = tvShowsFlow, actorsFlow = flow {}, moviesFlow = flow {})
        }
    }


    private fun clearAllSearchResults() {
        _uiState.update { currentState ->
            currentState.copy(moviesFlow = flow {}, tvShowsFlow = flow {}, actorsFlow = flow {})
        }
    }

    private suspend fun applyMovieFilters(movies: List<Movie>): List<Movie> {
        val filterState = _filterUiState.value
        val interests = getGenreInterestCountsUseCase.invoke("movie")
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

        val interests = getGenreInterestCountsUseCase.invoke("tv")
        val interestMap = interests.associate { it.first to it.second }

        val tvShowsFiltered = tvShows.filter { tvShow ->
            val matchesGenres = filterState.selectedGenres.isEmpty() || tvShow.genres.any { genre ->
                filterState.selectedGenres.contains(genre)
            }

            val matchesRating = tvShow.rating >= filterState.imdbRating

            val matchesYear =
                tvShow.releaseYear in filterState.releaseYearRange.start.toInt()..filterState.releaseYearRange.endInclusive.toInt()

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