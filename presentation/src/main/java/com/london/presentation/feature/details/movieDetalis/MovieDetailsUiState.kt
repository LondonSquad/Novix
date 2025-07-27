package com.london.presentation.feature.details.movieDetalis

import com.london.domain.entity.Actor
import com.london.domain.entity.Movie
import com.london.presentation.screen.base.ErrorState
import com.london.domain.entity.moviedatails.Genre
import com.london.domain.entity.moviedatails.SimilarMovie
import com.london.presentation.feature.base.ErrorState

data class MovieDetailsUiState(
    val movieId: Int = 0,
    val movieImage: List<String> = listOf(),
    val movieName: String = "",
    val movieGenres: List<Int> = listOf(),
    val movieRating: String = "",
    val movieDuration: String = "",
    val releaseDate: String = "",
    val movieOverview: String = "",
    val actors: List<Actor> = listOf(),
    val similarMovies: List<Movie> = listOf(),
    val isRated: Boolean = false,
    val isSaved: Boolean = false,
    val currentImageIndex: Int = 0,
    val imageSlideDirection: Int = 1,
    val expanded: Boolean = false,
    val isLoading: Boolean = true,
    val error: ErrorState? = null,
    val movieVideo: String = ""
){
    val movieHaveTrailer: Boolean
        get() = movieVideo.isNotEmpty()
}
