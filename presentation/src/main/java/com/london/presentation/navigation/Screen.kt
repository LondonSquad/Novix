package com.london.presentation.navigation

import com.london.domain.entity.shared.MediaType
import com.london.presentation.shared.genre.MovieGenreUi
import com.london.presentation.shared.genre.TvShowGenreUi
import kotlinx.serialization.Serializable

@Serializable
sealed interface Screen {

    @Serializable
    data object Splash : Screen

    @Serializable
    object OnBoarding : Screen {
        @Serializable
        object Welcome : Screen
    }

    @Serializable
    data object Login : Screen {
        private var _source: Screen? = null
        val source: Screen?
            get() = _source?.also { _source = null }

        fun withSource(value: Screen?): Login = this.also { _source = value }
    }

    @Serializable
    data object Home : Screen

    @Serializable
    object Register : Screen

    @Serializable
    data object Search : Screen

    @Serializable
    data object Categories : Screen

    @Serializable
    data class Lists(val createList: Boolean = false) : Screen

    @Serializable
    data object Account : Screen

    @Serializable
    data object TrendingMovies : Screen

    @Serializable
    data object TrendingTvShows : Screen

    @Serializable
    data object TrendingActors : Screen

    @Serializable
    data class TvShowDetails(val tvShowId: Int) : Screen

    @Serializable
    data class MovieDetails(val movieId: Int, val isBookmarkSheetVisible: Boolean = false) : Screen

    @Serializable
    data class ActorDetails(val actorId: Int) : Screen

    @Serializable
    data class ActorTopMoviesPicksDetails(val actorId: Int) : Screen

    @Serializable
    data class TopTvShowsPicksDetails(val actorId: Int) : Screen

    @Serializable
    data class MoviesByCategory(val category: MovieGenreUi) : Screen

    @Serializable
    data class TvShowsByCategory(val category: TvShowGenreUi) : Screen

    @Serializable
    data class EpisodeDetails(
        val tvShowId: Int,
        val seasonNumber: Int,
        val episodeNumber: Int,
    ) : Screen

    @Serializable
    data class ActorGallery(val actorId: Int) : Screen

    @Serializable
    data class Reviews(
        val mediaId: Int,
        val mediaType: MediaType
    ) : Screen

    @Serializable
    data class ViewListItems(val listId: Int) : Screen

    @Serializable
    data object TopRated : Screen

    @Serializable
    data object ContinueWatching : Screen

    @Serializable
    data object WatchingHistory : Screen

    @Serializable
    data object MyRating : Screen

}
