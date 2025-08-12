package com.london.presentation.feature.details.actor.info.toptvshowspicks

import androidx.lifecycle.SavedStateHandle
import com.london.domain.usecase.toppicks.GetActorTvShowPicksByIdUseCase
import com.london.presentation.navigation.Screen
import com.london.presentation.navigation.getArgs
import com.london.presentation.shared.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TopTvShowsPicksViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getActorTvShowPicksById: GetActorTvShowPicksByIdUseCase,
) : BaseViewModel<TopTvShowsPicksUiState, TopTvShowsPicksEffect>(
    TopTvShowsPicksUiState()
), TopTvShowsPicksContract {

    private val args = savedStateHandle.getArgs<Screen.TopTvShowsPicksDetails>()
    private val actorId = args?.actorId ?: 0

    init {
        getActorTvShowsPicksData()
    }

    private fun getActorTvShowsPicksData() {
        tryToExecute(
            block = { getActorTvShowPicksById.invoke(actorId) },
            onStart = { updateState { copy(isLoading = true) } },
            onSuccess = { actorTvShowDetails -> updateState { copy(actorTvShowDetails = actorTvShowDetails) } },
            onError = { errorState -> updateState { copy(errorState = errorState) } },
            onCompleted = { updateState { copy(isLoading = false) } },
            checkSuccess = { actorId != 0 }
        )
    }

    override fun onRetry(){
        updateState { copy(errorState = null) }
        getActorTvShowsPicksData()
    }

    override fun onSaveClick(tvShowId: Int) {
        updateState { copy(isSaved = !this.isSaved) }
    }

    override fun onBackClick() {
        emitEffect(TopTvShowsPicksEffect.NavigateBack)
    }

    override fun onTvShowClick(tvShowId: Int) {
        emitEffect(TopTvShowsPicksEffect.NavigateToTvShowDetails(tvShowId))
    }
}
