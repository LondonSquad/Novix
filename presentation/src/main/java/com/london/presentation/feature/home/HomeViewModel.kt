package com.london.presentation.feature.home

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.london.domain.entity.Movie
import com.london.domain.usecase.GetPopularMovies
import com.london.domain.usecase.GetPopularTvShow
import com.london.domain.usecase.GetUpComingMoviesByCategoryUseCase
import com.london.domain.usecase.recent.watched.GetRecentWatchedMoviesUseCase
import com.london.domain.usecase.recent.watched.GetRecentWatchedTvShowsUseCase
import com.london.domain.usecase.toprated.GetTopRatedMoviesUseCase
import com.london.domain.usecase.toprated.GetTopRatedTvSeriesUseCase
import com.london.presentation.feature.base.BaseViewModel
import com.london.presentation.feature.base.createPagingSourceFlow
import com.london.presentation.utils.MovieGenre
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getPopularMovies: GetPopularMovies,
    private val getPopularTvShows: GetPopularTvShow,
    private val getUpcomingMoviesByCategoryUseCase: GetUpComingMoviesByCategoryUseCase,
    private val getTopRatedMovies: GetTopRatedMoviesUseCase,
    private val getTopRatedTvShows: GetTopRatedTvSeriesUseCase,
    private val getRecentWatchedMovies: GetRecentWatchedMoviesUseCase,
    private val getRecentWatchedTvShows: GetRecentWatchedTvShowsUseCase
) : BaseViewModel<HomeScreenUiState, HomeScreenEffect>(HomeScreenUiState()), HomeScreenContract {

    private val _upcomingMoviesFlow = MutableStateFlow<PagingData<Movie>>(PagingData.empty())
    private var upcomingJob: Job? = null

    init {
        initializePopularMedia()
        initializeTopRatedMedia()
        updateState {
            copy(upcomingMovies = _upcomingMoviesFlow)
        }
        loadUpcomingMovies(categoryId = null)
    }

    private fun initializeTopRatedMedia() {
        tryToExecute(
            block = {
                val movies = getTopRatedMovies.invoke(pageNumber = 1)
                val shows = getTopRatedTvShows.invoke(pageNumber = 1)
                Pair(movies, shows)
            },
            onStart = { updateState { copy(isTopRatedLoading = true) } },
            onSuccess = { (movies, shows) ->
                val topRatedMedia = movies.items.take(10).toUiMedia() +
                        shows.items.take(10).toUiMedia()

                updateState {
                    copy(topRatedMediaList = topRatedMedia.shuffled())
                }
            },
            onError = { errorState -> updateState { copy(error = errorState) } },
            onCompleted = { updateState { copy(isTopRatedLoading = false) } },
            checkSuccess = { (movies, shows) ->
                movies.items.isNotEmpty() || shows.items.isNotEmpty()
            }
        )
    }

    fun fetchRecentWatchedMedia() {
        tryToExecute(
            block = {
                val movies = getRecentWatchedMovies.invoke(limit = 10)
                val shows = getRecentWatchedTvShows.invoke(limit = 10)
                Pair(movies, shows)
            },
            onStart = { updateState { copy(isLoading = true) } },
            onSuccess = { (movies, shows) ->
                val recentWatchedMedia = movies.toUiMedia() +
                        shows.toUiMedia()

                updateState {
                    copy(recentWatchedMediaList = recentWatchedMedia.shuffled())
                }
            },
            onError = { errorState -> updateState { copy(error = errorState) } },
            onCompleted = { updateState { copy(isLoading = false) } },
        )
    }

    private fun initializePopularMedia() {
        tryToExecute(
            block = {
                val movies = getPopularMovies.invoke()
                val shows = getPopularTvShows.invoke()
                Pair(movies, shows)
            },
            onStart = { updateState { copy(isTopRatedLoading = true) } },
            onSuccess = { (movies, shows) ->
                val popularMedia = movies.toPopularUiMedia() +
                        shows.toPopularUiMedia()

                updateState {
                    copy(popularMediaList = popularMedia)
                }
            },
            onError = { errorState -> updateState { copy(error = errorState) } },
            onCompleted = { updateState { copy(isTopRatedLoading = false) } },
            checkSuccess = { (movies, shows) ->
                movies.isNotEmpty() || shows.isNotEmpty()
            }
        )
    }

    fun onRetry() {
        updateState { copy(error = null) }
        initializeTopRatedMedia()
        fetchRecentWatchedMedia()
        initializePopularMedia()
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

    override fun onContinueWatchingClick() {
        emitEffect(HomeScreenEffect.NavigationContinueWatching)
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
