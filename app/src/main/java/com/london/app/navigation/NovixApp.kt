package com.london.app.navigation

import android.util.Log
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
import com.london.presentation.navigation.Screen.ActorDetails
import com.london.presentation.navigation.Screen.ActorTopMoviesPicksDetails
import com.london.presentation.navigation.Screen.Bookmarks
import com.london.presentation.navigation.Screen.Categories
import com.london.presentation.navigation.Screen.Home
import com.london.presentation.navigation.Screen.MovieDetails
import com.london.presentation.navigation.Screen.Search
import com.london.presentation.navigation.Screen.TvShowDetails
import com.london.presentation.screen.account.AccountScreen
import com.london.presentation.screen.bookmark.BookmarksScreen
import com.london.presentation.screen.category.CategoriesScreen
import com.london.presentation.screen.details.actor.ActorDetailsScreen
import com.london.presentation.screen.details.actordetails.topmoviespicks.TopMoviesPicksScreen
import com.london.presentation.screen.details.movieDetalis.MovieDetailsScreen
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
            composable<Home> {
                HomeScreen()
            }
            composable<Search> {
                SearchScreen(
                    onNavigateToTvShowDetails = { tvShowId ->
                        navController.navigate(TvShowDetails(tvShowId))
                    },
                    onNavigateToActorDetails = { actorId ->
                        navController.navigate(ActorDetails(actorId))
                    },
                    onNavigateToMovieDetails = { movieId ->
                        navController.navigate(MovieDetails(movieId))
                    }
                )
            }
            composable<Categories> {
                CategoriesScreen()
            }
            composable<Bookmarks> {
                BookmarksScreen()
            }
            composable<Account> {
                AccountScreen()
            }
            composable<TvShowDetails> { backStackEntry ->
                val tvShowDetails = backStackEntry.arguments?.let {
                    TvShowDetails(
                        tvShowId = it.getInt("tvShowId"),
                    )
                }
                TvShowsDetailsScreen(
                    onBackClick = {
                        navController.navigateUp()
                    }
                )
            }

            composable<ActorTopMoviesPicksDetails> { backStackEntry ->
                val actorTopMoviesPicksDetails = backStackEntry.arguments?.let {
                    ActorTopMoviesPicksDetails(
                        actorId = it.getInt("actorId"),
                    )
                }
                TopMoviesPicksScreen()
            }
            composable<MovieDetails> { backStackEntry ->
                val movieDetails = backStackEntry.arguments?.let {
                    MovieDetailsScreen(
                        movieId = it.getInt("movieId"),
                        onBackClick = { navController.navigateUp() },

                        )
                }
            }

            composable<ActorDetails> { backStackEntry ->
                val actorDetails = backStackEntry.arguments?.let {
                    ActorDetails(
                        actorId = it.getInt("actorId"),
                    )
                }
                ActorDetailsScreen(
                    onNavigateToMoviePicks = { actorId->
                        navController.navigate(ActorTopMoviesPicksDetails(actorId))
                    },
                    onBackClick = {
                        navController.navigateUp()
                    }
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