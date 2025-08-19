package com.london.presentation.feature.category.movie

import androidx.lifecycle.SavedStateHandle
import androidx.paging.PagingData
import com.london.domain.entity.movie.Movie
import com.london.domain.usecase.details.movie.GetMovieUseCase
import com.london.presentation.navigation.Screen
import com.london.presentation.navigation.getArgs
import com.london.presentation.shared.base.BaseViewModel
import com.london.presentation.shared.base.ErrorState
import com.london.presentation.shared.base.createPagingSourceFlow
import com.london.presentation.shared.genre.MovieGenreUi
import com.london.presentation.shared.genre.toDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MovieCategoryViewModel @Inject constructor(
    private val getMovieUseCase: GetMovieUseCase,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<MovieCategoryUiState, MovieCategoryEffect>(MovieCategoryUiState()),
    MovieCategoryContract {

    private val args = savedStateHandle.getArgs<Screen.MoviesByCategory>()
    private val genre =
        args?.category ?: MovieGenreUi.All

    init {
        initializeMovies(genre)
    }

    override fun onMovieClick(movieId: Int) =
        emitEffect(MovieCategoryEffect.MovieDetailsNavigation(movieId = movieId))

    override fun onBack() =
        emitEffect(MovieCategoryEffect.BackNavigation)

    override fun onManageBookmarkClicked(movieId: Int) {
        updateState {
            copy(
                isBookmarkSheetVisible = true,
                bookmarkedMovieId = movieId
            )
        }
    }

    override fun onBookmarkSheetDismiss() {
        updateState {
            copy(
                isBookmarkSheetVisible = false,
                bookmarkedMovieId = 0
            )
        }
    }

    private fun initializeMovies(genreUi: MovieGenreUi) {
        tryToExecute(
            onStart = { onInitializeMoviesStarted(genre = genreUi) },
            block = { createMoviesPagingSourceFlow(genreUi = genreUi) },
            onSuccess = ::onInitializeMoviesSuccess,
            checkSuccess = { genreUi != MovieGenreUi.All },
            onError = ::onInitializeMoviesFailed,
            onCompleted = ::onInitializeMoviesCompleted
        )
    }

    private fun createMoviesPagingSourceFlow(genreUi: MovieGenreUi): Flow<PagingData<Movie>> {

        return createPagingSourceFlow(query = "") { _, pageNumber ->
            getMovieUseCase.getMoviesByGenre(
                genre = genreUi.toDomain(),
                pageNumber = pageNumber
            )
        }
    }

    private fun onInitializeMoviesStarted(genre: MovieGenreUi) =
        updateState { copy(genre = genre, isLoading = true) }

    private fun onInitializeMoviesSuccess(moviesFlow: Flow<PagingData<Movie>>) =
        updateState { copy(moviesFlow = moviesFlow) }

    private fun onInitializeMoviesCompleted() =
        updateState { copy(isLoading = false) }

    private fun onInitializeMoviesFailed(errorState: ErrorState) =
        updateState { copy(error = errorState) }
}
