package com.london.presentation.navigation

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavHostController

val LocalNavController =
    staticCompositionLocalOf<NavHostController> { error("Nav controller is not provided") }
