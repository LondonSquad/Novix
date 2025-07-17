package com.london.presentation.screen.details.actordetails.topmoviespicks

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.london.domain.usecase.GetActorDetailsByIdUseCase
import com.london.domain.usecase.GetActorMoviePicksByIdUseCase
import com.london.presentation.navigation.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class TopMoviesPicksViewModel(
    private val getActorDetailsUseCase: GetActorDetailsByIdUseCase,
    private val getActorMoviePicksById: GetActorMoviePicksByIdUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel(), TopMoviesPicksInteractions {

    private val _uiState = MutableStateFlow(TopMoviesPicksUiState())
    val uiState: StateFlow<TopMoviesPicksUiState> = _uiState.asStateFlow()

    private val actorId: Int = savedStateHandle.toRoute<Screen.ActorTopMoviesPicksDetails>().actorId

    init {
        if (actorId != 0) {
            getActorMoviePicksData()
        }
    }

    private fun getActorMoviePicksData() {
        viewModelScope.launch {
            Log.d("AAA","getActorMoviePicksData: ${getActorMoviePicksById.invoke(actorId)}")
            try {
                _uiState.update {
                    it.copy(
                        id = it.id,
                        movieDetails = getActorMoviePicksById.invoke(actorId),
                        isSaved = it.isSaved,
                        backdropPath = it.backdropPath,
                        numberOfMovies = it.numberOfMovies
                    )
                }
            } catch (e: Exception){
                Log.d("TAG", "getActorMoviePicksData: $e")
            }
        }
    }

    override fun onMovieClick(movieId: Int) {
        // TODO(navigate to movie details)
    }

    override fun onBackClick() {
        // TODO(navigate back)
    }

    override fun onSaveMovie(movieId: Int) {
        _uiState.update {
            it.copy(isSaved = !it.isSaved)
        }
    }
}
