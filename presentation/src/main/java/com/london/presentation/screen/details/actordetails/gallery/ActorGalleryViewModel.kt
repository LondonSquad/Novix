package com.london.presentation.screen.details.actordetails.gallery

import androidx.lifecycle.SavedStateHandle
import com.london.domain.usecase.GetActorImagesByIdUseCase
import com.london.presentation.navigation.Screen
import com.london.presentation.navigation.getArgs
import com.london.presentation.screen.base.BaseViewModel
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class ActorGalleryViewModel(
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
                ActorGalleryUiState(
                    images = imageDetails.map { it.fileUrl },
                    isLoading = false,
                    error = null
                )
            },
            onError = { errorState ->
                updateState { copy(error = errorState) }
            },
            onCompleted = { updateState { copy(isLoading = false) } },
            checkSuccess = { actorId != 0 },
        )
    }

    override fun onBackClick() {
        emitEffect(ActorGalleryEffectUiState.NavigationBack)
    }
}