package com.london.presentation.screen.details.actordetails.gallery

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.london.domain.usecase.GetActorImagesByIdUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class ActorGalleryViewModel(
    private val getActorImagesByIdUseCase: GetActorImagesByIdUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel(), ActorGalleryInteractions {

    private val _uiState = MutableStateFlow(ActorGalleryUiState())
    val uiState: StateFlow<ActorGalleryUiState> = _uiState.asStateFlow()

    private val actorId: Int = savedStateHandle.get<Int>("actorId") ?: 0

    init {
        loadImages(actorId)
    }

    private fun loadImages(actorId: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val imageDetails = getActorImagesByIdUseCase.invoke(actorId)
                val imageUrls = imageDetails.map { it.fileUrl }
                _uiState.value = ActorGalleryUiState(
                    images = imageUrls,
                    isLoading = false,
                    error = null
                )
            } catch (e: Exception) {
                _uiState.value = ActorGalleryUiState(
                    images = emptyList(),
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }

    override fun onBackClick() { }
}