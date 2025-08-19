package com.london.presentation.feature.details.movie

import com.london.domain.entity.actor.Actor
import com.london.domain.entity.movie.Movie
import com.london.domain.entity.movie.MovieDetails
import com.london.presentation.shared.base.ErrorState
import com.london.presentation.shared.genre.MovieGenreUi

data class MovieDetailsUiState(
    val movieId: Int = 0,
    val movieName: String = "",
    val selectedRating: Int = 0,
    val movieVideo: String = "",
    val movieRating: String = "",
    val releaseDate: String = "",
    val isRated: Boolean = false,
    val isLoading: Boolean = true,
    val expanded: Boolean = false,
    val error: ErrorState? = null,
    val movieDuration: String = "",
    val movieOverview: String = "",
    val isGuestUser: Boolean = false,
    val actors: List<Actor> = listOf(),
    val movieGenres: List<MovieGenreUi> = listOf(),
    val isSuccessfullyRated: Boolean? = null,
    val movieImages: List<String> = listOf(),
    val similarMovies: List<Movie> = listOf(),
    val isRateBottomSheetVisible: Boolean = false,
    val isGuestUserBottomSheetVisible: Boolean = false,
    val isBookmarkSheetVisible: Boolean = false,
) {
    val hasTrailer: Boolean
        get() = movieVideo.isNotEmpty()
}

data class AdditionalMovieDetailsData(
    val similarMovies: List<Movie>,
    val movieVideoUrl: List<String>,
    val movieRating: Int
)

data class MainMovieData(
    val details: MovieDetails,
    val images: List<String>,
    val cast: List<Actor>
)
