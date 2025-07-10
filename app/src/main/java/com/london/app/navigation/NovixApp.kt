package com.london.app.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.london.app.navigation.Screen.Categories
import com.london.app.navigation.Screen.Home
import com.london.app.navigation.Screen.Search
import com.london.designsystem.component.NavBar
import com.london.presentation.screens.categoriesScreen.CategoriesScreen
import com.london.presentation.screens.homeScreen.HomeScreen
import com.london.presentation.screens.searchScreen.SearchScreen
import androidx.compose.runtime.getValue
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.london.app.navigation.Screen.Account
import com.london.app.navigation.Screen.Bookmarks
import com.london.presentation.screens.myAccountScreen.AccountScreen
import com.london.presentation.screens.myListScreen.BookmarksScreen

@Composable
fun NovixApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val currentRoute = when {
        currentDestination?.hasRoute<Home>() == true -> "home"
        currentDestination?.hasRoute<Search>() == true -> "search"
        currentDestination?.hasRoute<Categories>() == true -> "categories"
        currentDestination?.hasRoute<Bookmarks>() == true -> "bookmarks"
        currentDestination?.hasRoute<Account>() == true -> "account"
        else -> "home"
    }

    Scaffold(
        bottomBar = {
            NavBar(
                navDestinations = NavigationHelper.getNavigationTabs(),
                currentSelectedRoute = currentRoute,
                onNavDestinationClicked = { route ->
                    navigateToBottomBarDestination(navController, route)
                }
            )
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
                SearchScreen()
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
        }
    }
}

private fun navigateToBottomBarDestination(
    navController: NavHostController,
    route: String
) {
    val destination = when (route) {
        "home" -> Home
        "search" -> Search
        "categories" -> Categories
        "bookmarks" -> Bookmarks
        "account" -> Account
        else -> Home
    }

    navController.navigate(destination) {
        popUpTo(navController.graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}