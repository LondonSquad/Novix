package com.london.presentation.screen.home.trending

sealed class TrendingActorsEffect {
    data class NavigateToActor(val actorId: Int) : TrendingActorsEffect()
    data object NavigateBack : TrendingActorsEffect()
} 