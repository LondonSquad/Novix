package com.london.app.navigation

import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.london.domain.entity.recent.MediaType
import com.london.presentation.navigation.Screen
import com.london.presentation.navigation.Screen.ActorDetails
import com.london.presentation.navigation.Screen.ActorGallery
import com.london.presentation.navigation.Screen.ActorTopMoviesPicksDetails
import com.london.presentation.navigation.Screen.ContinueWatching
import com.london.presentation.navigation.Screen.EpisodeDetails
import com.london.presentation.navigation.Screen.Login
import com.london.presentation.navigation.Screen.MovieDetails
import com.london.presentation.navigation.Screen.MoviesByCategory
import com.london.presentation.navigation.Screen.MyRating
import com.london.presentation.navigation.Screen.OnBoarding
import com.london.presentation.navigation.Screen.Register
import com.london.presentation.navigation.Screen.Reviews
import com.london.presentation.navigation.Screen.TopRated
import com.london.presentation.navigation.Screen.TopTvShowsPicksDetails
import com.london.presentation.navigation.Screen.TrendingActors
import com.london.presentation.navigation.Screen.TrendingMovies
import com.london.presentation.navigation.Screen.TrendingTvShows
import com.london.presentation.navigation.Screen.TvShowDetails
import com.london.presentation.navigation.Screen.TvShowsByCategory
import com.london.presentation.navigation.Screen.WatchingHistory
import com.london.presentation.shared.genre.MovieGenreUi
import com.london.presentation.shared.genre.TvShowGenreUi

fun NavController.navigateToEpisodeDetails(tvShowId: Int, seasonNumber: Int, episodeNumber: Int) =
    navigate(EpisodeDetails(tvShowId, episodeNumber, seasonNumber))
fun NavController.navigateToMovieDetails(movieId: Int) =
    navigate(MovieDetails(movieId))

fun NavController.navigateToTvShowDetails(tvShowId: Int) =
    navigate(TvShowDetails(tvShowId))

fun NavController.navigateToActorDetails(actorId: Int) =
    navigate(ActorDetails(actorId))


fun NavController.navigateToActorGallery(actorId: Int) =
    navigate(ActorGallery(actorId))

fun NavController.navigateToTopMoviesPicks(actorId: Int) =
    navigate(ActorTopMoviesPicksDetails(actorId))

fun NavController.navigateToTopTvShowsPicks(actorId: Int) =
    navigate(TopTvShowsPicksDetails(actorId))

fun NavController.navigateToTvShowsByCategory(genre: TvShowGenreUi) =
    navigate(TvShowsByCategory(genre))

fun NavController.navigateToMovieCategory(genre: MovieGenreUi) =
    navigate(MoviesByCategory(genre))

fun NavController.navigateToReviews(movieId: Int, mediaType: MediaType) =
    navigate(Reviews(movieId, mediaType))

fun NavController.navigateToLoginWithPopUp() {
    navigate(Login) {
        popUpTo(AppNavGraph.Main) {
            inclusive = true
        }
    }
}

fun NavController.navigateToLogin() = navigate(Login)
fun NavController.navigateToWatchingHistory() = navigate(WatchingHistory)
fun NavController.navigateToMyRating() = navigate(MyRating)

fun NavController.navigateToContinueWatching() = navigate(ContinueWatching)
fun NavController.navigateToTopRated() = navigate(TopRated)
fun NavController.navigateToTrendingMovies() = navigate(TrendingMovies)
fun NavController.navigateToTrendingTvShows() = navigate(TrendingTvShows)
fun NavController.navigateToTrendingActors() = navigate(TrendingActors)

fun NavController.navigateToRegister() = navigate(Register)

fun NavController.navigateToAuthGraph() = navigateTo(AppNavGraph.Auth)
fun NavController.navigateToMainGraph() = navigateTo(AppNavGraph.Main)
fun NavController.navigateToOnboardingGraph() = navigateTo(AppNavGraph.OnBoarding)

fun NavController.navigateToWelcome() = navigateTo(OnBoarding.Welcome)

fun NavController.navigateToListDetails(listId: Int) = navigateTo(Screen.ViewListItems(listId))

 fun navigateToBottomBarDestination(
    navController: NavHostController,
    destination: Any
) {
    if (navController.currentBackStackEntry?.destination?.hasRoute(destination::class) == true) {
        return
    }

    navController.navigate(destination) {
        popUpTo(navController.graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}