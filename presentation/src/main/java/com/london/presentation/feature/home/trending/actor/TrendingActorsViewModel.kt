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
        initializeActors()
    }

    private fun initializeActors() {
        tryToExecute(
            block = {
                val actorsFlow = createPagingSourceFlow(query = "") { _, pageNumber ->
                    val actors = getTrendingActors.invoke(page = pageNumber)
                    actors.copy(items = actors.items)
                }
                actorsFlow
            },
            onStart = {
                updateState { copy(isLoading = true) }
            },
            onSuccess = { actorsFlow ->
                updateState {
                    copy(actorsFlow = actorsFlow)
                }
            },
            onCompleted = { updateState { copy(isLoading = false) } },
        )
    }

    override fun onActorClick(id: Int) = emitEffect(TrendingActorsEffect.NavigateToActor(id))
    override fun onBack() = emitEffect(TrendingActorsEffect.NavigateBack)

    override fun onRetry() {
        initializeActors()
    }
}
