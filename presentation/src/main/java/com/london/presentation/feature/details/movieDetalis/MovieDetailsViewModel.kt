package com.london.presentation.feature.details.movieDetalis

import androidx.lifecycle.SavedStateHandle
import com.london.domain.entity.Movie
import com.london.domain.entity.recent.MediaType
import com.london.domain.entity.recent.RecentViewed
import com.london.domain.usecase.details.movie.GetFirstTenMovieImagesUseCase
import com.london.domain.usecase.details.movie.GetMovieCastUseCase
import com.london.domain.usecase.details.movie.GetMovieDetailsById
import com.london.domain.usecase.details.movie.GetMovieVideoUseCase
import com.london.domain.usecase.details.movie.GetSimilarMoviesUseCase
import com.london.domain.usecase.recent.viewed.AddToRecentViewedUseCase
import com.london.domain.usecase.recent.watched.AddMovieToRecentWatchedUseCase
import com.london.presentation.feature.base.BaseViewModel
import com.london.presentation.navigation.Screen
import com.london.presentation.navigation.getArgs
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class MovieDetailsViewModel(
    private val getMovieById: GetMovieDetailsById,
    private val getMovieImagesUseCase: GetFirstTenMovieImagesUseCase,
    private val getMovieCastUseCase: GetMovieCastUseCase,
    private val getSimilarMoviesUseCase: GetSimilarMoviesUseCase,
    private val getMovieVideosUseCase: GetMovieVideoUseCase,
    private val addMovieToRecentWatchedUseCase:AddMovieToRecentWatchedUseCase,
    private val addToRecentViewedUseCase:AddToRecentViewedUseCase,
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
                        id =details.id,
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

    private suspend fun addMovieToRecentWatched(movie: Movie){
        addMovieToRecentWatchedUseCase.invoke(movie)
    }
    private suspend fun addMovieToRecentViewed(movie: RecentViewed){
        addToRecentViewedUseCase.invoke(movie)
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
