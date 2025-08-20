package com.london.presentation.feature.details.actor

import androidx.lifecycle.SavedStateHandle
import com.london.domain.usecase.details.actor.GetActorUseCase
import com.london.presentation.navigation.Screen
import com.london.presentation.navigation.getArgs
import com.london.presentation.shared.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ActorDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getActorUseCase: GetActorUseCase
) : BaseViewModel<ActorDetailsUiState, ActorEffect>(ActorDetailsUiState()),
    ActorDetailsContract {

    private val args = savedStateHandle.getArgs<Screen.ActorDetails>()
    private val actorId = args?.actorId

    init {
        getActorInformation()
    }

    override fun onRetryClick() {
        updateState { copy(error = null, movieError = false, tvShowError = false) }
        getActorInformation()
    }

    override fun onBackClick() {
        emitEffect(ActorEffect.BackNavigation)
    }

    override fun onActorGalleryClick(actorId: Int) {
        emitEffect(ActorEffect.GalleryNavigation(actorId))
    }

    override fun onTopMoviePicksClick(actorId: Int) {
        emitEffect(ActorEffect.TopMoviePicksNavigation(actorId))
    }

    override fun onMovieScreenClick(movieId: Int) {
        emitEffect(ActorEffect.MovieScreenNavigation(movieId))
    }

    override fun onTopTvShowPicksClick(actorId: Int) {
        emitEffect(ActorEffect.TopTvShowPicksNavigation(actorId))
    }

    override fun onTvShowScreenClick(tvShowId: Int) {
        emitEffect(ActorEffect.TvShowScreenNavigation(tvShowId))
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

    private fun getActorInformation() {
        getActorImage()
        getActorDetails()
        getActorMovieDetails()
        getActorTvShowDetails()
    }

    private fun getActorImage() {
        tryToExecute(
            block = {
                getActorUseCase.getActorImagesById(actorId ?: 0)
            },
            onStart = { updateState { copy(isLoading = true) } },
            onSuccess = { images -> updateState { copy(actorImageDetails = images) } },
            onError = { errorState -> updateState { copy(error = errorState) } },
            onCompleted = { updateState { copy(isLoading = false) } },
        )
    }

    private fun getActorDetails() {
        tryToExecute(
            block = {
                getActorUseCase.getActorDetailsById(actorId ?: 0)
            },
            onStart = { updateState { copy(isLoading = true) } },
            onSuccess = { actorDetails -> updateState { copy(actorDetails = actorDetails) } },
            onError = { errorState -> updateState { copy(error = errorState) } },
            onCompleted = { updateState { copy(isLoading = false) } },
        )
    }

    private fun getActorMovieDetails() {
        tryToExecute(
            block = {
                getActorUseCase.getActorMoviePicksById(actorId ?: 0)
            },
            onStart = { updateState { copy(isLoading = true) } },
            onSuccess = { movieDetails -> updateState { copy(actorMovieDetails = movieDetails) } },
            onError = { errorState -> updateState { copy(error = errorState) } },
            onCompleted = { updateState { copy(isLoading = false) } },
        )
    }

    private fun getActorTvShowDetails() {
        tryToExecute(
            block = {
                getActorUseCase.getActorTvShowPicksById(actorId ?: 0)
            },
            onStart = { updateState { copy(isLoading = true) } },
            onSuccess = { tvShows -> updateState { copy(actorTvShowDetails = tvShows) } },
            onError = { errorState -> updateState { copy(error = errorState) } },
            onCompleted = { updateState { copy(isLoading = false) } },
        )
    }

}
