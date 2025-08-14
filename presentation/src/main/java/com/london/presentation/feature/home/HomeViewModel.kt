package com.london.presentation.feature.home

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.london.domain.entity.Movie
import com.london.domain.entity.TvShow
import com.london.domain.entity.UpComingMovie
import com.london.domain.entity.toprated.TopRatedMedia
import com.london.domain.usecase.GetPopularMovies
import com.london.domain.usecase.GetUpComingMoviesByCategoryUseCase
import com.london.domain.usecase.details.tvshow.ManageTvShowDetailsUseCase
import com.london.domain.usecase.recent.watched.movie.ManageRecentMovieWatchedUseCase
import com.london.domain.usecase.recent.watched.tvshow.ManageRecentTvShowWatchedUseCase
import com.london.domain.usecase.toprated.GetTopRatedMoviesUseCase
import com.london.domain.usecase.toprated.GetTopRatedTvShowUseCase
import com.london.presentation.feature.home.popular.PopularUiMedia
import com.london.presentation.shared.base.BaseViewModel
import com.london.presentation.shared.base.ErrorState
import com.london.presentation.shared.base.createPagingSourceFlow
import com.london.presentation.utils.MovieGenre
import com.london.presentation.utils.toPopularUiMedia
import com.london.presentation.utils.toUiMedia
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModel @Inject constructor(
    private val getPopularMovies: GetPopularMovies,
    private val manageTvShowDetailsUseCase: ManageTvShowDetailsUseCase,
    private val getUpcomingMoviesByCategoryUseCase: GetUpComingMoviesByCategoryUseCase,
    private val getTopRatedMovies: GetTopRatedMoviesUseCase,
    private val getTopRatedTvShows: GetTopRatedTvShowUseCase,
    private val manageRecentMovieWatchedUseCase: ManageRecentMovieWatchedUseCase,
    private val manageRecentTvShowWatchedUseCase: ManageRecentTvShowWatchedUseCase
) : BaseViewModel<HomeScreenUiState, HomeScreenEffect>(HomeScreenUiState()), HomeScreenContract {

    init {
        initializePopularMedia()
        initializeTopRatedMedia()
        handleRecentWatchedMedia()
        initializeUpcomingMoviesFlow()
    }

    private fun initializeUpcomingMoviesFlow() =
        updateState { copy(upcomingMovies = createUpComingFlow()) }

    override fun loadUpcomingMoviesClick(categoryId: Int?) {
        updateState {
            copy(selectedCategoryFlow = selectedCategoryFlow.apply { value = categoryId })
        }
    }

    private fun createUpComingFlow(): Flow<PagingData<UpComingMovie>> {
        val upcomingMoviesFlow: Flow<PagingData<UpComingMovie>> =
            state.value.selectedCategoryFlow
                .flatMapLatest { categoryId -> createUpcomingPagingFlow(categoryId) }
                .cachedIn(viewModelScope)

        return upcomingMoviesFlow
    }

    private fun createUpcomingPagingFlow(categoryId: Int?): Flow<PagingData<UpComingMovie>> {
        return createPagingSourceFlow(query = "") { _, pageNumber ->
            getUpcomingMoviesByCategoryUseCase.invoke(
                categoryId = categoryId,
                pageNumber = pageNumber
            )
        }
    }

    private fun initializeTopRatedMedia() {
        tryToExecute(
            block = { fetchTopRatedMedia() },
            onStart = { updateLoadingState(true) },
            onSuccess = { topRatedMedia -> handleTopRatedSuccess(topRatedMedia) },
            onError = { errorState -> updateErrorState(errorState) },
            onCompleted = { updateLoadingState(false) },
        )
    }

    private fun handleTopRatedSuccess(topRatedMediaList: List<TopRatedMedia>) =
        updateState { copy(topRatedMediaList = topRatedMediaList.toUiMedia().shuffled()) }

    private suspend fun fetchTopRatedMedia(): List<TopRatedMedia> {
        val movies = getTopRatedMovies.getMostRecent()
        val tvShows = getTopRatedTvShows.getMostRecent()

        return movies + tvShows
    }

    private fun handleRecentWatchedMedia() {
        tryToCollect(
            block = { fetchRecentWatchedMedia() },
            onStart = { updateLoadingState(true) },
            onNewValue = { recentWatchedMedia ->
                handleRecentWatchedMediaSuccess(recentWatchedMedia)
            },
            onError = { errorState -> updateErrorState(errorState) },
            onCompleted = { updateLoadingState(false) },
        )
    }

    private fun initializePopularMedia() {
        tryToExecute(
            block = { fetchPopularMediaList() },
            onStart = { updateLoadingState(true) },
            onSuccess = { popularMedia -> handlePopularMediaSuccess(popularMedia) },
            onError = { errorState -> updateErrorState(errorState) },
            onCompleted = { updateLoadingState(false) },
        )
    }

    private fun handleRecentWatchedMediaSuccess(recentWatchedMedia: List<HomeUiMedia>) =
        updateState { copy(recentWatchedMediaFlow = flowOf(recentWatchedMedia)) }

    private suspend fun fetchRecentWatchedMedia(): Flow<List<HomeUiMedia>> {
        val movies = manageRecentMovieWatchedUseCase.getMostRecent()
        val shows = manageRecentTvShowWatchedUseCase.getMostRecent()
        return combineRecentMedia(movies, shows)
    }

    private fun combineRecentMedia(
        movies: Flow<List<Movie>>,
        shows: Flow<List<TvShow>>
    ): Flow<List<HomeUiMedia>> {
        return combine(movies, shows) { movieList, showList ->
            movieList.toUiMedia() + showList.toUiMedia()
        }
    }

    private fun handlePopularMediaSuccess(popularMedia: List<PopularUiMedia>) =
        updateState { copy(popularMediaList = popularMedia) }

    private fun updateErrorState(errorState: ErrorState) =
        updateState { copy(error = errorState) }

    private fun updateLoadingState(isLoading: Boolean) =
        updateState { copy(isPopularLoading = isLoading) }

    private suspend fun fetchPopularMediaList(): List<PopularUiMedia> {
        val movies = getPopularMovies.invoke()
        val tvShows = manageTvShowDetailsUseCase.getPopularTvShows()

        return movies.toPopularUiMedia() + tvShows.toPopularUiMedia()
    }

    override fun onRetryClick() {
        updateState { copy(error = null) }
        initializeTopRatedMedia()
        handleRecentWatchedMedia()
        initializePopularMedia()
    }

    override fun onMovieClick(id: Int) =
        emitEffect(HomeScreenEffect.NavigationMovieDetails(id))

    override fun onTvShowClick(id: Int) =
        emitEffect(HomeScreenEffect.NavigationTvShowDetails(id))

    override fun onMovieGenreSelect(genre: MovieGenre) {
        if (genre == state.value.selectedMovieGenre) return
        updateState { copy(selectedMovieGenre = genre) }
        loadUpcomingMoviesClick(categoryId = if (genre == MovieGenre.All) null else genre.id)
    }

    override fun onTopRatedClick() =
        emitEffect(HomeScreenEffect.NavigationTopRated)

    override fun onContinueWatchingClick() =
        emitEffect(HomeScreenEffect.NavigationContinueWatching)

    override fun onTrendingMoviesCardClick() =
        emitEffect(HomeScreenEffect.NavigationTrendingMovie)

    override fun onTrendingTvShowsCardClick() =
        emitEffect(HomeScreenEffect.NavigationTrendingTvShows)

    override fun onTrendingActorsCardClick() =
        emitEffect(HomeScreenEffect.NavigationTrendingActor)
}