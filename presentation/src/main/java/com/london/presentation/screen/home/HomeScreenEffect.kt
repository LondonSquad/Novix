package com.london.presentation.screen.home

interface HomeScreenEffect {
    data class NavigationMovieDetails(val id: Int): HomeScreenEffect
    data class NavigationTvShowDetails(val id: Int): HomeScreenEffect
    data class NavigationTrendingCard(val id: Int): HomeScreenEffect
}
