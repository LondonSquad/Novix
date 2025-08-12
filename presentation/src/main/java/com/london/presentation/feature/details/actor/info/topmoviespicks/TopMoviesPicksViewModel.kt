package com.london.presentation.feature.details.actor.info.topmoviespicks

import androidx.lifecycle.SavedStateHandle
import com.london.domain.usecase.toppicks.GetActorMoviePicksByIdUseCase
import com.london.presentation.navigation.Screen
import com.london.presentation.navigation.getArgs
import com.london.presentation.shared.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TopMoviesPicksViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getActorMoviePicksById: GetActorMoviePicksByIdUseCase,
) : BaseViewModel<TopMoviesPicksUiState, TopMoviesPicksEffect>(TopMoviesPicksUiState()),
    TopMoviesPicksContract {

    private val args = savedStateHandle.getArgs<Screen.ActorTopMoviesPicksDetails>()
    private val actorId = args?.actorId ?: 0

    init {
        if (actorId != 0) {
            getActorMoviePicksData()
        }
    }

    private fun getActorMoviePicksData() {
        tryToExecute(
            block = { getActorMoviePicksById.invoke(actorId) },
            onStart = { updateState { copy(isLoading = true) } },
            onSuccess = { actorMovieDetails -> updateState { copy(actorMovieDetails = actorMovieDetails) } },
            onError = { errorState -> updateState { copy(errorState = errorState) } },
            onCompleted = { updateState { copy(isLoading = false) } },
            checkSuccess = { actorId != 0 },
        )
    }

    override fun onRetry() {
        updateState { copy(errorState = null) }
        getActorMoviePicksData()
    }

    override fun onSaveClick(movieId: Int) {
        updateState { copy(isSaved = !this.isSaved) }
    }

    override fun onMovieClick(movieId: Int) {
        emitEffect(TopMoviesPicksEffect.NavigateToMovieDetails(movieId))
    }

    override fun onBackClick() {
        emitEffect(TopMoviesPicksEffect.NavigateBack)
    }
}
