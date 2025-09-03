package com.london.presentation.feature.list.details

interface MovieListEffect {
    object NavigateBack : MovieListEffect
    data class NavigationMovieDetails(val id: Int) : MovieListEffect
}
