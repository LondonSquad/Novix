package com.london.presentation.feature.category.main

import com.london.presentation.shared.MediaCategory
import com.london.presentation.shared.base.BaseViewModel
import com.london.presentation.utils.MovieGenre
import com.london.presentation.utils.TvShowGenre

class CategoriesViewModel : BaseViewModel<CategoriesUiState, CategoriesEffect>(CategoriesUiState()),
    CategoriesContract {

    override fun onMovieGenreClick(genre: MovieGenre) =
        emitEffect(CategoriesEffect.NavigateToMovieCategory(genre))

    override fun onTvShowGenreClick(genre: TvShowGenre) =
        emitEffect(CategoriesEffect.NavigateToTvShowCategory(genre))

    override fun onCategoryClick(category: MediaCategory) {
        if (category == state.value.selectedCategory) return
        updateState { copy(selectedCategory = category) }
    }
}
