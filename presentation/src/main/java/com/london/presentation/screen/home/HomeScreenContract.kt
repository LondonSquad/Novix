package com.london.presentation.screen.home

import com.london.presentation.utils.Genre

interface HomeScreenContract {
    fun onMovieClick(id: Int)
    fun onGenreSelect(genre: Genre)
    fun onTrendingCardClicked(id: Int)
}
