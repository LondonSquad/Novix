package com.london.presentation.feature.home

interface HomeScreenEffect {
    object TopRatedNavigation : HomeScreenEffect
    object TrendingActorNavigation : HomeScreenEffect
    object TrendingMovieNavigation : HomeScreenEffect
    object TrendingTvShowsNavigation : HomeScreenEffect
    object ContinueWatchingNavigation : HomeScreenEffect
    data class TvShowDetailsNavigation(val id: Int) : HomeScreenEffect
    data class MovieDetailsNavigation(val id: Int) : HomeScreenEffect
}
