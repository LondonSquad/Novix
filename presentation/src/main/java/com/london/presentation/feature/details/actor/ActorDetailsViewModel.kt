package com.london.presentation.feature.details.actor

import androidx.lifecycle.SavedStateHandle
import com.london.domain.usecase.GetActorDetailsByIdUseCase
import com.london.domain.usecase.GetActorImagesByIdUseCase
import com.london.domain.usecase.toppicks.GetActorMoviePicksByIdUseCase
import com.london.domain.usecase.toppicks.GetActorTvShowPicksByIdUseCase
import com.london.presentation.navigation.Screen
import com.london.presentation.navigation.getArgs
import com.london.presentation.shared.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ActorDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getActorDetailsById: GetActorDetailsByIdUseCase,
    private val getActorImagesById: GetActorImagesByIdUseCase,
    private val getActorMoviePicksById: GetActorMoviePicksByIdUseCase,
    private val getActorTvShowPicksById: GetActorTvShowPicksByIdUseCase,
) : BaseViewModel<ActorDetailsUiState, ActorEffect>(ActorDetailsUiState()),
    ActorDetailsContract {

    private val args = savedStateHandle.getArgs<Screen.ActorDetails>()
    private val actorId = args?.actorId

    init {
        getActorImage()
        getActorDetails()
        getActorMovieDetails()
        getActorTvShowDetails()
    }

    override fun onRetryClick() {
        updateState { copy(error = null, movieError = false, tvShowError = false) }
        getActorImage()
        getActorDetails()
        getActorMovieDetails()
        getActorTvShowDetails()
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

    private fun getActorImage() {
        tryToExecute(
            block = { getActorImagesById.invoke(actorId ?: 0) },
            onStart = { updateState { copy(isLoading = true) } },
            onSuccess = { images -> updateState { copy(actorImageDetails = images) } },
            onError = { errorState -> updateState { copy(error = errorState) } },
            onCompleted = { updateState { copy(isLoading = false) } },
        )
    }

    private fun getActorDetails() {
        tryToExecute(
            block = { getActorDetailsById.invoke(actorId ?: 0) },
            onStart = { updateState { copy(isLoading = true) } },
            onSuccess = { actorDetails -> updateState { copy(actorDetails = actorDetails) } },
            onError = { errorState -> updateState { copy(error = errorState) } },
            onCompleted = { updateState { copy(isLoading = false) } },
        )
    }

    private fun getActorMovieDetails() {
        tryToExecute(
            block = { getActorMoviePicksById.invoke(actorId ?: 0) },
            onStart = { updateState { copy(isLoading = true) } },
            onSuccess = { movieDetails -> updateState { copy(actorMovieDetails = movieDetails,) } },
            onError = { errorState -> updateState { copy(error = errorState) } },
            onCompleted = { updateState { copy(isLoading = false) } },
        )
    }

    private fun getActorTvShowDetails() {
        tryToExecute(
            block = { getActorTvShowPicksById.invoke(actorId ?: 0) },
            onStart = { updateState { copy(isLoading = true) } },
            onSuccess = { tvShows -> updateState { copy( actorTvShowDetails = tvShows ) } },
            onError = { errorState -> updateState { copy(error = errorState) } },
            onCompleted = { updateState { copy(isLoading = false) } },
        )
    }
}
