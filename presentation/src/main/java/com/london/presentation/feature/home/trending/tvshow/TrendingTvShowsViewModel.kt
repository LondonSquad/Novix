package com.london.presentation.feature.home.trending.tvshow

import com.london.domain.usecase.details.tvshow.ManageTvShowDetailsUseCase
import com.london.presentation.shared.base.BaseViewModel
import com.london.presentation.shared.base.createPagingSourceFlow
import com.london.presentation.utils.TvShowGenre
import dagger.hilt.android.lifecycle.HiltViewModel
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
        initializeTvShows()
    }

    override fun onTvShowClick(id: Int) =
        emitEffect(TrendingTvShowsEffect.NavigateToTvShow(id))

    override fun onBack() = emitEffect(TrendingTvShowsEffect.NavigateBack)

    override fun onRetry() = initializeTvShows()


    private fun initializeTvShows() {
        tryToExecute(
            block = {
                val tvShowsFlow = createPagingSourceFlow(query = "") { _, pageNumber ->
                    manageTvShowDetailsUseCase.getTrendingTvShows(
                        page = pageNumber,
                        genreId = state.value.selectedGenreId
                    )
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
