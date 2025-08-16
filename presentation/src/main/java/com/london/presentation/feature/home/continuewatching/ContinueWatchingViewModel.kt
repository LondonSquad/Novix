package com.london.presentation.feature.home.continuewatching

import com.london.domain.usecase.recent.watched.movie.ManageRecentMovieWatchedUseCase
import com.london.domain.usecase.recent.watched.tvshow.ManageRecentTvShowWatchedUseCase
import com.london.presentation.shared.MediaCategory
import com.london.presentation.shared.base.BaseViewModel
import com.london.presentation.shared.base.ErrorState
import com.london.presentation.shared.genre.MovieGenreUi
import com.london.presentation.shared.genre.TvShowGenreUi
import com.london.presentation.shared.genre.toDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ContinueWatchingViewModel @Inject constructor(
    private val manageRecentTvShowWatchedUseCase: ManageRecentTvShowWatchedUseCase,
    private val manageRecentMovieWatchedUseCase: ManageRecentMovieWatchedUseCase,
) : BaseViewModel<ContinueWatchingUiState, ContinueWatchingEffect>(ContinueWatchingUiState()),
    ContinueWatchingContract {

    init {
        getRecentWatchedMedia()
    }

    override fun onMovieGenreClick(genre: MovieGenreUi) {
        if (genre == state.value.selectedMovieGenre) return
        updateState { copy(selectedMovieGenre = genre) }
        getRecentWatchedMedia()
    }

    override fun onTvShowGenreClick(genre: TvShowGenreUi) {
        if (genre == state.value.selectedTvShowGenre) return
        updateState { copy(selectedTvShowGenre = genre) }
        getRecentWatchedMedia()
    }

    override fun onMediaCategoryTabClick(selectedMediaCategory: MediaCategory) {
        isNotSelectedMediaCategory(selectedMediaCategory)
        updateState {
            copy(
                selectedMediaCategory = selectedMediaCategory,
                isMovieSelected = isSelectedMediaCategory(MediaCategory.Movies),
                isTvSelected = isSelectedMediaCategory(MediaCategory.TvShows)
            )
        }
    }

    private fun <T : Any> isNotSelectedMediaCategory(mediaCategory: T) =
        mediaCategory != state.value.selectedMediaCategory

    fun isSelectedMediaCategory(mediaCategory: MediaCategory) =
        mediaCategory == state.value.selectedMediaCategory

    override fun onBackClick() = emitEffect(ContinueWatchingEffect.NavigateBack)

    override fun onNavigateToMovie(id: Int) =
        emitEffect(ContinueWatchingEffect.NavigateToMovieDetails(id))

    override fun onNavigateToTvShow(id: Int) =
        emitEffect(ContinueWatchingEffect.NavigateToTvShowDetails(id))

    override fun onRetryCLick() = getRecentWatchedMedia()


    private fun getRecentWatchedMedia() {
        tryToExecute(
            block = {
                val recentWatchedMovie = manageRecentMovieWatchedUseCase.getAllWatchedMovies(
                    genre = state.value.selectedMovieGenre.toDomain()
                )
                val recentWatchedTvShow = manageRecentTvShowWatchedUseCase.getAllRecentTvShow(
                    genre = state.value.selectedTvShowGenre.toDomain()
                )
                recentWatchedMovie to recentWatchedTvShow
            },
            onStart = { setLoadingState(true) },
            onSuccess = { (movies, shows) ->
                updateState {
                    copy(
                        movies = movies,
                        tvSeries = shows
                    )
                }
            },
            onError = ::setErrorState,
            onCompleted = { setLoadingState(false) },
        )
    }

    private fun setErrorState(errorState: ErrorState) = updateState { copy(error = errorState) }
    private fun setLoadingState(isLoading: Boolean) = updateState { copy(isLoading = isLoading) }
}
