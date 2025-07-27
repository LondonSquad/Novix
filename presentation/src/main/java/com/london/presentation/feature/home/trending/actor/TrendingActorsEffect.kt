package com.london.presentation.feature.home.trending.actor

sealed interface TrendingActorsEffect {
    data class NavigateToActor(val actorId: Int) : TrendingActorsEffect
    data object NavigateBack : TrendingActorsEffect
}