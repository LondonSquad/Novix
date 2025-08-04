package com.london.presentation.feature.list.bottomsheets

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.london.designsystem.component.Icon
import com.london.designsystem.component.ModalBottomSheet
import com.london.designsystem.component.OutlinedTextField
import com.london.designsystem.component.SheetState
import com.london.designsystem.component.Text
import com.london.designsystem.component.button.PrimaryButton
import com.london.designsystem.component.rememberModalBottomSheetState
import com.london.designsystem.theme.NovixTheme
import com.london.domain.entity.recent.MediaType
import com.london.presentation.R
import com.london.presentation.feature.list.savedlist.ListContract
import com.london.presentation.feature.list.savedlist.ListSheetMode
import com.london.presentation.feature.list.savedlist.ListUiState
import com.london.presentation.feature.list.savedlist.ListViewModel
import kotlinx.coroutines.launch

@Composable
fun AddEditListBottomSheet(
    modifier: Modifier = Modifier,
    editInteractions: ListContract = hiltViewModel<ListViewModel>(),
    sheetState: SheetState = rememberModalBottomSheetState(),
    listUiState: ListUiState,
) {
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(listUiState.isSheetVisible) {
        if (listUiState.isSheetVisible) {
            coroutineScope.launch { sheetState.show() }
        }
    }
    if (listUiState.isSheetVisible) {
        ModalBottomSheet(
            onDismissRequest = {
                if (!listUiState.isLoading) {
                    editInteractions.onEditListSheetDismiss()
                }
            },
            containerColor = NovixTheme.colors.surface,
            state = sheetState,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .heightIn(max = LocalWindowInfo.current.containerSize.height.dp * 0.35f)
                    .padding(bottom = 24.dp)
            ) {
                AddEditListBottomSheetContent(
                    modifier = modifier,
                    editInteractions = editInteractions,
                    listUiState = listUiState,
                    onCloseClicked = {
                        if (!listUiState.isLoading) {
                            coroutineScope.launch {
                                sheetState.hide()
                            }.invokeOnCompletion {
                                if (!sheetState.isVisible) {
                                    editInteractions.onEditListSheetDismiss()
                                }
                            }
                        }
                    },
                    onActionClicked = {
                        editInteractions.onSaveEdit(listUiState.listName)
                    }
                )
            }
        }
    }
}

@Composable
private fun AddEditListBottomSheetContent(
    modifier: Modifier = Modifier,
    editInteractions: ListContract,
    onCloseClicked: () -> Unit,
    onActionClicked: () -> Unit,
    listUiState: ListUiState,
) {
    val interactionSourceUserName = remember { MutableInteractionSource() }

    val titleText = when (listUiState.sheetMode) {
        ListSheetMode.ADD -> stringResource(R.string.add_new_list)
        ListSheetMode.EDIT -> stringResource(R.string.edit_list)
    }

    val buttonText = when (listUiState.sheetMode) {
        ListSheetMode.ADD -> stringResource(R.string.add)
        ListSheetMode.EDIT -> stringResource(R.string.save)
    }

    val isButtonEnabled = when (listUiState.sheetMode) {
        ListSheetMode.ADD -> !listUiState.isLoading &&
                listUiState.listName.text.trim().isNotEmpty()

        ListSheetMode.EDIT -> !listUiState.isLoading &&
                listUiState.listName.text.trim().isNotEmpty() &&
                listUiState.listName.text.trim() != listUiState.originalListName
    }

    Column(
        modifier = modifier.padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = titleText,
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
                    .clickable(
                        enabled = !listUiState.isLoading,
                        onClick = onCloseClicked
                    )
                    .padding(6.dp),
                painter = painterResource(com.london.designsystem.R.drawable.cancel),
                contentDescription = "Close",
                tint = if (listUiState.isLoading)
                    NovixTheme.colors.title.copy(alpha = 0.5f)
                else
                    NovixTheme.colors.title
            )
        }

        Text(
            text = stringResource(R.string.list_title),
            style = NovixTheme.typography.title.small,
            color = NovixTheme.colors.title,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = listUiState.listName,
            interactionSource = interactionSourceUserName,
            onValueChange = editInteractions::onListNameChanged,
            leadingIcon = painterResource(R.drawable.editlisticon),
            isError = listUiState.errorMessage != null,
            enabled = !listUiState.isLoading
        )

        PrimaryButton(
            text = buttonText,
            hasLabel = true,
            hasIcon = false,
            isLoading = listUiState.isLoading,
            icon = null,
            onClick = onActionClicked,
            enabled = isButtonEnabled,
            modifier = Modifier
                .padding(top = 24.dp)
                .fillMaxWidth()
        )
    }
}

@Composable
@Preview(name = "Add Mode")
fun PreviewAddMode() {
    AddEditListBottomSheetContent(
        listUiState = ListUiState(
            listName = TextFieldValue(""),
            isSheetVisible = true,
            sheetMode = ListSheetMode.ADD
        ),
        onActionClicked = {},
        onCloseClicked = {},
        editInteractions = object : ListContract {
            override fun onRetry() {}
            override fun onLoginClick() {}
            override fun onListClick(id: Int) {}
            override fun onFabClick() {}
            override fun onEditListSheetDismiss() {}
            override fun onSaveEdit(listName: TextFieldValue) {}
            override fun onListNameChanged(listName: TextFieldValue) {}
            override fun onMediaTypeChanged(mediaType: MediaType) {}
            override fun showAddListSheet(mediaType: MediaType) {}
            override fun showEditListSheet(
                listId: String,
                currentName: String,
                mediaType: MediaType
            ) {
            }
        }
    )
}

@Composable
@Preview(name = "Edit Mode")
fun PreviewEditMode() {
    AddEditListBottomSheetContent(
        listUiState = ListUiState(
            listName = TextFieldValue("My Movie List"),
            originalListName = "My Movie List",
            isSheetVisible = true,
            sheetMode = ListSheetMode.EDIT
        ),
        onActionClicked = {},
        onCloseClicked = {},
        editInteractions = object : ListContract {
            override fun onRetry() {}
            override fun onLoginClick() {}
            override fun onListClick(id: Int) {}
            override fun onFabClick() {}
            override fun onEditListSheetDismiss() {}
            override fun onSaveEdit(listName: TextFieldValue) {}
            override fun onListNameChanged(listName: TextFieldValue) {}
            override fun onMediaTypeChanged(mediaType: MediaType) {}
            override fun showAddListSheet(mediaType: MediaType) {}
            override fun showEditListSheet(
                listId: String,
                currentName: String,
                mediaType: MediaType
            ) {
            }
        }
    )
}