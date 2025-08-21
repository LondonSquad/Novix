package com.london.app.navigation.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.london.app.navigation.AppNavGraph
import com.london.app.navigation.appComposable
import com.london.app.navigation.navigateTo
import com.london.app.navigation.navigateToLoginWithPopUp
import com.london.app.navigation.navigateToMainGraph
import com.london.app.navigation.navigateToRegister
import com.london.app.navigation.navigateToWelcome
import com.london.presentation.feature.authentication.login.LoginScreen
import com.london.presentation.feature.authentication.register.RegistrationScreen
import com.london.presentation.navigation.Screen.Login
import com.london.presentation.navigation.Screen.Register

fun NavGraphBuilder.authNavGraph(
    navController: NavHostController
) = navigation<AppNavGraph.Auth>(startDestination = Login()) {
    appComposable<Login> {
        val currentRoute = it.toRoute<Login>()
        LoginScreen(
            onNavigateToHome = {
                currentRoute.source?.let(navController::navigateTo)
                    ?: navController.navigateToMainGraph()
            },
            onNavigateBack = navController::navigateToWelcome,
            onNavigateToRegister = navController::navigateToRegister
        )
    }

    appComposable<Register> {
        RegistrationScreen(
            onNavigateBack = navController::navigateUp,
            onRegisterComplete = navController::navigateToLoginWithPopUp
        )
    }
}
