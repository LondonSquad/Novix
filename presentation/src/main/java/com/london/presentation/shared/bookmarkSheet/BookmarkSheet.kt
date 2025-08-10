package com.london.presentation.shared.bookmarkSheet

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.london.designsystem.component.Icon
import com.london.designsystem.component.ModalBottomSheet
import com.london.designsystem.component.Selection
import com.london.designsystem.component.SheetState
import com.london.designsystem.component.Text
import com.london.designsystem.component.button.OutlineButton
import com.london.designsystem.component.button.PrimaryButton
import com.london.designsystem.component.rememberModalBottomSheetState
import com.london.designsystem.theme.NovixTheme
import com.london.designsystem.utils.painter
import com.london.designsystem.utils.string
import com.london.presentation.R
import com.london.presentation.utils.Listen
import kotlinx.coroutines.launch

@Composable
fun BookmarkBottomSheet(
    modifier: Modifier = Modifier,
    onSheetDismiss: () -> Unit,
    isSheetVisible: Boolean,
    viewModel: BookmarkSheetViewModel = hiltViewModel(),
    sheetState: SheetState = rememberModalBottomSheetState(),
    bookmarkedMovieId: UInt
) {
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(isSheetVisible) {
        if (isSheetVisible) {
            coroutineScope.launch { sheetState.show() }
        }
    }

    val uiState by viewModel.state.collectAsStateWithLifecycle()
    val effect by viewModel.effect.collectAsState(null)

    effect?.Listen { currentEffect ->
        when (currentEffect) {
            BookmarkSheetEffect.NewListCreation -> {
                // navigate
            }
        }
    }

    if (isSheetVisible) {
        ModalBottomSheet(
            onDismissRequest = onSheetDismiss,
            containerColor = NovixTheme.colors.surface,
            state = sheetState,
            modifier = modifier,
        ) {
            BookmarkBottomSheetContent(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 24.dp),
                hideSheet = {
                    coroutineScope.launch { sheetState.hide() }
                        .invokeOnCompletion { if (sheetState.isNotVisible) onSheetDismiss() }
                },
                contract = viewModel,
                uiState = uiState,
                bookmarkedMovieId = bookmarkedMovieId,
            )
        }
    }
}

@Composable
private fun BookmarkBottomSheetContent(
    uiState: BookmarkSheetUiState,
    contract: BookmarkSheetContract,
    modifier: Modifier = Modifier,
    hideSheet: () -> Unit,
    bookmarkedMovieId: UInt
) {
    val lists = uiState.lists.collectAsLazyPagingItems()

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Text(
                text = R.string.save_to_list.string,
                style = NovixTheme.typography.title.large,
                color = NovixTheme.colors.title,
            )

            Icon(
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .border(
                        width = 1.dp,
                        color = NovixTheme.colors.stroke,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clickable(onClick = hideSheet)
                    .padding(8.dp),
                painter = com.london.designsystem.R.drawable.cancel.painter,
                contentDescription = R.string.cancel_addition_to_list.string,
                tint = NovixTheme.colors.title
            )

        }

        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(lists.itemCount) { index ->
                val movieList = lists[index]
                movieList?.let {
                    Selection(
                        modifier = Modifier.fillMaxWidth(),
                        mainText = movieList.name,
                        isSelected = movieList.id in uiState.selectedLists,
                        subText = stringResource(R.string.n_items, movieList.itemCount.toInt()),
                        onClick = { contract.onListSelected(movieList.id) }
                    )
                }
            }
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            PrimaryButton(
                modifier = Modifier.fillMaxWidth(),
                text = R.string.add.string,
                hasLabel = true,
                hasIcon = false,
                isLoading = false,
                icon = null,
                onClick = {
                    contract.onAddToLists(bookmarkedId = bookmarkedMovieId)
                }
            )

            OutlineButton(
                modifier = Modifier.fillMaxWidth(),
                text = R.string.create_new_list.string,
                onClick = contract::onCreateNewList,
                hasLabel = true,
                icon = null,
                hasIcon = false,
                isLoading = false
            )
        }
    }
}

@Preview
@Composable
fun Preview() {
    NovixTheme {
        BookmarkBottomSheetContent(
            uiState = BookmarkSheetUiState(),
            contract = hiltViewModel<BookmarkSheetViewModel>(),
            modifier = Modifier.background(NovixTheme.colors.surface),
            hideSheet = {},
            bookmarkedMovieId = 0u
        )
    }
}