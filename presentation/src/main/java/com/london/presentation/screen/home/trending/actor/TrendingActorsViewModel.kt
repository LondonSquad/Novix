package com.london.presentation.screen.home.trending.actor

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.london.domain.usecase.GetTrendingActorsUseCase
import com.london.presentation.screen.base.BaseViewModel
import com.london.presentation.screen.base.createPagingSourceFlow
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class TrendingActorsViewModel(
    private val getTrendingActors: GetTrendingActorsUseCase,
) : BaseViewModel<TrendingActorsUiState, TrendingActorsEffect>(TrendingActorsUiState()), TrendingActorsContract {

    init {
        fetchTrendingActors()
    }

    private fun fetchTrendingActors() {
        val actorsFlow = createPagingSourceFlow("") { _, pageNumber ->
            getTrendingActors.invoke(pageNumber)
        }.cachedIn(viewModelScope)
        updateState { copy(actorsFlow = actorsFlow, isLoading = false) }
    }

    override fun onActorClick(id: Int) = emitEffect(TrendingActorsEffect.NavigateToActor(id))

    override fun onBackClick() = emitEffect(TrendingActorsEffect.NavigateBack)
}
