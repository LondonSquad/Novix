package com.london.presentation.feature.details.movie

import androidx.lifecycle.SavedStateHandle
import com.london.domain.entity.Actor
import com.london.domain.entity.Movie
import com.london.domain.entity.moviedatails.MovieDetails
import com.london.domain.entity.recent.MediaType
import com.london.domain.entity.recent.RecentViewed
import com.london.domain.usecase.authentication.AuthenticationUseCase
import com.london.domain.usecase.details.movie.GetMovieUseCase
import com.london.domain.usecase.rating.ManageRatingUseCase
import com.london.domain.usecase.recent.viewed.ManageRecentViewedUseCase
import com.london.domain.usecase.recent.watched.movie.ManageRecentMovieWatchedUseCase
import com.london.presentation.navigation.Screen
import com.london.presentation.navigation.getArgs
import com.london.presentation.shared.base.BaseViewModel
import com.london.presentation.shared.genre.MovieGenreUi
import com.london.presentation.shared.genre.toUi
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val movieDetails: GetMovieUseCase,
    private val manageRecentMovieWatchedUseCase: ManageRecentMovieWatchedUseCase,
    private val manageRecentViewedUseCase: ManageRecentViewedUseCase,
    private val ratingUseCase: ManageRatingUseCase,
    private val authenticationUseCase: AuthenticationUseCase,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<MovieDetailsUiState, MovieDetailsEffect>(MovieDetailsUiState()),
    MovieDetailsContract {

    private val args = savedStateHandle.getArgs<Screen.MovieDetails>()
    private val movieId = args?.movieId ?: 0

    init {
        handleMovieDetailsData(movieId)
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
        emitEffect(MovieDetailsEffect.LoginNavigation)
    }

    override fun onReviewsClick(movieId: Int, mediaType: MediaType) {
        emitEffect(MovieDetailsEffect.ReviewsNavigation(movieId, mediaType))
    }

    override fun onGenreClick(genre: MovieGenreUi) {
        emitEffect(MovieDetailsEffect.GenreNavigation(genre))
    }

    override fun onRetryClick() {
        updateState { copy(error = null) }
        handleMovieDetailsData(movieId)
        loadSimilarAndVideos(movieId)
    }

    override fun onRateBottomSheetClick() {
        tryToExecute(
            block = { authenticationUseCase.isLoggedIn() },
            onSuccess = { isLoggedIn -> onRateClickSuccess(isLoggedIn) },
            onError = { errorState ->
                updateState { copy(error = errorState) }
            }
        )
    }

    override fun onSelectRatingClick(rating: Int) {
        tryToExecute(
            block = { ratingUseCase.addMovieRatingById(movieId, rating) },
            onSuccess = { onRatingSuccess(rating) },
            onError = { errorState ->
                updateState { copy(error = errorState, isSuccessfullyRated = false) }
            },
            onCompleted = { setLoadingState(false) },
        )
    }

    private fun handleMovieDetailsData(movieId: Int) {
        tryToExecute(
            block = { fetchMovieData(movieId) },
            onStart = { setLoadingState(true) },
            onSuccess = { triple -> onMovieDataLoaded(triple) },
            onError = { errorState -> updateState { copy(error = errorState) } },
            onCompleted = { setLoadingState(false) }
        )
    }

    private fun onRateClickSuccess(isLoggedIn: Boolean) {
        if (isLoggedIn)
            updateState { copy(isRateBottomSheetVisible = isRateBottomSheetVisible.not()) }
        else
            updateState {
                copy(
                    isGuestUserBottomSheetVisible = isGuestUserBottomSheetVisible.not(),
                    isGuestUser = true
                )

            }
    }

    private fun onRatingSuccess(rating: Int) {
        updateState {
            copy(
                selectedRating = rating,
                isRated = true,
                isRateBottomSheetVisible = false,
                isSuccessfullyRated = true
            )
        }
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
                updateState {
                    copy(
                        similarMovies = similarMovies,
                        movieVideo = videos.firstOrNull().orEmpty(),
                        isRated = movieRating != 0 && state.value.isGuestUser.not()
                    )
                }
            },
            onError = { errorState ->
                updateState { copy(error = errorState) }
            },
            onCompleted = {
                setLoadingState(false)
            }
        )
    }

    private suspend fun fetchMovieData(movieId: Int): Triple<MovieDetails, List<String>, List<Actor>> {
        val movie = movieDetails.getMovieDetails(movieId)
        val movieImages = movieDetails.getMovieImagesUseCase(movieId)
        val movieCast = movieDetails.getMovieCast(movieId)
        return Triple(movie, movieImages, movieCast)
    }

    private suspend fun onMovieDataLoaded(triple: Triple<MovieDetails, List<String>, List<Actor>>) {
        updateMovieDetailsState(triple)
        addMovieToRecentHistory(triple.first)
    }

    private fun updateMovieDetailsState(triple: Triple<MovieDetails, List<String>, List<Actor>>) {
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
    }

    private suspend fun addMovieToRecentHistory(details: MovieDetails) {
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
    }

    private fun setLoadingState(isLoading: Boolean) {
        updateState { copy(isLoading = isLoading) }
    }
}
