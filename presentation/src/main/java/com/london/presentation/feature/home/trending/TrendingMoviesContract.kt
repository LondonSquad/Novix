package com.london.presentation.feature.home.trending

interface TrendingActorsContract {
    fun onActorClick(id: Int)
    fun onBack()
}

fun defaultTrendingActorsContract() = object : TrendingActorsContract {
    override fun onActorClick(id: Int) = Unit
    override fun onBack() = Unit
}