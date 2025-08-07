package com.london.presentation.feature.list.viewlistitems

import com.london.domain.usecase.movielist.GetMovieListDetailsUseCase
import com.london.presentation.feature.base.BaseViewModel
import com.london.presentation.feature.base.createPagingSourceFlow
import com.london.presentation.feature.list.viewlistitems.ViewListItemsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class ViewListItemsViewModel @Inject constructor(
    private val getMovieListDetailsUseCase: GetMovieListDetailsUseCase,
) :
    BaseViewModel<ViewListItemsUiState, ViewListItemsEffect>(ViewListItemsUiState()),
    ViewListItemsContract {

    init {
        fetchMovieListDetails(8548075)
    }

    private fun fetchMovieListDetails(listId: Int) {
        tryToExecute(
            block = {
                val moviesFlow = createPagingSourceFlow(query = "") { _, pageNumber ->
                    val movies = getMovieListDetailsUseCase.invoke(
                        listId = listId.toUInt(),
                        pageNumber = pageNumber
                    )
                    movies.copy(items = movies.items)
                }
                moviesFlow
            },
            onStart = {
                updateState { copy(isLoading = true) }
            },
            onSuccess = { moviesFlow ->
                updateState {
                    copy(listItems = moviesFlow, listTitle = "marwan")
                }
            },
            onError = { errorState ->
                updateState {
                    copy(error = errorState)
                }
            },
            onCompleted = { updateState { copy(isLoading = false) } },
        )
    }

    override fun onBack() {
        emitEffect(ViewListItemsEffect.NavigateBack)
    }

    override fun onRetry() {
        //TODO("handle retry to fetch data")
    }


    override fun onDeleteClick() {
        fetchMovieListDetails(8547741)
    }

    override fun onMovieClick(id: Int) {
        emitEffect(ViewListItemsEffect.NavigationMovieDetails(id))
    }

    override fun onRemoveMovieClick(id: Int) {
        //TODO("Not yet implemented")
    }

}
