package com.london.presentation.feature.continuewatching

import com.london.domain.usecase.recent.watched.GetRecentWatchedMoviesUseCase
import com.london.domain.usecase.recent.watched.GetRecentWatchedTvShowsUseCase
import com.london.presentation.feature.base.BaseViewModel
import com.london.presentation.utils.MovieGenre
import com.london.presentation.utils.TvShowGenre
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ContinueWatchingViewModel @Inject constructor(
    private val getRecentWatchedMoviesUseCase: GetRecentWatchedMoviesUseCase,
    private val getRecentWatchedTvShowsUseCase: GetRecentWatchedTvShowsUseCase,
) : BaseViewModel<ContinueWatchingUiState, ContinueWatchingEffect>(ContinueWatchingUiState()),
    ContinueWatchingContract {

    init {
        initializeContinueWatching()
    }

    private fun initializeContinueWatching() {
        tryToExecute(
            block = {
                val recentWatchedMovie = getRecentWatchedMoviesUseCase.invoke(
                    genreId = if (state.value.selectedMovieGenre == MovieGenre.All) null
                    else state.value.selectedMovieGenre.id
                )
                val recentWatchedTvShow = getRecentWatchedTvShowsUseCase.invoke(
                    genreId = if (state.value.selectedTvShowGenre == TvShowGenre.All) null
                    else state.value.selectedTvShowGenre.id
                )

                Pair(recentWatchedMovie, recentWatchedTvShow)
            },
            onStart = {
                updateState { copy(isLoading = true) }
            },
            onSuccess = {
                updateState {
                    copy(
                        movies = it.first,
                        tvSeries = it.second
                    )
                }
            },
            onError = { errorState -> updateState { copy(error = errorState) } },
            onCompleted = {
                updateState { copy(isLoading = false) }
            },
        )
    }

    override fun movieGenre(genre: MovieGenre) {
        if (genre == state.value.selectedMovieGenre) return
        updateState { copy(selectedMovieGenre = genre) }
        initializeContinueWatching()
    }

    override fun tvShowGenre(genre: TvShowGenre) {
        if (genre == state.value.selectedTvShowGenre) return
        updateState { copy(selectedTvShowGenre = genre) }
        initializeContinueWatching()
    }

    override fun tabSelected(index: Int) {
        if (index == state.value.tabSelected) return
        updateState {
            copy(
                tabSelected = index, isMovieSelected = index == 0
            )
        }
        initializeContinueWatching()
    }

    override fun onBack() =
        emitEffect(ContinueWatchingEffect.NavigateBack)

    override fun onNavigateToMovie(id: Int) =
        emitEffect(ContinueWatchingEffect.NavigateToMovieDetails(id))


    override fun onNavigateToTvShow(id: Int) =
        emitEffect(ContinueWatchingEffect.NavigateToTvShowDetails(id))

}
