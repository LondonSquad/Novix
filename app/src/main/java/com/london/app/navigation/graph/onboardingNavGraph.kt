package com.london.app.navigation.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.london.app.navigation.AppNavGraph
import com.london.app.navigation.navigateToAuthGraph
import com.london.app.navigation.navigateToMainGraph
import com.london.app.navigation.navigateToWelcome
import com.london.presentation.feature.welcome.onboarding.OnboardingRoute
import com.london.presentation.feature.welcome.onboarding.WelcomeScreen
import com.london.presentation.navigation.Screen.OnBoarding

fun NavGraphBuilder.onboardingNavGraph(
    navController: NavHostController,
) = navigation<AppNavGraph.OnBoarding>(startDestination = OnBoarding) {
    composable<OnBoarding> {
        OnboardingRoute(
            onNavigateToWelcome = navController::navigateToWelcome,
        )
    }

    composable<OnBoarding.Welcome> {
        WelcomeScreen(
            onNavigateLogin = navController::navigateToAuthGraph,
            onNavigateAsGuest = navController::navigateToMainGraph
        )
    }
}