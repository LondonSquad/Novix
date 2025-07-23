package com.london.presentation.screen.home

interface HomeScreenEffect {
    data class NavigationMovieDetails(val id: Int): HomeScreenEffect
}
