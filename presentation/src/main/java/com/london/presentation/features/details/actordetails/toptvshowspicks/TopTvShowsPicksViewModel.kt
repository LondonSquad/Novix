package com.london.presentation.features.details.actordetails.toptvshowspicks

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.london.domain.usecase.GetActorTvShowPicksByIdUseCase
import com.london.presentation.navigation.arguments.TopTvShowsArgs
import com.london.presentation.utils.launchCatching
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class TopTvShowsPicksViewModel(
    private val getActorTvShowPicksById: GetActorTvShowPicksByIdUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel(), TopTvShowsPicksInteractions {

    private val _uiState = MutableStateFlow(TopTvShowsPicksUiState())
    val uiState: StateFlow<TopTvShowsPicksUiState> = _uiState.asStateFlow()

    private val args by lazy { TopTvShowsArgs(savedStateHandle) }

    init {
        if (args.actorId != 0) {
            getActorTvShowsPicksData()
        }
    }

    private fun getActorTvShowsPicksData() {
        launchCatching {
            try {
                _uiState.update {
                    it.copy(
                        id = it.id,
                        tvShowDetails = getActorTvShowPicksById.invoke(args.actorId),
                        isSaved = it.isSaved,
                        backdropPath = it.backdropPath,
                    )
                }
            } catch (e: Exception) {
                Log.d("TAG", "getActorTvShowsPicksData: $e")
            }
        }
    }

    override fun onSaveMovie(movieId: Int) {
        _uiState.update {
            it.copy(isSaved = !it.isSaved)
        }
    }
}