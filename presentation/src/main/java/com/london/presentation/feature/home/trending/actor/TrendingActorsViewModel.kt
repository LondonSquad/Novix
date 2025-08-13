package com.london.presentation.feature.home.trending.actor

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.london.domain.entity.Actor
import com.london.domain.usecase.GetTrendingActorsUseCase
import com.london.presentation.shared.base.BaseViewModel
import com.london.presentation.shared.base.ErrorState
import com.london.presentation.shared.base.createPagingSourceFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class TrendingActorsViewModel @Inject constructor(
    private val getTrendingActors: GetTrendingActorsUseCase,
) : BaseViewModel<TrendingActorsUiState, TrendingActorsEffect>(TrendingActorsUiState()),
    TrendingActorsContract {

    init {
        reloadTrendingActors()
    }

    override fun onActorClick(id: Int) = emitEffect(TrendingActorsEffect.NavigateToActor(id))

    override fun onBack() = emitEffect(TrendingActorsEffect.NavigateBack)

    override fun onRetry() = reloadTrendingActors()

    private fun reloadTrendingActors() {
        tryToExecute(
            block = ::createTrendingActorsPagingFlow,
            onStart = { handlingLoadingState(true) },
            onError = ::handlingErrorState,
            onSuccess = { handlingPagingState(it) },
            onCompleted = { handlingLoadingState(false) },
        )
    }

    fun handlingErrorState(errorState: ErrorState) = updateState { copy(errorState = errorState) }

    fun handlingPagingState(actorsPagingData: Flow<PagingData<Actor>>) {
        return updateState {
            copy(
                actorsFlow = actorsPagingData
            )
        }
    }

    private fun createTrendingActorsPagingFlow() : Flow<PagingData<Actor>> {
        return createPagingSourceFlow(
            query = "",
            block = { _, pageNumber ->
                getTrendingActors.invoke(
                    page = pageNumber
                )
            }
        ).cachedIn(viewModelScope)
    }

    fun handlingLoadingState(isLoading: Boolean) = updateState { copy(isLoading = isLoading) }
}
