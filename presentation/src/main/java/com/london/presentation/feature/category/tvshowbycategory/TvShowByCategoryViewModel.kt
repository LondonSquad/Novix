package com.london.presentation.feature.category.tvshowbycategory

import androidx.lifecycle.SavedStateHandle
import com.london.domain.usecase.GetTvShowsByCategoryId
import com.london.presentation.feature.base.BaseViewModel
import com.london.presentation.feature.base.createPagingSourceFlow
import com.london.presentation.navigation.Screen
import com.london.presentation.navigation.getArgs
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.Provided

@KoinViewModel
class TvShowByCategoryViewModel(
    @Provided private val getTvShowsByCategoryIdUseCase: GetTvShowsByCategoryId,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<TvShowByCategoryUiState, TvShowByCategoryEffect>(TvShowByCategoryUiState()),
    TvShowByCategoryContract {

    private val args = savedStateHandle.getArgs<Screen.TvShowsByCategory>()
    private val categoryId = args?.categoryId ?: 0

    init {
        initializeTvShows(categoryId)
    }

    override fun onSavedClick(tvShowId: Int) {
        //TODO("Save Tv Show Not yet implemented")
    }

    override fun onTvShowClick(tvShowId: Int) {
        emitEffect(TvShowByCategoryEffect.NavigateToTvShowDetails(tvShowId = tvShowId))
    }

    override fun onBackClick() {
        emitEffect(TvShowByCategoryEffect.NavigateBack)
    }

    private fun initializeTvShows(categoryId: Int) {
        tryToExecute(
            block = {
            val tvShowFlow = createPagingSourceFlow(query = "") { _, pageNumber ->
                val tvShows = getTvShowsByCategoryIdUseCase.invoke(
                    categoryId = categoryId, pageNumber = pageNumber
                )
                tvShows.copy(items = tvShows.items)
            }
            tvShowFlow
        },
            onStart = {
                updateState { copy(categoryId = categoryId, isLoading = true) }
            },
            onSuccess = { tvShowFlow ->
                updateState {
                    copy(tvShowFlow = tvShowFlow)
                }
            },
            onError = { errorState ->
                updateState {
                    copy(error = errorState)
                }
            },
            onCompleted = { updateState { copy(isLoading = false) } },
            checkSuccess = { categoryId != 0 })
    }
}
