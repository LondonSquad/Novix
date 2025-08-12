package com.london.presentation.shared.bookmarkSheet

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.stringResource
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
import com.london.presentation.navigation.LocalNavController
import com.london.presentation.navigation.Screen
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
    val navController = LocalNavController.current

    val hideSheet: () -> Unit = {
        coroutineScope.launch {
            sheetState.hide()
        }.invokeOnCompletion {
            if (sheetState.isNotVisible) onSheetDismiss()
        }
    }

    LaunchedEffect(isSheetVisible) {
        if (isSheetVisible) {
            coroutineScope.launch { sheetState.show() }
        }
    }

    val uiState by viewModel.state.collectAsStateWithLifecycle()
    val effect by viewModel.effect.collectAsState(null)

    effect?.Listen { currentEffect ->
        when (currentEffect) {
            BookmarkSheetEffect.ItemSuccessfulAddition -> {
                hideSheet()
            }

            BookmarkSheetEffect.NewListCreation -> {
                hideSheet()
                navController.navigate(Screen.Lists(createList = true))
            }

            BookmarkSheetEffect.LoginNavigation -> {
                hideSheet()
                navController.navigate(Screen.Login)
            }
        }
    }

    if (isSheetVisible) {
        ModalBottomSheet(
            state = sheetState,
            onDismissRequest = onSheetDismiss,
            modifier = modifier,
            containerColor = NovixTheme.colors.surface,
        ) {
            BookmarkBottomSheetContent(
                hideSheet = hideSheet,
                contract = viewModel,
                uiState = uiState,
                bookmarkedMovieId = bookmarkedMovieId
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
    Column(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .padding(bottom = 24.dp)
            .fillMaxWidth()
            .heightIn(max = LocalWindowInfo.current.containerSize.height.dp * 0.5f)
            .scrollable(
                state = rememberScrollState(),
                orientation = Orientation.Vertical
            ),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SheetHeader(hideSheet = hideSheet)
        if (uiState.isGuestSession) {
            GuestLoginView()
            LoginButton(onLoginClick = contract::onLoginClick)
        } else {
            UserListsView(
                uiState = uiState,
                contract = contract,
            )

            UserActions(
                contract = contract,
                bookmarkedMovieId = bookmarkedMovieId,
                uiState = uiState
            )
        }
    }
}

@Composable
private fun SheetHeader(
    hideSheet: () -> Unit
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
}


@Composable
private fun UserListsView(
    uiState: BookmarkSheetUiState,
    contract: BookmarkSheetContract
) {
    val lists = uiState.lists.collectAsLazyPagingItems()

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 148.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
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
}

@Composable
fun UserActions(
    contract: BookmarkSheetContract,
    uiState: BookmarkSheetUiState,
    bookmarkedMovieId: UInt
) {
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
            onClick = { contract.onAddToLists(bookmarkedId = bookmarkedMovieId) },
            enabled = uiState.selectedLists.isNotEmpty()
        )

        OutlineButton(
            modifier = Modifier
                .fillMaxWidth()
                .requiredHeight(48.dp),
            text = R.string.create_new_list.string,
            onClick = contract::onCreateNewList,
            hasLabel = true,
            icon = null,
            hasIcon = false,
            isLoading = false
        )
    }
}

@Composable
private fun GuestLoginView(
    isDarkTheme: Boolean = NovixTheme.isThemeDark
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            painter =
                if (isDarkTheme) R.drawable.guest_login_dark.painter
                else R.drawable.guest_login_light.painter,
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier
                .size(100.dp)
        )

        Text(
            text = R.string.login_to_add_list.string,
            style = NovixTheme.typography.body.small,
            color = NovixTheme.colors.body
        )
    }
}

@Composable
private fun LoginButton(
    onLoginClick: () -> Unit
) {
    OutlineButton(
        modifier = Modifier.fillMaxWidth(),
        text = R.string.login.string,
        hasLabel = true,
        icon = R.drawable.app_icon,
        hasIcon = false,
        isLoading = false,
        onClick = onLoginClick,
        enabled = true,
    )
}
