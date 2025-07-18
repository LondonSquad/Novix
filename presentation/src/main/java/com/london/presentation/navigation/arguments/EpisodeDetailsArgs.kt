package com.london.presentation.navigation.arguments

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.london.presentation.navigation.Screen

class EpisodeDetailsArgs(savedStateHandle: SavedStateHandle) {
    private val route = savedStateHandle.toRoute<Screen.EpisodeDetails>()
    val tvShowId = route.tvShowId
    val episodeNumber = route.episodeNumber
    val seasonNumber = route.seasonNumber
}