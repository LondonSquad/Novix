package com.london.presentation.feature.category.tvshow

import androidx.lifecycle.SavedStateHandle
import com.london.domain.usecase.details.tvshow.ManageTvShowDetailsUseCase
import com.london.presentation.navigation.Screen
import com.london.presentation.navigation.getArgs
import com.london.presentation.shared.base.BaseViewModel
import com.london.presentation.shared.base.createPagingSourceFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TvShowCategoryViewModel @Inject constructor(
    private val managerTvShowDetailsUseCase: ManageTvShowDetailsUseCase,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<TvShowCategoryUiState, TvShowCategoryEffect>(TvShowCategoryUiState()),
    TvShowCategoryContract {

    private val args = savedStateHandle.getArgs<Screen.TvShowsByCategory>()
    private val categoryId = args?.categoryId ?: 0

    init {
        initializeTvShows(categoryId)
    }

    override fun onSavedClick(tvShowId: Int) {
        //TODO("Save Tv Show Not yet implemented")
    }

    override fun onTvShowClick(tvShowId: Int) {
        emitEffect(TvShowCategoryEffect.NavigateToTvShowDetails(tvShowId = tvShowId))
    }

    override fun onBack() {
        emitEffect(TvShowCategoryEffect.NavigateBack)
    }

    private fun initializeTvShows(categoryId: Int) {
        tryToExecute(
            block = {
                val tvShowFlow = createPagingSourceFlow(query = "") { _, pageNumber ->
                    val tvShows = managerTvShowDetailsUseCase.getTvShowsByCategory(
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
