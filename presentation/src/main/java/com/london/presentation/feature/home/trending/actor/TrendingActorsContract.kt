package com.london.presentation.feature.home.trending.actor

interface TrendingActorsContract {
    fun onActorClick(id: Int)
    fun onBack()
    fun onRetry()
}

fun defaultTrendingActorsContract() = object : TrendingActorsContract {
    override fun onActorClick(id: Int) {}
    override fun onBack() {}
    override fun onRetry() {}
}
