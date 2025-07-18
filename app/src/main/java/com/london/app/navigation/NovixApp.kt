package com.london.app.navigation

import android.util.Log
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.london.designsystem.component.NavBar
import com.london.designsystem.theme.NovixTheme
import com.london.presentation.navigation.Screen
import com.london.presentation.navigation.Screen.Account
import com.london.presentation.navigation.Screen.ActorTopMoviesPicksDetails
import com.london.presentation.navigation.Screen.Bookmarks
import com.london.presentation.navigation.Screen.Categories
import com.london.presentation.navigation.Screen.EpisodeDetails
import com.london.presentation.navigation.Screen.Home
import com.london.presentation.navigation.Screen.MovieDetails
import com.london.presentation.navigation.Screen.MoviesByCategory
import com.london.presentation.navigation.Screen.Reviews
import com.london.presentation.navigation.Screen.Search
import com.london.presentation.navigation.Screen.TopTvShowsPicksDetails
import com.london.presentation.navigation.Screen.TvShowDetails
import com.london.presentation.screen.account.AccountScreen
import com.london.presentation.screen.bookmark.BookmarksScreen
import com.london.presentation.screen.category.CategoriesScreen
import com.london.presentation.screen.category.moviesbycategory.MoviesByCategoryScreen
import com.london.presentation.screen.details.actor.ActorDetailsScreen
import com.london.presentation.screen.details.actordetails.gallery.ActorGalleryScreen
import com.london.presentation.screen.details.actordetails.topmoviespicks.TopMoviesPicksScreen
import com.london.presentation.screen.details.actordetails.toptvshowspicks.TopTvShowsPicksScreen
import com.london.presentation.screen.details.movieDetalis.MovieDetailsScreen
import com.london.presentation.screen.details.tvshow.episodedetails.EpisodeDetailsScreen
import com.london.presentation.screen.details.tvshow.tvshowdetails.TvShowsDetailsScreen
import com.london.presentation.screen.home.HomeScreen
import com.london.presentation.screen.reviews.ReviewsScreen
import com.london.presentation.screen.search.SearchScreen


@Composable
fun NovixApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val currentScreen = when {
        currentDestination?.hasRoute<Home>() == true -> Home
        currentDestination?.hasRoute<Search>() == true -> Search
        currentDestination?.hasRoute<Categories>() == true -> Categories
        currentDestination?.hasRoute<Bookmarks>() == true -> Bookmarks
        currentDestination?.hasRoute<Account>() == true -> Account
        else -> Home
    }

    val showBottomNav = currentDestination?.hasRoute<TvShowDetails>() != true
    val showMovieDetails = currentDestination?.hasRoute<MovieDetails>() != true
    Log.d("test", "NovixApp: $showMovieDetails")
    Scaffold(
        bottomBar = {
            if (showBottomNav && showMovieDetails) {
                NavBar(
                    modifier = Modifier
                        .background(NovixTheme.colors.surface)
                        .navigationBarsPadding(),
                    navDestinations = NavigationHelper.getNavigationTabs(),
                    currentSelectedDestination = currentScreen,
                    onNavDestinationClicked = { destination ->
                        navigateToBottomBarDestination(navController, destination)
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Home,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable<Home>(
                exitTransition = { fadeOut(tween(500)) },
                popEnterTransition = { fadeIn(tween(500)) },
                enterTransition = { fadeIn(tween(500)) },
                popExitTransition = { fadeOut(tween(500)) },
            ) {
                HomeScreen()
            }

            composable<Search>(
                exitTransition = { fadeOut(tween(500)) },
                popEnterTransition = { fadeIn(tween(500)) },
                enterTransition = { fadeIn(tween(500)) },
                popExitTransition = { fadeOut(tween(500)) },
            ) {
                SearchScreen(
                    onNavigateToTvShowDetails = { tvShowId ->
                        navController.navigate(TvShowDetails(tvShowId))
                    },
                    onNavigateToActorDetails = { actorId ->
                        navController.navigate(Screen.ActorDetails(actorId))
                    },
                    onNavigateToMovieDetails = { movieId ->
                        navController.navigate(MovieDetails(movieId))
                    }
                )
            }

            composable<Categories>(
                exitTransition = { fadeOut(tween(500)) },
                popEnterTransition = { fadeIn(tween(500)) },
                enterTransition = { fadeIn(tween(500)) },
                popExitTransition = { fadeOut(tween(500)) },
            ) {
                CategoriesScreen()
            }

            composable<Bookmarks>(
                exitTransition = { fadeOut(tween(500)) },
                popEnterTransition = { fadeIn(tween(500)) },
                enterTransition = { fadeIn(tween(500)) },
                popExitTransition = { fadeOut(tween(500)) },
            ) {
                BookmarksScreen()
            }

            composable<Account>(
                exitTransition = { fadeOut(tween(500)) },
                popEnterTransition = { fadeIn(tween(500)) },
                enterTransition = { fadeIn(tween(500)) },
                popExitTransition = { fadeOut(tween(500)) },
            ) {
                AccountScreen()
            }

            composable<TvShowDetails>(
                exitTransition = { fadeOut(tween(500)) },
                popEnterTransition = { fadeIn(tween(500)) },
                enterTransition = { fadeIn(tween(500)) },
                popExitTransition = { fadeOut(tween(500)) },
            ) {
                TvShowsDetailsScreen(
                    onBackClick = {
                        navController.navigateUp()
                    },
                    onNavigateToEpisodeDetails = { tvShowId, episodeNumber, seasonNumber ->
                        navController.navigate(
                            EpisodeDetails(
                                tvShowId,
                                episodeNumber,
                                seasonNumber
                            )
                        )
                    },
                    onNavigateToReviews = { tvShowId, mediaType ->
                        navController.navigate(Reviews(tvShowId, mediaType))
                    }
                )
            }

            composable<MovieDetails>(
                exitTransition = { fadeOut(tween(500)) },
                popEnterTransition = { fadeIn(tween(500)) },
                enterTransition = { fadeIn(tween(500)) },
                popExitTransition = { fadeOut(tween(500)) },
            ) {
                MovieDetailsScreen(
                    onBackClick = { navController.navigateUp() },
                    onGenreClick = {
                        navController.navigate(MoviesByCategory(it))
                    },
                    onNavigateToPreviews = { movieId, mediaType ->
                        navController.navigate(Reviews(movieId, mediaType))
                    }
                )
            }

            composable<TopTvShowsPicksDetails>(
                exitTransition = { fadeOut(tween(500)) },
                popEnterTransition = { fadeIn(tween(500)) },
                enterTransition = { fadeIn(tween(500)) },
                popExitTransition = { fadeOut(tween(500)) },
            ) {
                TopTvShowsPicksScreen(
                    onBackClick = {
                        navController.navigateUp()
                    },
                    onTvShowClick = { tvShowId ->
                        navController.navigate(TvShowDetails(tvShowId))
                    }
                )
            }
            composable<ActorTopMoviesPicksDetails> {
                TopMoviesPicksScreen(
                    onBackClick = {
                        navController.navigateUp()
                    },
                    onMovieClick = { movieId ->
                        navController.navigate(MovieDetails(movieId))
                    }
                )
            }


            composable<Reviews> { backStackEntry ->
                val reviews = backStackEntry.arguments?.let {
                    Reviews(
                        mediaId = it.getInt("mediaId"),
                        mediaType = it.getInt("mediaType")
                    )
                }

                ReviewsScreen(
                    onBackClick = {
                        navController.navigateUp()
                    }
                )
            }

            composable<MoviesByCategory>(
                exitTransition = { fadeOut(tween(500)) },
                popEnterTransition = { fadeIn(tween(500)) },
                enterTransition = { fadeIn(tween(500)) },
                popExitTransition = { fadeOut(tween(500)) },
            ) {
                MoviesByCategoryScreen(
                    onNavigateToMovieDetails = { movieId ->
                        navController.navigate(MovieDetails(movieId))
                    },
                    onBackClick = {
                        navController.navigateUp()
                    },
                )
            }

            composable<Screen.ActorDetails> {
                ActorDetailsScreen(
                    onNavigateToMoviePicks = { actorId ->
                        navController.navigate(ActorTopMoviesPicksDetails(actorId))
                    }, onNavigateToTvShowPicks = { actorId ->
                        navController.navigate(TopTvShowsPicksDetails(actorId))
                    },
                    onNavigateToGallery = { actorId ->
                        navController.navigate(Screen.ActorGallery(actorId))
                    },
                    onBackClick = { navController.navigateUp() }
                )
            }

            composable<EpisodeDetails>(
                exitTransition = { fadeOut(tween(500)) },
                popEnterTransition = { fadeIn(tween(500)) },
                enterTransition = { fadeIn(tween(500)) },
                popExitTransition = { fadeOut(tween(500)) },
            ) {
                EpisodeDetailsScreen(
                    onBackClick = { navController.popBackStack() }
                )
            }
            composable<Screen.ActorGallery> {
                ActorGalleryScreen(
                    onBackClick = { navController.popBackStack() }
                )
            }
        }
    }
}

private fun navigateToBottomBarDestination(
    navController: NavHostController,
    destination: Screen
) {
    navController.navigate(destination) {
        popUpTo(navController.graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}