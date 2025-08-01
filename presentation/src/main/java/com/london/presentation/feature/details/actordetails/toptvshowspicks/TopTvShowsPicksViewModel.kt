package com.london.presentation.feature.details.actordetails.toptvshowspicks

import androidx.lifecycle.SavedStateHandle
import com.london.domain.usecase.GetActorTvShowPicksByIdUseCase
import com.london.presentation.feature.base.BaseViewModel
import com.london.presentation.navigation.Screen
import com.london.presentation.navigation.getArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TopTvShowsPicksViewModel @Inject constructor(
    private val getActorTvShowPicksById: GetActorTvShowPicksByIdUseCase,
    savedStateHandle: SavedStateHandle,
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
            block = {
                getActorTvShowPicksById.invoke(actorId)
            },
            onStart = { updateState { copy(isLoading = true) } },
            onSuccess = { tvShowDetails ->
                updateState {
                    copy(tvShowDetails = tvShowDetails)
                }
            },
            onError = { errorState ->
                updateState {
                    copy(errorState = errorState)
                }
            },
            onCompleted = { updateState { copy(isLoading = false) } },
            checkSuccess = { actorId != 0 }
        )
    }

    override fun onRetry(){
        updateState { copy(errorState = null) }
        getActorTvShowsPicksData()
    }

    override fun onSaveMovie(movieId: Int) {
        updateState { copy(isSaved = !this.isSaved) }
    }

    override fun onBack() {
        emitEffect(TopTvShowsPicksEffect.BackNavigation)
    }

    override fun onTvShowClicked(tvShowId: Int) {
        emitEffect(TopTvShowsPicksEffect.TvShowNavigation(tvShowId))
    }
}