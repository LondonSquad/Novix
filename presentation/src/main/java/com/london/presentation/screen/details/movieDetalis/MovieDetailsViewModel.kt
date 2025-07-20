package com.london.presentation.screen.details.movieDetalis

import androidx.lifecycle.SavedStateHandle
import com.london.domain.usecase.GetMovieDetailsUseCase
import com.london.domain.usecase.GetMovieVideoUseCase
import com.london.presentation.navigation.Screen
import com.london.presentation.navigation.getArgs
import com.london.presentation.screen.base.BaseViewModel
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class MovieDetailsViewModel(
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase,
    private val getMovieVideosUseCase: GetMovieVideoUseCase,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<MovieDetailsUiState, MovieDetailsEffect>(MovieDetailsUiState()),
    MovieDetailsContract {

    private val args = savedStateHandle.getArgs<Screen.MovieDetails>()
    val movieId = args?.movieId ?: 0

    init {
        loadMovieDetails(movieId)
    }

    override fun onBackClick() {
        emitEffect(MovieDetailsEffect.BackNavigation)
    }

    override fun onSavedClick() {
        //TODO("Not yet implemented")
    }

    override fun onExpandClick() {
        updateState { copy(expanded = !this.expanded) }
    }

    override fun onMovieClick(movieId: Int) {
        emitEffect(MovieDetailsEffect.MovieNavigation(movieId))
    }

    override fun onActorClick(actorId: Int) {
        emitEffect(MovieDetailsEffect.ActorNavigation(actorId))
    }

    override fun onReviewsClick(movieId: Int, mediaNumber: Int) {
        emitEffect(
            MovieDetailsEffect.ReviewsNavigation(
                movieId = movieId,
                mediaNumber = mediaNumber
            )
        )
    }

    override fun onGenreClick(genreId: Int) {
        emitEffect(MovieDetailsEffect.GenreNavigation(genreId))
    }

    private fun loadMovieDetails(movieId: Int) {

        tryToExecute(
            block = {
                val movieDetails = getMovieDetailsUseCase(movieId)
                val movieVideos = getMovieVideosUseCase.invoke(movieId)
                Pair(movieDetails, movieVideos)
            },
            onStart = { updateState { copy(isLoading = true) } },
            onSuccess = { (movieDetails, movieVideos) ->
                updateState {
                    movieDetails.toUiState(this).copy(
                        isLoading = false,
                        movieVideo = movieVideos.firstOrNull()?.videoUrl.orEmpty()
                    )
                }
            },
            onError = { errorState ->
                updateState { copy(error = errorState) }
            },
            onCompleted = { updateState { copy(isLoading = false) } },
            checkSuccess = { movieId != 0 }
        )
    }
}