package com.london.presentation.feature.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.london.domain.entity.movie.Movie
import com.london.domain.entity.movie.UpComingMovie
import com.london.domain.entity.toprated.TopRatedMedia
import com.london.domain.entity.tvshow.TvShow
import com.london.domain.usecase.details.movie.GetMovieUseCase
import com.london.domain.usecase.details.tvshow.GetTvShowUseCase
import com.london.domain.usecase.recent.watched.movie.ManageRecentMovieWatchedUseCase
import com.london.domain.usecase.recent.watched.tvshow.ManageRecentTvShowWatchedUseCase
import com.london.presentation.feature.home.popular.PopularUiMedia
import com.london.presentation.shared.base.BaseViewModel
import com.london.presentation.shared.base.ErrorState
import com.london.presentation.shared.base.createPagingSourceFlow
import com.london.presentation.shared.genre.MovieGenreUi
import com.london.presentation.shared.genre.toDomain
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
    private val getMovieUseCase: GetMovieUseCase,
    private val getTvShowUseCase: GetTvShowUseCase,
    private val manageRecentMovieWatchedUseCase: ManageRecentMovieWatchedUseCase,
    private val manageRecentTvShowWatchedUseCase: ManageRecentTvShowWatchedUseCase
) : BaseViewModel<HomeScreenUiState, HomeScreenEffect>(HomeScreenUiState()), HomeScreenContract {

    init {
        initializeData()
    }

    override fun loadUpcomingMoviesClick(genre: MovieGenreUi) {
        updateState {
            copy(selectedCategoryFlow = selectedCategoryFlow.apply { value = genre })
        }
    }

    override fun onMovieClick(id: Int) =
        emitEffect(HomeScreenEffect.MovieDetailsNavigation(id))

    override fun onTvShowClick(id: Int) =
        emitEffect(HomeScreenEffect.TvShowDetailsNavigation(id))

    override fun onMovieGenreSelect(genre: MovieGenreUi) {
        if (genre == state.value.selectedMovieGenre) return
        updateState { copy(selectedMovieGenre = genre) }
        loadUpcomingMoviesClick(genre)
    }

    override fun onTopRatedClick() = emitEffect(HomeScreenEffect.TopRatedNavigation)

    override fun onContinueWatchingClick() = emitEffect(HomeScreenEffect.ContinueWatchingNavigation)

    override fun onTrendingMoviesCardClick() = emitEffect(HomeScreenEffect.TrendingMovieNavigation)

    override fun onTrendingTvShowsCardClick() = emitEffect(HomeScreenEffect.TrendingTvShowsNavigation)

    override fun onTrendingActorsCardClick() = emitEffect(HomeScreenEffect.TrendingActorNavigation)

    override fun onRetryClick() {
        updateState { copy(error = null) }
        initializeTopRatedMedia()
        handleRecentWatchedMedia()
        initializePopularMedia()
    }

    override fun onManageBookmarkClicked(movieId: Int) {
        updateState {
            copy(
                isBookmarkSheetVisible = true,
                bookmarkedMovieId = movieId
            )
        }
    }

    override fun onBookmarkSheetDismiss() {
        updateState {
            copy(
                isBookmarkSheetVisible = false,
                bookmarkedMovieId = 0
            )
        }
    }

    private fun initializeData() {
        initializePopularMedia()
        initializeTopRatedMedia()
        handleRecentWatchedMedia()
        initializeUpcomingMoviesFlow()
    }

    private fun initializeUpcomingMoviesFlow() = updateState { copy(upcomingMovies = createUpComingFlow()) }

    private fun createUpComingFlow(): Flow<PagingData<UpComingMovie>> {
        val upcomingMoviesFlow: Flow<PagingData<UpComingMovie>> =
            state.value.selectedCategoryFlow
                .flatMapLatest { category -> createUpcomingPagingFlow(category ?: MovieGenreUi.All) }
                .cachedIn(viewModelScope)

        return upcomingMoviesFlow
    }

    private fun createUpcomingPagingFlow(genre: MovieGenreUi): Flow<PagingData<UpComingMovie>> {
        return createPagingSourceFlow(query = "") { _, pageNumber ->
            getMovieUseCase.getUpcomingMoviesByGenre(
                genre = genre.toDomain(),
                pageNumber = pageNumber
            )
        }
    }

    private fun initializeTopRatedMedia() = tryToExecute(
        block = { fetchTopRatedMedia() },
        onStart = { updateState { copy(isTopRatedLoading = true) } },
        onSuccess = { topRatedMedia -> handleTopRatedSuccess(topRatedMedia) },
        onError = { errorState -> updateErrorState(errorState) },
        onCompleted = { updateState { copy(isTopRatedLoading = false) } },
    )

    private fun handleTopRatedSuccess(topRatedMediaList: List<TopRatedMedia>) =
        updateState { copy(topRatedMediaList = topRatedMediaList.toUiMedia().shuffled()) }

    private suspend fun fetchTopRatedMedia(): List<TopRatedMedia> {
        val movies = getMovieUseCase.getMostRecentMovies()
        val tvShows = getTvShowUseCase.getMostRecentTvShows()

        return movies + tvShows
    }

    private fun handleRecentWatchedMedia() {
        tryToCollect(
            block = { fetchRecentWatchedMedia() },
            onNewValue = { recentWatchedMedia ->
                handleRecentWatchedMediaSuccess(recentWatchedMedia)
            },
            onError = { errorState -> updateErrorState(errorState) },
        )
    }

    private fun initializePopularMedia() = tryToExecute(
        block = { fetchPopularMediaList() },
        onStart = { updateState { copy(isPopularLoading = true) } },
        onSuccess = { popularMedia -> handlePopularMediaSuccess(popularMedia) },
        onError = { errorState -> updateErrorState(errorState) },
        onCompleted = { updateState { copy(isPopularLoading = false) } },
    )

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
    ): Flow<List<HomeUiMedia>> = combine(movies, shows) { movieList, showList ->
        movieList.toUiMedia() + showList.toUiMedia()
    }

    private fun handlePopularMediaSuccess(popularMedia: List<PopularUiMedia>) =
        updateState { copy(popularMediaList = popularMedia) }

    private fun updateErrorState(errorState: ErrorState) = updateState { copy(error = errorState) }

    private suspend fun fetchPopularMediaList(): List<PopularUiMedia> {
        val movies = getMovieUseCase.getPopularMovies()
        val tvShows = getTvShowUseCase.getPopularTvShows()

        return movies.toPopularUiMedia() + tvShows.toPopularUiMedia()
    }
}
