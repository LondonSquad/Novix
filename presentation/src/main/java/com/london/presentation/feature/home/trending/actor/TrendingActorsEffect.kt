package com.london.presentation.feature.home.trending.actor

sealed interface TrendingActorsEffect {
    data object OnNavigateBackClick : TrendingActorsEffect
    data class OnNavigateToActorClick(val actorId: Int) : TrendingActorsEffect
}
