package com.london.app.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface Screen {
    @Serializable
    object Home : Screen

    @Serializable
    object Search : Screen

    @Serializable
    object Categories : Screen

    @Serializable
    object Bookmarks : Screen

    @Serializable
    object Account : Screen
}