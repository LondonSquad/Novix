package com.london.presentation.screen.home

import com.london.domain.usecase.GetPopularMovies
import com.london.presentation.screen.base.BaseViewModel
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class HomeViewModel(
    private val getPopularMovies: GetPopularMovies
) : BaseViewModel<HomeScreenUiState, HomeScreenEffect>(HomeScreenUiState()), HomeScreenContract {

    init {
        initializePopularMovies()
    }

    private fun initializePopularMovies() {
        tryToExecute(
            block = { getPopularMovies.invoke() },
            onStart = { updateState { copy(isLoading = true) } },
            onSuccess = { popularMovies ->
                updateState { copy(popularMovies = popularMovies) }
            },
            onError = { errorState -> updateState { copy(error = errorState) } },
            onCompleted = { updateState { copy(isLoading = false) } },
            checkSuccess = { it.isNotEmpty() },
        )
    }

    override fun onPopularCardClicked(id: Int) {
        emitEffect(HomeScreenEffect.NavigationPopularCard(id))
    }
}