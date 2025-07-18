package com.london.app.navigation

import android.util.Log
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
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
import com.london.presentation.navigation.Screen.Bookmarks
import com.london.presentation.navigation.Screen.Categories
import com.london.presentation.navigation.Screen.EpisodeDetails
import com.london.presentation.navigation.Screen.Home
import com.london.presentation.navigation.Screen.MovieDetails
import com.london.presentation.navigation.Screen.MoviesByCategory
import com.london.presentation.navigation.Screen.Search
import com.london.presentation.navigation.Screen.TopTvShowsPicksDetails
import com.london.presentation.navigation.Screen.TvShowDetails
import com.london.presentation.screen.account.AccountScreen
import com.london.presentation.screen.bookmark.BookmarksScreen
import com.london.presentation.screen.category.CategoriesScreen
import com.london.presentation.screen.category.moviesbycategory.MoviesByCategoryScreen
import com.london.presentation.screen.details.actordetails.toptvshowspicks.TopTvShowsPicksScreen
import com.london.presentation.screen.details.movieDetalis.MovieDetailsScreen
import com.london.presentation.screen.details.tvshow.episodedetails.EpisodeDetailsScreen
import com.london.presentation.screen.details.tvshow.tvshowdetails.TvShowsDetailsScreen
import com.london.presentation.screen.home.HomeScreen
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
                exitTransition = { ExitTransition.None },
                popEnterTransition = { EnterTransition.None },
                enterTransition = { EnterTransition.None },
                popExitTransition = { ExitTransition.None },
            ) {
                HomeScreen()
            }
            composable<Search>(
                exitTransition = { ExitTransition.None },
                popEnterTransition = { EnterTransition.None },
                enterTransition = { EnterTransition.None },
                popExitTransition = { ExitTransition.None },
            ) {
                SearchScreen(
                    onNavigateToTvShowDetails = { tvShowId ->
                        navController.navigate(TvShowDetails(tvShowId))
                    },
                    onNavigateToActorDetails = { actorId ->
                        navController.navigate(TopTvShowsPicksDetails(actorId))
                    },
                    onNavigateToMovieDetails = { movieId ->
                        navController.navigate(MovieDetails(movieId))
                    }
                )
            }
            composable<Categories>(
                exitTransition = { ExitTransition.None },
                popEnterTransition = { EnterTransition.None },
                enterTransition = { EnterTransition.None },
                popExitTransition = { ExitTransition.None },
            ) {
                CategoriesScreen()
            }
            composable<Bookmarks>(
                exitTransition = { ExitTransition.None },
                popEnterTransition = { EnterTransition.None },
                enterTransition = { EnterTransition.None },
                popExitTransition = { ExitTransition.None },
            ) {
                BookmarksScreen()
            }
            composable<Account>(
                exitTransition = { ExitTransition.None },
                popEnterTransition = { EnterTransition.None },
                enterTransition = { EnterTransition.None },
                popExitTransition = { ExitTransition.None },
            ) {
                AccountScreen()
            }
            composable<TvShowDetails>(
                exitTransition = { ExitTransition.None },
                popEnterTransition = { EnterTransition.None },
                enterTransition = { EnterTransition.None },
                popExitTransition = { ExitTransition.None },
            ) { backStackEntry ->
                val tvShowDetails = backStackEntry.arguments?.let {
                    TvShowDetails(
                        tvShowId = it.getInt("tvShowId"),
                    )
                }
                TvShowsDetailsScreen(
                    onBackClick = {
                        navController.navigateUp()
                    },
                    onNavigateToEpisodeDetails = { tvShowId, episodeNumber, seasonNumber ->
                        navController.navigate(
                            Screen.EpisodeDetails(
                                tvShowId,
                                episodeNumber,
                                seasonNumber
                            )
                        )
                    }
                )
            }

            composable<TopTvShowsPicksDetails>(
                exitTransition = { ExitTransition.None },
                popEnterTransition = { EnterTransition.None },
                enterTransition = { EnterTransition.None },
                popExitTransition = { ExitTransition.None },
            ) { backStackEntry ->
                val topTvShowsPicksDetails = backStackEntry.arguments?.let {
                    TopTvShowsPicksDetails(
                        actorId = it.getInt("actorId"),
                    )
                }
                TopTvShowsPicksScreen(
                    onBackClick = {
                        navController.navigateUp()
                    },
                    onMovieClick = { movieId ->
                        navController.navigate(MovieDetails(movieId))
                    }
                )
            }

            composable<MovieDetails>(
                exitTransition = { ExitTransition.None },
                popEnterTransition = { EnterTransition.None },
                enterTransition = { EnterTransition.None },
                popExitTransition = { ExitTransition.None },
            ) {
                MovieDetailsScreen(
                    onBackClick = { navController.navigateUp() },
                    onGenreClick = {
                        navController.navigate(MoviesByCategory(it))
                    }
                )
            }
            composable<MoviesByCategory>(
                exitTransition = { ExitTransition.None },
                popEnterTransition = { EnterTransition.None },
                enterTransition = { EnterTransition.None },
                popExitTransition = { ExitTransition.None },
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

            composable<EpisodeDetails>(
                exitTransition = { ExitTransition.None },
                popEnterTransition = { EnterTransition.None },
                enterTransition = { EnterTransition.None },
                popExitTransition = { ExitTransition.None },
            ) {
                EpisodeDetailsScreen()
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