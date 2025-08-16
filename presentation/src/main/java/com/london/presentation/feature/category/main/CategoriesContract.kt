package com.london.presentation.feature.category.main

import com.london.presentation.shared.MediaCategory
import com.london.presentation.shared.genre.MovieGenreUi
import com.london.presentation.shared.genre.TvShowGenreUi

interface CategoriesContract {

    fun onMovieGenreClick(genre: MovieGenreUi)
    fun onTvShowGenreClick(genre: TvShowGenreUi)
    fun onCategoryClick(category: MediaCategory)
}
