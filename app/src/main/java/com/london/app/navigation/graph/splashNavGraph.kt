package com.london.app.navigation.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.london.app.navigation.AppNavGraph
import com.london.app.navigation.navigateToMainGraph
import com.london.app.navigation.navigateToOnboardingGraph
import com.london.app.navigation.navigateToWelcome
import com.london.presentation.feature.welcome.splash.SplashRoute
import com.london.presentation.navigation.Screen

fun NavGraphBuilder.splashNavGraph(
    navController: NavHostController,
) = navigation<AppNavGraph.Splash>(startDestination = Screen.Splash) {
    composable<Screen.Splash> {
        SplashRoute(
            onNavigateToOnboarding = navController::navigateToOnboardingGraph,
            onNavigateToWelcome = navController::navigateToWelcome,
            onNavigateToHome = navController::navigateToMainGraph,
        )
    }
}
