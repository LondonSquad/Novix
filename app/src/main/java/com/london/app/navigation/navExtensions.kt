package com.london.app.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.london.data.utils.isTrue
import timber.log.Timber

fun NavController.navigateTo(
    route: Any,
    popBackStack: Boolean = true
) = runCatching {
    if (currentBackStackEntry?.destination?.hasRoute(route::class) == true) return@runCatching
    navigate(
        route = route,
        builder = {
            if (popBackStack)
                popUpTo(0) {
                    inclusive = true
                    saveState = true
                }
            launchSingleTop = true
            restoreState = true
        }
    )
}.onFailure(Timber::e)

inline fun <reified T : Any> NavGraphBuilder.appComposable(
    noinline content: @Composable (AnimatedContentScope.(NavBackStackEntry) -> Unit)
) = composable<T>(
    exitTransition = { fadeOut(tween(500)) },
    popEnterTransition = { fadeIn(tween(500)) },
    enterTransition = { fadeIn(tween(500)) },
    popExitTransition = { fadeOut(tween(500)) },
    content = content
)

fun NavBackStackEntry?.hasRoute(vararg routes: Any): Boolean = routes.any {
    this?.destination?.hasRoute(it::class).isTrue
 }