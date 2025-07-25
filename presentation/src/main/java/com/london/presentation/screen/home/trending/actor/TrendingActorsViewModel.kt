package com.london.presentation.screen.home.trending.actor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.london.domain.usecase.GetTrendingActorsUseCase
import com.london.presentation.screen.base.createPagingSourceFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class TrendingActorsViewModel(
    private val getTrendingActors: GetTrendingActorsUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(TrendingActorsUiState())
    val state: StateFlow<TrendingActorsUiState> = _state

    private val _effect = MutableStateFlow<TrendingActorsEffect?>(null)
    val effect: StateFlow<TrendingActorsEffect?> = _effect.asStateFlow()

    init {
        fetchTrendingActors()
    }

    private fun fetchTrendingActors() {
        val actorsFlow = createPagingSourceFlow("") { _, pageNumber ->
            getTrendingActors.invoke(pageNumber)
        }.cachedIn(viewModelScope)
        _state.value = _state.value.copy(actorsFlow = actorsFlow, isLoading = false)
    }

    fun onActorClick(actorId: Int) {
        _effect.value = TrendingActorsEffect.NavigateToActor(actorId)
    }

    fun onBackClick() {
        _effect.value = TrendingActorsEffect.NavigateBack
    }

    fun resetEffect() {
        _effect.value = null
    }
} 