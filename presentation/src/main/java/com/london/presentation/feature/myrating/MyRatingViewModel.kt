package com.london.presentation.feature.myrating

import com.london.presentation.shared.base.BaseViewModel
import com.london.presentation.shared.base.createPagingSourceFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class MyRatingViewModel @Inject constructor(
    private val getMyRatingMovies: GetMyRatingUseCase,
) : BaseViewModel<MyRatingUiState, MyRatingEffect>(MyRatingUiState()),
    MyRatingContract {

    init {
        initializeItems()
    }

    private fun initializeItems() {
        tryToExecute(
            block = {
                val itemsFlow = createPagingSourceFlow(query = "") { _, pageNumber ->
                    val items = getMyRatingMovies.invoke(page = pageNumber)
                    val filteredItems =
                        if (state.value.selectedGenreId != null && state.value.selectedGenreId != -1) {
                            items.items.filter { it.genreIds.contains(state.value.selectedGenreId) }
                        } else {
                            items.items
                        }
                    items.copy(items = filteredItems)
                }
                itemsFlow
            },
            onStart = {
                updateState { copy(isLoading = true) }
            },
            onSuccess = { itemsFlow ->
                updateState {
                    copy(itemsFlow = itemsFlow)
                }
            },
            onCompleted = { updateState { copy(isLoading = false) } },
        )
    }

    override fun onBackClicked() = emitEffect(MyRatingEffect.NavigateBack)

    override fun onItemClick(id: Int) = emitEffect(MyRatingEffect.NavigateToItem(id))

    override fun onDelete(id: Int) {
        TODO("Not yet implemented")
    }

}