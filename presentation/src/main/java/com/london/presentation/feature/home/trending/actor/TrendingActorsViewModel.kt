package com.london.presentation.feature.home.trending.actor

import com.london.domain.usecase.GetTrendingActorsUseCase
import com.london.presentation.shared.base.BaseViewModel
import com.london.presentation.shared.base.createPagingSourceFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

@HiltViewModel
class TrendingActorsViewModel @Inject constructor(
    private val getTrendingActors: GetTrendingActorsUseCase,
) : BaseViewModel<TrendingActorsUiState, TrendingActorsEffect>(TrendingActorsUiState()),
    TrendingActorsContract {

    init {
        initializeActors()
    }

    override fun onActorClick(id: Int) = emitEffect(TrendingActorsEffect.NavigateToActor(id))

    override fun onBack() = emitEffect(TrendingActorsEffect.NavigateBack)

    override fun onRetry() = initializeActors()

    private fun initializeActors() {
        tryToCollect(
            block = { createTrendingActorsPagingFlow() },
            onStart = { handlingLoadingState(true) },
            onNewValue = { actorsPagingData ->
                updateState { copy(actorsFlow = flowOf(actorsPagingData)) }
            },
            onCompleted = { handlingLoadingState(false) },
        )
    }

    private fun createTrendingActorsPagingFlow() = createPagingSourceFlow(
        query = "",
        block = { _, _ ->
            getTrendingActors.invoke(
                page = 1,
            )
        }
    )
    fun handlingLoadingState(isLoading: Boolean) = updateState { copy(isLoading = isLoading) }
}