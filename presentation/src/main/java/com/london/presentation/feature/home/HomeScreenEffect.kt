package com.london.presentation.feature.home

interface HomeScreenEffect {
    object NavigationTopRated : HomeScreenEffect
    object NavigationTrendingActor : HomeScreenEffect
    object NavigationTrendingMovie : HomeScreenEffect
    object NavigationTrendingTvShows : HomeScreenEffect
    object NavigationContinueWatching : HomeScreenEffect
    data class NavigationTvShowDetails(val id: Int) : HomeScreenEffect
    data class NavigationMovieDetails(val id: Int) : HomeScreenEffect
}
