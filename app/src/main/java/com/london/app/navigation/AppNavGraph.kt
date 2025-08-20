package com.london.app.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface AppNavGraph {
    @Serializable
    data object Splash : AppNavGraph

    @Serializable
    data object OnBoarding : AppNavGraph

    @Serializable
    data object Auth : AppNavGraph

    @Serializable
    data object Main : AppNavGraph
}
