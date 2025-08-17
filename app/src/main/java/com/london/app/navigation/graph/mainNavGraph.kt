package com.london.app.navigation.graph

import androidx.compose.ui.res.stringResource
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.london.app.navigation.AppNavGraph
import com.london.app.navigation.appComposable
import com.london.app.navigation.navigateToActorDetails
import com.london.app.navigation.navigateToActorGallery
import com.london.app.navigation.navigateToContinueWatching
import com.london.app.navigation.navigateToEpisodeDetails
import com.london.app.navigation.navigateToListDetails
import com.london.app.navigation.navigateToLogin
import com.london.app.navigation.navigateToLoginWithPopUp
import com.london.app.navigation.navigateToMovieCategory
import com.london.app.navigation.navigateToMovieDetails
import com.london.app.navigation.navigateToMyRating
import com.london.app.navigation.navigateToReviews
import com.london.app.navigation.navigateToTopMoviesPicks
import com.london.app.navigation.navigateToTopRated
import com.london.app.navigation.navigateToTopTvShowsPicks
import com.london.app.navigation.navigateToTrendingActors
import com.london.app.navigation.navigateToTrendingMovies
import com.london.app.navigation.navigateToTrendingTvShows
import com.london.app.navigation.navigateToTvShowDetails
import com.london.app.navigation.navigateToTvShowsByCategory
import com.london.app.navigation.navigateToWatchingHistory
import com.london.presentation.R
import com.london.presentation.feature.account.AccountScreen
import com.london.presentation.feature.account.rating.MyRatingScreen
import com.london.presentation.feature.category.main.CategoriesScreen
import com.london.presentation.feature.category.movie.MoviesByCategoryScreen
import com.london.presentation.feature.category.tvshow.TvShowByCategoryScreen
import com.london.presentation.feature.details.actor.ActorDetailsScreen
import com.london.presentation.feature.details.actor.info.gallery.ActorsGalleryScreen
import com.london.presentation.feature.details.actor.info.topmoviespicks.TopMoviesPicksScreen
import com.london.presentation.feature.details.actor.info.toptvshowspicks.TopTvShowsPicksScreen
import com.london.presentation.feature.details.movie.MovieDetailsScreen
import com.london.presentation.feature.details.tvshow.episode.EpisodeDetailsScreen
import com.london.presentation.feature.details.tvshow.info.TvShowsDetailsScreen
import com.london.presentation.feature.home.HomeScreen
import com.london.presentation.feature.home.continuewatching.ContinueWatchingScreen
import com.london.presentation.feature.home.toprated.TopRatedScreen
import com.london.presentation.feature.home.trending.actor.TrendingActorsScreen
import com.london.presentation.feature.home.trending.movie.TrendingMoviesScreen
import com.london.presentation.feature.home.trending.tvshow.TrendingTvShowsScreen
import com.london.presentation.feature.list.savedlist.ListScreen
import com.london.presentation.feature.list.viewitems.ViewListItemsScreen
import com.london.presentation.feature.reviews.ReviewsScreen
import com.london.presentation.feature.search.SearchScreen
import com.london.presentation.navigation.Screen.Account
import com.london.presentation.navigation.Screen.ActorDetails
import com.london.presentation.navigation.Screen.ActorGallery
import com.london.presentation.navigation.Screen.ActorTopMoviesPicksDetails
import com.london.presentation.navigation.Screen.Categories
import com.london.presentation.navigation.Screen.ContinueWatching
import com.london.presentation.navigation.Screen.EpisodeDetails
import com.london.presentation.navigation.Screen.Home
import com.london.presentation.navigation.Screen.Lists
import com.london.presentation.navigation.Screen.MovieDetails
import com.london.presentation.navigation.Screen.MoviesByCategory
import com.london.presentation.navigation.Screen.MyRating
import com.london.presentation.navigation.Screen.Reviews
import com.london.presentation.navigation.Screen.Search
import com.london.presentation.navigation.Screen.TopRated
import com.london.presentation.navigation.Screen.TopTvShowsPicksDetails
import com.london.presentation.navigation.Screen.TrendingActors
import com.london.presentation.navigation.Screen.TrendingMovies
import com.london.presentation.navigation.Screen.TrendingTvShows
import com.london.presentation.navigation.Screen.TvShowDetails
import com.london.presentation.navigation.Screen.TvShowsByCategory
import com.london.presentation.navigation.Screen.ViewListItems
import com.london.presentation.navigation.Screen.WatchingHistory


fun NavGraphBuilder.mainNavGraph(
    navController: NavHostController
) = navigation<AppNavGraph.Main>(startDestination = Home) {
    with(navController) {

        homeNavGraph(navController)
        tvShowDetailsNavGraph(navController)
        movieDetailsNavGraph(navController)
        onListNavGraph(navController)
        actorDetailsNavGraph(navController)

        appComposable<Search> {
            SearchScreen(
                onNavigateToTvShowDetails = ::navigateToTvShowDetails,
                onNavigateToActorDetails = ::navigateToActorDetails,
                onNavigateToMovieDetails = ::navigateToMovieDetails
            )
        }

        appComposable<Categories> {
            CategoriesScreen(
                onNavigateToMovieCategory = ::navigateToMovieCategory,
                onNavigateToTvShowCategory = ::navigateToTvShowsByCategory
            )
        }

        appComposable<Account> {
            AccountScreen(
                onNavigateToWatchingHistory = ::navigateToWatchingHistory,
                onNavigateToMyRating = ::navigateToMyRating,
                onNavigateToLogin = ::navigateToLoginWithPopUp
            )
        }

        appComposable<MyRating> {
            MyRatingScreen(
                onNavigateToMovieDetails = ::navigateToMovieDetails,
                onNavigateToTvShowDetails = ::navigateToTvShowDetails,
                onNavigateBack = ::navigateUp
            )
        }

        composable<Reviews> { ReviewsScreen(onNavigateBack = ::navigateUp)
        }
    }
}

fun NavGraphBuilder.homeNavGraph(navController: NavHostController) =
    with(navController) {
        trendingNavGraph(navController)
        continueWatchingNavGraph(navController)
        appComposable<Home> {
            HomeScreen(
                onNavigateToMovieDetails = ::navigateToMovieDetails,
                onNavigateToTvShowDetails = ::navigateToTvShowDetails,
                onNavigateToTopRated = ::navigateToTopRated,
                onNavigateToTrendingMovies = ::navigateToTrendingMovies,
                onNavigateToTrendingTvShows = ::navigateToTrendingTvShows,
                onNavigateToTrendingActors = ::navigateToTrendingActors,
                onNavigateToContinueWatching = ::navigateToContinueWatching
            )
        }
        composable<TopRated> {
            TopRatedScreen(
                onNavigateBack = ::navigateUp,
                onNaviagteToMovieDetalis = ::navigateToMovieDetails,
                onNaviagteToTvShowDetalis = ::navigateToTvShowDetails
            )
        }
    }

fun NavGraphBuilder.continueWatchingNavGraph(navController: NavHostController) =
    with(navController) {
        composable<ContinueWatching> {
            ContinueWatchingScreen(
                onNavigateBack = ::navigateUp,
                onNavigateToMovieDetails = ::navigateToMovieDetails,
                onNavigateToTvShowDetails = ::navigateToTvShowDetails,
                screenTitle = stringResource(R.string.continue_watch)
            )
        }

        appComposable<WatchingHistory> {
            ContinueWatchingScreen(
                onNavigateBack = ::navigateUp,
                onNavigateToMovieDetails = ::navigateToMovieDetails,
                onNavigateToTvShowDetails = ::navigateToTvShowDetails,
                screenTitle = stringResource(R.string.watching_history)
            )
        }
    }

fun NavGraphBuilder.onListNavGraph(navController: NavHostController) =
    with(navController) {
        appComposable<Lists> {
            ListScreen(
                onNavigateToListDetails = ::navigateToListDetails,
                onNavigateToLogin = ::navigateToLoginWithPopUp,
            )
        }
        appComposable<ViewListItems> {
            ViewListItemsScreen(
                onNavigateBack = ::navigateUp,
                onNavigateToMovieDetails = ::navigateToMovieDetails,
            )
        }
    }

fun NavGraphBuilder.movieDetailsNavGraph(navController: NavHostController) =
    with(navController) {
        appComposable<MovieDetails> {
            MovieDetailsScreen(
                onNavigateBack = ::navigateUp,
                onNavigateToMovieCategory = ::navigateToMovieCategory,
                onNavigateToMovie = ::navigateToMovieDetails,
                onNavigateToActor = ::navigateToActorDetails,
                onNavigateToReviews = ::navigateToReviews,
                onNavigateToLogin = ::navigateToLogin,
            )
        }

        appComposable<MoviesByCategory> {
            MoviesByCategoryScreen(
                onNavigateToMovieDetails = ::navigateToMovieDetails,
                onNavigateBack = ::navigateUp,
            )
        }
    }


fun NavGraphBuilder.tvShowDetailsNavGraph(navController: NavHostController) =
    with(navController) {
        appComposable<TvShowDetails> {
            TvShowsDetailsScreen(
                onNavigateToEpisodeDetails = ::navigateToEpisodeDetails,
                onNavigateToReviews = ::navigateToReviews,
                onNavigateToActorDetails = ::navigateToActorDetails,
                onNavigateBack = ::navigateUp,
                onNavigateToTvShowCategory = ::navigateToTvShowsByCategory,
                onNavigateToLogin = ::navigateToLogin
            )
        }

        appComposable<EpisodeDetails> {
            EpisodeDetailsScreen(
                onNavigateBack = ::navigateUp,
                onNaviagteToActorDetalis = ::navigateToActorDetails,
                onNavigateToLogin = ::navigateToLogin
            )

            appComposable<TvShowsByCategory> {
                TvShowByCategoryScreen(
                    onNavigateBack = ::navigateUp,
                    onNavigateToTvShowDetails = ::navigateToTvShowDetails
                )
            }
        }
    }

fun NavGraphBuilder.actorDetailsNavGraph(navController: NavHostController) =
    with(navController) {
        appComposable<TopTvShowsPicksDetails> {
            TopTvShowsPicksScreen(
                onNavigateToTvShowDetails = ::navigateToTvShowDetails,
                onNavigateBack = ::navigateUp,
            )
        }

        composable<ActorTopMoviesPicksDetails> {
            TopMoviesPicksScreen(
                onNavigateToMovieDetails = ::navigateToMovieDetails,
                onNavigateBack = ::navigateUp,
            )
        }
        composable<ActorDetails> {
            ActorDetailsScreen(
                onNavigateToTopMoviePicks = ::navigateToTopMoviesPicks,
                onNavigateToTopTvShowPicks = ::navigateToTopTvShowsPicks,
                onNavigateToGallery = ::navigateToActorGallery,
                onNavigateToMovieDetails = ::navigateToMovieDetails,
                onNavigateToTvShowDetails = ::navigateToTvShowDetails,
                onNavigateBack = ::navigateUp
            )
        }
        composable<ActorGallery> { ActorsGalleryScreen(onNavigateBack = ::navigateUp) }
    }

fun NavGraphBuilder.trendingNavGraph(navController: NavHostController) =
    with(navController) {
        composable<TrendingMovies> {
            TrendingMoviesScreen(
                onNavigateToMovieDetailsClick = ::navigateToMovieDetails,
                onNavigateBackClick = ::navigateUp
            )
        }
        composable<TrendingTvShows> {
            TrendingTvShowsScreen(
                onNavigateToTvShowDetailsClick = ::navigateToTvShowDetails,
                onNavigateBack = ::navigateUp
            )
        }
        composable<TrendingActors> {
            TrendingActorsScreen(
                onNavigateToActorDetailsClick = ::navigateToActorDetails,
                onNavigateBackClick = ::navigateUp
            )
        }
    }
