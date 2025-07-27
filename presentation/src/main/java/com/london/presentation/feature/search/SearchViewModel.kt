package com.london.presentation.feature.search

import androidx.compose.ui.text.input.TextFieldValue
import com.london.domain.entity.Movie
import com.london.domain.entity.TvShow
import com.london.domain.entity.recent.RecentSearch
import com.london.domain.entity.recent.RecentViewed
import com.london.domain.usecase.GetActorsUseCase
import com.london.domain.usecase.GetGenreInterestCountsUseCase
import com.london.domain.usecase.GetMoviesUseCase
import com.london.domain.usecase.GetTvShowsUseCase
import com.london.domain.usecase.IncrementGenreInterestUseCase
import com.london.domain.usecase.recent.search.AddToRecentSearchUseCase
import com.london.domain.usecase.recent.search.ClearRecentSearchUseCase
import com.london.domain.usecase.recent.search.DeleteRecentSearchUseCase
import com.london.domain.usecase.recent.search.GetRecentSearchUseCase
import com.london.domain.usecase.recent.viewed.AddToRecentViewedUseCase
import com.london.domain.usecase.recent.viewed.ClearRecentViewedUseCase
import com.london.domain.usecase.recent.viewed.GetRecentViewedUseCase
import com.london.presentation.feature.base.BaseViewModel
import com.london.presentation.feature.base.createPagingSourceFlow
import com.london.presentation.feature.search.model.MovieUi
import com.london.presentation.utils.convertGenreCodeToString
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flow
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
    @Provided
    private val deleteRecentSearchUseCase: DeleteRecentSearchUseCase
) : BaseViewModel<SearchUiState, SearchEffect>(SearchUiState()), SearchInteractions {

    private val _searchQuery = MutableStateFlow("")

    init {
        initializeData()
        setupSearchDebouncing()
    }

    private fun initializeData() {
        tryToExecute(
            block = {
                val recentViewed = getRecentViewedUseCase.invoke().reversed()
                val recentSearches = getRecentSearchUseCase.invoke()
                Pair(recentViewed, recentSearches)
            },
            onSuccess = { (recentViewed, recentSearches) ->
                updateState {
                    copy(
                        recentViewed = recentViewed,
                        recentSearches = recentSearches
                    )
                }
            },
            onError = { errorState ->
                updateState { copy(error = errorState) }
            },
        )
    }

    private fun setupSearchDebouncing() {
        tryToCollect(
            block = {
                _searchQuery.debounce(500)
            },
            onNewValue = { query ->
                performSearch(query = query, category = state.value.selectedCategory)
            },
            onError = { errorState ->
                updateState { copy(error = errorState) }
            }
        )
    }

    override fun onSearchQueryChange(newValue: TextFieldValue) {
        val limitedQuery = if (newValue.text.length > MAX_QUERY_SEARCH_LENGTH) {
            newValue.copy(text = newValue.text.take(MAX_QUERY_SEARCH_LENGTH))
        } else {
            newValue
        }

        updateState { copy(searchQuery = limitedQuery) }
        _searchQuery.value = limitedQuery.text
    }

    override fun onCategorySelected(category: SearchCategory) {
        if (category==state.value.selectedCategory) return
        updateState {
            copy(
                selectedCategory = category,
                showFilterButton = category != SearchCategory.Actors
            )
        }

        updateAvailableGenres(category)

        tryToExecute(
            block = {
                performSearch(state.value.searchQuery.text, category)
            },
            onError = { errorState ->
                updateState { copy(error = errorState) }
            },
            checkSuccess = { true }
        )
    }

    override fun onSearchFilterClick(query: String, category: SearchCategory) {
        tryToExecute(
            block = {
                performSearch(query, category)
            },
            onError = { errorState ->
                updateState { copy(error = errorState) }
            },
            checkSuccess = { true }
        )
    }

    override fun onApplyFilter(
        selectedGenres: List<Int>,
        minimumRating: Int,
        releaseYearRange: ClosedFloatingPointRange<Float>
    ) {
        updateState {
            copy(
                selectedGenres = selectedGenres,
                imdbRating = minimumRating,
                releaseYearRange = releaseYearRange
            )
        }

        tryToExecute(
            block = {
                performSearch(state.value.searchQuery.text, state.value.selectedCategory)
            },
            onError = { errorState ->
                updateState { copy(error = errorState) }
            },
            checkSuccess = { true }
        )
    }

    override fun onSavedMovieClick(movie: MovieUi) {
        tryToExecute(
            block = {
                val currentState = state.value
                val isNowSaved = !currentState.savedMovies.contains(movie.id)
                val updatedSavedMovies = if (isNowSaved) {
                    currentState.savedMovies + movie.id
                } else {
                    currentState.savedMovies - movie.id
                }
                updatedSavedMovies
            },
            onSuccess = { updatedSavedMovies ->
                updateState { copy(savedMovies = updatedSavedMovies) }
            },
            onError = { errorState ->
                updateState { copy(error = errorState) }
            },
        )
    }

    override fun addToRecentSearches(query: RecentSearch) {
        if (query.query.isBlank() || query.query == state.value.lastSearch) return
        updateState { copy(lastSearch = query.query) }

        tryToExecute(
            block = {
                addToRecentSearchUseCase.invoke(query)
                getRecentSearchUseCase.invoke().reversed()
            },
            onSuccess = { recentSearches ->
                updateState { copy(recentSearches = recentSearches) }
            },
            onError = { errorState ->
                updateState { copy(error = errorState) }
            },
        )
    }

    override fun addToRecentViewed(item: RecentViewed) {
        tryToExecute(
            block = {
                addToRecentViewedUseCase.invoke(item)
                getRecentViewedUseCase.invoke().reversed()
            },
            onSuccess = { recentViewed ->
                updateState { copy(recentViewed = recentViewed) }
            },
            onError = { errorState ->
                updateState { copy(error = errorState) }
            },
        )
    }

    override fun onClickMovie(genresListId: List<Int>) {
        genresListId.forEach { genreId ->
            incrementGenreInterest(genreId, "tv")
        }
    }

    override fun clearRecentViewed() {
        updateState { copy(recentViewed = emptyList()) }

        tryToExecute(
            block = {
                clearRecentViewedUseCase.invoke()
            },
            onSuccess = { },
            onError = { errorState ->
                updateState { copy(error = errorState) }
            },
            checkSuccess = { true }
        )
    }

    override fun clearRecentSearches() {
        updateState { copy(recentSearches = emptyList()) }

        tryToExecute(
            block = {
                clearRecentSearchUseCase.invoke()
            },
            onError = { errorState ->
                updateState { copy(error = errorState) }
            },
            checkSuccess = { true }
        )
    }

    override fun removeRecentSearch(search: RecentSearch) {
        val updatedSearches = state.value.recentSearches.filter { it != search }
        updateState { copy(recentSearches = updatedSearches) }

        tryToExecute(
            block = {
                deleteRecentSearchUseCase.invoke(search)
            },
            onError = { errorState ->
                updateState { copy(error = errorState) }
            },
            checkSuccess = { true }
        )
    }

    override fun onRecentSearchClick(search: String) {
        updateState { copy(searchQuery = TextFieldValue(search)) }

        tryToExecute(
            block = {
                performSearch(search, state.value.selectedCategory)
            },
            onError = { errorState ->
                updateState { copy(error = errorState) }
            },
            checkSuccess = { true }
        )
    }

    override fun clearSearch() {
        updateState {
            copy(
                searchQuery = TextFieldValue(""),
                actorsFlow = flow {},
                moviesFlow = flow {},
                tvShowsFlow = flow {}
            )
        }
    }

    override fun onClearFilter() {
        updateState {
            copy(
                selectedGenres = emptyList(),
                imdbRating = 0,
                releaseYearRange = 1950f..2030f
            )
        }

        tryToExecute(
            block = {
                performSearch(state.value.searchQuery.text, state.value.selectedCategory)
            },
            onError = { errorState ->
                updateState { copy(error = errorState) }
            },
            checkSuccess = { true }
        )
    }

    override fun onReleaseYearRangeChange(range: ClosedFloatingPointRange<Float>) {
        updateState { copy(releaseYearRange = range) }
    }

    override fun onGenreSelectedChange(selectedGenres: List<Int>) {
        updateState { copy(selectedGenres = selectedGenres) }
    }

    override fun onRatingChanged(selectedRating: Int) {
        updateState { copy(imdbRating = selectedRating) }
    }

    override fun onMovieClick(movieId: Int) {
        emitEffect(SearchEffect.MovieNavigation(movieId = movieId))
    }

    override fun onActorClick(actorId: Int) {
        emitEffect(SearchEffect.ActorNavigation(actorId = actorId))
    }

    override fun onTvShowClick(tvShowId: Int) {
        emitEffect(SearchEffect.TvNavigation(tvId = tvShowId))
    }

    override fun onFilterClick() = updateState { copy(showFilterBottomSheet = true) }

    override fun onFilterSheetDismiss() = updateState { copy(showFilterBottomSheet = false) }

    fun incrementGenreInterest(genreId: Int, mediaType: String) {
        tryToExecute(
            block = {
                incrementGenreInterestUseCase.invoke(genreId, mediaType)
            },
            onStart = { },
            onSuccess = { },
            onError = { errorState ->
                updateState { copy(error = errorState) }
            },
            onCompleted = { },
            checkSuccess = { true }
        )
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
        updateState {
            copy(
                actorsFlow = flow {},
                moviesFlow = flow {},
                tvShowsFlow = flow {}
            )
        }
    }

    private fun searchWithApi(query: String, category: SearchCategory) {
        tryToExecute(
            block = {
                when (category) {
                    SearchCategory.Movies -> searchMovies(query)
                    SearchCategory.Actors -> searchActors(query)
                    SearchCategory.TvShows -> searchTvShows(query)
                }
            },
            onError = { errorState ->
                clearAllSearchResults()
                updateState { copy(error = errorState) }
            },
            checkSuccess = { true }
        )
    }

    private fun searchMovies(query: String) {
        val moviesFlow = createPagingSourceFlow(query) { currentQuery, pageNumber ->
            val movies = getMoviesUseCase(
                name = currentQuery,
                pageNumber = pageNumber
            )
            movies.copy(items = applyMovieFilters(movies.items))
        }

        updateState {
            copy(
                moviesFlow = moviesFlow,
                actorsFlow = flow {},
                tvShowsFlow = flow {}
            )
        }
    }

    private fun searchActors(query: String) {
        val actorsFlow = createPagingSourceFlow(query) { currentQuery, pageNumber ->
            val actors = getActorsUseCase(
                name = currentQuery,
                pageNumber = pageNumber
            )
            actors.copy(items = actors.items)
        }

        updateState {
            copy(
                actorsFlow = actorsFlow,
                moviesFlow = flow {},
                tvShowsFlow = flow {}
            )
        }
    }

    private fun searchTvShows(query: String) {
        val tvShowsFlow = createPagingSourceFlow(query) { currentQuery, pageNumber ->
            val tvShows = getTvShowsUseCase(
                name = currentQuery,
                pageNumber = pageNumber
            )
            tvShows.copy(items = applyTvShowFilters(tvShows.items))
        }

        updateState {
            copy(
                tvShowsFlow = tvShowsFlow,
                actorsFlow = flow {},
                moviesFlow = flow {}
            )
        }
    }

    private fun clearAllSearchResults() {
        updateState {
            copy(
                moviesFlow = flow {},
                tvShowsFlow = flow {},
                actorsFlow = flow {}
            )
        }
    }

    private suspend fun applyMovieFilters(movies: List<Movie>): List<Movie> {
        val filterState = state.value
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
        val filterState = state.value

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

        updateState {
            copy(
                availableGenres = availableGenres,
                availableGenresWithNames = availableGenresWithNames
            )
        }
    }

    private companion object{
        private const val MAX_QUERY_SEARCH_LENGTH = 30
    }
}