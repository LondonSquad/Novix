package com.london.presentation.feature.home.trending.tvshow

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.london.domain.entity.shared.Trending
import com.london.domain.usecase.details.tvshow.GetTvShowUseCase
import com.london.presentation.shared.base.BaseViewModel
import com.london.presentation.shared.base.ErrorState
import com.london.presentation.shared.base.createPagingSourceFlow
import com.london.presentation.shared.genre.TvShowGenreUi
import com.london.presentation.shared.genre.toDomain
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
        emitEffect(TrendingTvShowsEffect.TvShowDetailsNavigation(id))

    override fun onBackClick() = emitEffect(TrendingTvShowsEffect.BackNavigation)
    override fun onRetryClick() = reloadTrendingTvShows()
    private fun reloadTrendingTvShows() {
        tryToCollect(
            block = ::createTrendingTvShowsPagingFlow,
            onStart = { updateState { copy(isLoading = true) } },
            onError = ::setErrorState,
            onNewValue = ::setPagingState,
        )
    }

    private fun setErrorState(errorState: ErrorState) =
        updateState { copy(errorState = errorState) }

    private fun setPagingState(tvShowsPagingData: PagingData<Trending>) {
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
                    tvShowGenre = state.value.selectedGenre.toDomain(),
                )
            }
        ).cachedIn(viewModelScope)
    }
}
