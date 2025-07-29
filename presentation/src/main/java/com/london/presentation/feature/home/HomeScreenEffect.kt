package com.london.presentation.feature.home

interface HomeScreenEffect {
    data class NavigationMovieDetails(val id: Int) : HomeScreenEffect
    data class NavigationTvShowDetails(val id: Int) : HomeScreenEffect
    object NavigationTrendingMovie : HomeScreenEffect
    object NavigationTrendingTvShows : HomeScreenEffect
    object NavigationTrendingActor : HomeScreenEffect
    object NavigationTopRated : HomeScreenEffect
}
