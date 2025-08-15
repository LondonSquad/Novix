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
import com.london.presentation.navigation.Screen.Splash

fun NavGraphBuilder.splashNavGraph(
    navController: NavHostController,
) = navigation<AppNavGraph.Splash>(startDestination = Splash) {
    composable<Splash> {
        SplashRoute(
            onNavigateToOnboarding = navController::navigateToOnboardingGraph,
            onNavigateToWelcome = navController::navigateToWelcome,
            onNavigateToHome = navController::navigateToMainGraph,
        )
    }
}
