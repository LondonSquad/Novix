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
import com.london.presentation.R
import com.london.presentation.feature.list.savedlist.EditAddSheetState
import com.london.presentation.feature.list.savedlist.ListContract
import com.london.presentation.feature.list.savedlist.ListSheetMode
import com.london.presentation.feature.list.savedlist.ListUiState
import com.london.presentation.feature.list.savedlist.ListViewModel
import com.london.presentation.feature.list.savedlist.defaultContractList
import kotlinx.coroutines.launch

@Composable
fun AddEditListBottomSheet(
    modifier: Modifier = Modifier,
    editAddInteractions: ListContract = hiltViewModel<ListViewModel>(),
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
                editAddInteractions.onEditAddListSheetDismiss()
            },
            containerColor = NovixTheme.colors.surface,
            state = sheetState,
        ) {
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .heightIn(max = LocalWindowInfo.current.containerSize.height.dp * 0.35f)
                    .padding(bottom = 24.dp)
            ) {
                AddEditListBottomSheetContent(
                    modifier = modifier,
                    editInteractions = editAddInteractions,
                    editAddSheetState = listUiState.editAddSheetState,
                    onCloseClicked = {
                        if (!listUiState.isLoading) {
                            coroutineScope.launch {
                                sheetState.hide()
                            }.invokeOnCompletion {
                                if (!sheetState.isVisible) {
                                    editAddInteractions.onEditAddListSheetDismiss()
                                }
                            }
                        }
                    },
                    onActionClicked = {
                        editAddInteractions.onSaveEdit(listUiState.editAddSheetState.listName)
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
    editAddSheetState: EditAddSheetState,
) {
    val interactionSourceUserName = remember { MutableInteractionSource() }

    val titleText = when (editAddSheetState.sheetMode) {
        ListSheetMode.ADD -> stringResource(R.string.add_new_list)
        ListSheetMode.EDIT -> stringResource(R.string.edit_list)
    }

    val buttonText = when (editAddSheetState.sheetMode) {
        ListSheetMode.ADD -> stringResource(R.string.add)
        ListSheetMode.EDIT -> stringResource(R.string.save)
    }

    val isButtonEnabled = when (editAddSheetState.sheetMode) {
        ListSheetMode.ADD -> !editAddSheetState.isSheetLoading &&
                editAddSheetState.listName.text.trim().isNotEmpty()

        ListSheetMode.EDIT -> !editAddSheetState.isSheetLoading &&
                editAddSheetState.listName.text.trim().isNotEmpty() &&
                editAddSheetState.listName.text.trim() != editAddSheetState.originalListName
    }

    Column(
        modifier = modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
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
                        enabled = !editAddSheetState.isSheetLoading,
                        onClick = onCloseClicked
                    )
                    .padding(6.dp),
                painter = painterResource(com.london.designsystem.R.drawable.cancel),
                contentDescription = "Close",
                tint = NovixTheme.colors.title
            )
        }

        Text(
            text = stringResource(R.string.list_title),
            style = NovixTheme.typography.title.small,
            color = NovixTheme.colors.title,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = editAddSheetState.listName,
            interactionSource = interactionSourceUserName,
            onValueChange = editInteractions::onListNameChanged,
            leadingIcon = painterResource(R.drawable.ic_edit_list),
            isError = editAddSheetState.errorMessage != null,
            enabled = !editAddSheetState.isSheetLoading
        )

        PrimaryButton(
            text = buttonText,
            hasLabel = true,
            hasIcon = false,
            isLoading = editAddSheetState.isSheetLoading,
            icon = null,
            onClick = onActionClicked,
            enabled = isButtonEnabled,
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

@Composable
@Preview(name = "Add Mode")
fun PreviewAddMode() {
    AddEditListBottomSheetContent(
        editAddSheetState = EditAddSheetState(
            listName = TextFieldValue(""),
            sheetMode = ListSheetMode.ADD
        ),
        onActionClicked = {},
        onCloseClicked = {},
        editInteractions = defaultContractList()
    )
}

@Composable
@Preview(name = "Edit Mode")
fun PreviewEditMode() {
    AddEditListBottomSheetContent(
        editAddSheetState = EditAddSheetState(
            listName = TextFieldValue("My Movie List"),
            originalListName = "My Movie List",
            sheetMode = ListSheetMode.EDIT
        ),
        onActionClicked = {},
        onCloseClicked = {},
        editInteractions = defaultContractList()
    )
}