package com.london.presentation.screen.home

interface HomeScreenEffect {
    data class NavigationPopularCard(val id: Int): HomeScreenEffect
}
