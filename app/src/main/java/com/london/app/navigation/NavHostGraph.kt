package com.london.app.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.london.app.navigation.graph.authNavGraph
import com.london.app.navigation.graph.mainNavGraph
import com.london.app.navigation.graph.onboardingNavGraph
import com.london.app.navigation.graph.splashNavGraph
import com.london.designsystem.component.NavBar
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
    val currentDestination = navBackStackEntry?.destination

    val currentScreen = when {
        currentDestination?.hasRoute<Home>() == true -> Home
        currentDestination?.hasRoute<Search>() == true -> Search
        currentDestination?.hasRoute<Categories>() == true -> Categories
        currentDestination?.hasRoute<Lists>() == true -> Lists()
        currentDestination?.hasRoute<Account>() == true -> Account
        currentDestination?.hasRoute<Login>() == true -> Login
        else -> Home
    }

    val showBottomNav = currentDestination?.hasRoute<Home>() == true ||
            currentDestination?.hasRoute<Search>() == true ||
            currentDestination?.hasRoute<Categories>() == true ||
            currentDestination?.hasRoute<Lists>() == true ||
            currentDestination?.hasRoute<Account>() == true

    Scaffold(
        backgroundColor = NovixTheme.colors.surface,
        bottomBar = {
            AnimatedVisibility(
                visible = showBottomNav,
                enter = slideInVertically(animationSpec = tween(), initialOffsetY = { it }),
                exit = slideOutVertically(animationSpec = tween(), targetOffsetY = { it })
            ) {
                NavBar(
                    modifier = Modifier.navigationBarsPadding(),
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
                modifier = Modifier.padding(innerPadding)
            ) {
                onboardingNavGraph(navController)
                splashNavGraph(navController)
                authNavGraph(navController)
                mainNavGraph(navController)
            }
        }
    }
}
