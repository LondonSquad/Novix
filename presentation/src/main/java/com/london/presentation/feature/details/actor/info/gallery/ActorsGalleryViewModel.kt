package com.london.presentation.feature.details.actor.info.gallery

import androidx.lifecycle.SavedStateHandle
import com.london.domain.usecase.details.actor.GetActorUseCase
import com.london.presentation.navigation.Screen
import com.london.presentation.navigation.getArgs
import com.london.presentation.shared.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ActorsGalleryViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getActorUseCase: GetActorUseCase
) : BaseViewModel<ActorsGalleryUiState, ActorsGalleryEffect>(ActorsGalleryUiState()),
    ActorsGalleryContract {

    private val args = savedStateHandle.getArgs<Screen.ActorGallery>()
    private val actorId = args?.actorId ?: 0

    init {
        loadImages(actorId)
    }

    private fun loadImages(actorId: Int) {
        tryToExecute(
            block = {
                getActorUseCase.getActorImagesById(actorId)
            },
            onStart = { updateState { copy(isLoading = true) } },
            onSuccess = { imageDetails -> updateState { copy(images = imageDetails) } },
            onError = { errorState -> updateState { copy(error = errorState) } },
            onCompleted = { updateState { copy(isLoading = false) } },
        )
    }

    override fun onRetryClick() {
        updateState { copy(error = null) }
        loadImages(actorId)
    }

    override fun onBackClick() {
        emitEffect(ActorsGalleryEffect.BackNavigation)
    }

}
