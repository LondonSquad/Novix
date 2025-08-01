package com.london.presentation.feature.details.actordetails.topmoviespicks

import androidx.lifecycle.SavedStateHandle
import com.london.domain.usecase.GetActorMoviePicksByIdUseCase
import com.london.presentation.feature.base.BaseViewModel
import com.london.presentation.navigation.Screen
import com.london.presentation.navigation.getArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TopMoviesPicksViewModel @Inject constructor(
    private val getActorMoviePicksById: GetActorMoviePicksByIdUseCase,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<TopMoviesPicksUiState, TopMoviesPicksEffect>(TopMoviesPicksUiState()),
    TopMoviesPicksContract {

    private val args = savedStateHandle.getArgs<Screen.ActorTopMoviesPicksDetails>()
    val actorId = args?.actorId ?: 0

    init {
        if (actorId != 0) {
            getActorMoviePicksData()
        }
    }

    override fun onSaveMovie(movieId: Int) {
        updateState { copy(isSaved = isSaved) }
        emitEffect(TopMoviesPicksEffect.NavigationToMovieDetails(movieId))
    }

    override fun onBack() {
        emitEffect(TopMoviesPicksEffect.NavigateBack)
    }

    private fun getActorMoviePicksData() {

        tryToExecute(
            block = { getActorMoviePicksById.invoke(actorId) },
            onStart = { updateState { copy(isLoading = true) } },
            onSuccess = { actorMovieDetails ->
                updateState {
                    copy(
                        id = actorMovieDetails.id,
                        movieDetails = actorMovieDetails,
                        isSaved = isSaved,
                        backdropPath = backdropPath,
                    )
                }
            },
            onError = { errorState ->
                updateState { copy(errorState = errorState) }
            },
            onCompleted = { updateState { copy(isLoading = false) } },
            checkSuccess = { actorId != 0 },
        )
    }
}
