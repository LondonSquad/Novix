package com.london.presentation.screen.details.actor

import androidx.lifecycle.SavedStateHandle
import com.london.domain.usecase.GetActorDetailsByIdUseCase
import com.london.domain.usecase.GetActorImagesByIdUseCase
import com.london.domain.usecase.GetActorMoviePicksByIdUseCase
import com.london.domain.usecase.GetActorTvShowPicksByIdUseCase
import com.london.presentation.features.base.BaseViewModel
import com.london.presentation.navigation.Screen
import com.london.presentation.navigation.getArgs
import com.london.presentation.screen.base.ErrorState
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class ActorDetailsViewModel(
    private val getActorDetailsByIdUseCase: GetActorDetailsByIdUseCase,
    private val getActorImagesByIdUseCase: GetActorImagesByIdUseCase,
    private val getActorMoviePicksByIdUseCase: GetActorMoviePicksByIdUseCase,
    private val getActorTvShowPicksByIdUseCase: GetActorTvShowPicksByIdUseCase,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<ActorDetailsUiState, ActorEffectUiState>(ActorDetailsUiState()),
    ActorDetailsContract {

    private val args = savedStateHandle.getArgs<Screen.ActorDetails>()
    private val actorId = args?.actorId

    init {
        getActorImage()
        getActorDetails()
        getActorMovieDetails()
        getActorTvShowDetails()
    }

    private fun getActorImage() {
        tryToExecute(
            block = {
                getActorImagesByIdUseCase.invoke(actorId ?: 0)
            },
            onStart = { updateState { copy(isLoading = true) } },
            onSuccess = { images -> updateState { copy(actorImageDetails = images) } },
            onError = { error ->
                when (error) {
                    ErrorState.NoInternet -> updateState { copy(error = error) }
                    is ErrorState.RequestFailed -> updateState { copy(error = error) }
                }
            },
            onCompleted = { updateState { copy(isLoading = false) } },
        )
    }

    private fun getActorDetails() {

        tryToExecute(
            block = {
                getActorDetailsByIdUseCase.invoke(actorId ?: 0)
            },
            onStart = { updateState { copy(isLoading = true) } },
            onSuccess = { actorDetails ->
                updateState {
                    copy(
                        actorName = actorDetails.name,
                        actorBirthday = actorDetails.birthday,
                        actorDeathDay = actorDetails.deathDay,
                        actorPlaceOfBirth = actorDetails.placeOfBirth,
                        actorBiography = actorDetails.biography,
                        knownForDepartment = actorDetails.knownForDepartment,
                        actorId = actorDetails.id
                    )
                }
            },
            onError = { error ->
                when (error) {
                    ErrorState.NoInternet -> updateState { copy(error = error) }
                    is ErrorState.RequestFailed -> updateState { copy(error = error) }
                }
            },
            onCompleted = { updateState { copy(isLoading = false) } },
        )
    }

    private fun getActorMovieDetails() {
        tryToExecute(
            block = {
                getActorMoviePicksByIdUseCase.invoke(actorId ?: 0)
            },
            onStart = { updateState { copy(isLoading = true) } },
            onSuccess = { movieDetails ->
                updateState {
                    copy(
                        actorMovieDetails = movieDetails,
                        movieId = movieDetails.id
                    )
                }
            },
            onError = { error ->
                when (error) {
                    ErrorState.NoInternet -> updateState { copy(error = error) }
                    is ErrorState.RequestFailed -> updateState { copy(error = error) }
                }
            },
            onCompleted = { updateState { copy(isLoading = false) } },
        )
    }

    private fun getActorTvShowDetails() {
        tryToExecute(
            block = {
                getActorTvShowPicksByIdUseCase.invoke(actorId ?: 0)
            },
            onStart = { updateState { copy(isLoading = true) } },
            onSuccess = { tvShows ->
                updateState {
                    copy(
                        actorTvShowDetails = tvShows,
                        tvShowError = false,
                        tvShowId = tvShows.id
                    )
                }
            },
            onError = { error ->
                when (error) {
                    ErrorState.NoInternet -> updateState { copy(error = error) }
                    is ErrorState.RequestFailed -> updateState { copy(error = error) }
                }
            },
            onCompleted = { updateState { copy(isLoading = false) } },
            checkSuccess = { actorId != null },
        )
    }

    override fun onNavigateBack() {
        emitEffect(ActorEffectUiState.NavigationBack)
    }

    override fun onGalleryClick(actorId: Int) {
        emitEffect(ActorEffectUiState.NavigateToGallery(actorId))
    }

    override fun onTvShowPicksClick(actorId: Int) {
        emitEffect(ActorEffectUiState.NavigateToTvShowPicks(actorId))
    }

    override fun onMoviePicksClick(actorId: Int) {
        emitEffect(ActorEffectUiState.NavigateToMoviePicks(actorId))
    }

    override fun onTvShowScreenClick(tvShowId: Int) {
        emitEffect(ActorEffectUiState.NavigateToTvShowScreen(tvShowId))
    }

    override fun onMovieScreenClick(movieId: Int) {
        emitEffect(ActorEffectUiState.NavigateToMovieScreen(movieId))
    }
}