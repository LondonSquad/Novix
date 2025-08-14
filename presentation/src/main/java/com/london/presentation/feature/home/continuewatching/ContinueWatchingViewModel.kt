package com.london.presentation.feature.home.continuewatching

import com.london.domain.usecase.recent.watched.movie.ManageRecentMovieWatchedUseCase
import com.london.domain.usecase.recent.watched.tvshow.ManageRecentTvShowWatchedUseCase
import com.london.presentation.shared.MediaCategory
import com.london.presentation.shared.base.BaseViewModel
import com.london.presentation.shared.genre.MovieGenreUi
import com.london.presentation.shared.genre.TvShowGenreUi
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

    override fun onMovieGenreChanged(genre: MovieGenreUi) {
        if (genre == state.value.selectedMovieGenre) return
        updateState { copy(selectedMovieGenre = genre) }
        fetchRecentWatchedMedia()
    }

    override fun onTvShowGenreChanged(genre: TvShowGenreUi) {
        if (genre == state.value.selectedTvShowGenre) return
        updateState { copy(selectedTvShowGenre = genre) }
        fetchRecentWatchedMedia()
    }

    override fun onMediaCategoryTabSelected(selectedMediaCategory: MediaCategory) {
        if (selectedMediaCategory == state.value.selectedMediaCategory) return
        updateState {
            copy(
                selectedMediaCategory = selectedMediaCategory,
                isMovieSelected = selectedMediaCategory == MediaCategory.Movies,
                isTvSelected = selectedMediaCategory == MediaCategory.TvShows
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

    private fun fetchRecentWatchedMedia() {
        tryToExecute(
            block = {
                val recentWatchedMovie = manageRecentMovieWatchedUseCase.getAllWatchedMovies(
                    genreId = if (state.value.selectedMovieGenre == MovieGenreUi.All) null
                    else state.value.selectedMovieGenre.id
                )
                val recentWatchedTvShow = manageRecentTvShowWatchedUseCase.getAllRecentTvShow(
                    genreId = if (state.value.selectedTvShowGenre == TvShowGenreUi.All) null
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
