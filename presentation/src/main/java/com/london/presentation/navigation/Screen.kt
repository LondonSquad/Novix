package com.london.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface Screen {
    @Serializable
    data object Splash : Screen

    @Serializable
    data object OnboardingPager : Screen

    @Serializable
    data object Welcome : Screen

    @Serializable
    data object Login : Screen

    @Serializable
    data object Home : Screen

    @Serializable
    data object Search : Screen

    @Serializable
    data object Categories : Screen

    @Serializable
    data object Bookmarks : Screen

    @Serializable
    data object Account : Screen

    @Serializable
    data object TrendingMovies : Screen

    @Serializable
    data object TrendingTvShows : Screen

    @Serializable
    data object TrendingActors : Screen

    @Serializable
    data class TvShowDetails(
        val tvShowId: Int,
    ) : Screen

    @Serializable
    data class MovieDetails(
        val movieId: Int,
    ) : Screen

    @Serializable
    data class ActorDetails(
        val actorId: Int,
    ) : Screen

    @Serializable
    data class ActorTopMoviesPicksDetails(
        val actorId: Int,
    ) : Screen

    @Serializable
    data class TopTvShowsPicksDetails(
        val actorId: Int,
    ) : Screen

    @Serializable
    data class MoviesByCategory(
        val categoryId: Int,
    )

    @Serializable
    data class EpisodeDetails(
        val tvShowId: Int,
        val seasonNumber: Int,
        val episodeNumber: Int,
    ) : Screen

    @Serializable
    data class ActorGallery(
        val actorId: Int,
    ) : Screen

    @Serializable
    data class Reviews(
        val mediaId: Int,
        val mediaType: Int
    ) : Screen

    @Serializable
    data object TopRated : Screen

}