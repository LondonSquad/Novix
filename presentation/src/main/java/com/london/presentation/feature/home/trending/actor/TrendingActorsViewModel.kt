package com.london.presentation.feature.home.trending.actor

import com.london.domain.usecase.GetTrendingActorsUseCase
import com.london.presentation.feature.base.BaseViewModel
import com.london.presentation.feature.base.createPagingSourceFlow
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class TrendingActorsViewModel(
    private val getTrendingActors: GetTrendingActorsUseCase,
) : BaseViewModel<TrendingActorsUiState, TrendingActorsEffect>(TrendingActorsUiState()),
    TrendingActorsContract {

    init {
        initializeTvShows()
    }

    private fun initializeTvShows() {
        tryToExecute(
            block = {
                val actorsFlow = createPagingSourceFlow(query = "") { _, pageNumber ->
                    val movies = getTrendingActors.invoke(page = pageNumber)
                    movies.copy(items = movies.items)
                }
                actorsFlow
            },
            onStart = {
                updateState { copy(isLoading = true) }
            },
            onSuccess = { moviesFlow ->
                updateState {
                    copy(actorsFlow = moviesFlow)
                }
            },
            onCompleted = { updateState { copy(isLoading = false) } },
        )
    }

    override fun onActorClick(id: Int) = emitEffect(TrendingActorsEffect.NavigateToActor(id))
    override fun onBack() = emitEffect(TrendingActorsEffect.NavigateBack)
}
