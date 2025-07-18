package com.london.presentation.screen.details.movieDetalis

import com.london.domain.entity.moviedatails.Genre

data class MovieDetailsUiState(
    val movieId: Int = 0,
    val movieImage: List<Any> = listOf(),
    val movieName: String = "",
    val movieGenres: List<Genre> = listOf(),
    val movieRating: String = "",
    val movieDuration: String = "",
    val releaseDate: String = "",
    val movieOverview: String = "",
    val actors: List<ActorUIState> = listOf(),
    val similarMovies: List<SimilarMovieUIState> = listOf(),
    val movieHaveTrailer: Boolean = false,
    val isRated: Boolean = false,
    val isSaved: Boolean = false,
    val currentImageIndex: Int = 0,
    val imageSlideDirection: Int = 1,
    val expanded: Boolean = false,
    val isLoading: Boolean = true,
)

data class ActorUIState(
    val name: String,
    val avatarUrl: String,
    val characterName: String,
)

data class SimilarMovieUIState(
    val image: String,
    val isSaved: Boolean,
    val id: Int
)
