package com.london.presentation.feature.home.trending.tvshows

import com.london.domain.usecase.GetTrendingTvShowsUseCase
import com.london.presentation.feature.base.BaseViewModel
import com.london.presentation.feature.base.createPagingSourceFlow
import com.london.presentation.utils.TvShowGenre
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TrendingTvShowsViewModel @Inject constructor(private val getTrendingTvShows: GetTrendingTvShowsUseCase) :
    BaseViewModel<TrendingTvShowsUiState, TrendingTvShowsEffect>(TrendingTvShowsUiState()),
    TrendingTvShowsContract {

    init {
        initializeTvShows()
    }

    override fun onGenreSelected(genre: TvShowGenre) {
        if (genre.id == state.value.selectedGenreId) return
        updateState { copy(selectedGenreId = genre.id) }
        initializeTvShows()
    }

    override fun onTvShowClick(id: Int) = emitEffect(TrendingTvShowsEffect.NavigateToTvShow(id))

    override fun onBack() = emitEffect(TrendingTvShowsEffect.NavigateBack)

    override fun onRetry() = initializeTvShows()

    private fun initializeTvShows() {
        tryToExecute(
            block = {
                val tvShowsFlow = createPagingSourceFlow(query = "") { _, pageNumber ->
                    val tvShows = getTrendingTvShows.invoke(page = pageNumber)
                    val filteredItems =
                        if (state.value.selectedGenreId != null && state.value.selectedGenreId != -1) {
                            tvShows.items.filter { it.genreIds.contains(state.value.selectedGenreId) }
                        } else {
                            tvShows.items
                        }
                    tvShows.copy(items = filteredItems)
                }
                tvShowsFlow
            },
            onStart = {
                updateState { copy(isLoading = true) }
            },
            onSuccess = { tvShowsFlow ->
                updateState {
                    copy(tvShowsFlow = tvShowsFlow)
                }
            },
            onCompleted = { updateState { copy(isLoading = false) } },
        )
    }
}
