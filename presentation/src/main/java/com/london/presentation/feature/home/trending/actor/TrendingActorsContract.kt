package com.london.presentation.feature.home.trending.actor

interface TrendingActorsContract {
    fun onBackClick()
    fun onRetryClick()
    fun onActorClick(id: Int)
}

fun defaultTrendingActorsContract() = object : TrendingActorsContract {
    override fun onBackClick() {}
    override fun onRetryClick() {}
    override fun onActorClick(id: Int) {}
}
