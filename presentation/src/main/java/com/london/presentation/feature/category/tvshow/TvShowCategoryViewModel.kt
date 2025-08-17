package com.london.presentation.feature.category.tvshow

import androidx.lifecycle.SavedStateHandle
import androidx.paging.PagingData
import com.london.domain.entity.TvShow
import com.london.domain.usecase.details.tvshow.GetTvShowUseCase
import com.london.presentation.navigation.Screen
import com.london.presentation.navigation.getArgs
import com.london.presentation.shared.base.BaseViewModel
import com.london.presentation.shared.base.ErrorState
import com.london.presentation.shared.base.createPagingSourceFlow
import com.london.presentation.shared.genre.TvShowGenreUi
import com.london.presentation.shared.genre.toDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class TvShowCategoryViewModel @Inject constructor(
    private val managerTvShowDetailsUseCase: GetTvShowUseCase,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<TvShowCategoryUiState, TvShowCategoryEffect>(TvShowCategoryUiState()),
    TvShowCategoryContract {

    private val args = savedStateHandle.getArgs<Screen.TvShowsByCategory>()

    private val genre = args?.category ?: TvShowGenreUi.All

    init {
        initializeTvShows(genreUi = genre)
    }

    override fun onTvShowClick(tvShowId: Int) =
        emitEffect(TvShowCategoryEffect.TvShowDetailsNavigation(tvShowId = tvShowId))

    override fun onBackClick() =
        emitEffect(TvShowCategoryEffect.BackNavigation)

    override fun onSavedClick(tvShowId: Int) = Unit //TODO("Save Tv Show Not yet implemented")

    private fun initializeTvShows(genreUi: TvShowGenreUi) {
        tryToExecute(
            onStart = { onInitializeTvShowsStarted(genreUi = genreUi) },
            block = { createTvShowsPagingSourceFlow(genreUi = genreUi) },
            onSuccess = ::onInitializeTvShowSuccess,
            checkSuccess = { genreUi != TvShowGenreUi.All },
            onError = ::onInitializeTvShowsFailed,
            onCompleted = ::onInitializeTvShowsCompleted
        )
    }

    private fun createTvShowsPagingSourceFlow(genreUi: TvShowGenreUi): Flow<PagingData<TvShow>> {

        return createPagingSourceFlow(query = "") { _, pageNumber ->
            managerTvShowDetailsUseCase.getTvShowsByGenre(
                genre = genreUi.toDomain(), pageNumber = pageNumber
            )
        }
    }

    private fun onInitializeTvShowsStarted(genreUi: TvShowGenreUi) =
        updateState { copy(genre = genreUi, isLoading = true) }

    private fun onInitializeTvShowSuccess(tvShowFlow: Flow<PagingData<TvShow>>) =
        updateState { copy(tvShowFlow = tvShowFlow) }

    private fun onInitializeTvShowsCompleted() =
        updateState { copy(isLoading = false) }

    private fun onInitializeTvShowsFailed(errorState: ErrorState) =
        updateState { copy(error = errorState) }
}
