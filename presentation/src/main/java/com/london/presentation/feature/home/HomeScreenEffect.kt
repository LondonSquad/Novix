package com.london.presentation.feature.home

interface HomeScreenEffect {
    data class NavigationTvShowDetails(val id: Int): HomeScreenEffect
    data class NavigationMovieDetails(val id: Int): HomeScreenEffect
    object NavigationContinueWatching: HomeScreenEffect
    object NavigationTopRated: HomeScreenEffect
}
