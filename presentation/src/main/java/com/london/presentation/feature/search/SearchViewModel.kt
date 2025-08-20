package com.london.presentation.feature.search

import androidx.compose.ui.text.input.TextFieldValue
import com.london.domain.entity.genre.Genre
import com.london.domain.entity.recent.RecentSearch
import com.london.domain.entity.recent.RecentViewed
import com.london.domain.usecase.recent.search.ManageRecentSearchUseCase
import com.london.domain.usecase.recent.viewed.ManageRecentViewedUseCase
import com.london.domain.usecase.search.ManageSearchUseCase
import com.london.presentation.shared.base.BaseViewModel
import com.london.presentation.shared.base.createPagingSourceFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val manageSearchUseCase: ManageSearchUseCase,
    private val manageRecentSearchUseCase: ManageRecentSearchUseCase,
    private val manageRecentViewedUseCase: ManageRecentViewedUseCase,
) : BaseViewModel<SearchUiState, SearchEffect>(SearchUiState()), SearchContract {

    private val _searchQuery = MutableStateFlow("")

    init {
        getSearchInfo()
    }

    private fun getSearchInfo() {
        updateRecentData()
        setupSearchDebouncing()
    }

    override fun incrementGenreInterest(genre: Genre, mediaType: String) {
        tryToExecute(
            block = { manageSearchUseCase.incrementGenreInterest(genre, mediaType) },
            onError = { errorState -> updateState { copy(error = errorState) } },
        )
    }

    override fun performSearch(query: String, category: SearchCategory) {
        val trimmedQuery = query.trim()
        if (trimmedQuery.isEmpty()) {
            clearSearchResults()
            return
        }
        searchWithApi(trimmedQuery, category)
    }

    override fun updateRecentData() {
        tryToExecute(
            block = { getRecentData() },
            onSuccess = { (recentViewed, recentSearches) ->
                updateRecentViewedState(recentViewed)
                updateRecentSearchesState(recentSearches)
            },
            onError = { errorState ->
                updateState { copy(error = errorState) }
            },
        )
    }

    override fun updateSearchState(updater: SearchUiState.() -> SearchUiState) {
        updateState(updater)
    }

    override fun onSearchQueryChange(newValue: TextFieldValue) {
        updateState { copy(searchQuery = newValue) }

        val limitedQuery = applyLimitationOnTextFieldValue(newValue)
        updateState { copy(searchQuery = limitedQuery) }
        _searchQuery.value = limitedQuery.text.trim()
    }

    override fun onCategorySelected(category: SearchCategory) {
        updateSelectedCategoryState(category)
        executeSearchForCategory(category)
    }

    override fun onSavedMovieClick(movie: MovieUi) {
        tryToExecute(
            block = { updateSavedMoviesList(movie.id) },
            onSuccess = { updatedSavedMovies -> updateSavedMoviesState(updatedSavedMovies) },
            onError = { errorState -> updateState { copy(error = errorState) } },
        )
    }

    override fun addToRecentSearches(query: String) {
        if (query.isBlank() || query == state.value.lastSearch) return
        if (isQueryDuplicated(query)) return

        updateState { copy(lastSearch = query) }

        tryToExecute(
            block = {
                manageRecentSearchUseCase.addToRecentSearch(RecentSearch(query = query))
                manageRecentSearchUseCase.getRecentSearch().reversed()
            },
            onSuccess = { recentSearches -> updateState { copy(recentSearches = recentSearches) } },
            onError = { errorState -> updateState { copy(error = errorState) } },
        )
    }

    override fun addToRecentViewed(item: RecentViewed) {
        tryToExecute(
            block = {
                manageRecentViewedUseCase.addToRecentViewed(item)
                manageRecentViewedUseCase.getRecentViewed().reversed()
            },
            onSuccess = { recentViewed -> updateState { copy(recentViewed = recentViewed) } },
            onError = { errorState -> updateState { copy(error = errorState) } },
        )
    }

    override fun onMovieGenreClick(genresList: List<Genre>) {
        genresList.forEach { genre -> incrementGenreInterest(genre, "movie") }
    }

    override fun onTvShowGenreClick(genresList: List<Genre>) {
        genresList.forEach { genre -> incrementGenreInterest(genre, "tv") }
    }

    override fun clearRecentViewed() {
        updateState { copy(recentViewed = emptyList()) }

        tryToExecute(
            block = { manageRecentViewedUseCase.clearRecentViewed() },
            onError = { errorState -> updateState { copy(error = errorState) } },
        )
    }

    override fun clearRecentSearches() {
        updateState { copy(recentSearches = emptyList()) }

        tryToExecute(
            block = { manageRecentSearchUseCase.clearRecentSearch() },
            onError = { errorState -> updateState { copy(error = errorState) } },
        )
    }

    override fun removeRecentSearch(search: RecentSearch) {
        val updatedSearches = state.value.recentSearches.filter { it != search }
        updateState { copy(recentSearches = updatedSearches) }

        tryToExecute(
            block = { manageRecentSearchUseCase.deleteRecentSearch(search) },
            onError = { errorState -> updateState { copy(error = errorState) } },
        )
    }

    override fun onRecentSearchClick(search: String) {
        updateState { copy(searchQuery = TextFieldValue(search)) }

        tryToExecute(
            block = { performSearch(search, state.value.selectedCategory) },
            onError = { errorState -> updateState { copy(error = errorState) } },
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

    override fun onMovieClick(movieId: Int) {
        emitEffect(SearchEffect.MovieDetailsNavigation(movieId = movieId))
    }

    override fun onActorClick(actorId: Int) {
        emitEffect(SearchEffect.ActorDetailsNavigation(actorId = actorId))
    }

    override fun onTvShowClick(tvShowId: Int) {
        emitEffect(SearchEffect.TvShowDetailsNavigation(tvId = tvShowId))
    }

    override fun onRetryClick() {
        updateState { copy(error = null) }
        updateRecentData()
        setupSearchDebouncing()
    }

    override fun onManageBookmarkClick(movieId: Int) {
        updateState {
            copy(
                isBookmarkSheetVisible = true,
                bookmarkedMovieId = movieId
            )
        }
    }

    override fun onBookmarkSheetDismiss() {
        updateState {
            copy(
                isBookmarkSheetVisible = false,
                bookmarkedMovieId = 0
            )
        }
    }

    private suspend fun getRecentData(): Pair<List<RecentViewed>, List<RecentSearch>> {
        val recentViewed = manageRecentViewedUseCase.getRecentViewed().reversed()
        val recentSearches = manageRecentSearchUseCase.getRecentSearch()
        return Pair(recentViewed, recentSearches)
    }

    private fun setupSearchDebouncing() {
        tryToCollect(
            block = { _searchQuery.debounce(500) },
            onNewValue = { query -> executeDebouncedSearch(query) },
            onError = { errorState -> updateState { copy(error = errorState) } }
        )
    }

    private fun isQueryDuplicated(query: String) =
        query.equals(state.value.lastSearch, ignoreCase = true)

    private fun applyLimitationOnTextFieldValue(newValue: TextFieldValue): TextFieldValue =
        newValue.copy(
            text = newValue.text.replace(regex = Regex("\\s{2,}"), replacement = " ")
                .trimStart()
        )

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
        )
    }

    private fun searchMovies(query: String) {
        val moviesFlow = createPagingSourceFlow(query) { currentQuery, pageNumber ->
            manageSearchUseCase.searchForMovies(
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
            val actors = manageSearchUseCase.searchForActors(
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
            manageSearchUseCase.searchForTvShows(
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

    private fun updateRecentViewedState(recentViewed: List<RecentViewed>) {
        updateState { copy(recentViewed = recentViewed) }
    }

    private fun updateRecentSearchesState(recentSearches: List<RecentSearch>) {
        updateState { copy(recentSearches = recentSearches) }
    }

    private fun updateSelectedCategoryState(category: SearchCategory) {
        updateState { copy(selectedCategory = category) }
    }

    private fun updateSavedMoviesList(movieId: Int): Set<Int> {
        val currentState = state.value
        val isNowSaved = !currentState.savedMovies.contains(movieId)
        return if (isNowSaved) {
            currentState.savedMovies + movieId
        } else {
            currentState.savedMovies - movieId
        }
    }

    private fun updateSavedMoviesState(updatedSavedMovies: Set<Int>) {
        updateState { copy(savedMovies = updatedSavedMovies) }
    }

    private fun executeSearchForCategory(category: SearchCategory) {
        tryToExecute(
            block = { performSearch(state.value.searchQuery.text, category) },
            onError = { errorState -> updateState { copy(error = errorState) } },
        )
    }

    private fun executeDebouncedSearch(query: String) {
        performSearch(
            query = query,
            category = state.value.selectedCategory
        )
    }

}
