package com.london.presentation.feature.details.movie

import androidx.lifecycle.SavedStateHandle
import com.london.domain.entity.actor.Actor
import com.london.domain.entity.movie.Movie
import com.london.domain.entity.movie.MovieDetails
import com.london.domain.entity.recent.RecentViewed
import com.london.domain.entity.shared.MediaType
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
    savedStateHandle: SavedStateHandle,
    private val movieDetails: GetMovieUseCase,
    private val ratingUseCase: ManageRatingUseCase,
    private val authenticationUseCase: AuthenticationUseCase,
    private val manageRecentViewedUseCase: ManageRecentViewedUseCase,
    private val manageRecentMovieWatchedUseCase: ManageRecentMovieWatchedUseCase
) : BaseViewModel<MovieDetailsUiState, MovieDetailsEffect>(MovieDetailsUiState()),
    MovieDetailsContract {

    private val args = savedStateHandle.getArgs<Screen.MovieDetails>()
    private val movieId = args?.movieId ?: 0

    init {
        loadMovieDetails()
        args?.let { updateState { copy(isBookmarkSheetVisible = it.isBookmarkSheetVisible) } }
    }

    private fun loadMovieDetails() {
        loadMainMovieData()
        loadAdditionalMovieData()
    }

    override fun onBackClick() = emitEffect(MovieDetailsEffect.BackNavigation)

    override fun onManageBookmarkClicked(movieId: Int) {
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

    override fun onExpandClick() = updateState { copy(expanded = !expanded) }


    override fun onMovieClick(movieId: Int) {
        clearRatedState()
        emitEffect(MovieDetailsEffect.MovieNavigation(movieId))
    }

    override fun onActorClick(actorId: Int) {
        clearRatedState()
        emitEffect(MovieDetailsEffect.ActorNavigation(actorId))
    }

    override fun onReviewsClick(movieId: Int, mediaType: MediaType) {
        clearRatedState()
        emitEffect(MovieDetailsEffect.ReviewsNavigation(movieId, mediaType))
    }

    override fun onGenreClick(genre: MovieGenreUi) {
        clearRatedState()
        emitEffect(MovieDetailsEffect.GenreNavigation(genre))
    }

    override fun onRetryClick() {
        resetErrorState()
        loadMovieDetails()
    }

    override fun onLoginClick(movieId: Int) {
        emitEffect(MovieDetailsEffect.LoginNavigation(movieId))
    }
    override fun onRateBottomSheetClick() = checkUserAuthenticationForRating()

    override fun onSelectRatingClick(rating: Int) = submitMovieRating(rating)

    private fun clearRatedState() = updateState { copy(isSuccessfullyRated = null) }

    private fun handleRatingAuthenticationResult(isLoggedIn: Boolean) =
        if (isLoggedIn) showRatingBottomSheet() else showGuestUserBottomSheet()

    private fun showRatingBottomSheet() =
        updateState { copy(isRateBottomSheetVisible = !isRateBottomSheetVisible) }

    private fun showGuestUserBottomSheet() {
        updateState {
            copy(
                isGuestUserBottomSheetVisible = !isGuestUserBottomSheetVisible,
                isGuestUser = true
            )
        }
    }

    private fun submitMovieRating(rating: Int) {
        tryToExecute(
            block = { ratingUseCase.addMovieRatingById(movieId, rating) },
            onSuccess = { handleRatingSuccess(rating) },
            onError = { errorState ->
                updateState { copy(error = errorState, isSuccessfullyRated = false) }
            },
            onCompleted = { setLoadingState(false) }
        )
    }

    private fun handleRatingSuccess(rating: Int) {
        updateState {
            copy(
                selectedRating = rating,
                isRated = true,
                isRateBottomSheetVisible = false,
                isSuccessfullyRated = true
            )
        }
    }

    private suspend fun addMovieToRecentHistory(details: MovieDetails) {
        addMovieToRecentViewed(details)
        addMovieToRecentWatched(details)
    }

    private suspend fun addMovieToRecentViewed(details: MovieDetails) {
        val recentViewed = RecentViewed(
            id = details.id,
            imageUrl = details.posterUrl,
            type = MediaType.Movie,
            viewDate = System.currentTimeMillis()
        )
        manageRecentViewedUseCase.addToRecentViewed(recentViewed)
    }

    private suspend fun addMovieToRecentWatched(details: MovieDetails) {
        val movie = Movie(
            id = details.id,
            name = details.title,
            posterUrl = details.posterUrl,
            releaseYear = 2025,
            rating = 1,
            genres = details.genres
        )
        manageRecentMovieWatchedUseCase.addMovieToRecentWatched(movie)
    }

    private fun loadMainMovieData() {
        tryToExecute(
            block = { fetchMainMovieData() },
            onStart = { setLoadingState(true) },
            onSuccess = { movieData -> handleMainMovieDataSuccess(movieData) },
            onError = { errorState -> updateState { copy(error = errorState) } },
            onCompleted = { setLoadingState(false) }
        )
    }

    private fun loadAdditionalMovieData() {
        tryToExecute(
            block = { fetchAdditionalMovieData() },
            onSuccess = { additionalData -> handleAdditionalMovieDataSuccess(additionalData) },
            onError = { errorState -> updateState { copy(error = errorState) } },
            onCompleted = { setLoadingState(false) }
        )
    }

    private suspend fun fetchMainMovieData(): MainMovieData {
        val cast = movieDetails.getMovieCast(movieId)
        val images = movieDetails.getMovieImages(movieId)
        val details = movieDetails.getMovieDetails(movieId)
        return MainMovieData(details, images, cast)
    }

    private suspend fun fetchAdditionalMovieData(): AdditionalMovieDetailsData {
        val similarMovies = movieDetails.getSimilarMovies(movieId)
        val movieVideos = movieDetails.getMovieVideo(movieId)
        val movieRating = getUserMovieRating()

        return AdditionalMovieDetailsData(
            similarMovies = similarMovies,
            movieVideoUrl = movieVideos,
            movieRating = movieRating
        )
    }

    private suspend fun getUserMovieRating(): Int {
        return if (authenticationUseCase.isLoggedIn())
            ratingUseCase.getRateAccountMovieStatesById(movieId) else 0
    }

    private suspend fun handleMainMovieDataSuccess(movieData: MainMovieData) {
        val (details, images, cast) = movieData
        updateMovieDetailsState(details, images, cast)
        addMovieToRecentHistory(details)
    }

    private fun handleAdditionalMovieDataSuccess(additionalData: AdditionalMovieDetailsData) {
        updateState {
            copy(
                similarMovies = additionalData.similarMovies,
                movieVideo = additionalData.movieVideoUrl.firstOrNull().orEmpty(),
                isRated = additionalData.movieRating != 0 && !state.value.isGuestUser
            )
        }
    }

    private fun updateMovieDetailsState(
        details: MovieDetails,
        images: List<String>,
        cast: List<Actor>
    ) {
        updateState {
            copy(
                movieId = details.id,
                movieName = details.title,
                movieGenres = details.genres.map { it.toUi() },
                movieRating = details.voteAverage,
                movieDuration = details.runtime.toString(),
                releaseDate = details.releaseDate,
                movieOverview = details.overview,
                movieImages = images.ifEmpty { listOf(details.posterUrl) },
                actors = cast
            )
        }
    }

    private fun resetErrorState() = updateState { copy(error = null) }

    private fun setLoadingState(isLoading: Boolean) = updateState { copy(isLoading = isLoading) }

    private fun checkUserAuthenticationForRating() {
        tryToExecute(
            block = { authenticationUseCase.isLoggedIn() },
            onSuccess = { isLoggedIn -> handleRatingAuthenticationResult(isLoggedIn) },
            onError = { errorState -> updateState { copy(error = errorState) } }
        )
    }
}
