package com.london.presentation.screen.details.actordetails.topmoviespicks

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.london.domain.usecase.GetActorMoviePicksByIdUseCase
import com.london.presentation.navigation.arguments.TopMoviesArgs
import com.london.presentation.utils.launchCatching
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class TopMoviesPicksViewModel(
    private val getActorMoviePicksById: GetActorMoviePicksByIdUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel(), TopMoviesPicksInteractions {

    private val _uiState = MutableStateFlow(TopMoviesPicksUiState())
    val uiState: StateFlow<TopMoviesPicksUiState> = _uiState.asStateFlow()

    private val args by lazy { TopMoviesArgs(savedStateHandle) }

    init {
        if (args.actorId != 0) {
            getActorMoviePicksData()
        }
    }

    private fun getActorMoviePicksData() {
        launchCatching {
            try {
                _uiState.update {
                    it.copy(
                        id = it.id,
                        movieDetails = getActorMoviePicksById.invoke(args.actorId),
                        isSaved = it.isSaved,
                        backdropPath = it.backdropPath,
                    )
                }
            } catch (e: Exception){
                Log.d("TAG", "getActorMoviePicksData: $e")
            }
        }
    }

    override fun onSaveMovie(movieId: Int) {
        _uiState.update {
            it.copy(isSaved = !it.isSaved)
        }
    }
}
