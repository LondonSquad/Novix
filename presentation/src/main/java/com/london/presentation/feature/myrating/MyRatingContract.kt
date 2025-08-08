package com.london.presentation.feature.myrating

interface MyRatingContract {
    fun onBackClicked()
    fun onItemClick(id: Int)
    fun onDelete(id: Int)
}

fun defaultMyRatingContract() = object : MyRatingContract {
    override fun onBackClicked() {}
    override fun onItemClick(id: Int) {}
    override fun onDelete(id: Int) {}
}
