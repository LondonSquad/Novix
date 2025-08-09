package com.london.presentation.feature.home.continuewatching

import com.london.domain.usecase.recent.watched.movie.ManageRecentMovieWatchedUseCase
import com.london.domain.usecase.recent.watched.tvshow.ManageRecentTvShowWatchedUseCase
import com.london.presentation.shared.MediaCategory
import com.london.presentation.shared.base.BaseViewModel
import com.london.presentation.utils.MovieGenre
import com.london.presentation.utils.TvShowGenre
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ContinueWatchingViewModel @Inject constructor(
    private val manageRecentTvShowWatchedUseCase: ManageRecentTvShowWatchedUseCase,
    private val manageRecentMovieWatchedUseCase: ManageRecentMovieWatchedUseCase,
) : BaseViewModel<ContinueWatchingUiState, ContinueWatchingEffect>(ContinueWatchingUiState()),
    ContinueWatchingContract {

    init {
        fetchRecentWatchedMedia()
    }

    override fun onMovieGenreChanged(genre: MovieGenre) {
        if (genre == state.value.selectedMovieGenre) return
        updateState { copy(selectedMovieGenre = genre) }
        fetchRecentWatchedMedia()
    }

    override fun onTvShowGenreChanged(genre: TvShowGenre) {
        if (genre == state.value.selectedTvShowGenre) return
        updateState { copy(selectedTvShowGenre = genre) }
        fetchRecentWatchedMedia()
    }

    override fun onMediaCategoryTabSelected(selectedMediaCategory: MediaCategory) {
        if (selectedMediaCategory == state.value.selectedMediaCategory) return
        updateState {
            copy(
                selectedMediaCategory = selectedMediaCategory,
                isMovieSelected = selectedMediaCategory == MediaCategory.MOVIES,
                isTvSelected = selectedMediaCategory == MediaCategory.TV_SHOWS
            )
        }
    }

    override fun onBack() = emitEffect(ContinueWatchingEffect.NavigateBack)

    override fun onNavigateToMovie(id: Int) =
        emitEffect(ContinueWatchingEffect.NavigateToMovieDetails(id))


    override fun onNavigateToTvShow(id: Int) =
        emitEffect(ContinueWatchingEffect.NavigateToTvShowDetails(id))

    override fun onRetry() {
        fetchRecentWatchedMedia()
    }

    fun fetchRecentWatchedMedia() {
        tryToExecute(
            block = {
                val recentWatchedMovie = manageRecentMovieWatchedUseCase.getAllWatchedMovies(
                    genreId = if (state.value.selectedMovieGenre == MovieGenre.All) null
                    else state.value.selectedMovieGenre.id
                )
                val recentWatchedTvShow = manageRecentTvShowWatchedUseCase.getAllRecentTvShow(
                    genreId = if (state.value.selectedTvShowGenre == TvShowGenre.All) null
                    else state.value.selectedTvShowGenre.id
                )

                Pair(recentWatchedMovie, recentWatchedTvShow)
            },
            onStart = {
                updateState { copy(isLoading = true) }
            },
            onSuccess = { (movies, shows) ->
                updateState {
                    copy(
                        movies = movies,
                        tvSeries = shows
                    )
                }
            },
            onError = { errorState -> updateState { copy(error = errorState) } },
            onCompleted = {
                updateState { copy(isLoading = false) }
            },
        )
    }

}
