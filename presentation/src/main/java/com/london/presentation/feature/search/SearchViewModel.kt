package com.london.presentation.feature.search

import androidx.compose.ui.text.input.TextFieldValue
import com.london.domain.entity.recent.RecentSearch
import com.london.domain.entity.recent.RecentViewed
import com.london.domain.usecase.GetActorsUseCase
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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getActorsUseCase: GetActorsUseCase,
    private val getTvShowsUseCase: GetTvShowsUseCase,
    private val getMoviesUseCase: GetMoviesUseCase,
    private val addToRecentSearchUseCase: AddToRecentSearchUseCase,
    private val getRecentSearchUseCase: GetRecentSearchUseCase,
    private val clearRecentSearchUseCase: ClearRecentSearchUseCase,
    private val incrementGenreInterestUseCase: IncrementGenreInterestUseCase,
    private val getRecentViewedUseCase: GetRecentViewedUseCase,
    private val addToRecentViewedUseCase: AddToRecentViewedUseCase,
    private val clearRecentViewedUseCase: ClearRecentViewedUseCase,
    private val deleteRecentSearchUseCase: DeleteRecentSearchUseCase
) : BaseViewModel<SearchUiState, SearchEffect>(SearchUiState()), SearchContract {

    private val _searchQuery = MutableStateFlow("")

    init {
        updateRecentData()
        setupSearchDebouncing()
    }

    fun updateRecentData() {
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

    fun updateSearchState(updater: SearchUiState.() -> SearchUiState) {
        updateState(updater)
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
        updateState { copy(searchQuery = newValue) }

        val limitedQuery = applyLimitationOnTextFieldValue(newValue)
        updateState { copy(searchQuery = limitedQuery) }
        _searchQuery.value = limitedQuery.text.trim()
    }

    private fun applyLimitationOnTextFieldValue(newValue: TextFieldValue): TextFieldValue =
        newValue.copy(
            text = newValue.text.replace(regex = Regex("\\s{2,}"), replacement = " ")
                .trimStart()
        )

    override fun onCategorySelected(category: SearchCategory) {
        updateState {
            copy(
                selectedCategory = category
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

    override fun onGenreSelectedChange(selectedGenres: List<Int>) {
        updateState { copy(selectedGenres = selectedGenres) }
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

    fun performSearch(query: String, category: SearchCategory) {
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
            getMoviesUseCase(
                name = currentQuery,
                pageNumber = pageNumber
            )
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
            getTvShowsUseCase(
                name = currentQuery,
                pageNumber = pageNumber
            )
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

    private fun updateAvailableGenres(searchCategory: SearchCategory) {
        val availableGenres = when (searchCategory) {
            SearchCategory.Movies -> availableMovieGenres
            SearchCategory.TvShows -> availableTvGenres
            SearchCategory.Actors -> emptyList()
        }

        updateState {
            copy(availableGenres = availableGenres)
        }
    }

    override fun onRetry() {
        updateState { copy(error = null) }
        updateRecentData()
        setupSearchDebouncing()
    }
}