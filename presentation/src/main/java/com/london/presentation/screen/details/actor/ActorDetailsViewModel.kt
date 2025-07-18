package com.london.presentation.screen.details.actor

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.london.domain.GetCastByIdFailedException
import com.london.domain.usecase.GetActorDetailsByIdUseCase
import com.london.domain.usecase.GetActorImagesByIdUseCase
import com.london.domain.usecase.GetActorMoviePicksByIdUseCase
import com.london.domain.usecase.GetActorTvShowPicksByIdUseCase
import com.london.presentation.navigation.arguments.ActorDetailsArgs
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class ActorDetailsViewModel(
    private val getActorDetailsByIdUseCase: GetActorDetailsByIdUseCase,
    private val getActorImagesByIdUseCase: GetActorImagesByIdUseCase,
    private val getActorMoviePicksByIdUseCase: GetActorMoviePicksByIdUseCase,
    private val getActorTvShowPicksByIdUseCase: GetActorTvShowPicksByIdUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ActorDetailsUiState())
    val uiState = _uiState.asStateFlow()

    private val args by lazy { ActorDetailsArgs(savedStateHandle) }
    private val actorId=args.actorId

    init {
        getActorImage()
        getActorDetails()
        getActorMovieDetails()
        getActorTvShowDetails()
    }

    private fun getActorImage() {
        viewModelScope.launch {
            _uiState.update {
                    it.copy(
                        actorImageDetails = getActorImagesByIdUseCase.invoke(actorId)
                    )
            }
        }
    }

    private fun getActorDetails() {
        viewModelScope.launch {
            _uiState.update {
                val actorDetails = getActorDetailsByIdUseCase.invoke(actorId)
                it.copy(
                    actorName = actorDetails.name,
                    actorBirthday = actorDetails.birthday,
                    actorDeathDay = actorDetails.deathDay,
                    actorPlaceOfBirth = actorDetails.placeOfBirth,
                    actorBiography = actorDetails.biography,
                    knownForDepartment = actorDetails.knownForDepartment,
                    actorId = actorDetails.id
                )
            }
        }
    }

    private fun getActorMovieDetails() {
        viewModelScope.launch {
            try {
                _uiState.update {
                    it.copy(
                        actorMovieDetails = getActorMoviePicksByIdUseCase.invoke(actorId)
                    )
                }
            } catch (e: GetCastByIdFailedException) {
                _uiState.update { it.copy(tvShowError = true) }
            }
        }
    }

    private fun getActorTvShowDetails() {
        viewModelScope.launch {
            try {
                val tvShows = getActorTvShowPicksByIdUseCase.invoke(actorId)
                _uiState.update { it.copy(actorTvShowDetails = tvShows, tvShowError = false) }
            } catch (e: GetCastByIdFailedException) {
                _uiState.update { it.copy(tvShowError = true) }
            }
        }
    }
}