package com.london.presentation.feature.details.actor.info.gallery

import androidx.lifecycle.SavedStateHandle
import com.london.domain.usecase.GetActorImagesByIdUseCase
import com.london.presentation.navigation.Screen
import com.london.presentation.navigation.getArgs
import com.london.presentation.shared.base.BaseViewModel
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

    override fun onRetry(){
        updateState { copy(error = null) }
        loadImages(actorId)
    }

    override fun onBackClick() {
        emitEffect(ActorGalleryEffectUiState.NavigationBack)
    }
}