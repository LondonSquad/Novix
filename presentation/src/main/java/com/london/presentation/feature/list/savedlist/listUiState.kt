package com.london.presentation.feature.list.savedlist

import androidx.compose.ui.text.input.TextFieldValue
import androidx.paging.PagingData
import com.london.domain.entity.MovieList
import com.london.presentation.shared.base.ErrorState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

data class ListUiState(
    val isGuest: Boolean = false,
    val error: ErrorState? = null,
    val isLoading: Boolean = false,
    val isSnackBarSuccessVisible: Boolean = false,
    val items: Flow<PagingData<MovieList>> = flow {},
    val addListSheetState: AddSheetState = AddSheetState(),
)

data class AddSheetState(
    val errorMessage: String? = null,
    val isSheetVisible: Boolean = false,
    val isSheetLoading: Boolean = false,
    val listName: TextFieldValue = TextFieldValue(""),
)
