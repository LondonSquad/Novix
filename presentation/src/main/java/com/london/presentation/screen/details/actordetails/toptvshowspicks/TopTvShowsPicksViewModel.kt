package com.london.presentation.screen.details.actordetails.toptvshowspicks

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.london.domain.usecase.GetActorTvShowPicksByIdUseCase
import com.london.presentation.navigation.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class TopTvShowsPicksViewModel(
    private val getActorTvShowPicksById: GetActorTvShowPicksByIdUseCase,
    savedStateHandle: SavedStateHandle,
    ) : ViewModel(), TopTvShowsPicksInteractions {

    private val _uiState = MutableStateFlow(TopTvShowsPicksUiState())
    val uiState: StateFlow<TopTvShowsPicksUiState> = _uiState.asStateFlow()

    private val actorId: Int = savedStateHandle.toRoute<Screen.TopTvShowsPicksDetails>().actorId

    init {
        if (actorId != 0) {
            getActorTvShowsPicksData()
        }
    }

    private fun getActorTvShowsPicksData() {
        viewModelScope.launch {
            Log.d("AAA","getActorTvShowsPicksData: ${getActorTvShowPicksById.invoke(actorId)}")
            try {
                _uiState.update {
                    it.copy(
                        id = it.id,
                        movieDetails = getActorTvShowPicksById.invoke(actorId),
                        isSaved = it.isSaved,
                        backdropPath = it.backdropPath,
                        numberOfMovies = it.numberOfMovies
                    )
                }
            } catch (e: Exception){
                Log.d("TAG", "getActorTvShowsPicksData: $e")
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