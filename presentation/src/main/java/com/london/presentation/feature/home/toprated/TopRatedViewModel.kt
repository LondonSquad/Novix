package com.london.presentation.feature.home.toprated

import com.london.domain.usecase.details.movie.GetMovieUseCase
import com.london.domain.usecase.details.tvshow.GetTvShowUseCase
import com.london.presentation.shared.MediaCategory
import com.london.presentation.shared.base.BaseViewModel
import com.london.presentation.shared.base.createPagingSourceFlow
import com.london.presentation.shared.genre.MovieGenreUi
import com.london.presentation.shared.genre.TvShowGenreUi
import com.london.presentation.shared.genre.toDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TopRatedViewModel @Inject constructor(
    private val getMoviesUseCase: GetMovieUseCase,
    private val getTvShowUseCase: GetTvShowUseCase,
) : BaseViewModel<TopRatedUiState, TopRatedEffect>(TopRatedUiState()), TopRatedContract {

    init {
        initializeTopRated()
    }

    override fun onRetry() {
        updateState { copy(errorMessage = null) }
        initializeTopRated()
    }

    override fun movieGenre(genre: MovieGenreUi) {
        if (genre == state.value.selectedMovieGenre) return
        updateState { copy(selectedMovieGenre = genre) }
        initializeTopMovies()
    }


    override fun tvShowGenre(genre: TvShowGenreUi) {
        if (genre == state.value.selectedTvShowGenre) return
        updateState { copy(selectedTvShowGenre = genre) }
        initializeTvShow()
    }

    override fun onMediaCategoryTabSelected(selectedMediaCategory: MediaCategory) {
        if (selectedMediaCategory == state.value.selectedMediaCategory) return
        updateState {
            copy(
                selectedMediaCategory = selectedMediaCategory,
                isMovieSelected = selectedMediaCategory == MediaCategory.Movies
            )
        }
        initializeTopRated()
    }

    override fun onBackClicked() {
        emitEffect(TopRatedEffect.NavigateBack)
    }

    override fun onMovieClick(id: Int) {
        emitEffect(TopRatedEffect.NavigateToMovieDetails(id))
    }

    override fun onTvShowClick(id: Int) {
        emitEffect(TopRatedEffect.NavigateToTvShowDetails(id))
    }

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

    private fun initializeTopRated() {
        if (state.value.isMovieSelected) initializeTopMovies()
        else initializeTvShow()
    }

    private fun initializeTopMovies() {
        tryToExecute(block = {
            val moviesFlow = createPagingSourceFlow(query = "") { _, pageNumber ->
                getMoviesUseCase.getAllTopRatedMovies(
                    pageNumber = pageNumber,
                    genre = state.value.selectedMovieGenre.toDomain()
                )
            }
            moviesFlow
        }, onStart = {
            updateState { copy(isLoading = true) }
        }, onSuccess = { media ->
            updateState { copy(movies = media) }
        }, onError = { error ->
            updateState { copy(errorMessage = error.toString()) }
        }, onCompleted = {
            updateState { copy(isLoading = false) }
        }, checkSuccess = { true })
    }

    private fun initializeTvShow() {
        tryToExecute(block = {
            val tvSeriesFlow = createPagingSourceFlow(query = "") { _, pageNumber ->
                getTvShowUseCase.getAllTopRatedTvShows(
                    pageNumber = pageNumber,
                    genre = state.value.selectedTvShowGenre.toDomain()
                )
            }
            tvSeriesFlow
        }, onStart = {
            updateState { copy(isLoading = true) }
        }, onSuccess = { media ->
            updateState { copy(tvSeries = media) }
        }, onError = { error ->
            updateState { copy(errorMessage = error.toString()) }
        }, onCompleted = {
            updateState { copy(isLoading = false) }
        }, checkSuccess = { true })
    }
}
