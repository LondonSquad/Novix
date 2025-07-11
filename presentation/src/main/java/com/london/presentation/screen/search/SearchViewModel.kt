package com.london.presentation.screen.search

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.london.domain.entity.Actor
import com.london.domain.entity.Movie
import com.london.domain.entity.TvShow
import com.london.domain.repo.SearchRepository
import com.london.presentation.screen.search.model.MovieUi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class SearchViewModel(
    private val searchRepository: SearchRepository
) : ViewModel(), SearchInteractions {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private var allMovies: List<Movie> = emptyList()
    private var allTvShows: List<TvShow> = emptyList()
    private var allActors: List<Actor> = emptyList()

    init {
        loadInitialAllData()
    }

    private fun loadInitialAllData() {
        viewModelScope.launch(Dispatchers.IO) {
            allMovies = searchRepository.searchForMovies("", "en")
            allTvShows = searchRepository.searchForTvShows("", "en")
            allActors = searchRepository.searchForActors("", "en")

            _uiState.update {
                it.copy(
                    movieResults = allMovies,
                    tvShowUiResults = allTvShows,
                    actorUiResults = allActors
                )
            }
        }
    }

    override fun onSearchQueryChange(newValue: TextFieldValue) {
        _uiState.update { it.copy(searchQuery = newValue) }
        performSearch(newValue.text, _uiState.value.selectedCategory)
    }

    override fun onCategorySelected(category: SearchCategory) {
        _uiState.update { it.copy(selectedCategory = category) }
        performSearch(_uiState.value.searchQuery.text, category)
    }

    override fun onSearchFilterClick(query: String, category: SearchCategory) {
        performSearch(query, category)
    }

    private fun performSearch(query: String, category: SearchCategory) {
        val trimmedQuery = query.trim()

        if (trimmedQuery.isEmpty()) {
            _uiState.update { currentState ->
                currentState.copy(
                    movieResults = emptyList(),
                    tvShowUiResults = emptyList(),
                    actorUiResults = emptyList()
                )
            }
            return
        }

        filterDataByQuery(trimmedQuery, category)
    }

    private fun filterDataByQuery(query: String, category: SearchCategory) {
        val queryLower = query.lowercase()

        _uiState.update { currentState ->
            when (category) {
                SearchCategory.Movies -> {
                    val filteredMovies = allMovies.filter {
                        it.name.lowercase().contains(queryLower)
                    }
                    currentState.copy(
                        movieResults = filteredMovies,
                        actorUiResults = emptyList(),
                        tvShowUiResults = emptyList()
                    )
                }

                SearchCategory.Actors -> {
                    val filteredActors = allActors.filter {
                        it.name.lowercase().contains(queryLower)
                    }
                    currentState.copy(
                        actorUiResults = filteredActors,
                        movieResults = emptyList(),
                        tvShowUiResults = emptyList()
                    )
                }

                SearchCategory.TvShows -> {
                    val filteredShows = allTvShows.filter {
                        it.name.lowercase().contains(queryLower)
                    }
                    currentState.copy(
                        tvShowUiResults = filteredShows,
                        actorUiResults = emptyList(),
                        movieResults = emptyList()
                    )
                }
            }
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
        performSearch(search, _uiState.value.selectedCategory)
    }

    fun clearSearch() {
        _uiState.update {
            it.copy(
                searchQuery = TextFieldValue(""),
                movieResults = emptyList(),
                tvShowUiResults = emptyList(),
                actorUiResults = emptyList()
            )
        }
    }
}