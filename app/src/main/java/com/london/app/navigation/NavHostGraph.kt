package com.london.app.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
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
import com.london.designsystem.theme.NovixTheme
import com.london.presentation.navigation.LocalNavController

@Composable
fun NavHostGraph() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    Scaffold(
        backgroundColor = NovixTheme.colors.surface,
        bottomBar = {
            NavBar(
                destinations = NavigationHelper.destinations,
                navController = navController,
                backStackEntry = navBackStackEntry
            )
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
