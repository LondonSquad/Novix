package com.london.presentation.feature.details.movieDetalis

import androidx.lifecycle.SavedStateHandle
import com.london.domain.usecase.GetMovieById
import com.london.domain.usecase.GetMovieCastUseCase
import com.london.domain.usecase.GetMovieImagesUseCase
import com.london.domain.usecase.GetMovieVideoUseCase
import com.london.domain.usecase.GetSimilarMoviesUseCase
import com.london.presentation.navigation.Screen
import com.london.presentation.navigation.getArgs
import com.london.presentation.feature.base.BaseViewModel
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class MovieDetailsViewModel(
    private val getMovieById: GetMovieById,
    private val getMovieImagesUseCase: GetMovieImagesUseCase,
    private val getMovieCastUseCase: GetMovieCastUseCase,
    private val getSimilarMoviesUseCase: GetSimilarMoviesUseCase,
    private val getMovieVideosUseCase: GetMovieVideoUseCase,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<MovieDetailsUiState, MovieDetailsEffect>(MovieDetailsUiState()),
    MovieDetailsContract {

    private val args = savedStateHandle.getArgs<Screen.MovieDetails>()
    private val movieId = args?.movieId ?: 0

    init {
        loadMovieDetails(movieId)
        loadSimilarAndVideos(movieId)
    }

    override fun onBackClick() = emitEffect(MovieDetailsEffect.BackNavigation)

    override fun onSavedClick() {
        // TODO: implement saving logic
    }

    override fun onExpandClick() {
        updateState { copy(expanded = !expanded) }
    }

    override fun onMovieClick(movieId: Int) {
        emitEffect(MovieDetailsEffect.MovieNavigation(movieId))
    }

    override fun onActorClick(actorId: Int) {
        emitEffect(MovieDetailsEffect.ActorNavigation(actorId))
    }

    override fun onReviewsClick(movieId: Int, mediaNumber: Int) {
        emitEffect(MovieDetailsEffect.ReviewsNavigation(movieId, mediaNumber))
    }

    override fun onGenreClick(genreId: Int) {
        emitEffect(MovieDetailsEffect.GenreNavigation(genreId))
    }

    private fun loadMovieDetails(movieId: Int) {
        tryToExecute(
            block = {
                val movieDetails = getMovieById.invoke(movieId)
                val movieImages = getMovieImagesUseCase.invoke(movieId)
                val movieCast = getMovieCastUseCase.invoke(movieId)
                Triple(movieDetails, movieImages, movieCast)
            },
            onStart = { updateState { copy(isLoading = true) } },
            onSuccess = { triple ->
                val (details, images, cast) = triple
                updateState {
                    copy(
                        movieName = details.title,
                        movieGenres = details.genres,
                        movieRating = details.voteAverage,
                        movieDuration = details.runtime.toString(),
                        releaseDate = details.releaseDate,
                        movieOverview = details.overview,
                        movieImage = images,
                        actors = cast
                    )
                }
            },
            onError = { errorState ->
                updateState { copy(error = errorState) }
            },
            onCompleted = {
                updateState { copy(isLoading = false) }
            }
        )
    }

    private fun loadSimilarAndVideos(movieId: Int) {
        tryToExecute(
            block = {
                val similarMovies = getSimilarMoviesUseCase.invoke(movieId)
                val movieVideos = getMovieVideosUseCase.invoke(movieId)
                Pair(similarMovies, movieVideos)
            },
            onSuccess = { pair ->
                val (similarMovies, videos) = pair
                updateState {
                    copy(
                        similarMovies = similarMovies,
                        movieVideo = videos.firstOrNull()?.videoUrl.orEmpty()
                    )
                }
            },
            onError = { errorState ->
                updateState { copy(error = errorState) }
            },
            onCompleted = {
                updateState { copy(isLoading = false) }
            }
        )
    }
}
