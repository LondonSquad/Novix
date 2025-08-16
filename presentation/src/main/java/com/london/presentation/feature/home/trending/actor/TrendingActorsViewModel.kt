package com.london.presentation.feature.home.trending.actor

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.london.domain.entity.Actor
import com.london.domain.usecase.details.actor.GetActorUseCase
import com.london.presentation.shared.base.BaseViewModel
import com.london.presentation.shared.base.ErrorState
import com.london.presentation.shared.base.createPagingSourceFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class TrendingActorsViewModel @Inject constructor(
    private val getActorUseCase: GetActorUseCase,
) : BaseViewModel<TrendingActorsUiState, TrendingActorsEffect>(TrendingActorsUiState()),
    TrendingActorsContract {

    init {
        getTrendingActors()
    }

    override fun onActorClick(id: Int) = emitEffect(TrendingActorsEffect.NavigateToActor(id))

    override fun onBackClick() = emitEffect(TrendingActorsEffect.NavigateBack)

    override fun onRetryClick() = getTrendingActors()

    private fun getTrendingActors() {
        tryToExecute(
            block = ::createTrendingActorsPagingFlow,
            onStart = { handlingLoadingState(true) },
            onError = ::handlingErrorState,
            onSuccess = ::handlingPagingState,
            onCompleted = { handlingLoadingState(false) }
        )
    }

    private fun handlingErrorState(errorState: ErrorState) =
        updateState { copy(errorState = errorState) }

    private fun handlingPagingState(actorsPagingData: Flow<PagingData<Actor>>) {
        return updateState { copy(actorsFlow = actorsPagingData) }
    }

    private fun createTrendingActorsPagingFlow(): Flow<PagingData<Actor>> {
        return createPagingSourceFlow(
            query = "",
            block = { _, pageNumber ->
                getActorUseCase.getTrendingActors(
                    page = pageNumber
                )
            }
        ).cachedIn(viewModelScope)
    }

    private fun handlingLoadingState(isLoading: Boolean) =
        updateState { copy(isLoading = isLoading) }
}
