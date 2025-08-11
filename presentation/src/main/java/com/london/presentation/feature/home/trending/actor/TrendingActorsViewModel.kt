package com.london.presentation.feature.home.trending.actor

import com.london.domain.usecase.details.actor.GetActorUseCase
import com.london.presentation.shared.base.BaseViewModel
import com.london.presentation.shared.base.createPagingSourceFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TrendingActorsViewModel @Inject constructor(
    private val getActorUseCase: GetActorUseCase,
) : BaseViewModel<TrendingActorsUiState, TrendingActorsEffect>(TrendingActorsUiState()),
    TrendingActorsContract {

    init {
        initializeActors()
    }

    override fun onActorClick(id: Int) = emitEffect(TrendingActorsEffect.NavigateToActor(id))

    override fun onBack() = emitEffect(TrendingActorsEffect.NavigateBack)

    override fun onRetry() = initializeActors()

    private fun initializeActors() {
        tryToExecute(
            block = {
                val actorsFlow = createPagingSourceFlow(query = "") { _, pageNumber ->
                    val actors = getActorUseCase.getTrendingActors(page = pageNumber)
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

}
