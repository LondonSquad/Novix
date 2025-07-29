package com.london.presentation.feature.home

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.london.domain.entity.Movie
import com.london.domain.usecase.GetPopularMovies
import com.london.domain.usecase.GetPopularTvShow
import com.london.domain.usecase.GetUpComingMoviesByCategoryUseCase
import com.london.presentation.feature.base.BaseViewModel
import com.london.presentation.feature.base.createPagingSourceFlow
import com.london.presentation.utils.MovieGenre
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class HomeViewModel(
    private val getPopularMovies: GetPopularMovies,
    private val getPopularTvShows: GetPopularTvShow,
    private val getUpcomingMoviesByCategoryUseCase: GetUpComingMoviesByCategoryUseCase,
) : BaseViewModel<HomeScreenUiState, HomeScreenEffect>(HomeScreenUiState()), HomeScreenContract {

    private val _upcomingMoviesFlow = MutableStateFlow<PagingData<Movie>>(PagingData.empty())
    private var upcomingJob: Job? = null

    init {
        initializePopularMovies()
        initializePopularTvShows()
        updateState {
            copy(upcomingMovies = _upcomingMoviesFlow)
        }
        loadUpcomingMovies(categoryId = null)
    }

    private fun initializePopularTvShows() {
        tryToExecute(
            block = { getPopularTvShows.invoke() },
            onStart = { updateState { copy(isLoading = true) } },
            onSuccess = { popularTvShows ->
                updateState { copy(popularTvShows = popularTvShows) }
            },
            onError = { errorState -> updateState { copy(error = errorState) } },
            onCompleted = { updateState { copy(isLoading = false) } },
            checkSuccess = { it.isNotEmpty() },
        )
    }

    private fun initializePopularMovies() {
        tryToExecute(
            block = { getPopularMovies.invoke() },
            onStart = { updateState { copy(isLoading = true) } },
            onSuccess = { popularMovies ->
                updateState { copy(popularMovies = popularMovies) }
            },
            onError = { errorState -> updateState { copy(error = errorState) } },
            onCompleted = { updateState { copy(isLoading = false) } },
            checkSuccess = { it.isNotEmpty() },
        )
    }

    override fun onMovieClick(id: Int) {
        emitEffect(HomeScreenEffect.NavigationMovieDetails(id))
    }

    override fun onTvShowClick(id: Int) {
        emitEffect(HomeScreenEffect.NavigationTvShowDetails(id))
    }

    private fun loadUpcomingMovies(categoryId: Int?) {
        runCatching {
            upcomingJob?.cancel()
        }
        upcomingJob = viewModelScope.launch {
            tryToExecute(
                block = {
                    val pagingFlow = createPagingSourceFlow(query = "") { _, pageNumber ->
                        getUpcomingMoviesByCategoryUseCase.invoke(
                            categoryId = categoryId,
                            pageNumber = pageNumber
                        )
                    }.cachedIn(viewModelScope)
                    updateState { copy(isLoading = false) }
                    pagingFlow
                },
                onStart = { updateState { copy(isLoading = true) } },
                onError = { errorState -> updateState { copy(error = errorState) } },
                onCompleted = { updateState { copy(isLoading = false) } },
                onSuccess = { flow ->
                    delay(70)
                    flow.collectLatest { pagingData ->
                        _upcomingMoviesFlow.value = pagingData
                    }
                },
            )
        }
    }

    override fun onMovieGenreSelect(genre: MovieGenre) {
        if (genre == state.value.selectedMovieGenre) return
        updateState { copy(selectedMovieGenre = genre) }
        loadUpcomingMovies(categoryId = if (genre == MovieGenre.All) null else genre.id)
    }

    override fun onTopRatedClick() {
        emitEffect(HomeScreenEffect.NavigationTopRated)
    }

    override fun onTrendingMoviesCardClicked() {
        emitEffect(HomeScreenEffect.NavigationTrendingMovie)
    }

    override fun onTrendingTvShowsCardClicked() {
        emitEffect(HomeScreenEffect.NavigationTrendingTvShows)
    }

    override fun onTrendingActorsCardClicked() {
        emitEffect(HomeScreenEffect.NavigationTrendingActor)
    }
}
