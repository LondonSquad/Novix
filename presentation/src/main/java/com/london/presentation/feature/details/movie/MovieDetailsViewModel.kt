package com.london.presentation.feature.details.movie

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import com.london.domain.entity.Movie
import com.london.domain.entity.recent.MediaType
import com.london.domain.entity.recent.RecentViewed
import com.london.domain.usecase.authentication.AuthenticationUseCase
import com.london.domain.usecase.details.movie.ManageMovieDetailsUseCase
import com.london.domain.usecase.rating.RatingUseCase
import com.london.domain.usecase.recent.viewed.ManageRecentViewedUseCase
import com.london.domain.usecase.recent.watched.movie.ManageRecentMovieWatchedUseCase
import com.london.presentation.navigation.Screen
import com.london.presentation.navigation.getArgs
import com.london.presentation.shared.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val movieDetails: ManageMovieDetailsUseCase,
    private val manageRecentMovieWatchedUseCase: ManageRecentMovieWatchedUseCase,
    private val manageRecentViewedUseCase: ManageRecentViewedUseCase,
    private val ratingUseCase: RatingUseCase,
    private val authenticationUseCase: AuthenticationUseCase,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<MovieDetailsUiState, MovieDetailsEffect>(MovieDetailsUiState()),
    MovieDetailsContract {

    private val args = savedStateHandle.getArgs<Screen.MovieDetails>()
    private val movieId = args?.movieId ?: 0

    init {
        loadMovieDetails(movieId)
        loadSimilarAndVideos(movieId)
    }

    override fun onBackClick() {
        emitEffect(MovieDetailsEffect.BackNavigation)
    }

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

    override fun onLoginClick() {
        emitEffect(MovieDetailsEffect.OnLoginNavigation)
    }

    override fun onReviewsClick(movieId: Int, mediaNumber: Int) {
        emitEffect(MovieDetailsEffect.ReviewsNavigation(movieId, mediaNumber))
    }

    override fun onGenreClick(genreId: Int) {
        emitEffect(MovieDetailsEffect.GenreNavigation(genreId))
    }

    override fun onRetry() {
        updateState { copy(error = null) }
        loadMovieDetails(movieId)
        loadSimilarAndVideos(movieId)
    }

    override fun onRateBottomSheetClick() {
        tryToExecute(
            block = { authenticationUseCase.isLoggedIn() },
            onSuccess = { isLoggedIn ->
                if (isLoggedIn)
                    updateState { copy(isRateBottomSheetVisible = isRateBottomSheetVisible.not()) }
                else
                    updateState {
                        copy(
                            isGuestUserBottomSheetVisible = isGuestUserBottomSheetVisible.not(),
                            isGuestUser = true
                        )

                    }
            },
            onError = { errorState ->
                updateState { copy(error = errorState) }
            }
        )
    }

    override fun onSelectRatingClick(rating: Int) {
        tryToExecute(
            block = { ratingUseCase.addMovieRatingById(movieId, rating) },
            onSuccess = {
                updateState {
                    copy(
                        selectedRating = rating,
                        isRated = true,
                        isRateBottomSheetVisible = false,
                        isSuccessfullyRated = true
                    )
                }
            },
            onError = { errorState ->
                updateState {
                    copy(
                        error = errorState,
                        isSuccessfullyRated = false
                    )
                }
            },
            onCompleted = { updateState { copy(isLoading = false) } },
        )
    }

    private fun loadMovieDetails(movieId: Int) {
        tryToExecute(
            block = {
                val movie = movieDetails.getMovieDetails(movieId)
                val movieImages = movieDetails.getMovieImagesUseCase(movieId)
                val movieCast = movieDetails.getMovieCast(movieId)
                Triple(movie, movieImages, movieCast)
            },
            onStart = { updateState { copy(isLoading = true) } },
            onSuccess = { triple ->
                val (details, images, cast) = triple
                updateState {
                    copy(
                        movieId = details.id,
                        movieName = details.title,
                        movieGenres = details.genresId,
                        movieRating = details.voteAverage,
                        movieDuration = details.runtime.toString(),
                        releaseDate = details.releaseDate,
                        movieOverview = details.overview,
                        movieImages = images.ifEmpty { listOf(details.posterUrl) },
                        actors = cast
                    )

                }
                addMovieToRecentViewed(
                    RecentViewed(
                        id = details.id,
                        imageUrl = details.posterUrl,
                        type = MediaType.Movie,
                        viewDate = System.currentTimeMillis()
                    )
                )
                addMovieToRecentWatched(
                    Movie(
                        id = details.id,
                        name = details.title,
                        posterUrl = details.posterUrl,
                        releaseYear = 2025,
                        rating = 1,
                        genreIds = details.genresId,
                    )
                )
            },
            onError = { errorState ->
                updateState { copy(error = errorState) }
            },
            onCompleted = {
                updateState { copy(isLoading = false) }
            }
        )
    }

    private suspend fun addMovieToRecentWatched(movie: Movie) {
        manageRecentMovieWatchedUseCase.addMovieToRecentWatched(movie)
    }

    private suspend fun addMovieToRecentViewed(movie: RecentViewed) {
        manageRecentViewedUseCase.addToRecentViewed(movie)
    }

    private fun loadSimilarAndVideos(movieId: Int) {
        tryToExecute(
            block = {
                val similarMovies = movieDetails.getSimilarMovies(movieId)
                val movieVideos = movieDetails.getMovieVideo(movieId)
                val movieRating = if (authenticationUseCase.isLoggedIn())
                    ratingUseCase.getRateAccountMovieStatesById(movieId) else 0
                Triple(similarMovies, movieVideos, movieRating)

            },
            onSuccess = { (similarMovies, videos, movieRating) ->
                Log.d("test", "loadSimilarAndVideos: $videos")
                updateState {
                    copy(
                        similarMovies = similarMovies,
                        movieVideo = videos.first(),
                        isRated = movieRating != 0 && state.value.isGuestUser.not()
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
