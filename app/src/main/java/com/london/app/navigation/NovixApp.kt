package com.london.app.navigation

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
import com.london.app.navigation.Screen.Account
import com.london.app.navigation.Screen.Bookmarks
import com.london.app.navigation.Screen.Categories
import com.london.app.navigation.Screen.Home
import com.london.app.navigation.Screen.Search
import com.london.app.navigation.Screen.TvShowDetails
import com.london.designsystem.component.NavBar
import com.london.presentation.screen.search.SearchScreen
import com.london.presentation.screen.account.AccountScreen
import com.london.presentation.screen.bookmark.BookmarksScreen
import com.london.presentation.screen.category.CategoriesScreen
import com.london.presentation.screen.home.HomeScreen
import com.london.presentation.screen.details.tvshow.tvshowdetails.TvShowsDetailsScreen

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

    // Show bottom navigation only for main screens
    val showBottomNav = currentDestination?.hasRoute<TvShowDetails>() != true

    Scaffold(
        bottomBar = {
            if (showBottomNav) {
                NavBar(
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