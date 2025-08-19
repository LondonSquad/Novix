package com.london.presentation.feature.details.actor.info.toptvshowspicks

import androidx.lifecycle.SavedStateHandle
import com.london.domain.usecase.details.actor.GetActorUseCase
import com.london.presentation.navigation.Screen
import com.london.presentation.navigation.getArgs
import com.london.presentation.shared.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TopTvShowsPicksViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getActorUseCase: GetActorUseCase,
) : BaseViewModel<TopTvShowsPicksUiState, TopTvShowsPicksEffect>(
    TopTvShowsPicksUiState()
), TopTvShowsPicksContract {

    private val args = savedStateHandle.getArgs<Screen.TopTvShowsPicksDetails>()
    private val actorId = args?.actorId ?: 0

    init {
        getActorTvShowsPicksData()
    }

    override fun onRetryClick() {
        updateState { copy(errorState = null) }
        getActorTvShowsPicksData()
    }

    override fun onBackClick() {
        emitEffect(TopTvShowsPicksEffect.BackNavigation)
    }

    override fun onTvShowClick(tvShowId: Int) {
        emitEffect(TopTvShowsPicksEffect.TvShowDetailsNavigation(tvShowId))
    }

    private fun getActorTvShowsPicksData() {
        tryToExecute(
            block = { getActorUseCase.getActorTvShowPicksById(actorId) },
            onStart = { updateState { copy(isLoading = true) } },
            onSuccess = { actorTvShowDetails -> updateState { copy(tvShowDetails = actorTvShowDetails) } },
            onError = { errorState -> updateState { copy(errorState = errorState) } },
            onCompleted = { updateState { copy(isLoading = false) } },
        )
    }

}
