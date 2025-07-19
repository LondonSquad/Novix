package com.london.presentation.navigation.arguments

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.london.presentation.navigation.Screen

class ReviewsScreenArgs(savedStateHandle: SavedStateHandle) {
    private val route = savedStateHandle.toRoute<Screen.Reviews>()
    val mediaId = route.mediaId
    val mediaType = route.mediaType
}