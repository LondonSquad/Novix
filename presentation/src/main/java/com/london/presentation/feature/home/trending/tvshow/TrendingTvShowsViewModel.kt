package com.london.presentation.feature.home.trending.tvshow

import com.london.domain.usecase.details.tvshow.ManageTvShowDetailsUseCase
import com.london.presentation.feature.home.shared.handlingPagingFlow
import com.london.presentation.shared.base.BaseViewModel
import com.london.presentation.utils.TvShowGenre
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

@HiltViewModel
class TrendingTvShowsViewModel @Inject constructor(
    private val manageTvShowDetailsUseCase: ManageTvShowDetailsUseCase,
) :
    BaseViewModel<TrendingTvShowsUiState, TrendingTvShowsEffect>(TrendingTvShowsUiState()),
    TrendingTvShowsContract {

    init {
        initializeTvShows()
    }

    override fun onGenreSelected(genre: TvShowGenre) {
        if (genre.id == state.value.selectedGenreId) return
        updateState { copy(selectedGenreId = genre.id) }
        onRefresh()
    }

    override fun onTvShowClick(id: Int) =
        emitEffect(TrendingTvShowsEffect.NavigateToTvShow(id))

    override fun onBack() = emitEffect(TrendingTvShowsEffect.NavigateBack)
    override fun onRetry() = initializeTvShows()
    private fun onRefresh() {
        tryToCollect(
            block = {
                handlingPagingFlow { pageNumber ->
                    manageTvShowDetailsUseCase.getTrendingTvShows(
                        page = pageNumber,
                        movieGenreId = state.value.selectedGenreId
                    )
                }
            },
            onNewValue = { tvShowsFlow ->
                updateState {
                    copy(tvShowsFlow = flowOf(tvShowsFlow))
                }
            },
        )
    }

    private fun initializeTvShows() {
        tryToCollect(
            block = {
                handlingPagingFlow { pageNumber ->
                    manageTvShowDetailsUseCase.getTrendingTvShows(
                        page = pageNumber,
                        movieGenreId = state.value.selectedGenreId
                    )
                }
            },
            onStart = {
                updateState { copy(isLoading = true) }
            },
            onNewValue = { tvShowsFlow ->
                updateState {
                    copy(tvShowsFlow = flowOf(tvShowsFlow))
                }
            },
            onCompleted = { updateState { copy(isLoading = false) } },
        )
    }
}
