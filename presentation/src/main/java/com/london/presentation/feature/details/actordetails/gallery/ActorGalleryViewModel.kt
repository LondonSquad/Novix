package com.london.presentation.feature.details.actordetails.gallery

import androidx.lifecycle.SavedStateHandle
import com.london.domain.usecase.GetActorImagesByIdUseCase
import com.london.presentation.feature.base.BaseViewModel
import com.london.presentation.navigation.Screen
import com.london.presentation.navigation.getArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ActorGalleryViewModel @Inject constructor(
    private val getActorImagesByIdUseCase: GetActorImagesByIdUseCase,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<ActorGalleryUiState, ActorGalleryEffectUiState>(ActorGalleryUiState()),
    ActorGalleryContract {

    private val args = savedStateHandle.getArgs<Screen.ActorGallery>()
    private val actorId = args?.actorId ?: 0

    init {
        loadImages(actorId)
    }

    override fun onBackClick() {
        emitEffect(ActorGalleryEffectUiState.NavigationBack)
    }

    private fun loadImages(actorId: Int) {
        tryToExecute(
            block = {
                getActorImagesByIdUseCase.invoke(actorId)
            },
            onStart = { updateState { copy(isLoading = true) } },
            onSuccess = { imageDetails ->
                updateState {
                    copy(
                        images = imageDetails.map { it.fileUrl }
                    )
                }
            },
            onError = { errorState ->
                updateState { copy(error = errorState) }
            },
            onCompleted = { updateState { copy(isLoading = false) } },
            checkSuccess = { actorId != 0 },
        )
    }
}