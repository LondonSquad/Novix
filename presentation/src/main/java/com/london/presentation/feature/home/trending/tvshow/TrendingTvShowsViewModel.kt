package com.london.presentation.feature.home.trending.tvshow

import com.london.domain.entity.genre.TvShowGenre
import com.london.domain.usecase.details.tvshow.GetTvShowUseCase
import com.london.presentation.shared.base.BaseViewModel
import com.london.presentation.shared.base.createPagingSourceFlow
import com.london.presentation.shared.genre.TvShowGenreUi
import com.london.presentation.shared.genre.toUi
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TrendingTvShowsViewModel @Inject constructor(
    private val getTvShowUseCase: GetTvShowUseCase,
) :
    BaseViewModel<TrendingTvShowsUiState, TrendingTvShowsEffect>(TrendingTvShowsUiState()),
    TrendingTvShowsContract {

    init {
        initializeTvShows()
    }

    override fun onGenreSelected(genre: TvShowGenreUi) {
        if (genre == state.value.selectedGenre) return
        updateState { copy(selectedGenre = genre) }
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
                    val tvShows = getTvShowUseCase.getTrendingTvShows(page = pageNumber)
                    val filteredItems =
                        if (state.value.selectedGenre != null && state.value.selectedGenre != TvShowGenreUi.All) {
                            tvShows.items.filter { movie ->
                                movie.genres.map {
                                    (it as TvShowGenre).toUi()
                                }.contains(state.value.selectedGenre)
                            }
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
