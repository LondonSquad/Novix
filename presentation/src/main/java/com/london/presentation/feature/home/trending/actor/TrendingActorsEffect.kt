package com.london.presentation.feature.home.trending.actor

sealed interface TrendingActorsEffect {
    data object BackNavigation : TrendingActorsEffect
    data class ActorDetailsNavigation(val actorId: Int) : TrendingActorsEffect
}
