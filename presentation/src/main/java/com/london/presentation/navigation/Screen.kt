package com.london.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface Screen {
    @Serializable
    object Splash : Screen

    @Serializable
    object OnboardingPager : Screen

    @Serializable
    object Welcome : Screen

    @Serializable
    object Login : Screen

    @Serializable
    object Register : Screen

    @Serializable
    object Home : Screen

    @Serializable
    object Search : Screen

    @Serializable
    object Categories : Screen

    @Serializable
    object Bookmarks : Screen

    @Serializable
    object Account : Screen

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
    object TopRated : Screen
}