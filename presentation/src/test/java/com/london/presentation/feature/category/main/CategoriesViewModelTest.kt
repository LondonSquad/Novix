package com.london.presentation.feature.category.main

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.london.presentation.shared.MediaCategory
import com.london.presentation.utils.MovieGenre
import com.london.presentation.utils.TvShowGenre
import kotlinx.coroutines.test.runTest
import org.junit.Test

class CategoriesViewModelTest {
    private var viewModel: CategoriesViewModel? = CategoriesViewModel()

    @Test
    fun `onMovieGenreClick should emit NavigateToMovieCategory effect`() = runTest {
        // Given
        val genre = MovieGenre.Action
        // When // Then
        viewModel?.effect?.test {
            viewModel?.onMovieGenreClick(genre)
            assertThat(awaitItem()).isInstanceOf(CategoriesEffect.NavigateToMovieCategory::class.java)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onTvShowGenreClick should emit NavigateToTvShowCategory effect`() = runTest {
        // Given
        val genre = TvShowGenre.Animation
        // When // Then
        viewModel?.effect?.test {
            viewModel?.onTvShowGenreClick(genre)
            assertThat(awaitItem()).isInstanceOf(CategoriesEffect.NavigateToTvShowCategory::class.java)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onCategoryClick should update selectedCategory`() = runTest {
        // Given
        val category = MediaCategory.TvShows
        // When
        viewModel?.onCategoryClick(category)
        // Then
        viewModel?.state?.test {
            assertThat(awaitItem().selectedCategory).isEqualTo(category)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onCategoryClick should not update selectedCategory if it is already selected`() = runTest {
        // Given
        val category = MediaCategory.Movies
        // When
        viewModel?.onCategoryClick(category)
        viewModel?.onCategoryClick(category)
        // Then
        viewModel?.state?.test {
            assertThat(awaitItem().selectedCategory).isEqualTo(category)
            cancelAndIgnoreRemainingEvents()
        }
    }
}
