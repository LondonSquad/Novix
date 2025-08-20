package com.london.presentation.feature.details.actor.info.topmoviespicks

import androidx.lifecycle.SavedStateHandle
import com.london.domain.usecase.details.actor.GetActorUseCase
import com.london.presentation.navigation.Screen
import com.london.presentation.navigation.getArgs
import com.london.presentation.shared.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TopMoviesPicksViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getActorUseCase: GetActorUseCase
) : BaseViewModel<TopMoviesPicksUiState, TopMoviesPicksEffect>(TopMoviesPicksUiState()),
    TopMoviesPicksContract {

    private val args = savedStateHandle.getArgs<Screen.ActorTopMoviesPicksDetails>()
    private val actorId = args?.actorId ?: 0

    init {
        getActorMoviePicksData()
    }


    override fun onRetryClick() {
        updateState { copy(errorState = null) }
        getActorMoviePicksData()
    }

    override fun onMovieClick(movieId: Int) {
        emitEffect(TopMoviesPicksEffect.MovieDetailsNavigation(movieId))
    }

    override fun onBackClick() {
        emitEffect(TopMoviesPicksEffect.BackNavigation)
    }

    override fun onManageBookmarkClicked(movieId: Int) {
        updateState {
            copy(
                isBookmarkSheetVisible = true,
                bookmarkedMovieId = movieId
            )
        }
    }

    override fun onBookmarkSheetDismiss() {
        updateState {
            copy(
                isBookmarkSheetVisible = false,
                bookmarkedMovieId = 0
            )
        }
    }

    private fun getActorMoviePicksData() {
        tryToExecute(
            block = { getActorUseCase.getActorMoviePicksById(actorId) },
            onStart = { updateState { copy(isLoading = true) } },
            onSuccess = { actorMovieDetails -> updateState { copy(movieDetails = actorMovieDetails) } },
            onError = { errorState -> updateState { copy(errorState = errorState) } },
            onCompleted = { updateState { copy(isLoading = false) } },
        )
    }

}
