package com.london.presentation.feature.home.trending.actor

sealed interface TrendingActorsEffect {
    data object BackClickNavigation : TrendingActorsEffect
    data class ActorClickNavigation(val actorId: Int) : TrendingActorsEffect
}
