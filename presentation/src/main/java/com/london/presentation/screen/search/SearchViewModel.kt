package com.london.presentation.screen.search

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import com.london.presentation.screen.search.model.MovieUi
import com.london.presentation.screen.search.model.TvShowUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SearchViewModel : ViewModel(), SearchInteractions {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    fun isMovieSaved(movie: MovieUi) = _uiState.value.savedMovies.contains(movie.id)

    override fun onSearchQueryChange(newValue: TextFieldValue) {
        _uiState.update { it.copy(searchQuery = newValue) }
        onSearchFilterClick(newValue.text, _uiState.value.selectedCategory)
    }

    override fun onCategorySelected(category: SearchCategory) {
        _uiState.update { it.copy(selectedCategory = category) }
        onSearchFilterClick(_uiState.value.searchQuery.text, category)
    }

    override fun onSearchFilterClick(query: String, category: SearchCategory) {
        val trimmedQuery = query.trim().lowercase()

        _uiState.update { currentState ->
            when (category) {
                SearchCategory.Movies -> {
                    val filteredMovies = if (trimmedQuery.isNotEmpty()) {
                        DummyData.dummyMoviesList.map {
                            it.copy(isSaved = currentState.savedMovies.contains(it.id))
                        }.filter {
                            it.title.lowercase().contains(trimmedQuery)
                        }
                    } else emptyList()

                    currentState.copy(
                        movieResults = filteredMovies,
                        actorUiResults = emptyList(),
                        tvShowUiResults = emptyList()
                    )
                }

                SearchCategory.Actors -> {
                    val filteredActors = if (trimmedQuery.isNotEmpty()) {
                        DummyData.dummyActorsList.filter {
                            it.name.lowercase().contains(trimmedQuery)
                        }
                    } else emptyList()

                    currentState.copy(
                        actorUiResults = filteredActors,
                        movieResults = emptyList(),
                        tvShowUiResults = emptyList()
                    )
                }

                SearchCategory.TvShows -> {
                    val filteredShows = if (trimmedQuery.isNotEmpty()) {
                        DummyData.dummyTvShowsList.filter {
                            it.title.lowercase().contains(trimmedQuery)
                        }
                    } else emptyList()

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

            val updatedMovies = currentState.movieResults.map {
                if (it.id == movie.id) it.copy(isSaved = isNowSaved) else it
            }

            currentState.copy(
                savedMovies = updatedSavedMovies,
                movieResults = updatedMovies
            )
        }
    }


    fun addToRecentSearches(query: String) {
        val currentSearches = _uiState.value.recentSearches.toMutableList()

        currentSearches.remove(query)
        currentSearches.add(0, query)

        if (currentSearches.size > 10) {
            currentSearches.removeAt(currentSearches.size - 1)
        }

        _uiState.value = _uiState.value.copy(recentSearches = currentSearches)
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
        onSearchFilterClick(search, _uiState.value.selectedCategory)
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
