package com.london.presentation.screen.details.actor

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.london.domain.usecase.GetActorDetailsByIdUseCase
import com.london.presentation.navigation.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class ActorDetailsViewModel(
    private val getActorDetailsByIdUseCase: GetActorDetailsByIdUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ActorDetailsUiState())
    val uiState = _uiState.asStateFlow()

    private val actorId: Int = savedStateHandle.toRoute<Screen.ActorDetails>().actorId

    init {
        updateActorImage()
    }

    private fun updateActorImage() {
        viewModelScope.launch {
            val actorDetails = getActorDetailsByIdUseCase.invoke(actorId)
            _uiState.update {
                it.copy(
                    actorDetails = actorDetails
                )
            }
        }
    }
}