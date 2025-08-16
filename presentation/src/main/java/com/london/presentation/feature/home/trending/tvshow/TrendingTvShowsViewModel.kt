package com.london.presentation.feature.home.trending.tvshow

import com.london.domain.usecase.details.tvshow.GetTvShowUseCase
import com.london.presentation.shared.base.BaseViewModel
import com.london.presentation.shared.base.ErrorState
import com.london.presentation.shared.base.createPagingSourceFlow
import com.london.presentation.shared.genre.TvShowGenreUi
import com.london.presentation.shared.genre.toUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

@HiltViewModel
class TrendingTvShowsViewModel @Inject constructor(
    private val getTvShowUseCase: GetTvShowUseCase,
) :
    BaseViewModel<TrendingTvShowsUiState, TrendingTvShowsEffect>(TrendingTvShowsUiState()),
    TrendingTvShowsContract {

    init {
        reloadTrendingTvShows()
    }

    override fun onGenreClick(genre: TvShowGenreUi) {
        if (genre == state.value.selectedGenre) return
        updateState { copy(selectedGenre = genre) }
        reloadTrendingTvShows()
    }

    override fun onTvShowClick(id: Int) =
        emitEffect(TrendingTvShowsEffect.NavigateToTvShow(id))

    override fun onBackClick() = emitEffect(TrendingTvShowsEffect.NavigateBack)
    override fun onRetryClick() = reloadTrendingTvShows()
    private fun reloadTrendingTvShows() {
        tryToCollect(
            block = ::createTrendingTvShowsPagingFlow,
            onStart = { handlingLoadingState(true) },
            onError = ::handlingErrorState,
            onNewValue = ::handlingPagingState,
        )
    }

    private fun handlingErrorState(errorState: ErrorState) =
        updateState { copy(errorState = errorState) }

    private fun handlingPagingState(tvShowsPagingData: PagingData<Trending>) {
        updateState {
            copy(
                tvShowsFlow = flowOf(tvShowsPagingData),
                isLoading = false
            )
        }
    }

    private fun createTrendingTvShowsPagingFlow(): Flow<PagingData<Trending>> {
        return createPagingSourceFlow(
            query = "",
            block = { _, pageNumber ->
                getTvShowUseCase.getTrendingTvShows(
                    page = pageNumber,
                    movieGenreId = state.value.selectedGenreId,
                )
            }
        ).cachedIn(viewModelScope)
    }

    private fun handlingLoadingState(isLoading: Boolean) =
        updateState { copy(isLoading = isLoading) }
}