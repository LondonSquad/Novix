package com.london.presentation.screen.details.movieDetalis

import com.london.domain.entity.Actor
import com.london.domain.entity.moviedatails.Genre
import com.london.domain.entity.moviedatails.MovieDetails
import com.london.domain.entity.moviedatails.SimilarMovie

fun mapToUiState(
    movieDetails: MovieDetails,
    currentUiState: MovieDetailsUiState
): MovieDetailsUiState {
    return currentUiState.copy(
        movieId = movieDetails.movieId,
        movieImage = movieDetails.movieImage,
        movieName = movieDetails.movieName,
        movieGenres = extractGenreNames(movieDetails.genres),
        movieRating = movieDetails.movieRating,
        movieDuration = movieDetails.movieDuration,
        releaseDate = movieDetails.releaseDate,
        movieOverview = movieDetails.movieOverview,
        genres = mapActorsToUiState(movieDetails.actors),
        similarMovies = mapSimilarMoviesToUiState(movieDetails.similarMovies),
        movieHaveTrailer = movieDetails.movieHaveTrailer,
        isRated = currentUiState.isRated,
        isSaved = currentUiState.isSaved
    )
}

private fun extractGenreNames(actors: List<Genre>): List<String> {
    return actors.map { it.name }
}

private fun mapActorsToUiState(actors: List<Actor>): List<ActorUIState> {
    return actors.map { actor ->
        ActorUIState(
            name = actor.name, avatarUrl = actor.profilePicture, characterName = actor.characterName
        )
    }
}

private fun mapSimilarMoviesToUiState(similarMovies: List<SimilarMovie>): List<SimilarMovieUIState> {
    return similarMovies.map { movie ->
        SimilarMovieUIState(
            image = movie.image, isSaved = movie.isSaved
        )
    }
}

fun MovieDetails.toUiState(currentUiState: MovieDetailsUiState): MovieDetailsUiState {
    return mapToUiState(this, currentUiState)
}