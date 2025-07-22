package com.london.presentation.screen.home

import com.london.domain.usecase.GetPopularMovies
import com.london.domain.usecase.GetUpComingMoviesByCategoryUseCase
import com.london.presentation.screen.base.BaseViewModel
import com.london.presentation.screen.base.createPagingSourceFlow
import com.london.presentation.utils.Genre
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class HomeViewModel(
    private val getPopularMovies: GetPopularMovies,
    private val getUpcomingMoviesByCategoryUseCase: GetUpComingMoviesByCategoryUseCase,
) : BaseViewModel<HomeScreenUiState, HomeScreenEffect>(HomeScreenUiState()), HomeScreenContract {

    init {
        initializePopularMovies()
        loadUpcomingMovies(categoryId = null)
    }

    private fun initializePopularMovies() {
        tryToExecute(
            block = { getPopularMovies.invoke() },
            onStart = { updateState { copy(isLoading = true) } },
            onSuccess = { popularMovies ->
                updateState { copy(popularMovies = popularMovies) }
            },
            onError = { errorState -> updateState { copy(error = errorState) } },
            onCompleted = { updateState { copy(isLoading = false) } },
            checkSuccess = { it.isNotEmpty() },
        )
    }

    override fun onMovieClick(id: Int) {
        emitEffect(HomeScreenEffect.NavigationMovieDetails(id))
    }

    private fun loadUpcomingMovies(categoryId: Int?) {
        tryToExecute(
            block = {
                val moviesFlow = createPagingSourceFlow(query = "") { _, pageNumber ->
                    val movies = getUpcomingMoviesByCategoryUseCase.invoke(
                        categoryId = categoryId,
                        pageNumber = pageNumber
                    )
                    movies.copy(items = movies.items)
                }
                moviesFlow},
            onStart = { updateState { copy(isLoading = true) } },
            onSuccess = { upcomingMovies ->
                updateState { copy(upcomingMovies = upcomingMovies) }
            },
            onError = { errorState -> updateState { copy(error = errorState) } },
            onCompleted = { updateState { copy(isLoading = false) } },
        )
    }

    override fun onGenreSelect(genre: Genre) {
        updateState { copy(selectedGenre = genre) }
        loadUpcomingMovies(
            categoryId = if (genre == Genre.All) null else genre.id
        )
    }
}
