package com.london.presentation.screen.details.movieDetalis

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.london.domain.usecase.GetMovieDetailsUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class MovieDetailsViewModel(
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase
) : ViewModel(), MovieDetailsIntersection {

    private val _uiState = MutableStateFlow(MovieDetailsUiState())
    val uiState: StateFlow<MovieDetailsUiState> = _uiState

    init {
        startImageCarousel()
    }

    fun loadMovieDetails(movieId: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true
            )
            val movieDetails = getMovieDetailsUseCase(movieId)

            _uiState.value = movieDetails.toUiState(_uiState.value).copy(
                isLoading = false
            )
        }
    }


    private fun startImageCarousel() {
        viewModelScope.launch {
            while (true) {
                delay(DEALEY_MOVIE_IMAGE_TIME)

                val currentState = _uiState.value
                val images = currentState.movieImage
                if (images.isEmpty() || images.size == 1) continue

                val currentIndex = currentState.currentImageIndex
                val newDirection = when (currentIndex) {
                    images.lastIndex -> -1
                    0 -> 1
                    else -> currentState.imageSlideDirection
                }

                val newIndex = currentIndex + newDirection

                _uiState.value = currentState.copy(
                    currentImageIndex = newIndex, imageSlideDirection = newDirection
                )
            }
        }
    }

    override fun onSavedClick() {
        //TODO("Not yet implemented")
    }

    override fun onExpandClick() {
        _uiState.value = _uiState.value.copy(expanded = !_uiState.value.expanded)
    }

    companion object {
        const val DEALEY_MOVIE_IMAGE_TIME = 4000L
    }
}