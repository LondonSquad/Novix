package com.london.presentation.feature.home.trending.tvshow

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.london.domain.entity.Trending
import com.london.domain.usecase.details.tvshow.ManageTvShowDetailsUseCase
import com.london.presentation.shared.base.BaseViewModel
import com.london.presentation.shared.base.ErrorState
import com.london.presentation.shared.base.createPagingSourceFlow
import com.london.presentation.utils.TvShowGenre
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

@HiltViewModel
class TrendingTvShowsViewModel @Inject constructor(
    private val manageTvShowDetailsUseCase: ManageTvShowDetailsUseCase,
) :
    BaseViewModel<TrendingTvShowsUiState, TrendingTvShowsEffect>(TrendingTvShowsUiState()),
    TrendingTvShowsContract {

    init {
        reloadTrendingTvShows()
    }

    override fun onGenreSelected(genre: TvShowGenre) {
        if (genre.id == state.value.selectedGenreId) return
        updateState { copy(selectedGenreId = genre.id) }
        reloadTrendingTvShows()
    }

    override fun onTvShowClick(id: Int) =
        emitEffect(TrendingTvShowsEffect.NavigateToTvShow(id))

    override fun onBack() = emitEffect(TrendingTvShowsEffect.NavigateBack)
    override fun onRetry() = reloadTrendingTvShows()
    private fun reloadTrendingTvShows() {
        tryToCollect(
            block = ::createTrendingTvShowsPagingFlow,
            onStart = {handlingLoadingState(true)},
            onError = ::handlingErrorState,
            onNewValue = ::handlingPagingState,
            onCompleted = { handlingLoadingState(false) },
        )
    }

    fun handlingErrorState(errorState: ErrorState) = updateState { copy(errorState = errorState) }
    fun handlingPagingState(tvShowsPagingData: PagingData<Trending>) {
        updateState { copy(tvShowsFlow = flowOf(tvShowsPagingData)) }
    }
    private fun createTrendingTvShowsPagingFlow(): Flow<PagingData<Trending>> {
        return createPagingSourceFlow(
            query = "",
            block = { _, pageNumber ->
                manageTvShowDetailsUseCase.getTrendingTvShows(
                    page = pageNumber,
                    movieGenreId = state.value.selectedGenreId,
                )
            }
        ).cachedIn(viewModelScope)
    }

    fun handlingLoadingState(isLoading: Boolean) = updateState { copy(isLoading = isLoading) }
}