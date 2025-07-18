package com.london.presentation.navigation.arguments

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.london.presentation.navigation.Screen

class TvShowDetailsArgs(savedStateHandle: SavedStateHandle) {
    private val route = savedStateHandle.toRoute<Screen.TvShowDetails>()
    val tvShowId=route.tvShowId
}