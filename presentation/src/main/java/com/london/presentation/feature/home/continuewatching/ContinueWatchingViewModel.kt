package com.london.presentation.feature.home.continuewatching

import com.london.domain.entity.movie.Movie
import com.london.domain.entity.tvshow.TvShow
import com.london.domain.usecase.recent.watched.movie.ManageRecentMovieWatchedUseCase
import com.london.domain.usecase.recent.watched.tvshow.ManageRecentTvShowWatchedUseCase
import com.london.presentation.shared.MediaCategory
import com.london.presentation.shared.base.BaseViewModel
import com.london.presentation.shared.genre.MovieGenreUi
import com.london.presentation.shared.genre.TvShowGenreUi
import com.london.presentation.shared.genre.toDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class ContinueWatchingViewModel @Inject constructor(
    private val manageRecentMovieWatchedUseCase: ManageRecentMovieWatchedUseCase,
    private val manageRecentTvShowWatchedUseCase: ManageRecentTvShowWatchedUseCase
) : BaseViewModel<ContinueWatchingUiState, ContinueWatchingEffect>(ContinueWatchingUiState()),
    ContinueWatchingContract {

    init {
        getRecentWatchedMedia()
    }

    override fun onMovieGenreClick(genre: MovieGenreUi) {
        isNotCurrentGenreSelected(genre) {
            updateState { copy(selectedMovieGenre = genre) }
            getRecentWatchedMedia()
        }
    }

    override fun onTvShowGenreClick(genre: TvShowGenreUi) {
        isNotCurrentGenreSelected(genre) {
            updateState { copy(selectedTvShowGenre = genre) }
            getRecentWatchedMedia()
        }
    }

    override fun onMediaCategoryTabClick(selectedMediaCategory: MediaCategory) {
        if (isNotCurrentTabSelected(selectedMediaCategory))
            setSelectedCategory(selectedMediaCategory)
    }

    override fun onBackClick() = emitEffect(ContinueWatchingEffect.NavigateBack)

    override fun onNavigateToMovieClick(id: Int) =
        emitEffect(ContinueWatchingEffect.NavigateToMovieDetails(id))

    override fun onNavigateToTvShowClick(id: Int) =
        emitEffect(ContinueWatchingEffect.NavigateToTvShowDetails(id))

    override fun onRetryClick() = getRecentWatchedMedia()

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

    private fun setSelectedCategory(category: MediaCategory) =
        updateState { copy(selectedMediaCategory = category) }

    private inline fun <reified T : Enum<T>> isNotCurrentGenreSelected(
        genre: T,
        onNotSelected: () -> Unit
    ) {
        val alreadySelected = when (genre) {
            is MovieGenreUi -> genre == state.value.selectedMovieGenre
            is TvShowGenreUi -> genre == state.value.selectedTvShowGenre
            else -> false
        }

        if (!alreadySelected) onNotSelected()
    }

    fun getRecentWatchedMedia() {
        tryToExecute(
            block = ::fetchRecentWatchedMedia,
            onStart = { setLoadingState(true) },
            onSuccess = { (movies, tvSeries) -> setContinueWatchingMedia(movies, tvSeries) },
            onCompleted = { setLoadingState(false) },
        )
    }

    private fun isNotCurrentTabSelected(mediaCategory: MediaCategory): Boolean =
        mediaCategory != state.value.selectedMediaCategory

    private suspend fun fetchRecentWatchedMedia(): Pair<Flow<List<Movie>>, Flow<List<TvShow>>> {
        val recentWatchedMovie = manageRecentMovieWatchedUseCase.getAllWatchedMovies(
            genre = state.value.selectedMovieGenre.toDomain()
        )
        val recentWatchedTvShow = manageRecentTvShowWatchedUseCase.getAllRecentTvShow(
            genre = state.value.selectedTvShowGenre.toDomain()
        )
        return recentWatchedMovie to recentWatchedTvShow
    }

    private fun setContinueWatchingMedia(movies: Flow<List<Movie>>, tvSeries: Flow<List<TvShow>>) =
        updateState { copy(movies = movies, tvSeries = tvSeries) }

    private fun setLoadingState(isLoading: Boolean) = updateState { copy(isLoading = isLoading) }
}
