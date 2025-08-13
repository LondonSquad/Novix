package com.london.presentation.feature.category.main

import com.london.presentation.shared.MediaCategory
import com.london.presentation.utils.MovieGenre
import com.london.presentation.utils.TvShowGenre

interface CategoriesContract {

    fun onMovieGenreClick(genre: MovieGenre)
    fun onTvShowGenreClick(genre: TvShowGenre)
    fun onCategoryClick(category: MediaCategory)
}
