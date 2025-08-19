package com.london.app.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.london.app.navigation.graph.authNavGraph
import com.london.app.navigation.graph.mainNavGraph
import com.london.app.navigation.graph.onboardingNavGraph
import com.london.app.navigation.graph.splashNavGraph
import com.london.designsystem.component.NavBar
import com.london.designsystem.component.Scaffold
import com.london.designsystem.theme.NovixTheme
import com.london.presentation.navigation.LocalNavController
import com.london.presentation.navigation.Screen.Account
import com.london.presentation.navigation.Screen.Categories
import com.london.presentation.navigation.Screen.Home
import com.london.presentation.navigation.Screen.Lists
import com.london.presentation.navigation.Screen.Login
import com.london.presentation.navigation.Screen.Search

@Composable
fun NavHostGraph() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    val currentScreen = when {
        navBackStackEntry.hasRoute(Home) -> Home
        navBackStackEntry.hasRoute(Search) -> Search
        navBackStackEntry.hasRoute(Categories) -> Categories
        navBackStackEntry.hasRoute(Lists()) -> Lists()
        navBackStackEntry.hasRoute(Account) -> Account
        navBackStackEntry.hasRoute(Login) -> Login
        else -> Home
    }

    val showBottomNav = navBackStackEntry.hasRoute(Home) ||
        navBackStackEntry.hasRoute(Search) ||
        navBackStackEntry.hasRoute(Categories) ||
        navBackStackEntry.hasRoute(Lists()) ||
        navBackStackEntry.hasRoute(Account)

    Scaffold(
        containerColor = NovixTheme.colors.surface,
        bottomBar = {
            AnimatedVisibility(
                visible = showBottomNav,
                enter = slideInVertically(animationSpec = tween(), initialOffsetY = { it }),
                exit = slideOutVertically(animationSpec = tween(), targetOffsetY = { it })
            ) {
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
        CompositionLocalProvider(LocalNavController provides navController) {
            NavHost(
                navController = navController,
                startDestination = AppNavGraph.Splash,
                modifier = Modifier.consumeWindowInsets(innerPadding)
            ) {
                onboardingNavGraph(navController)
                splashNavGraph(navController)
                authNavGraph(navController)
                mainNavGraph(navController)
            }
        }
    }
}
