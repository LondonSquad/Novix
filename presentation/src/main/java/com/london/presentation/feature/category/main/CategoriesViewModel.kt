package com.london.presentation.feature.category.main

import com.london.presentation.shared.MediaCategory
import com.london.presentation.shared.base.BaseViewModel
import com.london.presentation.shared.genre.MovieGenreUi
import com.london.presentation.shared.genre.TvShowGenreUi

class CategoriesViewModel : BaseViewModel<CategoriesUiState, CategoriesEffect>(CategoriesUiState()),
    CategoriesContract {

    override fun onMovieGenreClick(genre: MovieGenreUi) =
        emitEffect(CategoriesEffect.MovieCategoryNavigation(genre))

    override fun onTvShowGenreClick(genre: TvShowGenreUi) =
        emitEffect(CategoriesEffect.TvShowCategoryNavigation(genre))

    override fun onCategoryClick(category: MediaCategory) {
        if (category == state.value.selectedCategory) return
        updateState { copy(selectedCategory = category) }
    }

}
