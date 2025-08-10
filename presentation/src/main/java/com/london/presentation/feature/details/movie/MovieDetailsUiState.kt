package com.london.presentation.feature.details.movie

import com.london.domain.entity.Actor
import com.london.domain.entity.Movie
import com.london.presentation.shared.base.ErrorState

data class MovieDetailsUiState(
    val movieId: Int = 0,
    val movieName: String = "",
    val selectedRating: Int = 0,
    val movieVideo: String = "",
    val movieRating: String = "",
    val releaseDate: String = "",
    val isRated: Boolean = false,
    val isSaved: Boolean = false,
    val isLoading: Boolean = true,
    val expanded: Boolean = false,
    val error: ErrorState? = null,
    val currentImageIndex: Int = 0,
    val movieDuration: String = "",
    val movieOverview: String = "",
    val imageSlideDirection: Int = 1,
    val isGuestUser: Boolean = false,
    val actors: List<Actor> = listOf(),
    val movieGenres: List<Int> = listOf(),
    val movieImages: List<String> = listOf(),
    val isSuccessfullyRated: Boolean? = null,
    val similarMovies: List<Movie> = listOf(),
    val isRateBottomSheetVisible: Boolean = false,
    val isGuestUserBottomSheetVisible: Boolean = false,
){
    val movieHaveTrailer: Boolean
        get() = movieVideo.isNotEmpty()
}
