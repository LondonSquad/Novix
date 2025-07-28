package com.london.presentation.feature.home.trending.tvshows

import com.london.domain.usecase.GetTrendingTvShowsUseCase
import com.london.presentation.feature.base.BaseViewModel
import com.london.presentation.feature.base.createPagingSourceFlow
import com.london.presentation.utils.TvShowGenre
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class TrendingTvShowsViewModel(private val getTrendingTvShows: GetTrendingTvShowsUseCase) :
    BaseViewModel<TrendingTvShowsUiState, TrendingTvShowsEffect>(TrendingTvShowsUiState()),
    TrendingTvShowsContract {

    init {
        initializeTvShows()
    }

    private fun initializeTvShows() {
        tryToExecute(
            block = {
                val tvShowsFlow = createPagingSourceFlow(query = "") { _, pageNumber ->
                    val movies = getTrendingTvShows.invoke(page = pageNumber)
                    movies.copy(items = movies.items)
                }
                tvShowsFlow
            },
            onStart = {
                updateState { copy(isLoading = true) }
            },
            onSuccess = { moviesFlow ->
                updateState {
                    copy(tvShowsFlow = moviesFlow)
                }
            },
            onCompleted = { updateState { copy(isLoading = false) } },
        )
    }

    override fun onGenreSelected(genre: TvShowGenre) {
        if (genre.id == state.value.selectedGenreId) return
        updateState { copy(selectedGenreId = genre.id) }
        initializeTvShows()
    }

    override fun onTvShowClick(id: Int) = emitEffect(TrendingTvShowsEffect.NavigateToTvShow(id))

    override fun onBack() = emitEffect(TrendingTvShowsEffect.NavigateBack)

}
