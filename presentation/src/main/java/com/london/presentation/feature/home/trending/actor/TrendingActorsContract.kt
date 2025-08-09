package com.london.presentation.feature.home.trending.actor

interface TrendingActorsContract {
    fun onBack()
    fun onRetry()
    fun onActorClick(id: Int)
}

fun defaultTrendingActorsContract() = object : TrendingActorsContract {
    override fun onBack() {}
    override fun onRetry() {}
    override fun onActorClick(id: Int) {}
}
