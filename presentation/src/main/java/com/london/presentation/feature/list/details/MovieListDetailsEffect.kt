package com.london.presentation.feature.list.details

interface MovieListDetailsEffect {
    object NavigateBack : MovieListDetailsEffect
    data class NavigationMovieDetails(val id: Int) : MovieListDetailsEffect
}
