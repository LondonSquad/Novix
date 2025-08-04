package com.london.presentation.feature.bookmark.sheet

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
import com.london.domain.entity.Movie
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
    movie: Movie
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
            BookmarkSheetEffect.NewListCreation -> TODO()
            BookmarkSheetEffect.ItemFailedAddition -> TODO()
            BookmarkSheetEffect.ItemSuccessfulAddition -> TODO()
        }
    }

    if (isSheetVisible) {
        ModalBottomSheet(
            onDismissRequest = onSheetDismiss,
            containerColor = NovixTheme.colors.surface,
            state = sheetState,
            modifier = modifier
                .padding(horizontal = 16.dp)
                .padding(bottom = 24.dp),
        ) {
            BookmarkBottomSheetContent(
                modifier = modifier,
                hideSheet = {
                    coroutineScope.launch { sheetState.hide() }
                        .invokeOnCompletion { if (sheetState.isNotVisible) onSheetDismiss() }
                },
                contract = viewModel,
                uiState = uiState
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
) {

    Column(
        modifier = modifier
            .fillMaxWidth(),
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

            Box(
                modifier = modifier
                    .background(color = NovixTheme.colors.iconBackgroundLow)
                    .clip(RoundedCornerShape(8.dp))
                    .border(
                        width = 1.dp,
                        color = NovixTheme.colors.stroke,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clickable(onClick = { /*onCancelClick*/ }),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier
                        .padding(8.dp)
                        .size(16.dp),
                    painter = com.london.designsystem.R.drawable.cancel.painter,
                    contentDescription = R.string.cancel_addition_to_list.string,
                    tint = NovixTheme.colors.title
                )
            }

        }

        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(uiState.lists) { list ->
                Selection(
                    modifier = Modifier.fillMaxWidth(),
                    mainText = list.name,
                    isSelected = list.id in uiState.selectedLists,
                    subText = list.itemCount.toString(),
                    onClick = { contract.onListSelected(list.id) }
                )
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
                    contract.onAddToLists(uiState.selectedLists)
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
            uiState = BookmarkSheetUiState(
                lists = listOf(
                    BookmarkUiList(
                        id = 0u,
                        name = "TODO()",
                        itemCount = 12u
                    )
                )
            ),
            contract = hiltViewModel<BookmarkSheetViewModel>(),
            modifier = Modifier.background(NovixTheme.colors.surface),
            hideSheet = {}
        )
    }
}